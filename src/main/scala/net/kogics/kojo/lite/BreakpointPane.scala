package net.kogics.kojo.lite

import java.awt.Component

import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.ScrollPaneConstants
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

  def show(msg: Any, pauseMessage: String, resumeMsg: String, kojoCtx: core.KojoCtx) = {
    if (dlg == null) {
      dlg = new BreakpointPane(kojoCtx.frame)
    }

    def showDlg(): Unit = {
      kojoCtx.activateOutputPane()
      escapeStop = false
      if (dlg.show(msg, pauseMessage, resumeMsg) == 0) {
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

  val bkptMsglabel = new JLabel()
  val sp = new JScrollPane(bkptMsglabel)
  sp.setBorder(new EmptyBorder(15, 10, 5, 10))
  sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

  val resumeCancelLabel = new JLabel()
  resumeCancelLabel.setAlignmentX(Component.CENTER_ALIGNMENT)
  resumeCancelLabel.setBorder(new EmptyBorder(10, 10, 10, 10))

  val d = new JButton("Resume")
  d.setAlignmentX(Component.CENTER_ALIGNMENT)
  d.addActionListener { _ =>
    btnPressed = true
    setVisible(false)
  }
  getRootPane.setDefaultButton(d)

  val panel = ColPanel(sp, resumeCancelLabel, d)
  getContentPane.add(panel)

  def show(msg: Any, pauseMessage: String, resumeMsg: String): Int = {
    setVisible(false)
    btnPressed = false
    setTitle(pauseMessage)
    bkptMsglabel.setText(msg.toString)
    resumeCancelLabel.setText(resumeMsg + ", Escape to stop")
    pack()
    val sz = getSize
    setSize(500, sz.height)
    setLocationRelativeTo(owner)
    setVisible(true)
    if (btnPressed) 1 else 0
  }
}
