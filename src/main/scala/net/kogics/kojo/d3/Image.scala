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

import java.lang.Math._
import javax.swing.JPanel
import java.awt.image.BufferedImage
import java.awt.Graphics
import java.awt.Color
import java.awt.Dimension

class Image extends JPanel with MouseControlledMover {
  import language.postfixOps

  var canvas : Option[Canvas3D] = None
  var image = new BufferedImage(Defaults.cameraWidth, Defaults.cameraHeight, BufferedImage.TYPE_INT_RGB)
  var interpolate = false
  addMouseListener(this)
  addMouseMotionListener(this)

  override def paint(g: Graphics): Unit = {
    g.drawImage(image, 0, 0, this)
  }
  
  def setDimensions(width : Int, height : Int) {
    val nonZeroWidth = if(width < 1) Defaults.cameraWidth else width
    val nonZeroHeight = if(height < 1) Defaults.cameraHeight else height
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
  }
  
  def fillWith(buffer : BufferedImage) {
    val g2d = image.createGraphics()
    val width = image.getWidth()
    val height = image.getHeight()
    val bufferWidth = buffer.getWidth()
    val bufferHeight = buffer.getHeight()
    
    for (row <- 0 until height) {
      for (column <- 0 until width) {

        if (interpolate) {
          val sourceX = ((column toDouble) / width) * (bufferWidth - 1)
          val sourceX1 = sourceX toInt
          val sourceX2 = if (sourceX + 1 < width) sourceX1 + 1 else sourceX1
          val xFactor = sourceX - floor(sourceX)
          val sourceY = ((row toDouble) / height) * (bufferHeight - 1)
          val sourceY1 = sourceY toInt
          val sourceY2 = if (sourceY + 1 < height) sourceY1 + 1 else sourceY1
          val yFactor = sourceY - floor(sourceY)

          val color1 = buffer.getRGB(sourceX1, sourceY1)
          val color2 = buffer.getRGB(sourceX1, sourceY2)
          val color3 = buffer.getRGB(sourceX2, sourceY1)
          val color4 = buffer.getRGB(sourceX2, sourceY2)
          val color = interpolate(color1, color2, color3, color4, xFactor, yFactor)
          g2d.setColor(new Color(color.x toInt, color.y toInt, color.z toInt))
	    }
        else {
          val sourceX = (((column toDouble) / width) * (bufferWidth - 1) + 0.5) toInt
          val sourceY = (((row toDouble) / height) * (bufferHeight - 1) + 0.5) toInt
          val color = buffer.getRGB(sourceX, sourceY)
          g2d.setColor(new Color(color))
        }
        
	    g2d.drawLine(column, row, column, row)
      }
    }
    
    g2d.dispose()
  }
  
  def interpolate(c1 : Int, c2 : Int, c3 : Int, c4 : Int, fx : Double, fy : Double) = {
    val c1v = Vector3d((c1 & 0x00FF0000) >> 16, (c1 & 0x0000FF00) >> 8, (c1 & 0x000000FF))
    val c2v = Vector3d((c2 & 0x00FF0000) >> 16, (c2 & 0x0000FF00) >> 8, (c2 & 0x000000FF))
    val c3v = Vector3d((c3 & 0x00FF0000) >> 16, (c3 & 0x0000FF00) >> 8, (c3 & 0x000000FF))
    val c4v = Vector3d((c4 & 0x00FF0000) >> 16, (c4 & 0x0000FF00) >> 8, (c4 & 0x000000FF))
    
    (c1v * (1 - fx) + c3v * fx) * (1 - fy) + (c2v * (1 - fx) + c4v * fx) * fy
  }
}
