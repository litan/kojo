/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.util

object Throttler {
  val systemThrottler = new Throttler(100, 5)
  val hardSystemThrottler = new Throttler(20, 20)
  def throttle() = systemThrottler.throttle()
  def throttleHard() = hardSystemThrottler.throttle()
}

class Throttler(after: Int, sleepTime: Int) {
  val numCalls = new ThreadLocal[Int] {
    override def initialValue = 0
  }

  /**
   * Slow things down if stuff is happening too quickly
   * Meant to slow down runaway computation inside the interpreter, so that the
   * user can interrupt the runaway thread
   */
  def throttle() {
    val nc = numCalls.get + 1
    if (nc > after) {
      numCalls.set(0)
      allowInterruption()
    }
    else {
      numCalls.set(nc)
    }
  }

  def allowInterruption() {
    Thread.sleep(sleepTime) // Throws interrupted exception if the thread has been interrupted
  }
}
