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

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.awt.Color
import java.awt.EventQueue
import java.awt.Font
import java.awt.Image
import java.awt.Toolkit
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
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger
import java.util.LinkedList
import java.util.Locale
import java.util.Properties
import java.util.ResourceBundle
import javax.imageio.ImageIO
import javax.swing.text.JTextComponent
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.KeyStroke
import javax.swing.Timer

import scala.collection.mutable
import scala.collection.mutable.HashMap

import akka.actor.ActorSystem
import edu.umd.cs.piccolo.event.PInputEvent
import net.kogics.kojo.core.CodingMode
import net.kogics.kojo.core.KojoCtx
import net.kogics.kojo.core.TwMode
import net.kogics.kojo.core.VanillaMode
import net.kogics.kojo.util.RichFile.enrichFile

object Utils {
  lazy val Log = Logger.getLogger("Utils")
  lazy val imageCache = new HashMap[String, BufferedImage]
  lazy val iconCache = new HashMap[String, ImageIcon]
  val GuiTimeout = 15000

  def absolutePath(fname0: String): String = {
    def expandHomeDir(fname: String): String =
      if (fname.startsWith("~")) fname.replaceFirst("~", homeDir.replace("\\", "/")) else fname

    val fname = expandHomeDir(fname0)
    val f = new java.io.File(fname)
    val path = if (f.isAbsolute) f.getAbsolutePath else kojoCtx.baseDir + fname
    path.replace("\\", "/")
  }

  def loadBufImage(fname: String): BufferedImage = {
    val url = getClass.getResource(fname)
    if (url != null) {
      ImageIO.read(url)
    }
    else {
      val pfname = absolutePath(fname)
      val imageFile = new File(pfname)
      require(imageFile.exists, "Image file should exist: " + imageFile.getAbsolutePath)
      ImageIO.read(imageFile)
    }
  }

  def loadImage(fname: String): Image = {
    val url = getClass.getResource(fname)
    if (url != null) {
      Toolkit.getDefaultToolkit.createImage(url)
    }
    else {
      val pfname = absolutePath(fname)
      val imageFile = new File(pfname)
      require(imageFile.exists, "Image file should exist: " + imageFile.getAbsolutePath)
      Toolkit.getDefaultToolkit.createImage(pfname)
    }
  }

  def loadUrlImage(url: URL): BufferedImage = {
    ImageIO.read(url)
  }

  def loadImageC(fname: String): Image = {
    imageCache.getOrElseUpdate(fname, loadBufImage(fname))
  }

