/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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
package mathworld

import util.Utils

class MwFigure(name: String) extends core.VisualElements {
  val shapes = new collection.mutable.ArrayBuffer[core.VisualElement]()

  def add(newshapes: core.VisualElement*) {
    newshapes.foreach { shape =>
      shapes += shape
    }
  }

  def show {
    Utils.runInSwingThread {
      shapes.foreach {s => s.show}
    }
  }

  def hide {
    Utils.runInSwingThread {
      shapes.foreach {s => s.hide}
    }
  }

  def setColor(color: java.awt.Color) {
    Utils.runInSwingThread {
      shapes.foreach {s => s.setColor(color)}
    }
  }
}
