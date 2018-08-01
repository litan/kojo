/*
 * Copyright (C) 2014-2018 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 */

import jssc.SerialPortList
import jssc.SerialPort
import jssc.SerialPortEventListener
import jssc.SerialPortEvent
import concurrent.Promise
import concurrent.Future
import concurrent.Await
import concurrent.duration._
import java.nio.ByteBuffer
import java.util.prefs.Preferences
import java.util.Date

clearOutput()
@volatile var serialPort: SerialPort = _
@volatile var bytePromise: Promise[Byte] = _
@volatile var intPromise: Promise[Int] = _
@volatile var longPromise: Promise[Long] = _

def writeArray(arr: Array[Byte]) {
    debugMsg(s"Arduino <- ${arr.toList}")
    arr.foreach { b =>
        var written = false
        do {
            written = serialPort.writeByte(b)
        } while (written == false)
    }
}

val MaxArduinoInt = math.pow(2, 16).toInt
val MaxArduinoByte = math.pow(2, 8).toInt

// write out an arduino unsigned int
val intArray = new Array[Byte](2)
def writeInt(i: Int) {
    require(i < MaxArduinoInt, s"writeInt(n) - an Arduino unsigned int has to be less than: $MaxArduinoInt")
    intArray(0) = (i & 0x00FF).toByte
    intArray(1) = (i >> 8).toByte
    writeArray(intArray)
}

def writeString(s: String) {
    val stringArray = s.getBytes("US-ASCII")
    writeArray(stringArray)
}

def awaitResult[T](f: Future[T]): T = {
    Await.result(f, 5.seconds)
}

val debug = false
def debugMsg(msg: => String) {
    if (debug) {
        println(msg)
    }
}

class SerialPortReader extends SerialPortEventListener {
    //    var currPacket: ByteBuffer = _
    @volatile var currData = ByteBuffer.allocate(0)
    @volatile var state = 1 // new packet
    @volatile var packetSize = 0
    @volatile var bytesAvailable = 0

    def readByte: Byte = {
        currData.get
    }

    def readInt: Int = {
        val lowByte: Int = readByte & 0x00FF
        val hiByte: Int = readByte & 0x00FF
        hiByte << 8 | lowByte
    }

    def readLong: Long = {
        val lowByte: Long = readByte & 0x00FF
        val hiByte: Long = readByte & 0x00FF
        val hi2Byte: Long = readByte & 0x00FF
        val hi3Byte: Long = readByte & 0x00FF
        hi3Byte << 24 | hi2Byte << 16 | hiByte << 8 | lowByte
    }

    def readString: String = {
        val buf = new Array[Byte](packetSize - 3)
        currData.get(buf)
        new String(buf)
    }

    def serialEvent(event: SerialPortEvent) = synchronized {
        if (event.isRXCHAR && event.getEventValue > 0) { //If data is available
            val data = serialPort.readBytes(event.getEventValue)
            debugMsg(s"Arduino -> ${data.toList}")
            if (currData.hasRemaining) {
                val combinedData = ByteBuffer.allocate(currData.remaining + data.length)
                combinedData.put(currData)
                combinedData.put(data)
                currData = combinedData
                currData.flip()
            }
            else {
                currData = ByteBuffer.wrap(data)
            }
            handleData0()
        }
    }

    def handleData0() {
        state match {
            case 1 =>
                packetSize = currData.get
                bytesAvailable = currData.limit - currData.position
                state = 2
                handleData()
            case 2 =>
                bytesAvailable = currData.limit - currData.position
                handleData()
        }
    }

