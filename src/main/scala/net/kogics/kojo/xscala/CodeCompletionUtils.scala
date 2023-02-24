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

package net.kogics.kojo
package xscala

object CodeCompletionUtils {
  val NotIdChars = """ .(){}!%&+\-<=>?@\\^`|~#:/*""" + "\n\r\t"

  val Keywords = List(
    "abstract",
    "case",
    "catch",
    "class",
    "def",
    "do",
    "else",
    "extends",
    "false",
    "final",
    "finally",
    "for",
    "forSome",
    "if",
    "implicit",
    "import",
    "lazy",
    "match",
    "new",
    "null",
    "object",
    "override",
    "package",
    "private",
    "protected",
    "return",
    "sealed",
    "super",
    "this",
    "throw",
    "trait",
    "try",
    "true",
    "type",
    "val",
    "var",
    "while",
    "with",
    "yield"
  )

  val KeywordTemplates = Map(
    "for" -> "for (i <- 1 to ${n}) {\n    ${cursor}\n}",
    "while" -> "while (${condition}) {\n    ${cursor}\n}",
    "if" -> "if (${condition}) {\n    ${cursor}\n}"
  )

  // UserCommand adds to this
  val BuiltinsMethodTemplates = collection.mutable.Map(
    "resetInterpreter" -> "resetInterpreter()",
    "switchToDefaultPerspective" -> "switchToDefaultPerspective()",
    "switchToDefault2Perspective" -> "switchToDefault2Perspective()",
    "switchToScriptEditingPerspective" -> "switchToScriptEditingPerspective()",
    "switchToWorksheetPerspective" -> "switchToWorksheetPerspective()",
    "switchToStoryViewingPerspective" -> "switchToStoryViewingPerspective()",
    "switchToHistoryBrowsingPerspective" -> "switchToHistoryBrowsingPerspective()",
    "switchToCanvasPerspective" -> "switchToCanvasPerspective()",
    "toggleFullScreenCanvas" -> "toggleFullScreenCanvas()",
    "toggleFullScreenOutput" -> "toggleFullScreenOutput()",
    "setOutputBackground" -> "setOutputBackground(${color})",
    "setOutputTextColor" -> "setOutputTextColor(${color})",
    "setOutputTextFontSize" -> "setOutputTextFontSize(${size})",
    "onKeyPress" -> "onKeyPress {\n    case Kc.VK_A     => ${cursor}\n    case Kc.VK_RIGHT => \n    case other       => \n}",
    "onKeyRelease" -> "onKeyRelease {\n    case Kc.VK_A     => ${cursor}\n    case Kc.VK_RIGHT => \n    case other       => \n}",
    "stAddLinkHandler" -> "stAddLinkHandler(${handlerName}, ${story}) {d: ${argType} =>\n    ${cursor}\n}",
    "stAddLinkEnterHandler" -> "stAddLinkHandler(${handlerName}, ${story}) {d: ${argType} =>\n    ${cursor}\n}",
    "stAddLinkExitHandler" -> "stAddLinkHandler(${handlerName}, ${story}) {d: ${argType} =>\n    ${cursor}\n}",
    "stInsertCodeInline" -> "stInsertCodeInline(${code})",
    "stInsertCodeBlock" -> "stInsertCodeBlock(${code})",
    "stOnStoryStop" -> "stOnStoryStop {\n    ${cursor}\n}",
    "HPics" -> "HPics(\n      p,\n      p\n)",
    "picRow" -> "picRow(${p1}, ${p2})",
    "picRowCentered" -> "picRowCentered(${p1}, ${p2})",
    "VPics" -> "VPics(\n      p,\n      p\n)",
    "picCol" -> "picCol(${p1}, ${p2})",
    "picColCentered" -> "picColCentered(${p1}, ${p2})",
    "GPics" -> "GPics(\n      p,\n      p\n)",
    "picStack" -> "picStack(${p1}, ${p2})",
    "picStackCentered" -> "picStackCentered(${p1}, ${p2})",
    "picBatch" -> "picBatch(\n      p,\n      p\n)",
    // Todo - is there any commonality here with the staging templates
    "trans" -> "trans(${x}, ${y})",
    "rot" -> "rot(${angle})",
    "rotp" -> "rotp(${angle}, ${x}, ${y})",
    "scale" -> "scale(${factor})",
    "scalep" -> "scalep(${factor}, ${x}, ${y})",
    "opac" -> "opac(${changeFactor})",
    "hue" -> "hue(${changeFactor})",
    "hueMod" -> "hueMod(${color}, ${changeFactor})",
    "sat" -> "sat(${changeFactor})",
    "satMod" -> "satMod(${color}, ${changeFactor})",
    "brit" -> "brit(${changeFactor})",
    "britMod" -> "britMod(${color}, ${changeFactor})",
    "translate" -> "translate(${x}, ${y})",
    "transv" -> "transv(${vector})",
    "offset" -> "offset(${x}, ${y})",
    "rotate" -> "rotate(${angle})",
    "rotateAboutPoint" -> "rotateAboutPoint(${angle}, ${x}, ${y})",
    "fillColor" -> "fillColor(${color})",
    "penColor" -> "penColor(${color})",
    "penWidth" -> "penWidth(${n})",
    "penThickness" -> "penThickness(${n})",
    "spin" -> "spin(${n})",
    "reflect" -> "reflect(${gap})",
    "fade" -> "fade(${distance})",
    "blur" -> "blur(${radius})",
    "flipX" -> "flipAroundX",
    "flipY" -> "flipAroundY",
    "pointLight" -> "pointLight(${x}, ${y}, ${direction}, ${elevation}, ${distance})",
    "spotLight" -> "spotLight(${x}, ${y}, ${direction}, ${elevation}, ${distance})",
    "PointLight" -> "PointLight(${x}, ${y}, ${direction}, ${elevation}, ${distance})",
    "SpotLight" -> "SpotLight(${x}, ${y}, ${direction}, ${elevation}, ${distance})",
    "noise" -> "noise(${amount}, ${density})",
    "weave" -> "weave(${xWidth}, ${xGap}, ${yWidth}, ${yGap})",
    "effect" -> "effect(${'name}, ${'prop -> value})",
    "draw" -> "draw(${pic})",
    "drawAndHide" -> "drawAndHide(${pic})",
    "setRefreshRate" -> "setRefreshRate(${framesPerSec})",
    "setScreenDPI" -> "setScreenDPI(${dotsPerInch})",
    "timer" -> "timer(${milliSeconds}) {\n    ${cursor}\n}",
    "animate" -> "animate {\n    ${cursor}\n}",
    "animateWithState" -> "animateWithState(${initState}) { s =>\n    ${cursor}\n}",
    "animateWithRedraw" -> "animateWithRedraw(${initState}, ${nextState}, ${stateView})",
    "drawLoop" -> "drawLoop {\n    ${cursor}\n}",
    "setup" -> "setup {\n    ${cursor}\n}",
    "schedule" -> "schedule(${seconds}) {\n    ${cursor}\n}",
    "runInBackground" -> "runInBackground {\n    ${cursor}\n}",
    "runInDrawingThread" -> "runInDrawingThread {\n    ${cursor}\n}",
    "onAnimationStart" -> "onAnimationStart {\n    ${cursor}\n}",
    "onAnimationStop" -> "onAnimationStop {\n    ${cursor}\n}",
    "act" -> "act { self => // we give this turtle the name 'self' within act {...}\n    ${cursor}\n}",
    "react" -> "react { self => \n    ${cursor}\n}",
    "row" -> "row(${picture}, ${n})",
    "col" -> "col(${picture}, ${n})",
    "setUnitLength" -> "setUnitLength(${unit})",
    "clearWithUL" -> "clearWithUL(${unit})",
    "clearOutput" -> "clearOutput()",
    "stopAnimation" -> "stopAnimation()",
    "isKeyPressed" -> "isKeyPressed(Kc.VK_${cursor})",
    "activateCanvas" -> "activateCanvas()",
    "activateEditor" -> "activateEditor()",
    "setBackground" -> "setBackground(${color})",
    "setBackgroundH" -> "setBackgroundH(${color1}, ${color2})",
    "setBackgroundV" -> "setBackgroundV(${color1}, ${color2})",
    "Color" -> "Color(${red}, ${green}, ${blue}, ${opacity})",
    "ColorG" -> "ColorG(${x1}, ${y1}, ${color1}, ${x2}, ${y2}, ${color2}, ${cyclic})",
    "ColorLinearG" -> "ColorLinearG(${x1}, ${y1}, ${x2}, ${y2}, ${distribution}, ${colors}, ${cyclic})",
    "ColorRadialG" -> "ColorRadialG(${x}, ${y}, ${radius}, ${distribution}, ${colors}, ${cyclic})",
    "TexturePaint" -> "TexturePaint(${fileName}, ${x}, ${y})",
    "Font" -> "Font(${name}, ${size})",
    "ColorHSB" -> "ColorHSB(${h}, ${s}, ${b})",
    "Vector2D" -> "Vector2D(${x}, ${y})",
    "angle" -> "angle(${vector})",
    "angleTo" -> "angleTo(${vector})",
    "fastDraw" -> "fastDraw {\n    ${cursor}\n}",
    "stopMp3" -> "stopMp3()",
    "stopMusic" -> "stopMusic()",
    "addCodeTemplates" -> "addCodeTemplates(${lang}, ${templates})",
    "addHelpContent" -> "addHelpContent(${lang}, ${content})",
    "Point" -> "Point(${x}, ${y})",
    "Point2D" -> "Point2D(${x}, ${y})",
    "drawStage" -> "drawStage(${background})",
    "bounceVecOffStage" -> "bounceVecOffStage(${vec}, ${forPic})",
    "bouncePicVectorOffStage" -> "bouncePicVectorOffStage(${pic}, ${vec})",
    "bouncePicVectorOffPic" -> "bouncePicVectorOffPic(${pic}, ${vec}, ${obstacle})",
    "countDownLatch" -> "countDownLatch(${count})",
    "setEditorTabSize" -> "setEditorTabSize(${tabSize})",
    "setCostumes" -> "setCostumes(${costume1}, ${etc})",
    "setCostumeImage" -> "setCostumeImage(${img})",
    "setCostumeImages" -> "setCostumeImages(${img1}, ${etc})",
    "nextCostume" -> "nextCostume()",
    "scaleCostume" -> "scaleCostume(${factor})",
    "changePosition" -> "changePosition(${x}, ${y})",
    "pause" -> "pause(${secs})",
    "showGrid" -> "showGrid()",
    "hideGrid" -> "hideGrid()",
    "showAxes" -> "showAxes()",
    "hideAxes" -> "hideAxes()",
    "showProtractor" -> "showProtractor()",
    "hideProtractor" -> "hideProtractor()",
    "showScale" -> "showScale()",
    "hideScale" -> "hideScale()",
    "foreach" -> "foreach { n => \n    ${cursor}\n}",
    "map" -> "map { n => \n    ${cursor}\n}",
    "filter" -> "filter { n => \n    ${cursor}\n}",
    "Label" -> "Label(${label})",
    "TextField" -> "TextField(${default})",
    "TextArea" -> "TextArea(${initialContent})",
    "DropDown" -> "DropDown(${elems})",
    "Button" -> "Button(${label}) {\n    ${cursor}\n}",
    "ToggleButton" -> "ToggleButton(${label}) { on =>\n    ${cursor}\n}",
    "Slider" -> "Slider(${min}, ${max}, ${current}, ${tickSpacing})",
    "RowPanel" -> "RowPanel(\n      ${widget},\n      w\n)",
    "ColPanel" -> "ColPanel(\n      ${widget},\n      w\n)",
    "repeatFor" -> "repeatFor(${seq}) { ${e} =>\n    ${cursor}\n}",
    "randomFrom" -> "randomFrom(${seq})",
    "round" -> "round(${n}, ${digits})",
    "image" -> "image(${height}, ${width})",
    "PicShape.image" -> "", // use default via reflection
    "setImagePixel" -> "setImagePixel(${img}, ${x}, ${y}, ${color})",
    "scroll" -> "scroll(${x}, ${y})",
    "breakpoint" -> "breakpoint(${msg})",
    "beginShape" -> "beginShape()",
    "endShape" -> "endShape()"
  )

