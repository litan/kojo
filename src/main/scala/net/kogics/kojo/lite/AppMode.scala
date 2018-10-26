/*
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.lite

import net.kogics.kojo.core.CodeRunner
import net.kogics.kojo.core.CodingMode
import net.kogics.kojo.core.VanillaMode
import net.kogics.kojo.core.RunContext
import net.kogics.kojo.core.TwMode
import net.kogics.kojo.xscala
import net.kogics.kojo.xscala.KojoInterpreter
import net.kogics.kojo.xscala.ScalaCodeRunner2

trait AppMode {
  def richSyntaxHighlighting: Boolean
  def defaultCodingMode: CodingMode
  def scalaCodeRunner(rc: RunContext): CodeRunner = new xscala.ScalaCodeRunner2(rc, defaultCodingMode)
  def kojoInterpreter(cr: CodeRunner): KojoInterpreter =
    cr.asInstanceOf[ScalaCodeRunner2].kojoInterpreter
}

class DesktopMode extends AppMode {
  val defaultCodingMode = TwMode
  val richSyntaxHighlighting = true
}

class EmbeddedMode extends AppMode {
  val defaultCodingMode = VanillaMode
  val richSyntaxHighlighting = false
}

object AppMode {
  val currentMode: AppMode = {
    val osName = System.getProperty("os.name")
    val osArch = System.getProperty("os.arch")
    if (osName == "Linux" && osArch == "arm") new EmbeddedMode else new DesktopMode
  }
}
