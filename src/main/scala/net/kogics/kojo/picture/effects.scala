/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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
package picture

import core.Picture

abstract class Effect extends Transformer {
  def draw() {
    tpic.draw()
  }
}

case class Spin(n: Int)(pic: Picture) extends Effect {
  val tpic = spunP
  def spunP = {
    val lb = new collection.mutable.ListBuffer[Picture]
    lb += pic
    var angle = 360.0 / n
    for (i <- 1 to n-1) {
      lb += rot(angle) -> pic.copy
      angle += 360.0 / n
    }
    GPics(lb.toList)
  }
  
  def copy = Spin(n)(pic.copy)
}

case class Reflect(n: Int)(pic: Picture) extends Effect {
  val tpic = reflectedP
  def reflectedP = {
    HPics(pic, trans(n, 0)(FlipY(pic.copy)))
  }
  
  def copy = Reflect(n)(pic.copy)
}

case class Spinc(n: Int) extends ComposableTransformer {
  def apply(p: Picture) = Spin(n)(p)
}

case class Reflectc(n: Int) extends ComposableTransformer {
  def apply(p: Picture) = Reflect(n)(p)
}
