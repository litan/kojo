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
package xscala

import net.kogics.kojo.util.Throttler

object RepeatCommands extends RepeatCommands

trait RepeatCommands {
  def repeat(n: Int)(fn: => Unit): Unit = {
    var i = 0
    while (i < n) {
      fn
      i += 1
    }
  }

  def repeati(n: Int)(fn: Int => Unit): Unit = {
    var i = 0
    while (i < n) {
      fn(i + 1)
      i += 1
    }
  }

  def repeatWhile(cond: => Boolean)(fn: => Unit): Unit = {
    while (cond) {
      fn
      Throttler.throttle()
    }
  }

  def repeatUntil(cond: => Boolean)(fn: => Unit): Unit = {
    while (!cond) {
      fn
      Throttler.throttle()
    }
  }

  def repeatFor[T](seq: Iterable[T])(fn: T => Unit): Unit = {
    val iter = seq.iterator
    while (iter.hasNext) {
      fn(iter.next())
      Throttler.throttle()
    }
  }
}
