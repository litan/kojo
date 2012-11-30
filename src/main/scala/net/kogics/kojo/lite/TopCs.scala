package net.kogics.kojo.lite

import net.kogics.kojo.lite.topc.DrawingCanvasHolder
import net.kogics.kojo.lite.topc.OutputWindowHolder
import net.kogics.kojo.lite.topc.ScriptEditorHolder
import net.kogics.kojo.lite.topc.StoryTellerHolder
import net.kogics.kojo.lite.topc.MathworldHolder
import net.kogics.kojo.lite.topc.D3CanvasHolder
import net.kogics.kojo.lite.topc.HistoryHolder

case class TopCs(
  dch: DrawingCanvasHolder,
  owh: OutputWindowHolder,
  seh: ScriptEditorHolder,
  sth: StoryTellerHolder,
  mwh: MathworldHolder,
  d3h: D3CanvasHolder,
  hih: HistoryHolder)