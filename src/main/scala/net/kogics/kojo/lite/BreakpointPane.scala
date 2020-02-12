package net.kogics.kojo.lite

import java.awt.Component

import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder

import net.kogics.kojo.core
import net.kogics.kojo.widget.ColPanel

object BreakpointPane {
  var dlg: BreakpointPane = _
  var escapeStop = false
  var scriptRunning = false

  def onRunStart(): Unit = {
    scriptRunning = true
    escapeStop = false
  }

  def onRunDone(): Unit = {
    scriptRunning = false
  }

  def show(msg: Any, resumeMsg: String, kojoCtx: core.KojoCtx) = {
    if (dlg == null) {
      dlg = new BreakpointPane(kojoCtx.frame)
    }

    def showDlg(): Unit = {
      kojoCtx.activateOutputPane()
      escapeStop = false
      if (dlg.show(msg, resumeMsg) == 0) {
        escapeStop = true
        kojoCtx.stopScript()
      }
    }
    if (escapeStop) {
      if (scriptRunning) {
        showDlg()
      }
    }
    else {
      showDlg()
    }
  }
}

class BreakpointPane(owner: JFrame) extends JDialog(owner) {
  var btnPressed = false

  setTitle("Breakpoint")
  setModal(true)
  val d = new JButton("Resume")
  d.setAlignmentX(Component.CENTER_ALIGNMENT)
  d.addActionListener { _ =>
    btnPressed = true
    setVisible(false)
  }
  val label = new JLabel()
  label.setBorder(new EmptyBorder(0, 10, 0, 10))
  val sp = new JScrollPane(label)
  getRootPane.setDefaultButton(d)
  val panel = ColPanel(sp, d)
  getContentPane.add(panel)
  setSize(400, 120)
  setLocationRelativeTo(owner)

  def show(msg: Any, resumeMsg: String): Int = {
    setVisible(false)
    btnPressed = false
    label.setText(msg.toString)
    setTitle(resumeMsg)
    setVisible(true)
    if (btnPressed) 1 else 0
  }
}
