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
package xscala

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import scala.tools.nsc.Settings

abstract class CompilerAndRunnerTestBase {

  def settings = {
    val settings0 = new Settings()
    settings0.usejavacp.value = true
    settings0
  }

  var errLine = 0
  var errColumn = 0
  var errOffset = 0

  val listener = new CompilerListener {
    def error(msg: String, line: Int, column: Int, offset: Int, lineContent: String) {
      errLine = line
      errColumn = column
      errOffset = offset
    }

    def warning(msg: String, line: Int, column: Int) {
    }

    def info(msg: String, line: Int, column: Int) {
    }

    def message(msg: String) {
    }
  }

  val runner = makeRunner()

  def makeRunner(): CompilerAndRunner

  @Before
  def reset() {
    errLine = 0
    errColumn = 0
    errOffset = 0
  }

  @Test
  def testError1() {
    val code = """val x = 10y
      """
    runner.compile(code)
    assertEquals(1, errLine)
    assertEquals(9, errColumn)
    assertEquals(8, errOffset)
  }

  @Test
  def testError2() {
    val code = """class BaseData {
    val baseList = List(1,2,3)
}

class Data extends BaseData {
    val someInt = 9
    val someDouble = 2.3
    val array = Array(1,2,3,4)
    val arrayList = new java.util.ArrayList[String]()
    arrayList2.add("a"); arrayList.add("b"); arrayList.add("c")
}

val data = new Data()
// inspect(data)
      """
    runner.compile(code)
    assertEquals(10, errLine)
    assertEquals(5, errColumn)
    assertEquals(215, errOffset)
  }

  @Test
  def testError3() {
    val code = """def tree(distance: Double) {
    if (distance > 4) {
        setPenThickness(distance/7)
        setPenColor(color(distance.toInt, Math.abs(255-distance*3).toInt, 125))
        forward(distance)
        right(25)
        tree(distance*0.8-2)
        left("45")
        tree(distance-10)
        right(20)
        back(distance)
    }
}

clear()
invisible()
setAnimationDelay(10)
penUp()
back(200)
penDown()
tree(90)
      """
    runner.compile(code)
    assertEquals(8, errLine)
    assertEquals(14, errColumn)
    assertEquals(255, errOffset)
  }

  @Test
  def testError4() {
    val code = """def border(t: Turtle, a: Double) {
    t.setAnimationDelay(200)
    t.setPenColor(black)
    t.right()
    t.forward(1200)
    repeat(15){
        t.setFillColor(red)
        t.turn(a)
        t.forward(40)
        t.turn(a)
        t.forward(40)
        t.turn(a)

        t.setFillColor(blue)
        t.turn(a)
        t.forward(40)
        t.turn(a)
        t.forward(40)
        t.turn(a)
    }
}

def flower(tt:Turtle, c:Color) {
    tt.setAnimationDelay(400)
    tt.setPenColor(black)
    tt.setFillColor(c)
    repeat(4){
        tt.right()
        repeat(90){
            tt.turn(-2)
            tt.forward(2)
        }
    }
}

clear()

val t1=newTurtle(-600,-150)
val t2=newTurtle(-600, 150)

border(t1,120)
border(t2,-120)


jumpTo(-50,100)
setAnimationDelay(20)
setPenColor(black)
setFillColor(green)
repeat(6){
    turn(-120)
    repeat(90){
        turn(-2)
        forward(2)
    }
}

val t3=newTurtle(-300,100)
val t4=newTurtle(-400,0)
val t5=newTurtle(-500,100)
val t6=newTurtle(-600,0)

val t7=newTurtle(200,100)
val t8=newTurtle(300,0)
val t9=newTurtle(400,100)
val t10=newTurtle(500,0)

flower(t3, orange)
flower(t4, yellow)
flower(t5, red)
flower(t6, purple)

flower(t7, orange)
flower(t8, yellow)
flower(t9, red)
flower(t10, purple)

turtle0.invisible()
t1.invisible()
t2..invisible()
t3.invisible()
t4.invisible()
t5.invisible()
t6.invisible()
t7.invisible()
t8.invisible()
t9.invisible()
t10.invisible()
      """
    runner.compile(code)
    assertEquals(79, errLine)
    assertEquals(4, errColumn)
    assertEquals(1294, errOffset)
  }

  @Test
  def testGood() {
    val code = """def border(t: Turtle, a: Double) {
    t.setAnimationDelay(200)
    t.setPenColor(black)
    t.right()
    t.forward(1200)
    repeat(15){
        t.setFillColor(red)
        t.turn(a)
        t.forward(40)
        t.turn(a)
        t.forward(40)
        t.turn(a)

        t.setFillColor(blue)
        t.turn(a)
        t.forward(40)
        t.turn(a)
        t.forward(40)
        t.turn(a)
    }
}

def flower(tt:Turtle, c:Color) {
    tt.setAnimationDelay(400)
    tt.setPenColor(black)
    tt.setFillColor(c)
    repeat(4){
        tt.right()
        repeat(90){
            tt.turn(-2)
            tt.forward(2)
        }
    }
}

clear()

val t1=newTurtle(-600,-150)
val t2=newTurtle(-600, 150)

border(t1,120)
border(t2,-120)


jumpTo(-50,100)
setAnimationDelay(20)
setPenColor(black)
setFillColor(green)
repeat(6){
    turn(-120)
    repeat(90){
        turn(-2)
        forward(2)
    }
}

val t3=newTurtle(-300,100)
val t4=newTurtle(-400,0)
val t5=newTurtle(-500,100)
val t6=newTurtle(-600,0)

val t7=newTurtle(200,100)
val t8=newTurtle(300,0)
val t9=newTurtle(400,100)
val t10=newTurtle(500,0)

flower(t3, orange)
flower(t4, yellow)
flower(t5, red)
flower(t6, purple)

flower(t7, orange)
flower(t8, yellow)
flower(t9, red)
flower(t10, purple)

turtle0.invisible()
t1.invisible()
t2.invisible()
t3.invisible()
t4.invisible()
t5.invisible()
t6.invisible()
t7.invisible()
t8.invisible()
t9.invisible()
t10.invisible()
      """
    runner.compile(code)
    assertEquals(0, errLine)
    assertEquals(0, errColumn)
  }

  @Test
  def testStaging() {
    val code = """import Staging._
import Staging.{clear, setPenColor, animate, circle}
clear()

setPenColor(blue)
val c = circle(0, 0, 50)
animate {
    c.translate(4, 0)
}
    """

    runner.compile(code)
    assertEquals(0, errLine)
    assertEquals(0, errColumn)
  }
}
