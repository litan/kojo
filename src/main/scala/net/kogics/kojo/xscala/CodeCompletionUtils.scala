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
    "yield")

  val KeywordTemplates = Map(
    "for" -> "for (i <- 1 to ${n}) {\n    ${cursor}\n}",
    "while" -> "while (${condition}) {\n    ${cursor}\n}",
    "if" -> "if (${condition}) {\n    ${cursor}\n}"
  )

  // UserCommand adds to this
  val BuiltinsMethodTemplates = collection.mutable.Map(
    "resetInterpreter" -> "resetInterpreter()",
    "switchToDefaultPerspective" -> "switchToDefaultPerspective()",
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
    "onKeyPress" -> "onKeyPress { k =>\n    k match {\n        case Kc.VK_RIGHT => ${cursor}\n        case _ => \n    }\n}",
    "onKeyRelease" -> "onKeyRelease { k =>\n    k match {\n        case Kc.VK_RIGHT => ${cursor}\n        case _ => \n    }\n}",
    "onMousePress" -> "onMousePress { (x, y) =>\n    ${cursor}\n}",
    "onMouseClick" -> "onMouseClick { (x, y) =>\n    ${cursor}\n}",
    "onMouseDrag" -> "onMouseDrag { (x, y) =>\n    ${cursor}\n}",
    "stAddLinkHandler" -> "stAddLinkHandler(${handlerName}, ${story}) {d: ${argType} =>\n    ${cursor}\n}",
    "stAddLinkEnterHandler" -> "stAddLinkHandler(${handlerName}, ${story}) {d: ${argType} =>\n    ${cursor}\n}",
    "stAddLinkExitHandler" -> "stAddLinkHandler(${handlerName}, ${story}) {d: ${argType} =>\n    ${cursor}\n}",
    "stInsertCodeInline" -> "stInsertCodeInline(${code})",
    "stInsertCodeBlock" -> "stInsertCodeBlock(${code})",
    "stOnStoryStop" -> "stOnStoryStop {\n    ${cursor}\n}",
    "HPics" -> "HPics(\n      p,\n      p\n)",
    "picRow" -> "picRow(\n      p,\n      p\n)",
    "VPics" -> "VPics(\n      p,\n      p\n)",
    "picCol" -> "picCol(\n      p,\n      p\n)",
    "GPics" -> "GPics(\n      p,\n      p\n)",
    "picStack" -> "picStack(\n      p,\n      p\n)",
    // Todo - is there any commonality here with the staging templates
    "trans" -> "trans(${x}, ${y})",
    "rot" -> "rot(${angle})",
    "rotp" -> "rotp(${angle}, ${x}, ${y})",
    "scale" -> "scale(${factor})",
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
    "spin" -> "spin(${n})",
    "reflect" -> "reflect(${gap})",
    "draw" -> "draw(${pic})",
    "drawAndHide" -> "drawAndHide(${pic})",
    "setRefreshRate" -> "setRefreshRate(${framesPerSec})",
    "setScreenDPI" -> "setScreenDPI(${dotsPerInch})",
    "animate" -> "animate {\n    ${cursor}\n}",
    "schedule" -> "schedule (${inSecs}) {\n    ${cursor}\n}",
    "runInBackground" -> "runInBackground {\n    ${cursor}\n}",
    "runInGuiThread" -> "runInGuiThread {\n    ${cursor}\n}",
    "onAnimationStop" -> "onAnimationStop {\n    ${cursor}\n}",
    "act" -> "act { self => // we give this turtle the name 'self' within act {...}\n    ${cursor}\n}",
    "react" -> "react { self => \n    ${cursor}\n}",
    "row" -> "row(${picture}, ${n})",
    "col" -> "col(${picture}, ${n})",
    "setUnitLength" -> "setUnitLength(${unit})",
    "clearWithUL" -> "clearWithUL(${unit})",
    "clearOutput" -> "clearOutput()",
//    "intersects" -> "intersects(${otherPic})",
//    "intersection" -> "intersection(${otherPic})",
//    "collidesWith" -> "collidesWith(${otherPic})",
//    "collisions" -> "collisions(pics)",
//    "distanceTo" -> "distanceTo(${otherPic})",
    "stopAnimation" -> "stopAnimation()",
    "isKeyPressed" -> "isKeyPressed(Kc.VK_${cursor})",
    "activateCanvas" -> "activateCanvas()",
    "activateEditor" -> "activateEditor()",
    "setBackground" -> "setBackground(${paint})",
    "setBackgroundH" -> "setBackgroundH(${color1}, ${color2})",
    "setBackgroundV" -> "setBackgroundV(${color1}, ${color2})",
    "Color" -> "Color(${red}, ${green}, ${blue}, ${opacity})",
    "ColorG" -> "ColorG(${x1}, ${y1}, ${color1}, ${x2}, ${y2}, ${color2}, ${cyclic})",
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
    "countDownLatch" -> "countDownLatch(${count})",
    "setEditorTabSize" -> "setEditorTabSize(${tabSize})",
    "setCostumes" -> "setCostumes(${costume1}, ${etc})",
    "nextCostume" -> "nextCostume()",
    "scaleCostume" -> "scaleCostume(${factor})",
    "changePosition" -> "changePosition(${x}, ${y})",
    "pause" -> "pause(${secs})"
  )
  
  val TwMethodTemplates = Map(
    "newTurtle" -> "newTurtle(${x}, ${y})",
    "pict" -> "PictureT { t =>\n    import t._\n    ${cursor}\n}",
    "PictureT" -> "PictureT { t =>\n    import t._\n    ${cursor}\n}",
    "Picture" -> "Picture {\n    ${cursor}\n}",
    "circle" -> "circle(${radius})",
    "arc" -> "arc(${radius}, ${angle})",
    "show" -> "draw(${pic/s})",
    "wipe" -> "wipe()",
    "morph" -> "morph { polyLines =>\n    ${cursor}\n}",
    "PolyLine" -> "PolyLine(List(Point2D(${x1}, ${y1}), Point2D(${x2}, ${y2})))"
  ) 

  val MwMethodTemplates = Map(
    "figure" -> "figure(${name})",
    "point" -> "point(${x}, ${y}, ${optionalLabel})",
    "pointOn" -> "pointOn(${line}, ${x}, ${y})",
    "line" -> "line(${point1}, ${point2})",
    "lineSegment" -> "lineSegment(${point1}, ${point2Orlength})",
    "ray" -> "ray(${point1}, ${point2})",
    "circle" -> "circle(${center}, ${radius})",
    "angle" -> "angle(${point1}, ${point2}, ${pointOrSize})",
    "intersect" -> "intersect(${shape1}, ${shape2})",
    "midpoint" -> "midpoint(${lineSegment})",
    "perpendicular" -> "perpendicular(${toLine}, ${thruPoint})",
    "parallel" -> "perpendicular(${toLine}, ${thruPoint})",
    "setLabel" -> "setLabel(${label})",
    "setColor" -> "setColor(${color})",
    "add" -> "add(${shapes})",
    "show" -> "show(${shapes})",
    "showGrid" -> "showGrid()",
    "hideGrid" -> "hideGrid()",
    "showAxes" -> "showAxes()",
    "hideAxes" -> "hideAxes()",
    "showAlgebraView" -> "showAlgebraView()",
    "hideAlgebraView" -> "hideAlgebraView()",
    "turtle" -> "turtle(${x}, ${y})",
    "labelPosition" -> "labelPosition(${label})",
    "findPoint" -> "findPoint(${label})",
    "findLine" -> "findLine(${label})",
    "findAngle" -> "findAngle(${label})",
    "beginPoly" -> "beginPoly()",
    "endPoly" -> "endPoly()",
    "showAngles" -> "showAngles()",
    "showLengths" -> "showLengths()",
    "hideLengths" -> "hideLengths()",
    "hideAngles" -> "hideAngles()",
    "showExternalAngles" -> "showExternalAngles()",
    "hideExternalAngles" -> "hideExternalAngles()"
  )

  val StagingMethodTemplates = Map(
    "point" -> "point(${x}, ${y})",
    "line" -> "line(${x0}, ${y0}, ${x1}, ${y1})",
    "rectangle" -> "rectangle(${x0}, ${y0}, ${width}, ${height})",
    "text" -> "text(${content}, ${x}, ${y})",
    "circle" -> "circle(${cx}, ${cy}, ${radius})",
    "ellipse" -> "ellipse(${cx}, ${cy}, ${width}, ${height})",
    "arc" -> "arc(${cx}, ${cy}, ${rx}, ${ry}, ${startDegree}, ${extentDegree})",
    "refresh" -> "refresh {\n    ${cursor}\n}",
    "screenSize" -> "screenSize(${width}, ${height})",
    "background" -> "background(${color})",
    "dot" -> "dot(${x}, ${y})",
    "square" -> "square(${x}, ${y}, ${side})",
    "roundRectangle" -> "roundRectangle(${x}, ${y}, ${width}, ${height}, ${rx}, ${ry})",
    "pieslice" -> "pieslice(${cx}, ${cy}, ${rx}, ${ry}, ${startDegree}, ${extentDegree})",
    "openArc" -> "openArc(${cx}, ${cy}, ${rx}, ${ry}, ${startDegree}, ${extentDegree})",
    "chord" -> "chord(${cx}, ${cy}, ${rx}, ${ry}, ${startDegree}, ${extentDegree})",
    "vector" -> "vector(${x0}, ${y0}, ${x1}, ${y1}, ${headLength})",
    "star" -> "star(${cx}, ${cy}, ${inner}, ${outer}, ${numPoints})",
    "polyline" -> "polyline(${points})",
    "polygon" -> "polygon(${points})",
    "triangle" -> "triangle(${point1}, ${point2}, ${point3})",
    "quad" -> "quad(${point1}, ${point2}, ${point3}, ${point4})",
    "svgShape" -> "svgShape(${element})",
    "grayColors" -> "grayColors(${highGrayNum})",
    "grayColorsWithAlpha" -> "grayColorsWithAlpha(${highGrayNum}, ${highAlphaNum})",
    "rgbColors" -> "rgbColors(${highRedNum}, ${highGreenNum}, ${highBlueNum})",
    "rgbColorsWithAlpha" -> "rgbColorsWithAlpha(${highRedNum}, ${highGreenNum}, ${highBlueNum}, ${highAlphaNum})",
    "hsbColors" -> "hsbColors(${highHueNum}, ${highSaturationNum}, ${highBrightnessNum})",
    "namedColor" -> "namedColor(${colorName})",
    "lerpColor" -> "lerpColor(${colorFrom}, ${colorTo}, ${amount})",
    "fill" -> "fill(${color})",
    "noFill" -> "noFill()",
    "stroke" -> "stroke(${color})",
    "noStroke" -> "noStroke()",
    "strokeWidth" -> "strokeWidth(${width})",
    "withStyle" -> "withStyle (${fillColor}, ${strokeColor}, ${strokeWidth}) {\n    ${cursor}\n}",
    "constrain" -> "constrain(${value}, ${min}, ${max})",
    "norm" -> "norm(${value}, ${low}, ${high})",
    "map" -> "map(${value}, ${min1}, ${max1}, ${min2}, ${max2})",
    "sq" -> "sq(${value})",
    "sqrt" -> "sqrt(${value})",
    "dist" -> "dist(${x0}, ${y0}, ${x1}, ${y1})",
    "mag" -> "mag(${x}, ${y})",
    "lerp" -> "lerp(${low}, ${high}, ${value})",
    "loop" -> "loop {\n    ${cursor}\n}",
    "stop" -> "stop()",
    "reset" -> "reset()",
    "wipe" -> "wipe()",
    "sprite" -> "sprite(${x}, ${y}, ${filename})",
    "path" -> "path(${x}, ${y})",
    "lineTo" -> "lineTo(${x}, ${y})",
    "setFontSize" -> "setFontSize(${size})",
    "setContent" -> "setContent(${content})",
    "show" -> "show()",
    "hide" -> "hide()",
    "erase" -> "erase()"
  )
  
  val D3MethodTemplates = Map(
    "clear" -> "clear()",
    "render" -> "render()",
    "renderAlways" -> "renderAlways()",
    "renderOnRequest" -> "renderOnRequest()",
    "sphere" -> "sphere(${radius})",
    "cylinder" -> "cylinder(${radius}, ${height})",
    "plane" -> "plane()",
    "cube" -> "cube(${dimension})",
    "pointLight" -> "pointLight(${r}, ${g}, ${b}, ${x}, ${y}, ${z})",
    "forward" -> "forward(${distance})",
    "back" -> "back(${distance})",
    "turn" -> "turn(${angle})",
    "left" -> "left(${angle})",
    "right" -> "right(${angle})",
    "pitch" -> "pitch(${angle})",
    "roll" -> "roll(${angle})",
    "moveTo" -> "moveTo(${x}, ${y}, ${z})",
    "lookAt" -> "lookAt(${x}, ${y}, ${z})",
    "strafeUp" -> "strafeUp(${distance})",
    "strafeDown" -> "strafeDown(${distance})",
    "strafeLeft" -> "strafeLeft(${distance})",
    "strafeRight" -> "strafeRight(${distance})",
    "invisible" -> "invisible()",
    "visible" -> "visible()",
    "trailOn" -> "trailOn()",
    "trailOff" -> "trailOff()",
    "lineWidth" -> "lineWidth(${width})",
    "color" -> "color(${r}, ${g}, ${b})",
    "cameraForward" -> "cameraForward(${distance})",
    "cameraBack" -> "cameraBack(${distance})",
    "cameraTurn" -> "cameraTurn(${angle})",
    "cameraLeft" -> "cameraLeft(${angle})",
    "cameraRight" -> "cameraRight(${angle})",
    "cameraPitch" -> "cameraPitch(${angle})",
    "cameraRoll" -> "cameraRoll(${angle})",
    "cameraStrafeUp" -> "cameraStrafeUp(${distance})",
    "cameraStrafeDown" -> "cameraStrafeDown(${distance})",
    "cameraStrafeLeft" -> "cameraStrafeLeft(${distance})",
    "cameraStrafeRight" -> "cameraStrafeRight(${distance})",
    "cameraMoveTo" -> "cameraMoveTo(${x}, ${y}, ${z})",
    "cameraLookAt" -> "cameraLookAt(${x}, ${y}, ${z})",
    "cameraAngle" -> "cameraAngle(${angle})",
    "enableMouseControl" -> "enableMouseControl()",
    "disableMouseControl" -> "disableMouseControl()",
    "axesOn" -> "axesOn()",
    "axesOff" -> "axesOff()",
    "defaultLightsOn" -> "defaultLightsOn()",
    "defaultLightsOff" -> "defaultLightsOff()",
    "imageInterpolationOn" -> "imageInterpolationOn()",
    "imageInterpolationOff" -> "imageInterpolationOff()",
    "enableOrthographicMode" -> "enableOrthographicMode()",
    "disableOrthographicMode" -> "disableOrthographicMode()"
  ) 
  
  
  
  @volatile var ExtraMethodTemplates: collection.Map[String, String] = TwMethodTemplates

  def activateTw() {
    ExtraMethodTemplates = TwMethodTemplates
    clearLangTemplates()
    Help.activateTw()
  }

  def activateMw() {
    ExtraMethodTemplates = MwMethodTemplates
    clearLangTemplates()
    Help.activateMw()
  }

  def activateStaging() {
    ExtraMethodTemplates = StagingMethodTemplates
    clearLangTemplates()
    Help.activateStaging()
  }

  def activateD3() {
    ExtraMethodTemplates = D3MethodTemplates
    clearLangTemplates()
    Help.activateD3()
  }

  val langTemplates: collection.mutable.Map[String, Map[String, String]] = collection.mutable.Map()
  def addTemplates(lang: String, templates: Map[String, String]) {
//    import util.Typeclasses._
//    langTemplates +=  (lang -> (langTemplates.getOrElse(lang, Map()) |+| templates))
      langTemplates +=  (lang -> (langTemplates.getOrElse(lang, Map()) ++ templates))
  }
  
  def clearLangTemplates() {
    langTemplates.clear()
  }
  
  def langMethodTemplate(name: String, lang: String): Option[String] = {
    langTemplates.get(lang) match {
      case Some(ts) => ts.get(name)
      case None => None
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

  def notIdChar(c: Char): Boolean =  NotIdChars.contains(c)

  def findLastIdentifier(rstr: String): Option[String] = {
    val str = " " + rstr
    var remaining = str.length
    while(remaining > 0) {
      if (notIdChar(str(remaining-1))) return Some(str.substring(remaining))
      remaining -= 1
    }
    None
  }

  def findIdentifier(str: String): (Option[String], Option[String]) = {
    if (str.length == 0) return (None, None)

    if (str.endsWith(".")) {
      (findLastIdentifier(str.substring(0, str.length-1)), None)
    }
    else {
      val lastDot = str.lastIndexOf('.')
      if (lastDot == -1) (None, findLastIdentifier(str))
      else {
        val tPrefix = str.substring(lastDot+1)
        if (tPrefix == findLastIdentifier(tPrefix).get)
          (findLastIdentifier(str.substring(0, lastDot)), Some(tPrefix))
        else
          (None, findLastIdentifier(tPrefix))
      }
    }
  }
}
