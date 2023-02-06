/*
 * Copyright (C) 2023 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.music

import javax.sound.midi._

class RealtimeNotePlayer {
  println("Getting Midi Sequencer...")
  private val sequencer = MidiSystem.getSequencer()
  println("Done")
  private val sequence = new Sequence(Sequence.PPQ, 1)
  private val track = sequence.createTrack()
  private var trackTicks = 0

  sequencer.open()
  sequencer.setSequence(sequence)
  setInstrument(0)

  def setInstrument(instrumentCode: Int): Unit = synchronized {
    require(instrumentCode >= 0 && instrumentCode <= 127, "Instrument Code should be between 0 and 127")
    val ins = new ShortMessage()
    ins.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrumentCode, 0)
    val specifyInstrument = new MidiEvent(ins, trackTicks)
    track.add(specifyInstrument)
  }

  def playNote(pitch: Int, millis: Int, volume: Int): Unit = synchronized {
    require(pitch >= 0 && pitch <= 127, "Note pitch should be between 0 and 127")
    require(volume >= 0 && volume <= 127, "Note volume should be between 0 and 127")

    val a = new ShortMessage()
    a.setMessage(ShortMessage.NOTE_ON, 0, pitch, volume)
    val noteOn = new MidiEvent(a, trackTicks)
    track.add(noteOn)
    trackTicks += 1

    val b = new ShortMessage()
    b.setMessage(ShortMessage.NOTE_OFF, 0, pitch, volume)
    val noteOff = new MidiEvent(b, trackTicks)
    track.add(noteOff)

    if (!sequencer.isRunning) {
      sequencer.setTempoInMPQ(millis * 1000f)
      sequencer.start()
    }
    else {
      // ignore play command if earlier play is running
    }
  }
}
