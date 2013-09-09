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
package mathworld

import util.Utils

class MwTurtle(x: Double, y: Double, _Mw: MathWorld) extends core.RichTurtleCommands {
  import turtle.TurtleHelper._
  import java.awt.Color

  // underscore vars/methods do not show up in code completion
  import _Mw._

  val _MarkerSize = 0.2
  private var _headingMarker: MwPoint = _
  private var _position: MwPoint = _
  private var _theta: Double = _
  private var _penColor: Color = _
  private var _penThickness: Int = _
  private var _penIsDown: Boolean = _
  private var _angleShow: Boolean = _
  private var _externalAngleShow: Boolean = _
  private var _lengthShow: Boolean = _
  private var _polyPoints: List[MwPoint] = _

  private var _lines: List[MwLineSegment] = _
  private var _angles: List[MwAngle] = _
  private var _points: List[MwPoint] = _

  init()

  def _lastLine: Option[MwLineSegment] = _lines match {
    case Nil => None
    case _ => Some(_lines.head)
  }

  def init() {
    _penColor = Color.red
    _penThickness = 2
    _penIsDown = true
    _angleShow = false
    _externalAngleShow = false
    _lengthShow = false
    _polyPoints = Nil
    _headingMarker = point(0, 0)
    _headingMarker.setColor(Color.orange)
    _headingMarker.show()

    _lines = Nil
    _angles = Nil
    _points = Nil

    _setPos(point(x, y))
    setHeading(90)
  }

  private def _forwardLine(p0: MwPoint, p1: MwPoint) {
    setPosition(p1)
    if (_penIsDown) {
      val ls = lineSegment(p0, _position)
      ls.setColor(_penColor)
      ls.setLineThickness(_penThickness)
      ls.show()

      if (_lengthShow) {
        ls.showValueInLabel()
      }

      if (_lastLine.isDefined && _lastLine.get.p2 == p0) {
        val a = angle(_lastLine.get.p1, p0, _position)
        a.showValueInLabel()
        _angles = a :: _angles
        if (_angleShow) {
          a.show()
        }

        val ea = angle(_position, p0, _lastLine.get.p1)
        ea.showValueInLabel()
        _angles = ea :: _angles
        if (_externalAngleShow) {
          ea.show()
        }
      }

      if (_polyPoints != Nil) {
        _polyPoints = _position :: _polyPoints
      }

      _lines = ls :: _lines
    }
  }

  def forward(n: Double) {
    Utils.runInSwingThread {
      val p0 = _position
      val xy = posAfterForward(p0.x, p0.y, _theta, n)
      _forwardLine(p0, point(xy._1, xy._2))
    }
  }

  def turn(angle: Double) {
    Utils.runInSwingThread {
      setRotation(thetaAfterTurn(angle, _theta))
    }
  }

  private def _updateHMarker() {
    val xy = posAfterForward(_position.x, _position.y, _theta, _MarkerSize)
    _headingMarker.moveTo(xy._1, xy._2)
  }

  // should be called on swing thread
  private def _setPos(p: MwPoint) {
    _position = p
    _points = p :: _points
    if (_penIsDown) {
      _position.setColor(Color.green)
      _position.show()
      _updateHMarker()
    }
  }

  def setPosition(x: Double, y: Double) {
    setPosition(point(x, y))
  }

  def setPosition(p: MwPoint) {
    Utils.runInSwingThread {
      _position.setColor(Color.blue)
      _setPos(p)
    }
  }

  def setHeading(angle: Double) {
    Utils.runInSwingThread {
      setRotation(Utils.deg2radians(angle))
    }
  }

  private def setRotation(angle: Double) {
    _theta = angle
    _updateHMarker()
  }

  def setPenColor(color: Color) {
    Utils.runInSwingThread {
      _penColor = color
    }
  }

  def setPenThickness(t: Int) {
    Utils.runInSwingThread {
      _penThickness = t
    }
  }

  def moveTo(x: Double, y: Double) {
    moveTo(point(x, y))
  }

  def moveTo(p: MwPoint) {
    Utils.runInSwingThread {
      setRotation(thetaTowards(_position.x, _position.y, p.x, p.y, _theta))
      _forwardLine(_position, p)
    }
  }

  def penUp() {
    Utils.runInSwingThread {
      _penIsDown = false
    }
  }

  def penDown() {
    Utils.runInSwingThread {
      _penIsDown = true
      _position.show()
    }
  }

  def labelPosition(l: String) {
    Utils.runInSwingThread {
      _position.setLabel(l)
    }
  }

  def showAngles() {
    Utils.runInSwingThread {
      _angleShow = true
    }
  }

  def hideAngles() {
    Utils.runInSwingThread {
      _angleShow = false
    }
  }

  def showExternalAngles() {
    Utils.runInSwingThread {
      _externalAngleShow = true
    }
  }

  def hideExternalAngles() {
    Utils.runInSwingThread {
      _externalAngleShow = false
    }
  }

  def showLengths() {
    Utils.runInSwingThread {
      _lengthShow = true
    }
  }

  def hideLengths() {
    Utils.runInSwingThread {
      _lengthShow = false
    }
  }

  def position = {
    Utils.runInSwingThreadAndWait {
      _position
    }
  }

  def heading = {
    Utils.runInSwingThreadAndWait {
      Utils.rad2degrees(_theta)
    }
  }

  def beginPoly() {
    Utils.runInSwingThread {
      _polyPoints = List(_position)
    }
  }

  def endPoly() {
    Utils.runInSwingThread {
      if (_polyPoints.size > 2) {
        val pp = _polyPoints.reverse
        setRotation(thetaTowards(_position.x, _position.y, pp(0).x, pp(0).y, _theta))
        val p0 = _position
        _forwardLine(p0, pp(0))

        // make _angles at first vertex of poly
        if (_penIsDown) {
          val a2 = angle(p0, _position, pp(1))
          a2.showValueInLabel()
          _angles = a2 :: _angles
          if (_angleShow) {
            a2.show()
          }

          val ea2 = angle(pp(1), _position, p0)
          ea2.showValueInLabel()
          _angles = ea2 :: _angles
          if (_externalAngleShow) {
            ea2.show()
          }
        }
      }
      _polyPoints = Nil
    }
  }

  def findLine(label: String) = {
    Utils.runInSwingThreadAndWait {
      _lines.find {l => l.p1.label + l.p2.label == label} match {
        case Some(l) => l
        case None => throw new RuntimeException("Unknown line: " + label)
      }
    }
  }

  def findAngle(label: String) = {
    Utils.runInSwingThreadAndWait {
      _angles.find {a => a.p1.label + a.p2.label + a.p3.label == label} match {
        case Some(a) => a
        case None => throw new RuntimeException("Unknown angle: " + label)
      }
    }
  }

  def findPoint(label: String) = {
    Utils.runInSwingThreadAndWait {
      _points.find {p => p.label == label} match {
        case Some(l) => l
        case None => throw new RuntimeException("Unknown point: " + label)
      }
    }
  }
}
