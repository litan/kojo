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

object Main {

  def main(args: Array[String]): Unit = {
    System.setSecurityManager(null)
    Utils.runInSwingThread {
      import javax.swing.UIManager

      val xx = UIManager.getInstalledLookAndFeels.find { _.getName == "Nimbus" }.foreach { nim =>
        UIManager.setLookAndFeel(nim.getClassName)
      }

      val frame = new JFrame("Kojo Lite")
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      val control = new CControl(frame)
      frame.setLayout(new GridLayout(1, 1))
      frame.add(control.getContentArea)

      val menuBar = new JMenuBar
      menuBar.add(new JMenu("Samples"))
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
              |                        Kojo Lite
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
          aboutBox.getContentPane.add(aboutPanel)
          aboutBox.setBounds(300, 300, 450, 300)
          aboutBox.pack
          aboutBox.setVisible(true)
        }
      })
      helpMenu.add(about)
      frame.setJMenuBar(menuBar)

      val ctx = new KojoCtx()
      SpriteCanvas.initedInstance(ctx)
      StoryTeller.initedInstance(ctx)
      GeoGebraCanvas.initedInstance(ctx)

      val textArea = new RSyntaxTextArea(20, 60)
      textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA)
      textArea.setCodeFoldingEnabled(true)
      textArea.setAntiAliasingEnabled(true)
      val sp = new RTextScrollPane(textArea)
      sp.setFoldIndicatorEnabled(true);

      val codeSupport = CodeExecutionSupport.initedInstance(textArea)

      val drawingCanvasH = new DrawingCanvasHolder(SpriteCanvas.instance)

      val scriptEditor = new JPanel()
      scriptEditor.setLayout(new BorderLayout)
      scriptEditor.add(codeSupport.toolbar, BorderLayout.NORTH)
      scriptEditor.add(sp, BorderLayout.CENTER)
      val scriptEditorH = new ScriptEditorHolder(scriptEditor)
      codeSupport.toolbar.setOpaque(true)
      codeSupport.toolbar.setBackground(new Color(230, 230, 230))

      val outputHolder = new OutputWindowHolder(new JScrollPane(codeSupport.outputWindow))
      
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

      frame.setVisible(true)
      frame.setExtendedState(Frame.MAXIMIZED_BOTH)
    }
  }

  def create(title: String, color: Color) = {
    val panel = new JPanel()
    panel.setOpaque(true)
    panel.setBackground(color)
    new DefaultSingleCDockable(title, title, panel)
  }
}
