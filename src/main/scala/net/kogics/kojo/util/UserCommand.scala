/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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

import net.kogics.kojo.xscala.CodeCompletionUtils

object UserCommand {
  val synopses = new scala.collection.mutable.StringBuilder

  def addCompletion(name: String, args: String) {
    CodeCompletionUtils.BuiltinsMethodTemplates(name) = name + args
  }

  def addCompletion(name: String, args: Seq[String]) {
    addCompletion(name, args map ("${%s}" format _) mkString("(", ", ", ")"))
  }

  def addSynopsisSeparator() { synopses.append("\n") }

  def addSynopsis(s: String) { synopses.append("\n  " + s) }

  def addSynopsis(name: String, args: Seq[String], synopsis: String) {
    addSynopsis(name + args.mkString("(", ", ", ")") + " - " + synopsis)
  }

  def apply(name: String, args: Seq[String], synopsis: String) = {
    addCompletion(name, args)
    addSynopsis(name, args, synopsis)
  }
}
