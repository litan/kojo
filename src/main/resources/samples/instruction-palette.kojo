// This is the Kojo script that defines the Instruction Palette
// It is silently run and added to History (as an example of powerful Kojo script) when you load the Palette

val pageStyle = "background-color:#93989c; margin:5px;font-size:small;"
val titleStyle = "font-size:95%;text-align:center;color:#1a1a1a;margin-top:5px;margin-bottom:3px;"
val headerStyle = "text-align:center;font-size:95%;color:#fafafa;font-weight:bold;"
val codeStyle = "background-color:#4a6cd4;margin-top:3px"
val linkStyle = "color:#fafafa"
val summaryLinkStyle = "color:#1a1a1a"
val codeLinkStyle = "text-decoration:none;font-size:x-small;color:#fafafa;"
val footerStyle = "font-size:90%;margin-top:15px;color:#1a1a1a;"
val helpStyle = "background-color:#ffffcc;margin:10px;"
val footerPanelColor = color(0x93989c)

val Turtle = "t"
val Pictures = "p"
val PictureXforms = "pt"
val ControlFlow = "cf"
val Abstraction = "a"
val Conditions = "c"
val Summary = "s"

val catName = Map(
    Turtle -> "Turtle",
    Pictures -> "Picture",
    PictureXforms -> "Picture Transforms",
    ControlFlow -> "Flow",
    Abstraction -> "Abstraction",
    Conditions -> "Condition"
)
def navLinks =
    <div style={ headerStyle }>
        <a style={ linkStyle } href={ "http://localpage/" + Turtle }>{ catName(Turtle) }</a> | <a style={ linkStyle } href={ "http://localpage/" + Pictures }>{ catName(Pictures) }</a> <br/>
        <a style={ linkStyle } href={ "http://localpage/" + PictureXforms }>{ catName(PictureXforms) }</a> <br/>
        <a style={ linkStyle } href={ "http://localpage/" + ControlFlow }>{ catName(ControlFlow) }</a> | <a style={ linkStyle } href={ "http://localpage/" + Conditions }>{ catName(Conditions) }</a> <br/>
        <a style={ linkStyle } href={ "http://localpage/" + Abstraction }>{ catName(Abstraction) }</a> <br/>
        <hr/>
    </div>

def footer =
    <div style={ footerStyle }>
        Click on any instruction to insert it into the Script Editor at the current Caret location.<br/>
        <br/>
        If you don't like an insertion, press Ctrl+Z in the Script Editor to undo it.<br/>
    </div>

import scala.collection.mutable.LinkedHashMap

val tTemplates = LinkedHashMap(
    "clear()" -> "clear()",
    "invisible()" -> "invisible()",
    "cleari()" -> "cleari()",
    "setAnimationDelay(d)" -> "setAnimationDelay(${c}100)",
    "forward(n)" -> "forward(${c}50)",
    "back(n)" -> "back(${c}50)",
    "right(a)" -> "right(${c}90)",
    "left(a)" -> "left(${c}90)",
    "setPenColor(c)" -> "setPenColor(${c}blue)",
    "setFillColor(c)" -> "setFillColor(${c}blue)",
    "setBackground(c)" -> "setBackground(${c}yellow)",
    "setPenThickness(t)" -> "setPenThickness(${c}4)",
    "penUp()" -> "penUp()",
    "penDown()" -> "penDown()",
    "write(t)" -> """write(${c}"Hi There")""",
    "setPenFontSize(n)" -> "setPenFontSize(${c}18)"
)

val cfTemplates = LinkedHashMap(
    "repeat    [command]" -> """repeat(${c}4) {
    forward(50)
}""",
    "if        [command]" -> """if (${c}true) {
    setPenColor(blue)
}""",
    "if-else   [command]" -> """if (${c}true) {
    setPenColor(blue)
}
else {
    setPenColor(green)
}""",
    "if-else   [expr]" -> """if (${c}true) 5 else 9""",
    "for       [command]" -> """for (i <- ${c}1 to 4) {
    println(i)
}""",
    "for       [expr]" -> """for (${c}i <- 1 to 4) yield (2 * i)""",
    "recursion [command]" -> """def ${c}pattern(n: Int) {
    if (n <= 10) {
        forward(n)
    }
    else {
        forward(n)
        right(90)
        pattern(n-5)
    }
}""",
    "recursion [function]" -> """def ${c}factorial(n: Int): Int = 
    if (n == 0) 1 else n * factorial(n-1)"""
)

