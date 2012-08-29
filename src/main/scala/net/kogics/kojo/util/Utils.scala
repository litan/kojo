/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo
package util

import lite.canvas.SpriteCanvas
import java.awt.{ List => _, _ }
import java.util.concurrent.locks.Lock
import javax.swing._
import java.awt.event.{ ActionListener, ActionEvent }
import java.io._
import net.kogics.kojo.core.CodingMode
import net.kogics.kojo.core.MwMode
import net.kogics.kojo.core.TwMode
import net.kogics.kojo.lite.canvas.SpriteCanvas
import java.util.ResourceBundle
import java.util.Locale
import java.net.URL

object Utils {

  val imageCache = Map("/images/turtle32.png" -> loadImage0("/images/turtle32.png"))

  def loadImage0(fname: String): Image = {
    val url = getClass.getResource(fname)
    Toolkit.getDefaultToolkit.getImage(url)
  }

  def loadImage(fname: String): Image = {
    imageCache.get(fname).getOrElse { loadImage0(fname) }
  }

  def loadIcon(fname: String, desc: String = ""): ImageIcon = {
    new ImageIcon(loadImage(fname), desc)
  }

  def inSwingThread = EventQueue.isDispatchThread

  def runAsync(fn: => Unit) {
    new Thread(new Runnable {
      def run {
        fn
      }
    }).start
  }

  import collection.mutable.{ HashSet, SynchronizedSet }
  val threads = new HashSet[Thread] with SynchronizedSet[Thread]
  lazy val listener = SpriteCanvas.instance().megaListener // hack!
  var timer: Timer = _
  var startCount = 0

  def startPumpingEvents() = synchronized {
    startCount += 1
    if (startCount == 1) {
      listener.hasPendingCommands()
      timer = Utils.scheduleRec(0.5) {
        listener.hasPendingCommands()
      }
    }
  }

  def stopPumpingEvents() = synchronized {
    startCount -= 1
    if (startCount == 0) {
      timer.stop()
      timer = null
      listener.pendingCommandsDone()
      Utils.schedule(0.5) {
        listener.pendingCommandsDone()
      }
    }
  }

  def runAsyncMonitored(fn: => Unit) {
    lazy val t: Thread = new Thread(new Runnable {
      def run {
        startPumpingEvents()
        try {
          fn
        }
        catch {
          case e: InterruptedException => // println("Background Thread Interrupted.")
          case t: Throwable            => reportException(t)
        }
        finally {
          threads.remove(t)
          stopPumpingEvents()
        }
      }
    })
    threads.add(t)
    t.start()
  }

  def stopMonitoredThreads() {
    threads.foreach { t => t.interrupt() }
    threads.clear()
  }

  def invokeLaterInSwingThread(fn: => Unit) {
    javax.swing.SwingUtilities.invokeLater(new Runnable {
      override def run {
        fn
      }
    })
  }

  def runInSwingThread(fn: => Unit) {
    if (inSwingThread) {
      fn
    }
    else {
      javax.swing.SwingUtilities.invokeLater(new Runnable {
        override def run {
          fn
        }
      })
    }
  }

  def runInSwingThreadAndWait[T](fn: => T): T = {
    if (inSwingThread) {
      fn
    }
    else {
      var t: T = null.asInstanceOf[T]
      javax.swing.SwingUtilities.invokeAndWait(new Runnable {
        override def run {
          t = fn
        }
      })
      t
    }
  }

  def doublesEqual(d1: Double, d2: Double, tol: Double): Boolean = {
    if (d1 == d2) return true
    else if (math.abs(d1 - d2) < tol) return true
    else return false
  }

  def schedule(secs: Double)(f: => Unit): Timer = {
    lazy val t: Timer = new Timer((secs * 1000).toInt, new ActionListener {
      def actionPerformed(e: ActionEvent) {
        t.stop
        f
      }
    })
    t.start
    t
  }

  def scheduleRec(secs: Double)(f: => Unit): Timer = {
    val t: Timer = new Timer((secs * 1000).toInt, new ActionListener {
      def actionPerformed(e: ActionEvent) {
        f
      }
    })
    t.start
    t
  }

  def replAssertEquals(a: Any, b: Any) {
    if (a != b) println("Not Good. First: %s, Second: %s" format (a.toString, b.toString))
    else println("Good")
  }

  // actually - the dir with the jars, one level under the actual install dir
  def installDir = System.getProperty("user.home")

