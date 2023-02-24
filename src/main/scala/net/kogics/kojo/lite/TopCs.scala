package net.kogics.kojo.lite

import net.kogics.kojo.lite.topc.ArithAerobicsHolder
import net.kogics.kojo.lite.topc.DrawingCanvasHolder
import net.kogics.kojo.lite.topc.HistoryHolder
import net.kogics.kojo.lite.topc.OutputWindowHolder
import net.kogics.kojo.lite.topc.ScriptEditorHolder
import net.kogics.kojo.lite.topc.StoryTellerHolder

case class TopCs(
    dch: DrawingCanvasHolder,
    owh: OutputWindowHolder,
    seh: ScriptEditorHolder,
    sth: StoryTellerHolder,
    hih: HistoryHolder,
    aah: ArithAerobicsHolder
)
