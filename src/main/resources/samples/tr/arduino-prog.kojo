// Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
// The contents of this file are subject to
// the GNU General Public License Version 3 (http://www.gnu.org/copyleft/gpl.html)

val pageStyle = "color:black;background-color:#99CCFF; margin:15px;font-size:small;"
val centerStyle = "text-align:center;"
val headerStyle = "text-align:center;font-size:110%;color:maroon;"
val codeStyle = "font-size:90%;"
val smallNoteStyle = "color:dark-gray;font-size:95%;"
val sublistStyle = "margin-left:60px;"

def pgHeader(hdr: String) =
    <p style={ headerStyle }>
        { hdr }
        <hr/>
        <br/> 
    </p>

val scripts = Yöney(
    net.kogics.kojo.util.Utils.loadResource("/ka-bridge/ka_bridge/ka_bridge.ino"),
    net.kogics.kojo.util.Utils.loadResource("/ka-bridge/ka-bridge.kojo"),
    net.kogics.kojo.util.Utils.loadResource("/ka-bridge/sample.kojo")
)

def runLink(n: Int) = "http://runhandler/script/" + n
def code(n: Int) = {
    <div style="background-color:CCFFFF;margin-top:10px"> 
        <hr/>
        <pre><code><a href={ runLink(n) } style="text-decoration: none;font-size:x-small;">
{ scripts(n) }</a></code></pre>
        <hr/>
    </div>
}

var pages = Yöney.boş[StoryPage]
var pg: StoryPage = _

def satırarası(tane: Sayı) = for (i <- 1 to tane) yield { <br/> }
pages :+= Page(
    name = "intro",
    body =
        <body style={ pageStyle }>
            { pgHeader("Kojo ile Arduino Programlama Yapalım!") }
          Arduino anakartından haberin oldu mu? Pazarda bulması zor ya da pahalı elektronik ve akıllı cihazları evde yapmak artık çok kolay! Hatta kendin icat ederek yepyeni cihazlar yapabilmek çok güzel olurdu diyorsan, okumaya devam! İşe şu 15 dakikalık TED konuşmasını izleyerek başlayabilirsin: { satırarası(2) }

          <a href="http://www.ted.com/talks/massimo_banzi_how_arduino_is_open_sourcing_imagination?language=tr"> Arduino Nasıl Açık Kaynaklı Hayal Gücü Oldu</a> { satırarası(2) }

          Arduino'nu programlaman ve cihazına beceriler katman Kojo'yla daha da kolay. Bunun için Kojo ile Arduino kartın arasında bir köprü oluşturacağız. Bu köprünün iki parçası var:
            <ul>
                <li> Arduino kartında çalışan, <tt>ka_bridge.ino</tt> adında bir program, </li>
                <li> Kojo yazılımcıklarının içine ekleyeceğin, <tt>ka-bridge.kojo</tt> adında bir dosya.</li>
            </ul>
            Bu iki parça birbirleriyle iletişim halinde olacak. Ve bu sayede Kojo yazılımcığının içinde Arduino kartının yapmasını istediğin şeyleri tanımlayabileceksin.<br/><br/> 
            
            Haydi Kojo-Arduino köprüsünü kuralım:
            <ol>
                <li>Önce <a href="http://localpage/bridge-arduino"> <tt>ka_bridge.ino</tt></a> programını kartına yükle.</li>
                <li>Şu yazılımı <a href="http://localpage/bridge-kojo"> <tt>ka-bridge.kojo</tt></a> bilgisayarında bir dizin (yani klasör) içine yazdırıp sonra da Arduino'nu programlamak için yazdığın Kojo yazılımcığının içine ekle.</li>
            </ol>

            Kojo-Arduino köprümüz hazır! Artık küçük bir <a href="http://localpage/bridge-sample">örnekle</a> Arduino'nu programlamaya başlayabilirsin!
        </body>,
    code = {}
)

pages :+= Page(
    name = "bridge-arduino",
    body =
        <body style={ pageStyle }>
            { pgHeader("ka_bridge.ino programı Arduino kartına nasıl yüklenir?") }
            Arduino kartına <tt>ka_bridge.ino</tt> programını yüklemek için şunları yap:
            <ol>
                <li>Önce <a href="http://arduino.cc/en/Guide/Environment">Arduino Programlama Ortamı </a>'nı (IDE) çalıştır ve Arduino kartını bilgisayarına bağla.</li>
                <li>Aşağıdaki yazılımcığa tıklayarak onu yazılımcık düzenleyicisine taşı.</li>
                <li>Hepsini seç (<em>Ctrl+A</em>), kopyala (<em>Ctrl+C</em>), ve sonra da Arduino IDE'sine yapıştır (<em>Ctrl+V</em>).</li>
                <li>Arduino IDE'sindeki Upload yani yükle düğmesine basarak Kojo köprüsünü Arduino kartında çalıştırmaya başla.</li>
            </ol>
            <br/> 
            <tt>ka_bridge.ino</tt>:
            { code(0) }
        </body>,
    code = {}
)

pages :+= Page(
    name = "bridge-kojo",
    body =
        <body style={ pageStyle }>
            { pgHeader("ka-bridge.kojo yazılımını belleğe yazalım") }
            Daha sonra yazacağınız yazılımcıklarda kullanabilmen için <tt>ka-bridge.kojo</tt> yazılımını bilgisayarının belleğine yazmanda fayda var:
            <ol>
                <li>Aşağıdaki yazılımcığa tıklayarak onu yazılımcık düzenleyicisine taşı.</li>
                <li>Yazılımcık düzenleyicisindeki yazılımcığı dosya olarak belleğe kaydetmek için <em>'Dosya -> Yeni Adla Kaydet'</em> komudunu çalıştır.</li>
                <li>Ev dizinine ({ homeDir }) git ve yeni bir dizin aç. Adını <tt>kojo-includes</tt> yani Kojo için ek dosyalar adını koy.</li>
                <li>Sonra bu <tt>kojo-includes</tt> dizinine gir, dosyaya <em>ka-bridge.kojo</em> adını ver ve kaydet.</li>
            </ol>
            <br/> 
            <tt>ka-bridge.kojo</tt>:
            { code(1) }
        </body>,
    code = {}
)

pages :+= Page(
    name = "bridge-sample",
    body =
        <body style={ pageStyle }>
            { pgHeader("Kojo Arduino köprüsünü kullanalım") }
            Köprümüz kurulunca Kojo'yla Arduino yazılımları yazabiliriz.
            İşte bir örnek:
            { code(2) }
        </body>,
    code = {}
)

val story = Story(pages: _*)
stClear()
stAddLinkHandler("script", story) { idx: Int =>
    stSetScript(scripts(idx))
}
stPlayStory(story)
