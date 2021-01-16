// Este es el script de Kojo que define la Paleta de Instrucciones
// Se ejecuta de manera silenciosa y se añade a la Historia (como ejemplo de un script potente) cuando cargas la Paleta

val pageStyle = "background-color:#93989c; margin:5px;font-size:small;"
val titleStyle = "font-size:95%;text-align:center;color:#1a1a1a;margin-top:5px;margin-bottom:3px;"
val headerStyle = "text-align:center;font-size:95%;color:#fafafa;font-weight:bold;"
val codeStyle = "background-color:#4a6cd4;margin-top:3px"
val linkStyle = "color:#fafafa"
val summaryLinkStyle = "color:#1a1a1a"
val codeLinkStyle = "text-decoration:none;font-size:x-small;color:#fafafa;"
val footerStyle = "font-size:90%;margin-top:15px;color:#1a1a1a;"
val helpStyle = "color:black;background-color:#ffffcc;margin:10px;"
val footerPanelColor = Color(0x93989c)

val Turtle = "t"
val Pictures = "p"
val PictureXforms = "pt"
val ControlFlow = "cf"
val Abstraction = "a"
val Conditions = "c"
val Summary = "s"

val catName = Map(
    Turtle -> "Tortuga",
    Pictures -> "Foto",
    PictureXforms -> "Transformaciones de Foto",
    ControlFlow -> "Flow",
    Abstraction -> "Abstracción",
    Conditions -> "Condición"
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
        Haz click en alguna instrucción para insertarla en el editor de scripts en la localización actual (si estás en una línea en blanco) o en la siguiente línea(en cualquier otro caso).<br/>
    </div>

import scala.collection.mutable.LinkedHashMap

val tTemplates = LinkedHashMap(
    "limpiar()              " -> "limpiar()",
    "adelante(num)           " -> "adelante(${c})",
    "salto(num)               " -> "salto(${c})",
    "derecha(a)             " -> "derecha(${c})",
    "izquierda(a)              " -> "izquierda(${c})",
    "establecerVelocidad(s)          " -> "establecerVelocidad(${c}medio)",
    "establecerColorPluma(c)       " -> "establecerColorPluma(${c}colorAleatorio)",
    "establecerColorRelleno(c)      " -> "establecerColorRelleno(${c}azul)",
    "establecerGrosorPluma(t)   " -> "establecerGrosorPluma(${c}4)",
    "establecerFondo(c)     " -> "establecerFondo(${c}negro)",
    "guardaPosHe()          " -> "guardaPosHe()",
    "restaurarPosHe()       " -> "restaurarPosHe()",
    "repite(n) {...}      " -> """repite(${c}4) {
}""",
    "def       [comando]  " -> """def ${c}nuevoComando() {
}"""
)

val cfTemplates = LinkedHashMap(
    "repite    [comando]  " -> """repite(${c}4) {
    adelante(50)
}""",
    "repetirDurante [comando]  " -> """repetirDurante(${c}1 to 5) { n =>
    imprimirln(n)
}""",
    "if        [comando]  " -> """if (${c}true) {
    establecerColorPluma(blue)
}""",
    "if-else   [comando]  " -> """if (${c}true) {
    establecerColorPluma(azul)
}
else {
    establecerColorPluma(verde)
}""",
    "if-else   [expr]     " -> """if (${c}verdad) 5 else 9""",
    "for       [comando]  " -> """for (i <- ${c}1 to 4) {
    imprimirln(i)
}""",
    "for       [expr]     " -> """for (${c}i <- 1 to 4) yield (2 * i)""",
    "recursión [comando]  " -> """def ${c}patrón(n: Int) {
    if (n <= 10) {
        adelante(n)
    }
    else {
        adelante(n)
        derecha(90)
        patrón(n-5)
    }
}""",
    "recursión [función] " -> """def ${c}factorial(n: Int): Int =
    if (n == 0) 1 else n * factorial(n-1)"""
)

val aTemplates = LinkedHashMap(
    "val       [expr]     " -> "val x = ${c}10",
    "def       [comando]  " -> """def ${c}newcmd(n: Int) {
    adelante(50)
}""",
    "def       [función] " -> """def ${c}max(n1: Int, n2: Int) =
        if (n1 > n2) n1 else n2"""
)

val pTemplates = LinkedHashMap(
    "Picture              " -> """Picture {
    ${c}adelante(50)
}""",
    "picRow(pics)             " -> "picRow(${c}p, p)",
    "picCol(pics)             " -> "picCol(${c}p, p)",
    "picStack(pics)           " -> "picStack(${c}p, p)",
    "draw(pics)               " -> "draw(${c}pic)",
    "" -> "",
    "Picture.line(w, h)       " -> "Picture.line(${c}50, 0)",
    "Picture.rectangle(w, h)  " -> "Picture.rectangle(${c}100, 50)",
    "Picture.circle(r)        " -> "Picture.circle(${c}50)",
    "Picture.ellipse(rx, ry)  " -> "Picture.ellipse(${c}50, 25)",
    "Picture.text(s, n)       " -> """Picture.text(${c}"Hello", 18)""",
    "Picture.image(f)         " -> "Picture.image(${c}Costume.womanWaving)",
    "Picture.widget(c)        " -> """Picture.widget(${c}Label("Hi there"))"""
)

val ptTemplates = LinkedHashMap(
    "rot(a)               " -> "rot(${c}45)",
    "scale(f)             " -> "scale(${c}2)",
    "trans(x,y)           " -> "trans(${c}10, 10)",
    "penColor(c)          " -> "penColor(${c}blue)",
    "fillColor(c)         " -> "fillColor(${c}blue)",
    "penWidth(w)          " -> "penWidth(${c}4)",
    "hue(f)               " -> "hue(${c}0.1)",
    "sat(f)               " -> "sat(${c}0.1)",
    "brit(f)              " -> "brit(${c}0.1)",
    "opac(f)              " -> "opac(${c}0.1)",
    "flipX                " -> "flipX",
    "flipY                " -> "flipY",
    "axes                 " -> "axes"
)

val cTemplates = LinkedHashMap(
    "==   [iagual a]        " -> "${c}2 == 2",
    "!=   [distinto de]    " -> "${c}1 != 2",
    ">    [mayor que]    " -> "${c}2 > 1",
    "<    [menor que]       " -> "${c}1 < 2",
    ">=   [mayor o igual a ]   " -> "${c}2 >= 1",
    "<=   [menor o igual a ] " -> "${c}1 <= 2"
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
val stWidth = {
    val l = new javax.swing.JLabel("")
    val te = textExtent("http://runhandler/cc/nn Pg n#n ", l.getFont.getSize, l.getFont.getName)
    math.max(50, te.width.toInt)
}
stSetStorytellerWidth(stWidth)

import javax.swing._
import java.awt.event._
@volatile var helpFrame: JWindow = _
@volatile var helpPane: JEditorPane = _
@volatile var footerPanel: JPanel = _
@volatile var helpOn = falso

def insertCodeInline(cat: String, idx: Int) {
    stInsertCodeInline(templates(cat)(instructions(cat)(idx)))
    helpFrame.setVisible(falso)
}
def insertCodeBlock(cat: String, idx: Int) {
    stInsertCodeBlock(templates(cat)(instructions(cat)(idx)))
    helpFrame.setVisible(falso)
}

stAddLinkHandler(Turtle, story) { idx: Int => insertCodeBlock(Turtle, idx) }
stAddLinkHandler(ControlFlow, story) { idx: Int => insertCodeBlock(ControlFlow, idx) }
stAddLinkHandler(Pictures, story) { idx: Int => insertCodeInline(Pictures, idx) }
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
        helpFrame.setVisible(verdad)
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
    helpFrame.setVisible(falso)
    helpFrame.dispose()
    switchToDefaultPerspective()
}
stPlayStory(story)

runInGuiThread {
    helpFrame = new JWindow(stFrame)
    helpFrame.setBounds(300, 100, 500, 300)
    helpPane = new JEditorPane
    helpPane.setBackground(Color(255, 255, 51))
    helpPane.setContentType("text/html")
    helpPane.setEditable(falso)
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
    val helpLabel = new JLabel("Ayuda"); helpLabel.setForeground(Color(0xfafafa))
    footerPanel.add(helpLabel)
    val onButton = new JRadioButton("On"); onButton.setForeground(Color(0xfafafa))
    onButton.setSelected(falso)
    val offButton = new JRadioButton("Off"); offButton.setForeground(Color(0xfafafa))
    offButton.setSelected(verdad)
    val onOff = new ButtonGroup; onOff.add(onButton); onOff.add(offButton)
    footerPanel.add(onButton)
    footerPanel.add(offButton)
    onButton.addActionListener(new ActionListener {
        override def actionPerformed(e: ActionEvent) {
            helpOn = verdad
        }
    })
    offButton.addActionListener(new ActionListener {
        override def actionPerformed(e: ActionEvent) {
            helpOn = falso
        }
    })
}
