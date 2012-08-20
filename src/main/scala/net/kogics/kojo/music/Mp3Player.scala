/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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
package music

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import javax.swing.Timer
import javazoom.jl.player.Player
import java.io._
import util.Utils
import Utils.withLock
import Utils.giveupLock
import net.kogics.kojo.lite.canvas.SpriteCanvas


trait Mp3Player {
  val pumpEvents: Boolean
  def showError(msg: String)
  private val listener = SpriteCanvas.instance().megaListener // hack!

  @volatile private var mp3Player: Option[Player] = None
  @volatile private var bgmp3Player: Option[Player] = None
  private val playLock = new ReentrantLock
  private val stopped = playLock.newCondition
  private var stopBg = false
  private var stopFg = false
  // Todo - bad dependency
  private val KojoCtx = net.kogics.kojo.lite.KojoCtx.instance
  private var timer: Timer = _
  
  private def startPumpingEvents() {
    if (pumpEvents && timer == null) {
      listener.hasPendingCommands()
      timer = Utils.scheduleRec(0.5) {
        listener.hasPendingCommands()
      }
    }
  }

  private def stopPumpingEvents() {
    if (pumpEvents && bgmp3Player.isEmpty) {
      if (timer != null) {
        timer.stop()
        timer = null
      }
      listener.pendingCommandsDone()
      Utils.schedule(0.5) {
        listener.pendingCommandsDone()
      }
    }
  }
  
  private def playHelper(mp3File: String)(fn: (FileInputStream) => Unit) {
    val f = new File(mp3File)
    val f2 = if (f.exists) f else new File(KojoCtx.baseDir + mp3File)

    if (f2.exists) {
      val is = new FileInputStream(f2)
      fn(is)
//      is.close() - player closes the stream
    }
    else {
      showError("MP3 file does not exist - %s or %s" format(f.getAbsolutePath, f2.getAbsolutePath))
    }
  }

  def isMusicPlaying: Boolean = {
    withLock(playLock) {
      mp3Player.isDefined
    }
  }
  
  def playMp3(mp3File: String) {
    def done() {
      stopFg = false
      mp3Player = None
      stopPumpingEvents()
      stopped.signal()
    }
    withLock(playLock) {
      stopMp3()
      playHelper(mp3File) { is =>
        mp3Player = Some(new Player(is))
      }
      if (mp3Player.isDefined) {
        Utils.runAsync {
          withLock(playLock) {
            if (stopFg) {
              done()
            }
            else {
              val music = mp3Player.get
              giveupLock(playLock) {
                music.play()
              }
              done()
            }
          }
        }
        startPumpingEvents()
      }
    }
  }
  
  def playMp3Loop(mp3File: String) {

    def playLoop0() {
      Utils.runAsync {
        withLock(playLock) {
          if (stopBg) {
            stopBg = false
            bgmp3Player = None
            stopPumpingEvents()
            stopped.signal()
          }
          else {
            val music = bgmp3Player.get
            giveupLock(playLock) {
              music.play()
            }
            playHelper(mp3File) { is =>
              bgmp3Player = Some(new Player(is))
            }
            if (bgmp3Player.isDefined) {
              playLoop0()
            }
          }
        }
      }
    }

    withLock(playLock) {
      stopMp3Loop()
      playHelper(mp3File) { is =>
        bgmp3Player = Some(new Player(is))
      }
      if (bgmp3Player.isDefined) {
        playLoop0()
        startPumpingEvents()
      }
    }       
  }

  def stopMp3() {
    withLock(playLock) {
      if (mp3Player.isDefined) {
        stopFg = true
        if (!mp3Player.get.isComplete) {
          mp3Player.get.close()
        }
        while(stopFg) {
          val signalled = stopped.await(20, TimeUnit.MILLISECONDS)
          if (!signalled) {
            try {
              if (!mp3Player.get.isComplete) {
                mp3Player.get.close()
              }
            }
            catch {
              case t: Throwable => // do nothing
            }
          }
        }
      }
    }
  }

  def stopMp3Loop() {
    withLock(playLock) {
      if (bgmp3Player.isDefined) {
        stopBg = true
        if (!bgmp3Player.get.isComplete) {
          bgmp3Player.get.close()
        }
        while(stopBg) {
          val signalled = stopped.await(20, TimeUnit.MILLISECONDS)
          if (!signalled) {
            try {
              if (!bgmp3Player.get.isComplete) {
                bgmp3Player.get.close()
              }
            }
            catch {
              case t: Throwable => // do nothing
            }
          }
        }
      }
    }
  }
}

object KMp3 extends core.Singleton[KMp3] {
  protected def newInstance = new KMp3
}

class KMp3 extends Mp3Player {
  val pumpEvents = true
  def showError(msg: String) = println(msg)
}


