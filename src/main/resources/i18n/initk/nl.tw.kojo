/*
 * Copyright (C) 2014
 *   Lalit Pant <pant.lalit@gmail.com>
 *   Eric Zoerner <eric.zoerner@gmail.com>
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

//Dutch Turtle wrapper init for Kojo

//make Dutch names visible
val DutchAPI = net.kogics.kojo.lite.i18n.DutchAPI
import DutchAPI.{  //explicit imports needed due to problems with multiple wildcard imports
  Schildpad,schildpad,wis,wisOutput,
  blauw,rood,geel,groen,paars,roze,bruin,zwart,wit,geenKleur,cyaan,
  achterGrond, achterGrondV,
  herhaal, herhaalTel, zolangAls,
  input,
  rondAf, willekeurig, willekeurigDecimaal, kleur,
  Nummer, Decimaal, Tekst,
  systeemTijd, telTot}
import schildpad.{wis => _, _}

//simple IO
def output(data: Any) = println(data)
