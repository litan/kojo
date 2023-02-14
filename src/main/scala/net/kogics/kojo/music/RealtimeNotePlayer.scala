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

import javax.sound.midi.{MidiEvent, MidiSystem, Sequence, ShortMessage}

class RealtimeNotePlayer {
  println("Getting Midi Sequencer...")
  private val sequencer = MidiSystem.getSequencer()
  println("Done")
  private val sequence = new Sequence(Sequence.PPQ, 1)
  private var track = sequence.createTrack()
  private var trackTicks = 0
  private val channel = 0

  sequencer.open()
  sequencer.setSequence(sequence)
  setInstrument(Instrument.PIANO)

  def setInstrument(instrumentCode: Int): Unit = {
    require(instrumentCode >= 0 && instrumentCode <= 127, "Instrument Code should be between 0 and 127")
    val ins = new ShortMessage()
    ins.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrumentCode, 0)
    val specifyInstrument = new MidiEvent(ins, trackTicks)
    track.add(specifyInstrument)
  }

  def playNote(pitch: Int, duration: Int, volume: Int): Unit = {
    require(pitch >= 0 && pitch <= 127, "Note pitch should be between 0 and 127")
    require(volume >= 0 && volume <= 127, "Note volume should be between 0 and 127")

    val a = new ShortMessage()
    a.setMessage(ShortMessage.NOTE_ON, channel, pitch, volume)
    val noteOn = new MidiEvent(a, trackTicks)
    track.add(noteOn)
    trackTicks += 1

    val b = new ShortMessage()
    b.setMessage(ShortMessage.NOTE_OFF, channel, pitch, volume)
    val noteOff = new MidiEvent(b, trackTicks)
    track.add(noteOff)

    if (!sequencer.isRunning) {
      sequencer.setTempoInMPQ(duration * 1000f)
      sequencer.start()
    }
    else {
      // let the running sequencer play this note after the previous ones are done
      // the duration of this note is ignored
    }
  }

  def stop(): Unit = {
    if (sequencer.isRunning) {
      sequencer.stop()
      sequence.deleteTrack(track)
      track = sequence.createTrack()
      sequencer.setSequence(sequence)
      trackTicks = 0
      sequencer.setTickPosition(0)
    }
  }

  def close(): Unit = {
    sequencer.stop()
    sequence.deleteTrack(track)
    sequencer.close()
  }
}
