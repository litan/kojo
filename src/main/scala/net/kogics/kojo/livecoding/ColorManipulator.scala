package net.kogics.kojo.livecoding

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Point
import java.awt.event.ActionEvent
import java.util.regex.Pattern

import javax.swing.AbstractAction
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JColorChooser
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.KeyStroke
import javax.swing.Popup
import javax.swing.PopupFactory
import javax.swing.SwingUtilities
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import javax.swing.text.JTextComponent
import javax.swing.text.Utilities

import net.kogics.kojo.util.Utils

class ColorManipulator(ctx: ManipulationContext) extends InteractiveManipulator {
  var target = ""
  var targetStart = 0
  var targetEnd = 0
  var colorPopup: Popup = _
  var inSliderChange = false
  def isAbsent = colorPopup == null
  def isPresent = !isAbsent

  lazy val ColorPattern = Pattern.compile("""(C\.)?(%s)""" format (ctx.knownColors.mkString("|")))
  def matcher(possibleColor: String) = ColorPattern.matcher(possibleColor)

  def isHyperlinkPoint(pane: JTextComponent, offset: Int): Boolean = {
    try {
      val wordStart = Utilities.getWordStart(pane, offset)
      val wordEnd = Utilities.getWordEnd(pane, offset)

      if (wordStart == wordEnd) {
        false
      }
      else {
        val possibleColor = pane.getDocument.getText(wordStart, wordEnd - wordStart)
        val m = matcher(possibleColor)
        if (m.matches()) {
          target = possibleColor
          targetStart = wordStart
          targetEnd = wordEnd
          true
        }
        else {
          false
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

  def activate(pane: JTextComponent, offset: Int) {
    activate(pane, offset, target, targetStart)
  }

  def activate(pane: JTextComponent, offset: Int, target0: String, targetStart: Int) = Utils.safeProcess {
    showPopup(pane, offset)
  }

  def showPopup(pane: JTextComponent, offset: Int) {
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
    val cc = new JColorChooser
    var oldColor = cc.getColor()
    var lastChangeTime = 0l
    cc.getSelectionModel.addChangeListener(new ChangeListener {
      def stateChanged(e: ChangeEvent) = Utils.safeProcess {
        val currTime = System.currentTimeMillis
        if (currTime - lastChangeTime > 300) {
          lastChangeTime = currTime
          val newColor = cc.getColor()
          if (oldColor != newColor) {
            inSliderChange = true
            doc.remove(targetStart, target.length())
            target = if (newColor.getAlpha == 255) {
              "Color(%d, %d, %d)" format (newColor.getRed, newColor.getGreen, newColor.getBlue)
            }
            else {
              "Color(%d, %d, %d, %d)" format (newColor.getRed, newColor.getGreen, newColor.getBlue, newColor.getAlpha)
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
      def actionPerformed(e: ActionEvent) {
        close()
        ctx.activateEditor()
      }
    }
    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "esc")
    panel.getActionMap.put("esc", closeAction)

    val closeButton = new JButton(closeAction)
    closeButton.setText(Utils.loadString("S_Close"))
    val closePanel = new JPanel
    closePanel.setLayout(new BorderLayout)
    closePanel.add(closeButton, BorderLayout.SOUTH)
    closePanel.setBorder(BorderFactory.createEtchedBorder())
    panel.add(closePanel, BorderLayout.EAST)

    colorPopup = factory.getPopup(ctx.codePane, panel, pt.x + 50, pt.y - 300)
    colorPopup.show()
  }

  def close() {
    if (colorPopup != null) {
      colorPopup.hide()
      colorPopup = null
      ctx.removeManipulator(this)
    }
  }
}