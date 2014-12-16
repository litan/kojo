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

package net.kogics.swill

import java.util.logging._
import com.meterware.httpunit._
import scala.util.matching.Regex

object Conversation {
  HttpUnitOptions.setScriptingEnabled(false)
  val tid = new ThreadLocal[Int]() {
    override def initialValue = 1
  }
  @volatile var _server: String = _ // server host and port e.g. http://localhost:8081

  def server_=(s: String ) = synchronized {
    if (_server == null) {
      _server = s
    }
  }

  def server = _server
}

class Conversation {
  val Log = Logger.getLogger(getClass.getName)
  
  import Conversation._
  val wc = new WebConversation
  var response: WebResponse = _
  val rg = new java.util.Random()

//  does not seem to work
//  def setProxyServer(host: String, port: Int) {
//    wc.setProxyServer(host, port)
//  }

  def go(relativeUrl: String) {
    val url = server + relativeUrl
    Log.info("[Thread %d] Going to page - %s" format(tid.get, url))
    response = wc.getResponse(url);
  }

  def click(link: WebLink) {
    response = link.click()
  }

  def find(regex: String) {
    Log.info("[Thread %d] Finding - %s" format(tid.get, regex))
    if (regex.r.findFirstIn(response.getText) == None) {
      throw new RuntimeException("[Thread %d] Not found - %s\nResponse Text:\n%s"
                                 format(tid.get, regex, response.getText))
    }
  }

  def notFind(regex: String) {
    Log.info("[Thread %d] Not Finding - %s" format(tid.get, regex))
    if (regex.r.findFirstIn(response.getText) != None) {
      throw new RuntimeException("[Thread %d] Found - %s" format(tid.get, regex))
    }
  }

  def url(regex: String) {
    Log.info("[Thread %d] Matching current URL - %s" format(tid.get, regex))
    val url = response.getURL.toString
    if (regex.r.findFirstIn(url) == None) {
      throw new RuntimeException("[Thread %d] Actual URL: %s - does not match: %s" format(tid.get, url, regex))
    }
  }

  def title(regex: String) {
    Log.info("[Thread %d] Matching current Title - %s" format(tid.get, regex))
    val url = response.getTitle.toString
    if (regex.r.findFirstIn(url) == None) {
      throw new RuntimeException("[Thread %d] Actual Title: %s - does not match: %s" format(tid.get, url, regex))
    }
  }

  def code(n: Int) {
    Log.info("[Thread %d] Checking current Code - %d" format(tid.get, n))
    val cd = response.getResponseCode
    if (cd != n) {
      throw new RuntimeException("[Thread %d] Expected code: %d, Actual code: %d" format(tid.get, n, cd))
    }
  }

  def responseText() = response.getText

  def form0 = response.getForms()(0)

  def formSubmit() {
    Log.info("[Thread %d] Submitting Form" format(tid.get))
    form0.getButtons()(0).click()
    response = wc.getCurrentPage()
  }

  def formField(name: String, value: String) {
    Log.info("[Thread %d] Setting Form Field: %s to %s" format(tid.get, name, value))
    form0.setParameter(name, value)
  }

  def formField(name: String, value: java.io.File) {
    Log.info("[Thread %d] Setting Form Field: %s to %s" format(tid.get, name, value))
    form0.setParameter(name, value)
  }

  def file(name: String) = new java.io.File(name)

  def uuid: String = synchronized {
    java.util.UUID.randomUUID().toString()
  }

  def random = rg.nextInt.toString
}
