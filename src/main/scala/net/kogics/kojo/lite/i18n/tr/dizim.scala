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

import collection.mutable.{ArrayBuffer}
import scala.reflect.ClassTag

// todo: this has only the bare essentials for Array and ArrayBuffer. Add more to the interface..

object Dizim {
  def boş[T:ClassTag](b1: Sayı) = new Dizim(Array.ofDim[T](b1))
  def boş[T:ClassTag](b1: Sayı, b2: Sayı) = new Dizim(Array.ofDim[T](b1, b2))
  def boş[T:ClassTag](b1: Sayı, b2: Sayı, b3: Sayı) = new Dizim(Array.ofDim[T](b1, b2, b3))

  def doldur[T:ClassTag](b1: Sayı)(e: => T) = new Dizim(Array.fill[T](b1)(e))
  def doldur[T:ClassTag](b1: Sayı, b2: Sayı)(e: => T) = new Dizim(Array.fill[T](b1, b2)(e))
  def doldur[T:ClassTag](b1: Sayı, b2: Sayı, b3: Sayı)(e: => T) = new Dizim(Array.fill[T](b1, b2, b3)(e))
}
class Dizim[T](val a: Array[T]) {
  def apply(b1: Sayı) = a(b1)
  def boyut: Sayı = { // just an exercise -- not really needed 
    var b = 1
    var p = a // scala style pointer
    var recurse = true
    while (recurse) p(0) match {
      case x: Array[T] => b += 1; p = x
      case _ => recurse = false
    }
    b
  }
}

object EsnekDizim {
  def apply[T](elemanlar: T*) = new EsnekDizim[T](ArrayBuffer.from(elemanlar))
  def boş[T] = new EsnekDizim[T](ArrayBuffer.empty[T])
}
class EsnekDizim[T](val a: ArrayBuffer[T]) {
  def apply(yer: Sayı) = a(yer)
  def sayı = a.size
  def ekle(eleman: T) = {a.append(eleman); this}
  def +=(eleman: T) = ekle(eleman)
  def çıkar(yer: Sayı) = a.remove(yer)
  def sil() = a.clear()
  def dizi = a.toSeq
}
