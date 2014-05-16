/*
 * Copyright (C) 2013 
 *   Bjorn Regnell <bjorn.regnell@cs.lth.se>,
 *   Lalit Pant <pant.lalit@gmail.com>,
 *   Mikołaj Sochacki <mbsochacki@wp.pl>
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

//Polish Turtle wrapper init for Kojo

//make polish names visible
import net.kogics.kojo.lite.i18n.PlInit
import net.kogics.kojo.lite.i18n.PolishAPI.{
  //explicit imports needed due to problems with multiple wildcard imports
  Żółw,żółw,czyść,czyśćWyjście,
  niebieski,czerwony,żółty,zielony,fioletowy,różowy,brązowy,czarny,biały,przezroczysty,
  tło, KcPL, 
  powtarzaj, powtarzajZLicznikem, dopóki,
  wejście, zaokrągl,
  liczbaLosowa, liczbaLosowaRzeczywista,
  Całkowita, Rzeczywista, Napis,
  czasSystemowy}
import żółw.{czyść => _, _}

//simple IO
def drukuj(data: Any) = println(data)
