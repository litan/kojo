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

package net.kogics.kojo.codex

import net.kogics.swill.Conversation
import net.kogics.kojo.util.Utils

trait TalkListener {
  def onStart()
  def onEvent(msg: String)
  def onFinish(success: Boolean)
}

object Talker {
  private val envS = System.getenv("CODEX_SERVER")
  val server = if (envS != null) envS else "http://www.kogics.net"
//  val server = "http://localhost"
  val Competition = "Competition"
}

class Talker(email: String, password: String, listener: TalkListener) {
  
  Conversation.server = Talker.server
  @volatile var _cancel = false

  def fireEvent(msg: String) {
    Utils.runInSwingThread {
      listener.onEvent(msg)
    }
  }

  def fireStart() {
    Utils.runInSwingThread {
      listener.onStart()
    }
  }

  def fireFinish(success: Boolean) {
    Utils.runInSwingThread {
      listener.onFinish(success)
    }
  }

  def fireProblem(msg: String) {
    throw new IllegalStateException(msg)
  }

  def checkCancel() {
    if (_cancel) {
      throw new IllegalStateException("Upload Cancelled.")
    }
  }

  def cancel() {
    _cancel = true
  }

  def upload(title: String, category: String, catData: String, code: String, file: java.io.File) {
    val uploadRunner = new Runnable {
      def run {
        try {
          fireStart()

          if (title == null || title.trim == "") {
            fireProblem("Please provide a Title (above) before uploading your sketch.")
          }

          if (Talker.Competition == category && (catData == null || catData.trim == "")) {
            fireProblem("Please provide a Competition Number (above) before uploading your sketch.")
          }
        
          val conv = new Conversation
          fireEvent(Utils.loadString(classOf[Talker], "Talker.login", Talker.server))
          try {
            conv.go("/login")
          }
          catch {
            case t: Throwable => fireProblem(t.getMessage)
          }

          checkCancel()

          conv.formField("email", email)
          conv.formField("password", password)
          try {
            conv.formSubmit()
            // redirects to code exchange page after login
            conv.title("Code Exchange")
            fireEvent(Utils.loadString(classOf[Talker], "Talker.login.success"))
          }
          catch {
            case ex: RuntimeException => fireProblem(Utils.loadString(classOf[Talker], "Talker.login.error"))
            case t: Throwable => fireProblem(t.getMessage)
          }

          checkCancel()

          fireEvent(Utils.loadString(classOf[Talker], "Talker.upload.init"))
          try {
            conv.go("/codeupload")
          }
          catch {
            case t: Throwable => fireProblem(t.getMessage)
          }

          checkCancel()

          conv.formField("title", title)
          conv.formField("code", code)
          if (catData == null || catData.trim == "") {
            conv.formField("category", category)
          }
          else {
            conv.formField("category", category + "-" + catData)
          }
          conv.formField("image", file)
          fireEvent(Utils.loadString(classOf[Talker], "Talker.upload.start"))
          try {
            conv.formSubmit()
            conv.title("Code Exchange")
            fireEvent(Utils.loadString(classOf[Talker], "Talker.upload.success"))
            fireFinish(true)
          }
          catch {
            case ex: RuntimeException => fireProblem(Utils.loadString(classOf[Talker], "Talker.upload.error"))
            case t: Throwable => fireProblem(t.getMessage)
          }
        }
        catch {
          case t: Throwable =>
            fireEvent(t.getMessage)
            fireFinish(false)
        }
      }
    }

    new Thread(uploadRunner).start()
  }
}
