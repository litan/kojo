/*
 * Copyright (C) 2012 Jerzy Redlarski <5xinef@gmail.com>
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

package net.kogics.kojo.d3

import javax.swing.JSlider
import java.util.Hashtable
import javax.swing.JLabel
import javax.swing.SwingConstants
import javax.swing.event.ChangeListener
import javax.swing.event.ChangeEvent

class QualitySlider(val canvas : Canvas3D) extends JSlider(SwingConstants.VERTICAL, 0, 30, 15)
	with ChangeListener{

  setMajorTickSpacing(5);
  setMinorTickSpacing(1)
  val labelTable = new Hashtable[Integer, JLabel]
  labelTable.put(new Integer(0), new JLabel("Unlimited"))
  labelTable.put(new Integer(3), new JLabel("High"))
  labelTable.put(new Integer(6), new JLabel("Good"))
  labelTable.put(new Integer(15), new JLabel("Balanced"))
  labelTable.put(new Integer(21), new JLabel("Fast"))
  labelTable.put(new Integer(30), new JLabel("Very fast"))
  setLabelTable(labelTable)
  setPaintLabels(true)
  setPaintTicks(true)
  setSnapToTicks(true)
  addChangeListener(this)
  
  override def stateChanged(e : ChangeEvent) {
    canvas.camera = canvas.camera.setFrequency(getValue())
    if(canvas.renderLock.available)
    	canvas.renderAsynchronous()
  }
}