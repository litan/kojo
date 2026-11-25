// Bu Kojo yazılımcığı Komut Paletini tanımlıyor. 
// Araçlar menüsünden çağırdığımızda sessizce çalıştırılır ve (epey esaslı ve güçlü bir yazılım olarak) tarihçeye eklenir. 
// Ama doğrudan yükleyip kendin de çalıştırabilirsin.

dez pageStyle = "background-color:#93989c; margin:5px;font-size:small;"
dez titleStyle = "font-size:95%;text-align:center;color:#1a1a1a;margin-top:5px;margin-bottom:3px;"
dez headerStyle = "text-align:center;font-size:95%;color:#fafafa;font-weight:bold;"
dez codeStyle = "background-color:#4a6cd4;margin-top:3px"
dez linkStyle = "color:#fafafa"
dez summaryLinkStyle = "color:#1a1a1a"
dez codeLinkStyle = "text-decoration:none;font-size:x-small;color:#fafafa;"
dez footerStyle = "font-size:90%;margin-top:15px;color:#1a1a1a;"
dez helpStyle = "color:black;background-color:#ffffcc;margin:10px;"
dez footerPanelColor = Color(0x93989c)

dez Turtle = "t"
dez Pictures = "p"
dez PictureXforms = "pt"
dez ControlFlow = "cf"
dez Abstraction = "a"
dez Conditions = "c"
dez Summary = "s"

dez catName = Map(
    Turtle -> "Kaplumbağacık",
    Pictures -> "Resim",
    PictureXforms -> "Resim Değişimleri",
    ControlFlow -> "Akış",
    Abstraction -> "Soyutlama",
    Conditions -> "Koşullar"
)
tanım navLinks =
    <div style={ headerStyle }>
        <a style={ linkStyle } href={ "http://localpage/" + Turtle }>{ catName(Turtle) }</a> | <a style={ linkStyle } href={ "http://localpage/" + Pictures }>{ catName(Pictures) }</a> <br/>
        <a style={ linkStyle } href={ "http://localpage/" + PictureXforms }>{ catName(PictureXforms) }</a> <br/>
        <a style={ linkStyle } href={ "http://localpage/" + ControlFlow }>{ catName(ControlFlow) }</a> | <a style={ linkStyle } href={ "http://localpage/" + Conditions }>{ catName(Conditions) }</a> <br/>
        <a style={ linkStyle } href={ "http://localpage/" + Abstraction }>{ catName(Abstraction) }</a> <br/>
        <hr/>
    </div>

tanım footer =
    <div style={ footerStyle }>
        Mavi kutucuklara tıklayarak içindeki komutu yazılımcık düzenleyicisine taşıyabilirsin. İmleçin olduğu satır boşsa oraya, yoksa bir sonraki satıra yazılıverir.<br/>
    </div>

getir scala.collection.mutable.LinkedHashMap

dez tTemplates = LinkedHashMap(
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
    "tanım       [komut]  " -> """tanım ${c}yeniKomut() {
}"""
)

dez cfTemplates = LinkedHashMap(
    "yinele     [komut]  " -> """yinele(${c}4) {
    ileri(50)
    sağ(90)
}""",
    "yineleİçin [komut]  " -> """yineleİçin(${c}1 |-| 5) { n =>
    satıryaz(n)
}""",
    "eğer       [komut]  " -> """eğer (${c}doğru) {
    boyamaRenginiKur(mavi)
}""",
    "eğer-yoksa [komut]  " -> """eğer (${c}doğru) {
    boyamaRenginiKur(mavi)
}
yoksa {
    boyamaRenginiKur(yeşil)
}""",
    "eğer-yoksa [deyiş]     " -> """eğer (${c}doğru) 5 yoksa 9""",
    "için       [komut]  " -> """için (i <- ${c}1 |-| 4) {
    satıryaz(i)
}""",
    "için       [deyiş]     " -> """için (${c}i <- 1 |-| 4) ver (2 * i)""",
    "özyinele   [komut]  " -> """tanım ${c}desen(adım: Sayı) {
    eğer (adım <= 10) {
        ileri(adım)
    }
    yoksa {
        ileri(adım)
        sağ(90)
        desen(adım - 5)
    }
}
sil; hızıKur(orta); desen(100); desen(100); görünmez
""",
    "özyinele   [işlev]" -> """tanım faktöryel(s: Sayı): Sayı =
    eğer (s == 0) 1 yoksa s * faktöryel(s - 1)
satıryaz("f(5)=" + faktöryel(5))
dez s = 10
satıryaz(s"f($s)=${faktöryel(s)}")"""
)

