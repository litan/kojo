/*
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo.lite

import java.awt.Color

import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLightLaf
import net.kogics.kojo.core
import net.kogics.kojo.util.Utils
import org.fife.ui.rsyntaxtextarea.{ Theme => RTheme }

trait Theme {
  def outputPaneFg: Color
  def outputPaneBg: Color
  def canvasBg: Color
  def loadEditorTheme: RTheme
  def loadLookAndFeel(): Unit
  def loadDefaultPerspective(kojoCtx: core.KojoCtx): Unit
  def toolbarBg: Option[Color]
  def defaultBg: Color
  def textBg: Color
  def textFg: Color
  def errorPaneFg: String
  def errorPaneBg: String
  def errorColor: Color
  def successColor: Color
  def neutralColor: Color
  def tracingBg: Color
  def tracingHighlightColor: Color
  def tracingCallColor: String
  def canvasAxesColor: Color
  def canvasGridColor: Color
  def canvasTickColor: Color
  def canvasTickLabelColor: Color
  def canvasTickIntegerLabelColor: Color
  def runPng: String
  def runwPng: String
  def runtPng: String
  def stopPng: String
  def checkPng: String
  def clearSePng: String
  def clearOwPng: String
  def supportsRithica: Boolean
}

class DarkTheme extends Theme {
  val canvasBg = new Color(0x424647)
  def loadEditorTheme = RTheme.load(getClass.getResourceAsStream("dark-editor-theme.xml"))
  def loadLookAndFeel(): Unit = {
    if (Utils.isMac) {
      System.setProperty("apple.laf.useScreenMenuBar", "true")
    }
    FlatDarkLaf.setup()
  }
  def loadDefaultPerspective(kojoCtx: core.KojoCtx): Unit = {
    kojoCtx.switchToDefault2Perspective()
  }
  val toolbarBg = None
  val defaultBg = new Color(0x3c3f41)
  val textBg = defaultBg
  val textFg = new Color(0xbbbbbb)
  val outputPaneFg = textFg
  val outputPaneBg = new Color(0x2d2d2d)
  val errorPaneFg = "#F42E2E"
  val errorPaneBg = "#2d2d2d"
  val errorColor = new Color(0xf42e2e)
  val successColor = new Color(0x06cc06)
  val neutralColor = defaultBg
  val tracingBg = defaultBg
  val tracingHighlightColor = new Color(173, 206, 238)
  val tracingCallColor = "rgb(0,200,50)"
  val canvasAxesColor = new Color(200, 200, 200)
  val canvasGridColor = new Color(100, 100, 100)
  val canvasTickColor = new Color(120, 120, 120)
  val canvasTickLabelColor = new Color(150, 150, 150)
  val canvasTickIntegerLabelColor = textFg
  val runPng = "run-d.png"
  val runwPng = "runw-d.png"
  val runtPng = "runt-d.png"
  val stopPng = "stop-d.png"
  val checkPng = "check-d.png"
  val clearSePng = "clears-d.png"
  val clearOwPng = "clear-d.png"
  val supportsRithica = false
}

class LightTheme extends Theme {
  val canvasBg = Color.white
  def loadEditorTheme = RTheme.load(getClass.getResourceAsStream("light-editor-theme.xml"))
  def loadLookAndFeel(): Unit = {
    if (Utils.isMac) {
      System.setProperty("apple.laf.useScreenMenuBar", "true")
    }
    FlatLightLaf.setup()
  }
  def loadDefaultPerspective(kojoCtx: core.KojoCtx): Unit = {
    kojoCtx.switchToDefault2Perspective()
  }

  val toolbarBg = Some(new Color(230, 230, 230))
  val defaultBg = Color.white
  val textBg = Color.white
  val textFg = new Color(32, 32, 32)
  val outputPaneFg = textFg
  val outputPaneBg = textBg
  val errorPaneFg = "rgb(240, 0, 0)"
  val errorPaneBg = "white"
  val errorColor = new Color(0xff1a1a)
  val successColor = new Color(0x33ff33)
  val neutralColor = new Color(0xf0f0f0)
  val tracingBg = Color.white
  val tracingHighlightColor = new Color(173, 206, 238)
  val tracingCallColor = "rgb(0,50,225)"
  val canvasAxesColor = new Color(100, 100, 100)
  val canvasGridColor = new Color(200, 200, 200)
  val canvasTickColor = new Color(150, 150, 150)
  val canvasTickLabelColor = new Color(50, 50, 50)
  val canvasTickIntegerLabelColor = Color.blue
  val runPng = "run.png"
  val runwPng = "runw.png"
  val runtPng = "runt.png"
  val stopPng = "stop.png"
  val checkPng = "check.png"
  val clearSePng = "clears.png"
  val clearOwPng = "clear.png"
  val supportsRithica = true
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
