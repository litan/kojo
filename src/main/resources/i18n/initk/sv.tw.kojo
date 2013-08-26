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

//Swedish Turtle wrapper init for Kojo

println("Välkommen till Kojo med svensk padda!")
if (isScratchPad) {
  println("Historiken kommer inte att sparas när du stänger Kojo Scratchpad.")
}
setEditorTabSize(2)

import net.kogics.kojo.lite.i18n.SwedishAPI.SwedishTurtle
class Padda(override val englishTurtle: Turtle) extends SwedishTurtle {
  def this() = this(newTurtle())
  def this(startX: Double, startY: Double) = this(newTurtle(startX, startY))
  def this(startX: Double, startY: Double, kostymFilNamn: String) = this(newTurtle(startX, startY, kostymFilNamn))
}
class Padda0(t0: => Turtle) extends SwedishTurtle { //by-name construction as turtle0 is volatile }
  override def englishTurtle: Turtle = t0
}
object padda extends Padda0(turtle0)
import padda.{ sudda => _, _ }
def sudda() = clear()
def suddaUtdata() = clearOutput()
val blå = blue
val röd = red
val gul = yellow
val grön = green
val lila = purple
val rosa = pink
val brun = brown
val svart = black
val vit = white
val genomskinlig = noColor
def bakgrund(färg: Color) = setBackground(färg)
def bakgrund2(färg1: Color, färg2: Color) = setBackgroundV(färg1, färg2)

def indata(ledtext: String = "") =  readln(ledtext)
def slumptal(n: Int) = random(n)
def slumptalMedDecimaler(n: Int) = randomDouble(n)

//make swedish names visible
import net.kogics.kojo.lite.i18n.SvInit
import net.kogics.kojo.lite.i18n.SwedishAPI.{
  //explicit imports needed due to problems with multiple wildcard imports
  KcSwe,
  upprepa,
  räkneslinga,
  sålänge,
  utdata,
  avrunda,
  Heltal,
  Decimaltal,
  Sträng,
  systemtid,
  räknaTill
}

//code completion
addCodeTemplates(
  "sv",
  SvInit.codeTemplates
)
//help texts
addHelpContent(
  "sv",
  SvInit.helpContent
)