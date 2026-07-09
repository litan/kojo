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

import net.kogics.kojo.core.CompletionInfo
import net.kogics.kojo.core.MemberKind

abstract class CompilerAndRunnerTestBase {
  private val CompletionMarker = "@@"

  def settings = {
    val settings0 = new Settings()
    settings0.usejavacp.value = true
    settings0
  }

  var errLine = 0
  var errColumn = 0
  var errOffset = 0

  val listener = new CompilerListener {
    def error(msg: String, line: Int, column: Int, offset: Int, lineContent: String): Unit = {
      errLine = line
      errColumn = column
      errOffset = offset
    }

    def warning(msg: String, line: Int, column: Int): Unit = {
    }

    def info(msg: String, line: Int, column: Int): Unit = {
    }

    def message(msg: String): Unit = {
    }
  }

  val runner = makeRunner()

  def makeRunner(): CompilerAndRunner

  def completionsAt(
      codeWithMarker: String,
      selection: Boolean,
      prefix: String = ""
  ): List[CompletionInfo] = {
    val markerOffset = codeWithMarker.indexOf(CompletionMarker)
    assertTrue("missing completion marker", markerOffset >= 0)
    assertEquals("multiple completion markers", markerOffset, codeWithMarker.lastIndexOf(CompletionMarker))

    val codeWithPrefix = codeWithMarker.patch(markerOffset, "", CompletionMarker.length)
    val offset = markerOffset - prefix.length
    assertTrue("completion prefix extends before start of source", offset >= 0)
    if (prefix.nonEmpty) {
      assertEquals("prefix must appear immediately before marker", prefix, codeWithPrefix.substring(offset, markerOffset))
    }

    val code =
      if (prefix.nonEmpty) codeWithPrefix.substring(0, offset).concat(codeWithPrefix.substring(markerOffset))
      else codeWithPrefix

    runner.completions(code, offset, selection, prefix)
  }

  def completionNamed(completions: List[CompletionInfo], name: String): CompletionInfo = {
    completions.find(_.name == name).getOrElse {
      fail(s"Missing completion: $name\nGot: ${completions.map(_.name).sorted.mkString(", ")}")
      null.asInstanceOf[CompletionInfo]
    }
  }

  @Before
  def reset(): Unit = {
    errLine = 0
    errColumn = 0
    errOffset = 0
  }

  @Test
  def testError1(): Unit = {
    val code = """val x = 10y
      """
    runner.compileForRunning(code)
    assertEquals(1, errLine)
    assertEquals(9, errColumn)
    assertEquals(8, errOffset)
  }

  @Test
  def testError2(): Unit = {
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
    runner.compileForRunning(code)
    assertEquals(10, errLine)
    assertEquals(5, errColumn)
    assertEquals(215, errOffset)
  }

  @Test
  def testError3(): Unit = {
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
    runner.compileForRunning(code)
    assertEquals(8, errLine)
    assertEquals(14, errColumn)
    assertEquals(255, errOffset)
  }

  @Test
  def testError4(): Unit = {
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
    runner.compileForRunning(code)
    assertEquals(79, errLine)
    assertEquals(4, errColumn)
    assertEquals(1294, errOffset)
  }

  @Test
  def testGood(): Unit = {
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
    runner.compileForRunning(code)
    assertEquals(0, errLine)
    assertEquals(0, errColumn)
  }

  @Test
  def testStaging(): Unit = {
    val code = """import Staging._
import Staging.{clear, setPenColor, animate, circle}
clear()

setPenColor(blue)
val c = circle(0, 0, 50)
animate {
    c.translate(4, 0)
}
    """

    runner.compileForRunning(code)
    assertEquals(0, errLine)
    assertEquals(0, errColumn)
  }

  @Test
  def testJavaMethodCompletionKind(): Unit = {
    val code = """val s = "hello"
s."""

    val cs = runner.completions(code, code.length, selection = true, prefix = "sub")
    val substring = cs.find(_.name == "substring")

    assertTrue(substring.isDefined)
    assertEquals(MemberKind.Def, substring.get.kind)
  }