  val TwMethodTemplates = Map(
    "turnNorth" -> "turnNorth()",
    "turnSouth" -> "turnSouth()",
    "turnEast" -> "turnEast()",
    "turnWest" -> "turnWest()",
    "newTurtle" -> "newTurtle(${x}, ${y})",
    "pict" -> "PictureT { t =>\n    import t._\n    ${cursor}\n}",
    "PictureT" -> "PictureT { t =>\n    import t._\n    ${cursor}\n}",
    "circle" -> "circle(${radius})",
    "arc" -> "arc(${radius}, ${angle})",
    "ellipse" -> "ellipse(${radius1}, ${radius2})",
    "show" -> "draw(${pic/s})",
    "wipe" -> "wipe()",
    "morph" -> "morph { polyLines =>\n    ${cursor}\n}",
    "PolyLine" -> "PolyLine(List(Point2D(${x1}, ${y1}), Point2D(${x2}, ${y2})))",
    "dot" -> "dot(${diameter})",

    // Picture Completions
    "InputAware.onMousePress" -> "onMousePress { (x, y) =>\n    ${cursor}\n}",
    "InputAware.onMouseRelease" -> "onMouseRelease { (x, y) =>\n    ${cursor}\n}",
    "InputAware.onMouseClick" -> "onMouseClick { (x, y) =>\n    ${cursor}\n}",
    "InputAware.onMouseDrag" -> "onMouseDrag { (x, y) =>\n    ${cursor}\n}",
    "InputAware.onMouseMove" -> "onMouseMove { (x, y) =>\n    ${cursor}\n}",
    "InputAware.onMouseEnter" -> "onMouseEnter { (x, y) =>\n    ${cursor}\n}",
    "InputAware.onMouseExit" -> "onMouseExit { (x, y) =>\n    ${cursor}\n}",
    "Picture.act" -> "act { self => \n    ${cursor}\n}",
    "CorePicOps2.react" -> "react { self => \n    ${cursor}\n}",
    "setSpeed" -> "setSpeed(${speed})",
    "setSlowness" -> "setSlowness(${delay})"
  )

