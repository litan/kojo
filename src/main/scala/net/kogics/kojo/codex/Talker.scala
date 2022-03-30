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

import net.kogics.kojo.util.Utils

trait TalkListener {
  def onStart(): Unit
  def onEvent(msg: String): Unit
  def onFinish(success: Boolean): Unit
}

object Talker {
  private val envS = System.getenv("CODEX_SERVER")
  val server = if (envS != null) envS else "https://codex.kogics.net"
//  val server = "http://localhost"
  val Competition = "Competition"
}

class Talker(email: String, password: String, listener: TalkListener) {
  
  @volatile var _cancel = false

  def fireEvent(msg: String): Unit = {
    Utils.runInSwingThread {
      listener.onEvent(msg)
    }
  }

  def fireStart(): Unit = {
    Utils.runInSwingThread {
      listener.onStart()
    }
  }

  def fireFinish(success: Boolean): Unit = {
    Utils.runInSwingThread {
      listener.onFinish(success)
    }
  }

  def fireProblem(msg: String): Unit = {
    throw new IllegalStateException(msg)
  }

  def checkCancel(): Unit = {
    if (_cancel) {
      throw new IllegalStateException("Upload Cancelled.")
    }
  }

  def cancel(): Unit = {
    _cancel = true
  }

  def upload(title: String, category: String, catData: String, code: String, file: java.io.File): Unit = {
    val uploadRunner = new Runnable {
      def run: Unit = {
        try {
          fireStart()

          if (title == null || title.trim == "") {
            fireProblem("Please provide a Title (above) before uploading your sketch.")
          }

          if (Talker.Competition == category && (catData == null || catData.trim == "")) {
            fireProblem("Please provide a Competition Number (above) before uploading your sketch.")
          }
        
          val session = new CodexSession(Talker.server)
          fireEvent(Utils.loadString(classOf[Talker], "Talker.login", Talker.server))

          try {
            val resp = session.login(email, password)
            fireEvent(Utils.loadString(classOf[Talker], "Talker.login.success"))
          }
          catch {
            case ex: RuntimeException => fireProblem(Utils.loadString(classOf[Talker], "Talker.login.error"))
            case t: Throwable => fireProblem(t.getMessage)
          }

          checkCancel()

          fireEvent(Utils.loadString(classOf[Talker], "Talker.upload.init"))

          val cat = if (Talker.Competition != category) category else category + "-" + catData
          fireEvent(Utils.loadString(classOf[Talker], "Talker.upload.start"))
          try {
            session.upload(title, cat, code, file)
            fireEvent(Utils.loadString(classOf[Talker], "Talker.upload.success"))
            fireFinish(true)
          }
          catch {
            case _: UploadTooBigException => fireProblem("The drawing is too big to upload. Try reducing your canvas size.")
            case _: RuntimeException => fireProblem(Utils.loadString(classOf[Talker], "Talker.upload.error"))
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
