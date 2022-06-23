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

trait OptionMethodsInTurkish {
  // https://www.scala-lang.org/api/2.13.x/scala/Option.html
  type Belki[T] = Option[T]
  type Biri[T] = Some[T]
  val Hiçbiri = None
  object Biri {
    def apply[T](elem: T): Belki[T] = Some(elem)
    def unapply[T](b: Belki[T]) = b match {
      case None    => Hiçbiri
      case Some(n) => Biri(n)
    }
  }

  // val varMı = tr.varMı _
  // örnek: if (varMı(resim.çarpışma(Resim.tuvalınSınırları))) {...} else {...}
  // .isDefined yerine
  def varMı[T](o: Belki[T]): İkil = o match {
    case None    => yanlış
    case Some(x) => doğru
  }
  def yokMu[T](o: Belki[T]): İkil = !varMı(o)

  implicit class BelkiYöntemleri[T](b: Belki[T]) {
    def al = b.get
    def alYoksa[T](t: T) = b.getOrElse(t)

    def varMı: İkil = b.nonEmpty
    def yokMu: İkil = b.isEmpty
    def boşMu: İkil = b.isEmpty
    def doluMu: İkil = b.nonEmpty

    def işle[A](işlev: T => A): Belki[A] = b.map(işlev)
    def düzİşle[A](işlev: T => Option[A]): Belki[A] = b.flatMap(işlev)
    def ele(deneme: T => İkil): Belki[T] = b.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Belki[T] = b.filterNot(deneme)

    def dizine: Dizin[T] = b.toList

    // todo: more to come
  }
}