  val VnMethodTemplates = Map[String, String](
  )

  @volatile var ExtraMethodTemplates: collection.Map[String, String] = TwMethodTemplates

  def activateTw(): Unit = {
    ExtraMethodTemplates = TwMethodTemplates
    clearLangTemplates()
    Help.activateTw()
  }

  def activateVn(): Unit = {
    ExtraMethodTemplates = VnMethodTemplates
    clearLangTemplates()
    Help.activateVn()
  }

  val langTemplates: collection.mutable.Map[String, Map[String, String]] = collection.mutable.Map()
  def addTemplates(lang: String, templates: Map[String, String]): Unit = {
//    import util.Typeclasses._
//    langTemplates +=  (lang -> (langTemplates.getOrElse(lang, Map()) |+| templates))
    langTemplates += (lang -> (langTemplates.getOrElse(lang, Map()) ++ templates))
  }

  def clearLangTemplates(): Unit = {
    langTemplates.clear()
  }

  def langMethodTemplate(name: String, lang: String): Option[String] = {
    langTemplates.get(lang) match {
      case Some(ts) => ts.get(name)
      case None     => None
    }
  }

  def methodTemplate(completion: String): String = {
    BuiltinsMethodTemplates.getOrElse(
      completion,
      ExtraMethodTemplates.getOrElse(
        completion,
        langMethodTemplate(completion, System.getProperty("user.language")).getOrElse(null)
      )
    )
  }