val aTemplates = LinkedHashMap(
    "val       [expr]" -> "val x = ${c}10",
    "def       [command]" -> """def ${c}newcmd(n: Int) {
    forward(50)
} """,
    "def       [function]" -> """def ${c}max(n1: Int, n2: Int) = 
        if (n1 > n2) n1 else n2"""
)

val pTemplates = LinkedHashMap(
    "Picture" -> """Picture {
    ${c}forward(50)    
}""",
    "picRow(pics)" -> "picRow(${c}p, p)",
    "picCol(pics)" -> "picCol(${c}p, p)",
    "picStack(pics)" -> "picStack(${c}p, p)",
    "draw(pics)" -> "draw(${c}pic)",
    "" -> "",
    "PicShape.hline(len)" -> "PicShape.hline(${c}50)",
    "PicShape.vline(len)" -> "PicShape.vline(${c}50)",
    "PicShape.rect(h, w)" -> "PicShape.rect(${c}50, 100)",
    "PicShape.circle(r)" -> "PicShape.circle(${c}50)",
    "PicShape.text(s, n)" -> """PicShape.text(${c}"Hello", 18)"""
)

val ptTemplates = LinkedHashMap(
    "rot(a)" -> "rot(${c}45)",
    "scale(f)" -> "scale(${c}2)",
    "trans(x,y)" -> "trans(${c}10, 10)",
    "penColor(c)" -> "penColor(${c}blue)",
    "fillColor(c)" -> "fillColor(${c}blue)",
    "penWidth(w)" -> "penWidth(${c}4)",
    "hue(f)" -> "hue(${c}0.1)",
    "sat(f)" -> "sat(${c}0.1)",
    "brit(f)" -> "brit(${c}0.1)",
    "opac(f)" -> "opac(${c}0.1)",
    "flipX" -> "flipX",
    "flipY" -> "flipY",
    "axes" -> "axes"
)

val cTemplates = LinkedHashMap(
    "==   [equal to]" -> "${c}2 == 2",
    "!=   [not equal to]" -> "${c}1 != 2",
    ">    [greater than]" -> "${c}2 > 1",
    "<    [less than]" -> "${c}1 < 2",
    ">=   [greater/equal]" -> "${c}2 >= 1",
    "<=   [less/equal than]" -> "${c}1 <= 2"
)

val instructions = Map(
    "t" -> tTemplates.keys.toIndexedSeq,
    "cf" -> cfTemplates.keys.toIndexedSeq,
    "a" -> aTemplates.keys.toIndexedSeq,
    "p" -> pTemplates.keys.toIndexedSeq,
    "pt" -> ptTemplates.keys.toIndexedSeq,
    "c" -> cTemplates.keys.toIndexedSeq
)

val templates = Map(
    "t" -> tTemplates,
    "cf" -> cfTemplates,
    "a" -> aTemplates,
    "p" -> pTemplates,
    "pt" -> ptTemplates,
    "c" -> cTemplates
)

def runLink(category: String, n: Int) = s"http://runhandler/$category/$n"
def code(category: String, n: Int) =
    <div style={ codeStyle }> 
        <pre><code><a href={ runLink(category, n) } style={ codeLinkStyle }> { instructions(category)(n) }</a></code></pre>
    </div>

def pageFor(cat: String) = Page(
    name = cat,
    body =
        <body style={ pageStyle }>
        { navLinks }
        <div style={ titleStyle }><a style={ summaryLinkStyle } href={ "http://runhandler/%s/%s" format(Summary, cat) }>{ catName(cat) }</a></div>        
        { for (i <- 0 until instructions(cat).length) yield (if (instructions(cat)(i) == "") <br/> else code(cat, i)) }
        { footer }
        </body>,
    code = {
        stSetUserControlsBg(footerPanelColor)
        stAddUiComponent(footerPanel)
    }
)

val story = Story(
    pageFor(Turtle),
    pageFor(ControlFlow),
    pageFor(Abstraction),
    pageFor(Pictures),
    pageFor(PictureXforms),
    pageFor(Conditions)
)

switchToDefaultPerspective()
stClear()
stSetStorytellerWidth(50)

import javax.swing._
import java.awt.event._
@volatile var helpFrame: JWindow = _
@volatile var helpPane: JEditorPane = _
@volatile var footerPanel: JPanel = _
@volatile var helpOn = true

