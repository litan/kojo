// Borrowed from: https://github.com/underscoreio/doodle
package net.kogics.kojo.syntax

import net.kogics.kojo.doodle.Normalized

trait NormalizedSyntax {
  implicit class ToNormalizedOps(val value: Double) {
    def normalized: Normalized =
      Normalized.clip(value)
  }
}