  def keywordTemplate(completion: String) = {
    KeywordTemplates.getOrElse(completion, null)
  }

  val MethodDropFilter = List("turtle0")
  val VarDropFilter = List("builtins", "predef")
  val InternalVarsRe = java.util.regex.Pattern.compile("""res\d+|\p{Punct}.*""")
  val InternalMethodsRe = java.util.regex.Pattern.compile("""_.*|.*\$.*""")

  def notIdChar(c: Char): Boolean = NotIdChars.contains(c)

  def findLastIdentifier(rstr: String): Option[String] = {
    val str = " " + rstr
    var remaining = str.length
    while (remaining > 0) {
      if (notIdChar(str(remaining - 1))) return Some(str.substring(remaining))
      remaining -= 1
    }
    None
  }

  def findIdentifier(str: String): (Option[String], Option[String]) = {
    if (str.length == 0) return (None, None)

    if (str.endsWith(".")) {
      (findLastIdentifier(str.substring(0, str.length - 1)), None)
    }
    else {
      val lastDot = str.lastIndexOf('.')
      if (lastDot == -1) (None, findLastIdentifier(str))
      else {
        val tPrefix = str.substring(lastDot + 1)
        if (tPrefix == findLastIdentifier(tPrefix).get)
          (findLastIdentifier(str.substring(0, lastDot)), Some(tPrefix))
        else
          (None, findLastIdentifier(tPrefix))
      }
    }
  }
}
