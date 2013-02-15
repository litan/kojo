package net.kogics.kojo
package lite

import java.awt.Frame
import java.awt.GridLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter
import javax.swing.JFrame
import javax.swing.JPanel
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
import net.kogics.kojo.util.Utils
import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.theme.ThemeMap
import net.kogics.kojo.turtle.TurtleWorldAPI

object Main extends AppMenu with ScriptLoader { main =>
  @volatile var codePane: RSyntaxTextArea = _
  @volatile var scriptEditorH: ScriptEditorHolder = _
  @volatile var frame: JFrame = _
  @volatile var splash: SplashScreen = _
  @volatile var codeSupport: CodeExecutionSupport = _
  @volatile var kojoCtx: KojoCtx = _

  def main(args: Array[String]): Unit = {
    System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tc, %3$s] %4$s: %5$s%n")
    System.setSecurityManager(null)
    kojoCtx = new KojoCtx // context needs to be created right up front to set user language
    Utils.kojoCtx = kojoCtx
    Utils.runInSwingThreadAndWait {
      splash = new SplashScreen()
    }

    setupLogging()
    val Log = Logger.getLogger("Main")

    runMultiInstancehandler()

    Utils.schedule(0.3) {
      import javax.swing.UIManager
      // using println here clobbers output redirection in CodeExecutionSupport
      // System.out.println("Desktop Session is: " + System.getenv("DESKTOP_SESSION"))
      val xx = UIManager.getInstalledLookAndFeels.find { _.getName == "Nimbus" }.foreach { nim =>
        UIManager.setLookAndFeel(nim.getClassName)
      }

      frame = new JFrame(Utils.loadString("S_Kojo"))
      frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
      val control = new CControl(frame)
      val themes = control.getThemes()
      themes.select(ThemeMap.KEY_ECLIPSE_THEME)
      frame.setLayout(new GridLayout(1, 1))
      frame.add(control.getContentArea)

      val spriteCanvas = new SpriteCanvas(kojoCtx)
      kojoCtx.canvasListener = spriteCanvas.megaListener
      val Tw = new TurtleWorldAPI(spriteCanvas.turtle0)
      val TSCanvas = new DrawingCanvasAPI(spriteCanvas)
      val Staging = new staging.API(spriteCanvas)
      kojoCtx.stagingAPI = Staging

      val storyTeller = new StoryTeller(kojoCtx)
      val ggbCanvas = new GeoGebraCanvas(kojoCtx)
      val canvas3d = new Canvas3D()
      val d3API = new d3.API(kojoCtx, canvas3d)

      val mp3player = new KMp3 {
        val kojoCtx = main.kojoCtx
      }
      val fuguePlayer = new FuguePlayer {
        val kojoCtx = main.kojoCtx
      }

      var scriptEditor: ScriptEditor = null
      codeSupport = new CodeExecutionSupport(
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

      scriptEditor = new ScriptEditor(codeSupport, frame)
      codePane = scriptEditor.codePane
      codeSupport.initPhase2(scriptEditor)

      kojoCtx.frame = frame
      kojoCtx.codeSupport = codeSupport
      kojoCtx.control = control
      kojoCtx.storyTeller = storyTeller

      val drawingCanvasH = new DrawingCanvasHolder(spriteCanvas, kojoCtx)
      scriptEditorH = new ScriptEditorHolder(scriptEditor)
      val outputHolder = new OutputWindowHolder(codeSupport.outputPane)
      val storyHolder = new StoryTellerHolder(storyTeller)
      val mwHolder = new MathworldHolder(ggbCanvas, kojoCtx)
      val d3Holder = new D3CanvasHolder(canvas3d, kojoCtx)
      val historyHolder = new HistoryHolder(new HistoryPanel(codeSupport))

      kojoCtx.topcs = TopCs(drawingCanvasH, outputHolder, scriptEditorH, storyHolder, mwHolder, d3Holder, historyHolder)
      kojoCtx.switchToDefaultPerspective()

      frame.setJMenuBar(menuBar)

      splash.close()

      frame.addWindowListener(new WindowAdapter {
        override def windowClosing(e: WindowEvent) {
          appExit()
        }
      })
      frame.setIconImage(Utils.loadImage("/images/kojo48.png"))
      frame.setExtendedState(Frame.MAXIMIZED_BOTH)
      frame.pack()

      //      The following (basically setBounds) triggers the menu offset bug on Gnome 3 
      //      val screenSize = Toolkit.getDefaultToolkit.getScreenSize
      //      frame.setBounds(50, 50, screenSize.width - 100, screenSize.height - 100)

      frame.setVisible(true)

      if (args.length == 1) {
        loadAndRunUrl(args(0), true)
      }
      else {
        Utils.schedule(1) { scriptEditorH.activate() }
        Utils.schedule(2) { scriptEditorH.activate() }
      }
    }

    // Do startup logging after scheduling the GUI stuff
    Log.info(s"Kojo version: ${Versions.KojoVersion}")
    Log.info(s"Java version: ${Versions.JavaVersion}. Scala version: ${Versions.ScalaVersion}")
    val sysProps =
      System.getProperties.toList.sorted.foldLeft(new StringBuilder) { case (sb, kv) => sb append s"\n${kv._1} = ${kv._2}" }
    Log.info(s"System Properties:${sysProps}\n\n")
  }
  
  def appExit() {
    try {
      codeSupport.closing()
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
    System.err.println(s"Logging has been redirected to: $logPath")
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
