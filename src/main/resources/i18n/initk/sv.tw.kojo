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

//initialize unstable value
net.kogics.kojo.lite.i18n.SwedishAPI.builtins = builtins

//make swedish names visible
import net.kogics.kojo.lite.i18n.SvInit
import net.kogics.kojo.lite.i18n.SwedishAPI.{
  //explicit imports needed due to problems with multiple wildcard imports
  Padda,padda,sudda,suddaUtdata,
  blå,röd,gul,grön,lila,rosa,brun,svart,vit,genomskinlig,
  bakgrund, bakgrund2, KcSwe, 
  upprepa, räkneslinga, sålänge,
  indata,
  avrunda, slumptal, slumptalMedDecimaler,
  Heltal, Decimaltal, Sträng,
  systemtid, räknaTill}
import padda.{sudda => _, _}

//simple IO
def utdata(data: Any) = println(data)

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