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
import javax.swing.KeyStroke
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener
import javax.swing.text.html.HTMLEditorKit

import net.kogics.kojo.action.CloseFile
import net.kogics.kojo.action.LoadFrom
import net.kogics.kojo.action.NewFile
import net.kogics.kojo.action.Save
import net.kogics.kojo.action.SaveAs
import net.kogics.kojo.lite.action.FullScreenCanvasAction
import net.kogics.kojo.lite.action.FullScreenOutputAction
import net.kogics.kojo.lite.action.FullScreenSupport
import net.kogics.kojo.util.Utils

trait AppMenu { self: Main.type =>
  def menuBar = {
    val menuBar = new JMenuBar

    val fileMenu = new JMenu(Utils.loadString("S_File"))
    fileMenu.setMnemonic('F')

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

    val scriptEditor = scriptEditorHolder.se
    fileMenu.add(new JMenuItem(new NewFile(scriptEditor)))

    val openFile = new JMenuItem(Utils.loadString("S_Open"))
    openFile.addActionListener(new LoadFrom(scriptEditor))
    openFile.setIcon(Utils.loadIcon("/images/extra/open.gif"))
    fileMenu.add(openFile)

    val saveFile = new JMenuItem(Utils.loadString("S_Save"))
    saveFile.addActionListener(new Save(scriptEditor))
    saveFile.setIcon(Utils.loadIcon("/images/extra/save.gif"))
    saveFile.setAccelerator(KeyStroke.getKeyStroke("control S"))
    fileMenu.add(saveFile)

    val saveAsActionListener = new SaveAs(scriptEditor)
    val saveAsFile = new JMenuItem(Utils.loadString("S_SaveAs"))
    saveAsFile.addActionListener(saveAsActionListener)
    saveAsFile.setIcon(Utils.loadIcon("/images/extra/saveas.gif"))
    fileMenu.add(saveAsFile)

    fileMenu.add(new JMenuItem(new CloseFile(scriptEditor)))

    fileMenu.add(openWeb)

    fileMenu.addSeparator()

    val newKojo = new JMenuItem(Utils.loadString("S_New_Scratchpad"))
    newKojo.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        Utils.runAsync {
          NewKojoInstance.main(Array("subKojo"))
        }
      }
    })
    fileMenu.add(newKojo)

    fileMenu.addSeparator()

    fileMenu.add(new JMenuItem(new AbstractAction(Utils.loadString("S_Exit")) {
      def actionPerformed(e: ActionEvent) {
        appExit()
      }
    }))

    menuBar.add(fileMenu)

    def menuItemFor(label: String, file: String) = {
      val item = new JMenuItem(label)
      item.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent) {
          loadAndRunResource("/samples/" + file)
        }
      })
      item
    }

    def menuItemNELFor(label: String, file: String) = {
      val item = new JMenuItem(label)
      item.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent) {
          loadAndRunResourceNEL("/samples/" + file)
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
    samplesMenu.setMnemonic('S')

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
    samplesMenu.add(drawingsMenu)

    val multiMenu = new JMenu(Utils.loadString("S_MultipleTurtles"))
    multiMenu.add(menuItemFor(Utils.loadString("S_SyncSquares"), "synchronized-squares.kojo"))
    multiMenu.add(menuItemFor(Utils.loadString("S_SyncSquares2"), "synchronized-squares2.kojo"))
    multiMenu.add(menuItemFor(Utils.loadString("S_Face"), "face-multi.kojo"))
    multiMenu.add(menuItemFor(Utils.loadString("S_Interaction"), "two-turtle-interaction.kojo"))
    multiMenu.add(menuItemFor(Utils.loadString("S_Interaction2"), "two-turtle-interaction2.kojo"))
    multiMenu.add(menuItemFor(Utils.loadString("S_SpriteAnimation"), "sprite-animation.kojo"))
    multiMenu.add(menuItemFor(Utils.loadString("S_FerrisWheel"), "ferris-wheel.kojo"))
    multiMenu.add(menuItemFor(Utils.loadString("S_Rangoli"), "rangoli.kojo"))
    samplesMenu.add(multiMenu)

    val fractalsMenu = new JMenu(Utils.loadString("S_Fractals"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_Tree"), "tree0.kojo"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_AnotherTree"), "tree1.kojo"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_FibonacciTree"), "fib-tree.kojo"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_Snowflake"), "snowflake.kojo"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_SierpinskiTriangle"), "sierpinski-tri.kojo"))
    fractalsMenu.add(menuItemFor(Utils.loadString("S_LSystems"), "l-systems.kojo"))
    samplesMenu.add(fractalsMenu)

    val numProgMenu = new JMenu(Utils.loadString("S_Numbers"))
    numProgMenu.add(menuItemFor(Utils.loadString("S_Primes"), "primes.kojo"))
    numProgMenu.add(menuItemFor(Utils.loadString("S_PrimeFactors"), "prime-factors.kojo"))
    samplesMenu.add(numProgMenu)

    val physicsMenu = new JMenu(Utils.loadString("S_Physics"))
    physicsMenu.add(menuItemFor(Utils.loadString("S_Kinematics"), "physics-uvats.kojo"))
    physicsMenu.add(menuItemFor(Utils.loadString("S_NewtonsSecond"), "physics-fma.kojo"))
    samplesMenu.add(physicsMenu)

    val animGameMenu = new JMenu(Utils.loadString("S_AnimationsGames"))
    animGameMenu.add(menuItemFor(Utils.loadString("S_TangramSkier"), "tangram-skier.kojo"))
    animGameMenu.add(menuItemFor(Utils.loadString("S_Pong"), "pong.kojo"))
    animGameMenu.add(menuItemFor(Utils.loadString("S_MemoryCards"), "memory-cards.kojo"))
    animGameMenu.add(menuItemFor(Utils.loadString("S_Hunted"), "hunted.kojo"))
    samplesMenu.add(animGameMenu)

    val musicMenu = new JMenu(Utils.loadString("S_Music"))
    musicMenu.add(menuItemFor(Utils.loadString("S_SomeNotes"), "some-notes.kojo"))
    musicMenu.add(menuItemFor(Utils.loadString("S_Tune1"), "tune1.kojo"))
    musicMenu.add(menuItemFor(Utils.loadString("S_Tune2"), "tune2.kojo"))
    samplesMenu.add(musicMenu)

    val genProgMenu = new JMenu(Utils.loadString("S_GeneralProgramming"))
    genProgMenu.add(menuItemFor(Utils.loadString("S_InputOutput"), "read-vector-mean.kojo"))
    genProgMenu.add(menuItemFor(Utils.loadString("S_InputGraphics"), "read-vector-bargraph.kojo"))
    samplesMenu.add(genProgMenu)

    samplesMenu.addSeparator()

    val mgeomMenu = new JMenu(Utils.loadString("S_MathActivities"))
    mgeomMenu.add(menuItemFor(Utils.loadString("S_SolvingLinearEquations"), "solving-linear-equations.kojo"))
    samplesMenu.add(mgeomMenu)

    menuBar.add(samplesMenu)

    val windowMenu = new JMenu(Utils.loadString("S_Window"))
    windowMenu.setMnemonic('W')

    val resetWindows = new JMenuItem(Utils.loadString("S_DefaultPerspective"))
    resetWindows.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        kojoCtx.switchToDefaultPerspective()
      }
    })
    windowMenu.add(resetWindows)

    val worksheetItem = new JMenuItem(Utils.loadString("S_WorksheetPerspective"))
    worksheetItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        kojoCtx.switchToWorksheetPerspective()
      }
    })
    windowMenu.add(worksheetItem)

    val scriptEditingItem = new JMenuItem(Utils.loadString("S_ScriptEditingPerspective"))
    scriptEditingItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        kojoCtx.switchToScriptEditingPerspective()
      }
    })
    windowMenu.add(scriptEditingItem)

    val historyItem = new JMenuItem(Utils.loadString("S_HistoryBrowsingPerspective"))
    historyItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        kojoCtx.switchToHistoryBrowsingPerspective()
      }
    })
    windowMenu.add(historyItem)

    val storyItem = new JMenuItem(Utils.loadString("S_StoryViewingPerspective"))
    storyItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        kojoCtx.switchToStoryViewingPerspective()
      }
    })
    windowMenu.add(storyItem)

    val canvasItem = new JMenuItem(Utils.loadString("S_CanvasPerspective"))
    canvasItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        kojoCtx.switchToCanvasPerspective()
      }
    })
    windowMenu.add(canvasItem)

    windowMenu.addSeparator()

    val fsCanvasAction = FullScreenCanvasAction(drawingCanvasHolder, kojoCtx)
    val fullScreenCanvasItem: JCheckBoxMenuItem = new JCheckBoxMenuItem(fsCanvasAction)
    windowMenu.add(fullScreenCanvasItem)

    val fsOutputAction = FullScreenOutputAction(outputPaneHolder)
    val fullScreenOutputItem: JCheckBoxMenuItem = new JCheckBoxMenuItem(fsOutputAction)
    windowMenu.add(fullScreenOutputItem)

    windowMenu.getPopupMenu().addPopupMenuListener(new PopupMenuListener {
      def popupMenuWillBecomeVisible(e: PopupMenuEvent) {
        FullScreenSupport.updateMenuItem(fullScreenCanvasItem, fsCanvasAction)
        FullScreenSupport.updateMenuItem(fullScreenOutputItem, fsOutputAction)
      }
      def popupMenuWillBecomeInvisible(e: PopupMenuEvent) {}

      def popupMenuCanceled(e: PopupMenuEvent) {}
    })

    menuBar.add(windowMenu)

    val langMenu = new JMenu(Utils.loadString("S_Language"))
    langMenu.setMnemonic('L')

    val langMap = Map(
      "English" -> "en",
      "Swedish" -> "sv",
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
    //    langMenu.add(langMenuItem("French (coming-up)"))
    //    langMenu.add(langMenuItem("Italian (coming-up)"))
    langMenu.add(langMenuItem("Swedish"))
    menuBar.add(langMenu)

    val toolsMenu = new JMenu(Utils.loadString("S_Tools"))
    toolsMenu.setMnemonic('T')
    toolsMenu.add(menuItemNELFor(Utils.loadString("S_InstructionPalette"), "instruction-palette.kojo"))
    menuBar.add(toolsMenu)

    val helpMenu = new JMenu(Utils.loadString("S_Help"))
    helpMenu.setMnemonic('H')

    helpMenu.add(menuItemFor(Utils.loadString("S_KojoOverview"), "kojo-overview.kojo"))
    helpMenu.add(menuItemFor(Utils.loadString("S_ScalaTutorial"), "scala-tutorial.kojo"))
    helpMenu.add(menuItemFor(Utils.loadString("S_ComposingMusic"), "composing-music.kojo"))
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
        aboutText.setText(s"""<html><body>
<div style="font-size: 12pt; font-family: Verdana, 'Verdana CE',  Arial, 'Arial CE', 'Lucida Grande CE', lucida, 'Helvetica CE', sans-serif; ">
              <strong>Kojo</strong> ${Versions.KojoMajorVersion}<br/>
              Version: ${Versions.KojoVersion} <br/>
              <em>Java version: ${Versions.JavaVersion}. Scala version: ${Versions.ScalaVersion}</em> <br/><br/>
              Copyright &copy; 2009-2013 Lalit Pant (pant.lalit@gmail.com) and the Kojo Dev Team.<br/><br/>
              Please visit <em>http://www.kogics.net/kojo</em> for more information about Kojo.<br/><br/>
              <strong>Kojo</strong> Contributors:<ul>
               <li>Lalit Pant</li>
               <li>Bj\u00f6rn Regnell</li>
               <li>Peter Lewerin</li>
               <li>Phil Bagwell</li>
               <li>Tanu Nayal</li>
               <li>Jerzy Redlarski</li>
               <li>Vibha Pant</li>
               <li>Anusha Pant</li>
               <li>Nikhil Pant</li>
               <li>Saurabh Kapoor</li>
               <li>Mushtaq Ahmed</li>
               <li>Ilango</li>
              </ul>
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
              <li>The Scratch Project (http://scratch.mit.edu) for some Media files</li>
              </ul>
              </div>
              </body></html>
              """
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
    about.setIcon(Utils.loadIcon("/images/extra/about.gif"))
    helpMenu.add(about)

    menuBar.add(helpMenu)
    menuBar
  }
}