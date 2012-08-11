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

import java.lang.Math._
import java.awt.Color
import java.awt.FlowLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider
import javax.swing.SwingConstants
import java.awt.BorderLayout
import java.util.Hashtable
import java.lang.Integer
import java.awt.event.ComponentListener
import java.awt.event.ComponentEvent
import net.kogics.kojo.util.Utils
import scala.concurrent.Lock
import net.kogics.kojo.core.InitedSingleton
import net.kogics.kojo.core.KojoCtx

object Canvas3D extends InitedSingleton[Canvas3D] {
  def initedInstance(kojoCtx: KojoCtx) = synchronized {
    instanceInit()
    val ret = instance()
    API.initedInstance(kojoCtx, ret)
    ret
  }

  protected def newInstance = new Canvas3D
}

class Canvas3D extends JPanel with ComponentListener {
  setLayout(new BorderLayout())
  setBackground(Color.white)
  //  add(new JLabel("Welcome to the world of 3D!"))
  val image = new Image()
  add(image, BorderLayout.CENTER)
  val qualitySlider = new QualitySlider(this)
  add(qualitySlider, BorderLayout.LINE_END)

  var shapes = List[Shape]()
  var lights = List[Light]()
  var intermediateRendering = Defaults.intermediateRendering
  var turtle = new Turtle3d()
  @volatile var camera: Camera = new PerspectiveCamera().pitch(90d).setFrequency(qualitySlider.getValue()).moveTo(5, 5, 5).lookAt(0, 0, 0).roll(15)
  @volatile var inRender = false
  var mouseControl = Defaults.mouseControl
  if(mouseControl)
    image.canvas = Option(this)
  var defaultLightsOn = Defaults.defaultLightsOn

  def clear() {
    shapes = List[Shape]()
    lights = List[Light]()
    intermediateRendering = Defaults.intermediateRendering
    turtle = new Turtle3d()
    camera = new PerspectiveCamera().pitch(90d).setFrequency(qualitySlider.getValue()).moveTo(5, 5, 5).lookAt(0, 0, 0).roll(15)
    mouseControl = Defaults.mouseControl
    defaultLightsOn = Defaults.defaultLightsOn
    fixDimensions()
  }
  val renderLock = new Lock

  private def render() {
    renderLock.acquire()
    inRender = true
    // temporary fix to force rendering of unlimited quality images via slider
    if (camera.frequency == 0) {
      camera = camera.setPictureDimensions(
        image.image.getWidth(), image.image.getHeight())
    }
    val time = System.currentTimeMillis()
    val buffer = camera.render(shapes, lights, turtle)
    val frameTime = System.currentTimeMillis() - time
    image.fillWith(buffer)
    Utils.runInSwingThread {
    	image.repaint()
    }
    //System.out.println(frameTime)

    if (frameTime / 1.3 > (1000d / camera.frequency)) {
      camera = camera.setPictureDimensions(
        (camera.width * 0.9) toInt,
        (camera.height * 0.9) toInt)
    } else if (frameTime * 1.3 < (1000d / camera.frequency)) {
      if (camera.frequency == 0) {
        camera = camera.setPictureDimensions(
          image.getWidth(), image.getHeight())
      } else {
        camera = camera.setPictureDimensions(
          (camera.width * 1.1) toInt,
          (camera.height * 1.1) toInt)
      }
    }
    inRender = false
    renderLock.release()
  }

  def renderSynchronous() {
    render()
  }

  def renderAsynchronous() {
    Utils.runAsyncMonitored {
      render()
    }
  }

  def sphere(radius: Double) {
    shapes = new Sphere(turtle.position, turtle.orientation, turtle.material, radius) :: shapes
  }

  def cylinder(radius: Double, height: Double) {
    shapes = new Cylinder(turtle.position, turtle.orientation, turtle.material, radius, height) :: shapes
  }

  def plane() {
    shapes = new Plane(turtle.position, turtle.orientation, turtle.material) :: shapes
  }

  def cube(dimension: Double) {
    shapes = new Cube(turtle.position, turtle.orientation, turtle.material, dimension / 2) :: shapes
  }

  def pointLight(r: Double, g: Double, b: Double) {
    lights = new PointLight(Vector3d(10d, 10d, 10d), Vector3d(r, g, b)) :: lights
  }

  def pointLight(r: Double, g: Double, b: Double,
    x: Double, y: Double, z: Double) {
    lights = new PointLight(Vector3d(x, y, z), Vector3d(r, g, b)) :: lights
  }

  def setPictureSize(width: Int, height: Int) {
    image.setDimensions(width, height)
    repaint()
  }

  addComponentListener(this)

  override def componentHidden(e: ComponentEvent) {}
  override def componentShown(e: ComponentEvent) {
    fixDimensions()
    renderAsynchronous()
  }

  override def componentMoved(e: ComponentEvent) {}

  override def componentResized(e: ComponentEvent) {
    fixDimensions()
    renderAsynchronous()
  }

  def fixDimensions() {
    val width = if(image.getWidth() > 0) image.getWidth() else 640
    val height = if(image.getHeight() > 0) image.getHeight() else 480 
    image.setDimensions(width, height)
    val cameraScale = sqrt(((width * height) toDouble) / ((camera.width * camera.height) toDouble))
    camera = camera.setPictureDimensions((width / cameraScale) toInt, (height / cameraScale) toInt)
  }
}
