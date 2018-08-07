package net.kogics.kojo.util

import akka.actor.Actor
import akka.actor.Props

trait AsyncQueuedRunner {
  case class RunCode(code: () => Unit)

  class Runner extends Actor {
    def receive = {
      case RunCode(code) =>
        Utils.safeProcess {
          code()
        }
    }
  }

  val asyncRunner = Utils.actorSystem.actorOf(Props(new Runner), name = "AsyncRunner")

  def runAsyncQueued(fn: => Unit) {
    asyncRunner ! RunCode { () =>
      fn
    }
  }
}

