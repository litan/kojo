package net.kogics.kojo.core

sealed trait CodingMode { def code: String }
case object TwMode extends CodingMode { val code = "tw" }
case object VanillaMode extends CodingMode { val code = "vn" }
