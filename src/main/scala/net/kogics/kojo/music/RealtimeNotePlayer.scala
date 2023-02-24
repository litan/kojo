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

import javax.sound.midi.MidiEvent
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequence
import javax.sound.midi.ShortMessage

class RealtimeNotePlayer {
  println("Initializing Midi Sequencer...")
  private val sequencer = MidiSystem.getSequencer()
  private val ticksPerQuarterNote = 1
  private val sequence = new Sequence(Sequence.PPQ, ticksPerQuarterNote)
  private var track = sequence.createTrack()
  private var trackTicks = 0L
  private val channel = 0

  sequencer.open()
  sequencer.setSequence(sequence)
  Thread.sleep(2500) // give Midi Synth a chance to warm up, as in alda
  println("Done")
  setInstrument(Instrument.PIANO)

  def setInstrument(instrumentCode: Int): Unit = {
    require(instrumentCode >= 0 && instrumentCode <= 127, "Instrument Code should be between 0 and 127")
    val insMsg = new ShortMessage(ShortMessage.PROGRAM_CHANGE, channel, instrumentCode, 0)
    val insEvent = new MidiEvent(insMsg, trackTicks)
    track.add(insEvent)
  }

  def playNote(pitch: Int, durationMillis: Int, volume: Int): Unit = {
    require(pitch >= 0 && pitch <= 127, "Note pitch should be between 0 and 127")
    require(volume >= 0 && volume <= 127, "Note volume should be between 0 and 127")

    val durationTicks = durationMillis // our ticks are in units of millis
    val endBuffer0 = (0.1 * durationTicks).toInt
    val endBuffer = if (endBuffer0 > 0) endBuffer0 else 1

    val noteOnMsg = new ShortMessage(ShortMessage.NOTE_ON, channel, pitch, volume)
    val noteOnEvent = new MidiEvent(noteOnMsg, trackTicks)
    track.add(noteOnEvent)
    trackTicks += durationTicks

    val noteOffMsg = new ShortMessage(ShortMessage.NOTE_OFF, channel, pitch, volume)
    val noteOffEvent = new MidiEvent(noteOffMsg, trackTicks - endBuffer)
    track.add(noteOffEvent)

    if (!sequencer.isRunning) {
      // tempo in increments of 1ms
      sequencer.setTempoInMPQ(1000f)
      sequencer.start()
    }
    else {
      // let the running sequencer play this note after the previous ones are done
    }
  }

  def stop(): Unit = {
    if (sequencer.isRunning) {
      sequencer.stop()
      sequence.deleteTrack(track)
      track = sequence.createTrack()
      sequencer.setSequence(sequence)
      trackTicks = 0L
      sequencer.setTickPosition(0)
    }
  }

  def close(): Unit = {
    sequencer.stop()
    sequence.deleteTrack(track)
    sequencer.close()
  }
}
