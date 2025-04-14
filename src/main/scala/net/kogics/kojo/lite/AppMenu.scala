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
package net.kogics.kojo.lite

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.Component
import java.awt.Dimension
import java.awt.Insets
import javax.swing._
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener
import javax.swing.text.html.HTMLEditorKit

import net.kogics.kojo.action.CloseFile
import net.kogics.kojo.action.LoadFrom
import net.kogics.kojo.action.NewFile
import net.kogics.kojo.action.Save
import net.kogics.kojo.action.SaveAs
import net.kogics.kojo.appexport.WebAppExporter
import net.kogics.kojo.lite.action.FullScreenCanvasAction
import net.kogics.kojo.lite.action.FullScreenOutputAction
import net.kogics.kojo.lite.action.FullScreenSupport
import net.kogics.kojo.util.Utils

trait AppMenu {
  self: Main.type =>
  def menuBar = {
    val menuBar = new JMenuBar

    val fileMenu = newJMenu(Utils.loadString("S_File"))
    fileMenu.setMnemonic('F')

    val openWeb = new JMenuItem(Utils.loadString("S_OpenFromWeb"))
    openWeb.addActionListener(new ActionListener {
      def actionPerformed(ev: ActionEvent): Unit = {
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
          def actionPerformed(ev: ActionEvent): Unit = {
            urlGetter.setVisible(false)
            loadUrl(urlBox.getText)
          }
        })
        val cancel = new JButton(Utils.loadString("S_Cancel"))
        cancel.addActionListener(new ActionListener {
          def actionPerformed(ev: ActionEvent): Unit = {
            urlGetter.setVisible(false)
          }
        })
        okCancel.add(ok)
        okCancel.add(cancel)
        urlPanel.add(okCancel)

        urlGetter.setModal(true)
        urlGetter.getRootPane.setDefaultButton(ok)
        Utils.closeOnEsc(urlGetter)
        urlGetter.getContentPane.add(urlPanel)
        urlGetter.setBounds(300, 300, 450, 300)
        urlGetter.pack()
        urlGetter.setLocationRelativeTo(frame)
        urlGetter.setVisible(true)
      }
    })

    val scriptEditor = scriptEditorHolder.se
    fileMenu.add(new JMenuItem(new NewFile(scriptEditor)))

    val openFile = new JMenuItem(Utils.loadString("S_Open"))
    openFile.addActionListener(new LoadFrom(scriptEditor))
    openFile.setIcon(Utils.loadIcon("/images/extra/open.gif"))
    openFile.setAccelerator(KeyStroke.getKeyStroke("control O"))
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
      def actionPerformed(e: ActionEvent): Unit = {
        KojoScratchpadRunner.newScratchPad()
      }
    })
    newKojo.setAccelerator(KeyStroke.getKeyStroke("control N"))
    fileMenu.add(newKojo)

    fileMenu.add(new JMenuItem(new AbstractAction(Utils.loadString("S_Settings")) {
      def actionPerformed(e: ActionEvent): Unit = {
        new SettingsWindow(frame).setVisible(true)
      }
    }))

    fileMenu.add(new JMenuItem(new AbstractAction(Utils.loadString("S_ExportWebApp")) {
      def actionPerformed(e: ActionEvent): Unit = Utils.runAsync {
        WebAppExporter.run(scriptEditor.codePane.getText)
      }
    }))

    fileMenu.addSeparator()

    fileMenu.add(new JMenuItem(new AbstractAction(Utils.loadString("S_Exit")) {
      def actionPerformed(e: ActionEvent): Unit = {
        appExit()
      }
    }))

    menuBar.add(fileMenu)

    def menuItemFor(key: String, file: String, root: String = "/samples/") = {
      val label = Utils.loadString(key)
      val item = new JMenuItem(label)
      item.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent): Unit = {
          // loadAndRunResource(root + file)
          loadAndRunLocalizedResource(root, file)
        }
      })
      item
    }

    def menuItemNELFor(label: String, file: String) = {
      val item = new JMenuItem(label)
      item.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent): Unit = {
          loadAndRunResourceNEL("/samples/", file)
        }
      })
      item
    }

    def menuItemForUrl(label: String, url: String) = {
      val item = new JMenuItem(label)
      item.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent): Unit = {
          loadAndRunUrl(url)
        }
      })
      item
    }

    def menuItemForInstalledFile(key: String, file: String) = {
      val label = Utils.loadString(key)
      val item = new JMenuItem(label)
      item.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent): Unit = {
          loadAndRunInstalledFile(file + ".installed")
        }
      })
      item
    }

    val samplesMenu = newJMenu(Utils.loadString("S_Samples"))
    samplesMenu.setMnemonic('S')

    val simpleMenu = newJMenu(Utils.loadString("S_GetStart"))
    simpleMenu.add(menuItemFor("S_Square", "square.kojo"))
    simpleMenu.add(menuItemFor("S_ColorsShapes", "shapes-cols.kojo"))
    simpleMenu.add(menuItemFor("S_SquarePattern", "square-pattern.kojo"))
    samplesMenu.add(simpleMenu)

    val drawingsMenu = newJMenu(Utils.loadString("S_Intermediate"))
    drawingsMenu.add(menuItemFor("S_PentagonPattern", "penta-pattern.kojo"))
    drawingsMenu.add(menuItemFor("S_Circles", "circles.kojo"))
    drawingsMenu.add(menuItemFor("S_SpiralSquareTiles", "spiral-square-tiles.kojo"))
    drawingsMenu.add(menuItemFor("S_SpiralHexagonalTiles", "spiral-hexagon-tiles.kojo"))
    samplesMenu.add(drawingsMenu)

    val multiMenu = newJMenu(Utils.loadString("S_MultipleTurtles"))
    multiMenu.add(menuItemFor("S_SyncSquares", "synchronized-squares.kojo"))
    multiMenu.add(menuItemFor("S_SyncSquares2", "synchronized-squares2.kojo"))
    multiMenu.add(menuItemFor("S_Face", "face-multi.kojo"))
    multiMenu.add(menuItemFor("S_Interaction", "two-turtle-interaction.kojo"))
    multiMenu.add(menuItemFor("S_Interaction2", "two-turtle-interaction2.kojo"))
    multiMenu.add(menuItemFor("S_SpriteAnimation", "sprite-animation.kojo"))
    multiMenu.add(menuItemFor("S_FerrisWheel", "ferris-wheel.kojo"))
    multiMenu.add(menuItemFor("S_Rangoli", "rangoli.kojo"))
    samplesMenu.add(multiMenu)

    val fractalsMenu = newJMenu(Utils.loadString("S_Fractals"))
    fractalsMenu.add(menuItemFor("S_Tree", "tree0.kojo"))
    fractalsMenu.add(menuItemFor("S_AnotherTree", "tree1.kojo"))
    fractalsMenu.add(menuItemFor("S_DesertTree", "tree2.kojo"))
    fractalsMenu.add(menuItemFor("S_FibonacciTree", "fib-tree.kojo"))
    fractalsMenu.add(menuItemFor("S_Snowflake", "snowflake.kojo"))
    fractalsMenu.add(menuItemFor("S_DragonCurve", "dragon-curve.kojo"))
    fractalsMenu.add(menuItemFor("S_SierpinskiTriangle", "sierpinski-tri.kojo"))
    fractalsMenu.add(menuItemFor("S_LSystems", "l-systems.kojo"))
    samplesMenu.add(fractalsMenu)

    val imageMenu = newJMenu(Utils.loadString("S_ImageLayout"))
    imageMenu.add(menuItemFor("S_ImageCollage", "image-collage.kojo"))
    imageMenu.add(menuItemFor("S_ImageRightSplit", "image-right-split.kojo"))
    samplesMenu.add(imageMenu)

    val numProgMenu = newJMenu(Utils.loadString("S_Numbers"))
    numProgMenu.add(menuItemFor("S_Primes", "primes.kojo"))
    numProgMenu.add(menuItemFor("S_PrimeFactors", "prime-factors.kojo"))
    numProgMenu.add(menuItemFor("S_EstimatingPiMC", "estimating-pi-mc.kojo"))
    samplesMenu.add(numProgMenu)

    val physicsMenu = newJMenu(Utils.loadString("S_Physics"))
    physicsMenu.add(menuItemFor("S_Kinematics", "physics-uvats.kojo"))
    physicsMenu.add(menuItemFor("S_NewtonsSecond", "physics-fma.kojo"))
    samplesMenu.add(physicsMenu)

    val generativeArtMenu = newJMenu(Utils.loadString("S_GenerativeArt"))
    generativeArtMenu.add(menuItemFor("S_TiledLines", "genart-tiled-lines.kojo"))
    generativeArtMenu.add(menuItemFor("S_JoyDivision", "genart-joy-division.kojo"))
    generativeArtMenu.add(menuItemFor("S_CubicDisarray", "genart-cubic-disarray.kojo"))
    generativeArtMenu.add(menuItemFor("S_TriMesh", "genart-tri-mesh.kojo"))
    generativeArtMenu.add(menuItemFor("S_Mondrian", "genart-mondrian.kojo"))
    generativeArtMenu.add(menuItemFor("S_HypnoticSquares", "genart-hypnotic-squares.kojo"))
    generativeArtMenu.add(menuItemFor("S_Delaunay", "genart-delaunay.kojo"))

    samplesMenu.add(generativeArtMenu)

    val animGameMenu = newJMenu(Utils.loadString("S_AnimationsGames"))
    animGameMenu.add(menuItemFor("S_LunarLander", "lunar-lander.kojo"))
    animGameMenu.add(menuItemFor("S_PulsatingLamp", "lamp-animation.kojo"))
    animGameMenu.add(menuItemFor("S_PulsatingLamp2", "lamp-animation2.kojo"))
    animGameMenu.add(menuItemFor("S_DynamicSquare", "animated-square-creation.kojo"))
    animGameMenu.add(menuItemFor("S_Fireworks", "fireworks-canvas.kojo"))
    animGameMenu.add(menuItemFor("S_Fireworks2", "fireworks.kojo"))
    animGameMenu.add(menuItemFor("S_TangramSkier", "tangram-skier.kojo"))
    animGameMenu.add(menuItemFor("S_Pong", "pong.kojo"))
    animGameMenu.add(menuItemFor("S_MemoryCards", "memory-cards.kojo"))
    animGameMenu.add(menuItemFor("S_CrazySquare", "crazy-square.kojo"))
    animGameMenu.add(menuItemFor("S_Hunted", "hunted.kojo"))
    animGameMenu.add(menuItemFor("S_FlappyBall", "flappy-ball.kojo"))
    animGameMenu.add(menuItemFor("S_Collidium", "collidium.kojo"))
    animGameMenu.add(menuItemFor("S_CarRide", "car-ride.kojo"))
    animGameMenu.add(menuItemForInstalledFile("S_Platformer", "examples/tiledgame/game.kojo"))
    animGameMenu.add(menuItemFor("S_TicTacToe", "tic-tac-toe.kojo"))
    animGameMenu.add(menuItemForInstalledFile("S_Othello", "examples/othello/menu.kojo"))

    samplesMenu.add(animGameMenu)

    val roboSimMenu = newJMenu(Utils.loadString("S_RobotSimulations"))
    roboSimMenu.add(menuItemFor("S_RoboSim1", "obstacle-greedy.kojo", "/robosim/"))
    roboSimMenu.add(menuItemFor("S_RoboSim2", "obstacle-furthest.kojo", "/robosim/"))

    samplesMenu.add(roboSimMenu)

    val widgetsMenu = newJMenu(Utils.loadString("S_Widgets"))
    widgetsMenu.add(menuItemFor("S_WidgetsInCanvas", "widgets-canvas.kojo"))
    samplesMenu.add(widgetsMenu)

    val musicMenu = newJMenu(Utils.loadString("S_Music"))
    musicMenu.add(menuItemFor("S_SomeNotes", "some-notes.kojo"))
    musicMenu.add(menuItemFor("S_Tune1", "tune1.kojo"))
    musicMenu.add(menuItemFor("S_Tune2", "tune2.kojo"))
    samplesMenu.add(musicMenu)

    val genProgMenu = newJMenu(Utils.loadString("S_GeneralProgramming"))
    genProgMenu.add(menuItemFor("S_InputOutput", "read-vector-mean.kojo"))
    genProgMenu.add(menuItemFor("S_InputGraphics", "read-vector-bargraph.kojo"))
    genProgMenu.add(menuItemForInstalledFile("S_Anagrams", "examples/anagram/anagram.kojo"))
    samplesMenu.add(genProgMenu)

    samplesMenu.addSeparator()

    val mathActivityMenu = newJMenu(Utils.loadString("S_MathActivities"))
    mathActivityMenu.add(menuItemFor("S_AdditionGame", "addition-game.kojo"))
    mathActivityMenu.add(menuItemFor("S_SubtractionGame", "subtraction-game.kojo"))
    mathActivityMenu.add(menuItemFor("S_MultiplicationGame", "multiplication-game.kojo"))
    mathActivityMenu.add(menuItemFor("S_AngleExperiment", "angle-experiment.kojo"))
    mathActivityMenu.add(menuItemFor("S_CountingGame", "counting.kojo", "/mathgames/"))
    mathActivityMenu.add(menuItemFor("S_MakeFractions", "make-fractions.kojo", "/mathgames/"))
    mathActivityMenu.add(menuItemFor("S_IdentifyFractions", "identify-fractions.kojo", "/mathgames/"))
    mathActivityMenu.add(menuItemFor("S_AnglesAnimation", "angles.kojo"))
    mathActivityMenu.add(menuItemFor("S_UnitCircle", "unit-circle.kojo"))
    // mathActivityMenu.add(menuItemFor("S_SolvingLinearEquations", "solving-linear-equations.kojo"))
    samplesMenu.add(mathActivityMenu)

    menuBar.add(samplesMenu)

    val showcaseMenu = newJMenu(Utils.loadString("S_Showcase"))
    showcaseMenu.setMnemonic('c')
    showcaseMenu.add(menuItemFor("S_Eye", "eye.kojo"))
    showcaseMenu.add(menuItemFor("S_EyeEffects", "eye-effects.kojo"))
    showcaseMenu.add(menuItemFor("S_Spiral", "spiral.kojo"))
    showcaseMenu.add(menuItemFor("S_SpiralEffects", "spiral-effects.kojo"))
    showcaseMenu.add(menuItemFor("S_Radiance", "radiance.kojo"))
    showcaseMenu.add(menuItemFor("S_RandomDots", "random-dots.kojo"))
    showcaseMenu.add(menuItemFor("S_TanTheta", "tan-theta.kojo"))
    showcaseMenu.add(menuItemFor("S_Fern", "fern.kojo"))
    showcaseMenu.add(menuItemFor("S_LightedStar", "lighted-star.kojo"))
    showcaseMenu.add(menuItemFor("S_Mandel", "mandelbrot.kojo"))
    showcaseMenu.add(menuItemFor("S_Mondrian", "genart-mondrian.kojo"))
    showcaseMenu.add(menuItemFor("S_Delaunay", "genart-delaunay.kojo"))
    showcaseMenu.add(menuItemFor("S_Fireworks", "fireworks-canvas.kojo"))
    showcaseMenu.add(menuItemFor("S_Collidium", "collidium.kojo"))
    showcaseMenu.add(menuItemFor("S_CarRide", "car-ride.kojo"))
    showcaseMenu.add(menuItemForInstalledFile("S_Platformer", "examples/tiledgame/game.kojo"))
    showcaseMenu.add(menuItemFor("S_TicTacToe", "tic-tac-toe.kojo"))
    showcaseMenu.add(menuItemForInstalledFile("S_Othello", "examples/othello/menu.kojo"))

    menuBar.add(showcaseMenu)

    val windowMenu = newJMenu(Utils.loadString("S_Window"))
    windowMenu.setMnemonic('W')

    val resetWindows = new JMenuItem(Utils.loadString("S_DefaultPerspective"))
    resetWindows.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        kojoCtx.switchToDefaultPerspective()
      }
    })
    windowMenu.add(resetWindows)

    val resetWindows2 = new JMenuItem(Utils.loadString("S_Default2Perspective"))
    resetWindows2.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        kojoCtx.switchToDefault2Perspective()
      }
    })
    windowMenu.add(resetWindows2)

    val worksheetItem = new JMenuItem(Utils.loadString("S_WorksheetPerspective"))
    worksheetItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        kojoCtx.switchToWorksheetPerspective()
      }
    })
    windowMenu.add(worksheetItem)

    val scriptEditingItem = new JMenuItem(Utils.loadString("S_ScriptEditingPerspective"))
    scriptEditingItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        kojoCtx.switchToScriptEditingPerspective()
      }
    })
    windowMenu.add(scriptEditingItem)

    val historyItem = new JMenuItem(Utils.loadString("S_HistoryBrowsingPerspective"))
    historyItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        kojoCtx.switchToHistoryBrowsingPerspective()
      }
    })
    windowMenu.add(historyItem)

    val storyItem = new JMenuItem(Utils.loadString("S_StoryViewingPerspective"))
    storyItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        kojoCtx.switchToStoryViewingPerspective()
      }
    })
    windowMenu.add(storyItem)

    val canvasItem = new JMenuItem(Utils.loadString("S_OutputStoryViewingPerspective"))
    canvasItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        kojoCtx.switchToOutputStoryViewingPerspective()
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

    windowMenu.getPopupMenu.addPopupMenuListener(new PopupMenuListener {
      def popupMenuWillBecomeVisible(e: PopupMenuEvent): Unit = {
        FullScreenSupport.updateMenuItem(fullScreenCanvasItem, fsCanvasAction)
        FullScreenSupport.updateMenuItem(fullScreenOutputItem, fsOutputAction)
      }

      def popupMenuWillBecomeInvisible(e: PopupMenuEvent): Unit = {}

      def popupMenuCanceled(e: PopupMenuEvent): Unit = {}
    })

    menuBar.add(windowMenu)

    val langMenu = LangMenuFactory.createLangMenu()
    kojoCtx.menuReady(langMenu)
    menuBar.add(langMenu)

    val toolsMenu = newJMenu(Utils.loadString("S_Tools"))
    toolsMenu.setMnemonic('T')
    toolsMenu.add(menuItemNELFor(Utils.loadString("S_InstructionPalette"), "instruction-palette.kojo"))
    toolsMenu.add(menuItemFor("S_TurtleController", "turtle-controller.kojo"))
    toolsMenu.add(menuItemFor("S_ArduinoProgramming", "arduino-prog.kojo"))
    toolsMenu.add(menuItemFor("S_StartingChallenges", "get-started.kojo", "/challenge/"))
    toolsMenu.add(menuItemFor("S_SpriteBoundaryPolygon", "sprite-boundary-polygon.kojo"))
    menuBar.add(toolsMenu)

    val helpMenu = newJMenu(Utils.loadString("S_Help"))
    helpMenu.setMnemonic('H')

    // helpMenu.add(menuItemFor("S_KojoOverview", "kojo-overview.kojo"))
    helpMenu.add(menuItemFor("S_KojoDocs", "kojo-documentation.kojo"))
    helpMenu.add(menuItemFor("S_ScalaTutorial", "scala-tutorial.kojo"))
    helpMenu.addSeparator()

    val about = new JMenuItem(Utils.loadString("S_About"))
    about.addActionListener(new ActionListener {
      def actionPerformed(ev: ActionEvent): Unit = {
        val aboutBox = new JDialog(frame)
        val aboutPanel = new JPanel
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS))

        val kojoIcon = new JLabel()
        kojoIcon.setIcon(Utils.loadIcon("/images/splash.png"))
        kojoIcon.setSize(430, 280)
        kojoIcon.setAlignmentX(Component.CENTER_ALIGNMENT)
        aboutPanel.add(kojoIcon)

        val aboutText = new JEditorPane
        aboutText.setEditorKit(new HTMLEditorKit)
        aboutText.setEditable(false)
        aboutText.setText(s"""<html><body>
<div style="font-size: ${12 + kojoCtx.screenDpiFontDelta}pt; font-family: Verdana, 'Verdana CE',  Arial, 'Arial CE', 'Lucida Grande CE', lucida, 'Helvetica CE', sans-serif; ">
              <strong>Kojo</strong> ${Versions.KojoMajorVersion}<br/>
              Version: ${Versions.KojoVersion}  <em>${Versions.KojoRevision}</em><br/>
              Build date: ${Versions.KojoBuildDate}<br/>
              <em>Java version: ${Versions.JavaVersion}. Scala version: ${Versions.ScalaVersion}</em> <br/><br/>
              Copyright &copy; 2009-2025 Lalit Pant (lalit@kogics.net) as per contributions.<br/>
              Copyright &copy; Project contributors as per contributions.<br/><br/>
              Published by the Kogics Foundation, Dehradun, India (www.kogics.net).<br/>
              Please visit <em>www.kogics.net/kojo</em> for more information about Kojo.<br/><br/>
              <strong>Kojo</strong> Contributors:<ul>
               <li>Lalit Pant</li>
               <li>Bj\u00f6rn Regnell</li>
               <li>Peter Lewerin</li>
               <li>Phil Bagwell</li>
               <li>Tanu Nayal</li>
               <li>Vibha Pant</li>
               <li>Anusha Pant</li>
               <li>Nikhil Pant</li>
               <li>Sami Jaber</li>
               <li>Aditya Pant</li>
               <li>Jerzy Redlarski</li>
               <li>Saurabh Kapoor</li>
               <li>Mushtaq Ahmed</li>
               <li>Ilango</li>
               <li>Pierre Couillard</li>
               <li>Audrey Neveu</li>
               <li>Mikołaj Sochacki</li>
               <li>Eric Zoerner</li>
               <li>Jacco Huysmans</li>
               <li>Christoph Knabe</li>
               <li>Vipul Pandey</li>
               <li>Aleksei Loginov</li>
               <li>Massimo Maria Ghisalberti</li>
               <li>Luka Volaric</li>
               <li>Marcus Klang</li>
               <li>Bülent Başaran</li>
               <li>Guillermo Ovejero</li>
               <li>Alberto R.R. Manzanares</li>
               <li>Anay Kamat</li>
               <li>Vasu Sethia</li>
               <li>Mudit Pant</li>
              </ul>
              <strong>Kojo</strong> is licensed under The GNU General Public License (GPL). The full text of the GPL is available at: http://www.gnu.org/licenses/gpl.html<br/><br/>
              Kojo runs on the Java Platform.<br/><br/>
              The list of third-party software used by <strong>Kojo</strong> 2.x includes:
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
              <li>Table Layout (https://java.net/projects/tablelayout) for Arithmetic Aerobics</li>
              <li>The Netbeans Platform (http://www.netbeans.org) for some Script Editor Icons</li>
              <li>The Scratch Project (http://scratch.mit.edu) for some Media files</li>
              <li>The OpenJDK Project (http://openjdk.java.net/) for Tracing support</li>
              <li>JHLabs image filters (http://www.jhlabs.com/ip/filters/) for Picture effects</li>
              <li>jSSC (http://code.google.com/p/java-simple-serial-connector/) for serial port communication</li>
              <li>The Gargi font (http://savannah.nongnu.org/projects/gargi) for Devanagari support</li>
              <li>The Doodle project (https://github.com/underscoreio/doodle) for rich color support</li>
              <li>Scalatest (http://www.scalatest.org/) for testing</li>
              <li>Akka (https://akka.io/) for concurrency</li>
              <li>FlatLaf (https://github.com/JFormDesigner/FlatLaf) for the Kojo 'Look and Feel'</li>
              <li>libTiled (https://www.mapeditor.org/) to load game level files created with the Tiled Map Editor</li>
              <li>The Processing lib (https://processing.org/) for perlin noise and curved shapes</li>
              <li>Delaunay Triangulator (https://github.com/jdiemke/delaunay-triangulator) for triangulation of points</li>
              <li>Java implementation of HSLuv (https://github.com/hsluv/hsluv-java) for a perceptually uniform color space</li>
              <li>The Penner easing functions for animation (https://github.com/mattdesl/cisc226game/blob/master/SpaceGame/src/space/engine/easing/Easing.java)</li>
              <li>Rhino (https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino) for the Code Exchange interface</li>
              </ul>
              </div>
              </body></html>
              """)
        aboutText.setPreferredSize(new Dimension(650, 300))
        aboutText.setMaximumSize(new Dimension(650, 300))
        aboutText.setCaretPosition(0)
        aboutText.setMargin(new Insets(15, 20, 15, 20))
        aboutPanel.add(new JScrollPane(aboutText))

        val verticalSpaceDim = new Dimension(1, 10)
        aboutPanel.add(Box.createRigidArea(verticalSpaceDim))

        val ok = new JButton(Utils.loadString("S_OK"))
        ok.setAlignmentX(Component.CENTER_ALIGNMENT)
        aboutBox.getRootPane.setDefaultButton(ok)
        ok.addActionListener(new ActionListener {
          def actionPerformed(ev: ActionEvent): Unit = {
            aboutBox.setVisible(false)
          }
        })
        aboutPanel.add(ok)

        aboutPanel.add(Box.createRigidArea(verticalSpaceDim))

        aboutBox.setModal(true)
        aboutBox.getContentPane.add(aboutPanel)
        aboutBox.pack()
        aboutBox.setLocationRelativeTo(frame)
        ok.requestFocusInWindow()
        Utils.closeOnEsc(aboutBox)
        aboutBox.setVisible(true)
      }
    })
    about.setIcon(Utils.loadIcon("/images/extra/about.gif"))
    helpMenu.add(about)

    menuBar.add(helpMenu)
    menuBar
  }

  def newJMenu(s: String) = {
    val m = new JMenu(s)
    kojoCtx.menuReady(m)
    m
  }

}
