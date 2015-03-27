/*
 * Copyright (C) 2014 Lalit Pant <pant.lalit@gmail.com>
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

def writeArray(arr: Array[Byte]) {
    debugMsg(s"Arduino <- ${arr.toList}")
    arr.foreach { b =>
        var written = false
        do {
            written = serialPort.writeByte(b)
        } while (written == false)
    }
}

// write out an arduino unsigned int
val intArray = new Array[Byte](2)
def writeInt(i: Int) {
    intArray(0) = (i & 0x00FF).toByte
    intArray(1) = (i >> 8).toByte
    writeArray(intArray)
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
    var currData = ByteBuffer.allocate(0)
    var state = 1 // new packet
    var packetSize = 0
    var bytesAvailable = 0

    def readByte: Byte = {
        currData.get
    }

    def readInt: Int = {
        val lowByte: Int = readByte & 0x00FF
        val hiByte: Int = readByte & 0x00FF
        hiByte << 8 | lowByte
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
        debugMsg(s"  Bytes available: $bytesAvailable, Curr packet size: $packetSize")
        if (bytesAvailable >= packetSize) {
            readByte match {
                case 1 => // byte
                    readByte; readByte
                    bytePromise.success(readByte)
                case 2 => // int
                    readByte; readByte
                    intPromise.success(readInt)
                case 3 => // string
                    readByte; readByte
                    val msg = readString
                    println(s"[Arduino-Log] $msg")
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
        serialPort.setParams(SerialPort.BAUDRATE_9600,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE,
            true,
            true)
        serialPort.addEventListener(new SerialPortReader())
    }

    def ping(): Boolean = {
        val command = Array[Byte](2, 0, 1)
        intPromise = Promise()
        try {
            writeArray(command)
            val ret = awaitResult(intPromise.future)
            if (ret == 0xF0F0) true else false
        }
        catch {
            case e: Exception =>
                false
        }
    }

    def connectAndCheck(portName: String): Boolean = {
        println("Connecting to port")
        connect(portName)
        println("Pausing")
        pause(2)
        println("Pinging")
        val good = ping()
        println(s"Ping result: $good")
        if (!good) {
            serialPort.closePort()
        }
        good
    }

    var arduinoPort: Option[String] = None
    val prefs = builtins.kojoCtx.asInstanceOf[net.kogics.kojo.lite.KojoCtx].prefs
//    val knownPort = prefs.get("arduino.port", null)
//    if (knownPort != null) {
//        println(s"Last successful connection to: $knownPort")
//        try {
//            val good = connectAndCheck(knownPort)
//            if (good) {
//                arduinoPort = Some(knownPort)
//            }
//        }
//        catch {
//            case t: Throwable =>
//                println(s"Problem connecting to last used port: ${t.getMessage}\n")
//        }
//    }

    if (!arduinoPort.isDefined) {
        val names = List("/dev/rfcomm1") // SerialPortList.getPortNames
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

    setRefreshRate(1)
    animate {
    }

    onAnimationStop {
        println(s"Closing port: ${arduinoPort.get}")
        println(s"Stopped at: ${new Date}")
        serialPort.closePort()
    }

    println(s"Started at: ${new Date}")
    println("--")
    setup()
    repeatWhile(true) {
        // thread is interrupted when stop button is pressed
        loop()
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

val INPUT, LOW = 0.toByte
val OUTPUT, HIGH = 1.toByte

def delay(n: Int) = Thread.sleep(n)
def millis = epochTimeMillis

// uncomment setup and loop methods below to compile/run this file standalone
//def setup() {
//}
//
//def loop() {
//}
