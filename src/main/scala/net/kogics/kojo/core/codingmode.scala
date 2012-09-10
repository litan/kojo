package net.kogics.kojo.core

sealed trait CodingMode
case object TwMode extends CodingMode
case object MwMode extends CodingMode
case object StagingMode extends CodingMode
case object D3Mode extends CodingMode
