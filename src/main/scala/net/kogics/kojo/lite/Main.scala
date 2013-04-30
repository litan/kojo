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
import java.awt.Frame
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

import scala.collection.convert.WrapAsScala.propertiesAsScalaMap

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea

import net.kogics.kojo.d3.Canvas3D
import net.kogics.kojo.history.HistoryPanel
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.lite.topc.D3CanvasHolder
import net.kogics.kojo.lite.topc.DrawingCanvasHolder
import net.kogics.kojo.lite.topc.HistoryHolder
import net.kogics.kojo.lite.topc.MathworldHolder
import net.kogics.kojo.lite.topc.OutputWindowHolder
import net.kogics.kojo.lite.topc.ScriptEditorHolder
import net.kogics.kojo.lite.topc.StoryTellerHolder
import net.kogics.kojo.mathworld.GeoGebraCanvas
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
  @volatile var kojoCtx: KojoCtx = _

  def main(args: Array[String]): Unit = {
    System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tc, %3$s] %4$s: %5$s%n")
    System.setSecurityManager(null)
    kojoCtx = new KojoCtx(args.length == 1 && args(0) == "subKojo") // context needs to be created right up front to set user language
    Utils.runInSwingThreadAndWait {
      splash = new SplashScreen()
    }

    setupLogging()
    val Log = Logger.getLogger("Main")
    if (!kojoCtx.subKojo) {
      runMultiInstancehandler()
    }

    Utils.schedule(0.3) {
      UIManager.getInstalledLookAndFeels.find { _.getName == "Nimbus" }.foreach { nim =>
        UIManager.setLookAndFeel(nim.getClassName)
      }

      val spriteCanvas = new SpriteCanvas(kojoCtx)
      val Tw = new TurtleWorldAPI(spriteCanvas.turtle0)
      val TSCanvas = new DrawingCanvasAPI(spriteCanvas)
      val Staging = new staging.API(spriteCanvas)

      val storyTeller = new StoryTeller(kojoCtx)
      val ggbCanvas = new GeoGebraCanvas(kojoCtx)
      val canvas3d = new Canvas3D()
      val d3API = new d3.API(kojoCtx, canvas3d)

      val mp3player = new KMp3(kojoCtx)
      val fuguePlayer = new FuguePlayer(kojoCtx)

      var scriptEditor: ScriptEditor = null
      execSupport = new CodeExecutionSupport(
        TSCanvas,
        Tw,
        Staging,
        ggbCanvas.Mw,
        d3API,
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
      scriptEditor = new ScriptEditor(execSupport, frame)
      codePane = scriptEditor.codePane
      execSupport.initPhase2(scriptEditor)

      val titleKey = if (kojoCtx.subKojo) "S_Kojo_Scratchpad" else "S_Kojo"
      frame = new JFrame(Utils.loadString(titleKey))
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
      val mwHolder = new MathworldHolder(ggbCanvas, kojoCtx)
      val d3Holder = new D3CanvasHolder(canvas3d, kojoCtx)
      val historyHolder = new HistoryHolder(new HistoryPanel(execSupport))

      kojoCtx.topcs = TopCs(drawingCanvasH, outputPaneH, scriptEditorH, storyHolder, mwHolder, d3Holder, historyHolder)
      kojoCtx.switchToDefaultPerspective()

      frame.setJMenuBar(menuBar)

      splash.close()

      frame.addWindowListener(new WindowAdapter {
        override def windowClosing(e: WindowEvent) {
          appExit()
        }
      })
      frame.setExtendedState(Frame.MAXIMIZED_BOTH)
      frame.pack()

      //      The following (basically setBounds) triggers the menu offset bug on Gnome 3 
      //      val screenSize = Toolkit.getDefaultToolkit.getScreenSize
      //      frame.setBounds(50, 50, screenSize.width - 100, screenSize.height - 100)

      frame.setVisible(true)

      if (!kojoCtx.subKojo && args.length == 1) {
        loadAndRunUrl(args(0), true)
      }
      else {
        Utils.schedule(1) { scriptEditorH.activate() }
        Utils.schedule(2) { scriptEditorH.activate() }
      }
    }

    // Do startup logging after scheduling the GUI stuff
    Log.info(s"Kojo version: ${Versions.KojoMajorVersion}, ${Versions.KojoVersion}")
    Log.info(s"Java version: ${Versions.JavaVersion}. Scala version: ${Versions.ScalaVersion}")
    val sysProps =
      System.getProperties.toList.sorted.foldLeft(new StringBuilder) { case (sb, kv) => sb append s"\n${kv._1} = ${kv._2}" }
    Log.info(s"System Properties:${sysProps}\n\n")
  }

  def drawingCanvasHolder = kojoCtx.topcs.dch
  def scriptEditorHolder = kojoCtx.topcs.seh
  def outputPaneHolder = kojoCtx.topcs.owh

  def appExit() {
    try {
      scriptEditorHolder.se.closing()
      frame.dispose()
      System.exit(0)
    }
    catch {
      case rte: RuntimeException =>
    }
  }

  def setupLogging() {
    val userHome = System.getProperty("user.home")
    val logDir = new File(s"$userHome/.kojo/lite/log/")
    if (!logDir.exists()) {
      logDir.mkdirs()
    }
    val logPath = List(userHome, ".kojo", "lite", "log", "kojo0.log").mkString(File.separator)
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

  def runMultiInstancehandler() {
    MultiInstanceHandler.run()
  }
}
