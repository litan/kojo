package net.kogics.kojo.lite

import java.awt.Color
import java.awt.Font

import javax.swing.LookAndFeel
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource
import javax.swing.plaf.nimbus.NimbusLookAndFeel

import org.fife.ui.rsyntaxtextarea.{Theme => RTheme}

import com.bulenkov.darcula.DarculaLaf

trait Theme {
  def outputPaneFg: Color
  def outputPaneBg: Color
  def canvasBg: Color
  def editorTheme: RTheme
  def laf: LookAndFeel
  def toolbarBg: Option[Color]
}

class Dark extends Theme {
  val outputPaneFg = Color.lightGray
  val outputPaneBg = new Color(0x2d2d2d)
  val canvasBg = new Color(0x424447)
  val editorTheme = RTheme.load(getClass.getResourceAsStream("dark-editor-theme.xml"))
  val laf = new DarculaLaf
  val toolbarBg = None
}

class Light extends Theme {
  val outputPaneFg = new Color(32, 32, 32)
  val outputPaneBg = Color.white
  val canvasBg = Color.white
  val editorTheme = RTheme.load(getClass.getResourceAsStream("light-editor-theme.xml"))
  val laf = new NimbusLookAndFeel
  val toolbarBg = Some(new Color(230, 230, 230))
}

object Theme {
  val currentTheme = new Light
}
