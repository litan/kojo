val ddFont = Font("Monospaced", 17)
val tFont = Font("Serif", 17)
val tfFont = Font("Monospaced", 20, BoldFont)

import javax.swing.JPanel
import javax.swing.JScrollPane

val codePanelColor = Color(180, 222, 255)

object ProgramStatus extends Enumeration {
    val good, bad, incomplete = Value
}

val sb = new StringBuilder
def queueInterpret(code: String) {
    sb.append(code)
    sb.append("\n")
}

def flushInterpretQ() {
    val code = sb.toString
    //    println(s"Running code:\n$code")
    interpret(code)
    sb.clear()
}

val tfsm = collection.mutable.Map.empty[String, collection.mutable.ArrayBuffer[Label]]
kojoInterp.bind("tfsm", "scala.collection.mutable.Map[String,scala.collection.mutable.ArrayBuffer[net.kogics.kojo.widget.Label]]", tfsm)

val panelm = collection.mutable.Map.empty[String, JPanel]
kojoInterp.bind("panelm", "scala.collection.mutable.Map[String,javax.swing.JPanel]", panelm)

val mistakesm = collection.mutable.Map.empty[String, Int]

val initCode = s"""
def updateProgStatus(nm: String, idx: Int, marker: String, color: Color) = runInGuiThread { 
    val tf = tfsm(nm)(idx)
    val panel = panelm(nm)
    tf.setText(marker)
    tf.setForeground(color)
    panel.revalidate()
    panel.repaint() 
}

def updateCanvasMarker(t2: Turtle, x: Double, y: Double, marker: String, color: Color) {
    t2.clear()
    t2.invisible()
    t2.setPenFontSize(35)
    t2.setPosition(x, y)
    t2.setPenColor(color)
    t2.write(marker)
}

def initTurtles(t1: Turtle, t2: Turtle) {
    t1.clear()
    t1.invisible()
    t1.setPenColor(black)
    t1.beamsOn()
    t2.clear()
    t2.invisible()
}

def updateMistakes(t2: Turtle, x: Double, y: Double, mistakes: Int, total: Boolean = false) {
    t2.setPenFontSize(17)
    t2.setPenColor(darkGray)
    t2.setPosition(x, y)
    val msg = if (total) "Total Mistakes" else "Mistakes"
    t2.write("%s: %d" format (msg, mistakes))
}
"""
queueInterpret(initCode)

@volatile var totalMistakes = 0
val mbox = textExtent("Mistakes: 1000", 17)

