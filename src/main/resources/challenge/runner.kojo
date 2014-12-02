// This is a Story that runs the code below in 'challenge' mode for young kids
// Feel free to modify the challenge code to suit your needs.
val challengeLevels = Seq("""forward(50)
right(90)
forward(100)
left(90)
forward(50)
right(90)
forward(100)
right(90)
forward(100)
right(90)
forward(200)
""",
    """forward(50)
right(20)
forward(50)
right(30)
forward(50)
right(40)
forward(50)
right(50)
forward(50)
left(50)
forward(50)
left(40)
forward(50)
left(30)
forward(50)
left(20)
forward(50)
""",
    """left(90)
forward(200)
right(45)
forward(50)
right(135)
forward(272)
right(135)
forward(50)
""",
    """forward(100)
left(90)
forward(30)
right(135)
forward(100)
right(90)
forward(100)
right(135)
forward(30)
left(90)
forward(100)
right(90)
forward(81)
"""
)

val ddFont = Font("Monospaced", 17)
val tFont = Font("Serif", 17)
val tfFont = Font("Monospaced", 20, BoldFont)

import javax.swing.JPanel
import javax.swing.JScrollPane

val codePanelColor = Color(180, 222, 255)

object ProgramStatus extends Enumeration {
    val good, bad, incomplete = Value
}

val tfsm = collection.mutable.Map.empty[String, collection.mutable.ArrayBuffer[Label]]
kojoInterp.bind("tfsm", "scala.collection.mutable.Map[String,scala.collection.mutable.ArrayBuffer[net.kogics.kojo.widget.Label]]", tfsm)

val panelm = collection.mutable.Map.empty[String, JPanel]

def challengePage(challengeCode: String, nm: String, last: Boolean) = Page(
    name = nm,
    body =
        <body style="font-size:medium; text-align:center; background-color:#b4deff; color:#1a1a1a">
            Learning to Program<br/><br/> 
            Challenge { nm }
        </body>,
    code = {
        cleari()
        showScale()
        setAnimationDelay(0)
        setPenColor(Color(255, 109, 44))
        setPenThickness(10)
        interpret(challengeCode)
        interpret("""
          val t1 = newTurtle(0, 0)
          t1.beamsOn()
          val t2 = newTurtle(0, 0)
          t2.invisible()
        """)

        stDisableNextButton()

        runInGuiThread {
            val tfs = tfsm.getOrElseUpdate(nm, collection.mutable.ArrayBuffer.empty[Label])
            val uiPanel = panelm.getOrElseUpdate(nm, {
                tfs.clear()
                val uip: JPanel = ColPanel(
                    new RowPanel(new Label("Your Program:") { setFont(tFont) }) {
                        setBackground(codePanelColor)
                    },
                    new RowPanel(new Label(" clear()") { setFont(ddFont) }) {
                        setBackground(codePanelColor)
                    },
                    ColPanel(
                        challengeCode.lines.toVector.map { line =>
                            val tf = Label("  ")
                            tf.setFont(tfFont)
                            tfs += tf
                            val dd = DropDown[String](alternativesFor(line): _*)
                            dd.setFont(ddFont)
                            val rp = RowPanel(dd, tf)
                            rp.getLayout.asInstanceOf[java.awt.FlowLayout].setVgap(0)
                            rp.setBorder(javax.swing.BorderFactory.createEmptyBorder())
                            rp.setBackground(codePanelColor)
                            rp
                        }: _*)
                )
                uip.setBackground(codePanelColor)
                //                uip.getComponents.foreach { _.setBackground(blue) }
                uip
            })

            val expectedCode = challengeCode.lines.toVector
            val runButton = new Button("Run")({
                interpret("t1.clear(); t1.setAnimationDelay(100); t1.setPenColor(black); t1.beamsOn(); t2.clear(); t2.invisible()")
                tfs.foreach { tf => tf.setText("  ") }
                uiPanel.revalidate(); uiPanel.repaint()
                val dropDowns = findDropDowns(uiPanel)
                val codeLines = dropDowns.map { _.asInstanceOf[net.kogics.kojo.widget.DropDown[String]].value }
                val firstError = codeLines.zip(expectedCode).indexWhere { e => e._1 != e._2 }
                interpret("""
          var idx = 0
        """)
                def runCode(code: String, status: ProgramStatus.Value) {
                    if (code.trim != "") {
                        interpret(s"t1.$code")
                    }
                    val marker = if (status == ProgramStatus.good) "\u2714" else if (status == ProgramStatus.bad) "x" else "?"
                    val color = if (status == ProgramStatus.good) "Color(0, 190, 65)" else if (status == ProgramStatus.bad) "red" else "blue"
                    interpret(s"""tfsm("$nm")(idx).setText("$marker"); tfsm("$nm")(idx).setForeground($color)""")
                    interpret("idx += 1")
                    interpret(s"""t2.clear(); ; t2.invisible(); t2.setPenFontSize(35); t2.setPosition(-200, -100); t2.setPenColor($color); t2.write("$marker")""")
                }
                if (firstError != -1) {
                    codeLines.take(firstError).foreach { runCode(_, ProgramStatus.good) }
                    if (codeLines(firstError).trim == "") {
                        runCode(codeLines(firstError), ProgramStatus.incomplete)
                    }
                    else {
                        runCode(codeLines(firstError), ProgramStatus.bad)
                    }
                }
                else {
                    codeLines.foreach { runCode(_, ProgramStatus.good) }
                    interpret("""t2.clear(); ; t2.invisible(); t2.setPosition(-200, -100); t2.setPenColor(Color(0, 160, 65)); t2.write("Well Done! You have finished this Level.")""")
                    if (!last) {
                        interpret("""t2.setPosition(-200, -130); t2.write("Click the 'Next' button to move to the next Level.")""")
                        interpret("""stEnableNextButton()""")
                    }
                    else {
                        interpret("""t2.setPosition(-200, -130); t2.write("Congratulations on finishing the challenge!")""")
                    }
                }
            }) {
                setFont(tFont)
                setForeground(Color(0, 160, 65))
            }

            stSetUserControlsBg(codePanelColor)
            stAddUiBigComponent(uiPanel)
            val cb = canvasBounds
            draw(trans(cb.x, cb.y + cb.height / 2) -> PicShape.widget(runButton))
        }
    }
)

