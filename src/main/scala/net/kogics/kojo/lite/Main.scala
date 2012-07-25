package net.kogics.kojo
package lite

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import bibliothek.gui.dock.common.DefaultSingleCDockable
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.UIManager
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.lite.topc.DrawingCanvasHolder
import net.kogics.kojo.lite.topc.OutputWindowHolder
import net.kogics.kojo.lite.topc.ScriptEditorHolder
import net.kogics.kojo.story.StoryTeller
import net.kogics.kojo.CodeExecutionSupport
import util.Utils
import java.awt.TextArea
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.BoxLayout
import javax.swing.JTextArea
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import net.kogics.kojo.lite.topc.StoryTellerHolder
import java.awt.Frame
import net.kogics.kojo.mathworld.GeoGebraCanvas
import net.kogics.kojo.lite.topc.MathworldHolder
import javax.swing.JTextField
import javax.swing.JLabel
import net.kogics.kojo.xscala.Builtins
import javax.jnlp.ServiceManager
import javax.jnlp.SingleInstanceListener
import javax.jnlp.SingleInstanceService
import java.awt.event.WindowListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

object Main {

  @volatile var codePane: RSyntaxTextArea = _
  @volatile var frame: JFrame = _

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
          println("Shutting down")
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
          codePane.setText(code)
          codePane.setCaretPosition(0)
          postfn
        }

      } catch {
        case t: Throwable => codePane.append("// Problem loading code: %s" format (t.getMessage))
      }
    }
  }

  def loadUrl(url: String) = _loadUrl(url) {}

  def loadAndRunUrl(url: String) = _loadUrl(url) {
    codePane.insert("// Running code (loaded from %s).\n// Please wait for a few seconds ...\n\n" format (url), 0)
    Builtins.instance.stClickRunButton
  }

  def realMain(args: Array[String]): Unit = {
    //    System.setSecurityManager(null)
    Utils.runInSwingThread {
      import javax.swing.UIManager

      val xx = UIManager.getInstalledLookAndFeels.find { _.getName == "Nimbus" }.foreach { nim =>
        UIManager.setLookAndFeel(nim.getClassName)
      }

      frame = new JFrame("Kojo Lite")
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      val control = new CControl(frame)
      frame.setLayout(new GridLayout(1, 1))
      frame.add(control.getContentArea)

      val ctx = new KojoCtx()
      SpriteCanvas.initedInstance(ctx)
      StoryTeller.initedInstance(ctx)
      GeoGebraCanvas.initedInstance(ctx)

      codePane = new RSyntaxTextArea(20, 60)
      codePane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA)
      codePane.setCodeFoldingEnabled(true)
      codePane.setAntiAliasingEnabled(true)
      val sp = new RTextScrollPane(codePane)
      sp.setFoldIndicatorEnabled(true);

      val codeSupport = CodeExecutionSupport.initedInstance(codePane, ctx)

      val drawingCanvasH = new DrawingCanvasHolder(SpriteCanvas.instance)

      val scriptEditor = new JPanel()
      scriptEditor.setLayout(new BorderLayout)
      scriptEditor.add(codeSupport.toolbar, BorderLayout.NORTH)
      scriptEditor.add(sp, BorderLayout.CENTER)
      scriptEditor.add(codeSupport.statusStrip, BorderLayout.EAST)
      val scriptEditorH = new ScriptEditorHolder(scriptEditor)
      codeSupport.toolbar.setOpaque(true)
      codeSupport.toolbar.setBackground(new Color(230, 230, 230))

      val outputHolder = new OutputWindowHolder(codeSupport.outputWindow)

      val storyHolder = new StoryTellerHolder(StoryTeller.instance)
      val mwHolder = new MathworldHolder(GeoGebraCanvas.instance)

      ctx.topcs = TopCs(drawingCanvasH, outputHolder, scriptEditorH, storyHolder, mwHolder)

      val grid = new CGrid(control)
      grid.add(1, 0, 4, 2, mwHolder)
      grid.add(1, 0, 4, 2, drawingCanvasH)
      grid.add(1, 2, 2.5, 1, scriptEditorH)
      grid.add(3.5, 2, 1.5, 1, outputHolder)
      grid.add(0, 0, 1, 4, storyHolder)
      control.getContentArea.deploy(grid)

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
      fileMenu.add(openWeb)
      menuBar.add(fileMenu)

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
      samplesMenu.add(menuItemFor("Kojo Overview", "kojo-overview.kojo"))
      samplesMenu.add(menuItemFor("Scala Tutorial", "scala-tutorial.kojo"))
      samplesMenu.add(menuItemFor("Spiral Hexangonal Tiles", "spiral-hexagon-tiles.kojo"))
      menuBar.add(samplesMenu)

      val helpMenu = new JMenu("Help")
      menuBar.add(helpMenu)
      val about = new JMenuItem("About")
      about.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent) {
          val aboutBox = new JDialog
          val aboutPanel = new JPanel
          aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS))
          val aboutText = new JTextArea
          aboutText.setText("""
              |                        KojoLite
              |
              | Copyright © 2009-2012 Lalit Pant (pant.lalit@gmail.com) 
              |                     And the Kojo Dev Team
              |
              |                      www.kogics.net/kojo
              """.stripMargin)
          aboutText.setEditable(false)
          //          aboutText.setBackground(Color.white)
          aboutPanel.add(aboutText)
          val ok = new JButton("Ok")
          ok.addActionListener(new ActionListener {
            def actionPerformed(ev: ActionEvent) {
              aboutBox.setVisible(false)
            }
          })
          aboutPanel.add(ok)

          aboutBox.setModal(true)
          aboutBox.getRootPane.setDefaultButton(ok)
          aboutBox.getContentPane.add(aboutPanel)
          aboutBox.setBounds(300, 300, 450, 300)
          aboutBox.pack
          aboutBox.setVisible(true)
        }
      })
      helpMenu.add(about)
      frame.setJMenuBar(menuBar)

      frame.setBounds(200, 200, 600, 500)
      frame.pack()
      frame.setVisible(true)
      frame.setExtendedState(Frame.MAXIMIZED_BOTH)

      if (args.length == 1) {
        loadAndRunUrl(args(0))
      }
    }
  }
}