def challengePage(challengeCode: String, help: Option[xml.Node], nm: String, last: Boolean) = Page(
    name = nm,
    body =
        <body style="font-size:small; background-color:#b4deff; color:#1a1a1a">
          <div style="text-align:center; ">
            Challenge { nm } 
          </div>
          <br/> 
          <div style="font-size:x-small; ">
            { help.getOrElse("") } 
          </div>
          
        </body>,
    code = {
        cleari()
        setAnimationDelay(0)
        setPenColor(Color(255, 109, 44, 220))
        setPenThickness(ChallengePenWidth)
        queueInterpret(challengeCode)
        queueInterpret("""
          val t1 = newTurtle(0, 0)
          t1.beamsOn()
          val t2 = newTurtle(0, 0)
          t2.invisible()
        """)
        flushInterpretQ()

        if (BlockNextLevel) {
            stDisableNextButton()
        }

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
                            val tf = Label(" ")
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
                val cb = canvasBounds
                var mistakes = mistakesm.getOrElseUpdate(nm, 0)
                tfs.foreach { tf => tf.setText(" ") }
                uiPanel.revalidate(); uiPanel.repaint()
                val dropDowns = findDropDowns(uiPanel)
                val codeLines = dropDowns.map { _.asInstanceOf[net.kogics.kojo.widget.DropDown[String]].value }
                val codeLinesWithIdx = codeLines.zipWithIndex
                val firstError = codeLines.zip(expectedCode).indexWhere { e => e._1 != e._2 }
                def repeatsBefore(n: Int, lines: Seq[String]) = {
                    def repeatsIn(lines: Seq[String]) = {
                        val reps = lines.zipWithIndex.filter { case (line, idx) => line.contains("repeat") || line.contains("}") }
                        val st = collection.mutable.Stack.empty[Int]; val rps = collection.mutable.ArrayBuffer.empty[(Int, Int)]
                        reps.foreach { case (line, idx) => if (line.contains("repeat")) { st.push(idx) } else { rps += (st.pop -> idx) } }
                        rps
                    }
                    repeatsIn(lines).filter { case (start, end) => end < n }
                }
                val successfulRepeats = repeatsBefore(firstError, codeLines)
                def successfulRepeat(idx: Int) = {
                    successfulRepeats.find { case (start, end) => idx == start || idx == end }.isDefined
                }
                def runCode(code: String, idx: Int, status: ProgramStatus.Value) {
                    code match {
                        case c if c.trim == "" =>
                        case c if c.contains("repeat") =>
                            queueInterpret(code)
                        case c if c.trim == "}" =>
                            queueInterpret(code)
                        case _ => queueInterpret(s"t1.$code")
                    }

                    val marker = if (status == ProgramStatus.good) "\u2714" else if (status == ProgramStatus.bad) "x" else "?"
                    val color = if (status == ProgramStatus.good) "Color(0, 190, 65)" else if (status == ProgramStatus.bad) "red" else "blue"
                    queueInterpret(s"""updateProgStatus("$nm", $idx, "$marker", $color)""")
                    queueInterpret(s"""updateCanvasMarker(t2, ${cb.x + cb.width / 4}, ${cb.y + cb.height / 4}, "$marker", $color)""")
                }
                def firstErrorBlank = codeLines(firstError).trim == ""
                queueInterpret("initTurtles(t1, t2)")
                if (firstError != -1) {
                    queueInterpret("t1.setAnimationDelay(0)")
                    codeLinesWithIdx.take(firstError).foreach {
                        case (codeLine, idx) =>
                            if (idx == firstError - 1 && firstErrorBlank) {
                                queueInterpret("t1.visible(); t1.setAnimationDelay(500)")
                            }
                            if ((codeLine.contains("repeat") || codeLine.trim.contains("}")) && !successfulRepeat(idx)) {
                                // do nothing
                            }
                            else {
                                runCode(codeLine, idx, ProgramStatus.good)
                            }
                    }
                    if (firstErrorBlank) {
                        runCode(codeLines(firstError), codeLinesWithIdx(firstError)._2, ProgramStatus.incomplete)
                    }
                    else {
                        queueInterpret("t1.visible(); t1.setAnimationDelay(500)")
                        runCode(codeLines(firstError), codeLinesWithIdx(firstError)._2, ProgramStatus.bad)
                        mistakes += 1
                        mistakesm(nm) = mistakes
                        totalMistakes += 1
                    }
                    if (firstError == 0) {
                        queueInterpret("t1.visible()")
                    }
                    if (ShowMistakes) { queueInterpret(s"""updateMistakes(t2, ${-cb.x - mbox.width}, ${cb.y + mbox.height}, $mistakes)""") }
                }
                else {
                    queueInterpret("t1.visible(); t1.setAnimationDelay(300)")
                    codeLinesWithIdx.foreach { case (codeLine, idx) => runCode(codeLine, idx, ProgramStatus.good) }
                    queueInterpret(s"""t2.clear(); ; t2.invisible(); t2.setPosition(${cb.x + cb.width / 8}, ${cb.y + cb.height / 8}); t2.setPenColor(Color(0, 160, 65)); t2.write("Well Done! You have finished this Level.")""")
                    if (!last) {
                        queueInterpret(s"""t2.setPosition(${cb.x + cb.width / 8}, ${cb.y + cb.height / 8 - 30}); t2.write("Click the 'Next' button to move to the next Level.")""")
                        queueInterpret("""stEnableNextButton()""")
                        if (ShowMistakes) { queueInterpret(s"""updateMistakes(t2, ${-cb.x - mbox.width}, ${cb.y + mbox.height}, $mistakes)""") }
                    }
                    else {
                        queueInterpret(s"""t2.setPosition(${cb.x + cb.width / 8}, ${cb.y + cb.height / 8 - 30}); t2.write("Congratulations on finishing the challenge!")""")
                        if (ShowMistakes) { queueInterpret(s"""updateMistakes(t2, ${-cb.x - mbox.width * 3 / 2}, ${cb.y + mbox.height}, $totalMistakes, true)""") }
                    }
                }
                flushInterpretQ()

            }) {
                setFont(tFont)
                setForeground(Color(0, 160, 65))
            }

            stSetUserControlsBg(codePanelColor)
            stAddUiBigComponent(uiPanel)
            stAddUiComponent(ToggleButton("Grid") { on => if (on) { showAxes(); showGrid() } else { hideAxes(); hideGrid() } })
            stAddUiComponent(ToggleButton("Tools") { on => if (on) { showScale(); showProtractor() } else { hideScale(); hideProtractor() } })
            stAddUiComponent(Button("Reset Zoom") { resetPanAndZoom() })
            stAddUiComponent(runButton)
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

def alternativesFor(line: String) = line match {
    case l if l.trim == ""         => Seq("")
    case l if l.contains("repeat") => Seq(line)
    case l if l.trim == "}"        => Seq(l)
    case _ =>
        val spaces = line.takeWhile(_ == ' ')
        Seq("") ++ util.Random.shuffle(line +: { for (i <- 1 to NumCmdChoices - 1) yield (spaces + altLine(line, i)) })
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
        challengePage(code, levelsHelp.get(idx + 1), s"Level ${idx + 1}", last)
}
val story = Story(levelPages: _*)

cleari()
stClear()
stPlayStory(story)
switchToStoryViewingPerspective()
stSetStorytellerWidth(350)
