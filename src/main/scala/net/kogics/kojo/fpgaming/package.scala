/*
 * Copyright (C) 2022 Lalit Pant <pant.lalit@gmail.com>
 * Copyright (C) 2022 Anay Kamat <kamatanay@gmail.com>
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
import net.kogics.kojo.core.Point
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.util.Utils

package object fpgaming {
  trait GameMsgSink[Msg] {
    def triggerIncrementalUpdate(msg: Msg): Unit

    def triggerUpdate(msg: Msg): Unit
  }

  trait Sub[+Msg]

  trait NonTimerSub[Msg] extends Sub[Msg] {
    def activate(gameMsgSink: GameMsgSink[Msg]): Unit

    def deactivate(): Unit
  }

  trait TimerSub[Msg] extends Sub[Msg] {
    def fire(gameMsgSink: GameMsgSink[Msg]): Unit
  }

  object Subscriptions {
    case class OnAnimationFrame[Msg](mapper: () => Msg) extends TimerSub[Msg] {
      def fire(gameMsgSink: GameMsgSink[Msg]): Unit = {
        val msg = mapper()
        gameMsgSink.triggerIncrementalUpdate(msg)
      }
    }

    case class OnKeyDown[Msg](mapper: Int => Msg) extends TimerSub[Msg] {
      def fire(gameMsgSink: GameMsgSink[Msg]): Unit = {
        val pressedKeys = net.kogics.kojo.staging.Inputs.pressedKeys
        pressedKeys.foreach { keyCode =>
          val msg = mapper(keyCode)
          gameMsgSink.triggerIncrementalUpdate(msg)
        }
      }
    }

    case class OnMouseDown[Msg](mapper: Point => Msg) extends TimerSub[Msg] {
      def fire(gameMsgSink: GameMsgSink[Msg]): Unit = {
        import net.kogics.kojo.staging.Inputs
        if (Inputs.mousePressedFlag && Inputs.mouseBtn == 1) {
          val msg = mapper(Inputs.mousePos)
          gameMsgSink.triggerIncrementalUpdate(msg)
        }
      }
    }

    case class OnMouseClick[Msg](mapper: Point => Msg)(implicit canvas: SCanvas) extends NonTimerSub[Msg] {
      def activate(gameMsgSink: GameMsgSink[Msg]): Unit = {
        canvas.onMouseClick {
          case (x, y) =>
            val msg = mapper(Point(x, y))
            gameMsgSink.triggerUpdate(msg)
        }
      }

      def deactivate(): Unit = {
        println("Deactivating mouse click subscription")
        net.kogics.kojo.staging.Inputs.mouseClickHandler = None
      }
    }

    def onAnimationFrame[Msg](mapper: => Msg): Sub[Msg] = OnAnimationFrame(() => mapper)

    def onKeyDown[Msg](mapper: Int => Msg): Sub[Msg] = OnKeyDown(mapper)

    def onMouseDown[Msg](mapper: Point => Msg): Sub[Msg] = OnMouseDown(mapper)

    def onMouseClick[Msg](mapper: Point => Msg)(implicit cavas: SCanvas): Sub[Msg] = OnMouseClick(mapper)
  }

  trait CmdQ[+Msg] {
    def run(): Msg
  }

  class Game[Model, Msg](
      init: => Model,
      update: (Model, Msg) => Model,
      view: Model => Picture,
      subscriptions: Model => Seq[Sub[Msg]],
      refreshRate: Long
  )(implicit canvas: SCanvas)
      extends GameMsgSink[Msg] {
    private var currModel: Model = _
    private var currSubs: Seq[Sub[Msg]] = _
    private var currView: Picture = _
    private var firstTime = true

    private var gameTimer = canvas.timer(refreshRate) {
      if (firstTime) {
        firstTime = false
        currModel = init
        currSubs = subscriptions(currModel)
        nonTimerSubs.foreach(_.activate(this))
        currView = view(currModel)
        currView.draw()
      }
      else {
        fireTimerSubs()
        updateView()
        checkForStop()
      }
    }

    private def timerSubs: Seq[TimerSub[Msg]] =
      currSubs.filter(_.isInstanceOf[TimerSub[Msg]]).asInstanceOf[Seq[TimerSub[Msg]]]

    private def nonTimerSubs: Seq[NonTimerSub[Msg]] =
      currSubs.filter(_.isInstanceOf[NonTimerSub[Msg]]).asInstanceOf[Seq[NonTimerSub[Msg]]]

    private def updateView(): Unit = {
      val oldView = currView
      currView = view(currModel)
      oldView.erase()
      currView.draw()
    }

    private def updateModelAndSubs(msg: Msg): Unit = {
      currModel = update(currModel, msg)
      val oldSubs = currSubs
      currSubs = subscriptions(currModel)
      handleSubChanges(oldSubs, currSubs)
    }

    private def handleSubChanges(oldSubs: Seq[Sub[Msg]], newSubs: Seq[Sub[Msg]]): Unit = {
      if (newSubs != oldSubs) {
        lazy val newSubsSet = Set(newSubs: _*)
        oldSubs.foreach {
          case oldNtSub: NonTimerSub[Msg] =>
            if (!newSubsSet.contains(oldNtSub)) {
              oldNtSub.deactivate()
            }
          case _ =>
        }

        lazy val oldSubsSet = Set(oldSubs: _*)
        newSubs.foreach {
          case newNtSub: NonTimerSub[Msg] =>
            if (!oldSubsSet.contains(newNtSub)) {
              newNtSub.activate(this)
            }
          case _ =>
        }
      }
    }

    private def checkForStop(): Unit = {
      if (currSubs.isEmpty) {
        canvas.stopAnimationActivity(gameTimer)
      }
    }

    private def fireTimerSubs(): Unit = {
      timerSubs.foreach { sub =>
        sub.fire(this)
      }
    }

    def runCommandQuery(cmdQ: CmdQ[Msg]): Unit = {
      Utils.runAsync {
        val msg = cmdQ.run()
        Utils.runInSwingThreadNonBatched {
          triggerUpdate(msg)
        }
      }
    }

    def triggerIncrementalUpdate(msg: Msg): Unit = {
      if (currSubs.nonEmpty) {
        updateModelAndSubs(msg)
      }
    }

    def triggerUpdate(msg: Msg): Unit = {
      updateModelAndSubs(msg)
      updateView()
      checkForStop()
    }
  }

  class CollisionDetector(implicit canvas: SCanvas) {
    val cb = canvas.cbounds
    val minX = cb.getMinX
    val minY = cb.getMinY
    val maxX = cb.getMaxX
    val maxY = cb.getMaxY

    def collidesWithHorizontalEdge(x: Double, w: Double): Boolean =
      !(x >= minX && x <= (maxX - w))

    def collidesWithVerticalEdge(y: Double, h: Double): Boolean =
      !(y >= minY && y <= (maxY - h))

    def collidesWithEdge(x: Double, y: Double, w: Double, h: Double): Boolean = {
      !((x >= minX && x <= (maxX - w)) && (y >= minY && y <= (maxY - h)))
    }

    def collidesWith(
        x1: Double,
        y1: Double,
        w1: Double,
        h1: Double,
        x2: Double,
        y2: Double,
        w2: Double,
        h2: Double
    ): Boolean = {
      import java.awt.geom.Rectangle2D
      val r1 = new Rectangle2D.Double(x1, y1, w1, h1)
      val r2 = new Rectangle2D.Double(x2, y2, w2, h2)
      r1.intersects(r2)
    }
  }
}
