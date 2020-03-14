package net.kogics.kojo.util

object ScalatestHelper {
  import org.scalatest.FunSuite
  import org.scalatest.Matchers

  class TestRun extends FunSuite {
    override def suiteName = "test-run"
    def register(name: String)(fn: => Unit) = test(name)(fn)
    def registerIgnored(name: String)(fn: => Unit) = ignore(name)(fn)
  }

  def test(name: String)(fn: => Unit): Unit = {
    val suite = new TestRun()
    suite.register(name)(fn)
    suite.execute()
  }

  def ignore(name: String)(fn: => Unit): Unit = {
    val suite = new TestRun()
    suite.registerIgnored(name)(fn)
    suite.execute()
  }
}

