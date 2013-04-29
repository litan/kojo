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

import java.awt.Color
import java.awt.EventQueue
import java.awt.Font
import java.awt.Image
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FilenameFilter
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.io.StringWriter
import java.net.InetAddress
import java.net.URL
import java.util.ResourceBundle
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

import javax.swing.ImageIcon
import javax.swing.Timer

import scala.actors.Actor.actor
import scala.actors.Actor.loop
import scala.actors.Actor.react
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import scala.collection.mutable.SynchronizedSet

import net.kogics.kojo.core.CodingMode
import net.kogics.kojo.core.D3Mode
import net.kogics.kojo.core.KojoCtx
import net.kogics.kojo.core.MwMode
import net.kogics.kojo.core.StagingMode
import net.kogics.kojo.core.TwMode

import Typeclasses.mkIdentity
import edu.umd.cs.piccolo.nodes.PText

object Utils {
  lazy val imageCache = new HashMap[String, Image]
  lazy val iconCache = new HashMap[String, ImageIcon]

  def loadImage(fname: String): Image = {
    val url = getClass.getResource(fname)
    if (url != null) {
      Toolkit.getDefaultToolkit.createImage(url)
    }
    else {
      val pfname = if (fname.startsWith("~")) fname.replaceFirst("~", homeDir.replaceAllLiterally("\\", "/")) else fname
      val imageFile = new File(pfname)
      require(imageFile.exists, "Image file should exist: " + imageFile.getAbsolutePath)
      Toolkit.getDefaultToolkit.createImage(pfname)
    }
  }

  def loadImageC(fname: String): Image = {
    imageCache.getOrElseUpdate(fname, loadImage(fname))
  }

  def loadIcon(fname: String): ImageIcon = {
    new ImageIcon(loadImage(fname))
  }

  def loadIconC(fname: String): ImageIcon = {
    iconCache.getOrElseUpdate(fname, loadIcon(fname))
  }

  def loadResource(res: String): String = {
    readStream(getClass.getResourceAsStream(res))
  }

  def loadResource2(res: String): Option[String] = {
    val stream = getClass.getResourceAsStream(res)
    if (stream == null) None else Some(readStream(stream))
  }

  val RmiRegistryPort = 27468
  def localHostString = localHost.getHostAddress
  def localHost = InetAddress.getByName(null)

  def inSwingThread = EventQueue.isDispatchThread

  def runAsync(fn: => Unit) {
    new Thread(new Runnable {
      def run {
        fn
      }
    }).start
  }

  import collection.mutable.{ HashSet, SynchronizedSet }
  lazy val threads = new HashSet[Thread] with SynchronizedSet[Thread]
  var kojoCtx: KojoCtx = _
  lazy val listener = kojoCtx.activityListener
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

