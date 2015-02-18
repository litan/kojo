/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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
package turtle

import util.Utils

object TurtleHelper {
  
  def posAfterForward(x: Double, y: Double, theta: Double, n: Double): (Double, Double) = {
    val delX = math.cos(theta) * n
    val delY = math.sin(theta) * n
    (x + delX, y + delY)
  }

  def thetaTowards(px: Double, py: Double, x: Double, y: Double, oldTheta: Double): Double = {
    val (x0, y0) = (px, py)
    val delX = x - x0
    val delY = y - y0
    if (Utils.doublesEqual(delX,0,0.001)) {
      if (Utils.doublesEqual(delY,0,0.001)) oldTheta
      else if (delY > 0) math.Pi/2
      else 3*math.Pi/2
    }
    else if (Utils.doublesEqual(delY,0,0.001)) {
      if (delX > 0) 0
      else math.Pi
    }
    else {
      var nt2 = math.atan(delY/delX)
      if (delX < 0 && delY > 0) nt2 += math.Pi
      else if (delX < 0 && delY < 0) nt2 += math.Pi
      else if (delX > 0 && delY < 0) nt2 += 2* math.Pi
      nt2
    }
  }

  def thetaAfterTurn(angle: Double, oldTheta: Double) = {
    var newTheta = oldTheta + Utils.deg2radians(angle)
    if (newTheta < 0) newTheta = newTheta % (2*math.Pi) + 2*math.Pi
    else if (newTheta > 2*math.Pi) newTheta = newTheta % (2*math.Pi)
    newTheta
  }

  def distance(x0: Double, y0: Double, x: Double, y: Double): Double = {
    val delX = x-x0
    val delY = y-y0
    math.sqrt(delX * delX + delY * delY)
  }
}
