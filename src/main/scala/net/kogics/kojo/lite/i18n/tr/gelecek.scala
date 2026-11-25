/*
 * Copyright (C) 2023
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

// See: https://stackoverflow.com/questions/17215421/scala-concurrent-future-wrapper-for-java-util-concurrent-future
import java.util.concurrent.{Future => JFuture}
import scala.concurrent.{Future, ExecutionContext}
import edu.umd.cs.piccolo.activities.PActivity

trait FutureMethodsInTurkish {
  type JGelecek[T] = JFuture[T]
  type Gelecek[T] = Future[T]
  type İşletimBağlamı = ExecutionContext
  type PEtkinlik = PActivity

  object İşletimBağlamı {
    final lazy val küresel = ExecutionContext.global
  }

  object Gelecek {
    type KuralDışı = Exception
    def başarılı[T](sonuç: T): Gelecek[T] = Future.successful(sonuç)
    def başarısız[T](hata: KuralDışı): Gelecek[T] = Future.failed(hata)
    val olmaz = Future.never
  }

  implicit class FutureMethods[T](g: Gelecek[T]) {
    def işle[A](işlev: T => A)(implicit ex: ExecutionContext): Gelecek[A] = g.map(işlev)(ex)
    def düzİşle[A](işlev: T => Gelecek[A])(implicit ex: ExecutionContext): Gelecek[A] = g.flatMap(işlev)(ex)
    def ele(deneme: T => İkil)(implicit ex: ExecutionContext): Gelecek[T] = g.filter(deneme)(ex)
    def elekle(deneme: T => İkil)(implicit ex: ExecutionContext): Gelecek[T] = g.withFilter(deneme)(ex)
  }
}