  def runLaterInSwingThread(fn: => Unit) {
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

  def runInSwingThreadAndPause[T](fn: => T): T = runInSwingThreadAndWait(1500, "Potential Deadlock. Bailing out!")(fn)

  def runInSwingThreadAndWait[T](timeout: Long, msg: String)(fn: => T): T = {
    if (inSwingThread) {
      fn
    }
    else {
      var t: T = null.asInstanceOf[T]
      val latch = new CountDownLatch(1)
      javax.swing.SwingUtilities.invokeLater(new Runnable {
        override def run {
          t = fn
          latch.countDown()
        }
      })
      val timedOut = !latch.await(timeout, TimeUnit.MILLISECONDS)
      if (timedOut) {
        throw new RuntimeException(msg)
      }
      else {
        t
      }
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

  def scheduleRecN(n: Int, secs: Double)(f: => Unit): Timer = {
    @volatile var count = 0
    lazy val t: Timer = new Timer((secs * 1000).toInt, new ActionListener {
      def actionPerformed(e: ActionEvent) {
        count += 1
        if (count == n) {
          t.stop()
        }
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

  def installDir = System.getProperty("user.home")
  def homeDir = System.getProperty("user.home")
  def currentDir = System.getProperty("user.dir")

  def readStream(is: InputStream): String = {
    require(is != null, "resource should exist")
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

  def readFileIntoMem(f: File) {
    val fis = new FileInputStream(f)
    val bs = new BufferedInputStream(fis)
    val buf = new Array[Byte](1024)
    var nbytes = bs.read(buf)
    while (nbytes != -1) {
      nbytes = bs.read(buf)
    }
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

  lazy val messages = ResourceBundle.getBundle("net.kogics.kojo.lite.Bundle")
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

  lazy val userDir = System.getProperty("user.home")
  lazy val libDir = userDir + File.separatorChar + ".kojo/lite/libk"
  lazy val initScriptDir = userDir + File.separatorChar + ".kojo/lite/initk"

  lazy val libJars: List[String] = filesInDir(libDir, "jar")
  lazy val initScripts: List[String] = filesInDir(initScriptDir, "kojo")
  lazy val installLibJars: List[String] = Nil
  lazy val installInitScripts: List[String] = Nil

  def modeFilter(scripts: List[String], mode: CodingMode): List[String] = mode match {
    case TwMode =>
      scripts.filter { f => !(f.endsWith(".st.kojo") || f.endsWith(".mw.kojo") || f.endsWith(".d3.kojo")) }
    case StagingMode =>
      scripts.filter { f => !(f.endsWith(".tw.kojo") || f.endsWith(".mw.kojo") || f.endsWith(".d3.kojo")) }
    case MwMode =>
      scripts.filter { f => !(f.endsWith(".tw.kojo") || f.endsWith(".st.kojo") || f.endsWith(".d3.kojo")) }
    case D3Mode =>
      scripts.filter { f => !(f.endsWith(".tw.kojo") || f.endsWith(".st.kojo") || f.endsWith(".mw.kojo")) }
  }

  import Typeclasses._

  def langInit(mode: CodingMode): Option[String] = {
    loadResource2(s"/i18n/initk/${System.getProperty("user.language")}.${mode.code}.kojo")
  }

  lazy val initCode = collection.mutable.Map[CodingMode, Option[String]]()

  def kojoInitCode(mode: CodingMode): Option[String] =
    initCode.getOrElseUpdate(
      mode,
      (codeFromScripts(modeFilter(initScripts, mode), initScriptDir) |+| langInit(mode)) map stripCR
    )

  def isScalaTestAvailable = (libJars ++ installLibJars).exists { fname => fname.toLowerCase contains "scalatest" }

  lazy val scalaTestHelperCode = """
  import org.scalatest.FunSuite
  import org.scalatest.matchers.ShouldMatchers

  class TestRun extends FunSuite {
      override def suiteName = "test-run"
      def register(name: String)(fn: => Unit) = test(name)(fn)
      def registerIgnored(name: String)(fn: => Unit) = ignore(name)(fn)
  }

  def test(name: String)(fn: => Unit) {
      val suite = new TestRun()
      suite.register(name)(fn)
      suite.execute()
  }

  def ignore(name: String)(fn: => Unit) {
      val suite = new TestRun()
      suite.registerIgnored(name)(fn)
      suite.execute()
  }

  import ShouldMatchers._
"""

  def codeFromScripts(scripts: List[String], scriptDir: String): Option[String] = scripts match {
    case Nil => None
    case files => Some(
      files.map { file =>
        "// File: %s\n%s\n" format (file, readStream(new FileInputStream(scriptDir + File.separatorChar + file)))
      }.mkString("\n")
    )
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
  
  def trect(h: Double, w: Double, t: core.Turtle) {
    import t._
    for (i <- 1 to 2) {
      forward(h)
      right()
      forward(w)
      right()
    }
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

  def safeProcessSilent(fn: => Unit) {
    try {
      fn
    }
    catch {
      case t: Throwable => // ignore
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

  private def rgbaComps(color: Color) = (color.getRed, color.getGreen, color.getBlue, color.getAlpha())

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

  def hsbColor(h: Float, s: Float, b: Float, a: Int) = {
    val newrgb = Color.HSBtoRGB(h, s, b)
    new Color((newrgb & 0x00ffffff) | (a << 24), true)
  }

  def hueMod(c: Color, f: Double) = {
    val (r, g, b, a) = rgbaComps(c)
    val hsb = Color.RGBtoHSB(r, g, b, null)
    val h = modHsb(hsb(0), f).toFloat
    hsbColor(h, hsb(1), hsb(2), a)
  }

  def satMod(c: Color, f: Double) = {
    val (r, g, b, a) = rgbaComps(c)
    val hsb = Color.RGBtoHSB(r, g, b, null)
    val s = modHsb(hsb(1), f).toFloat
    hsbColor(hsb(0), s, hsb(2), a)
  }

  def britMod(c: Color, f: Double) = {
    val (r, g, b, a) = rgbaComps(c)
    val hsb = Color.RGBtoHSB(r, g, b, null)
    val br = modHsb(hsb(2), f).toFloat
    hsbColor(hsb(0), hsb(1), br, a)
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
  lazy val asyncRunner = actor {
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
