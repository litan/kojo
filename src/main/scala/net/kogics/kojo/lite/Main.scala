/*
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
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
package lite

import java.awt.BorderLayout
import java.awt.Font
import java.awt.Frame
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

import javax.swing.JFrame
import javax.swing.UIManager
import javax.swing.WindowConstants

import scala.jdk.CollectionConverters._

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea

import net.kogics.kojo.history.HistoryPanel
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.lite.topc.ArithAerobicsHolder
import net.kogics.kojo.lite.topc.DrawingCanvasHolder
import net.kogics.kojo.lite.topc.HistoryHolder
import net.kogics.kojo.lite.topc.OutputWindowHolder
import net.kogics.kojo.lite.topc.ScriptEditorHolder
import net.kogics.kojo.lite.topc.StoryTellerHolder
import net.kogics.kojo.music.FuguePlayer
import net.kogics.kojo.music.KMp3
import net.kogics.kojo.story.StoryTeller
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.util.Utils

import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.theme.ThemeMap

object Main extends AppMenu with ScriptLoader { main =>
  @volatile var codePane: RSyntaxTextArea = _
  @volatile var frame: JFrame = _
  @volatile var splash: SplashScreen = _
  @volatile var execSupport: CodeExecutionSupport = _
  @volatile implicit var kojoCtx: KojoCtx = _

  def main(args: Array[String]): Unit = {
    // snapshot of system props for logging at the end of main
    val sysProps = System.getProperties.asScala.toBuffer

    System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tc, %3$s] %4$s: %5$s%6$s%n")
    // app name needs to be set right in the beginning (this applies to Mac; is ignored elsewhere) 
    System.setProperty("apple.awt.application.name", "Kojo")
    kojoCtx = new KojoCtx(args.length == 1 && args(0) == "subKojo") // context needs to be created right up front to set user language
    if (Utils.isMac) {
      try {
        new MacTweaks().tweak(frame)
      }
      catch {
        case _: Throwable =>
      }
    }

    Utils.runInSwingThreadAndWait {
      splash = new SplashScreen()
    }

    setupLogging()
    val Log = Logger.getLogger("Main")
    if (!kojoCtx.subKojo) {
      runMultiInstancehandler()
    }

    Utils.schedule(0.3) {
      updateDefaultFonts(12 + kojoCtx.screenDpiFontDelta)
      Theme.currentTheme.loadLookAndFeel()
      kojoCtx.lookAndFeelReady()

      val spriteCanvas = new SpriteCanvas(kojoCtx)
      val Tw = new TurtleWorldAPI(spriteCanvas.turtle0)
      val TSCanvas = new DrawingCanvasAPI(spriteCanvas)
      val Staging = new staging.API(spriteCanvas)

      val storyTeller = new StoryTeller(kojoCtx)
      //      val canvas3d = new Canvas3D()
      //      val d3API = new d3.API(kojoCtx, canvas3d)

      val mp3player = new KMp3(kojoCtx)
      val fuguePlayer = new FuguePlayer(kojoCtx)

      var scriptEditor: ScriptEditor = null
      execSupport = new CodeExecutionSupport(
        TSCanvas,
        Tw,
        Staging,
        storyTeller,
        mp3player,
        fuguePlayer,
        spriteCanvas,
        scriptEditor, // lazy
        kojoCtx
      )

      kojoCtx.execSupport = execSupport
      kojoCtx.storyTeller = storyTeller
      val statusBar = new StatusBar
      kojoCtx.statusBar = statusBar

      val titleKey = if (kojoCtx.subKojo) "S_Kojo_Scratchpad" else "S_Kojo"
      frame = new JFrame(Utils.loadString(titleKey))

      scriptEditor = new ScriptEditor(execSupport, frame)
      codePane = scriptEditor.codePane
      execSupport.initPhase2(scriptEditor)

      frame.setIconImage(Utils.loadImage("/images/kojo48.png"))
      frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
      val control = new CControl(frame)
      val themes = control.getThemes()
      themes.select(ThemeMap.KEY_ECLIPSE_THEME)
      frame.setLayout(new BorderLayout)
      frame.add(control.getContentArea, BorderLayout.CENTER)
      frame.add(statusBar, BorderLayout.SOUTH)

      kojoCtx.frame = frame
      kojoCtx.control = control

      val drawingCanvasH = new DrawingCanvasHolder(spriteCanvas, kojoCtx)
      val scriptEditorH = new ScriptEditorHolder(scriptEditor)
      val outputPaneH = new OutputWindowHolder(execSupport.outputPane)
      val storyHolder = new StoryTellerHolder(storyTeller)
      //      val d3Holder = new D3CanvasHolder(canvas3d, kojoCtx)
      val historyHolder = new HistoryHolder(new HistoryPanel(execSupport))
      val arithAerobicsHolder = new ArithAerobicsHolder(new ArithAerobicsPane(frame, kojoCtx))

      kojoCtx.topcs = TopCs(drawingCanvasH, outputPaneH, scriptEditorH, storyHolder, historyHolder, arithAerobicsHolder)
      Theme.currentTheme.loadDefaultPerspective(kojoCtx)

      frame.setJMenuBar(menuBar)

      splash.close()

      frame.addWindowListener(new WindowAdapter {
        override def windowClosing(e: WindowEvent): Unit = {
          appExit()
        }
      })

      frame.pack()
      if (!Utils.isLinux) {
        val screenSize = Toolkit.getDefaultToolkit.getScreenSize
        // The following triggers the menu offset bug on Gnome 3
        frame.setBounds(50, 50, screenSize.width - 100, screenSize.height - 100)
      }
      frame.setExtendedState(Frame.MAXIMIZED_BOTH)
      frame.setVisible(true)

      if (!kojoCtx.subKojo && args.length == 1) {
        loadAndRunUrl(args(0), true)
      }
      else {
        frame.toFront()
        Utils.schedule(1) { scriptEditorH.activate() }
        Utils.schedule(2) { scriptEditorH.activate() }
      }
    }

    // Do startup logging after scheduling the GUI stuff
    Log.info(s"Kojo version: ${Versions.KojoMajorVersion}, ${Versions.KojoVersion}")
    Log.info(s"Java version: ${Versions.JavaVersion}. Scala version: ${Versions.ScalaVersion}")
    val sortedSysProps = sysProps.sorted.foldLeft(new StringBuilder) {
        case (sb, kv) =>
          sb append s"\n${kv._1} = ${kv._2}"
      }
    Log.info(s"System Properties:$sortedSysProps\n\n")
  }

  def drawingCanvasHolder = kojoCtx.topcs.dch
  def scriptEditorHolder = kojoCtx.topcs.seh
  def outputPaneHolder = kojoCtx.topcs.owh

  def appExit(): Unit = {
    try {
      scriptEditorHolder.se.closing()
      frame.dispose()
      System.exit(0)
    }
    catch {
      case rte: RuntimeException =>
    }
  }

  def setupLogging(): Unit = {
    val logDir = Utils.locateLogDir()
    val logPath = new File(logDir, "kojo0.log").getPath
    if (kojoCtx.subKojo) {
      System.err.println(s"[INFO] Kojo Scratchpad logging has been redirected to: $logPath.n")
    }
    else {
      System.err.println(s"[INFO] Logging has been redirected to: $logPath")
    }
    val rootLogger = Logger.getLogger("")
    val logHandler = new FileHandler("%h/.kojo/lite/log/kojo%g.log", 1 * 1024 * 1024, 6, false)
    logHandler.setFormatter(new SimpleFormatter())
    logHandler.setLevel(Level.INFO)
    rootLogger.removeHandler(rootLogger.getHandlers()(0))
    rootLogger.setLevel(Level.INFO)
    rootLogger.addHandler(logHandler)
  }

  def runMultiInstancehandler(): Unit = {
    MultiInstanceManager.run()
  }

  private def updateDefaultFonts(size: Int): Unit = {
    // not needed with FlatLAF
  }

}
