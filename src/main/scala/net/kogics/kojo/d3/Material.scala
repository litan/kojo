/*
 * Copyright (C) 2012 Jerzy Redlarski <5xinef@gmail.com>
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

package net.kogics.kojo.d3

import java.awt.Color

object Material {

  def apply(r : Int, g : Int, b : Int) =
    new Material(Vector3d(r / 255d, g / 255d, b / 255d))

  def apply(r : Double, g : Double, b : Double) =
    new Material(Vector3d(r, g, b))
  
  def apply(color : Color) =
    new Material(Vector3d(color.getRed() / 255d,
						  color.getGreen() / 255d,
						  color.getBlue() / 255d))
}

case class Material(val color : Vector3d) {
  
  def setColor(r : Int, g : Int, b : Int) =
    Material(r, g, b)
  
  def setColor(r : Double, g : Double, b : Double) =
    Material(r, g, b)
  
  def setColor(color : Color) =
    Material(color)
}