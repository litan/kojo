package net.kogics.kojo.lite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}

@RunWith(classOf[JUnitRunner])
class KojoCompletionProviderTest extends FunSuite with Matchers {

  // Option B: shared instance for the whole suite
  private lazy val provider = new KojoCompletionProvider(null)

  test("rstaTemplate escapes $ not followed by { and appends ${cursor} when no interpolation is present") {
    provider.rstaTemplate("$conforms") shouldBe "$$conforms${cursor}"
  }

  test("rstaTemplate does not append ${cursor} when an interpolation ${...} is already present") {
    provider.rstaTemplate("forward(${n})") shouldBe "forward(${n})"
  }

  test("rstaTemplate escapes plain $ but preserves $ that starts a ${...} interpolation") {
    provider.rstaTemplate("$forward(${n})") shouldBe "$$forward(${n})"
  }

  test("rstaTemplate escapes plain $ but preserves $ that starts a ${...} interpolation, #2") {
    provider.rstaTemplate("for$ard(${n})") shouldBe "for$$ard(${n})"
  }

  test("rstaTemplate doubles a trailing $ and appends ${cursor} when no interpolation is present") {
    provider.rstaTemplate("$") shouldBe "$$${cursor}"
  }

  test("rstaTemplate leaves non-$ strings unchanged except for appending ${cursor}") {
    provider.rstaTemplate("clear") shouldBe "clear${cursor}"
  }
}
