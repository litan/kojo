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

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._
import net.kogics.kojo.lite.NoOpKojoCtx

class MwTurtleTest {
  val kojoCtx = new NoOpKojoCtx
  val ggbCanvas = new GeoGebraCanvas(kojoCtx)
  val Mw = ggbCanvas.Mw
  Mw._clear2()
  val t = Mw.turtle(0, 0)

  @Test
  def testForward {
    t.labelPosition("A")
    t.forward(1)
    t.labelPosition("B")
    val line = t.findLine("AB")
    assertEquals(0, line.p1.x, 0.001)
    assertEquals(0, line.p1.y, 0.001)
    assertEquals(0, line.p2.x, 0.001)
    assertEquals(1, line.p2.y, 0.001)
  }

  @Test
  def testTurn {
    t.showAngles()
    t.setPosition(1, 1)
    t.labelPosition("A")
    t.forward(1)
    t.labelPosition("B")
    t.right(45)
    t.forward(1)
    t.labelPosition("C")
    val line = t.findLine("AB")
    assertEquals(1, line.p1.x, 0.001)
    assertEquals(1, line.p1.y, 0.001)
    assertEquals(1, line.p2.x, 0.001)
    assertEquals(2, line.p2.y, 0.001)

    val angle = t.findAngle("ABC")
    assertEquals(180-45, angle.size.toDegrees, 0.001)
  }

  @Test
  def testPoly {
    t.showAngles()
    t.beginPoly()
    t.labelPosition("A")
    t.forward(1)
    t.labelPosition("B")
    t.right()
    t.forward(1)
    t.labelPosition("C")
    t.right()
    t.forward(1)
    t.labelPosition("D")
    t.endPoly()
   
    val line = t.findLine("DA")
    assertEquals(1, line.p1.x, 0.001)
    assertEquals(0, line.p1.y, 0.001)
    assertEquals(0, line.p2.x, 0.001)
    assertEquals(0, line.p2.y, 0.001)

    val angle = t.findAngle("DAB")
    assertEquals(90, angle.size.toDegrees, 0.001)
  }
}
