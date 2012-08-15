package net.kogics.kojo
package lite

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.Dimension
import java.awt.Frame
import java.awt.GridLayout
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import bibliothek.gui.dock.common.mode.ExtendedMode
import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import javax.jnlp.ServiceManager
import javax.jnlp.SingleInstanceListener
import javax.jnlp.SingleInstanceService
import javax.swing.text.html.HTMLEditorKit
import javax.swing.Action
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.UIManager
import javax.swing.WindowConstants
import net.kogics.kojo.action.CloseFile
import net.kogics.kojo.action.LoadFrom
import net.kogics.kojo.action.NewFile
import net.kogics.kojo.action.Save
import net.kogics.kojo.action.SaveAs
import net.kogics.kojo.d3.Canvas3D
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.lite.topc.D3CanvasHolder
import net.kogics.kojo.lite.topc.DrawingCanvasHolder
import net.kogics.kojo.lite.topc.MathworldHolder
import net.kogics.kojo.lite.topc.OutputWindowHolder
import net.kogics.kojo.lite.topc.ScriptEditorHolder
import net.kogics.kojo.lite.topc.StoryTellerHolder
import net.kogics.kojo.mathworld.GeoGebraCanvas
import net.kogics.kojo.story.StoryTeller
import net.kogics.kojo.xscala.Builtins
import util.Utils
import javax.swing.AbstractAction
import net.kogics.kojo.lite.topc.HistoryHolder

object Main {

  @volatile var codePane: RSyntaxTextArea = _
  @volatile var scriptEditorH: ScriptEditorHolder = _
  @volatile var frame: JFrame = _
  @volatile var splash: SplashScreen = _

