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
package net.kogics.kojo.livecoding

import java.util.regex.Pattern
import javax.swing.JLabel
import javax.swing.JSlider
import javax.swing.JTextField
import javax.swing.JToggleButton
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import javax.swing.text.Document
import net.kogics.kojo.util.Utils
import javax.swing.text.Utilities
import javax.swing.text.JTextComponent

class IntManipulator(ctx: ManipulationContext) extends NumberManipulator(ctx) {
  val IntPattern = Pattern.compile("\\d*")

  def matcher(possibleNumber: String) = IntPattern.matcher(possibleNumber)

  def getHyperlinkSpan(pane: JTextComponent, offset: Int): Array[Int] = {
    Array(targetStart, targetEnd)
  }

  def activate(pane: JTextComponent, offset: Int) {
    activate(pane, offset, target, targetStart)
  }

  def activate(pane: JTextComponent, offset: Int, target0: String, targetStart: Int) = Utils.safeProcess {
    val doc = pane.getDocument
    close()
    ctx.addManipulator(this)
    var target = target0
    var ncenter = target0.toInt
    var ntarget = ncenter
    var delta = math.max(math.floor(ntarget.abs * 2.0 / 10).toInt, 1)
    var oldDelta = delta
    val slider = new JSlider();
    val leftLabel = new JLabel
    val rightLabel = new JLabel

    def slider2num(n: Int): Int = {
      ncenter + (n - 9) * delta
    }
    def num2slider(n: Double): Int = {
      9 + math.round((n - ncenter) / delta).toInt
    }

    def reConfigSlider(around: Int, delta0: Int, zoomB: JToggleButton) {
      ncenter = around
      delta = delta0
      slider.setMinimum(0)
      slider.setMaximum(18)
      slider.setValue(9)
      slider.setMajorTickSpacing(1)
      leftLabel.setText(slider2num(slider.getMinimum).toString)
      rightLabel.setText(slider2num(slider.getMaximum).toString)
    }
    slider.setValue(9)
    slider.setPaintTicks(true)

    var lastrunval = ntarget
    slider.addChangeListener(new ChangeListener {
      def stateChanged(e: ChangeEvent) = Utils.safeProcess {
        stepT.setText(delta.toString)
        val eslider = e.getSource.asInstanceOf[JSlider]
        val newnum = eslider.getValue()
        inSliderChange = true
        doc.remove(targetStart, target.length())
        ntarget = slider2num(newnum)
        target = ntarget.toString
        doc.insertString(targetStart, target, null);
        inSliderChange = false

        if (!eslider.getValueIsAdjusting) {
          // drag over
          if (lastrunval != ntarget) {
            lastrunval = ntarget
            Utils.runLaterInSwingThread {
              ctx.runIpmCode(doc.getText(0, doc.getLength))
            }
          }
          if (newnum == eslider.getMaximum || newnum == eslider.getMinimum) {
            simulateStepButtonClick()
          }
        }
      }
    })

    val zoomListener = { zoomB: JToggleButton =>
      val around = slider2num(slider.getValue)
      if (zoomB.isSelected) {
        oldDelta = delta
        reConfigSlider(around, 1, zoomB)
      }
      else {
        reConfigSlider(around, oldDelta, zoomB)
      }
      stepT.setText(delta.toString)
    }

    val stepListener = { (stepT: JTextField, zoomB: JToggleButton) =>
      try {
        val step = stepT.getText.toInt
        val around = slider2num(slider.getValue)
        reConfigSlider(around, step, zoomB)
      }
      catch {
        case nfe: NumberFormatException =>
          stepT.setText("Err")
      }
    }

    showPopup(offset, leftLabel, slider, rightLabel, zoomListener, Some(stepListener))
  }
}
