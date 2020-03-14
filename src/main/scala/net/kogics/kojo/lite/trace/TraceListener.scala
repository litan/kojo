package net.kogics.kojo.lite.trace

trait TraceListener {
    def onStart(): Unit
    def onMethodEnter(me: MethodEvent): Unit
    def onMethodExit(me: MethodEvent): Unit
    def onEnd(): Unit
}