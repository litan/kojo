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

package net.kogics.kojo
package d3

import util.Utils
import util.Throttler
import java.awt.Color
import net.kogics.kojo.core.KojoCtx

class API(_kojoCtx: KojoCtx, _canvas: Canvas3D) {
  val _throttler = new Throttler(1, 1)

  private def ensureVisible() {
    _kojoCtx.make3DCanvasVisible()
  }

  def clear() {
    Utils.runInSwingThread {
      ensureVisible()
    }
    _throttler.throttle()
    _canvas.clear()
  }

  def render() {
    _throttler.throttle()
    _canvas.renderSynchronous()
  }

  def renderAlways() {
    _throttler.throttle()
    _canvas.intermediateRendering = true
  }

  def renderOnRequest() {
    _throttler.throttle()
    _canvas.intermediateRendering = false
  }
  
  def sphere(radius : Double) {
    require(radius > 0, "Radius must be greater than 0")
    renderIfIntermediateRenderingOn {
      _canvas.sphere(radius)
    }
  }
  
  def cylinder(radius : Double, height : Double) {
    require(radius > 0, "Radius must be greater than 0")
    require(height > 0, "Height must be greater than 0")
    renderIfIntermediateRenderingOn {
      _canvas.cylinder(radius, height)
    }
  }
  
  def plane() {
    renderIfIntermediateRenderingOn {
      _canvas.plane()
    }
  }
  
  def cube(dimension : Double) {
    require(dimension > 0, "Dimensions must be greater than 0")
    renderIfIntermediateRenderingOn {
      _canvas.cube(dimension)
    }
  }
  
  def pointLight(r : Double, g : Double, b : Double,
                 x : Double, y : Double, z : Double) {
    require(r >= 0 && g >= 0 && b >= 0, "All components of the light source's color must be non-negative")
    renderIfIntermediateRenderingOn {
      _canvas.pointLight(r, g, b, x, y, z)
    }
  }
  
  def forward(distance : Double) {
    if(_canvas.turtle.trail) {
      renderIfIntermediateRenderingOn {
        if(distance > 0d) {
          _canvas.cylinder(_canvas.turtle.lineWidth, distance)
          _canvas.turtle = _canvas.turtle.forward(distance)
        }
        else {
          _canvas.turtle = _canvas.turtle.forward(distance)
          _canvas.cylinder(_canvas.turtle.lineWidth, -distance)
        }
      }
    }
    else {
      renderIfTurtleVisible {
        _canvas.turtle = _canvas.turtle.forward(distance)
      }
    }
  }
  
  def back(distance : Double) {
    forward(-distance)
  }
  
  def turn(angle : Double) {
    renderIfTurtleVisible {
      _canvas.turtle = _canvas.turtle.turn(angle)
    }
  }
  
  def left(angle : Double) {
    turn(angle)
  }
  
  def right(angle : Double) {
    turn(-angle)
  }
  
  def pitch(angle : Double) {
    renderIfTurtleVisible {
      _canvas.turtle = _canvas.turtle.pitch(angle)
    }
  }
  
  def roll(angle : Double) {
    renderIfTurtleVisible {
      _canvas.turtle = _canvas.turtle.roll(angle)
    }
  }
  
  def moveTo(x : Double, y : Double, z : Double) {
    renderIfTurtleVisible {
      _canvas.turtle = _canvas.turtle.moveTo(x, y, z)
    }
  }
  
  def lookAt(x : Double, y : Double, z : Double) {
    renderIfTurtleVisible {
      _canvas.turtle = _canvas.turtle.lookAt(x, y, z)
    }
  }
  
  def strafeUp(distance : Double) = {
    if (_canvas.turtle.trail) {
      renderIfIntermediateRenderingOn {
        _canvas.turtle = _canvas.turtle.pitch(-90)
        if (distance > 0d) {
          _canvas.cylinder(_canvas.turtle.lineWidth, distance)
          _canvas.turtle = _canvas.turtle.forward(distance)
        } else {
          _canvas.turtle = _canvas.turtle.forward(distance)
          _canvas.cylinder(_canvas.turtle.lineWidth, -distance)
        }
        _canvas.turtle = _canvas.turtle.pitch(90)
      }
    } else {
      renderIfTurtleVisible {
        _canvas.turtle = _canvas.turtle.strafeUp(distance)
      }
    }
  }
  
  def strafeDown(distance : Double) = {
    strafeUp(-distance)
  }
  
  def strafeLeft(distance : Double) = {
    if (_canvas.turtle.trail) {
      renderIfIntermediateRenderingOn {
        _canvas.turtle = _canvas.turtle.turn(90)
        if (distance > 0d) {
          _canvas.cylinder(_canvas.turtle.lineWidth, distance)
          _canvas.turtle = _canvas.turtle.forward(distance)
        } else {
          _canvas.turtle = _canvas.turtle.forward(distance)
          _canvas.cylinder(_canvas.turtle.lineWidth, -distance)
        }
        _canvas.turtle = _canvas.turtle.turn(-90)
      }
    } else {
      renderIfTurtleVisible {
        _canvas.turtle = _canvas.turtle.strafeLeft(distance)
      }
    }
  }
  
  def strafeRight(distance : Double) = {
    strafeLeft(-distance)
  }
  
  def invisible() {
    renderIfIntermediateRenderingOn {
      _canvas.turtle = _canvas.turtle.setVisible(false)
    }
  }
  
  def visible() {
    renderIfIntermediateRenderingOn {
      _canvas.turtle = _canvas.turtle.setVisible(true)
    }
  }
  
