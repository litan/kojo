/*
 * Copyright (C) 2013 "Sami Jaber" <jabersami@gmail.com>
 * Copyright (C) 2013 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.lite.trace

import com.sun.jdi.LocalVariable
import com.sun.jdi.StackFrame

class MethodEvent {
  var entry: String = _
  var exit: String = _
  var parent: Option[MethodEvent] = None
  var subcalls = Vector[MethodEvent]()
  var returnVal: String = _
  var entryLineNum: Int = _
  var exitLineNum: Int = _
  var methodName: String = _
  var sourceName: String = _
  var callerSourceName: String = _
  var callerLineNum: Int = -1
  var callerLine: String = _

  override def toString() = {
    s"""Entry: $entry
Exit: $exit
Source: $sourceName
Entry Line Number: $entryLineNum
Exit Line Number: $exitLineNum
Caller Source: $callerSourceName
Caller Line Number: $callerLineNum
Caller Source Line: $callerLine
	"""
  }

  def ended = exit != null

  def setParent(p: Option[MethodEvent]) {
    parent = p
    parent foreach { _.addChild(this) }
  }

  private def addChild(c: MethodEvent) { subcalls = subcalls :+ c }

  def level: Int = parent match {
    case None    => 0
    case Some(p) => p.level + 1
  }
}