val inverse = Map(
    "left" -> "right",
    "right" -> "left"
)

def altLine(line: String, n: Int) = {
    def splitLine = {
        val st = new java.util.StringTokenizer(line, "(, )")
        import collection.JavaConversions._
        st.toVector
    }

    val lineParts = splitLine
    def delta = {
        if (lineParts.size > 2) {
            val d = (random(5) - 2)
            if (d == 0) 15 else d * 10
        }
        else if (n < 3) {
            -20 + (n - 1) * 10
        }
        else {
            (n - 2) * 10
        }
    }

    val altArgs = lineParts.drop(1).map { _.asInstanceOf[String].toInt + delta }
    val instr0 = lineParts(0).toString
    val instruction = if (randomDouble(1) < 0.7) instr0 else inverse.get(instr0).getOrElse(instr0)
    instruction + altArgs.mkString("(", ", ", ")")
}

def alternativesFor(line: String) = {
    if (line.trim == "") Seq("")
    else Seq("") ++ util.Random.shuffle(line +: { for (i <- 1 to 3) yield (altLine(line, i)) })
}

def findDropDowns(w: Widget): Seq[DropDown[String]] = {
    w.getComponents.foldLeft(Vector.empty[DropDown[String]]) { (acc, c) =>
        c match {
            case dd: DropDown[_] => acc :+ dd.asInstanceOf[DropDown[String]]
            case cp: ColPanel    => acc ++ findDropDowns(cp)
            case rp: RowPanel    => acc ++ findDropDowns(rp)
            case _               => acc
        }
    }
}
//resetInterpreter()

val levelPages = challengeLevels.zipWithIndex.map {
    case (code, idx) =>
        val last = if (idx == challengeLevels.size - 1) true else false
        challengePage(code, s"Level ${idx + 1}", last)
}
val story = Story(levelPages: _*)

cleari()
stClear()
stPlayStory(story)
switchToStoryViewingPerspective()
stSetStorytellerWidth(250)
