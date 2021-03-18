// Bu Kojo yazılımcığı Komut Paletini tanımlıyor. 
// Araçlar menüsünden çağırdığımızda sessizce çalıştırılır ve (epey esaslı ve güçlü bir yazılım olarak) tarihçeye eklenir. 
// Ama doğrudan yükleyip kendin de çalıştırabilirsin.

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
    Turtle -> "Kaplumbağacık",
    Pictures -> "Resim",
    PictureXforms -> "Resim Değişimleri",
    ControlFlow -> "Akış",
    Abstraction -> "Soyutlama",
    Conditions -> "Koşullar"
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
        Mavi kutucuklara tıklayarak içindeki komutu yazılımcık düzenleyicisine taşıyabilirsin. İmleçin olduğu satır boşsa oraya, yoksa bir sonraki satıra yazılıverir.<br/>
    </div>

import scala.collection.mutable.LinkedHashMap

val tTemplates = LinkedHashMap(
    "sil()                " -> "sil()",
    "ileri(adım)          " -> "ileri(${c})",
    "zıpla(adım)          " -> "zıpla(${c})",
    "sağ(açı)             " -> "sağ(${c})",
    "sol(açı)             " -> "sol(${c})",
    "hızıKur(hız)         " -> "hızıKur(${c}orta)",
    "kalemRenginiKur(renk)" -> "kalemRenginiKur(${c}rastgeleRenk)",
    "boyamaRenginiKur(r)  " -> "boyamaRenginiKur(${c}mavi)",
    "kalemKalınlığınıKur(k)" -> "kalemKalınlığınıKur(${c}4)",
    "artalanıKur(renk)    " -> "artalanıKur(${c}siyah)",
    "konumVeYönüBelleğeYaz()" -> "konumVeYönüBelleğeYaz()",
    "konumVeYönüGeriYükle()" -> "konumVeYönüGeriYükle()",
    "yinele(s) {...}      " -> """yinele(${c}4) {
}""",
    "def       [komut]  " -> """def ${c}yeniKomut() {
}"""
)

val cfTemplates = LinkedHashMap(
    "yinele     [komut]  " -> """yinele(${c}4) {
    forward(50)
}""",
    "yineleİçin [komut]  " -> """yineleİçin(${c}1 to 5) { n =>
    satıryaz(n)
}""",
    "if         [komut]  " -> """if (${c}doğru) {
    setPenColor(mavi)
}""",
    "if-else    [komut]  " -> """if (${c}doğru) {
    setPenColor(mavi)
}
else {
    setPenColor(yeşil)
}""",
    "if-else    [deyiş]     " -> """if (${c}doğru) 5 else 9""",
    "for        [komut]  " -> """for (i <- ${c}1 to 4) {
    satıryaz(i)
}""",
    "for        [deyiş]     " -> """for (${c}i <- 1 to 4) yield (2 * i)""",
    "özyinele   [komut]  " -> """def ${c}desen(adım: Sayı) {
    if (adım <= 10) {
        ileri(adım)
    }
    else {
        ileri(adım)
        sağ(90)
        desen(adım - 5)
    }
}
sil; hızıKur(orta); desen(100); desen(100); görünmez
""",
    "özyinele   [işlev]" -> """def faktöryel(s: Sayı): Sayı =
    if (s == 0) 1 else s * faktöryel(s - 1)
satıryaz("f(5)=" + faktöryel(5))
val s = 10
satıryaz(s"f($s)=${faktöryel(s)}")"""
)

val aTemplates = LinkedHashMap(
    "val       [deyiş]     " -> "val x = ${c}10",
    "def       [komut]  " -> """def ${c}newcmd(n: Int) {
    forward(50)
}""",
    "def       [işlev] " -> """def ${c}max(n1: Int, n2: Int) =
        if (n1 > n2) n1 else n2"""
)

