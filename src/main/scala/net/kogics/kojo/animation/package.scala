/*
 * Copyright (C) 2021 Lalit Pant <pant.lalit@gmail.com>
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

import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.kmath.KEasing
import net.kogics.kojo.util.Utils

package object animation {
  val noOp = () => {}

  trait Animation {
    def run()(implicit canvas: SCanvas): Unit = {
      val r = runner
      r.run(() => if (hideOnDone) r.hideLastFrame())(noOp)
    }

    def reversed: Animation

    def repeated(count: Int) = RepeatedAnimation(this, count)

    def repeatedForever = repeated(Int.MaxValue)

    def runner: AnimationRunner

    def hideOnDone: Boolean
  }

  trait AnimationRunner {
    def run(onDone: () => Unit)(onStart: () => Unit)(implicit canvas: SCanvas): Unit

    def hideLastFrame(): Unit
  }

  // frames contain a seq of time => state
  case class KeyFrames(frames: Seq[(Double, Seq[Double])])

  object Animation {
    def apply(
        duration: Double,
        initState: Seq[Double],
        finalState: Seq[Double],
        easer: KEasing,
        picMaker: Seq[Double] => Picture,
        hideOnDone: Boolean
    ): Animation = Transition(duration, initState, finalState, easer, picMaker, hideOnDone)

    def apply(
        duration: Double,
        keyFrames: KeyFrames,
        easers: Seq[KEasing],
        picMaker: Seq[Double] => Picture,
        hideOnDone: Boolean
    ): Animation = Timeline(duration, keyFrames, easers, picMaker, hideOnDone)
  }

  private[animation] object AnimationUtils {
    def transitions(
        duration: Double,
        keyFrames: KeyFrames,
        easers: Seq[KEasing],
        picMaker: Seq[Double] => Picture,
        hideOnDone: Boolean
    ): Iterator[Transition] = {
      require(
        keyFrames.frames.length - 1 == easers.length,
        s"Incorrect number of easings for keyframes - required = ${keyFrames.frames.length - 1}, actual = ${easers.length}"
      )
      keyFrames.frames.sliding(2).zip(easers).map {
        case (Seq(as1, as2), easer) =>
          val tduration = (as2._1 - as1._1) / 100 * duration
          val initState = as1._2
          val finalState = as2._2
          Transition(tduration, initState, finalState, easer, picMaker, hideOnDone)
      }
    }

    def checkKeyFrames(keyFrames: KeyFrames): Unit = {
      val frames = keyFrames.frames
      require(frames.length > 1, "KeyFrames should have at least two frames")
      require(frames.head._1 == 0, "KeyFrames should start at 0%")
      require(frames.last._1 == 100, "KeyFrames should end at 100%")
      frames.sliding(2).foreach {
        case (Seq(as1, as2)) =>
          require(as2._1 > as1._1, "Keyframe start times should be in increasing order")
      }
    }
  }

  case class Transition(
      duration: Double, // in seconds
      initState: Seq[Double],
      finalState: Seq[Double],
      easer: KEasing,
      picMaker: Seq[Double] => Picture,
      hideOnDone: Boolean
  ) extends Animation {

    def runner = new TransitionRunner(this)

    def reversed: Animation = this.copy(initState = finalState, finalState = initState)
  }

  class TransitionRunner(transition: Transition) extends AnimationRunner {

    import transition._

    val stateSize = initState.size
    val durationMillis = duration * 1000

    var currState: Seq[Double] = _
    var currPic: Picture = _

    private def nextState(s: Seq[Double], elapsedTimeMillis: Double): Seq[Double] = {
      if (elapsedTimeMillis >= durationMillis) {
        finalState
      }
      else {
        for (idx <- 0 to stateSize - 1) yield {
          easer.ease(initState(idx), finalState(idx), elapsedTimeMillis, durationMillis)
        }
      }
    }

    def run(onDone: () => Unit)(onStart: () => Unit)(implicit canvas: SCanvas): Unit = {
      import edu.umd.cs.piccolo.activities.PActivity
      import java.util.concurrent.Future

      val startMillis = System.currentTimeMillis
      var firstTime = true

      lazy val anim: Future[PActivity] =
        canvas.animate {
          if (firstTime) {
            val initPic = picMaker(initState)
            initPic.draw()
            currState = initState
            currPic = initPic
            onStart()
            firstTime = false
          }
          else {
            val elapsedTimeMillis =
              (System.currentTimeMillis - startMillis).toDouble
            val ns = nextState(currState, elapsedTimeMillis)
            val pic2 = picMaker(ns)
            pic2.draw()
            currPic.erase()

            currState = ns
            currPic = pic2

            if (elapsedTimeMillis >= durationMillis) {
              onDone()
              canvas.stopAnimationActivity(anim)
            }
          }
        }
      anim
    }

    def hideLastFrame(): Unit = {
      if (hideOnDone) {
        currPic.erase()
      }
    }
  }

  case class Timeline(
      duration: Double,
      keyFrames: KeyFrames,
      easers: Seq[KEasing],
      picMaker: Seq[Double] => Picture,
      hideOnDone: Boolean,
      isReversed: Boolean = false
  ) extends Animation {
    AnimationUtils.checkKeyFrames(keyFrames)

    def runner = new TimelineRunner(this)

    def reversed: Animation = this.copy(isReversed = !isReversed)
  }

  class TimelineRunner(timeline: Timeline) extends AnimationRunner {

    import timeline._

    private def transitions = AnimationUtils.transitions(duration, keyFrames, easers, picMaker, true)

    def anims = {
      val anims0 = animSeq(transitions.toSeq)
      if (isReversed) anims0.reversed else anims0
    }

    lazy val animsRunner = anims.runner

    def run(onDone: () => Unit)(onStart: () => Unit)(implicit canvas: SCanvas): Unit = {
      animsRunner.run(onDone)(onStart)
    }

    def hideLastFrame(): Unit = {
      if (hideOnDone) {
        animsRunner.hideLastFrame()
      }
    }
  }

  case class RepeatedAnimation(a: Animation, count: Int, isReversed: Boolean = false) extends Animation {
    def runner = new RepeatedAnimationRunner(this)

    def hideOnDone = a.hideOnDone

    def reversed: Animation = this.copy(isReversed = !isReversed)
  }

  class RepeatedAnimationRunner(repeatedAnimation: RepeatedAnimation) extends AnimationRunner {

    import repeatedAnimation._

    var prevRunner: AnimationRunner = _
    var currRunner: AnimationRunner = _

    def run(onDone: () => Unit)(onStart: () => Unit)(implicit canvas: SCanvas): Unit = {
      var doneCount = 0
      var startCount = 0

      def triggerDone(): Unit = {
        doneCount += 1
        if (doneCount == count) {
          onDone()
        }
        else {
          runAnim()
        }
      }

      def triggerStart(): Unit = {
        if (startCount == 0) {
          onStart()
        }
        startCount += 1
        if (prevRunner != null) {
          prevRunner.hideLastFrame()
        }
      }

      def runAnim(): Unit = {
        prevRunner = currRunner
        currRunner = a.runner
        currRunner.run(triggerDone)(triggerStart)
      }

      if (count > 0) {
        runAnim()
      }
    }

    def hideLastFrame(): Unit = {
      if (hideOnDone) {
        currRunner.hideLastFrame()
      }
    }
  }

  case class SeqAnimation(a1: Animation, a2: Animation) extends Animation {
    def runner = new SeqAnimationRunner(this)

    def hideOnDone = a2.hideOnDone

    def reversed: Animation = SeqAnimation(a2.reversed, a1.reversed)
  }

  class SeqAnimationRunner(seqAnimation: SeqAnimation) extends AnimationRunner {

    import seqAnimation._

    lazy val a1runner = a1.runner
    lazy val a2runner = a2.runner

    def run(onDone: () => Unit)(onStart: () => Unit)(implicit canvas: SCanvas): Unit = {
      a1runner.run(() => a2runner.run(onDone)(() => a1runner.hideLastFrame()))(onStart)
    }

    def hideLastFrame(): Unit = {
      a2runner.hideLastFrame()
    }
  }

  case class ParAnimation(a1: Animation, a2: Animation) extends Animation {

    def runner = new ParAnimationRunner(this)

    def hideOnDone = a1.hideOnDone || a2.hideOnDone

    def reversed: Animation = ParAnimation(a1.reversed, a2.reversed)
  }

  class ParAnimationRunner(parAnimation: ParAnimation) extends AnimationRunner {

    import parAnimation._

    lazy val a1runner = a1.runner
    lazy val a2runner = a2.runner

    def run(onDone: () => Unit)(onStart: () => Unit)(implicit canvas: SCanvas): Unit = {
      var doneCount = 0
      var startCount = 0

      def triggerDone(): Unit = {
        doneCount += 1
        if (doneCount == 2) {
          onDone()
        }
      }

      def triggerStart(): Unit = {
        if (startCount == 0) {
          onStart()
        }
        startCount += 1
      }

      a1runner.run(triggerDone)(triggerStart)
      a2runner.run(triggerDone)(triggerStart)
    }

    def hideLastFrame(): Unit = {
      a1runner.hideLastFrame()
      a2runner.hideLastFrame()
    }
  }

  def animSeq(as: Seq[Animation]): Animation = as match {
    case Seq()       => throw new RuntimeException("To sequence animations, we need one animation at least!")
    case Seq(a)      => a
    case Seq(a1, a2) => SeqAnimation(a1, a2)
    case h +: tail   => SeqAnimation(h, animSeq(tail))
  }

  def animPar(as: Seq[Animation]): Animation = as match {
    case Seq()       => throw new RuntimeException("To `par` animations, we need one animation at least!")
    case Seq(a)      => a
    case Seq(a1, a2) => ParAnimation(a1, a2)
    case h +: tail   => ParAnimation(h, animPar(tail))
  }
}
