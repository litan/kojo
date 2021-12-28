package net.kogics.kojo

import net.kogics.kojo.core.{Picture, SCanvas}

package object animation {
  trait Animation {
    def run()(implicit canvas: SCanvas): Unit = run(() => {})

    def run(onStop: () => Unit)(implicit canvas: SCanvas): Unit
  }

  object Animation {
    def apply(
               tickDuration: Int,
               initState: Seq[Double],
               finalState: Seq[Double],
               easer: net.kogics.kojo.kmath.KEasing,
               picMaker: Seq[Double] => Picture
             ): Animation = SingleAnimation(tickDuration, initState, finalState, easer, picMaker)
  }

  case class SingleAnimation(
                         tickDuration: Int,
                         initState: Seq[Double],
                         finalState: Seq[Double],
                         easer: net.kogics.kojo.kmath.KEasing,
                         picMaker: Seq[Double] => Picture
                       ) extends Animation {
    val initStateEx = initState :+ 0.0
    val finalStateEx = finalState :+ tickDuration.toDouble

    private def nextState(s: Seq[Double]): Seq[Double] = {
      val size = initStateEx.size
      val currTick = s(size - 1)
      if (currTick == tickDuration + 1) {
        s
      }
      else {
        val ns = for (idx <- 0 to size - 2) yield {
          easer.ease(initStateEx(idx), finalStateEx(idx), currTick, tickDuration)
        }
        ns :+ (currTick + 1)
      }
    }

    def run(onStop: () => Unit)(implicit canvas: SCanvas): Unit = {
      import edu.umd.cs.piccolo.activities.PActivity

      import java.util.concurrent.Future
      val initPic: Picture = picture.rect2(0, 0)
      lazy val anim: Future[PActivity] =
        canvas.animateWithState((initPic, initStateEx)) { case (pic, s) =>
          pic.erase()
          val pic2 = picMaker(s)
          pic2.draw()
          val ns = nextState(s)
          if (ns == s) {
            canvas.stopAnimationActivity(anim)
            onStop()
            pic2.erase()
          }
          (pic2, ns)
        }
      anim
    }
  }

  case class SeqAnimation(a1: Animation, a2: Animation) extends Animation {
    def run(onStop: () => Unit)(implicit canvas: SCanvas): Unit = {
      a1.run(() => a2.run(() => onStop()))
    }
  }

  case class ParAnimation(a1: Animation, a2: Animation) extends Animation {
    def run(onStop: () => Unit)(implicit canvas: SCanvas): Unit = {
      var stopCount = 0

      def triggerStop(): Unit = {
        stopCount += 1
        if (stopCount == 2) {
          onStop()
        }
      }

      a1.run(() => triggerStop())
      a2.run(() => triggerStop())
    }
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