  def main(args: Array[String]): Unit = {
    realMain(args)
    if (System.getProperty("ide.run") != "true") {
      val sis = ServiceManager.lookup("javax.jnlp.SingleInstanceService").asInstanceOf[SingleInstanceService]
      val sisl = new SingleInstanceListener {
        def newActivation(params: Array[String]) {
          Utils.runInSwingThread {
            frame.toFront()
            loadAndRunUrl(params(0))
          }
        }
      }
      sis.addSingleInstanceListener(sisl)
      Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
        def run() {
          sis.removeSingleInstanceListener(sisl)
        }
      }))
    }
  }

  def _loadUrl(url: String)(postfn: => Unit = {}) {
    codePane.setText("// Loading code from URL: %s ...\n" format (url))
    Utils.runAsyncMonitored {
      try {
        val code = Utils.readUrl(url)
        Utils.runInSwingThread {
          codePane.setText(Utils.stripCR(code))
          codePane.setCaretPosition(0)
          postfn
        }

      } catch {
        case t: Throwable => codePane.append("// Problem loading code: %s" format (t.getMessage))
      }
      scriptEditorH.activate()
    }
  }

  def loadUrl(url: String) = _loadUrl(url) {}

  def loadAndRunUrl(url: String, onStartup: Boolean = false) = _loadUrl(url) {
    if (!url.startsWith("http://www.kogics.net/public/kojolite/samples/") && codePane.getText.toLowerCase.contains("file")) {
      codePane.insert("// Loaded code from URL: %s\n// ** Not running it automatically ** because it references files.\n// Look carefully at the code before running it.\n\n" format (url), 0)
      codePane.setCaretPosition(0)
    } else {
      val msg2 = if (onStartup) "\n// Please wait, this might take a few seconds as Kojo starts up..." else ""
      codePane.insert("// Running code loaded from URL: %s%s\n\n" format (url, msg2), 0)
      codePane.setCaretPosition(0)
      Builtins.instance.stClickRunButton
    }
  }

  def realMain(args: Array[String]): Unit = {
    System.setSecurityManager(null)

    Utils.runInSwingThread {
      splash = new SplashScreen()
    }

    Utils.schedule(0.3) {
      import javax.swing.UIManager

      val xx = UIManager.getInstalledLookAndFeels.find { _.getName == "Nimbus" }.foreach { nim =>
        UIManager.setLookAndFeel(nim.getClassName)
      }

      frame = new JFrame("Kojo  \u2248Ray")
      frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
      val control = new CControl(frame)
      frame.setLayout(new GridLayout(1, 1))
      frame.add(control.getContentArea)

      val ctx = new KojoCtx
      SpriteCanvas.initedInstance(ctx)
      StoryTeller.initedInstance(ctx)
      GeoGebraCanvas.initedInstance(ctx)
      Canvas3D.initedInstance(ctx)

      codePane = new RSyntaxTextArea(5, 80)
      val codeSupport = CodeExecutionSupport.initedInstance(codePane, ctx)
      val drawingCanvasH = new DrawingCanvasHolder(SpriteCanvas.instance, ctx)
      scriptEditorH = new ScriptEditorHolder(new JPanel(), codePane, codeSupport)
      val outputHolder = new OutputWindowHolder(codeSupport.outputWindow)
      val storyHolder = new StoryTellerHolder(StoryTeller.instance)
      val mwHolder = new MathworldHolder(GeoGebraCanvas.instance, ctx)
      val d3Holder = new D3CanvasHolder(Canvas3D.instance)
      val historyHolder = new HistoryHolder(new JPanel(), ctx, codeSupport)

      ctx.topcs = TopCs(drawingCanvasH, outputHolder, scriptEditorH, storyHolder, mwHolder, d3Holder)
      ctx.frame = frame

      val grid = new CGrid(control)
      grid.add(1, 0, 4, 2, d3Holder)
      grid.add(1, 0, 4, 2, mwHolder)
      grid.add(1, 0, 4, 2, drawingCanvasH)
      grid.add(1, 2, 2.5, 1, scriptEditorH)
      grid.add(3.5, 2, 1.5, 1, outputHolder)
      grid.add(0, 0, 2, 4, historyHolder)
      grid.add(0, 0, 1, 4, storyHolder)
      control.getContentArea.deploy(grid)

      historyHolder.setExtendedMode(ExtendedMode.MINIMIZED)
      storyHolder.setExtendedMode(ExtendedMode.MINIMIZED)

      def appExit() {
        try {
          codeSupport.closing()
          frame.dispose()
          System.exit(0)
        } catch {
          case rte: RuntimeException =>
        }
      }

      val menuBar = new JMenuBar

      val fileMenu = new JMenu("File")
      val openWeb = new JMenuItem("Open From Web")
      openWeb.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent) {
          val urlGetter = new JDialog(frame)
          urlGetter.setTitle("Open From Web")

          val urlPanel = new JPanel
          urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.Y_AXIS))

          val url = new JPanel
          url.add(new JLabel("URL: "))
          val urlBox = new JTextField(30)
          url.add(urlBox)
          urlPanel.add(url)

          val okCancel = new JPanel
          val ok = new JButton("Ok")
          ok.addActionListener(new ActionListener {
            def actionPerformed(ev: ActionEvent) {
              urlGetter.setVisible(false)
              loadUrl(urlBox.getText)
            }
          })
          val cancel = new JButton("Cancel")
          cancel.addActionListener(new ActionListener {
            def actionPerformed(ev: ActionEvent) {
              urlGetter.setVisible(false)
            }
          })
          okCancel.add(ok); okCancel.add(cancel)
          urlPanel.add(okCancel)

          urlGetter.setModal(true)
          urlGetter.getRootPane.setDefaultButton(ok)
          urlGetter.getContentPane.add(urlPanel)
          urlGetter.setBounds(300, 300, 450, 300)
          urlGetter.pack
          urlGetter.setVisible(true)
        }
      })

      fileMenu.add(new JMenuItem(new NewFile(ctx)))

      val openFile = new JMenuItem("Open...")
      openFile.addActionListener(new LoadFrom(ctx))
      fileMenu.add(openFile)

      val saveFile = new JMenuItem("Save...")
      saveFile.addActionListener(new Save(ctx))
      fileMenu.add(saveFile)

      val saveAsActionListener = new SaveAs(ctx)
      val saveAsFile = new JMenuItem("Save As...")
      saveAsFile.addActionListener(saveAsActionListener)
      fileMenu.add(saveAsFile)

      fileMenu.add(new JMenuItem(new CloseFile))

      fileMenu.add(openWeb)

      fileMenu.addSeparator()

      fileMenu.add(new JMenuItem(new AbstractAction("Exit") {
        def actionPerformed(e: ActionEvent) {
          appExit()
        }
      }))

      menuBar.add(fileMenu)
      ctx.saveAsActionListener = saveAsActionListener

      def menuItemFor(label: String, file: String) = {
        val item = new JMenuItem(label)
        item.addActionListener(new ActionListener {
          def actionPerformed(ev: ActionEvent) {
            loadAndRunUrl("http://www.kogics.net/public/kojolite/samples/" + file)
          }
        })
        item
      }

      val samplesMenu = new JMenu("Samples")
      samplesMenu.add(menuItemFor("Spiral Hexangonal Tiles", "spiral-hexagon-tiles.kojo"))
      menuBar.add(samplesMenu)

      val helpMenu = new JMenu("Help")
      menuBar.add(helpMenu)

      helpMenu.add(menuItemFor("Kojo Overview", "kojo-overview.kojo"))
      helpMenu.add(menuItemFor("Scala Tutorial", "scala-tutorial.kojo"))
      helpMenu.addSeparator()

      val about = new JMenuItem("About")
      about.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent) {
          val aboutBox = new JDialog
          val aboutPanel = new JPanel
          aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS))

          val kojoIcon = new JLabel();
          kojoIcon.setIcon(Utils.loadIcon("/images/splash.png"))
          kojoIcon.setSize(430, 280);
          aboutPanel.add(kojoIcon)

          val aboutText = new JEditorPane
          aboutText.setEditorKit(new HTMLEditorKit)
          aboutText.setEditable(false)
          aboutText.setText("""<html><body>
<div style\="font-size\: 12pt; font-family\: Verdana, 'Verdana CE',  Arial, 'Arial CE', 'Lucida Grande CE', lucida, 'Helvetica CE', sans-serif; ">
              <strong>Kojo</strong> \u2248Ray (Early Access)<br/>
              <br/>Copyright &copy; 2009-2012 Lalit Pant (pant.lalit@gmail.com) and the Kojo Dev Team.<br/><br/> 
              <strong>Kojo</strong> \u2248Ray is the online version of Kojo (Ray - partly because it comes in through the cloud!).<br/>Please visit <em>http://www.kogics.net/webkojo</em> and <em>http://www.kogics.net/kojo</em> for more information.<br/><br/>
              <strong>Kojo</strong> \u2248Ray Contributors:<ul><li>Lalit Pant</li><li>Peter Lewerin</li><li>Jerzy Redlarski</li><li>(The late) Tanu Nayal</li><li>Phil Bagwell</li><li>Vibha Pant</li><li>Anusha Pant</li><li>Nikhil Pant</li><li>Saurabh Kapoor</li><li>Bj\u00f6rn Regnell</li></ul>
              <strong>Kojo</strong> \u2248Ray is licensed under The GNU General Public License (GPL). The full text of the GPL is available at: http://www.gnu.org/licenses/gpl.html<br/><br/>
              The list of third-party software used by <strong>Kojo</strong> \u2248Ray includes:
              <ul>
              <li>The Scala Programming Language (http://www.scala-lang.org)</li>
              <li>Docking Frames (http://dock.javaforge.com/) for providing multiple, dockable windows</li>
              <li>RSyntaxTextArea (http://fifesoft.com/rsyntaxtextarea/) for Syntax Highlighting and Code Completion within the Script Editor</li>
              <li>Piccolo2D (http://www.piccolo2d.org) for 2D Graphics</li>
              <li>JTS Topology Suite (http://tsusiatsoftware.net/jts/main.html) for Collision Detection</li>
              <li>JFugue (http://www.jfugue.org) for computer generated music</li>
              <li>The H2 Database Engine (http://www.h2database.com) for storing history</li>
              <li>GeoGebra (http://www.geogebra.org) for Interactive Geometry and Algebra</li>
              <li>HttpUnit (http://httpunit.sourceforge.net/) for HTTP communication</li>
              <li>JLaTeXMath (http://forge.scilab.org/index.php/p/jlatexmath/) to display LaTeX commands</li>
              <li>JLayer (http://www.javazoom.net/javalayer/javalayer.html) to play MP3 files</li>
              <li>The Netbeans Platform (http://www.netbeans.org) for some Script Editor Icons</li>
              </ul>
              </div>
              </body></html>
              """)
          aboutText.setPreferredSize(new Dimension(430, 300))
          aboutText.setMaximumSize(new Dimension(430, 300))
          aboutText.setCaretPosition(0)
          aboutPanel.add(new JScrollPane(aboutText))
          val ok = new JButton("Ok")
          ok.addActionListener(new ActionListener {
            def actionPerformed(ev: ActionEvent) {
              aboutBox.setVisible(false)
            }
          })
          aboutPanel.add(ok)

          aboutBox.setModal(true)
          aboutBox.getContentPane.add(aboutPanel)
          aboutBox.getRootPane.setDefaultButton(ok)
          aboutBox.setSize(430, 600)
          aboutBox.setLocationRelativeTo(null)
          aboutBox.pack
          aboutBox.setVisible(true)
        }
      })
      helpMenu.add(about)
      frame.setJMenuBar(menuBar)

      splash.close()

      frame.setBounds(100, 100, 600, 500)
      frame.addWindowListener(new WindowAdapter {
        override def windowClosing(e: WindowEvent) {
          appExit()
        }
      })
      //      frame.pack()
      frame.setVisible(true)
      frame.setExtendedState(Frame.MAXIMIZED_BOTH)

      if (args.length == 1) {
        loadAndRunUrl(args(0), true)
      } else {
        Utils.schedule(1) {
          scriptEditorH.activate()
        }
      }
    }
  }
}