dez aTemplates = LinkedHashMap(
    "dez       [deyiş] " -> "dez x = ${c}10",
    "tanım     [komut] " -> """tanım ${c}yeniKomut(adım: Sayı) {
    ileri(adım)
}""",
    "tanım     [işlev] " -> """tanım ${c}irisi(sayı1: Sayı, sayı2: Sayı) =
        eğer (sayı1 > sayı2) sayı1 yoksa sayı2"""
)

dez pTemplates = LinkedHashMap(
    "Resim                  " -> """Resim {
    ${c}ileri(50)
}""",
    "Resim.dizi(resimler)   " -> "Resim.dizi(${c}r, r)",
    "Resim.diziDikey(resimler) " -> "Resim.diziDikey(${c}r, r)",
    "Resim.diziYatay(resimler) " -> "Resim.diziYatay(${c}r, r)",
    "çiz(resim)             " -> "çiz(${c}resim)",
    "" -> "",
    "Resim.köşegen(e, b)    " -> "Resim.köşegen(${c}50, 20)",
    "Resim.dikdörtgen(e, b) " -> "Resim.dikdörtgen(${c}100, 50)",
    "Resim.daire(yç)        " -> "Resim.daire(${c}50)",
    "Resim.elips(yçx, yçy)  " -> "Resim.elips(${c}50, 25)",
    "Resim.yazı(y, boyu)    " -> """Resim.yazı(${c}"Merhaba!", 18)""",
    "Resim.imge(f)          " -> "Resim.imge(${c}Çizim.elSallayanKadın)",
    "Resim.arayüz(jc1)      " -> """Resim.arayüz(${c}ay.Tanıt("Selam!"))""",
    "Resim.arayüz(jc2)      " -> """Resim.arayüz(${c}ay.Düğme("Selam!")(satıryaz("Nasılsın?")))"""
)

