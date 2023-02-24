package net.kogics.kojo.livecoding

import java.awt.{ Color => JColor }
import java.awt.event.ActionEvent
import java.awt.BorderLayout
import java.awt.Point
import java.util.regex.Pattern
import java.util.Locale
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import javax.swing.text.JTextComponent
import javax.swing.text.Utilities
import javax.swing.AbstractAction
import javax.swing.BorderFactory
import javax.swing.JColorChooser
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.KeyStroke
import javax.swing.PopupFactory
import javax.swing.SwingUtilities

import net.kogics.kojo.doodle.Color
import net.kogics.kojo.util.JUtils
import net.kogics.kojo.util.Utils

class ColorMakerManipulatorHsv(ctx: ManipulationContext) extends InteractiveManipulator {
  var target = ""
  var targetStart = 0
  var targetEnd = 0
  var targetColor: Color = null
  var colorPopup: JDialog = _
  var inSliderChange = false
  def isAbsent = colorPopup == null
  def isPresent = !isAbsent

  lazy val ColorPattern2 =
    Pattern.compile("""(ColorMaker|cm)\.hsb(a)?\((\d+),\s*(\d+\.?\d?\d?),\s*(\d+\.?\d?\d?)(,\s*(\d+\.?\d?\d?))?\)""")
  def matcher2(possibleColorLine: String) = ColorPattern2.matcher(possibleColorLine)

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
        val hsva = Seq(3, 4, 5, 7).map { e =>
          val ret = m.group(e)
          e match {
            case 3 => ret.toInt
            case _ => if (ret != null) ret.toDouble else 1.0
          }
        }
        targetColor = Color.hsba(hsva(0).toInt, hsva(1), hsva(2), hsva(3))
        targetStart = lineStart + start
        targetEnd = lineStart + end
        found = true
      }
    }
    found
  }

  def trimColor(c: String) = {
    val parts = c.split(raw"\.")
    if (parts.length == 2) parts(1) else parts(0)
  }

  def isHyperlinkPoint(pane: JTextComponent, offset: Int): Boolean = {
    try {
      val wordStart = Utilities.getWordStart(pane, offset)
      val wordEnd = Utilities.getWordEnd(pane, offset)

      if (wordStart == wordEnd) {
        false
      }
      else {
        findColorFunction(pane, offset)
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
    panel.setBorder(BorderFactory.createLineBorder(JColor.darkGray, 1))
    panel.setLayout(new BorderLayout)
    val cc = new JColorChooser(targetColor.toAwt)
    val panels = cc.getChooserPanels.toVector
    val hsvPanel = panels.find { p =>
      // a hack, but can't think of anything better
      // need to check HSL text for each new language
      p.getDisplayName.contains("HSV") /* || p.getDisplayName.contains("RVB") */
    }
    hsvPanel match {
      case Some(hslp) =>
        val newps = hslp +: panels.filter { p => p != hslp }
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
            val hsvVals = JColor.RGBtoHSB(newColor.getRed, newColor.getGreen, newColor.getBlue, null)
            val hueAngle = math.round(hsvVals(0) * 360)
            val sat = hsvVals(1)
            val value = hsvVals(2)
            target = if (newColor.getAlpha == 255) {
              Utils.strFormat("ColorMaker.hsb(%d, %.2f, %.2f)", hueAngle, sat, value)
            }
            else {
              Utils.strFormat("ColorMaker.hsba(%d, %.2f, %.2f, %.2f)", hueAngle, sat, value, newColor.getAlpha / 255.0)
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