  def trailOn() {
    renderIfIntermediateRenderingOn {
      _canvas.turtle = _canvas.turtle.setTrail(true)
    }
  }
  
  def trailOff() {
    renderIfIntermediateRenderingOn {
      _canvas.turtle = _canvas.turtle.setTrail(false)
    }
  }
  
  def lineWidth(width : Double) {
    _canvas.turtle = _canvas.turtle.setLineWidth(width)
  }
  
  def color(r : Double, g : Double, b : Double) {
    require(r >= 0 && r <= 1.0 && 
    		g >= 0 && g <= 1.0 &&
    		b >= 0 && b <= 1.0,
    		"All components of the color must be in the range <0.0, 1.0>")
    _canvas.turtle = _canvas.turtle.setColor(r, g, b)
  }
  
  def color(r : Int, g : Int, b : Int) {
    require(r >= 0 && r <= 255 && 
    		g >= 0 && g <= 255 &&
    		b >= 0 && b <= 255,
    		"All components of the color must be in the range <0, 255>")
    _canvas.turtle = _canvas.turtle.setColor(r, g, b)
  }
  
  def color(color : Color) {
    _canvas.turtle = _canvas.turtle.setColor(color)
  }
  
  def cameraForward(distance : Double) {
    renderIfIntermediateRenderingOn {
      _canvas.camera = _canvas.camera.forward(distance)
    }
  }
  
  def cameraBack(distance : Double) {
    cameraForward(-distance)
  }
  
  def cameraTurn(angle : Double) {
    renderIfIntermediateRenderingOn {
      _canvas.camera = _canvas.camera.turn(angle)
    }
  }
  
  def cameraLeft(angle : Double) {
    cameraTurn(angle)
  }
  
  def cameraRight(angle : Double) {
    cameraTurn(-angle)
  }
  
  def cameraPitch(angle : Double) {
    renderIfIntermediateRenderingOn {
      _canvas.camera = _canvas.camera.pitch(angle)
    }
  }
  
  def cameraRoll(angle : Double) {
    renderIfIntermediateRenderingOn {
      _canvas.camera = _canvas.camera.roll(angle)
    }
  }
  
  def cameraStrafeUp(distance : Double) = {
    renderIfIntermediateRenderingOn {
      _canvas.camera = _canvas.camera.strafeUp(distance)
    }
  }
  
  def cameraStrafeDown(distance : Double) = {
    cameraStrafeUp(-distance)
  }
  
  def cameraStrafeLeft(distance : Double) = {
    renderIfIntermediateRenderingOn {
      _canvas.camera = _canvas.camera.strafeLeft(distance)
    }
  }
  
  def cameraStrafeRight(distance : Double) = {
    cameraStrafeLeft(-distance)
  }
  
  def cameraMoveTo(x : Double, y : Double, z : Double) {
    renderIfIntermediateRenderingOn {
      _canvas.camera = _canvas.camera.moveTo(x, y, z)
    }
  }
  
  def cameraLookAt(x : Double, y : Double, z : Double) {
    renderIfIntermediateRenderingOn {
      _canvas.camera = _canvas.camera.lookAt(x, y, z)
    }
  }
  
  def cameraAngle(angle : Double) {
    require(angle > 0 && angle < 180.0,
    		"Angle should be greater than 0 and smaller than 180")
    require(_canvas.camera.isInstanceOf[PerspectiveCamera],
    		"Camera's viewing angle can only be set when in perspective mode," +
    		"not in orthographic mode")
    _canvas.camera match {
      case c : PerspectiveCamera => {
	    renderIfIntermediateRenderingOn {
	      _canvas.camera = c.setAngle(angle)
	    }
      }
      case _ => {}
    }
  }
  
  def enableMouseControl() {
    if(_canvas.image.canvas == None) {
      _canvas.image.canvas = Option(_canvas)
    }
    _canvas.mouseControl = true
  }
  
  def disableMouseControl() {
    _canvas.mouseControl = false
  }
  
  def axesOn() {
    renderIfIntermediateRenderingOn {
    	_canvas.camera = _canvas.camera.setAxesVisibility(true)
    }
  }
  
  def axesOff() {
    renderIfIntermediateRenderingOn {
    	_canvas.camera = _canvas.camera.setAxesVisibility(false)
    }
  }
  
  def defaultLightsOn() {
    renderIfIntermediateRenderingOn {
    	_canvas.camera = _canvas.camera.setDefaultLights(true)
    }
  }
  
  def defaultLightsOff() {
    renderIfIntermediateRenderingOn {
    	_canvas.camera = _canvas.camera.setDefaultLights(false)
    }
  }
  
  def imageInterpolationOn() {
    renderIfIntermediateRenderingOn {
    	_canvas.image.interpolate = true
    }
  }
  
  def imageInterpolationOff() {
    renderIfIntermediateRenderingOn {
    	_canvas.image.interpolate = false
    }
  }
  
  def enableOrthographicMode() {
    _canvas.camera = new OrthographicCamera()(_canvas).pitch(90)
    render
  }
  
  def disableOrthographicMode() {
    _canvas.camera = new PerspectiveCamera().pitch(90)
    render
  }
  
  private def renderIfIntermediateRenderingOn(f : => Unit) {
    _throttler.throttle()
    f
    if(_canvas.intermediateRendering) {
      _canvas.renderSynchronous()
    }
  }
  
  private def renderIfTurtleVisible(f : => Unit) {
    if(_canvas.turtle.visible) {
      renderIfIntermediateRenderingOn{f}
    }
    else {
      _throttler.throttle()
      f
    }
  }
}
