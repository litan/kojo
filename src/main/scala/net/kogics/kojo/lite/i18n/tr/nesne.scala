/*
 * Copyright (C) 2022
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

// her nesne, herneyse! java Object?
trait AnyMethodsInTurkish {
  // todo: duplicates in yazi.scala and dizin.scala
  implicit class HerNesneYöntemleri(h: Her) {
    def kıymaKodu = h.hashCode
    // warnings:
    // def eşitMi(h2: Her) = h.equals(h2)
    // def nesnesiMi[T] = h.isInstanceOf[T]
    def nesnesiOlarak[T] = h.asInstanceOf[T]
    // https://contributors.scala-lang.org/t/what-is-the-purpose-of-tostring-in-scala/4779
    // def yazıya = h.getClass().getName() + '@' + Integer.toHexString(h.hashCode())
    def yazıya = h.toString
  }

  implicit class NesneYöntemleri(h: Nesne) {
    def kıymaKodu = h.hashCode
    def eşitMi(h2: Her) = h.equals(h2)
    // warnings:
    // def nesnesiMi[T] = h.isInstanceOf[T]
    def nesnesiOlarak[T] = h.asInstanceOf[T]
    def yazıya = h.toString
  }

}
