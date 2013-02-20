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
package story

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.util.logging.Logger

import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.text.html.HTMLDocument

import net.kogics.kojo.util.Read

import util.Utils

class StoryTeller(val kojoCtx: core.KojoCtx) extends JPanel with music.Mp3Player {
  val Log = Logger.getLogger(getClass.getName);
  val NoText = <span/>
  @volatile var currStory: Option[Story] = None
  @volatile var savedStory: Option[Story] = None

  def running = currStory.isDefined
  def story = currStory.get

  var outputFn: String => Unit = { msg =>
    Log.info(msg)
  }

  val pageFields = new collection.mutable.HashMap[String, JTextField]()
  val defaultMsg =
    <div style="text-align:center;color:#808080;font-size:15px">
      { for (idx <- 1 to 6) yield { <br/> } }
      <p>
        Run a story by loading/writing your story script within the <em>Script Editor</em>, 
        and then clicking the <em>Run</em> button.
      </p>
      <p>
        You can control a running story via buttons that appear at the bottom of this pane.
      </p>
    </div>

  //  setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
  setLayout(new BorderLayout())

  val ep = new JEditorPane()
  ep.setEditorKit(CustomHtmlEditorKit())

  ep.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
  ep.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20))
  ep.setEditable(false)
  ep.addHyperlinkListener(new LinkListener(this))
  ep.setBackground(Color.white)
  ep.setBorder(BorderFactory.createEmptyBorder())
  val sp = new JScrollPane(ep)
  sp.setBorder(BorderFactory.createEmptyBorder())
  add(sp, BorderLayout.CENTER)

  val holder = new JPanel()
  holder.setBackground(Color.white)
  holder.setLayout(new BoxLayout(holder, BoxLayout.Y_AXIS))

  var uc = new JPanel
  //  var ucSp = new JScrollPane(uc)

  def removeOldUc() {
    holder.remove(uc)
  }

  def addNewUc() {
    uc = new JPanel
    uc.setBackground(Color.white)
    holder.add(uc, 0)
  }

  val (cp, prevButton, nextButton) = makeControlPanel()
  holder.add(cp)

  val statusBar = new JLabel()
  val pageNumBar = new JLabel()
  pageNumBar.setBorder(BorderFactory.createEtchedBorder())
  //  statusBar.setPreferredSize(new Dimension(100, 16))
  val sHolder = new JPanel()
  sHolder.setLayout(new BorderLayout())
  sHolder.add(statusBar, BorderLayout.CENTER)
  sHolder.add(pageNumBar, BorderLayout.EAST)

  holder.add(sHolder)
  add(holder, BorderLayout.SOUTH)
  displayContent(defaultMsg)

  def makeControlPanel(): (JPanel, JButton, JButton) = {
    val cp = new JPanel
    cp.setBackground(Color.white)
    cp.setBorder(BorderFactory.createEtchedBorder())

    val prevBut = new JButton()
    prevBut.setIcon(Utils.loadIcon("/images/back.png"))
    prevBut.setToolTipText("Previous View")
    prevBut.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        prevPage()
      }
    })
    cp.add(prevBut)

    val stopBut = new JButton()
    stopBut.setIcon(Utils.loadIcon("/images/stop.png"))
    stopBut.setToolTipText("Stop Story")
    stopBut.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        stop()
      }
    })
    cp.add(stopBut)

    val nextBut = new JButton()
    nextBut.setIcon(Utils.loadIcon("/images/forward.png"))
    nextBut.setToolTipText("Next View")
    nextBut.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        nextPage()
      }
    })
    cp.add(nextBut)

    cp.setVisible(false)
    (cp, prevBut, nextBut)
  }

  def ensureVisible() {
    kojoCtx.makeStoryTellerVisible()
  }

  def updateCp() {
    if (story.hasPrevView) {
      prevButton.setEnabled(true)
    }
    else {
      prevButton.setEnabled(false)
    }

    if (story.hasNextView) {
      nextButton.setEnabled(true)
    }
    else {
      nextButton.setEnabled(false)
    }
  }

  private def clearHelper() {
    // needs to run on GUI thread
    newPage()
    displayContent(NoText)
  }

  def showCurrStory() = Utils.runInSwingThread {
    newPage()
    displayContent(story.view)
    updateCp()
  }

  private def prevPage() {
    // needs to run on GUI thread
    kojoCtx.stopInterpreter()
    newPage()
    if (story.hasPrevView) {
      story.back()
      displayContent(story.view)
      updateCp()
    }
    else {
      stop()
    }
  }

  def nextPage() = Utils.runInSwingThread {
    kojoCtx.stopInterpreter()
    newPage()

    if (story.hasNextView) {
      story.forward()
      displayContent(story.view)
      updateCp()
    }
    else {
      stop()
    }
  }

  def viewPage(page: Int, view: Int) {
    // needs to run on GUI thread. Currently called only from link handler (in GUI thread)
    kojoCtx.stopInterpreter()
    newPage()

    if (story.hasView(page, view)) {
      story.goto(page, view)
      displayContent(story.view)
      updateCp()
    }
    else {
      showStatusError("Nonexistent page#view - %d#%d" format (page, view))
    }
  }

  private def newPage() {
    // needs to run on GUI thread
    removeOldUc()
    addNewUc()
    pageFields.clear()
    //    clearStatusBar()
    holder.revalidate()
    sp.revalidate()
    repaint()

    kojoCtx.stopActivity()
    stopMp3()
  }

  def clear() = Utils.runInSwingThread {
    if (currStory.isDefined) {
      done()
    }
    clearHelper()
    ensureVisible()
    cp.setVisible(true)
    val doc = ep.getDocument.asInstanceOf[HTMLDocument]
    doc.setBase(new java.net.URL("file:///" + kojoCtx.baseDir))
    ep.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
  }

  def pageNumber(name: String): Option[Int] = story.pageNumber(name)

  def onStop(story: Story, fn: => Unit) {
    story.onStop(fn)
  }

  def stop() = Utils.runInSwingThread {
    kojoCtx.stopInterpreter()
    done()
  }

  private def done() = {
    // call in gui thread
    currStory foreach { _.stop }
    if (savedStory.isDefined) {
      currStory = savedStory
      savedStory = None
      showCurrStory()
    }
    else {
      currStory = None
      clearHelper()
      stopMp3Loop()
      cp.setVisible(false)
      displayContent(defaultMsg)
    }
  }

  private def scrollEp() {
    if (currStory.isDefined) {
      if (story.scrollToEnd) {
        scrollToEnd()
      }
      else {
        scrollToBeginning()
      }
    }
    else {
      scrollToBeginning()
    }
  }

  private def scrollToEnd() {
    Utils.schedule(0.3) {
      val sb = sp.getVerticalScrollBar
      sb.setValue(sb.getMaximum)
    }
  }

  private def scrollToBeginning() {
    Utils.schedule(0.3) {
      val sb = sp.getVerticalScrollBar
      sb.setValue(sb.getMinimum)
    }
  }

  private def displayContent(html: xml.Node) = Utils.runInSwingThread {
    clearStatusBar()
    ep.setText(html.toString)
    scrollEp()
  }

  def addField(label: String, deflt: Any): JTextField = {
    val l = new JLabel(label)
    val default = deflt.toString
    val tf = new JTextField(default, if (default.length < 6) 6 else default.length)

    Utils.runInSwingThread {
      uc.add(l)
      uc.add(tf)
      uc.setBorder(BorderFactory.createEtchedBorder())
      uc.revalidate()
      uc.repaint()
      pageFields += (label -> tf)
      scrollEp()
    }
    tf
  }

  def fieldValue[T](label: String, default: T)(implicit reader: Read[T]): T = Utils.runInSwingThreadAndWait {
    val tf = pageFields.get(label)
    if (tf.isDefined) {
      val svalue = tf.get.getText
      if (svalue != null && svalue.trim != "") {
        try {
          reader.read(svalue)
        }
        catch {
          case ex: Exception =>
            showStatusError("Unable to convert value - %s - to required type %s" format (svalue, reader.typeName))
            throw ex
        }
      }
      else {
        tf.get.setText(default.toString)
        default
      }
    }
    else {
      showStatusError("Field with label - %s is not defined" format (label))
      throw new IllegalArgumentException("Field with label - %s is not defined" format (label))
    }
  }

  def addButton(label: String)(fn: => Unit) {
    val but = new JButton(label)
    but.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        clearStatusBar()
        Utils.stopMonitoredThreads()
        Utils.runAsyncMonitored {
          fn
        }
      }
    })

    addUiComponent(but)
  }

  def addUiComponent(c: JComponent) = Utils.runInSwingThread {
    uc.add(c)
    uc.setBorder(BorderFactory.createEtchedBorder())
    uc.revalidate()
    uc.repaint()
    scrollEp()
  }

  def setUserControlsBg(color: Color) = Utils.runInSwingThread {
    uc.setBackground(color)
  }

  addComponentListener(new ComponentAdapter {
    override def componentResized(e: ComponentEvent) {
      statusBar.setPreferredSize(new Dimension(getSize().width - 6, 16))
    }
  })

  def clearStatusBar() = Utils.runInSwingThread {
    statusBar.setForeground(Color.black)
    statusBar.setText("")
    pageNumBar.setText(storyLocation)
  }

  def storyLocation = {
    if (currStory.isDefined) {
      if (savedStory.isDefined) {
        "St 2, Pg %d#%d" format (story.location._1, story.location._2)

      }
      else {
        "Pg %d#%d" format (story.location._1, story.location._2)
      }
    }
    else {
      ""
    }
  }

  def showStatusMsg(msg: String, output: Boolean = true) {
    Utils.runInSwingThread {
      statusBar.setForeground(Color.black)
      statusBar.setText(msg)
    }
    if (output) {
      outputFn("[Storyteller] %s\n" format (msg))
    }
  }

  def showStatusError(msg: String) {
    Utils.runInSwingThread {
      statusBar.setForeground(Color.red)
      statusBar.setText(msg)
    }
    outputFn("[Storyteller] %s\n" format (msg))
  }

  def playStory(story: Story) = Utils.runInSwingThread {
    if (currStory.isDefined) {
      println("Can't run more than one story.")
    }
    else {
      currStory = Some(story)
      showCurrStory()
    }
  }

  def storyComing() {
    ensureVisible()
    ep.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
  }

  def storyAborted() {
    ep.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
  }

  def addLinkHandler[T](name: String, story0: Story)(hm: HandlerHolder[T]) = {
    story0.addLinkHandler(name)(hm)
  }

  def addLinkEnterHandler[T](name: String, story0: Story)(hm: HandlerHolder[T]) = {
    story0.addLinkEnterHandler(name)(hm)
  }

  def addLinkExitHandler[T](name: String, story0: Story)(hm: HandlerHolder[T]) = {
    story0.addLinkExitHandler(name)(hm)
  }

  def handleLink(name: String, data: String) {
    story.handleLink(name, data)
  }

  def handleLinkEnter(name: String, data: String) {
    story.handleLinkEnter(name, data)
  }
  def handleLinkExit(name: String, data: String) {
    if (currStory.isDefined) { // work around link exits showing up after story ends 
      story.handleLinkExit(name, data)
    }
  }

  // mp3 player stuff
  val pumpEvents = false
  override def playMp3(mp3File: String) {
    if (running) {
      super.playMp3(mp3File)
    }
    else {
      throw new IllegalStateException("Trying to play story music without a running story.")
    }
  }
  override def playMp3Loop(mp3File: String) {
    if (running) {
      super.playMp3Loop(mp3File)
    }
    else {
      throw new IllegalStateException("Trying to play story music without a running story.")
    }
  }
  def showError(msg: String) = showStatusError(msg)
}
