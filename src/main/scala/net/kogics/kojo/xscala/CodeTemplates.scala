package net.kogics.kojo.xscala

object CodeTemplates {
  // this template is used twice, so define as val outside the map
  val canvasSketch = """cleari()
originBottomLeft()
setBackground(white)

class Sketch {
    def setup(surface: CanvasDraw) {
        import surface._
        stroke(${cursor}gray)
        strokeWeight(1)
    }

    def drawLoop(surface: CanvasDraw) {
        import surface._
        val xloc = random(cwidth)
        val yloc = random(cheight)
        fill(cm.rgba(0, 50, mathx.constrain(yloc, 100, 150).toInt, 50))
        val dia = mathx.constrain(xloc / 10, 5, 100)
        ellipse(xloc, yloc, dia, dia)
    }
}

val sketch = new Sketch
val pic = Picture.fromSketch(sketch, 1)
draw(pic)
"""

  val templates = Map(
    "pre" -> """<pre>
    ${cursor}
</pre>
""",
    "incr" -> """IncrPage(
    name = "",
    style = "",
    body = List(
        Para(
            <p>
                ${cursor}
            </p>
        ),
        Para(
            <p>
                
            </p>
        )
    )
)
""",
    "melody" -> """Melody("${cursor}", "")""",
    "mscore" -> """MusicScore(
    Melody("${cursor}", ""),
    Rhythm("", "", "")
)
""",
    "rythm" -> """Rhythm("${cursor}", "q", "o")""",
    "page" -> """Page(
    name = "",
    body =
        <body>
            ${cursor}
        </body>,
    code = {}
)
""",
    "para" -> """Para(
    <p>
        ${cursor}
    </p>,
    code = {}
)
""",
    "a" -> """<a href="${cursor}"></a>""",
    "br" -> "<br/>${cursor} ",
    "code" -> "<code>${cursor}</code>",
    "div" -> """<div>
    ${cursor}
</div>
""",
    "em" -> "<em>${cursor}</em>",
    "tt" -> "<tt>${cursor}</tt>",
    "li" -> "<li>${cursor}</li>",
    "p" -> """<p>
    ${cursor}
</p>
""",
    "span" -> "<span>${cursor}</span>",
    "strong" -> "<strong>${cursor}</strong>",
    "sty" -> """style="${cursor}" """,
    "ul" -> """<ul>
    <li>${cursor}</li>
    <li></li>
</ul>
""",
    "def (function)" -> """def ${funcName}(${in1}: ${Type1}, ${in2}: ${Type2}) = {
    ${cursor}
}
""",
    "def (command)" -> """def ${cmdName}(${in1}: ${Type1}, ${in2}: ${Type2}) {
    ${cursor}
}
""",
    "canvasSketch" -> canvasSketch,
    "sketchpic" -> canvasSketch,
    "transition" -> """cleari()
                      |drawStage(white)
                      |
                      |def pic = Picture.rectangle(100, 50)
                      |
                      |def xProp(s: Seq[Double]) = s(0)
                      |def yProp(s: Seq[Double]) = s(1)
                      |def hueProp(s: Seq[Double]) = s(2)
                      |def scaleProp(s: Seq[Double]) = s(3)
                      |
                      |def makePic(s: Seq[Double]) = {
                      |    fillColor(cm.hsl(hueProp(s), 1, 0.5)) *
                      |        trans(xProp(s), yProp(s)) *
                      |        scale(scaleProp(s)) ->
                      |        pic
                      |}
                      |${cursor}
                      |val anim = Transition(
                      |    2,
                      |    Seq(0, 100, 0, 1),
                      |    Seq(300, 50, 240, 0.7),
                      |    easing.QuadInOut,
                      |    makePic,
                      |    true
                      |)
                      |
                      |run(anim)
                      |""".stripMargin,
    "timeline" -> """cleari()
                    |drawStage(white)
                    |
                    |def pic = Picture.rectangle(100, 50)
                    |
                    |def xProp(s: Seq[Double]) = s(0)
                    |def yProp(s: Seq[Double]) = s(1)
                    |def hueProp(s: Seq[Double]) = s(2)
                    |def scaleProp(s: Seq[Double]) = s(3)
                    |
                    |def makePic(s: Seq[Double]) = {
                    |    fillColor(cm.hsl(hueProp(s), 1, 0.5)) *
                    |        trans(xProp(s), yProp(s)) *
                    |        scale(scaleProp(s)) ->
                    |        pic
                    |}
                    |
                    |val kf = KeyFrames(
                    |    0 -> Seq(0, 100, 0, 1),
                    |    50 -> Seq(100, -50, 60, 1),
                    |    100 -> Seq(300, 50, 240, 0.7)
                    |)
                    |
                    |val easings = Seq(
                    |    easing.QuadIn,
                    |    easing.QuadOut
                    |)
                    |${cursor}
                    |val anim = Timeline(
                    |    4,
                    |    kf,
                    |    easings,
                    |    makePic,
                    |    false
                    |)
                    |
                    |run(anim)
                    |""".stripMargin,
    "test" -> """test("testName") {
    import Matchers._
    ${cursor}
    2 shouldBe 2
}
"""
  )

  def apply(name: String) = templates(name)
  def asString(name: String) = 
    xml.Utility.escape(templates(name).replace("${cursor}", "|").replace("$", "")).replace("\n", "<br/>")
  def beforeCursor(name: String) = templates(name).split("""\$\{cursor\}""")(0)
  def afterCursor(name: String) = templates(name).split("""\$\{cursor\}""")(1)
}