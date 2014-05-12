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

package net.kogics.kojo.core

import org.jfugue.{Rhythm => JFRhythm, _}

trait Voice {
  def pattern(n: Int): Pattern
}

case class Melody(instrument: String, music: String) extends Voice {
  def pattern(n: Int) = {
    val p = new Pattern("I[%s] %s" format(instrument, music))
    val ret = new Pattern()
    ret.add(p, n)
    ret
  }
}

case class Rhythm(instrument: String, duration: String, beat: String) extends Voice {
  def pattern(n: Int) = {
    val rhy = new JFRhythm()
    rhy.setLayer(1, beat)
    rhy.addSubstitution(beatChar(beat), "[%s]%s" format(instrument, duration))
    rhy.addSubstitution('.', "R%s" format(duration))
    val ret = new Pattern()
    ret.add(rhy.getPattern, n)
    ret
  }

  def beatChar(b: String): Char = {
    b.find {c => c != '.'}.get
  }
}

case class Score(voices: Voice *) extends Voice {
  def pattern(n: Int): Pattern = {
    val score = new Pattern()
    var idx = 0
    val rhy = new JFRhythm()
    var rLayer = 1
    voices.foreach { voice => voice match {
        case Melody(i, m) =>
          val p = new Pattern("V%d I[%s] %s" format(idx, i, m))
          score.add(p, n)

        case rv @ Rhythm(i, d, b) =>
          rhy.setLayer(rLayer, b)
          rhy.addSubstitution(rv.beatChar(b), "[%s]%s" format(i, d))
          rhy.addSubstitution('.', "R%s" format(d))
          rLayer += 1
      }
                    idx += 1
    }
    score.add(rhy.getPattern, n)
    score
  }
}