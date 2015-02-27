/*
 * Copyright (C) 2013 
 *   Bjorn Regnell <bjorn.regnell@cs.lth.se>,
 *   Lalit Pant <pant.lalit@gmail.com>
 *   Christoph Knabe http://public.beuth-hochschule.de/~knabe/
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

//German Turtle wrapper init for Kojo

//make german names visible
val GermanAPI = net.kogics.kojo.lite.i18n.GermanAPI
import GermanAPI.{
  //explicit imports needed due to problems with multiple wildcard imports
  Kröte,kröte,leeren,ausgabeLeeren,
  blau,rot,gelb,grün,lila,rosa,braun,schwarz,grau,weiß,durchsichtig,
  grundfarbe, grundfarbeUO, grundfarbeLR, KcGer, 
  mehrmals, fürBereich, fürAlle, solange,
  einlesen, ausgeben,
  runden, zufall, zufallBruch,
  Ganzzahl, Bruchzahl, Text,
  systemzeit, zählzeitStoppen
}
import kröte.{leeren => _, _}

//simple IO
//def utdata(data: Any) = println(data)
