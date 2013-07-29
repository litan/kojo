/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.kojo
package story

import util.Read
import util.Read._

trait HandlerHolder[+T] {
  def handle(data: String)
}
  
class IntHandlerHolder(handler: Int => Unit) extends HandlerHolder[Int] {
  def handle(data: String) {
    handler(convertArg(data))
  }
  def convertArg(data: String): Int = implicitly[Read[Int]].read(data)
}

class StringHandlerHolder(handler: String => Unit) extends HandlerHolder[String] {
  def handle(data: String) {
    handler(data)
  }
}

class VoidHandlerHolder(handler: () => Unit) extends HandlerHolder[Unit] {
  def handle(data: String) {
    handler()
  }
}

