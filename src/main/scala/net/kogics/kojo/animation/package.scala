package net.kogics.kojo

import net.kogics.kojo.core.{Picture, SCanvas}
import net.kogics.kojo.kmath.KEasing

package object animation {
  trait Animation {
    def run()(implicit canvas: SCanvas): Unit = run(() => {})

    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit

    def reversed: Animation
  }

  object Animation {
    def apply(
               duration: Double,
               initState: Seq[Double],
               finalState: Seq[Double],
               easer: KEasing,
               picMaker: Seq[Double] => Picture,
               hideOnDone: Boolean
             ): Animation = SingleAnimation(duration, initState, finalState, easer, picMaker, hideOnDone)
  }

  case class SingleAnimation(
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
        canvas.animateWithState((initPic, initState)) { case (pic, s) =>
          pic.erase()
          val pic2 = picMaker(s)
          pic2.draw()
          val ns = nextState(s, (System.currentTimeMillis - startMillis).toDouble)
          if (ns == s) {
            canvas.stopAnimationActivity(anim)
            onDone()
            if (hideOnDone) {
              pic2.erase()
            }
          }
          (pic2, ns)
        }
      anim
    }

    def reversed: Animation = this.copy(initState = this.finalState, finalState = this.initState)
  }

  case class SeqAnimation(a1: Animation, a2: Animation) extends Animation {
    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit = {
      a1.run(() => a2.run(() => onDone()))
    }

    def reversed: Animation = SeqAnimation(a2.reversed, a1.reversed)
  }

  case class ParAnimation(a1: Animation, a2: Animation) extends Animation {
    def run(onDone: () => Unit)(implicit canvas: SCanvas): Unit = {
      var stopCount = 0

      def triggerStop(): Unit = {
        stopCount += 1
        if (stopCount == 2) {
          onDone()
        }
      }

      a1.run(() => triggerStop())
      a2.run(() => triggerStop())
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
