package net.kogics.kojo.staging

import java.awt.BasicStroke

class CapJoin {
  val JOIN_MITER = BasicStroke.JOIN_MITER
  val JOIN_ROUND = BasicStroke.JOIN_ROUND
  val JOIN_BEVEL = BasicStroke.JOIN_BEVEL
  val CAP_BUTT = BasicStroke.CAP_BUTT
  val CAP_ROUND = BasicStroke.CAP_ROUND
  val CAP_SQUARE = BasicStroke.CAP_SQUARE

  // Note - CAPs are for "unclosed" lines
  // Joins come into play where lines connect

  // good combos
  val BUTT_BEVEL = (CAP_BUTT, JOIN_BEVEL)
  val ROUND_ROUND = (CAP_ROUND, JOIN_ROUND)
  val SQUARE_MITER = (CAP_SQUARE, JOIN_MITER)

  val ROUNDED_CORNERS = ROUND_ROUND
  val SHARP_CORNERS = SQUARE_MITER
  val SLOPING_CORNERS = BUTT_BEVEL
}

object CapJoinConstants {
  val CapThick = BasicStroke.CAP_ROUND
  val CapThin = BasicStroke.CAP_BUTT
  val JoinThick = BasicStroke.JOIN_ROUND
  val JoinThin = BasicStroke.JOIN_BEVEL
  val DefaultCap = -1
  val DefaultJoin = -1
}
