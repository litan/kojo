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

package net.kogics.kojo
package livecoding

import util.Utils
import java.awt.Color
import java.awt.Point
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.AbstractAction
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider
import javax.swing.JTextField
import javax.swing.JToggleButton
import javax.swing.KeyStroke
import javax.swing.Popup
import javax.swing.PopupFactory
import javax.swing.SwingUtilities
import javax.swing.text.JTextComponent
import javax.swing.text.Utilities
import java.util.regex.Matcher

abstract class NumberManipulator(ctx: ManipulationContext) extends InteractiveManipulator {
  var target = ""
  var targetStart = 0
  var targetEnd = 0

  var numberTweakPopup: Popup = _
  var stepT: JTextField = _
  var slider: JSlider = _
  var stepB: JButton = _
  var inSliderChange = false

  def isAbsent = numberTweakPopup == null
  def isPresent = !isAbsent

  def matcher(possibleNumber: String): Matcher

  def isHyperlinkPoint(pane: JTextComponent, offset: Int): Boolean = {
    try {
      val wordStart = Utilities.getWordStart(pane, offset)
      val wordEnd = Utilities.getWordEnd(pane, offset)

      if (wordStart == wordEnd) {
        false
      }
      else {
        val possibleNumber = pane.getDocument.getText(wordStart, wordEnd - wordStart)
        val m = matcher(possibleNumber)
        if (m.matches()) {
          val possibleMinus = pane.getDocument.getText(wordStart - 1, 1)
          if (possibleMinus == "-") {
            target = "-" + possibleNumber
            targetStart = wordStart - 1
          }
          else {
            target = possibleNumber
            targetStart = wordStart
          }
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

  def close() {
    if (numberTweakPopup != null) {
      numberTweakPopup.hide()
      numberTweakPopup = null
      stepT = null
      slider = null
      stepB = null
      ctx.removeManipulator(this)
    }
  }

  def showPopup(offset: Int,
    leftLabel: JLabel,
    slider0: JSlider,
    rightLabel: JLabel,
    zoomListener: JToggleButton => Unit,
    stepListener: Option[(JTextField, JToggleButton) => Unit]) {
    slider = slider0
    val factory = PopupFactory.getSharedInstance();
    val rect = ctx.codePane.modelToView(offset)
    val pt = new Point(rect.x, rect.y)
    SwingUtilities.convertPointToScreen(pt, ctx.codePane)
    implicit val klass = getClass
    val bluish = new Color(0, 78, 101)
    val zoomB = new JToggleButton("\u20aa")
    zoomB.setForeground(bluish)
    stepB = new JButton("\u0428")
    stepB.setForeground(bluish)
    stepT = new JTextField(4)
    stepT.setToolTipText(Utils.loadString("CTL_Input"))
    zoomB.setToolTipText(Utils.loadString("CTL_Decrease"))
    zoomB.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        zoomListener(zoomB)
        if (zoomB.isSelected) {
          zoomB.setToolTipText(Utils.loadString("CTL_Increase"))
          stepB.setForeground(null)
          stepB.setEnabled(false)
          stepT.setEnabled(false)
        }
        else {
          zoomB.setToolTipText(Utils.loadString("CTL_Decrease"))
          stepB.setForeground(bluish)
          stepB.setEnabled(true)
          stepT.setEnabled(true)
        }
        focusSlider()
      }
    })
    zoomListener(zoomB)

    val panel = new JPanel()
    panel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1))

    stepListener.foreach { stepL =>
      val stepP = new JPanel
      stepP.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1))
      stepP.add(stepT)
      stepB.setToolTipText(Utils.loadString("CTL_Change"))
      stepP.add(stepB)
      val al = new ActionListener {
        def actionPerformed(e: ActionEvent) {
          stepL(stepT, zoomB)
          focusSlider()
        }
      }
      stepB.addActionListener(al)
      stepT.addActionListener(al)
      panel.add(stepP)
    }

    panel.add(zoomB)
    panel.add(leftLabel)
    panel.add(slider)
    panel.add(rightLabel)
    panel.add(new JLabel(" " * 10))

    val closeAction = new AbstractAction {
      def actionPerformed(e: ActionEvent) {
        close()
        ctx.activateEditor()
      }
    }
    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "esc")
    panel.getActionMap.put("esc", closeAction)

    numberTweakPopup = factory.getPopup(ctx.codePane, panel, pt.x - 50, pt.y - (rect.height * 3.5).toInt)
    numberTweakPopup.show()
    //    Utils.schedule(0.3) {
    //      slider.requestFocus()
    //    }
  }

  def focusSlider() {
    slider.requestFocusInWindow()
  }

  def simulateStepButtonClick() {
    stepB.getActionListeners.foreach { al =>
      al.actionPerformed(null)
    }
  }
}