val pTemplates = LinkedHashMap(
    "Resim                  " -> """Resim {
    ${c}ileri(50)
}""",
    "resimDikeyDizi(resimler) " -> "resimDikeyDizi(${c}r, r)",
    "resimYatayDizi(resimler) " -> "resimYatayDizi(${c}r, r)",
    "resimDizisi(resimler)    " -> "resimDizisi(${c}r, r)",
    "çiz(resim)             " -> "çiz(${c}resim)",
    "" -> "",
    "Resim.köşegen(e, b)    " -> "Resim.köşegen(${c}50, 20)",
    "Resim.dikdörtgen(e, b) " -> "Resim.dikdörtgen(${c}100, 50)",
    "Resim.daire(yç)        " -> "Resim.daire(${c}50)",
    "Resim.elips(yçx, yçy)  " -> "Resim.elips(${c}50, 25)",
    "Resim.yazı(y, boyu)    " -> """Resim.yazı(${c}"Merhaba!", 18)""",
    "Resim.imge(f)          " -> "Resim.imge(${c}Costume.womanWaving)",
    "Resim.arayüz(jc)       " -> """Resim.arayüz(${c}Label("Selam!"))"""
)

val ptTemplates = LinkedHashMap(
    "döndür(açı)          " -> "döndür(${c}45)",
    "büyüt(oran)          " -> "büyüt(${c}2.5)",
    "götür(x,y)           " -> "götür(${c}10, 10)",
    "kalemRengi(renk)     " -> "kalemRengi(${c}mavi)",
    "boyaRengi(renk)      " -> "boyaRengi(${c}kırmızı)",
    "kalemBoyu(boy)       " -> "kalemBoyu(${c}20)",
    "ton(oran)            " -> "ton(${c}0.1)",
    "parlaklık(oran)      " -> "parlaklık(${c}0.1)",
    "aydınlık(oran)       " -> "aydınlık(${c}0.1)",
    "saydamlık(f)         " -> "saydamlık(${c}0.1)",
    "yansıtX              " -> "yansıtX",
    "yansıtY              " -> "yansıtY",
    "eksenler             " -> "eksenler"
)

val cTemplates = LinkedHashMap(
    "==   [eşittir]         " -> "${c}2 == 2",
    "!=   [eşit değil]      " -> "${c}1 != 2",
    ">    [büyüktür]        " -> "${c}2 > 1",
    "<    [küçüktür]        " -> "${c}1 < 2",
    ">=   [büyük ya da eşit]" -> "${c}2 >= 1",
    "<=   [küçük ya da eşit]" -> "${c}1 <= 2"
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
@volatile var helpOn = yanlış

def insertCodeInline(cat: String, idx: Int) {
    stInsertCodeInline(templates(cat)(instructions(cat)(idx)))
    helpFrame.setVisible(yanlış)
}
def insertCodeBlock(cat: String, idx: Int) {
    stInsertCodeBlock(templates(cat)(instructions(cat)(idx)))
    helpFrame.setVisible(yanlış)
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
        helpFrame.setVisible(doğru)
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
    helpFrame.setVisible(yanlış)
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
    helpPane.setEditable(yanlış)
    val helpScroller = new JScrollPane(helpPane)
    helpScroller.setBorder(BorderFactory.createLineBorder(gray, 1))
    helpFrame.getContentPane.add(helpScroller)
    helpPane.addFocusListener(new FocusAdapter {
        override def focusLost(e: FocusEvent) = schedule(0.3) {
            if (!helpPane.isFocusOwner) { // make Linux work
                helpFrame.setVisible(yanlış)
            }
        }
    })

    footerPanel = new JPanel
    footerPanel.setBackground(footerPanelColor)
    val helpLabel = new JLabel("Canlı Yardım"); helpLabel.setForeground(Color(0xfafafa))
    footerPanel.add(helpLabel)
    val onButton = new JRadioButton("Açık"); onButton.setForeground(Color(0xfafafa))
    onButton.setSelected(yanlış)
    val offButton = new JRadioButton("Kapalı"); offButton.setForeground(Color(0xfafafa))
    offButton.setSelected(doğru)
    val onOff = new ButtonGroup; onOff.add(onButton); onOff.add(offButton)
    footerPanel.add(onButton)
    footerPanel.add(offButton)
    onButton.addActionListener(new ActionListener {
        override def actionPerformed(e: ActionEvent) {
            helpOn = doğru
        }
    })
    offButton.addActionListener(new ActionListener {
        override def actionPerformed(e: ActionEvent) {
            helpOn = yanlış
        }
    })
}
