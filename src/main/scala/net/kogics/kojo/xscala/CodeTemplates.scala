package net.kogics.kojo.xscala

object CodeTemplates {
  // this template is used twice, so define as val outside the map
  val canvasSketch = """cleari()
originBottomLeft()
setBackground(white)

class Sketch {
    var x = 0
    def setup(surface: CanvasDraw) {
        import surface._
        strokeWeight(4)
        rect(0, cheight/2, 40, 40)
        ${cursor}
    }

    def drawLoop(surface: CanvasDraw) {
        import surface._
        background(255)
        x += 2
        rect(x, cheight/2, 40, 40)
    }
}

val sketch = new Sketch
canvasSketch(sketch)
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
    "sketch" -> canvasSketch,
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