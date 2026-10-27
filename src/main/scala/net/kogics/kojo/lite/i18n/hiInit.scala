/*
 * Copyright (C) 2013
 *   Bjorn Regnell <bjorn.regnell@cs.lth.se>,
 *   Lalit Pant <pant.lalit@gmail.com>
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

// Hindi Turtle wrapper for Kojo

package net.kogics.kojo.lite.i18n

import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.xscala.RepeatCommands

object HindiAPI {
  import net.kogics.kojo.core.Turtle
  import java.awt.Color
  var builtins: net.kogics.kojo.lite.CoreBuiltins = _ //unstable reference to module

  trait HindiTurtle {
    def englishTurtle: Turtle
    def पोंछो() = englishTurtle.clear()
    def visible() = englishTurtle.visible()
    def अदृश्य() = englishTurtle.invisible()
    def आगे(कदम: Double) = englishTurtle.forward(कदम)
    def आगे() = englishTurtle.forward(25)
    def दाएँ(कोण: Double) = englishTurtle.right(कोण)
    def दाएँ() = englishTurtle.right(90)
    def बाएँ(कोण: Double) = englishTurtle.left(कोण)
    def बाएँ() = englishTurtle.left(90)
    def jumpTo(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    def moveTo(x: Double, y: Double) = englishTurtle.moveTo(x, y)
    def कूदो(n: Double) = {
      englishTurtle.saveStyle() //to preserve pen state
      englishTurtle.hop(n) //hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def कूदो(): Unit = कूदो(25)
    def home() = englishTurtle.home()
    def towards(x: Double, y: Double) = englishTurtle.towards(x, y)
    def दिशा(कोण: Double) = englishTurtle.setHeading(कोण)
    def heading = englishTurtle.heading
    def east() = englishTurtle.setHeading(0)
    def west() = englishTurtle.setHeading(180)
    def north() = englishTurtle.setHeading(90)
    def south() = englishTurtle.setHeading(-90)
    def setAnimationDelay(n: Long) = englishTurtle.setAnimationDelay(n)
    def लिखो(t: Any) = englishTurtle.write(t)
    def setPenFontSize(size: Int) = englishTurtle.setPenFontSize(size)
    def arc(radius: Double, कोण: Double) = englishTurtle.arc(radius, math.round(कोण).toInt)
    def circle(radius: Double) = englishTurtle.circle(radius)
    def स्थान = englishTurtle.position
    def penDown() = englishTurtle.penDown()
    def penUp() = englishTurtle.penUp()
    def isPenDown = englishTurtle.style.down
    def कलम_का_रंग(color: java.awt.Color) = englishTurtle.setPenColor(color)
    def रंग_भरो(color: java.awt.Color) = englishTurtle.setFillColor(color)
    def setPenThickness(n: Double) = englishTurtle.setPenThickness(n)
    def saveStyle() = englishTurtle.saveStyle()
    def restoreStyle() = englishTurtle.restoreStyle()
    def savePosHe() = englishTurtle.savePosHe()
    def restorePosHe() = englishTurtle.restorePosHe()
    def beamsOn() = englishTurtle.beamsOn()
    def beamsOff() = englishTurtle.beamsOff()
    def setCostume(fileName: String) = englishTurtle.setCostume(fileName)
    def setCostumes(fileName: String*) = englishTurtle.setCostumes(fileName: _*)
    def nextCostume() = englishTurtle.nextCostume()
  }
  class कछुआ(override val englishTurtle: Turtle) extends HindiTurtle {
    def this(startX: Double, startY: Double, costumeFileName: String) = this(builtins.TSCanvas.newTurtle(startX, startY, costumeFileName))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0, 0)
  }
  class कछुआ0(t0: => Turtle) extends HindiTurtle { //by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object कछुआ extends कछुआ0(builtins.TSCanvas.turtle0)
  def पोंछो() = builtins.TSCanvas.clear()
  def clearOutput() = builtins.clearOutput()
  lazy val blue = builtins.blue
  lazy val red = builtins.red
  lazy val yellow = builtins.yellow
  lazy val green = builtins.green
  lazy val purple = builtins.purple
  lazy val pink = builtins.pink
  lazy val brown = builtins.brown
  lazy val black = builtins.black
  lazy val white = builtins.white
  lazy val noColor = builtins.noColor
  def setBackground(c: Color) = builtins.setBackground(c)
  def setBackgroundV(c1: Color, c2: Color) = builtins.TSCanvas.setBackgroundV(c1, c2)

  //  object KcSwe { //Key codes for Swedish keys
  //    lazy val VK_Å = 197
  //    lazy val VK_Ä = 196
  //    lazy val VK_Ö = 214
  //  }

  //loops
  def दोहराएं(n: Int)(block: => Unit): Unit = {
    RepeatCommands.repeat(n) { block }
  }

  def repeati(n: Int)(block: Int => Unit): Unit = {
    RepeatCommands.repeati(n) { i => block(i) }
  }

  def repeatWhile(condition: => Boolean)(block: => Unit): Unit = {
    RepeatCommands.repeatWhile(condition) { block }
  }

  def repeatFor[T](sequence: Iterable[T])(block: T => Unit): Unit = {
    RepeatCommands.repeatFor(sequence) { block }
  }

  //simple IO
  def readln(prompt: String = "") = builtins.readln(prompt)

  def पंक्ति_छापें(data: Any) = println(data) //Transferred here from sv.tw.kojo.
  def पंक्ति_छापें() = println()

  //math functions
  def round(number: Number, digits: Int = 0): Double = {
    val faktor = math.pow(10, digits).toDouble
    math.round(number.doubleValue * faktor).toLong / faktor
  }
  def random(upperBound: Int) = builtins.random(upperBound)
  def randomDouble(upperBound: Int) = builtins.randomDouble(upperBound)

  //some type aliases in Swedish
  type पूर्णांक = Int
  type अंक = Double
  type लिखित = String
}

object HindiInit {
  def preInit(): Unit = LanguageEditorDataRegistry.register(
    LanguageEditorData(keywords = List("मान", "चर", "परिभाषा"))
  )

  def init(builtins: CoreBuiltins): Unit = {
    //initialize unstable value
    HindiAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Welcome to Kojo with the Swedish turtle!")
        if (b.isScratchPad) {
          println("History will not be saved when you close Kojo Scratchpad.")
        }

        //        b.setEditorTabSize(2)

        //code completion
        b.addCodeTemplates(
          "hi",
          codeTemplates
        )
        //help texts
        b.addHelpContent(
          "hi",
          helpContent
        )

      case _ =>
    }
  }

  val codeTemplates = Map(
    "आगे" -> "आगे(${कदम})",
    "दाएँ" -> "दाएँ(${कोण})",
    "बाएँ" -> "बाएँ(${कोण})",
    "jumpTo" -> "jumpTo(${x},${y})",
    "moveTo" -> "moveTo(${x},${y})",
    "कूदो" -> "कूदो(${कदम})",
    "home" -> "home()",
    "towards" -> "towards(${x},${y})",
    "दिशा" -> "दिशा(${कोण})",
    "east" -> "east()",
    "west" -> "west()",
    "north" -> "north()",
    "south" -> "south()",
    "setAnimationDelay" -> "setAnimationDelay(${milliSeconds})",
    "लिखो" -> "लिखो(${text})",
    "setPenFontSize" -> "setPenFontSize(${size})",
    "arc" -> "arc(${radius},${कोण})",
    "circle" -> "circle(${radius})",
    "visible" -> "visible()",
    "अदृश्य" -> "अदृश्य()",
    "penDown" -> "penDown()",
    "penUp" -> "penUp()",
    "isPenDown" -> "isPenDown",
    "कलम_का_रंग" -> "कलम_का_रंग(${color})",
    "रंग_भरो" -> "रंग_भरो(${color})",
    "setPenThickness" -> "setPenThickness(${width})",
    "saveStyle" -> "saveStyle()",
    "restoreStyle" -> "restoreStyle()",
    "savePosHe" -> "savePosHe()",
    "restorePosHe" -> "restorePosHe()",
    "beamsOn" -> "beamsOn()",
    "beamsOff" -> "beamsOff()",
    "पोंछो" -> "पोंछो()",
    "clearOutput" -> "clearOutput()",
    "setBackground" -> "setBackground(${color})",
    "setBackgroundV" -> "setBackgroundV(${color1},${color2})",
    "दोहराएं" -> "दोहराएं(${count}) {\n    ${cursor}\n}",
    "repeati" -> "repeati(${count}) { i =>\n    ${cursor}\n}",
    "repeatWhile" -> "repeatWhile(${condition}) {\n    ${cursor}\n}",
    "repeatFor" -> "repeatFor(${sequence}) { ${e} =>\n    ${cursor}\n}",
    "पंक्ति_छापें" -> "पंक्ति_छापें(${text})",
    "readln" -> "readln(${prompt})",
    "round" -> "round(${number},${digits})",
    "random" -> "random(${upperBound})",
    "randomDouble" -> "randomDouble(${upperBound})",
    "setCostume" -> "setCostume(${fileName})",
    "setCostumes" -> "setCostumes(${fileName1},${fileName2})",
    "nextCostume" -> "nextCostume()"
  )

  val helpContent = Map(
    "आगे" -> <div><strong>forward</strong>(numSteps) - Moves the turtle forward by the given number of steps.</div>.toString,
    "बाएँ" -> <div> <strong>left</strong>() - Turns the turtle 90 degrees left (anti-clockwise). <br/> <strong>left</strong>(angle) - Turns the turtle left (anti-clockwise) through the given angle in degrees.<br/> <strong>left</strong>(angle, radius) - Turns the turtle left (anti-clockwise) through the given angle in degrees, around the given turning radius.<br/> </div>.toString,
    "दाएँ" -> <div> <strong>right</strong>() - Turns the turtle 90 degrees right (clockwise). <br/> <strong>right</strong>(angle) - Turns the turtle right (clockwise) through the given angle in degrees.<br/> <strong>right</strong>(angle, radius) - Turns the turtle right (clockwise) through the given angle in degrees, around the given turning radius.<br/> </div>.toString,
    "jumpTo" -> <div> <strong>jumpTo</strong>(x, y) - Places the turtle at the point (x, y) without drawing a line. The turtle's heading is not changed. <br/> </div>.toString,
    "moveTo" -> <div><strong>moveTo</strong>(x, y) - Turns the turtle towards (x, y) and moves the turtle to that point.</div>.toString,
    "कूदो" -> <div> <strong>hop</strong>(numSteps) - Moves the turtle forward by the given number of steps <em>with the pen up</em>, so that no line is drawn. The pen is put down after the hop. <br/> </div>.toString,
    "home" -> <div><strong>home</strong>() - Moves the turtle to its original location at the center of the screen, and makes it point north.</div>.toString,
    "towards" -> <div><strong>towards</strong>(x, y) - Turns the turtle towards the point (x, y).</div>.toString,
    "दिशा" -> <div><strong>setHeading</strong>(angle) - Sets the turtle's heading to the given angle (0 is towards the right side of the screen (<em>east</em>), 90 is up (<em>north</em>)).</div>.toString,
    "heading" -> <div><strong>heading</strong> - Queries the turtle's heading (0 is towards the right side of the screen (<em>east</em>), 90 is up (<em>north</em>)).</div>.toString,
    "east" -> <div><strong>east</strong>() - Turns the turtle east.</div>.toString,
    "west" -> <div><strong>west</strong>() - Turns the turtle west.</div>.toString,
    "north" -> <div><strong>north</strong>() - Turns the turtle north.</div>.toString,
    "south" -> <div><strong>south</strong>() - Turns the turtle south.</div>.toString,
    "setAnimationDelay" -> <div> <strong>setAnimationDelay</strong>(delay) - Sets the turtle's speed. The specified delay is the amount of time (in milliseconds) taken by the turtle to move through a distance of one hundred steps.<br/> The default delay is 1000 milliseconds (or 1 second).<br/> </div>.toString,
    "लिखो" -> <div><strong>write</strong>(obj) - Makes the turtle write, at its current location, the specified object as a string.</div>.toString,
    "setPenFontSize" -> <div><strong>setPenFontSize</strong>(n) - Specifies the font size of the pen that the turtle writes with.</div>.toString,
    "arc" -> <div> <strong>arc</strong>(radius, angle) - Gets the turtle to make an arc with the given radius and angle.<br/> Positive angles make the turtle go left (anti-clockwise). Negative angles make the turtle go right (clockwise) <br/> </div>.toString,
    "circle" -> <div> <strong>circle</strong>(radius) - Gets the turtle to make a circle with the given radius. <br/> A circle(50) command is equivalent to an arc(50, 360) command.<br/> </div>.toString,
    "visible" -> <div><strong>visible</strong>() - Makes the turtle visible after it was made invisible with the invisible() command.</div>.toString,
    "अदृश्य" -> <div><strong>invisible</strong>() - Makes the turtle invisible. Use the visible() command to make it visible again.</div>.toString,
    "penDown" -> <div> <strong>penDown</strong>() - Puts the turtle's pen down, so that it draws lines as the turtle moves.<br/> The turtle's pen is down by default. <br/> </div>.toString,
    "penUp" -> <div><strong>penUp</strong>() - Pulls the turtle's pen up, so that it does not draw lines as the turtle moves. <br/></div>.toString,
    "isPenDown" -> <div><strong>isPenDown</strong> - Tells you whether the turtle's pen is down.</div>.toString,
    "कलम_का_रंग" -> <div><strong>setPenColor</strong>(color) - Specifies the color of the pen that the turtle draws with. <br/></div>.toString,
    "रंग_भरो" -> <div><strong>setFillColor</strong>(color) - Specifies the fill color of the figures drawn by the turtle. <br/></div>.toString,
    "setPenThickness" -> <div><strong>setPenThickness</strong>(thickness) - Specifies the width of the pen that the turtle draws with. <br/></div>.toString,
    "saveStyle" -> <div> <strong>saveStyle</strong>() - Saves the turtle's current style, so that it can easily be restored later with a <tt>restoreStyle()</tt>. <br/> <p> The turtle's style includes: <ul> <li>Pen Color</li> <li>Pen Thickness</li> <li>Fill color</li> <li>Pen Font</li> <li>Pen Up/Down state</li> </ul> </p> </div>.toString,
    "restoreStyle" -> <div> <strong>restoreStyle</strong>() - Restores the turtle's style based on an earlier <tt>saveStyle()</tt>. <br/> <p> The turtle's style includes: <ul> <li>Pen Color</li> <li>Pen Thickness</li> <li>Fill color</li> <li>Pen Font</li> <li>Pen Up/Down state</li> </ul> </p> </div>.toString,
    "savePosHe" -> <div> <strong>savePosHe</strong>() - Saves the turtle's current position and heading, so that they can easily be restored later with a <tt>restorePosHe()</tt>. <br/> </div>.toString,
    "restorePosHe" -> <div> <strong>restorePosHe</strong>() - Restores the turtle's current position and heading based on an earlier <tt>savePosHe()</tt>. <br/> </div>.toString,
    "beamsOn" -> <div><strong>beamsOn</strong>() - Shows crossbeams centered on the turtle - to help with thinking about the turtle's heading/orientation.</div>.toString,
    "beamsOff" -> <div><strong>beamsOff</strong>() - Hides the turtle crossbeams that are turned on by a <tt>beamsOn()</tt>.</div>.toString,
    "पोंछो" -> <div><strong>clear</strong>() - Clears the turtle canvas, and brings the turtle to the center of the canvas.</div>.toString,
    "clearOutput" -> <div><strong>clearOutput</strong>() - Clears the output window.</div>.toString,
    "setBackground" -> <div> <strong>setBackground</strong>(color) - Sets the canvas background to the specified color. You can use predefined colors for setting the background, or you can create your own colors using the <tt>Color</tt>, <tt>ColorHSB</tt>, and <tt>ColorG</tt> functions. </div>.toString,
    "setBackgroundV" -> <div><strong>setBackgroundV</strong>(color1, color2) - Sets the canvas background to a vertical color gradient defined by the two specified colors. </div>.toString,
    "दोहराएं" -> <div><strong>repeat</strong>(n){{ }} - Repeats the specified block of commands (within braces) n number of times.<br/></div>.toString,
    "repeati" -> <div><strong>repeati</strong>(n) {{i => }} - Repeats the specified block of commands (within braces) n number of times. The current repeat index is available as <tt>i</tt> within the braces. </div>.toString,
    "repeatWhile" -> <div><strong>repeatWhile</strong>(cond) {{ }} - Repeats the specified block of commands (within braces) while the given condition is true.</div>.toString,
    "repeatFor" -> <div><strong>repeatFor</strong>(seq){{ }} - Repeats the specified block of commands (within braces) for each element in the given sequence.<br/></div>.toString,
    "पंक्ति_छापें" -> <div><strong>println</strong>(obj) - Displays the given object as a string in the output window, with a newline at the end.</div>.toString,
    "readln" -> <div><strong>readln</strong>(promptString) - Displays the given prompt in the output window and reads a line that the user enters.</div>.toString,
    "round" -> <div><strong>round</strong>(n, digits) - Rounds the given number n to the specified number of digits after the decimal point.</div>.toString,
    "random" -> <div><strong>random</strong>(upperBound) - Returns a random integer between 0 (inclusive) and upperBound (exclusive).</div>.toString,
    "randomDouble" -> <div><strong>randomDouble</strong>(upperBound) - Returns a random double-precision decimal number between 0 (inclusive) and upperBound (exclusive).</div>.toString,
    "setCostume" -> <div><strong>setCostume</strong>(costumeFile) - Changes the costume (i.e. image) associated with the turtle to the image in the specified file.</div>.toString,
    "setCostumes" -> <div><strong>setCostumes</strong>(costumeFile1, costumeFile2, ...) - Specifies multiple costumes for the turtle, and sets the turtle's costume to the first one in the sequence. You can cycle through the costumes by calling <tt>nextCostume()</tt>. </div>.toString,
    "nextCostume" -> <div><strong>nextCostume</strong>() - Changes the turtle's costume to the next one in the sequence of costumes specified by <tt>setCostumes(...)</tt>.</div>.toString
  )
}
