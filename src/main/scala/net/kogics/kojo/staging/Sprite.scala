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
package staging

import util.Utils
import core._

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._

object Sprite {
  def apply(p1: Point, fname: String) = {
    if (!new java.io.File(fname).exists) {
      throw new IllegalArgumentException("Unknown Sprite Filename: " + fname)
    }

    Utils.runInSwingThreadAndWait {
      val shape = new Sprite(p1, fname)
      Impl.figure0.pnode(shape.node)
      shape
    }
  }
}

class Sprite(val origin: Point, fname: String) extends BaseShape {
  val image =
    new PImage(fname)
  val imageHolder = new PNode

  val width = image.getWidth
  val height = image.getHeight
  
  image.getTransformReference(true).setToScale(1, -1)
  image.setOffset(-width/2, height/2)

  imageHolder.addChild(image)
  imageHolder.setOffset(origin.x, origin.y)

  def node = imageHolder

  override def toString = "Staging.Image(" + origin + ", " + fname + ")"
}
