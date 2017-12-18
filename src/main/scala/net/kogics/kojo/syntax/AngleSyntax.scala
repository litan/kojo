// Borrowed from: https://github.com/underscoreio/doodle
package net.kogics.kojo.syntax

import net.kogics.kojo.doodle.Angle

trait AngleSyntax {
  implicit class AngleOps(val angle: Double) {
    def degrees: Angle =
      Angle.degrees(angle)

    def radians: Angle =
      Angle.radians(angle)

    def turns: Angle =
      Angle.turns(angle)
  }
}
