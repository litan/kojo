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

package net.kogics.kojo.core

import tools.nsc.interactive.CompilerControl

// code borrowed from Scala Eclipse Plugin, after my own hacks in this area failed with 2.10.1
/** The kind of a completion proposal. */
object MemberKind extends Enumeration {
  val Class, Trait, Type, Object, Package, PackageObject, Def, Val, Var = Value
}

case class CompletionInfo(
  kind: MemberKind.Value,
  name: String,
  signature: String,
  owner: String, 
  prio: Int,
  isJava: Boolean,
  paramNames: List[List[String]], // parameter names (excluding any implicit parameter sections)
  paramTypes: List[List[String]], // parameter types matching parameter names (excluding implicit parameter sections)
  returnType: String,
  fullyQualifiedName: String // for Class, Trait, Type, Objects: the fully qualified name
  ) {
  
  def params: String = {
    if (paramNames.size == 0 && returnType == "Unit") {
      "()"
    }
    else {
      val contextInfo = for {
        (names, tpes) <- paramNames.zip(paramTypes)
      } yield for { (name, tpe) <- names.zip(tpes) } yield "%s: %s".format(name, tpe)

      contextInfo.map(_.mkString("(", ", ", ")")).mkString("")
    }
  }

  def templateParams: String = {
    if (paramNames.size == 0 && returnType == "Unit") {
      "()"
    }
    else {
      val contextInfo = for {
        names <- paramNames
      } yield for { name <- names } yield "${%s}" format (name)

      contextInfo.map(_.mkString("(", ", ", ")")).mkString("")
    }
  }

  import MemberKind._
  def fullCompletion = kind match {
    case Def => s"$name$params: $returnType"
    case Val => s"$name$params: $returnType"
    case Var => s"$name$params: $returnType"
    case _ => s"$name: $owner"
  }
}

trait CodeCompletionSupport {
  def varCompletions(prefix: Option[String]): (List[String], Int)
  def keywordCompletions(prefix: Option[String]): (List[String], Int)
  def memberCompletions(caretOffset: Int, objid: String, prefix: Option[String]): (List[CompletionInfo], Int)
  def objidAndPrefix(caretOffset: Int): (Option[String], Option[String])
}
