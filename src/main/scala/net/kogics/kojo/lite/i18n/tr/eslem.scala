/*
 * Copyright (C) 2021
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
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
package net.kogics.kojo.lite.i18n.tr

import collection.mutable.{Map}

// todo: add more to the interface
object Eşlem {
  def boş[A,D] = new Eşlem[A,D](Map.empty[A,D])
  def apply[A,D](elems: (A,D)*) = new Eşlem[A,D](Map.from(elems))
  def değişmezden[A,D](m: collection.immutable.Map[A,D]) = new Eşlem[A,D](Map.from(m.iterator))
}
case class Eşlem[A,D](val m: Map[A,D]) {
  def eşli(a: A) = m.contains(a)
  def eşle(ikili: (A, D)) = m += ikili
  def +=(ikili: (A, D)) = this eşle ikili
  def -=(birinci: A) = m -= birinci
  def herÖgeİçin(komutlar: ((A, D)) => Birim) = m.foreach(komutlar)
  def sayı: Sayı = m.size
  def dizi = m.toSeq
  def al(a: A): Belki[D] = m.get(a)
  def alYoksa(a: A, varsayılanDeğer: => D) = m.getOrElse(a, varsayılanDeğer)
  def apply(a: A) = m(a)
}
