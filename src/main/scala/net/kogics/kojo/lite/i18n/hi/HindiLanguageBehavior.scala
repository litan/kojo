package net.kogics.kojo.lite.i18n.hi

import java.awt.{Font, GraphicsEnvironment}
import javax.swing.UIManager
import javax.swing.text.StyleContext

import net.kogics.kojo.lite.i18n.LanguageBehavior

object HindiLanguageBehavior extends LanguageBehavior {
  // Behavior selection precedes look-and-feel setup. Read its font defaults only
  // when a UI component first needs the Hindi font.
  lazy val font: Font = {
    val stream = getClass.getResourceAsStream("/i18n/NotoSansDevanagariUI-Regular.ttf")
    val loadedFont = try Font.createFont(Font.TRUETYPE_FONT, stream)
    finally stream.close()
    GraphicsEnvironment.getLocalGraphicsEnvironment.registerFont(loadedFont)
    // Some look and feels, including Metal, only define component fonts.
    val defaultFont = Option(UIManager.getLookAndFeelDefaults.getFont("defaultFont"))
      .getOrElse(UIManager.getFont("Label.font"))
    // Swing's composite font supplies Latin fallback for mixed-language labels.
    StyleContext.getDefaultStyleContext.getFont(loadedFont.getFamily, Font.PLAIN, defaultFont.getSize + 2)
  }

  override def customUiFont: Option[Font] = Some(font)

  override def editorFont(baseFont: Font, baseSize: Float): Font =
    font.deriveFont(baseFont.getStyle).deriveFont(baseSize * 1.1f)

  // Lower matras extend one pixel beyond RSyntaxTextArea's metrics at some sizes.
  override def editorLinePadding: Int = 1
}
