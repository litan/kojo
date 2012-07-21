/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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

import java.util.jar._
import java.io._
import java.io.File
import net.kogics.kojo.util._
import java.util.{HashMap, ArrayList}

object PuzzleLoader {

  val jEntries = new HashMap[String, JarEntry]
  val puzzles = new ArrayList[String]
  var f: JarFile = _

  def init() {
    val kd = Utils.installDir
    val pf = kd + "/../puzzles/puzzles.jar"
    try {
      f = new JarFile(pf)
    }
    catch {
      case e: IOException => return
    }

    val entries = f.entries
    while (entries.hasMoreElements) {
      val je = entries.nextElement
      val name = je.getName.split('.')(0)
      jEntries.put(name, je)
      puzzles.add(name)
    }
  }

  def readPuzzle(str: String): Option[String] = {
    val je = jEntries.get(str)
    if (je == null) return None
    Some(Utils.readStream(f.getInputStream(je)))
  }

  def listPuzzles = puzzles
}
