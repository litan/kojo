/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.kojo
package action

import java.awt.Color
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JColorChooser
import net.kogics.kojo.core.KojoCtx
import net.kogics.kojo.util.Utils
import lite.CodeExecutionSupport
import java.awt.GraphicsEnvironment
import javax.swing.JFrame
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JCheckBoxMenuItem

class ChooseColor(ctx: KojoCtx) extends AbstractAction(Utils.loadString("S_ChooseColor")) {
  def actionPerformed(e: ActionEvent) {
    val sColor = JColorChooser.showDialog(null, util.Utils.stripDots(e.getActionCommand), ctx.lastColor)
    if (sColor != null) {
      val cprint = CodeExecutionSupport.instance.showOutput(_: String, _: Color)
      cprint("\u2500" * 3 + "\n", sColor)
      println("Selected Color:   ")
      // cprint("\u2588" * 6 + "\n", sColor)
      val color = if (sColor.getAlpha < 255) {
        "Color(%d, %d, %d, %d)" format (sColor.getRed, sColor.getGreen, sColor.getBlue, sColor.getAlpha)
      }
      else {
        "Color(%d, %d, %d)" format (sColor.getRed, sColor.getGreen, sColor.getBlue)
      }
      println(color)
      println("Example usage: setPenColor(%s)" format (color))
      cprint("\u2500" * 3 + "\n", sColor)
      ctx.lastColor = sColor
    }
  }
}

object CloseFile {
  var action: Action = _
  def onFileOpen() {
    action.setEnabled(true)
  }
  def onFileClose() {
    action.setEnabled(false)
  }
}

class CloseFile
  extends AbstractAction(Utils.loadString("S_Close"), Utils.loadIcon("/images/extra/close.gif")) {
  setEnabled(false)
  CloseFile.action = this

  def actionPerformed(e: ActionEvent) {
    try {
      CodeExecutionSupport.instance.closeFileAndClrEditor()
    }
    catch {
      case e: RuntimeException => // user cancelled
    }
  }
}

class NewFile(ctx: KojoCtx)
  extends AbstractAction(Utils.loadString("S_New"), Utils.loadIcon("/images/extra/new.gif")) {

  val saveAs = new SaveAs(ctx)

  def actionPerformed(e: ActionEvent) {
    try {
      CodeExecutionSupport.instance.closeFileAndClrEditor()
      saveAs.actionPerformed(e);
    }
    catch {
      case e: RuntimeException => // user cancelled
    }
  }
}

object FullScreenAction {
  var menuItems: Seq[JCheckBoxMenuItem] = Vector()
  lazy val sdev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
  var frame: JFrame = _

  def apply(kojoCtx: => KojoCtx) = new FullScreenAction(kojoCtx)
  def linkMenu(menuItem: JCheckBoxMenuItem) {
    menuItems :+= menuItem
  }
}

class FullScreenAction(kojoCtx: => KojoCtx)
  extends AbstractAction(Utils.loadString("S_FullScreenCanvas")) {
  import FullScreenAction._
  lazy val dch = kojoCtx.topcs.dch

  def isFullScreen = sdev.getFullScreenWindow != null

  def enterFullScreen() {
    frame = new JFrame
    frame.setUndecorated(true)
    frame.getContentPane.add(dch.dc)
    sdev.setFullScreenWindow(frame)
    frame.validate()

    dch.dc.addKeyListener(new KeyAdapter {
      override def keyPressed(event: KeyEvent) {
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
          dch.dc.removeKeyListener(this)
          leaveFullScreen()
        }
      }
    })
    dch.dc.requestFocusInWindow()
    menuItems foreach { _ setSelected true }
  }

  def leaveFullScreen() {
    sdev.setFullScreenWindow(null)
    frame.setVisible(false)
    dch.add(dch.dc)
    dch.dc.revalidate()
    menuItems foreach { _ setSelected false }
  }

  def actionPerformed(e: ActionEvent) {
    val source = e.getSource().asInstanceOf[JCheckBoxMenuItem]
    if (source.isSelected()) {
      enterFullScreen()
    }
    else {
      leaveFullScreen()
    }
  }
}


