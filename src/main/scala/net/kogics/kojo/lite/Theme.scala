package net.kogics.kojo.lite

import java.awt.Color

import javax.swing.LookAndFeel
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
  def defaultBg: Color
  def textBg: Color
  def textFg: Color
  def errorPaneFg: String
}

class Dark extends Theme {
  val canvasBg = new Color(0x424447)
  val editorTheme = RTheme.load(getClass.getResourceAsStream("dark-editor-theme.xml"))
  val laf = new DarculaLaf
  val toolbarBg = None
  val defaultBg = new Color(0x3c3f41)
  val textBg = defaultBg
  val textFg = new Color(0xbbbbbb)
  val outputPaneFg = textFg
  val outputPaneBg = new Color(0x2d2d2d)
  val errorPaneFg = "orange"
}

class Light extends Theme {
  val canvasBg = Color.white
  val editorTheme = RTheme.load(getClass.getResourceAsStream("light-editor-theme.xml"))
  val laf = new NimbusLookAndFeel
  val toolbarBg = Some(new Color(230, 230, 230))
  val defaultBg = Color.white
  val textBg = Color.white
  val textFg = new Color(32, 32, 32)
  val outputPaneFg = textFg
  val outputPaneBg = textBg
  val errorPaneFg = "red"
}

object Theme {
  val currentTheme: Theme = new Light
}
