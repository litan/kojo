/*
 * Copyright (C) 2010 Peter Lewerin <peter.lewerin@tele2.se>
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

object Math {
  def constrain(value: Double, min: Double, max: Double) = {
    if (value < min) min
    else if (value > max) max
    else value
  }

  def map(value: Double, low1: Double, high1: Double, low2: Double, high2: Double) = {
    val range1: Double = high1 - low1
    val range2: Double = high2 - low2
    low2 + range2 * (value - low1) / range1
  }

  def lerp(value1: Double, value2: Double, amt: Double) = {
    require(amt >= 0d && amt <= 1d)
    val range: Double = value2 - value1
    value1 + amt * range
  }
}
