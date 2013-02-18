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

package net.kogics.kojo.story

import java.awt.Desktop
import java.net.URL
import javax.swing._
import javax.swing.event._
import net.kogics.kojo.util.Utils

class LinkListener(st: StoryTeller) extends HyperlinkListener {
  val linkRegex = """(?i)http://localpage/(\d+)#?(\d*)""".r
  val linkPnameRegex = """(?i)http://localpage/([\w-]+)#?(\d*)""".r
  val handlerLinkRegex = """(?i)http://runHandler/(\w+)\/?(\w*)""".r

  def localpageLocation(url: String): (Int, Int) = {
    url match {
      case linkRegex(page, para) =>
        (page.toInt, if (para=="") 1 else para.toInt)
      case linkPnameRegex(pageName, para) =>
        val pageNum = st.pageNumber(pageName)
        if (pageNum.isDefined)
          (pageNum.get, if (para=="") 1 else para.toInt)
        else
          throw new IllegalArgumentException()
      case _ =>
        throw new IllegalArgumentException()
    }
  }
  
  // extract handler and data from runhandler url.
  def handlerData(url: String): (String, String) = {
    url.trim match {
      case handlerLinkRegex(handler, data) =>
        (handler, data)
      case _ =>
        throw new IllegalArgumentException()
    }
  }
  
  // satisfy url click
  def gotoUrl(url: URL) = Utils.runInSwingThread {
    if (url.getProtocol == "http") {
      if (url.getHost.toLowerCase == "localpage") {
        try {
          val loc = localpageLocation(url.toString)
          st.viewPage(loc._1, loc._2)
        }
        catch {
          case ex: IllegalArgumentException =>
            st.showStatusError("Invalid page/view in Link - " + url.toString)
          case t: Throwable =>
            st.showStatusError("Problem handling Url - %s: %s" format(url.toString, t.getMessage))
        }
      }
      else if (url.getHost.toLowerCase == "runhandler")  {
        try {
          val d = handlerData(url.toString)
          st.handleLink(d._1, d._2)
        }
        catch {
          case ex: IllegalArgumentException =>
            st.showStatusError("Invalid RunHandler Url - " + url.toString)
          case t: Throwable =>
            st.showStatusError("Problem handling Url - %s: %s" format(url.toString, t.getMessage))
        }
      }
      else {
        try {
            Desktop.getDesktop().browse(url.toURI)
        }
        catch {
          case t: Throwable =>
            st.showStatusError("Problem browsing Url - %s: %s" format(url.toString, t.getMessage))
        }
      }
    }
    else {
      st.showStatusError("Trying to use link with unsupported protocol - " + url.getProtocol)
    }
  }

  def handleLinkEnterExit(url: URL, enter: Boolean) {
    if (url.getHost.toLowerCase == "runhandler") {
      try {
        val d = handlerData(url.toString)
        if (enter) {
          st.handleLinkEnter(d._1, d._2)
        }
        else {
          st.handleLinkExit(d._1, d._2)
        }
      }
      catch {
        case ex: IllegalArgumentException =>
          st.showStatusError("Invalid RunHandler Url - " + url.toString)
        case t: Throwable =>
          st.showStatusError("Problem handling Url - %s: %s" format (url.toString, t.getMessage))
      }
    }
  }

  def hyperlinkUpdate(e: HyperlinkEvent) {
    if (e.getEventType == HyperlinkEvent.EventType.ACTIVATED) {
      gotoUrl(e.getURL)
    }
    else if (e.getEventType == HyperlinkEvent.EventType.ENTERED) {
      handleLinkEnterExit(e.getURL, true)
      st.showStatusMsg(e.getURL.toString, false)
    }
    else if (e.getEventType == HyperlinkEvent.EventType.EXITED) {
      handleLinkEnterExit(e.getURL, false)
      st.clearStatusBar()
    }
  }

  // for tests
  private [story] def setStory(story: Story) {
    st.currStory = Some(story)
  }
}
