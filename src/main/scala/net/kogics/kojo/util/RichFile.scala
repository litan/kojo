/*
 * Copyright (C) 2008, 2009 Lalit Pant <lalit_pant@yahoo.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo.util

import java.io._

object RichFile {
  import language.implicitConversions
  implicit def enrichFile(f: File) = new RichFile(f)
}

class RichFile(f: File) {
  def readAsString: String = {
    val flen = f.length.toInt;
    val fis = new BufferedInputStream(new FileInputStream(f))
    val buf = new Array[Byte](flen)
    try {
      var (read, offset) = (0, 0)
      while (read != flen) {
        val count = fis.read(buf, offset, flen - offset)
        read += count; offset += count
      }
      new String(buf, "UTF-8")
    }
    finally {
      fis.close
    }
  }

  def write(data: String) {
    val fos = new BufferedOutputStream(new FileOutputStream(f))
    try {
      fos.write(data.getBytes("UTF-8"))
    }
    finally {
      fos.close
    }
  }
}