    def handleData() {
        def drainBytes(n: Int, msg: String) {
            repeat(n) {
                readByte
            }
            println(msg)
        }
        debugMsg(s"  Bytes available: $bytesAvailable, Curr packet size: $packetSize")
        if (bytesAvailable >= packetSize) {
            readByte match {
                case 1 => // byte
                    if (bytePromise != null && !bytePromise.isCompleted) {
                        readByte; readByte
                        val d = readByte
                        bytePromise.success(d)
                    }
                    else {
                        drainBytes(packetSize - 1, "Unexpected Byte received.")
                    }
                case 2 => // int
                    if (intPromise != null && !intPromise.isCompleted) {
                        readByte; readByte
                        val d = readInt
                        intPromise.success(d)
                    }
                    else {
                        drainBytes(packetSize - 1, "Unexpected Int received.")
                    }
                case 3 => // string
                    readByte; readByte
                    val msg = readString
                    println(s"[Arduino-Log] $msg")
                case 4 => // long
                    if (longPromise != null && !longPromise.isCompleted) {
                        readByte; readByte
                        val d = readLong
                        longPromise.success(d)
                    }
                    else {
                        drainBytes(packetSize - 1, "Unexpected Long received.")
                    }
                case _ => // unknown
                    drainBytes(packetSize - 1, "Unknown data received.")
            }
            packetDone()
        }
    }

    def packetDone() {
        state = 1
        if (currData.hasRemaining) {
            handleData0()
        }
    }
}

import language.implicitConversions
implicit def i2b(i: Int) = i.toByte

runInBackground {
    def connect(portName: String) {
        serialPort = new SerialPort(portName)
        println(s"Opening port: $portName (and resetting Arduino board)...")
        serialPort.openPort()
        serialPort.setParams(SerialPort.BAUDRATE_115200,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE
        )
        serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
        serialPort.addEventListener(new SerialPortReader())
    }

    def ping(): Boolean = {
        val command = Array[Byte](2, 0, 1)
        intPromise = Promise()
        try {
            writeArray(command)
            val ret = awaitResult(intPromise.future)
            if (ret == 0xF0F0) {
                true
            }
            else {
                println("Saw (maybe) old response on the line. Reading ping response again...")
                // maybe old packet on the line; try one more time
                intPromise = Promise()
                val ret = awaitResult(intPromise.future)
                if (ret == 0xF0F0) true else false
            }
        }
        catch {
            case e: Exception =>
                false
        }
    }

    def connectAndCheck(portName: String): Boolean = {
        try {
            connect(portName)
            debugMsg("Connection done; pinging soon...")
            pause(2)
            debugMsg("Pinging...")
            val good = ping()
            if (!good) {
                serialPort.closePort()
            }
            good
        }
        catch {
            case _: Throwable => false
        }
    }

    var arduinoPort: Option[String] = None
    val prefs = builtins.kojoCtx.asInstanceOf[net.kogics.kojo.lite.KojoCtx].prefs
    val knownPort = prefs.get("arduino.port", null)
    if (knownPort != null) {
        println(s"Last successful connection to: $knownPort")
        try {
            val good = connectAndCheck(knownPort)
            if (good) {
                arduinoPort = Some(knownPort)
            }
        }
        catch {
            case t: Throwable =>
                println(s"Problem connecting to last used port: ${t.getMessage}\n")
        }
    }
    if (!arduinoPort.isDefined) {
        val names = SerialPortList.getPortNames
        println(s"Available Ports: ${names.toList}")
        arduinoPort = names.find { portName =>
            val good = connectAndCheck(portName)
            if (good) {
                prefs.put("arduino.port", portName)
            }
            else {
                println(s"Port does not have the Kojo-Arduino bridge running at the other end: $portName")
            }
            good
        }
    }
    if (!arduinoPort.isDefined) {
        throw new RuntimeException("Unable to find an Arduino port with the Kojo-Arduino bridge running at the other end.")
    }

    def done() {
        println("")
        println(s"Closing port: ${arduinoPort.get}")
        println(s"Stopped at: ${new Date}")
        println("--")
        try {
            if (serialPort.isOpened()) {
                serialPort.purgePort(SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXCLEAR)
                serialPort.closePort()
            }
        }
        catch {
            case _: Throwable =>
        }
    }

    println(s"Started at: ${new Date}")
    println("--")
    try {
        setup()
        repeatWhile(true) {
            // thread is interrupted when stop button is pressed
            loop()
        }
    }
    finally {
        done()
    }
}

