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
    def दिखो() = englishTurtle.visible()
    def छिपो() = englishTurtle.invisible()
    def आगे(कदम: Double) = englishTurtle.forward(कदम)
    def आगे() = englishTurtle.forward(25)
    def दाएँ(कोण: Double) = englishTurtle.right(कोण)
    def दाएँ() = englishTurtle.right(90)
    def बाएँ(कोण: Double) = englishTurtle.left(कोण)
    def बाएँ() = englishTurtle.left(90)
    def छलाँग_लगाओ(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    def यहाँ_जाओ(x: Double, y: Double) = englishTurtle.moveTo(x, y)
    def कूदो(n: Double) = {
      englishTurtle.saveStyle() //to preserve pen state
      englishTurtle.hop(n) //hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def कूदो(): Unit = कूदो(25)
    def घर() = englishTurtle.home()
    def ओर(x: Double, y: Double) = englishTurtle.towards(x, y)
    def दिशा(कोण: Double) = englishTurtle.setHeading(कोण)
    def वर्तमान_दिशा = englishTurtle.heading
    def पूर्व() = englishTurtle.setHeading(0)
    def पश्चिम() = englishTurtle.setHeading(180)
    def उत्तर() = englishTurtle.setHeading(90)
    def दक्षिण() = englishTurtle.setHeading(-90)
    def एनिमेशन_देरी(n: Long) = englishTurtle.setAnimationDelay(n)
    def लिखो(t: Any) = englishTurtle.write(t)
    def अक्षर_का_आकार(आकार: Int) = englishTurtle.setPenFontSize(आकार)
    def चाप(त्रिज्या: Double, कोण: Double) = englishTurtle.arc(त्रिज्या, math.round(कोण).toInt)
    def वृत्त(त्रिज्या: Double) = englishTurtle.circle(त्रिज्या)
    def स्थान = englishTurtle.position
    def कलम_नीचे() = englishTurtle.penDown()
    def कलम_ऊपर() = englishTurtle.penUp()
    def कलम_नीचे_है = englishTurtle.style.down
    def कलम_का_रंग(रंग: java.awt.Color) = englishTurtle.setPenColor(रंग)
    def भरने_का_रंग(रंग: java.awt.Color) = englishTurtle.setFillColor(रंग)
    def कलम_की_मोटाई(n: Double) = englishTurtle.setPenThickness(n)
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
  def पृष्ठभूमि(c: Color) = builtins.setBackground(c)
  def setBackgroundV(c1: Color, c2: Color) = builtins.TSCanvas.setBackgroundV(c1, c2)

  //  object KcSwe { //Key codes for Swedish keys
  //    lazy val VK_Å = 197
  //    lazy val VK_Ä = 196
  //    lazy val VK_Ö = 214
  //  }

  //loops
  def दोहराओ(n: Int)(block: => Unit): Unit = {
    RepeatCommands.repeat(n) { block }
  }

  def दोहराओ_गिनती_सहित(n: Int)(block: Int => Unit): Unit = {
    RepeatCommands.repeati(n) { i => block(i) }
  }

  def दोहराओ_जब_तक(स्थिति: => Boolean)(block: => Unit): Unit = {
    RepeatCommands.repeatWhile(स्थिति) { block }
  }

  def दोहराओ_प्रत्येक[T](क्रम: Iterable[T])(block: T => Unit): Unit = {
    RepeatCommands.repeatFor(क्रम) { block }
  }

  //simple IO
  def readln(संदेश: String = "") = builtins.readln(संदेश)

  def पंक्ति_छापो(data: Any) = println(data) //Transferred here from sv.tw.kojo.
  def पंक्ति_छापो() = println()

  //math functions
  def लगभग(संख्या: Number, अंक: Int = 0): Double = {
    val faktor = math.pow(10, अंक).toDouble
    math.round(संख्या.doubleValue * faktor).toLong / faktor
  }
  def random(upperBound: Int) = builtins.random(upperBound)
  def randomDouble(upperBound: Int) = builtins.randomDouble(upperBound)

  //some type aliases in Swedish
  type पूर्णांक = Int
  type दशमलव_संख्या = Double
  type अक्षरों_की_श्रृंखला = String
}

object HindiInit {
  def preInit(): Unit = LanguageEditorDataRegistry.register(
    LanguageEditorData(hi.keywordList, hi.keywordTemplateMap, hi.codeTemplateMap)
  )

  def init(builtins: CoreBuiltins): Unit = {
    //initialize unstable value
    HindiAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("हिंदी कोजो में आपका स्वागत है!")
        if (b.isScratchPad) {
          println("कोजो अभ्यास पटल बंद करने पर पिछली गतिविधियाँ सेव नहीं होंगी!")
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
    "छलाँग_लगाओ" -> "छलाँग_लगाओ(${x},${y})",
    "यहाँ_जाओ" -> "यहाँ_जाओ(${x},${y})",
    "कूदो" -> "कूदो(${कदम})",
    "घर" -> "घर()",
    "ओर" -> "ओर(${x},${y})",
    "दिशा" -> "दिशा(${कोण})",
    "पूर्व" -> "पूर्व()",
    "पश्चिम" -> "पश्चिम()",
    "उत्तर" -> "उत्तर()",
    "दक्षिण" -> "दक्षिण()",
    "एनिमेशन_देरी" -> "एनिमेशन_देरी(${मिलीसेकंड})",
    "लिखो" -> "लिखो(${text})",
    "अक्षर_का_आकार" -> "अक्षर_का_आकार(${आकार})",
    "चाप" -> "चाप(${त्रिज्या},${कोण})",
    "वृत्त" -> "वृत्त(${त्रिज्या})",
    "दिखो" -> "दिखो()",
    "छिपो" -> "छिपो()",
    "कलम_नीचे" -> "कलम_नीचे()",
    "कलम_ऊपर" -> "कलम_ऊपर()",
    "कलम_नीचे_है" -> "कलम_नीचे_है",
    "कलम_का_रंग" -> "कलम_का_रंग(${रंग})",
    "भरने_का_रंग" -> "भरने_का_रंग(${रंग})",
    "कलम_की_मोटाई" -> "कलम_की_मोटाई(${चौड़ाई})",
    "saveStyle" -> "saveStyle()",
    "restoreStyle" -> "restoreStyle()",
    "savePosHe" -> "savePosHe()",
    "restorePosHe" -> "restorePosHe()",
    "beamsOn" -> "beamsOn()",
    "beamsOff" -> "beamsOff()",
    "पोंछो" -> "पोंछो()",
    "clearOutput" -> "clearOutput()",
    "पृष्ठभूमि" -> "पृष्ठभूमि(${रंग})",
    "setBackgroundV" -> "setBackgroundV(${रंग1},${रंग2})",
    "दोहराओ" -> "दोहराओ(${गिनती}) {\n    ${cursor}\n}",
    "दोहराओ_गिनती_सहित" -> "दोहराओ_गिनती_सहित(${गिनती}) { i =>\n    ${cursor}\n}",
    "दोहराओ_जब_तक" -> "दोहराओ_जब_तक(${स्थिति}) {\n    ${cursor}\n}",
    "दोहराओ_प्रत्येक" -> "दोहराओ_प्रत्येक(${क्रम}) { ${e} =>\n    ${cursor}\n}",
    "पंक्ति_छापो" -> "पंक्ति_छापो(${text})",
    "readln" -> "readln(${संदेश})",
    "लगभग" -> "लगभग(${संख्या},${अंक})",
    "random" -> "random(${upperBound})",
    "randomDouble" -> "randomDouble(${upperBound})",
    "setCostume" -> "setCostume(${fileName})",
    "setCostumes" -> "setCostumes(${fileName1},${fileName2})",
    "nextCostume" -> "nextCostume()"
  )

  val helpContent = Map(
    "आगे" -> <div><strong>आगे</strong>(numSteps) - कछुए को दिए गए कदमों की संख्या के बराबर आगे बढ़ाता है। </div>.toString,
    "बाएँ" -> <div> <strong>बाएँ</strong>() - कछुए को 90 डिग्री बाएँ घुमाता है! <br/> <strong>बाएँ</strong>(कोण) - कछुए को दिए गए कोण के अनुसार बाएँ घुमाता है! <br/> <strong>बाएँ</strong>(कोण, radius) - दिए गए कोण के अनुसार, दी गयी त्रिज्या के आधार पर बाएँ घुमाता है! <br/> </div>.toString,
    "दाएँ" -> <div> <strong>दाएँ</strong>() - कछुए को 90 डिग्री दाएँ घुमाता है! <br/> <strong>दाएँ</strong>(कोण) - कछुए को दिए गए कोण के अनुसार दाएँ घुमाता है! <br/> <strong>दाएँ</strong>(कोण, त्रिज्या) - दिए गए कोण के अनुसार, दी गयी त्रिज्या के आधार पर दाएँ घुमाता है! <br/> </div>.toString,
    "छलाँग_लगाओ" -> <div> <strong>jumpTo</strong>(x, y) - बिना रेखा बनाये कछुए को (x,y) बिंदु पर स्थापित करता है! कछुए की दिशा नहीं बदली जाती!  <br/> </div>.toString,
    "यहाँ_जाओ" -> <div><strong>moveTo</strong>(x, y) - कछुए को (x,y) बिंदु की ओर घुमा कर बढ़ता है! </div>.toString,
    "कूदो" -> <div> <strong>hop</strong>(numSteps) - कछुए को <em>बिना रेखा बनाये</em> दी गयी संख्या के आधार पर आगे बढ़ता है!, so that no line is drawn. The pen is put down after the hop. <br/> </div>.toString,
    "घर" -> <div><strong>home</strong>() - Moves the turtle to its original location at the center of the screen, and makes it point north.</div>.toString,
    "ओर" -> <div><strong>towards</strong>(x, y) - Turns the turtle towards the point (x, y).</div>.toString,
    "दिशा" -> <div><strong>setHeading</strong>(angle) - Sets the turtle's heading to the given angle (0 is towards the right side of the screen (<em>east</em>), 90 is up (<em>north</em>)).</div>.toString,
    "वर्तमान_दिशा" -> <div><strong>heading</strong> - Queries the turtle's heading (0 is towards the right side of the screen (<em>east</em>), 90 is up (<em>north</em>)).</div>.toString,
    "पूर्व" -> <div><strong>east</strong>() - Turns the turtle east.</div>.toString,
    "पश्चिम" -> <div><strong>west</strong>() - Turns the turtle west.</div>.toString,
    "उत्तर" -> <div><strong>north</strong>() - Turns the turtle north.</div>.toString,
    "दक्षिण" -> <div><strong>south</strong>() - Turns the turtle south.</div>.toString,
    "एनिमेशन_देरी" -> <div> <strong>setAnimationDelay</strong>(delay) - Sets the turtle's speed. The specified delay is the amount of time (in milliseconds) taken by the turtle to move through a distance of one hundred steps.<br/> The default delay is 1000 milliseconds (or 1 second).<br/> </div>.toString,
    "लिखो" -> <div><strong>write</strong>(obj) - Makes the turtle write, at its current location, the specified object as a string.</div>.toString,
    "अक्षर_का_आकार" -> <div><strong>setPenFontSize</strong>(n) - Specifies the font size of the pen that the turtle writes with.</div>.toString,
    "चाप" -> <div> <strong>arc</strong>(radius, angle) - Gets the turtle to make an arc with the given radius and angle.<br/> Positive angles make the turtle go left (anti-clockwise). Negative angles make the turtle go right (clockwise) <br/> </div>.toString,
    "वृत्त" -> <div> <strong>circle</strong>(radius) - Gets the turtle to make a circle with the given radius. <br/> A circle(50) command is equivalent to an arc(50, 360) command.<br/> </div>.toString,
    "दिखो" -> <div><strong>visible</strong>() - Makes the turtle visible after it was made invisible with the invisible() command.</div>.toString,
    "छिपो" -> <div><strong>invisible</strong>() - Makes the turtle invisible. Use the visible() command to make it visible again.</div>.toString,
    "कलम_नीचे" -> <div> <strong>penDown</strong>() - Puts the turtle's pen down, so that it draws lines as the turtle moves.<br/> The turtle's pen is down by default. <br/> </div>.toString,
    "कलम_ऊपर" -> <div><strong>penUp</strong>() - Pulls the turtle's pen up, so that it does not draw lines as the turtle moves. <br/></div>.toString,
    "कलम_नीचे_है" -> <div><strong>isPenDown</strong> - Tells you whether the turtle's pen is down.</div>.toString,
    "कलम_का_रंग" -> <div><strong>setPenColor</strong>(color) - Specifies the color of the pen that the turtle draws with. <br/></div>.toString,
    "भरने_का_रंग" -> <div><strong>setFillColor</strong>(color) - Specifies the fill color of the figures drawn by the turtle. <br/></div>.toString,
    "कलम_की_मोटाई" -> <div><strong>setPenThickness</strong>(thickness) - Specifies the width of the pen that the turtle draws with. <br/></div>.toString,
    "saveStyle" -> <div> <strong>saveStyle</strong>() - Saves the turtle's current style, so that it can easily be restored later with a <tt>restoreStyle()</tt>. <br/> <p> The turtle's style includes: <ul> <li>Pen Color</li> <li>Pen Thickness</li> <li>Fill color</li> <li>Pen Font</li> <li>Pen Up/Down state</li> </ul> </p> </div>.toString,
    "restoreStyle" -> <div> <strong>restoreStyle</strong>() - Restores the turtle's style based on an earlier <tt>saveStyle()</tt>. <br/> <p> The turtle's style includes: <ul> <li>Pen Color</li> <li>Pen Thickness</li> <li>Fill color</li> <li>Pen Font</li> <li>Pen Up/Down state</li> </ul> </p> </div>.toString,
    "savePosHe" -> <div> <strong>savePosHe</strong>() - Saves the turtle's current position and heading, so that they can easily be restored later with a <tt>restorePosHe()</tt>. <br/> </div>.toString,
    "restorePosHe" -> <div> <strong>restorePosHe</strong>() - Restores the turtle's current position and heading based on an earlier <tt>savePosHe()</tt>. <br/> </div>.toString,
    "beamsOn" -> <div><strong>beamsOn</strong>() - Shows crossbeams centered on the turtle - to help with thinking about the turtle's heading/orientation.</div>.toString,
    "beamsOff" -> <div><strong>beamsOff</strong>() - Hides the turtle crossbeams that are turned on by a <tt>beamsOn()</tt>.</div>.toString,
    "पोंछो" -> <div><strong>clear</strong>() - Clears the turtle canvas, and brings the turtle to the center of the canvas.</div>.toString,
    "clearOutput" -> <div><strong>clearOutput</strong>() - Clears the output window.</div>.toString,
    "पृष्ठभूमि" -> <div> <strong>setBackground</strong>(color) - Sets the canvas background to the specified color. You can use predefined colors for setting the background, or you can create your own colors using the <tt>Color</tt>, <tt>ColorHSB</tt>, and <tt>ColorG</tt> functions. </div>.toString,
    "setBackgroundV" -> <div><strong>setBackgroundV</strong>(color1, color2) - Sets the canvas background to a vertical color gradient defined by the two specified colors. </div>.toString,
    "दोहराओ" -> <div><strong>repeat</strong>(n){{ }} - Repeats the specified block of commands (within braces) n number of times.<br/></div>.toString,
    "दोहराओ_गिनती_सहित" -> <div><strong>repeati</strong>(n) {{i => }} - Repeats the specified block of commands (within braces) n number of times. The current repeat index is available as <tt>i</tt> within the braces. </div>.toString,
    "दोहराओ_जब_तक" -> <div><strong>repeatWhile</strong>(cond) {{ }} - Repeats the specified block of commands (within braces) while the given condition is true.</div>.toString,
    "दोहराओ_प्रत्येक" -> <div><strong>repeatFor</strong>(seq){{ }} - Repeats the specified block of commands (within braces) for each element in the given sequence.<br/></div>.toString,
    "पंक्ति_छापो" -> <div><strong>println</strong>(obj) - Displays the given object as a string in the output window, with a newline at the end.</div>.toString,
    "readln" -> <div><strong>readln</strong>(promptString) - Displays the given prompt in the output window and reads a line that the user enters.</div>.toString,
    "लगभग" -> <div><strong>round</strong>(n, digits) - Rounds the given number n to the specified number of digits after the decimal point.</div>.toString,
    "random" -> <div><strong>random</strong>(upperBound) - Returns a random integer between 0 (inclusive) and upperBound (exclusive).</div>.toString,
    "randomDouble" -> <div><strong>randomDouble</strong>(upperBound) - Returns a random double-precision decimal number between 0 (inclusive) and upperBound (exclusive).</div>.toString,
    "setCostume" -> <div><strong>setCostume</strong>(costumeFile) - Changes the costume (i.e. image) associated with the turtle to the image in the specified file.</div>.toString,
    "setCostumes" -> <div><strong>setCostumes</strong>(costumeFile1, costumeFile2, ...) - Specifies multiple costumes for the turtle, and sets the turtle's costume to the first one in the sequence. You can cycle through the costumes by calling <tt>nextCostume()</tt>. </div>.toString,
    "nextCostume" -> <div><strong>nextCostume</strong>() - Changes the turtle's costume to the next one in the sequence of costumes specified by <tt>setCostumes(...)</tt>.</div>.toString
  )
}
