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

import net.kogics.kojo.core.{Picture, SCanvas}
import net.kogics.kojo.kmath.KEasing

package object animation {
  trait Animation {
    def run()(implicit canvas: SCanvas): Unit = run(() => {})

    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit

    def reversed: Animation

    def repeated(count: Int) = RepeatedAnimation(this, count)

    def repeatedForever = repeated(Int.MaxValue)
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
      keyFrames.frames.sliding(2).zip(easers).map { case (Seq(as1, as2), easer) =>
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
      frames.sliding(2).foreach { case (Seq(as1, as2)) =>
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

    val stateSize = initState.size
    val durationMillis = duration * 1000

    private def nextState(s: Seq[Double], elapsedTimeMillis: Double): Seq[Double] = {
      if (elapsedTimeMillis > durationMillis) {
        s
      }
      else {
        for (idx <- 0 to stateSize - 1) yield {
          easer.ease(initState(idx), finalState(idx), elapsedTimeMillis, durationMillis)
        }
      }
    }

    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit = {
      import edu.umd.cs.piccolo.activities.PActivity

      import java.util.concurrent.Future

      val startMillis = System.currentTimeMillis
      val initPic: Picture = picture.rect2(0, 0)
      lazy val anim: Future[PActivity] =
        canvas.animateWithState((initPic, initState, false, 0)) { case (pic, s, stop, stopcnt) =>
          if (stop) {
            if (stopcnt == 2) {
              canvas.stopAnimationActivity(anim)
              if (hideOnDone) {
                pic.erase()
              }
            }
            (pic, s, stop, stopcnt + 1)
          }
          else {
            pic.erase()
            val pic2 = picMaker(s)
            pic2.draw()
            val ns = nextState(s, (System.currentTimeMillis - startMillis).toDouble)
            if (ns == s) {
              onDone()
              (pic2, ns, true, 0)
            }
            else {
              (pic2, ns, stop, stopcnt)
            }
          }
        }
      anim
    }

    def reversed: Animation = this.copy(initState = this.finalState, finalState = this.initState)
  }

  case class Timeline(
                       duration: Double,
                       keyFrames: KeyFrames,
                       easers: Seq[KEasing],
                       picMaker: Seq[Double] => Picture,
                       hideOnDone: Boolean
                     ) extends Animation {
    AnimationUtils.checkKeyFrames(keyFrames)

    private def transitions = AnimationUtils.transitions(duration, keyFrames, easers, picMaker, true)

    lazy val anims = animSeq(transitions.toSeq)

    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit = {
      anims.run { () =>
        onDone()
        if (!hideOnDone) {
          val finalPic = picMaker(keyFrames.frames.last._2)
          finalPic.draw()
        }
      }
    }

    def reversed: Animation = TimelineReversed(duration, keyFrames, easers, picMaker, hideOnDone)
  }

  case class TimelineReversed(
                               duration: Double,
                               keyFrames: KeyFrames,
                               easers: Seq[KEasing],
                               picMaker: Seq[Double] => Picture,
                               hideOnDone: Boolean
                             ) extends Animation {
    AnimationUtils.checkKeyFrames(keyFrames)

    private def transitions = AnimationUtils.transitions(duration, keyFrames, easers, picMaker, true)

    lazy val anims = animSeq(transitions.toSeq).reversed

    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit = {
      anims.run { () =>
        onDone()
        if (!hideOnDone) {
          val finalPic = picMaker(keyFrames.frames.head._2)
          finalPic.draw()
        }
      }
    }

    def reversed: Animation = Timeline(duration, keyFrames, easers, picMaker, hideOnDone)
  }

  case class RepeatedAnimation(a: Animation, count: Int) extends Animation {
    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit = {
      var doneCount = 0

      def triggerDone(): Unit = {
        doneCount += 1
        if (doneCount == count) {
          onDone()
        }
        else {
          runAnim()
        }
      }

      def runAnim(): Unit = {
        a.run(triggerDone)
      }

      if (count > 0) {
        runAnim()
      }
    }

    def reversed: Animation = RepeatedAnimationReversed(a, count)
  }

  case class RepeatedAnimationReversed(a: Animation, count: Int) extends Animation {
    lazy val reversedA = a.reversed

    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit = {
      var doneCount = 0

      def triggerDone(): Unit = {
        doneCount += 1
        if (doneCount == count) {
          onDone()
        }
        else {
          runAnim()
        }
      }

      def runAnim(): Unit = {
        reversedA.run(triggerDone)
      }

      runAnim()
    }

    def reversed: Animation = RepeatedAnimation(a, count)
  }


  case class SeqAnimation(a1: Animation, a2: Animation) extends Animation {
    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit = {
      a1.run(() => a2.run(onDone))
    }

    def reversed: Animation = SeqAnimation(a2.reversed, a1.reversed)
  }

  case class ParAnimation(a1: Animation, a2: Animation) extends Animation {
    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit = {
      var doneCount = 0

      def triggerDone(): Unit = {
        doneCount += 1
        if (doneCount == 2) {
          onDone()
        }
      }

      a1.run(triggerDone)
      a2.run(triggerDone)
    }

    def reversed: Animation = ParAnimation(a1.reversed, a2.reversed)
  }

  def animSeq(as: Seq[Animation]): Animation = as match {
    case Seq() => throw new RuntimeException("To sequence animations, we need one animation at least!")
    case Seq(a) => a
    case Seq(a1, a2) => SeqAnimation(a1, a2)
    case h +: tail => SeqAnimation(h, animSeq(tail))
  }

  def animPar(as: Seq[Animation]): Animation = as match {
    case Seq() => throw new RuntimeException("To `par` animations, we need one animation at least!")
    case Seq(a) => a
    case Seq(a1, a2) => ParAnimation(a1, a2)
    case h +: tail => ParAnimation(h, animPar(tail))
  }
}
