package net.kogics.kojo.xscala

import akka.actor.{PoisonPill, Props}
import akka.pattern.{ask, gracefulStop}
import akka.util.Timeout
import net.kogics.kojo.core.VanillaMode
import net.kogics.kojo.lite.NoOpRunContext
import net.kogics.kojo.lite.i18n.{LanguageEditorData, LanguageTestSupport}
import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner
import scala.concurrent.Await
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class ScalaCodeRunnerLanguageTest extends FunSuite with Matchers {
  test("keyword requests use registered data before interpreter initialization and after actor restart") {
    // No interpreter is needed for keyword requests; avoid creating a user's extension directory.
    val runner = new ScalaCodeRunner2(new NoOpRunContext, VanillaMode) {
      override def ensureExtensionDir(): Unit = ()
    }
    // Add a test-only failure message to the real interpreter actor's receive loop.
    val restartable = net.kogics.kojo.util.Utils.actorSystem.actorOf(Props(new runner.InterpActor {
      override def receive: Receive = ({
        case "restart-for-test" => throw new IllegalStateException("restart-for-test")
      }: Receive).orElse(super.receive)
    }))
    def request(prefix: String): (List[String], Int) = {
      implicit val timeout: Timeout = Timeout(5.seconds)
      Await.result(restartable ? runner.KeywordCompletionRequest(Some(prefix)), 5.seconds)
        .asInstanceOf[runner.CompletionResponse].data
    }
    try LanguageTestSupport.withData(LanguageEditorData(List("invented"))) {
      runner.keywordCompletions(Some("inven")) should be((List("invented"), 5))
      runner.keywordCompletions(Some("val"))._1 should contain("val")
      request("inven") should be((List("invented"), 5))
      restartable ! "restart-for-test"
      request("inven") should be((List("invented"), 5))
      LanguageTestSupport.withData(LanguageEditorData(List("replacement"))) {
        request("inven")._1 should be(empty)
        request("replace")._1 should be(List("replacement"))
      }
    }
    finally {
      try Await.result(gracefulStop(restartable, 5.seconds, PoisonPill), 5.seconds)
      finally Await.result(gracefulStop(runner.codeRunner, 5.seconds, PoisonPill), 5.seconds)
    }
  }
}
