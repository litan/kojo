package net.kogics.kojo.lite.trace

trait TraceListener {
    def onStart()
    def onMethodEnter(me: MethodEvent)
    def onMethodExit(me: MethodEvent)
    def onEnd()
}