package net.kogics.kojo.util

trait AsyncQueuedRunner {
  case class RunCode(code: () => Unit)
  import scala.actors._
  import scala.actors.Actor._
  val asyncRunner = actor {
    loop {
      react {
        case RunCode(code) =>
          Utils.safeProcess {
            code()
          }
      }
    }
  }
}
  
