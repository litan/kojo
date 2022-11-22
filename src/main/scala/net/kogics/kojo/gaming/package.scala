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

import net.kogics.kojo.core.{Picture, SCanvas}

package object gaming {
  trait GameMsgSink[Msg] {
    def triggerUpdate(msg: Msg)
  }

  trait Sub[Msg] {
    def run()

    def cancel()

    var gameMsgSink: GameMsgSink[Msg] = _
  }

  object Subscriptions {
    case class OnAnimationFrame[Msg](mapper: () => Msg)(implicit canvas: SCanvas) extends Sub[Msg] {
      var t: java.util.concurrent.Future[edu.umd.cs.piccolo.activities.PActivity] = _

      def run() {
        t = canvas.timer(20) {
          val msg = mapper()
          gameMsgSink.triggerUpdate(msg)
        }
      }

      def cancel() {
        canvas.stopAnimationActivity(t)
      }
    }

    case class OnKeyDown[Msg](mapper: Int => Msg)(implicit canvas: SCanvas) extends Sub[Msg] {
      var t: java.util.concurrent.Future[edu.umd.cs.piccolo.activities.PActivity] = _

      def run() {
        t = canvas.timer(20) {
          val pressedKeys = net.kogics.kojo.staging.Inputs.pressedKeys
          pressedKeys.foreach { keyCode =>
            val msg = mapper(keyCode)
            gameMsgSink.triggerUpdate(msg)
          }
        }
      }

      def cancel() {
        canvas.stopAnimationActivity(t)
      }
    }

    def onAnimationFrame[Msg](mapper: => Msg)(implicit canvas: SCanvas): Sub[Msg] = OnAnimationFrame(() => mapper)

    def onKeyDown[Msg](mapper: Int => Msg)(implicit canvas: SCanvas): Sub[Msg] = OnKeyDown(mapper)
  }

  class Game[Model, Msg](
                          init: => Model,
                          update: (Model, Msg) => Model,
                          view: Model => Picture,
                          subscriptions: Model => Seq[Sub[Msg]]
                        ) extends GameMsgSink[Msg] {
    private var currModel = init
    private var currView = view(currModel)
    private val currSubs = subscriptions(currModel)
    currView.draw()
    currSubs.foreach { s =>
      s.gameMsgSink = this
      s.run()
    }

    def triggerUpdate(msg: Msg) {
      currModel = update(currModel, msg)
      net.kogics.kojo.picture.PicCache.clear()
      currView.erase()
      currView = view(currModel)
      currView.draw()
      val updatedSubs = subscriptions(currModel)
      if (updatedSubs.length == 0) {
        currSubs.foreach { s =>
          s.cancel()
        }
      }
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
                      x1: Double, y1: Double, w1: Double, h1: Double,
                      x2: Double, y2: Double, w2: Double, h2: Double
                    ): Boolean = {
      import java.awt.geom.Rectangle2D
      val r1 = new Rectangle2D.Double(x1, y1, w1, h1)
      val r2 = new Rectangle2D.Double(x2, y2, w2, h2)
      r1.intersects(r2)
    }
  }
}