  def readStream(is: InputStream): String = {
    val reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))
    val buf = new Array[Char](1024)
    var nbytes = reader.read(buf)
    val sb = new StringBuffer
    while (nbytes != -1) {
      sb.append(buf, 0, nbytes)
      nbytes = reader.read(buf)
    }
    reader.close()
    sb.toString
  }

  def readUrl(url: String) = readStream(new URL(url).openConnection().getInputStream)

  def copyFile(in: File, out: File) {
    val sourceChannel = new FileInputStream(in).getChannel
    val destinationChannel = new FileOutputStream(out).getChannel
    sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel)
    sourceChannel.close();
    destinationChannel.close();
  }

  def stackTraceAsString(t: Throwable): String = {
    val result = new StringWriter()
    val printWriter = new PrintWriter(result)
    t.printStackTrace(printWriter)
    result.toString()
  }

  def deg2radians(angle: Double) = angle * math.Pi / 180
  def rad2degrees(angle: Double) = angle * 180 / math.Pi

  def stripCR(str: String) = str.replaceAll("\r\n", "\n")

  val messages = ResourceBundle.getBundle("net.kogics.kojo.lite.Bundle")
  def loadString(key: String) = {
    messages.getString(key)
  }
  def loadString(klass: Class[_], key: String) = {
    messages.getString(key)
  }
  def loadString(klass: Class[_], key: String, args: AnyRef*) = {
    messages.getString(key) format (args: _*)
  }

  def filesInDir(dir: String, ext: String): List[String] = {
    val osDir = new File(dir)
    if (osDir.exists) {
      osDir.list(new FilenameFilter {
        override def accept(dir: File, name: String) = {
          name.endsWith("." + ext)
        }
      }).toList.sorted
    }
    else {
      Nil
    }
  }

  def runAsyncQueued(fn: => Unit) {
    asyncRunner ! RunCode { () =>
      fn
    }
  }

  import edu.umd.cs.piccolo.nodes.PText
  def textNode(text: String, x: Double, y: Double, camScale: Double): PText = {
    val tnode = new PText(text)
    tnode.getTransformReference(true).setToScale(1 / camScale, -1 / camScale)
    tnode.setOffset(x, y)
    tnode
  }

  def textNode(text: String, x: Double, y: Double, camScale: Double, n: Int): PText = {
    val tnode = textNode(text, x, y, camScale)
    val font = new Font(tnode.getFont.getName, Font.PLAIN, n)
    tnode.setFont(font)
    tnode
  }

  def reportException(t: Throwable) {
    println("Problem - " + t.getMessage)
  }

  def safeProcess(fn: => Unit) {
    try {
      fn
    }
    catch {
      case t: Throwable => reportException(t)
    }
  }

  def withLock[T](lock: Lock)(fn: => T): T = {
    lock.lock()
    try {
      fn
    }
    finally {
      lock.unlock()
    }
  }

  def giveupLock(lock: Lock)(fn: => Unit) {
    lock.unlock()
    try {
      fn
    }
    catch {
      case t: Throwable => // log this?
    }
    finally {
      lock.lock()
    }
  }

  private def rgbComps(color: Color) = (color.getRed, color.getGreen, color.getBlue)

  def checkHsbModFactor(f: Double) {
    if (f < -1 || f > 1) {
      throw new IllegalArgumentException("mod factor needs to be between -1 and 1")
    }
  }

  private def modHsb(q: Double, f: Double) = {
    checkHsbModFactor(f)

    if (f > 0) {
      q * (1 - f) + f
    }
    else {
      q * (1 + f)
    }
  }

  def hueMod(c: Color, f: Double) = {
    val (r, g, b) = rgbComps(c)
    val hsb = Color.RGBtoHSB(r, g, b, null)
    val h = modHsb(hsb(0), f).toFloat
    Color.getHSBColor(h, hsb(1), hsb(2))
  }

  def satMod(c: Color, f: Double) = {
    val (r, g, b) = rgbComps(c)
    val hsb = Color.RGBtoHSB(r, g, b, null)
    val s = modHsb(hsb(1), f).toFloat
    Color.getHSBColor(hsb(0), s, hsb(2))
  }

  def britMod(c: Color, f: Double) = {
    val (r, g, b) = rgbComps(c)
    val hsb = Color.RGBtoHSB(r, g, b, null)
    val br = modHsb(hsb(2), f).toFloat
    Color.getHSBColor(hsb(0), hsb(1), br)
  }

  def stripTrailingChar(s: String, c: Char): String = s.reverse.dropWhile(_ == c).reverse
  def stripTrailingDots(s: String) = stripTrailingChar(s, '.')
  def stripDots(s: String): String = s.filterNot { _ == '.' }

  lazy val (needsSanitizing, decimalSep) = {
    val tester = "%.1f" format (0.0)
    (tester != "0.0", tester(1).toString)
  }

  def sanitizeDoubleString(d: String) = {
    if (needsSanitizing) d.replaceAll(decimalSep, ".") else d
  }

  case class RunCode(code: () => Unit)
  import scala.actors._
  import scala.actors.Actor._
  val asyncRunner = actor {
    loop {
      react {
        case RunCode(code) =>
          safeProcess {
            code()
          }
      }
    }
  }
}
