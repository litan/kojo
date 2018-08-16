package net.kogics.kojo.core

sealed trait CodingMode { def code: String }
case object TwMode extends CodingMode { def code = "tw" }
case object D3Mode extends CodingMode { def code = "d3" }
