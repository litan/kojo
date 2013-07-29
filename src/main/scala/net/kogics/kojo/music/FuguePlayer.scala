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
package music

import util.Utils
import util.Utils.withLock
import Utils.giveupLock
import org.jfugue.{Rhythm => JFRhythm, _}
import java.util.logging._
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import javax.swing.Timer
import net.kogics.kojo.core.KojoCtx

class FuguePlayer(kojoCtx: KojoCtx) {
  val Log = Logger.getLogger(getClass.getName)
  lazy private val listener = kojoCtx.activityListener
  private var currMusic: Option[Music] = None
  private var currBgMusic: Option[Music] = None
  val playLock = new ReentrantLock
  val done = playLock.newCondition
  val stopped = playLock.newCondition
  var stopBg = false
  var stopFg = false
  var timer: Timer = _
  val pumpEvents: Boolean = true

  private def startPumpingEvents() {
    if (pumpEvents && timer == null) {
      listener.hasPendingCommands()
      timer = Utils.scheduleRec(0.5) {
        listener.hasPendingCommands()
      }
    }
  }

  private def stopPumpingEvents() {
    if (pumpEvents && currBgMusic.isEmpty) {
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
  
  private def stopAndCreate(voice: core.Voice, n: Int) {
    if (n > 10) {
      throw new IllegalArgumentException("Score repeat count cannot be more than 10")
    }
    
    stopMusic()    
    currMusic = Some(Music(voice, n))
  }
  
  def isMusicPlaying: Boolean = {
    withLock(playLock) {
      currMusic.isDefined
    }
  }
  
  def playMusic(voice: core.Voice, n: Int = 1) {
    def done() {
      stopFg = false
      currMusic = None
      stopPumpingEvents()
      stopped.signal()
    }
    withLock(playLock) {
      stopAndCreate(voice, n)
      Utils.runAsync {
        withLock(playLock) {
          if (stopFg) {
            done()
          }
          else {
            val music = currMusic.get
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

  def playMusicUntilDone(voice: core.Voice, n: Int = 1) {
    withLock(playLock) {
      stopAndCreate(voice, n)
      Utils.runAsync {
        withLock(playLock) {
          val music = currMusic.get
          giveupLock(playLock) {
            music.play()
          }
          done.signal()
          stopFg = false
          currMusic = None
          stopPumpingEvents()
          stopped.signal()
        }
      }
      startPumpingEvents()
      done.await()
    }
  }
  
  def playMusicLoop(voice: core.Voice) {

    def playLoop0() {
      Utils.runAsync {
        withLock(playLock) {
          if (stopBg) {
            stopBg = false
            currBgMusic = None
            stopPumpingEvents()
            stopped.signal()
          }
          else {
            val music = currBgMusic.get
            giveupLock(playLock) {
              music.play()
            }
            currBgMusic = Some(Music(voice, 5))
            playLoop0()
          }
        }
      }
    }

    withLock(playLock) {
      stopBgMusic()
      currBgMusic = Some(Music(voice, 5))
      playLoop0()
      startPumpingEvents()
    }       
  }

  def stopMusic() {
    withLock(playLock) {
      if (currMusic.isDefined) {
        stopFg = true
        currMusic.get.stop()
        while(stopFg) {
          val signalled = stopped.await(20, TimeUnit.MILLISECONDS)
          if (!signalled) {
            try {
              currMusic.get.stop()
            }
            catch {
              case t: Throwable => // do nothing
            }
          }
        }
      }
    }
  }

  def stopBgMusic() {
    withLock(playLock) {
      if (currBgMusic.isDefined) {
        stopBg = true
        currBgMusic.get.stop()
        while(stopBg) {
          val signalled = stopped.await(20, TimeUnit.MILLISECONDS)
          if (!signalled) {
            try {
              currBgMusic.get.stop()
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