  def loadUrlImageC(url: URL): BufferedImage = {
    imageCache.getOrElseUpdate(url.toExternalForm, loadUrlImage(url))
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

  /** Returns the content of the given file in the local variant selected by Locale.getDefault(). At first tries to find
    * a resource of the given file name in the subdirectory LL of the given root directory, if the current default
    * Locale has language LL selected. If this resource is not found, then tries to find the given file directly in the
    * named root directory.
    * @param root
    *   directory path inside the classpath. Should begin and end in '/', as "/samples".
    * @param file
    *   file name with extension in the given root directory, as "tree0.kojo".
    * @throws IllegalArgumentException
    *   the named file is found neither in the local variant for the default Locale, nor in the base variant.
    */
  def loadLocalizedResource(root: String, file: String): String = {
    val locale = Locale.getDefault
    val langCode = locale.getLanguage
    val myClass = getClass
    val localName = root + langCode + "/" + file
    val localStream = myClass.getResourceAsStream(localName)
    val baseName = root + file
    val stream = if (localStream == null) {
      myClass.getResourceAsStream(baseName)
    }
    else {
      localStream
    }
    if (stream == null) {
      throw new IllegalArgumentException(s"Resource $localName or $baseName should exist.")
    }
    readStream(stream)
  }

  def loadResource2(res: String): Option[String] = {
    val stream = getClass.getResourceAsStream(res)
    if (stream == null) None else Some(readStream(stream))
  }

  def loadInstalledFile(fileName: String): String = {
    val f = new File(installDir, fileName)
    import RichFile._
    if (f.exists()) {
      kojoCtx.setLastLoadStoreDir(f.getParent)
      f.readAsString
    }
    else {
      val f2 = new File(installDir + File.separatorChar + "installer", fileName)
      if (f2.exists()) {
        kojoCtx.setLastLoadStoreDir(f2.getParent)
        f2.readAsString
      }
      else {
        throw new RuntimeException(s"Unable to find installed file: $fileName")
      }
    }
  }

  def loadLocalizedInstalledFile(fileName: String): String = {
    def stripInstalled(fileName: String) = fileName.split(".installed")(0)

    def fileNameWithCode(fileName0: String, code: String) = {
      val fileName = stripInstalled(fileName0)
      val fileParts = fileName.split("/")
      val file = fileParts.last.split("\\.").head
      val localizedFile = s"${file}_$code.kojo.installed"
      fileParts(fileParts.length - 1) = localizedFile
      fileParts.mkString("/")
    }

    val langCode = Locale.getDefault.getLanguage
    val localizedFileName = fileNameWithCode(fileName, langCode)
    try {
      loadInstalledFile(localizedFileName)
    }
    catch {
      case _: Throwable =>
        loadInstalledFile(fileName)
    }
  }

  val RmiRegistryPort = 27468
  val RmiHandlerName = "MultiInstanceHandler"

  def localHostString = localHost.getHostAddress
  def localHost = InetAddress.getByName(null)

  def inSwingThread = EventQueue.isDispatchThread

  def runAsync(fn: => Unit): Unit = {
    import scala.util.control.NonFatal
    new Thread(new Runnable {
      def run: Unit = {
        try {
          fn
        }
        catch {
          case NonFatal(e) =>
            Log.log(Level.WARNING, "Problem on async runner thread", e)
        }
      }
    }).start
  }

  lazy val threads = ConcurrentHashMap.newKeySet[Thread]()
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

  def runAsyncMonitored(fn: => Unit): Unit = {
    lazy val t: Thread = new Thread(new Runnable {
      def run: Unit = {
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

  def stopMonitoredThreads(): Unit = {
    threads.forEach { t => t.interrupt() }
    threads.clear()
  }

  def noMonitoredThreads = threads.isEmpty

  def runLaterInSwingThread(fn: => Unit): Unit = {
    javax.swing.SwingUtilities.invokeLater(new Runnable {
      override def run: Unit = {
        fn
      }
    })
  }

  // a version of runInSwingThread - which can be useful if batching is not needed
  def runInSwingThreadNonBatched(fn: => Unit): Unit = {
    if (EventQueue.isDispatchThread) {
      fn
    }
    else {
      javax.swing.SwingUtilities.invokeLater(new Runnable {
        override def run: Unit = {
          fn
        }
      })
    }
  }

  val batchLock = new ReentrantLock
  val notFull = batchLock.newCondition
  val Max_Q_Size = 9000
  val batchQ = new LinkedList[() => Unit]

  @volatile var keepProcessingQ = true
  def clearGuiBatchQ(): Unit = {
    keepProcessingQ = false
  }

  // this is the core of Kojo UI/drawing performance - so the code is a little low-level
  def runInSwingThread(fn: => Unit): Unit = {
    if (EventQueue.isDispatchThread) {
      fn
    }
    else {
      if (batchLock.tryLock(GuiTimeout, TimeUnit.MILLISECONDS)) {
        keepProcessingQ = true
        try {
          while (batchQ.size > Max_Q_Size) {
            notFull.await()
          }
          val needDrainer = batchQ.isEmpty
          batchQ.add(() => fn)
          if (needDrainer) {
            javax.swing.SwingUtilities.invokeLater(new Runnable {
              override def run: Unit = {
                batchLock.lock()
                while (!batchQ.isEmpty) {
                  try {
                    if (keepProcessingQ) {
                      batchQ.remove.apply()
                    }
                    else {
                      batchQ.clear()
                    }
                  }
                  catch {
                    case t: Throwable =>
                      Utils.runLaterInSwingThread {
                        reportException(t)
                      }
                  }
                }
                notFull.signal()
                batchLock.unlock()
              }
            })
          }
        }
        finally {
          batchLock.unlock()
        }
      }
      else {
        keepProcessingQ = false
        throw new RuntimeException("Potential Deadlock Or Overload. Bailing out!")
      }
    }
  }

  def runInSwingThreadAndWait[T](fn: => T): T = {
    if (inSwingThread) {
      fn
    }
    else {
      var t: T = null.asInstanceOf[T]
      javax.swing.SwingUtilities.invokeAndWait(new Runnable {
        override def run: Unit = {
          t = fn
        }
      })
      t
    }
  }

  def runInSwingThreadAndPause[T](fn: => T): T =
    runInSwingThreadAndWait(GuiTimeout, "Potential Deadlock. Bailing out!")(fn)

  def runInSwingThreadAndWait[T](timeout: Long, msg: String)(fn: => T): T = {
    if (inSwingThread) {
      fn
    }
    else {
      var t: T = null.asInstanceOf[T]
      val latch = new CountDownLatch(1)
      javax.swing.SwingUtilities.invokeLater(new Runnable {
        override def run: Unit = {
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
    lazy val t: Timer = new Timer(
      (secs * 1000).toInt,
      new ActionListener {
        def actionPerformed(e: ActionEvent): Unit = {
          t.stop
          f
        }
      }
    )
    t.start
    t
  }

  def scheduleRec(secs: Double)(f: => Unit): Timer = {
    val t: Timer = new Timer(
      (secs * 1000).toInt,
      new ActionListener {
        def actionPerformed(e: ActionEvent): Unit = {
          f
        }
      }
    )
    t.start
    t
  }

  def scheduleRecN(n: Int, secs: Double)(f: => Unit): Timer = {
    @volatile var count = 0
    lazy val t: Timer = new Timer(
      (secs * 1000).toInt,
      new ActionListener {
        def actionPerformed(e: ActionEvent): Unit = {
          count += 1
          if (count == n) {
            t.stop()
          }
          f
        }
      }
    )
    t.start
    t
  }

  def replAssertEquals(a: Any, b: Any): Unit = {
    if (a != b) println("Not Good. First: %s, Second: %s".format(a.toString, b.toString))
    else println("Good")
  }

  def installDir = {
    val cp = System.getProperty("java.class.path").split(File.pathSeparator)
    cp.find { e => e.endsWith("rithica.jar") } match {
      case Some(rPath) =>
        val f = new File(rPath)
        f.getParentFile.getParent
      case None =>
        System.getProperty("user.home")
    }
  }
  def homeDir = System.getProperty("user.home")
  def currentDir = System.getProperty("user.dir")
  def isMac = {
    val os = System.getProperty("os.name").toLowerCase()
    os.startsWith("mac")
  }
  def isWin = {
    val os = System.getProperty("os.name").toLowerCase()
    os.startsWith("windows")
  }
  def isLinux = {
    val os = System.getProperty("os.name").toLowerCase()
    os.startsWith("linux")
  }

  def readStream(is: InputStream): String = {
    require(is != null, "resource should exist")
    val reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))
    val buf = new Array[Char](1024)
    val sb = new StringBuffer
    try {
      var nbytes = reader.read(buf)
      while (nbytes != -1) {
        sb.append(buf, 0, nbytes)
        nbytes = reader.read(buf)
      }
    }
    finally {
      reader.close()
    }
    sb.toString
  }

  def readUrl(url: String) = readStream(new URL(url).openConnection().getInputStream)

  def copyFile(in: File, out: File): Unit = {
    val sourceChannel = new FileInputStream(in).getChannel
    val destinationChannel = new FileOutputStream(out).getChannel
    sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel)
    sourceChannel.close();
    destinationChannel.close();
  }

  def readFileIntoMem(f: File): Unit = {
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
  lazy val keyWithStrings = appProperty("i18n.string.showkey") match {
    case Some(value) => java.lang.Boolean.valueOf(value).booleanValue()
    case None        => false
  }
  def stringSuffix(key: String) = {
    if (keyWithStrings) s"[$key]" else ""
  }

  /** Returns the localized String for the given key.
    * @throws NullPointerException
    *   if <code>key</code> is <code>null</code>
    * @throws MissingResourceException
    *   if no object for the given key can be found
    */
  def loadString(key: String) = {
    messages.getString(key).concat(stringSuffix(key))
  }
  def loadString(klass: Class[_], key: String) = {
    messages.getString(key).concat(stringSuffix(key))
  }
  def loadString(klass: Class[_], key: String, args: AnyRef*) = {
    messages.getString(key).format(args: _*).concat(stringSuffix(key))
  }

  // Loads the actual string in the bundle without the debug key suffix
  def loadBundleString(key: String) = {
    messages.getString(key)
  }

  def filesInDir(dir: String, ext: String): List[String] = {
    val osDir = new File(dir)
    if (osDir.exists && osDir.isDirectory) {
      osDir
        .list(new FilenameFilter {
          override def accept(dir: File, name: String) = {
            name.endsWith("." + ext)
          }
        })
        .toList
        .sorted
    }
    else {
      Nil
    }
  }

  def numFilesInDir(dir: String, ext: String): Int = {
    val osDir = new File(dir)
    if (osDir.exists && osDir.isDirectory) {
      osDir
        .list(new FilenameFilter {
          override def accept(dir: File, name: String) = {
            name.endsWith("." + ext)
          }
        })
        .length
    }
    else {
      0
    }
  }

  def dirsInDir(dir: String): List[String] = {
    val osDir = new File(dir)
    if (osDir.exists && osDir.isDirectory) {
      osDir
        .list(new FilenameFilter {
          override def accept(dir: File, name: String) = {
            dir.isDirectory
          }
        })
        .sorted
        .map { subDir => dir + File.separatorChar + subDir }
        .toList
    }
    else {
      Nil
    }
  }

  lazy val userDir = System.getProperty("user.home")
  lazy val libDir = userDir + File.separatorChar + ".kojo/lite/libk"
  lazy val initScriptDir = userDir + File.separatorChar + ".kojo/lite/initk"

  lazy val initScripts: List[String] = filesInDir(initScriptDir, "kojo")
  lazy val installLibJars: List[String] = Nil
  lazy val installInitScripts: List[String] = Nil

  lazy val extensionsDir = userDir + File.separatorChar + ".kojo/extension"

  /** Locates where the log directory should be, creates it if necessary, and returns its File object. */
  def locateLogDir(): File = {
    val logDir = new File(s"$userDir/.kojo/lite/log/")
    if (!logDir.exists()) {
      logDir.mkdirs()
    }
    logDir
  }

  def modeFilter(scripts: List[String], mode: CodingMode): List[String] = mode match {
    case TwMode =>
      scripts.filter { f => !(f.endsWith(".st.kojo") || f.endsWith(".mw.kojo") || f.endsWith(".vn.kojo")) }
    case VanillaMode =>
      scripts.filter { f => !(f.endsWith(".tw.kojo") || f.endsWith(".st.kojo") || f.endsWith(".mw.kojo")) }
  }

  import Typeclasses._

  def langInit(mode: CodingMode): Option[String] = {
    loadResource2(s"/i18n/initk/${System.getProperty("user.language")}.${mode.code}.kojo")
  }

  lazy val initCodeCache = collection.mutable.Map[CodingMode, Option[String]]()

  def initkCode(mode: CodingMode): Option[String] =
    initCodeCache.getOrElseUpdate(
      mode,
      (codeFromScripts(modeFilter(initScripts, mode), initScriptDir) |+| langInit(mode)).map(stripCR)
    )

  def codeFromScripts(scripts: List[String], scriptDir: String): Option[String] = scripts match {
    case Nil => None
    case files =>
      Some(
        files
          .map { file =>
            "// File: %s\n%s\n".format(file, readStream(new FileInputStream(scriptDir + File.separatorChar + file)))
          }
          .mkString("\n")
      )
  }

  def initCode(mode: CodingMode): Option[String] = {
    initkCode(mode)
  }

  lazy val actorSystem = ActorSystem("Kojo")

  lazy val queuedRunner = new AsyncQueuedRunner {}
  def runAsyncQueued(fn: => Unit): Unit = {
    queuedRunner.runAsyncQueued(fn)
  }

  import edu.umd.cs.piccolo.nodes.PText
  def textNode(text: String, x: Double, y: Double, camScale: Double): PText = {
    val tnode = new PText(text)
    tnode.getTransformReference(true).setToScale(1 / camScale, -1 / camScale)
    tnode.setOffset(x, y)
    tnode
  }

  def textNode(
      text: String,
      x: Double,
      y: Double,
      camScale: Double,
      fontSize: Int,
      fontName0: Option[String] = None
  ): PText = {
    val tnode = textNode(text, x, y, camScale)
    val fontName = fontName0 match {
      case Some(name) => name
      case None       => tnode.getFont.getName
    }
    val font = new Font(fontName, Font.PLAIN, fontSize)
    tnode.setFont(font)
    tnode
  }

  def trect(h: Double, w: Double, t: core.Turtle): Unit = {
    import t._
    var i = 0
    while (i < 2) {
      forward(h)
      right()
      forward(w)
      right()
      i += 1
    }
  }

  def reportException(t: Throwable): Unit = {
    println(s"Problem - ${t.toString} (see log for details)")
    Log.log(Level.SEVERE, "Problem", t)
  }

  def safeProcess(fn: => Unit): Unit = {
    try {
      fn
    }
    catch {
      case t: Throwable => reportException(t)
    }
  }

  def safeProcessSilent(fn: => Unit): Unit = {
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

  def giveupLock(lock: Lock)(fn: => Unit): Unit = {
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

  def checkHsbModFactor(f: Double): Unit = {
    if (f < -1 || f > 1) {
      throw new IllegalArgumentException("mod factor needs to be between -1 and 1")
    }
  }

  def awtColorToDoodleColor(c: Color): doodle.Color =
    doodle.Color.rgba(c.getRed, c.getGreen, c.getBlue, c.getAlpha)

  def hueMod(c: Color, f: Double): Color = {
    val rc = awtColorToDoodleColor(c)
    rc.spinBy(f).toAwt
  }

  def satMod(c: Color, f: Double): Color = {
    val rc = awtColorToDoodleColor(c)
    rc.saturate(f).toAwt
  }

  def britMod(c: Color, f: Double): Color = lightMod(c, f)

  def lightMod(c: Color, f: Double): Color = {
    val rc = awtColorToDoodleColor(c)
    rc.lighten(f).toAwt
  }

  def stripTrailingChar(s: String, c: Char): String = s.reverse.dropWhile(_ == c).reverse
  def stripTrailingDots(s: String) = stripTrailingChar(s, '.')
  def stripDots(s: String): String = s.filterNot { _ == '.' }

  lazy val (needsSanitizing, decimalSep) = {
    val tester = "%.1f".format(0.0)
    (tester != "0.0", tester(1).toString)
  }

  def sanitizeDoubleString(d: String) = {
    if (needsSanitizing) d.replaceAll(decimalSep, ".") else d
  }

  def getKeyCode(e: PInputEvent): Int = {
    val kc = e.getKeyCode
    if (kc == 0) e.getKeyChar.toUpper.toInt else kc
  }

  lazy val includePragma = loadBundleString("S_IncludePragma")
  lazy val includeRE = ("""//\s*#""" ++ includePragma ++ """.*""").r
  lazy val filenameRE = ("""//\s*#""" ++ includePragma).r
  lazy val basecodeRE = ("""//(\s)*#""" ++ includePragma ++ """(.*)""").r

  def preProcessInclude(code: String): (String, Int, Int) = {
    val included = new mutable.HashSet[String]()

    def _preProcessInclude(code: String): (String, Int, Int) = {
      def countLines(s: String) = s.count(_ == '\n')
      val includes = includeRE.findAllIn(code)
      def getFileName(s: String) = filenameRE.replaceFirstIn(s, "").trim
      def addKojoExtension(fileName: String) = {
        val justFileName = new File(fileName).getName
        if (!justFileName.contains(".")) fileName + ".kojo" else fileName
      }
      def load(fileName0: String): String = {
        val fileNameDotKojo = addKojoExtension(fileName0)
        val fileName = absolutePath(fileNameDotKojo)
        def readFileContent = {
          val file = new File(fileName)
          if (file.exists) {
            stripCR(file.readAsString)
          }
          else {
            val res = loadResource(fileNameDotKojo)
            if (res == null) {
              file.readAsString // trigger exception
            }
            else {
              stripCR(res)
            }
          }
        }
        try {
          if (included.contains(fileName)) {
            ""
          }
          else {
            included.add(fileName)
            val fileContent = readFileContent
            val codeToInclude = s"// #begin-include: $fileName\n$fileContent\n// #end-include: $fileName\n"
            val (result, _, _) = _preProcessInclude(codeToInclude) // non-tail-recursive call
            result
          }
        }
        catch { case e: Throwable => throw new IllegalArgumentException(s"Error when including file: $fileName", e) }
      }

      val addedCode = (for (i <- includes) yield load(getFileName(i))).mkString
      val baseCode = basecodeRE.replaceAllIn(code, "//$1#" ++ includePragma.capitalize ++ "$2")
      (addedCode + baseCode, countLines(addedCode), addedCode.length)
    }

    _preProcessInclude(code)
  }

  def scrollToOffset(offset: Int, comp: JTextComponent): Unit = {
    comp.scrollRectToVisible(comp.modelToView(offset))
  }

  def exceptionMessage(t: Throwable): String = {
    if (t == null) "" else s"${t.getMessage()}; ${exceptionMessage(t.getCause)}"
  }

  def roundDouble(d: Double, n: Int) = {
    if (needsSanitizing)
      s"%.${n}f".format(d).replace(decimalSep, ".").toDouble
    else
      s"%.${n}f".format(d).toDouble
  }

  val kojoProps = new Properties

  val defProps = """
                   |# Uncomment/tweak options as desired:
                   |
                   |# Increase Kojo font size
                   |# font.increase=1
                   |
                   |# Or decrease Kojo font size
                   |# font.increase=-1
                   |
                   |# Use dark UI theme; default is light theme.
                   |# theme=dark
                   |
                   |# Specify max memory for Kojo in MB
                   |# memory.max=768m
                   |
                   |# Or specify max memory for Kojo in GB (1g, 2g, etc)
                   |# memory.max=1g
                   |
                   |# library load path
                   |# library.path=
                   |
                   |# python home (for jep/python bridge)
                   |# python.home=
                   |
                   |# python version (for jep/python bridge)
                   |# python.version=
                   |
                   |# Show internal keys for UI elements; for localization developers
                   |# i18n.string.showkey=true
                   |
                   |# Specify canvas image export options
                   |# export.image.dpi=nn
                   |# export.image.inches=nn or a4, a3, etc.
                   |# export.image.dimension=width or height
                 """.stripMargin

  val propsFile = new File(Utils.userDir + File.separatorChar + ".kojo/lite/kojo.properties")
  if (propsFile.exists()) {
    val is = new FileInputStream(propsFile)
    try {
      kojoProps.load(is)
    }
    catch {
      case t: Throwable =>
    }
    finally {
      is.close()
    }
  }
  else {
    locateLogDir()
    propsFile.createNewFile()
    import RichFile._
    propsFile.write(defProps)
  }

  def appProperty(key: String) = {
    val ret = kojoProps.getProperty(key)
    if (ret != null && ret.length > 0) Some(ret) else None
  }

  def updateAppProperties(props: Map[String, String]): Unit = {
    props.foreach {
      case (key, value) =>
        kojoProps.put(key, value)
    }
    val os = new FileOutputStream(propsFile)
    try {
      kojoProps.store(os, defProps)
    }
    catch {
      case t: Throwable =>
    }
    finally {
      os.close()
    }
  }

  lazy val timerThreadCount = new java.util.concurrent.atomic.AtomicInteger(0)
  def increaseSysTimerResolutionIfNeeded(): Unit = {
    if (isWin) {
      if (timerThreadCount.getAndIncrement == 0) {
        runAsync {
          try {
            // The irregularly long sleep makes the JVM set the OS timer resolution to 1 ms
            Thread.sleep(Int.MaxValue)
          }
          catch {
            case _: Throwable =>
              if (timerThreadCount.decrementAndGet > 0) {
                println("Ignoring system timer resolution increase request.")
              }
            // When the thread terminates, the OS resets the system timer resolution
          }
        }
      }
      else {
        timerThreadCount.getAndDecrement
      }
    }
  }

  def strFormat(format: String, args: Any*): String = {
    String.format(Locale.ROOT, format, args.map(_.asInstanceOf[AnyRef]): _*)
  }

  def closeOnEsc(dlg: JDialog): Unit = {
    dlg.getRootPane.registerKeyboardAction(
      _ => dlg.setVisible(false),
      KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
      JComponent.WHEN_IN_FOCUSED_WINDOW
    )
  }

  lazy val javaMajorVersion = {
    val version = System.getProperty("java.specification.version").split('.')
    val major =
      if (version(0) == "1") version(1) // 1.8 is 8
      else version(0) // later versions are 9, 10, etc
    major.toInt
  }

  def isJava8 = javaMajorVersion == 8
  def isJavaAtLeast(n: Int) = javaMajorVersion >= n

}