runInGuiThread {
    helpFrame = new JWindow(stFrame)
    helpFrame.setBounds(300, 100, 500, 300)
    helpPane = new JEditorPane
    helpPane.setBackground(Color(255, 255, 51))
    helpPane.setContentType("text/html")
    helpPane.setEditable(false)
    val helpScroller = new JScrollPane(helpPane)
    helpScroller.setBorder(BorderFactory.createLineBorder(gray, 1))
    helpFrame.getContentPane.add(helpScroller)
    helpPane.addFocusListener(new FocusAdapter {
        override def focusLost(e: FocusEvent) = schedule(0.3) {
            if (!helpPane.isFocusOwner) { // make Linux work
                helpFrame.setVisible(false)
            }
        }
    })

    footerPanel = new JPanel
    footerPanel.setBackground(footerPanelColor)
    val helpLabel = new JLabel("Live Help"); helpLabel.setForeground(color(0xfafafa))
    footerPanel.add(helpLabel)
    val onButton = new JRadioButton("On"); onButton.setForeground(color(0xfafafa))
    onButton.setSelected(true)
    val offButton = new JRadioButton("Off"); offButton.setForeground(color(0xfafafa))
    offButton.setSelected(false)
    val onOff = new ButtonGroup; onOff.add(onButton); onOff.add(offButton)
    footerPanel.add(onButton)
    footerPanel.add(offButton)
    onButton.addActionListener(new ActionListener {
        override def actionPerformed(e: ActionEvent) {
            helpOn = true
        }
    })
    offButton.addActionListener(new ActionListener {
        override def actionPerformed(e: ActionEvent) {
            helpOn = false
        }
    })
}

def insertCodeInline(cat: String, idx: Int) {
    stInsertCodeInline(templates(cat)(instructions(cat)(idx)))
    helpFrame.setVisible(false)
}
def insertCodeBlock(cat: String, idx: Int) {
    stInsertCodeBlock(templates(cat)(instructions(cat)(idx)))
    helpFrame.setVisible(false)
}

stAddLinkHandler(Turtle, story) { idx: Int => insertCodeBlock(Turtle, idx) }
stAddLinkHandler(ControlFlow, story) { idx: Int => insertCodeBlock(ControlFlow, idx) }
stAddLinkHandler(Pictures, story) { idx: Int => insertCodeBlock(Pictures, idx) }
stAddLinkHandler(PictureXforms, story) { idx: Int => insertCodeInline(PictureXforms, idx) }
stAddLinkHandler(Abstraction, story) { idx: Int => insertCodeBlock(Abstraction, idx) }
stAddLinkHandler(Conditions, story) { idx: Int => insertCodeInline(Conditions, idx) }

def keyFor(cat: String, n: Int) = {
    instructions(cat)(n).takeWhile(c => c != '(' && c != '-' && c != '[').trim
}

def showCatHelp(cat: String, idx: Int) {
    showHelp(keyFor(cat, idx))
}

def showCatSummary(cat: String) {
    showHelp(catName(cat) + "Palette")
}

def showHelp(key: String) {
    if (helpOn) {
        helpPane.setText(s"""<body style="$helpStyle">
        ${stHelpFor(key)}
        </body>
        """
        )
        helpPane.setCaretPosition(0)
        val cloc = stCanvasLocation
        helpFrame.setLocation(cloc.x + 5, cloc.y + 5)
        helpFrame.setVisible(true)
        // try to make sure that the help pane gains focus
        helpPane.requestFocus()
        helpPane.requestFocusInWindow()
    }
}

stAddLinkEnterHandler(Turtle, story) { idx: Int => showCatHelp(Turtle, idx) }
stAddLinkEnterHandler(ControlFlow, story) { idx: Int => showCatHelp(ControlFlow, idx) }
stAddLinkEnterHandler(Pictures, story) { idx: Int => showCatHelp(Pictures, idx) }
stAddLinkEnterHandler(PictureXforms, story) { idx: Int => showCatHelp(PictureXforms, idx) }
stAddLinkEnterHandler(Abstraction, story) { idx: Int => showCatHelp(Abstraction, idx) }
stAddLinkEnterHandler(Conditions, story) { idx: Int => showCatHelp(Conditions, idx) }

stAddLinkEnterHandler(Summary, story) { cat: String => showCatSummary(cat) }

stOnStoryStop(story) {
    helpFrame.setVisible(false)
    helpFrame.dispose()
    switchToDefaultPerspective()
}
stPlayStory(story)
// runInBackground { stSetScript("") }
