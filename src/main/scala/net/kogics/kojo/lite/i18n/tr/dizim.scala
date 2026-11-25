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

import scala.reflect.ClassTag

import collection.mutable.ArrayBuffer
// Note: this is old attempt. Used in some samples. Use EsnekDizik in dizik.scala
// todo: this has only the bare essentials for ArrayBuffer. Add more to the interface..
object EsnekDizim {
  def apply[T](ögeler: T*) = new EsnekDizim[T](ArrayBuffer.from(ögeler))
  def boş[T] = new EsnekDizim[T](ArrayBuffer.empty[T])
}
class EsnekDizim[T](val a: ArrayBuffer[T]) {
  type Col = EsnekDizim[T]
  type C2[A] = EsnekDizim[A]
  def apply(yer: Sayı) = a(yer)
  def sayı = a.size
  def ekle(öge: T) = { a.append(öge); this }
  def +=(öge: T) = ekle(öge)
  def çıkar(yer: Sayı) = a.remove(yer)
  def sil() = a.clear()
  def dizi = a.toSeq
  def diziye = a.toSeq
  def boşMu: İkil = a.isEmpty
  def doluMu: İkil = a.nonEmpty

  def ele(deneme: T => İkil): Col = new EsnekDizim(a.filter(deneme))
  def eleYerinde(deneme: T => İkil): this.type = { a.filterInPlace(deneme); this }

  def işle[B](işlev: T => B): C2[B] = new EsnekDizim(a.map(işlev))
  def herbiriİçin[B](işlev: T => B): Birim = a.foreach(işlev)
  
  // todo: more to come
}

// use Dizik below instead of this older type attempt
object Dizim {
  def boş[T](implicit arg: ClassTag[T]) = new Dizim(Array.empty)
  def boş[T: ClassTag](b1: Sayı) = new Dizim(Array.ofDim[T](b1))
  def boş[T: ClassTag](b1: Sayı, b2: Sayı) = new Dizim(Array.ofDim[T](b1, b2))
  def boş[T: ClassTag](b1: Sayı, b2: Sayı, b3: Sayı) = new Dizim(Array.ofDim[T](b1, b2, b3))

  def doldur[T: ClassTag](b1: Sayı)(e: => T) = new Dizim(Array.fill[T](b1)(e))
  def doldur[T: ClassTag](b1: Sayı, b2: Sayı)(e: => T) = new Dizim(Array.fill[T](b1, b2)(e))
  def doldur[T: ClassTag](b1: Sayı, b2: Sayı, b3: Sayı)(e: => T) = new Dizim(Array.fill[T](b1, b2, b3)(e))
}
class Dizim[T](val a: Array[T]) {
  def diziye = a.toSeq
  def yazıya = toString
  override def toString = String.valueOf(a)
  def apply(b1: Sayı) = a(b1)
  @annotation.nowarn
  def boyut: Sayı = { // just an exercise -- not really needed
    var b = 1
    var p = a // scala style pointer
    var recurse = true
    while (recurse) p(0) match {
      case x: Array[T] => b += 1; p = x
      case _           => recurse = false
    }
    b
  }
}
