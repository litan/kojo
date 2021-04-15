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

import collection.mutable.{Stack, Queue}

// todo: add translate Queue as Kuyruk

object Yığın {
  def boş[T] = new Yığın[T]()
  def apply[T](elemanlar: T*) = {
    val y = new Yığın[T]()
    for (e <- elemanlar) y.s.push(e)
    y
  }
  def doldur[T](y2: Yığın[T]) = {
    val y = new Yığın[T]()
    y.koyHepsini(y2.dizi)
    y
  }
}
case class Yığın[T]() {
  val s = Stack.empty[T]
  def this(y: Yığın[T]) = {
    this()
    this.koyHepsini(y.s.toList.tail)
  }
  def koy(e: T) = s.push(e)
  def al() = s.pop()
  def tane = s.size
  def tepe = s.head
  def dizi = s.toSeq
  def koyHepsini(dizi: scala.collection.IterableOnce[T]) =
    s.pushAll(dizi)
}
