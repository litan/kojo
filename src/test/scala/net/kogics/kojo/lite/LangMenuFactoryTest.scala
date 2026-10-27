package net.kogics.kojo.lite

import org.scalatest.{Matchers, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.awt.{Component, Container}
import javax.swing.{JCheckBoxMenuItem, JLabel, JMenuItem, JOptionPane, SwingUtilities}
import net.kogics.kojo.lite.i18n.{LangInit, LanguageBehavior}

/**
 * Tests for LangMenuFactory.
 *
 * @author Eric Zoerner <a href="mailto:eric.zoerner@gmail.com">eric.zoerner@gmail.com</a>
 */
@RunWith(classOf[JUnitRunner])
class LangMenuFactoryTest extends FunSuite with Matchers {

  test("language entries use their own behavior font while the current UI is English") {
    SwingUtilities.invokeAndWait(new Runnable {
      def run(): Unit = {
        implicit val kojoCtx: net.kogics.kojo.core.KojoCtx = new NoOpKojoCtx
        kojoCtx.userLanguage should be("en")
        val defaultFont = new JCheckBoxMenuItem("English").getFont
        val menu = LangMenuFactory.createLangMenu()
        menu.getMenuComponents.foreach { component =>
          val item = component.asInstanceOf[JMenuItem]
          val expected = LanguageBehavior.forLanguage(item.getActionCommand).customUiFont.getOrElse(defaultFont)
          item.getFont should be(expected)
        }
        val hindi = menu.getMenuComponents.map(_.asInstanceOf[JMenuItem]).find(_.getActionCommand == "hi").get
        hindi.getFont should be(LangInit.fontForHindi)
      }
    })
  }

  test("only target-language message labels receive a font override in language-change dialogs") {
    def labels(component: Component): List[JLabel] = component match {
      case label: JLabel => List(label)
      case container: Container => container.getComponents.toList.flatMap(labels)
      case _ => Nil
    }
    SwingUtilities.invokeAndWait(new Runnable {
      def run(): Unit = {
        val message = "हिंदी (Hindi)\nRestart Kojo to change language"
        def messageLabels(pane: JOptionPane) = labels(pane).filter(label =>
          Option(label.getText).exists(text => text.contains("हिंदी") || text.contains("Restart Kojo")))
        val original = messageLabels(new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION))
        original.size should be(2)
        for (code <- List("en", "tr", "xx")) {
          messageLabels(LangMenuFactory.languageChangePane(message, code)).map(_.getFont) should be(original.map(_.getFont))
        }
        val hindi = messageLabels(LangMenuFactory.languageChangePane(message, "hi"))
        hindi.size should be(2)
        hindi.foreach(_.getFont should be(LangInit.fontForHindi))
      }
    })
  }

  test("supported languages should contain known supported languages") {
    LangMenuFactory.supportedLanguages.toSet.intersect(Set("en", "sv", "fr", "pl", "eo", "tr")).size should be(6)
  }

  test("langMenu has icon for supported languages") {
    LangMenuFactory.supportedLanguages.foreach {lang =>
      implicit val kojoCtx: net.kogics.kojo.core.KojoCtx = new NoOpKojoCtx {
        override def userLanguage: String = lang
      }
      val menu = LangMenuFactory.createLangMenu()
      val icon = menu.getIcon
      icon should not be null
    }
  }

  test("each menu item in langMenu has no icon (for now)") {
    implicit val kojoCtx: net.kogics.kojo.core.KojoCtx = new NoOpKojoCtx
    val menu = LangMenuFactory.createLangMenu()
    menu.getMenuComponents.foreach {menu =>
      menu.asInstanceOf[JMenuItem].getIcon should be (null)
    }
  }
}
