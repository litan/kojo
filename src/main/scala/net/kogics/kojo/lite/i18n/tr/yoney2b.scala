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

import net.kogics.kojo.lite.{Builtins, CoreBuiltins}

// ../../util/Vector2D.scala
case class Yöney2B(x: Kesir, y: Kesir) {
  def this(v: richBuiltins.Vector2D) = this(v.x, v.y)
  val v = richBuiltins.Vector2D(x, y)
  def döndür(açı: Kesir) = Yöney2B(v.rotate(açı))
  def büyüt(oran: Kesir) = Yöney2B(v.scale(oran))
  def boyunuBirYap = Yöney2B(v.normalize)
  def boyu = v.magnitude
  def boyunKaresi = v.magSquared
  def sınırla(sınır: Kesir): Yöney2B = {
    if (boyu < sınır) this
    else boyunuBirYap * sınır
  }
  def +(y2: Yöney2B): Yöney2B = Yöney2B(v + y2.v)
  def -(y2: Yöney2B): Yöney2B = Yöney2B(v - y2.v)
  def *(oran: Kesir): Yöney2B = Yöney2B(v * oran)
  def /(oran: Kesir): Yöney2B = Yöney2B(v / oran)
  def içÇarpım(y2: Yöney2B): Kesir = v.dot(y2.v)
  def izdüşümü(y2: Yöney2B): Yöney2B = Yöney2B(v.project(y2.v))
  def ağırlıklıToplam(y2: Yöney2B, oran: Kesir): Yöney2B = Yöney2B(v.lerp(y2.v, oran)) // lerp == weightedSum
  def uzaklık(y2: Yöney2B): Kesir = v.distance(y2.v)
  def doğrultu = v.heading
  def açı(y2: Yöney2B): Kesir = v.angle(y2.v)
  def açı2(y2: Yöney2B): Kesir = v.angleTo(y2.v)
  def unary_- : Yöney2B = Yöney2B(-v)
  def yansıt(y2: Yöney2B): Yöney2B = Yöney2B(v.bounceOff(y2.v))
  override def toString = "Yöney2B(%.2f, %.2f)".format(x, y)
  override def equals(y2: Any) = y2 match {
    case y3: Yöney2B => v.equals(y3.v)
    case _ => yanlış
  }
}
object Yöney2B {
  def apply(v: richBuiltins.Vector2D) = new Yöney2B(v.x, v.y)
}
