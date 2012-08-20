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

case class CompletionInfo(
  name: String,
  params: List[String],
  paramTypes: List[String],
  ret: String,
  prio: Int,
  isValue: Boolean = false,
  isClass: Boolean = false,
  isPackage: Boolean = false,
  isType: Boolean = false
)

trait CodeCompletionSupport {
  def varCompletions(prefix: Option[String]): (List[String], Int)
  def keywordCompletions(prefix: Option[String]): (List[String], Int)
  def memberCompletions(caretOffset: Int, objid: String, prefix: Option[String]): (List[CompletionInfo], Int)
  def objidAndPrefix(caretOffset: Int): (Option[String], Option[String])
}
