/*
 * Copyright (C) 2012 Bjorn Regnell <bjorn.regnell@cs.lth.se>
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
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
package picture

import java.awt.Font
import scala.collection.immutable.Queue
import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.Point
import net.kogics.kojo.kgeom.PolyLine
import net.kogics.kojo.util.Utils
import util.Utils
import Cartooning._
import Cartooning2._
import java.util.concurrent.CountDownLatch
import net.kogics.kojo.core.SCanvas

object Cartooning {
  import language.implicitConversions
  implicit def point2DtoPoint(p: Point2D): Point = new Point(p.x, p.y)
  implicit def pointtoPoint2D(p: Point): Point2D = Point2D(p.x, p.y)
  implicit def pimpPicture(pic: Picture): RichPicture = RichPicture(pic)
  type ListDD = List[(Double, Double)]
  implicit def pimpListDD(listDD: ListDD): RichListDD = RichListDD(listDD)
  type AnimationStep = Long => Unit // elapsed time to Unit
}

object Cartooning2 {
  type Point2D = java.awt.geom.Point2D.Double
  def Point2D(x: Double, y: Double) = new java.awt.geom.Point2D.Double(x, y)

  def isClose(p1: Point2D, p2: Point2D, minDist: Double = 0.1): Boolean =
    !(p1.distance(p2) > minDist)

  def approach(p1: Point2D, p2: Point2D, step: Double): Point2D = {
    val (dx, dy) = (p2.x - p1.x, p2.y - p1.y)
    val angle = math.atan2(dy, dx)
    val (xStep, yStep) = (step * math.cos(angle), step * math.sin(angle))
    if (isClose(p1, p2, step)) Point2D(p2.x, p2.y)
    else Point2D(p1.x + xStep, p1.y + yStep)
  }
  def midPoint(p1: Point2D, p2: Point2D): Point2D = {
    val (dx, dy) = ((p2.x - p1.x) / 2, (p2.y - p1.y) / 2)
    Point2D(p1.x + dx, p1.y + dy)
  }
  def isLine(p1: Point2D, mid: Point2D, p2: Point2D, accuracy: Double = 0.001) = {
    val (a, b) = (p1.distance(p2), p1.distance(mid) + mid.distance(p2))
    math.abs(a - b) < accuracy
  }

  //*** some utility functions related to PolyLines:

  def isZero(pl: PolyLine) =
    (pl.size == 0) || (pl.size == 1) && (pl.points(0) == Point2D(0, 0))
  def prependZeros(pl: PolyLine, n: Int): Unit =
    for (i <- 1 to n) pl.points.prepend(Point2D(0, 0))
  def stripExtraInitZeros(pl: PolyLine): Unit =
    while ((pl.size > 1) && (pl.points(1) == Point2D(0, 0))) {
      pl.points.remove(1)
    }
  def extendPolyLineTo(pl: PolyLine, s: Int): PolyLine = {
    val n = s - pl.size
    if (n <= 0 || pl.size < 2) return pl
    val pointBuffer = pl.points.toBuffer //copy; to not mutate pl
    var added = 0
    var i = 0
    while (added < n) {
      if (i >= pointBuffer.size - 1) { i = 0 }
      val newPt = midPoint(pointBuffer(i), pointBuffer(i + 1))
      pointBuffer.insert(i + 1, newPt)
      added += 1
      i += 2
    }
    PolyLine(pointBuffer.toSeq)
  }
  def maxDist(pl1: PolyLine, pl2: PolyLine): Double = {
    var result = 0.0
    val (pl1e, pl2e) =
      if (pl1.size < pl2.size) (extendPolyLineTo(pl1, pl2.size), pl2)
      else if (pl1.size > pl2.size) (pl1, extendPolyLineTo(pl2, pl1.size))
      else (pl1, pl2)
    val n = math.min(pl1.size, pl2.size)
    for (i <- 0 until n) {
      val dist = pl1.points(i).distance(pl2.points(i))
      if (dist > result) result = dist
    }
    result
  }
  def cleanUpMidPoints(pl: PolyLine): PolyLine = { //not used yet
    val pts = pl.points.toBuffer
    var i = 0
    while (i < pts.size - 2) {
      if (isLine(pts(i), pts(i + 1), pts(i + 2))) pts.remove(i + 1)
      else i += 1
    }
    PolyLine(pts.toSeq)
  }
  def plToString(pl: PolyLine): String = {
    pl.points.map(p => p.toString).mkString("PolyLine(", ",", ")")
  }
}

case class RichListDD(listDD: ListDD) {
  def toSkeleton = Skeleton(listDD)
  def toPicture(implicit canvas: SCanvas): Picture = Pic { t =>
    import t._
    listDD match {
      case p :: ps =>
        jumpTo(p._1, p._2)
        ps foreach { p => moveTo(p._1, p._2) }
      case _ =>
    }
  }
}

case class RichPicture(pic: Picture) {
  def morphTowards(skel: Skeleton, speed: Double = 1.0) = {
    pic.morph(skel.stepwiseMorpher(speed))
  }
  def morphTo(skel: Skeleton) = {
    pic.morph(skel.morpher)
  }
  def printPicture() {
    //as there is no elaborate toString on Pictures in Kojo
    //and we can't return a string as morph runs unsynched in another thread
    println("Picture(")
    pic.morph { polyLines =>
      polyLines map { pl =>
        println("  " + plToString(pl))
        pl
      }
      polyLines.toBuffer
    }
    println(")")
  }
}

case class Skeleton(points: Array[Point2D]) { //was MorphTarget
  def size = points.size
  def extendTo(s: Int): Skeleton = {
    val n = s - size
    if (n <= 0 || size < 2) return this
    val pointBuffer = points.toBuffer
    var added = 0
    var i = 0
    while (added < n) {
      if (i >= pointBuffer.size - 1) { i = 0 }
      val newPt = midPoint(pointBuffer(i), pointBuffer(i + 1))
      pointBuffer.insert(i + 1, newPt)
      added += 1
      i = i + 2
    }
    Skeleton(pointBuffer.toArray)
  }
  def extendShortest(pl: PolyLine): (Skeleton, PolyLine) =
    if (size < pl.size) (extendTo(pl.size), pl)
    else if (size > pl.size) (this, extendPolyLineTo(pl, size))
    else (this, pl)
  def maxDistTo(pl: PolyLine): Double = {
    var result = 0.0
    val (mte, ple) = extendShortest(pl)
    val n = math.min(mte.size, ple.size)
    for (i <- 0 until n) {
      val dist = mte.points(i).distance(ple.points(i))
      if (dist > result) result = dist
    }
    result
  }
  def isMorphedTo(pl: PolyLine, minDist: Double): Boolean =
    maxDistTo(pl) < minDist
  def approachTarget(pl: PolyLine, step: Double): PolyLine = {
    val (mte, ple) = extendShortest(pl)
    for (i <- 0 until ple.size) {
      ple.points(i) = approach(ple.points(i), mte.points(i), step)
    }
    val result = PolyLine(ple.points.toSeq)
    result.setPaint(pl.getPaint)
    result.setStrokePaint(pl.strokePaint)
    result.setStroke(pl.stroke)
    result
  }
  def stepwiseMorpher(step: Double): Seq[PolyLine] => Seq[PolyLine] = {
    //this is the actual morph function to be passed to pic.morph(mt.stepwiseMorpher(1)) 
    (pls: Seq[PolyLine]) =>
      {
        pls map (pl => {
          if (!isZero(pl) && !isMorphedTo(pl, step)) approachTarget(pl, step)
          else pl
        }
        )
      }
  }
  def morpher: Seq[PolyLine] => Seq[PolyLine] = {
    //a morph function to be passed to pic.morph(mySkeleton.morpher) 
    (pls: Seq[PolyLine]) =>
      {
        pls map (pl => {
          if (!isZero(pl)) {
            val (mte, ple) = extendShortest(pl)
            for (i <- 0 until ple.size) {
              ple.points(i) = mte.points(i)
            }
            val result = PolyLine(ple.points.toSeq)
            result.setPaint(pl.getPaint)
            result.setStrokePaint(pl.strokePaint)
            result.setStroke(pl.stroke)
            result
          }
          else pl
        }
        )
      }
  }
  def toPicture(implicit canvas: SCanvas) = Pic { t =>
    import t._
    if (size > 1) {
      jumpTo(points(0))
      for (i <- 1 until size) moveTo(points(i))
    }
  }
  override def toString = points.mkString("Skeleton(", ",", ")")
}

object Skeleton {
  import language.postfixOps
  def apply(pts: (Double, Double)*): Skeleton =
    new Skeleton(pts map {
      case (x, y) => Point2D(x, y)
    } toArray)
  def apply(pl: PolyLine) = new Skeleton(pl.points.toArray)
  def apply(pl: ListDD): Skeleton = apply(pl: _*)

} // end Skeleton

class Animator(canvas: SCanvas) {
  var agenda = Queue.empty[AnimationStep]
  def addToAgenda(w: AnimationStep) = synchronized {
    agenda :+= w
  }
  def getFromAgenda: Option[AnimationStep] = synchronized {
    if (agenda.isEmpty) {
      None
    }
    else {
      val (firstWork, agendaMinusFirst) = agenda.dequeue
      agenda = agendaMinusFirst
      Some(firstWork)
    }
  }
  def isAgendaEmpty = synchronized { agenda.isEmpty }

  def schedule(w: AnimationStep) {
    addToAgenda(w)
  }
  
  def stop() = canvas.stopAnimation() // TODO: canvas.figure0.stopAnimation(animation)

  def currTime = System.currentTimeMillis()
  val startTime = currTime
  val animation = canvas.animate {
    val work = getFromAgenda
    if (work.isDefined) {
      work.get(currTime - startTime)
    }
  }
}

trait AnimClip extends AnimationStep { outer: AnimClip =>
  def name: String
  def animator: Animator
  def apply(t: Long)
  def schedule(step: AnimationStep) = animator.schedule(step)

  def run() {
    schedule { t =>
      apply(t)
    }
  }

  def signalDone() = schedule { t =>
    doneFn.foreach { _(t) }
  }

  @volatile var doneFn: Option[AnimationStep] = None
  def whenDone(fn: AnimationStep) {
    doneFn = Some(fn)
  }

  def ~(as2: AnimClip): AnimClip = new AnimClip {
    val name = outer.name + "~" + as2.name
    val animator = outer.animator
    def apply(t: Long) {
      outer.run()
      outer.whenDone { t =>
        as2.run()
        as2.whenDone { t =>
          signalDone()
        }
      }
    }
  }

  def |(as2: AnimClip): AnimClip = new AnimClip {
    val name = outer.name + "!" + as2.name
    val animator = outer.animator
    def apply(t: Long) {
      outer.run()
      as2.run()
      var outerDone = false
      var as2Done = false
      outer.whenDone { t =>
        outerDone = true
        if (as2Done) {
          signalDone()
        }
      }
      as2.whenDone { t =>
        as2Done = true
        if (outerDone) {
          signalDone()
        }
      }
    }
  }
}

trait SpeakBalloon { self: AnimClip =>
  implicit val canvas: SCanvas
  case class WorkItem(var timeStamp: Long, var pic: Option[Picture])
  val wi = WorkItem(0, None)
  val showTimeMillis = 3 // seconds
  def textPos: Point2D
  def textSize = 30
  def speakPicture(lines: String*): Picture = Pic { t =>
    import t._
    import java.awt.Font
    val (x, y) = (textPos.x, textPos.y)
    val space = textSize / 2
    val tip = textSize * 2
    val txt = lines.mkString("\n")
    savePosHe()
    jumpTo(x, y)
    moveTo(x + tip, y + tip)
    val ptext = net.kogics.kojo.util.Utils.textNode(txt, x, y, 1.0)
    ptext.setFont(new Font(Font.MONOSPACED, Font.BOLD, textSize))
    tlayer.addChild(ptext)
    val w = ptext.getWidth
    val h = ptext.getHeight
    moveTo(x + tip, y + tip + h)
    ptext.translate(tip + space / 2, -(tip + h))
    ptext.repaint()
    moveTo(x + tip + w + space, y + tip + h)
    moveTo(x + tip + w + space, y + tip - space)
    moveTo(x + tip + space, y + tip - space)
    moveTo(x, y)
    restorePosHe()
  }

  def hide() {
    wi.pic.foreach(_.erase())
    wi.pic = None
  }

  def speak(lines: String*) = new AnimClip { outer: AnimClip =>
    val name = "speaker"
    val animator = self.animator
    def apply(t: Long) {
      applyNoHide(t)
      Utils.schedule(showTimeMillis) {
        hide()
        signalDone()
      }
    }

    def applyNoHide(t: Long) {
      val fillColor = fill _
      val penColor = stroke _
      import staging.KColor._
      wi.pic map { _.erase() }
      wi.timeStamp = System.currentTimeMillis
      wi.pic = Some(fillColor(white) * penColor(black) ->
        speakPicture(lines: _*))
      wi.pic.foreach(_.draw())
    }

    override def ~(as2: AnimClip): AnimClip = new AnimClip {
      val name = outer.name + "~" + as2.name
      val animator = outer.animator
      def apply(t: Long) {
        outer.applyNoHide(t)
        Utils.schedule(showTimeMillis) {
          hide()
          as2.run()
          as2.whenDone { t =>
            signalDone()
          }
        }
      }
    }

  }
}

trait Movable {
  def pos: Point2D
  def translate(x: Double, y: Double)
  def left(step: Double = 1) = translate(-step, 0)
  def right(step: Double = 1) = translate(step, 0)
  def up(step: Double = 1) = translate(0, step)
  def down(step: Double = 1) = translate(0, -step)
}

trait Morphable { self: AnimClip =>
  def workItem: Picture
  def morphTo(skel: Skeleton) = {
    schedule { t => workItem.morphTo(skel) }
  }
  def morphToStepwise(skel: Skeleton, step: Double = 1.0) = {
    workItem.foreachPolyLine { pl =>
      if (!isZero(pl)) {
        val maxDist = skel.maxDistTo(pl)
        for (i <- 0 to (maxDist / step).toInt) {
          schedule { t =>
            workItem.morphTowards(skel, step)
          }
        }
      }
    }
    signalDone()
  }

  def printPicture(): Unit = workItem.printPicture()
}
