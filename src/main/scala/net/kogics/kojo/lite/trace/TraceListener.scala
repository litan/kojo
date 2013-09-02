package net.kogics.kojo.lite.trace

trait TraceListener {
    def onStart()
    def onEnd()
}