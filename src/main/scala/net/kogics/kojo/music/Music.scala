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

import org.jfugue.{Rhythm => JFRhythm, _}
import util.Utils

object Music {
  def apply(mString: String) = new Music(new Pattern(mString))

  def apply(p: Pattern) = new Music(p)

  def apply(voice: core.Voice, n: Int = 1) = {
    new Music(voice.pattern(n))
  }
}

class Music(pattern: Pattern) {
  val player = new Player()
  val sequence = player.getSequence(pattern)

  def play() {
    try {
      player.play(sequence)
    }
    finally {
      player.close()
    }
  }

  def stop() {
    if (player.isPlaying) {
      player.stop()
    }
    player.close()
  }
}
