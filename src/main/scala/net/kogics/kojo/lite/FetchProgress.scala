/*
 * Copyright (C) 2026
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
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

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GraphicsEnvironment
import java.util.concurrent.CountDownLatch

import javax.swing.border.EmptyBorder
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JProgressBar
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

/** How the toolchain download reports itself. */
trait FetchProgress {
  /** Called before preparing the client or opening the first connection. */
  def connecting(): Unit = ()
  def message(msg: String): Unit
  def startJar(name: String, totalBytes: Long): Unit
  def bytes(n: Long): Unit
  def finished(): Unit
  /** The launcher may wait for acknowledgement; headless callers continue immediately. */
  def downloadFailed(): Unit = ()

  @volatile private var cancelledFlag = false
  private var cancelAction: () => Unit = () => ()

  def cancelled: Boolean = cancelledFlag

  def cancel(): Unit = {
    val action = synchronized {
      cancelledFlag = true
      val active = cancelAction
      cancelAction = () => ()
      active
    }
    action()
  }

  /** Register the current request, or cancel it immediately if already stopped. */
  private[lite] def onCancel(action: () => Unit): Unit = {
    val runNow = synchronized {
      if (cancelled) true
      else {
        cancelAction = action
        false
      }
    }
    if (runNow) action()
  }
}

/** The default: just log. Used by the tests and by headless runs. */
object ConsoleProgress extends FetchProgress {
  def message(msg: String): Unit = println(msg)
  def startJar(name: String, totalBytes: Long): Unit = println(s"[INFO] ... $name")
  def bytes(n: Long): Unit = ()
  def finished(): Unit = ()
}

/**
 * A small progress window for the launcher JVM, which has no Kojo UI yet -
 * StubMain already puts up a JOptionPane there for the Java version check.
 *
 * The label is shown in the language whose toolchain is being fetched (that is
 * the language the user has chosen), falling back to English; the log line
 * alongside it stays in English.
 *
 * Every UI touch is posted to the EDT (never invokeAndWait). Only the launcher
 * thread waits for failure acknowledgement; bar updates are throttled,
 * so a 20 MB fetch doesn't flood the event queue with repaints.
 */
class SwingFetchProgress(downloading: String,
    failurePrompt: DownloadFailurePrompt = LanguageRegistry.defaultDownloadFailurePrompt) extends FetchProgress {
  private var dialog: JDialog = _
  private var finishedFlag = false
  private var bar: JProgressBar = _
  private var label: JLabel = _
  private var actions: JPanel = _
  private val failureAcknowledged = new CountDownLatch(1)
  private var received = 0L
  private var total = 0L
  private var lastPainted = 0L

  private def onEdt(body: => Unit): Unit = SwingUtilities.invokeLater(new Runnable {
    def run(): Unit = body
  })

  // Called only on the EDT.
  private def ensureDialog(): Unit = {
    if (dialog == null && !cancelled && !finishedFlag) {
      label = new JLabel(s"$downloading...")
      bar = new JProgressBar(0, 100)
      bar.setIndeterminate(true)
      bar.setPreferredSize(new Dimension(320, 18))
      val content = new JPanel()
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS))
      content.setBorder(new EmptyBorder(14, 16, 14, 16))
      content.add(label)
      content.add(javax.swing.Box.createVerticalStrut(10))
      content.add(bar)
      actions = new JPanel(new FlowLayout(FlowLayout.CENTER))
      val proceed = new JButton(failurePrompt.continueLabel)
      proceed.addActionListener(_ => failureAcknowledged.countDown())
      actions.add(proceed)
      actions.setVisible(false)
      content.add(actions)
      dialog = new JDialog(null: java.awt.Frame, "Kojo", false)
      // closing the window cancels the download; Kojo then starts on stock Scala
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
      dialog.addWindowListener(new java.awt.event.WindowAdapter {
        override def windowClosing(e: java.awt.event.WindowEvent): Unit = {
          cancel()
          failureAcknowledged.countDown()
        }
      })
      dialog.getContentPane.add(content, BorderLayout.CENTER)
      dialog.getRootPane.setDefaultButton(proceed)
      dialog.pack()
      dialog.setLocationRelativeTo(null)
      dialog.setVisible(true)
    }
  }

  def message(msg: String): Unit = println(msg)

  override def connecting(): Unit = onEdt { ensureDialog() }

  override def downloadFailed(): Unit = {
    require(!SwingUtilities.isEventDispatchThread, "Download failure acknowledgement must wait outside the EDT")
    onEdt {
      try {
        if (cancelled || finishedFlag || dialog == null) failureAcknowledged.countDown()
        else {
          label.setText("<html><body style='width: 360px'>" + failurePrompt.message.replace("\n", "<br>") + "</body></html>")
          bar.setVisible(false)
          actions.setVisible(true)
          dialog.pack()
        }
      }
      catch {
        case scala.util.control.NonFatal(e) =>
          message(s"[WARNING] Could not show the toolchain download failure: ${e.getMessage}")
          failureAcknowledged.countDown()
      }
    }
    failureAcknowledged.await()
  }

  def startJar(name: String, totalBytes: Long): Unit = {
    println(s"[INFO] ... $name")
    received = 0L
    total = totalBytes
    lastPainted = 0L
    onEdt {
      ensureDialog()
      if (label != null) label.setText(s"$downloading: $name")
      if (bar != null) {
        bar.setIndeterminate(totalBytes <= 0)
        if (totalBytes > 0) bar.setValue(0)
      }
    }
  }

  def bytes(n: Long): Unit = {
    received += n
    // repaint at most once per percent, and only when the size is known
    if (total > 0) {
      val pct = (received * 100 / total).toInt
      if (pct != lastPainted) {
        lastPainted = pct
        onEdt(if (bar != null) bar.setValue(pct))
      }
    }
  }

  def finished(): Unit = onEdt {
    finishedFlag = true
    failureAcknowledged.countDown()
    if (dialog != null) {
      dialog.setVisible(false)
      dialog.dispose()
      dialog = null
    }
  }
}

object FetchProgress {

  /** A window when there is a display to put it on, plain logging otherwise. */
  def forLauncher(message: String = LanguageRegistry.defaultDownloadMessage,
      failurePrompt: DownloadFailurePrompt = LanguageRegistry.defaultDownloadFailurePrompt): FetchProgress =
    if (GraphicsEnvironment.isHeadless) ConsoleProgress
    else
      try new SwingFetchProgress(message, failurePrompt)
      catch { case _: Throwable => ConsoleProgress }
}