dez ptTemplates = LinkedHashMap(
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

dez cTemplates = LinkedHashMap(
    "==   [eşittir]         " -> "${c}2 == 2",
    "!=   [eşit değil]      " -> "${c}1 != 2",
    ">    [büyüktür]        " -> "${c}2 > 1",
    "<    [küçüktür]        " -> "${c}1 < 2",
    ">=   [büyük ya da eşit]" -> "${c}2 >= 1",
    "<=   [küçük ya da eşit]" -> "${c}1 <= 2"
)

dez instructions = Map(
    "t" -> tTemplates.keys.toIndexedSeq,
    "cf" -> cfTemplates.keys.toIndexedSeq,
    "a" -> aTemplates.keys.toIndexedSeq,
    "p" -> pTemplates.keys.toIndexedSeq,
    "pt" -> ptTemplates.keys.toIndexedSeq,
    "c" -> cTemplates.keys.toIndexedSeq
)

dez templates = Map(
    "t" -> tTemplates,
    "cf" -> cfTemplates,
    "a" -> aTemplates,
    "p" -> pTemplates,
    "pt" -> ptTemplates,
    "c" -> cTemplates
)

tanım runLink(category: String, n: Int) = s"http://runhandler/$category/$n"
tanım code(category: String, n: Int) =
    <div style={ codeStyle }>
        <pre><code><a href={ runLink(category, n) } style={ codeLinkStyle }> { instructions(category)(n) }</a></code></pre>
    </div>

tanım pageFor(cat: String) = Page(
    name = cat,
    body =
        <body style={ pageStyle }>
        { navLinks }
        <div style={ titleStyle }><a style={ summaryLinkStyle } href={ "http://runhandler/%s/%s" format(Summary, cat) }>{ catName(cat) }</a></div>
        { için (i <- 0 until instructions(cat).length) ver (eğer (instructions(cat)(i) == "") <br/> yoksa code(cat, i)) }
        { footer }
        </body>,
    code = {
        stSetUserControlsBg(footerPanelColor)
        stAddUiComponent(footerPanel)
    }
)

dez story = Story(
    pageFor(Turtle),
    pageFor(ControlFlow),
    pageFor(Abstraction),
    pageFor(Pictures),
    pageFor(PictureXforms),
    pageFor(Conditions)
)

switchToDefaultPerspective()
stClear()
dez stWidth = {
    dez l = yeni javax.swing.JLabel("")
    dez te = textExtent("http://runhandler/cc/nn Pg n#n ", l.getFont.getSize, l.getFont.getName)
    math.max(50, te.width.toInt)
}
stSetStorytellerWidth(stWidth)

getir javax.swing._
getir java.awt.event._
@volatile den helpFrame: JWindow = _
@volatile den helpPane: JEditorPane = _
@volatile den footerPanel: JPanel = _
@volatile den helpOn = yanlış

tanım insertCodeInline(cat: String, idx: Int) {
    stInsertCodeInline(templates(cat)(instructions(cat)(idx)))
    helpFrame.setVisible(yanlış)
}
tanım insertCodeBlock(cat: String, idx: Int) {
    stInsertCodeBlock(templates(cat)(instructions(cat)(idx)))
    helpFrame.setVisible(yanlış)
}

stAddLinkHandler(Turtle, story) { idx: Int => insertCodeBlock(Turtle, idx) }
stAddLinkHandler(ControlFlow, story) { idx: Int => insertCodeBlock(ControlFlow, idx) }
stAddLinkHandler(Pictures, story) { idx: Int => insertCodeInline(Pictures, idx) }
stAddLinkHandler(PictureXforms, story) { idx: Int => insertCodeInline(PictureXforms, idx) }
stAddLinkHandler(Abstraction, story) { idx: Int => insertCodeBlock(Abstraction, idx) }
stAddLinkHandler(Conditions, story) { idx: Int => insertCodeInline(Conditions, idx) }

tanım keyFor(cat: String, n: Int) = {
    instructions(cat)(n).takeWhile(c => c != '(' && c != '-' && c != '[').trim
}

tanım showCatHelp(cat: String, idx: Int) {
    showHelp(keyFor(cat, idx))
}

tanım showCatSummary(cat: String) {
    showHelp(catName(cat) + "Palette")
}

tanım showHelp(key: String) {
    eğer (helpOn) {
        helpPane.setText(s"""<body style="$helpStyle">
        ${stHelpFor(key)}
        </body>
        """
        )
        helpPane.setCaretPosition(0)
        dez cloc = stCanvasLocation
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
    helpFrame = yeni JWindow(stFrame)
    helpFrame.setBounds(300, 100, 500, 300)
    helpPane = yeni JEditorPane
    helpPane.setBackground(Color(255, 255, 51))
    helpPane.setContentType("text/html")
    helpPane.setEditable(yanlış)
    dez helpScroller = yeni JScrollPane(helpPane)
    helpScroller.setBorder(BorderFactory.createLineBorder(gray, 1))
    helpFrame.getContentPane.add(helpScroller)
    helpPane.addFocusListener(yeni FocusAdapter {
        baskın tanım focusLost(e: FocusEvent) = schedule(0.3) {
            eğer (!helpPane.isFocusOwner) { // make Linux work
                helpFrame.setVisible(yanlış)
            }
        }
    })

    footerPanel = yeni JPanel
    footerPanel.setBackground(footerPanelColor)
    dez helpLabel = yeni JLabel("Canlı Yardım"); helpLabel.setForeground(Color(0xfafafa))
    footerPanel.add(helpLabel)
    dez onButton = yeni JRadioButton("Açık"); onButton.setForeground(Color(0xfafafa))
    onButton.setSelected(yanlış)
    dez offButton = yeni JRadioButton("Kapalı"); offButton.setForeground(Color(0xfafafa))
    offButton.setSelected(doğru)
    dez onOff = yeni ButtonGroup; onOff.add(onButton); onOff.add(offButton)
    footerPanel.add(onButton)
    footerPanel.add(offButton)
    onButton.addActionListener(yeni ActionListener {
        baskın tanım actionPerformed(e: ActionEvent) {
            helpOn = doğru
        }
    })
    offButton.addActionListener(yeni ActionListener {
        baskın tanım actionPerformed(e: ActionEvent) {
            helpOn = yanlış
        }
    })
}
