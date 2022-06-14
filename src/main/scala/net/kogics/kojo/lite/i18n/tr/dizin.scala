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

trait ListMethodsInTurkish {
  implicit class DizinYöntemleri[T](d: Dizin[T]) {
    def başı: T = d.head
    def kuyruğu: Dizin[T] = d.tail
    def boyu: Sayı = d.length
    def boşMu: İkil = d.isEmpty
    def dolu: İkil = d.nonEmpty
    def ele(deneme: T => İkil): Dizin[T] = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Dizin[T] = d.filterNot(deneme)
    def işle[A](işlev: T => A): Dizin[A] = d.map(işlev)
    def düzİşle[A](işlev: T => Dizin[A]): Dizin[A] = d.flatMap(işlev)
    def sıralı(implicit ord: Ordering[T]): Dizin[T] = d.sorted(ord)
    def sırala[A](i: T => A)(implicit ord: Ordering[A]): Dizin[T] = d.sortBy(i)
    def sırayaSok(önce: (T, T) => İkil): Dizin[T] = d.sortWith(önce)
    def indirge[B >: T](işlem: (B, B) => B): B = d.reduce(işlem)
    // more to come
  }
}
