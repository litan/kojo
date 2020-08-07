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

import java.io._
import java.net.URL
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import javax.swing.Timer
import javazoom.jl.player.Player
import net.kogics.kojo.core.KojoCtx
import net.kogics.kojo.lite.LoadProgress
import net.kogics.kojo.util.Utils.{giveupLock, withLock}
import net.kogics.kojo.util.{AsyncQueuedRunner, Utils}

object Mp3Player {
  val streamCache = new ConcurrentHashMap[String, Array[Byte]]()
}

trait Mp3Player {
  import Mp3Player.streamCache
  val pumpEvents: Boolean
  val kojoCtx: KojoCtx
  def showError(msg: String): Unit
  lazy private val listener = kojoCtx.activityListener

  @volatile private var mp3Player: Option[Player] = None
  @volatile private var bgmp3Player: Option[Player] = None
  private val playLock = new ReentrantLock
  private val stopped = playLock.newCondition
  private var stopBg = false
  private var stopFg = false
  private var timer: Timer = _

  private def startPumpingEvents(): Unit = {
    if (pumpEvents && timer == null) {
      listener.hasPendingCommands()
      timer = Utils.scheduleRec(0.5) {
        listener.hasPendingCommands()
      }
    }
  }

  private def stopPumpingEvents(): Unit = {
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

  private def obtainStream(fname: String): InputStream = {
    def inputStreamToByteArray(is: InputStream): Array[Byte] = {
      import java.io.ByteArrayOutputStream
      val buff = new Array[Byte](8192)
      val bao = new ByteArrayOutputStream
      val in = new BufferedInputStream(is)
      var read = in.read(buff)
      while (read != -1) {
        bao.write(buff, 0, read)
        read = in.read(buff)
      }
      in.close()
      bao.toByteArray
    }

    def updateCacheAndObtainInputStream(ba: Array[Byte]): InputStream = {
      streamCache.put(fname, ba)
      new ByteArrayInputStream(ba)
    }

    val byteArray = streamCache.get(fname)
    if (byteArray != null) {
      new ByteArrayInputStream(byteArray)
    }
    else {
      val byteArrayIs = try {
        if (fname.startsWith("http")) {
          val is = new URL(fname).openConnection().getInputStream
          val ba = inputStreamToByteArray(is)
          updateCacheAndObtainInputStream(ba)
        }
        else {
          val is = getClass.getResourceAsStream(fname)
          if (is != null) {
            val ba = inputStreamToByteArray(is)
            updateCacheAndObtainInputStream(ba)
          }
          else {
            val mp3File = Utils.absolutePath(fname)
            val f = new File(mp3File)
            if (f.exists) {
              val is = new FileInputStream(f)
              val ba = inputStreamToByteArray(is)
              updateCacheAndObtainInputStream(ba)
            }
            else {
              null
            }
          }
        }
      }
      catch {
        case t: Throwable => null
      }
      byteArrayIs
    }
  }

  private def playHelper(fname: String)(fn: (InputStream) => Unit): Unit = {
    val is = obtainStream(fname)
    if (is != null) {
      fn(is)
    }
    else {
      showError("MP3 file does not exist - %s" format (fname))
    }
  }

  def isMp3Playing: Boolean = {
    withLock(playLock) {
      mp3Player.isDefined
    }
  }

  def preloadMp3(mp3File: String): Unit = {
    LoadProgress.showLoading()
    val is = obtainStream(mp3File)
    LoadProgress.hideLoading()
    if (is != null) {
      is.close()
    }
  }

  def playMp3(mp3File: String): Unit = {
    def done(): Unit = {
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

  lazy val queuedRunner = new AsyncQueuedRunner {}
  def playMp3Sound(mp3File: String): Unit = {
    def done(): Unit = {
      stopFg = false
      mp3Player = None
      stopped.signal()
    }
    withLock(playLock) {
      stopMp3()
      playHelper(mp3File) { is =>
        mp3Player = Some(new Player(is))
      }
      if (mp3Player.isDefined) {
        queuedRunner.runAsyncQueued {
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
      }
    }
  }

  def playMp3Loop(mp3File: String): Unit = {

    def playLoop0(): Unit = {
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

  def stopMp3(): Unit = {
    withLock(playLock) {
      if (mp3Player.isDefined) {
        stopFg = true
        if (!mp3Player.get.isComplete) {
          mp3Player.get.close()
        }
        while (stopFg) {
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

  def stopMp3Loop(): Unit = {
    withLock(playLock) {
      if (bgmp3Player.isDefined) {
        stopBg = true
        if (!bgmp3Player.get.isComplete) {
          bgmp3Player.get.close()
        }
        while (stopBg) {
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

class KMp3(val kojoCtx: KojoCtx) extends Mp3Player {
  val pumpEvents = true
  def showError(msg: String) = println(msg)
}

