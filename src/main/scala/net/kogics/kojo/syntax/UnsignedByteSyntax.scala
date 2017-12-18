// Borrowed from: https://github.com/underscoreio/doodle
package net.kogics.kojo.syntax

import net.kogics.kojo.doodle.UnsignedByte

trait UnsignedByteSyntax {
  implicit class ToUnsignedByteOps(val value: Int) {
    def uByte: UnsignedByte =
      UnsignedByte.clip(value)
  }
}