// API

def pinMode(pin: Byte, mode: Byte) {
    // INPUT - 0; OUTPUT - 1
    val command = Array[Byte](4, 1, 1, pin, mode)
    //                        sz,ns,cmd,arg1,arg2
    writeArray(command)
}

def digitalWrite(pin: Byte, value: Byte) {
    // LOW - 0; HIGH - 1
    val command = Array[Byte](4, 1, 2, pin, value)
    //                        sz,ns,cmd,arg1,arg2
    writeArray(command)
}

def digitalRead(pin: Byte): Byte = {
    val command = Array[Byte](3, 1, 3, pin)
    //                        sz,ns,cmd,arg1
    bytePromise = Promise()
    writeArray(command)
    awaitResult(bytePromise.future)
}

def analogWrite(pin: Byte, value: Int) {
    require(value < MaxArduinoByte, s"analogWrite() - value has to be less than: $MaxArduinoByte")
    val command = Array[Byte](4, 1, 7, pin, value.toByte)
    //                        sz,ns,cmd,arg1,arg2
    writeArray(command)
}

def analogRead(pin: Byte): Int = {
    val command = Array[Byte](3, 1, 4, pin)
    //                        sz,ns,cmd, arg1
    intPromise = Promise()
    writeArray(command)
    awaitResult(intPromise.future)
}

def tone(pin: Byte, freq: Int) {
    writeArray(Array[Byte](5, 1, 5, pin))
    writeInt(freq)
}

def tone(pin: Byte, freq: Int, duration: Int) {
    writeArray(Array[Byte](7, 1, 8, pin))
    writeInt(freq)
    writeInt(duration)
}

def noTone(pin: Byte) {
    writeArray(Array[Byte](3, 1, 6, pin))
}

object Servo {
    // proxy for servo library
    // namespace (ns) = 2
    def attach(pin: Byte) {
        val command = Array[Byte](3, 2, 1, pin)
        //                        sz,ns,cmd,arg1
        writeArray(command)
    }

    def write(angle: Int) {
        val command = Array[Byte](3, 2, 2, angle.toByte)
        //                        sz,ns,cmd,arg1
        writeArray(command)
    }
}

object SoftSerial {
    // proxy for Softserial library
    // namespace (ns) = 3

    def begin(baud: Int) {
        val command = Array[Byte](4, 3, 1)
        //                        sz,ns,cmd
        writeArray(command)
        writeInt(baud)
    }

    def available(): Int = {
        val command = Array[Byte](2, 3, 2)
        //                        sz,ns,cmd
        intPromise = Promise()
        writeArray(command)
        awaitResult(intPromise.future)
    }

    def read(): Int = {
        val command = Array[Byte](2, 3, 3)
        //                        sz,ns,cmd
        intPromise = Promise()
        writeArray(command)
        awaitResult(intPromise.future)
    }

    def println(s: String): Int = {
        val len = s.length
        val command = Array[Byte](2 + 2 + len, 3, 4)
        //                        sz,ns,cmd
        intPromise = Promise()
        writeArray(command)
        writeInt(len)
        writeString(s)
        awaitResult(intPromise.future)
    }
}

object UltraSonic {
    // proxy for ultrasonic sensor
    // namespace (ns) = 4

    def init(triggerPin: Byte, echoPin: Byte) {
        val command = Array[Byte](4, 4, 1, triggerPin, echoPin)
        //                        sz,ns,cmd,arg1, arg2
        writeArray(command)
    }
    
    def pingMicroSecs(): Long = {
        val command = Array[Byte](2, 4, 2)
        //                        sz,ns,cmd
        longPromise = Promise()
        writeArray(command)
        awaitResult(longPromise.future)
    }
}



val INPUT, LOW = 0.toByte
val OUTPUT, HIGH = 1.toByte

def delay(n: Long) = Thread.sleep(n)
def millis = epochTimeMillis

// uncomment setup and loop methods below to compile/run this file standalone
//def setup() {
//}
//
//def loop() {
//}
