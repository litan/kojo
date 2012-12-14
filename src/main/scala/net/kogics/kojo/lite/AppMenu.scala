package net.kogics.kojo.lite

import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.AbstractAction
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JCheckBoxMenuItem
import javax.swing.JDialog
import javax.swing.JEditorPane
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JOptionPane
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

import net.kogics.kojo.lite.Main

trait AppMenu { self: Main.type =>
  def menuBar = {
    val menuBar = new JMenuBar

    val fileMenu = new JMenu(Utils.loadString("S_File"))
    val openWeb = new JMenuItem(Utils.loadString("S_OpenFromWeb"))
    openWeb.addActionListener(new ActionListener {
      def actionPerformed(ev: ActionEvent) {
        val urlGetter = new JDialog(frame)
        urlGetter.setTitle(Utils.loadString("S_OpenFromWeb"))

        val urlPanel = new JPanel
        urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.Y_AXIS))

        val url = new JPanel
        url.add(new JLabel("URL: "))
        val urlBox = new JTextField(30)
        url.add(urlBox)
        urlPanel.add(url)

        val okCancel = new JPanel
        val ok = new JButton(Utils.loadString("S_OK"))
        ok.addActionListener(new ActionListener {
          def actionPerformed(ev: ActionEvent) {
            urlGetter.setVisible(false)
            loadUrl(urlBox.getText)
          }
        })
        val cancel = new JButton(Utils.loadString("S_Cancel"))
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

    val openFile = new JMenuItem(Utils.loadString("S_Open"))
    openFile.addActionListener(new LoadFrom(kojoCtx))
    fileMenu.add(openFile)

    val saveFile = new JMenuItem(Utils.loadString("S_Save"))
    saveFile.addActionListener(new Save(kojoCtx))
    fileMenu.add(saveFile)

    val saveAsActionListener = new SaveAs(kojoCtx)
    val saveAsFile = new JMenuItem(Utils.loadString("S_SaveAs"))
    saveAsFile.addActionListener(saveAsActionListener)
    fileMenu.add(saveAsFile)

    fileMenu.add(new JMenuItem(new CloseFile))

    fileMenu.add(openWeb)

    fileMenu.addSeparator()

    fileMenu.add(new JMenuItem(new AbstractAction(Utils.loadString("S_Exit")) {
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
    val samplesMenu = new JMenu(Utils.loadString("S_Samples"))
    val simpleMenu = new JMenu(Utils.loadString("S_GetStart"))
    simpleMenu.add(menuItemFor(Utils.loadString("S_Square"), "square.kojo"))
    simpleMenu.add(menuItemFor(Utils.loadString("S_ColorsShapes"), "shapes-cols.kojo"))
    simpleMenu.add(menuItemFor(Utils.loadString("S_SquarePattern"), "square-pattern.kojo"))
    samplesMenu.add(simpleMenu)

    val drawingsMenu = new JMenu(Utils.loadString("S_Intermediate"))
    drawingsMenu.add(menuItemFor(Utils.loadString("S_PentagonPattern"), "penta-pattern.kojo"))
    drawingsMenu.add(menuItemFor(Utils.loadString("S_Circles"), "circles.kojo"))
    drawingsMenu.add(menuItemFor(Utils.loadString("S_SpiralSquareTiles"), "spiral-square-tiles.kojo"))
    drawingsMenu.add(menuItemFor(Utils.loadString("S_SpiralHexagonalTiles"), "spiral-hexagon-tiles.kojo"))
    drawingsMenu.add(menuItemFor(Utils.loadString("S_FerrisWheel"), "ferris-wheel.kojo"))
    drawingsMenu.add(menuItemFor(Utils.loadString("S_Rangoli"), "rangoli.kojo"))
    samplesMenu.add(drawingsMenu)

    val fractalsMenu = new JMenu(Utils.loadString("S_Fractals"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_Tree"), "tree0.kojo"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_AnotherTree"), "tree1.kojo"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_FibonacciTree"), "fib-tree.kojo"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_Snowflake"), "snowflake.kojo"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_SierpinskiTriangle"), "sierpinski-tri.kojo"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_LSystems"), "l-systems.kojo"))
    samplesMenu.add(fractalsMenu)

    val animGameMenu = new JMenu(Utils.loadString("S_AnimationsGames"))
    animGameMenu.add(menuItemFor(Utils.loadString("S_TangramSkier"), "tangram-skier.kojo"))
    animGameMenu.add(menuItemFor(Utils.loadString("S_Hunted"), "hunted.kojo"))
    samplesMenu.add(animGameMenu)

    val mgeomMenu = new JMenu(Utils.loadString("S_MathActivities"))
    mgeomMenu.add(menuItemForUrl(Utils.loadString("S_SolvingLinearEquations"), "http://www.kogics.net/public/kojolite/samples/solving-linear-equations.kojo"))
    samplesMenu.add(mgeomMenu)

    menuBar.add(samplesMenu)

    val windowMenu = new JMenu(Utils.loadString("S_Window"))
    val resetWindows = new JMenuItem(Utils.loadString("S_DefaultPerspective"))
    resetWindows.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        kojoCtx.activateDefaultPerspective()
      }
    })
    windowMenu.add(resetWindows)

    val storyItem = new JMenuItem(Utils.loadString("S_StoryViewingPerspective"))
    storyItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        kojoCtx.activateStoryViewingPerspective()
      }
    })
    windowMenu.add(storyItem)

    val historyItem = new JMenuItem(Utils.loadString("S_HistoryBrowsingPerspective"))
    historyItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        kojoCtx.activateHistoryBrowsingPerspective()
      }
    })
    windowMenu.add(historyItem)

    val drawingCanvasItem = new JMenuItem(Utils.loadString("S_NoGraphicsPerspective"))
    drawingCanvasItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        kojoCtx.activateNoGraphicsPerspective()
      }
    })
    windowMenu.add(drawingCanvasItem)

    menuBar.add(windowMenu)

    val langMenu = new JMenu(Utils.loadString("S_Language"))
    val langMap = Map(
      "English" -> "en",
      "Swedish (in-progress)" -> "sv",
      "French (coming-up)" -> "fr",
      "Italian (coming-up)" -> "it"
    )
    var langMenus: Seq[JCheckBoxMenuItem] = Vector()
    val langHandler = new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        val lang = e.getActionCommand
        kojoCtx.userLanguage = lang
        langMenus foreach { mi =>
          if (mi.getActionCommand() != lang) {
            mi.setSelected(false)
          }
        }
        JOptionPane.showMessageDialog(kojoCtx.frame,
          Utils.loadString("S_LangChanged") format (e.getSource.asInstanceOf[JCheckBoxMenuItem].getText()),
          Utils.loadString("S_LangChange"),
          JOptionPane.INFORMATION_MESSAGE)
      }
    }

    def langMenuItem(lang: String) = {
      val langCode = langMap(lang)
      val mitem = new JCheckBoxMenuItem(lang)
      mitem.addActionListener(langHandler)
      mitem.setActionCommand(langCode)
      if (kojoCtx.userLanguage == langCode) {
        mitem.setSelected(true)
      }
      langMenus :+= mitem
      mitem
    }

    langMenu.add(langMenuItem("English"))
    langMenu.add(langMenuItem("French (coming-up)"))
    langMenu.add(langMenuItem("Italian (coming-up)"))
    langMenu.add(langMenuItem("Swedish (in-progress)"))
    menuBar.add(langMenu)

    val helpMenu = new JMenu(Utils.loadString("S_Help"))
    helpMenu.add(menuItemFor(Utils.loadString("S_KojoOverview"), "kojo-overview.kojo"))
    helpMenu.add(menuItemFor(Utils.loadString("S_ScalaTutorial"), "scala-tutorial.kojo"))
    helpMenu.add(menuItemFor(Utils.loadString("S_Intro3D"), "d3-intro.kojo"))
    helpMenu.addSeparator()

    val about = new JMenuItem(Utils.loadString("S_About"))
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
              <strong>Kojo</strong> 2.0 Beta<br/>
              Version: 141212-2 <br/>
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
        val ok = new JButton(Utils.loadString("S_OK"))
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

    menuBar.add(helpMenu)
    menuBar
  }
}