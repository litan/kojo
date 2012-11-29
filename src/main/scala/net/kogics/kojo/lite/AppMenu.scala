package net.kogics.kojo.lite

import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.AbstractAction
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JEditorPane
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.text.html.HTMLEditorKit

import net.kogics.kojo.action.CloseFile
import net.kogics.kojo.action.LoadFrom
import net.kogics.kojo.action.NewFile
import net.kogics.kojo.action.Save
import net.kogics.kojo.action.SaveAs
import net.kogics.kojo.util.Utils

trait AppMenu { self: Main.type =>
  def menuBar = {
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

    fileMenu.add(new JMenuItem(new NewFile(kojoCtx)))

    val openFile = new JMenuItem("Open...")
    openFile.addActionListener(new LoadFrom(kojoCtx))
    fileMenu.add(openFile)

    val saveFile = new JMenuItem("Save...")
    saveFile.addActionListener(new Save(kojoCtx))
    fileMenu.add(saveFile)

    val saveAsActionListener = new SaveAs(kojoCtx)
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
    kojoCtx.saveAsActionListener = saveAsActionListener

    def menuItemFor(label: String, file: String) = {
      val item = new JMenuItem(label)
      item.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent) {
          loadAndRunResource("/samples/" + file)
        }
      })
      item
    }

    def menuItemForUrl(label: String, url: String) = {
      val item = new JMenuItem(label)
      item.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent) {
          loadAndRunUrl(url)
        }
      })
      item
    }
    val samplesMenu = new JMenu("Samples")
    val simpleMenu = new JMenu("Getting Started")
    simpleMenu.add(menuItemFor("Square", "square.kojo"))
    simpleMenu.add(menuItemFor("Colors and Shapes", "shapes-cols.kojo"))
    simpleMenu.add(menuItemFor("Circles", "circles.kojo"))
    samplesMenu.add(simpleMenu)

    val drawingsMenu = new JMenu("Drawings")
    drawingsMenu.add(menuItemFor("Square Pattern", "square-pattern.kojo"))
    drawingsMenu.add(menuItemFor("Pentagon Pattern", "penta-pattern.kojo"))
    drawingsMenu.add(menuItemFor("Ferris Wheel", "ferris-wheel.kojo"))
    drawingsMenu.add(menuItemFor("Rangoli", "rangoli.kojo"))
    samplesMenu.add(drawingsMenu)

    val mgeomMenu = new JMenu("Math and Geometry")
    mgeomMenu.add(menuItemForUrl("Solving Linear Equations", "http://www.kogics.net/public/kojolite/samples/solving-linear-equations.kojo"))
    mgeomMenu.add(menuItemFor("Spiral Square Tiles", "spiral-square-tiles.kojo"))
    mgeomMenu.add(menuItemFor("Spiral Hexangonal Tiles", "spiral-hexagon-tiles.kojo"))
    samplesMenu.add(mgeomMenu)

    val fractalsMenu = new JMenu("Fractals")
    fractalsMenu.add(menuItemFor("Tree", "tree0.kojo"))
    fractalsMenu.add(menuItemFor("Another Tree", "tree1.kojo"))
    fractalsMenu.add(menuItemFor("Fibonacci Tree", "fib-tree.kojo"))
    fractalsMenu.add(menuItemFor("Snowflake", "snowflake.kojo"))
    fractalsMenu.add(menuItemFor("Sierpinski Triangle", "sierpinski-tri.kojo"))
    samplesMenu.add(fractalsMenu)
    
    val animGameMenu = new JMenu("Animations and Games")
    animGameMenu.add(menuItemFor("Tangram Skier", "tangram-skier.kojo"))
    animGameMenu.add(menuItemFor("Hunted", "hunted.kojo"))
    samplesMenu.add(animGameMenu)

    
    menuBar.add(samplesMenu)

    val helpMenu = new JMenu("Help")
    menuBar.add(helpMenu)

    helpMenu.add(menuItemFor("Kojo Overview", "kojo-overview.kojo"))
    helpMenu.add(menuItemFor("Scala Tutorial", "scala-tutorial.kojo"))
    helpMenu.add(menuItemFor("Introduction to 3D", "d3-intro.kojo"))
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
              <strong>Kojo</strong> 2.0 (Early Access)<br/>
              Version: 291112-1 <br/>
              <em>Java version: %s. Scala version: %s</em> <br/><br/>
              Copyright &copy; 2009-2012 Lalit Pant (pant.lalit@gmail.com) and the Kojo Dev Team.<br/><br/>
              Please visit <em>http://www.kogics.net/kojo</em> for more information about Kojo.<br/><br/>
              <strong>Kojo</strong> 2.0 Contributors:<ul><li>Lalit Pant</li><li>Peter Lewerin</li><li>Jerzy Redlarski</li><li>Bj\u00f6rn Regnell</li><li>Tanu Nayal</li><li>Phil Bagwell</li><li>Vibha Pant</li><li>Anusha Pant</li><li>Nikhil Pant</li><li>Saurabh Kapoor</li><li>Mushtaq Ahmed</li></ul>
              <strong>Kojo</strong> 2.0 is licensed under The GNU General Public License (GPL). The full text of the GPL is available at: http://www.gnu.org/licenses/gpl.html<br/><br/>
              The list of third-party software used by <strong>Kojo</strong> 2.0 includes:
              <ul>
              <li>The Scala Programming Language (http://www.scala-lang.org)</li>
              <li>Docking Frames (http://dock.javaforge.com/) for providing multiple, dockable windows</li>
              <li>RSyntaxTextArea (http://fifesoft.com/rsyntaxtextarea/) for Syntax Highlighting and Code Completion within the Script Editor</li>
              <li>Scalariform (https://github.com/mdr/scalariform/) for Code Formatting within the Script Editor</li>
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
              """ format (geogebra.main.AppD.getJavaVersion, scala.tools.nsc.Properties.versionString.substring("version ".length))
        )
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
        // does not work because there's a textpane in the panel. request focus below
        // aboutBox.getRootPane.setDefaultButton(ok)
        aboutBox.getContentPane.add(aboutPanel)
        aboutBox.setSize(430, 600)
        aboutBox.setLocationRelativeTo(null)
        aboutBox.pack
        ok.requestFocusInWindow()
        aboutBox.setVisible(true)
      }
    })
    helpMenu.add(about)
    menuBar
  }
}