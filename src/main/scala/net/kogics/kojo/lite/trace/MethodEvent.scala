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

import java.awt.geom.Point2D
import javax.swing.JComponent

import scala.collection.mutable.ArrayBuffer

import com.sun.jdi.LocalVariable
import com.sun.jdi.StackFrame
import net.kogics.kojo.core.Picture
import net.kogics.kojo.lite.Theme

class MethodEvent {
  @volatile var methodName: String = _
  @volatile var rawName: String = _
  @volatile var targetObject: String = _
  @volatile var targetType: String = _
  @volatile var declaringType: String = _
  @volatile var args: collection.Seq[String] = _
  @volatile var returnVal: String = _
  @volatile var returnType: String = _
  @volatile var parent: Option[MethodEvent] = None
  @volatile var entryLineNum: Int = _
  @volatile var exitLineNum: Int = _
  @volatile var sourceName: String = _
  @volatile var callerLineNum: Int = -1
  @volatile var callerSourceName: String = _
  @volatile var srcLine: String = _
  @volatile var callerLine: String = _
  @volatile var subcalls = Vector[MethodEvent]()
  @volatile var turtlePoints: Option[(Point2D.Double, Point2D.Double)] = None
  @volatile var turtleTurn: Option[(Point2D.Double, Double, Double)] = None
  @volatile var picture: Option[Picture] = None
  val uiElems = new ArrayBuffer[JComponent](2)

  def pargs = if (args.size > 0) {
    args.mkString("(", ", ", ")")
  }
  else {
    if (returnType == "void") "()" else ""
  }
  def assignArg = args(0).substring(args(0).indexOf('='), args(0).length)
  def pret = returnVal.replace("<void value>", "()")
  def psubcalls = _psubcalls(subcalls)
  def _psubcalls(scs: Seq[MethodEvent]): String = scs match {
    case Seq()           => ""
    case Seq(x, xs @ _*) => s"> ${x.methodName} ${_psubcalls(x.subcalls)} < ${_psubcalls(xs)}"
  }

  def entry(level: Int) = {
    if (rawName.endsWith("_$eq"))
      <html><div style="font-family:Monospace"><span style="color:rgb(200,0,200)">{"\u00b7 " * level} ASSIGN</span> {
        methodName
      } <span style="color:rgb(200,0,200)"> {assignArg}</span></div></html>.toString
    else
      <html><div style="font-family:Monospace"><span style={s"color:${Theme.currentTheme.tracingCallColor}"} >{
        "\u00b7 " * level
      } CALL</span> {methodName} <span style={s"color:${Theme.currentTheme.tracingCallColor}"}>{
        pargs
      }</span></div></html>.toString
  }

  def exit(level: Int) = {
    <html><div style="font-family:Monospace"><span style="color:rgb(255,120,0)">{"\u00b7 " * level} RETURN</span> {
      methodName
    } <span style="color:rgb(255,120,0)">= {pret}</span></div></html>.toString
  }

  override def toString() = {
    s"""Name: $methodName
Args: $pargs${if (returnVal != null) s"\nReturn Value: $pret" else ""}
Call Level: $level
Target Object: $targetObject
Target Type: $targetType
Source: $sourceName
Entry Line Number: $entryLineNum
Exit Line Number: $exitLineNum
Caller Source: $callerSourceName
Caller Line Number: $callerLineNum${if (srcLine != "") s"\nSource Line: $srcLine" else ""}
Caller Source Line: $callerLine
"""
  }

  def ended = returnVal != null

  def setParent(p: Option[MethodEvent]): Unit = {
    parent = p
    parent.foreach { _.addChild(this) }
  }

  private def addChild(c: MethodEvent): Unit = { subcalls = subcalls :+ c }

  lazy val level: Int = parent match {
    case None    => 0
    case Some(p) => p.level + 1
  }

  def hasVisibleSubcall = {
    def visibleSubCall(sme: Seq[MethodEvent]): Boolean = sme match {
      case Seq() => false
      case x +: xs =>
        if (!x.uiElems.isEmpty)
          true
        else if (visibleSubCall(x.subcalls)) true
        else visibleSubCall(xs)
    }
    visibleSubCall(subcalls)
  }
}