  @Test
  def testVarCompletionKind(): Unit = {
    val code = "var count = 1\n"

    val cs = runner.completions(code, code.length, selection = false, prefix = "co")
    val count = cs.find(_.name == "count")

    assertTrue(count.isDefined)
    assertEquals(MemberKind.Var, count.get.kind)
  }

  @Test
  def testMemberCompletionsForUserClassMethods(): Unit = {
    val code = """
class CompletionTestX {
    def m1 = 20
    def m2(n: Int) = n * 20
    val v1 = 10.0
    var v2 = 11.0

    class XC
    object XO
    trait XT

    def m3(narg: Int)(d: Double) = {
        def nested(n2: Int) = 5
        n
    }

    def m4[T](t: T)(d: Double) = 5
}

val completionTestX = new CompletionTestX
completionTestX.m@@
"""

    val cs = completionsAt(code, selection = true, prefix = "m")

    assertEquals(MemberKind.Def, completionNamed(cs, "m1").kind)
    assertEquals(MemberKind.Def, completionNamed(cs, "m2").kind)
    assertEquals(MemberKind.Def, completionNamed(cs, "m3").kind)
    assertEquals(MemberKind.Def, completionNamed(cs, "m4").kind)
  }

  @Test
  def testMemberCompletionsForUserClassValuesAndTypes(): Unit = {
    val code = """
class CompletionTestY {
    def m1 = 20
    val v1 = 10.0
    var v2 = 11.0

    class XC
    object XO
    trait XT
}

val completionTestY = new CompletionTestY
completionTestY.@@
"""

    val cs = completionsAt(code, selection = true)

    assertEquals(MemberKind.Val, completionNamed(cs, "v1").kind)
    assertEquals(MemberKind.Var, completionNamed(cs, "v2").kind)
    assertEquals(MemberKind.Class, completionNamed(cs, "XC").kind)
    assertEquals(MemberKind.Object, completionNamed(cs, "XO").kind)
    assertEquals(MemberKind.Trait, completionNamed(cs, "XT").kind)
  }

  @Test
  def testScopeCompletionsForTopLevelFunction(): Unit = {
    val code = """
class CompletionTestZ {
    def m1 = 20
}

def completionSquare(size: Int) {}

val completionTestZ = new CompletionTestZ
completionSq@@
"""

    val cs = completionsAt(code, selection = false, prefix = "completionSq")

    assertEquals(MemberKind.Def, completionNamed(cs, "completionSquare").kind)
  }

  @Test
  def testScopeCompletionsInsideMethodIncludeParamsAndNestedDefs(): Unit = {
    val code = """
class CompletionTestNested {
    def m3(narg: Int)(d: Double) = {
        def nested(n2: Int) = 5
        n@@
    }
}
"""

    val cs = completionsAt(code, selection = false, prefix = "n")

    assertEquals(MemberKind.Val, completionNamed(cs, "narg").kind)
    assertEquals(MemberKind.Def, completionNamed(cs, "nested").kind)
  }

  @Test
  def testPictureBuilderCompletions(): Unit = {
    val code = """
cleari()

def completionPic = {
    Picture.rectangle(100, 50)
        .with@@
        .withFillColor(green)
}.withPenThickness(4)

drawCentered(completionPic)
"""

    val cs = completionsAt(code, selection = true, prefix = "with")

    assertEquals(MemberKind.Def, completionNamed(cs, "withPenColor").kind)
    assertEquals(MemberKind.Def, completionNamed(cs, "withFillColor").kind)
  }

  @Test
  def testPictureCompletionAfterBlockExpression(): Unit = {
    val code = """
cleari()

def completionPic = {
    Picture.rectangle(100, 50)
        .withPenColor(blue)
        .withFillColor(green)
}.with@@

drawCentered(completionPic)
"""

    val cs = completionsAt(code, selection = true, prefix = "with")

    assertEquals(MemberKind.Def, completionNamed(cs, "withPenThickness").kind)
  }
}
