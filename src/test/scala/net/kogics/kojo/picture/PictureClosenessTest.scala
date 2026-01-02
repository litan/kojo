/*
 * Copyright (C) 2009-2012 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.kojo.picture

import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.lite.NoOpKojoCtx
import net.kogics.kojo.picture
import net.kogics.kojo.xscala.RepeatCommands
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PictureClosenessTest extends FunSuite with Matchers with RepeatCommands {

  val kojoCtx = new NoOpKojoCtx
  implicit val spriteCanvas: SpriteCanvas = new SpriteCanvas(kojoCtx)

  object Picture {
    def rectangle(width: Double, height: Double) = picture.rect2(width, height)
    def ellipse(xRadius: Double, yRadius: Double) = picture.ellipse(xRadius, yRadius)
  }

  test("Rectangle picture closeness") {
    val pic1 = Picture.rectangle(50, 50)
    val pic2 = Picture.rectangle(50, 50)
    pic2.setPosition(100, 0)
    pic1.draw()
    pic2.draw()
    // gap between them is now 50
    assert(!pic1.isCloser(pic2, 50))
    assert(pic1.isCloser(pic2, 50.1))
  }

  test("Ellipse picture closeness") {
    val pic1 = Picture.ellipse(50, 50)
    val pic2 = Picture.ellipse(50, 50)
    pic2.setPosition(150, 0)
    pic1.draw()
    pic2.draw()
    // gap between them is now 50
    assert(!pic1.isCloser(pic2, 50))
    assert(pic1.isCloser(pic2, 50.1))
  }

  test("Rectangle picture closeness with scaling") {
    val pic1 = Picture.rectangle(50, 50)
    pic1.scale(1.1)
    val pic2 = Picture.rectangle(50, 50)
    pic2.setPosition(100, 0)
    pic1.draw()
    pic2.draw()
    // gap between them is now less than 50
    assert(pic1.isCloser(pic2, 50))
  }

  test("Ellipse picture closeness with scaling") {
    val pic1 = Picture.ellipse(50, 50)
    pic1.scale(1.1)
    val pic2 = Picture.ellipse(50, 50)
    pic2.setPosition(150, 0)
    pic1.draw()
    pic2.draw()
    // gap between them is now less than 50
    assert(pic1.isCloser(pic2, 50))
  }

  test("Rectangle picture closeness with rotation") {
    val pic1 = Picture.rectangle(50, 50)
    pic1.rotate(5)
    val pic2 = Picture.rectangle(50, 50)
    pic2.setPosition(100, 0)
    pic1.draw()
    pic2.draw()
    // gap between them is now greater than 50.1
    assert(!pic1.isCloser(pic2, 50.1))
  }

  test("Ellipse picture closeness with rotation") {
    val pic1 = Picture.ellipse(50, 50)
    pic1.rotate(5)
    val pic2 = Picture.ellipse(50, 50)
    pic2.setPosition(150, 0)
    // gap between them is now still 50
    pic1.draw()
    pic2.draw()
    assert(!pic1.isCloser(pic2, 50))
    assert(pic1.isCloser(pic2, 50.1))
  }
}
