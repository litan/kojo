package net.kogics.kojo.lite

import org.scalatest.{Matchers, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import javax.swing.JMenuItem

/**
 * Tests for LangMenuFactory.
 *
 * @author Eric Zoerner <a href="mailto:eric.zoerner@gmail.com">eric.zoerner@gmail.com</a>
 */
@RunWith(classOf[JUnitRunner])
class LangMenuFactoryTest extends FunSuite with Matchers {

  test("supported languages should contain known supported languages") {
    LangMenuFactory.supportedLanguages.toSet.intersect(Set("en", "sv", "fr", "pl", "eo")).size should be(5)
  }

  test("langMenu has icon for supported languages") {
    LangMenuFactory.supportedLanguages.foreach {lang ⇒
      implicit val kojoCtx = new KojoCtx(false)
      kojoCtx.userLanguage = lang
      val menu = LangMenuFactory.createLangMenu()
      val icon = menu.getIcon
      icon should not be null
    }
  }

  test("each menu item in langMenu has an icon") {
    implicit val kojoCtx = new KojoCtx(false)
    kojoCtx.userLanguage = "en"
    val menu = LangMenuFactory.createLangMenu()
    menu.getMenuComponents.foreach {menu ⇒
      //Changed from "should be (null)" to "should not be (null)" as now we provide language menu items with their flag. Christoph Knabe 2014-12-16
      menu.asInstanceOf[JMenuItem].getIcon should not be (null)
    }
  }
}
