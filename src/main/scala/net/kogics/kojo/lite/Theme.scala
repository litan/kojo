package net.kogics.kojo.lite

import java.awt.Color

trait Theme {
  def outputPaneFg: Color
  def outputPaneBg: Color
  def canvasBg: Color
}

class Dark extends Theme {
  val outputPaneFg = Color.lightGray
  val outputPaneBg = new Color(0x2d2d2d)
  val canvasBg = new Color(0x3c3f41)
}

class Light extends Theme {
  val outputPaneFg = new Color(32, 32, 32)
  val outputPaneBg = Color.white
  val canvasBg = Color.white
}

object Theme {
  val currentTheme = new Dark
}
