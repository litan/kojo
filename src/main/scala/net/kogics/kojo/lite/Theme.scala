package net.kogics.kojo.lite

import java.awt.Color
import java.awt.Font

import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

import org.fife.ui.rsyntaxtextarea.{Theme => RTheme}

import com.bulenkov.darcula.DarculaLaf

import net.kogics.kojo.util.Utils

trait Theme {
  def outputPaneFg: Color
  def outputPaneBg: Color
  def canvasBg: Color
  def editorTheme: RTheme
  def loadLookAndFeel(): Unit
  def toolbarBg: Option[Color]
  def defaultBg: Color
  def textBg: Color
  def textFg: Color
  def errorPaneFg: String
  def tracingBg: Color
  def tracingHighlightColor: Color
}

class DarkTheme extends Theme {
  val canvasBg = new Color(0x424447)
  val editorTheme = RTheme.load(getClass.getResourceAsStream("dark-editor-theme.xml"))
  def loadLookAndFeel(): Unit = {
    val laf = new DarculaLaf
    UIManager.setLookAndFeel(laf)
    UIManager.getLookAndFeelDefaults.put("defaultFont", new FontUIResource("Arial", Font.PLAIN, 12))
  }
  val toolbarBg = None
  val defaultBg = new Color(0x3c3f41)
  val textBg = defaultBg
  val textFg = new Color(0xbbbbbb)
  val outputPaneFg = textFg
  val outputPaneBg = new Color(0x2d2d2d)
  val errorPaneFg = "orange"
  val tracingBg = defaultBg
  val tracingHighlightColor = new Color(173, 206, 238)
}

class LightTheme extends Theme {
  val canvasBg = Color.white
  val editorTheme = RTheme.load(getClass.getResourceAsStream("light-editor-theme.xml"))
  def loadLookAndFeel(): Unit = {
    if (Utils.isMac) {
      // use the system look and feel
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
      UIManager.getLookAndFeelDefaults.put("defaultFont", new FontUIResource("Arial", Font.PLAIN, 12))
    }
    else {
      UIManager.getInstalledLookAndFeels.find { _.getName == "Nimbus" }.foreach { nim =>
        UIManager.setLookAndFeel(nim.getClassName)
      }
    }

  }
  val toolbarBg = Some(new Color(230, 230, 230))
  val defaultBg = Color.white
  val textBg = Color.white
  val textFg = new Color(32, 32, 32)
  val outputPaneFg = textFg
  val outputPaneBg = textBg
  val errorPaneFg = "red"
  val tracingBg = Color.white
  val tracingHighlightColor = new Color(173, 206, 238)
}

object Theme {
  val currentTheme: Theme = {
    Utils.appProperty("theme") match {
      case Some("dark") => new DarkTheme
      case Some(_)      => new LightTheme
      case None         => new LightTheme
    }
  }
}
