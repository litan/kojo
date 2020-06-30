package net.kogics.kojo.livecoding

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Point
import java.awt.event.ActionEvent
import java.util.regex.Pattern

import javax.swing.AbstractAction
import javax.swing.BorderFactory
import javax.swing.JColorChooser
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.KeyStroke
import javax.swing.PopupFactory
import javax.swing.SwingUtilities
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import javax.swing.text.JTextComponent
import javax.swing.text.Utilities

import net.kogics.kojo.util.Utils

class ColorMakerManipulatorHexRgb(ctx: ManipulationContext) extends InteractiveManipulator {
  var target = ""
  var targetStart = 0
  var targetEnd = 0
  var targetColor: Color = null
  var colorPopup: JDialog = _
  var inSliderChange = false
  def isAbsent = colorPopup == null
  def isPresent = !isAbsent

  lazy val ColorPattern = Pattern.compile("""(ColorMaker|cm)\.hex(a)?\(0x((\d|[a-f])+)\)""")
  def matcher(possibleColor: String) = ColorPattern.matcher(possibleColor)
  lazy val ColorPattern2 = Pattern.compile("""(ColorMaker|cm)\.rgb(a)?\((\d+),\s*(\d+),\s*(\d+)(,\s*(\d+))?\)""")
  def matcher2(possibleColorLine: String) = ColorPattern2.matcher(possibleColorLine)

  def findColorHexFunction(pane: JTextComponent, offset: Int): Boolean = {
    val lineStart = Utilities.getRowStart(pane, offset)
    val lineEnd = Utilities.getRowEnd(pane, offset)
    val line = pane.getDocument.getText(lineStart, lineEnd - lineStart)
    val m = matcher(line)
    var found = false
    while (!found && m.find()) {
      val start = m.start
      val end = m.end
      val lineOffset = offset - lineStart
      if (start <= lineOffset && lineOffset <= end) {
        target = m.group
        val rgba = Integer.parseUnsignedInt(m.group(3), 16)
        val hasAlpha = (rgba >> 24) != 0
        targetColor = new Color(rgba, hasAlpha)
        targetStart = lineStart + start
        targetEnd = lineStart + end
        found = true
      }
    }
    found
  }

  def findColorFunction(pane: JTextComponent, offset: Int): Boolean = {
    val lineStart = Utilities.getRowStart(pane, offset)
    val lineEnd = Utilities.getRowEnd(pane, offset)
    val line = pane.getDocument.getText(lineStart, lineEnd - lineStart)
    val m = matcher2(line)
    var found = false
    while (!found && m.find()) {
      val start = m.start
      val end = m.end
      val lineOffset = offset - lineStart
      if (start <= lineOffset && lineOffset <= end) {
        target = m.group
        val rgba = Seq(3, 4, 5, 7) map { e =>
          val ret = m.group(e)
          if (ret != null) ret.toInt else 255
        }
        targetColor = new Color(rgba(0), rgba(1), rgba(2), rgba(3))
        targetStart = lineStart + start
        targetEnd = lineStart + end
        found = true
      }
    }
    found
  }

  def isHyperlinkPoint(pane: JTextComponent, offset: Int): Boolean = {
    try {
      val wordStart = Utilities.getWordStart(pane, offset)
      val wordEnd = Utilities.getWordEnd(pane, offset)

      if (wordStart == wordEnd) {
        false
      }
      else {
        if (findColorHexFunction(pane, offset)) {
          true
        }
        else {
          findColorFunction(pane, offset)
        }
      }
    }
    catch {
      case t: Throwable => false
    }
  }

  def getHyperlinkSpan(pane: JTextComponent, offset: Int): Array[Int] = {
    Array(targetStart, targetEnd)
  }

  def activate(pane: JTextComponent, offset: Int): Unit = {
    activate(pane, offset, target, targetStart)
  }

  def activate(pane: JTextComponent, offset: Int, target0: String, targetStart: Int) = Utils.safeProcess {
    showPopup(pane, offset)
  }

  def showPopup(pane: JTextComponent, offset: Int): Unit = {
    close()
    ctx.addManipulator(this)
    val doc = pane.getDocument
    val factory = PopupFactory.getSharedInstance();
    val rect = ctx.codePane.modelToView(offset)
    val pt = new Point(rect.x, rect.y)
    SwingUtilities.convertPointToScreen(pt, ctx.codePane)
    val panel = new JPanel()
    panel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1))
    panel.setLayout(new BorderLayout)
    val cc = new JColorChooser(targetColor)
    val panels = cc.getChooserPanels.toVector
    val rgbPanel = panels.find { p =>
      // a hack, but can't think of anything better
      // need to check RGB text for each new language
      p.getDisplayName.contains("RGB") || p.getDisplayName.contains("RVB")
    }
    rgbPanel match {
      case Some(rgbp) =>
        val newps = rgbp +: panels.filter { p => p != rgbp }
        cc.setChooserPanels(newps.toArray)
      case None =>
    }
    var oldColor = cc.getColor()
    var lastChangeTime = 0L
    cc.getSelectionModel.addChangeListener(new ChangeListener {
      def stateChanged(e: ChangeEvent) = Utils.safeProcess {
        val currTime = System.currentTimeMillis
        if (currTime - lastChangeTime > 200) {
          lastChangeTime = currTime
          val newColor = cc.getColor()
          if (oldColor != newColor) {
            inSliderChange = true
            doc.remove(targetStart, target.length())
            target = if (newColor.getAlpha == 255) {
              "ColorMaker.rgb(%d, %d, %d)".format(newColor.getRed, newColor.getGreen, newColor.getBlue)
            }
            else {
              "ColorMaker.rgba(%d, %d, %d, %d)".format(newColor.getRed, newColor.getGreen, newColor.getBlue, newColor.getAlpha)
            }
            doc.insertString(targetStart, target, null);
            inSliderChange = false
            oldColor = newColor
            Utils.runLaterInSwingThread {
              ctx.runIpmCode(doc.getText(0, doc.getLength))
            }
          }
        }
      }
    })
    panel.add(cc, BorderLayout.CENTER)
    val closeAction = new AbstractAction {
      def actionPerformed(e: ActionEvent): Unit = {
        close()
        ctx.activateEditor()
      }
    }
    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "esc")
    panel.getActionMap.put("esc", closeAction)

    colorPopup = new JDialog(ctx.frame)
    colorPopup.getContentPane.add(panel)
    colorPopup.pack()
    colorPopup.setLocationRelativeTo(ctx.codePane)
    colorPopup.setVisible(true)
  }

  def close(): Unit = {
    if (colorPopup != null) {
      colorPopup.setVisible(false)
      colorPopup = null
      ctx.removeManipulator(this)
    }
  }
}