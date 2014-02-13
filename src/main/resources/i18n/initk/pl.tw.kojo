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

println("Witamy w Kojo w polskiej wersji!")
if (isScratchPad) {
  println("Jesteś w brudnopisie, historia zmian nie zostanie zapisana po jego zamknięciu") 
}
setEditorTabSize(2)

//initialize unstable value
net.kogics.kojo.lite.i18n.PolishAPI.builtins = builtins

//make swedish names visible
import net.kogics.kojo.lite.i18n.PlInit
import net.kogics.kojo.lite.i18n.PolishAPI.{
  //explicit imports needed due to problems with multiple wildcard imports
  Żółw,żółw,czyść,czyśćWyjście,
  niebieski,czerwony,żółty,zielony,filotetowy,różowy,brązowy,czarny,biały,przezroczysty,
  tło, KcPL, 
  powtarzaj, powtarzajZLicznikem, dopóki,
  wejście,
  liczbaLosowa, liczbaLosowaRzeczywista,
  Całkowita, Rzeczywista, Napis,
  czasSystemowy}
import żółw.{czyść => _, _}

//simple IO
def drukuj(data: Any) = println(data)

//code completion
addCodeTemplates(
    "pl",
    PlInit.codeTemplates
)
//help texts
addHelpContent(
    "pl", 
    PlInit.helpContent
)
