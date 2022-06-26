/*
 * Copyright (C) 2009 Anthony Bagwell
 * Copyright (C) 2009 Phil Bagwell <pbagwell@bluewin.ch>
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
 * Copyright (C) 2021 Bülent Başaran <ben@scala.org>
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

// Click the Run button in the tool-bar above to start the story
//
// =================================================================
//
// This Tutorial originally created by Anthony Bagwell for simplyscala.com,
// then extended and adapted for Kojo by Phil Bagwell.
// Some of the Kojo examples provided by Lalit Pant.
// Turkish translation is lovingly provided by Bülent Başaran.
// Please note that simplyscala.com is no more. It can be revisited at the 
// wonderful web archive:
//    http://web.archive.org/web/20130305041026/http://www.simplyscala.com

// Set Up Styles for tutorial pages
val pageStyle = "color:black;background-color:#99CCFF; margin:15px;font-size:small;"
val centerStyle = "text-align:center;"
val headerStyle = "text-align:center;font-size:110%;color:maroon;"
val codeStyle = "font-size:90%;"
val smallNoteStyle = "color:gray;font-size:95%;"
val sublistStyle = "margin-left:60px;"

import language.implicitConversions
import language.postfixOps

showVerboseOutput()
retainSingleLineCode()
def pgHeader(hdr: String) =
    <p style={headerStyle}>
        {new xml.Unparsed(hdr)}
        {nav}
        <hr/>
        
    </p>
 
def tCode(cd:String)=Para(
    <div style="background-color:CCFFFF;"> <pre style={codeStyle}> {cd} </pre> </div>,
    code = {clearOutput;stRunCode(cd);stSetScript(cd)}
) 

var pages = new collection.mutable.ListBuffer[StoryPage]
var pg: StoryPage = _
var header: xml.Node = _

def link(page: String) = "http://localpage/%s" format(page)
val homeLink = <div style={smallNoteStyle+centerStyle}><a href={link("home#7")}>Start Page</a></div>

def nav={<div style={smallNoteStyle}>
        <a href={link(("Menu").toString)}>Menü</a>
         </div>}

// Mark up support

implicit def toSHtm(s:String):SHtm={new SHtm(escTrx(s))}
def escTrx(s:String) = s.replace("&" , "&amp;").replace(">" , "&gt;").replace("<" , "&lt;")
def escRem(s:String) = s.replace("&amp;","&"  ).replace("&gt;",">").replace("&lt;","<")
def row(c:SHtm *)={
    val r=c.map(x=>{new SHtm("<td>" + x.s + "</td>")}).reduce(_ + _) 
    new SHtm("<tr>" + r.s + "</tr>")
}
def table(c:SHtm *)={
    val r=c.reduce(_ + _) 
    new SHtm("""<table border="1">""" + r.s + "</table>")
}
//def toHtm(h:SHtm *)={h.reduce(_ + _)}

def tPage(title:String,h:SHtm *)={
    <body style={pageStyle}>
    <p style={headerStyle}>
        {new xml.Unparsed(title)}
        {nav}
        <hr/>
        {new xml.Unparsed(h.reduce(_ + _).s)}
    
    </p>
    </body>    
    
}
val codeExamples = new Array[String](1000) // no need to translate this Array
var codeID = 0 
// Mark up DSL definitions class

class SHtm (var s:String){
    def h2=new SHtm("<h2>" + s + "</h2>")    
    def h3=new SHtm("<h3>" + s + "</h3>")
    def h4=new SHtm("<h4>" + s + "</h4>")
    def i=new SHtm("<i>" + s + "</i>")    
    def h1=new SHtm("<h1>" + s + "</h1>")
    def p=new SHtm("<p>" + s + "</p>")
    def b=new SHtm("<b>" + s + "</b>")
    def link(url:String)=new SHtm("<a href=\"http://" + url + " \">" + s + "</a></br>")
    def c={
    codeID+=1
    codeExamples(codeID)=escRem(s)
    new SHtm("""<hr/><div style=background-color:CCFFFF;"> <pre><code><a href="http://runhandler/example/""" + 
                 codeID.toString +
                 """ " style="text-decoration: none;font-size:x-small;">""" + s + """</a></code></pre><hr/></div>""")
    }
    def + (a:SHtm)={new SHtm(s + "\n" + a.s) }
}


// **********  Start of Tutorial  *************
pages += Page(
  name = "Menu",
  body = <body style={pageStyle}>
    <div style={pageStyle+centerStyle}> <h1>Scala'ya Hızlı Giriş</h1> </div>
    <div style={pageStyle}>
    <p>Scala diline hoşgeldin! Skala diye oku, olur mu? Kılavuzcukta ileri veya geri gitmek için en aşağıdaki üçgenlere tıkla. Ya da istediğin bölüme atlamak için aşağıdaki başlıklardan birine tıkla. </p> <br/>
    <a href={link("GS")}>Başlayalım</a> <br/>
    <a href={link("Flow")}>Program Akışı If, Else ve While Komutları</a> <br/>
    <a href={link("Literals")}>Yalın Değerler, Sayılar, Kesirler ve Yazılar</a> <br/>
    <a href={link("Functions")}>İşlevler</a> <br/>
    <a href={link("OandC")}>Nesneler ve Sınıflar</a> <br/>
    <a href={link("PMS")}>Örüntü Eşleme ve Switch ve Case Komutları</a> <br/>
    <a href={link("BTree")}>İleri Eşleme Yöntemleri - İkil Ağaç</a> <br/>
    <a href={link("STI")}>Dingin Türleme ve Tür Çıkarımı</a> <br/>
    <a href={link("FAO")}>İşlevler de Birer Nesnedir</a> <br/>
    <a href={link("Tup")}>Sıralamalar (Tuple)</a> <br/>
    <a href={link("MF")}>Matematiksel İşlevler</a> <br/>
    <a href={link("OPA")}>İşlem Önceliği ve Birleşmeliği</a> <br/>
    <a href={link("US")}>Yazıların (String) Kullanılışı</a> <br/>
    <a href={link("UL")}>Dizinlerin (List) Kullanılışı</a> <br/>
    <a href={link("UT")}>Kaplumbağacığın Kullanılışı</a> <br/>
    <a href={link("GAG")}>Çizim ve Oyun</a> <br/>
    <a href={link("LM")}>Daha Çok Öğrenelim</a> <br/>
    <br/>
    <p>Not: Bu kılavuzcuk Anthony Bagwell'in simplyscala.com sitesinden Kojo'ya uyarlanarak yazılmıştır. O site ne yazık ki artık çalışmıyor. Ama arşivde bulabilirsin: <a href="http://web.archive.org/web/20130305041026/http://www.simplyscala.com">web.archive.org'dan simplyscala.com</a>.</p>
    </div>
    </body>
)

pages += Page(
  name = "GS",
  body = tPage("Başlayalım",
    "Başlayalım".h2,
    "Kılavuzcuğumuzda pek çok yazılımcık örneği bulacaksın. Onları buradan kolaylıkla çalıştırabilirsin. Herhangi birinin üstüne tıkladığında o örnek, yazılımcık düzenleyicisine taşınır ve çalışır. Yani hepsini yazmana gerek yok. Hatta yazılımcığı çalıştırma düğmesine bile tıklamaya gerek kalmıyor. Ayrıca, işin bir güzel yanı da şu: istediğin değişiklileri orada yapıp tekrar çalıştırabilirsin. Bunun için düzenleyicinin hemen üzerindeki yeşil üçgene tıklaman yeter. Haydi şimdi bunu deneyelim. Aşağıdaki mesajı Bülent yerine kendi adını yazıp tekrar çalıştırıver. Bu arada benim adım Bülent. Bu satırları siz Türkçe severler için severek çevirdim.".p,
    """satıryaz("merhaba dünya! kaplumbağacık ve Bülent'ten hepinize selamlar, sevgiler")""".c,
    "Yaptığın değişikliğin sonucunu Çıktı Gözünde ya da Çizim Tuvalinde hemen göreceksin.".p,
    "Bu kılavuzcuk ilk sayfada gördüğün bölümlerden oluşuyor. Bir sonraki bölüme geçmek için, ya da bir önceki bölüme dönmek için bu pencerenin altındaki sağa ve sola bakan mavi daire içindeki beyaz üçgenlere tıkla. İlgini çeken bölüme de kolaylıkla atlayabilirsin istersen. Bunun için ilk sayfaya geri dönüp ordaki mavi ve altı çizili başlıklardan herhangi birine tıklayabilirsin. İlk sayfaya dönmek için en üstte solda menü yazıyor ya, ona tıklayıver. En alttaki mavi daire içindeki kareye basarsan bu kılavuzcuktan çıkıp Kojo'nun normal düzenine dönebilirsin. Çıkınca kılavuzcukta son seçtiğin yazılımcık kapanmaz. Onun üzerinde değişikler yapıp çalıştırmaya odaklanabilirsin.".p,
    "Daha önce çalışan bir yazılıma dönmek de kolay: yazılım düzenleyicisinin üst kısmındaki menünün ortasında sağa ve sola bakan mavi oklara tıkla. Seçtiğin yazılımı düzenlemeye kaldığın yerden devam edebilirsin. Her örneği deneyebilir, istediğine geri dönüp değişiklikler yapıp tekrar çalıştırabilirsin. Değişik fikirler dener, programlama dilini daha iyi tanıyıp yazım, yazılım gramer (syntax) kurallarını daha çabuk öğrenebilirsin. Yazılımcık düzenleyicisindeki programları diske kaydedebilir ve sonra oradan geri yükleyebilirsin. Bunun için Dosya menüsüdeki komutları kullan.".p,
    "Şu anda kullandığın Kojo öğrenim ortamı, çok gelişmiş ve uzman bilgisayar mühendislerinin en sevdikleri dillerin önde gelenlerinden Scala programlama dilini öğrenmene yardımcı olmak için hem çok faydalı hem de eğlendirici özellikler ve beceriler içeriyor. Müzik çalan programlar yazabilir, ileri matematik kavramlarını resimler ve grafikler çizerek inceleyip daha iyi öğrenir, değişik tür oyunları hem oynayabilir hem de nasıl yazıldıklarını kolayca öğrenebilir, hatta fizik deneyleri bile yapabilirsin! En güzeli Kojo'nun kaplumbağaları var! Onlara yol göstererek çizimler yaptırabilirsin. Küçük hemen anlayıp seveceğin tek kaplumbağalı bir örnekle başlayalım mı? Kaplumbağa yürüsün ve bir taraftan da bir üçgen çizsin istersen, bu yazılımcığa tıklaman yeter:".p,
    """sil
ileri(100)
sağ(120)
ileri(100)
sağ(120)
ileri(100)
sağ(120)
""".c,
    "Bu kılavuzcuğun kaplumbağacığın kullanılışı başlıklı (sondan üçüncü) bölümünde daha pek çok örnek yazılımcık ve kaplumbağanın anladığı komutların bir listesini bulabilirsin.".p,
    "Scala dili tam anlamıyla genel ve güçlü bir bilgisayar programla dilidir. Özelliklerinin çoğunluğu uzman programcılara ve bilgisayar mühendislerine tanıdık gelecektir. Bu kılavuzcuktan faydalanmak için programlama dili bilmesen de olur, ama elbette daha önce başka bir programlama diliyle deneyimin olduysa daha hızlı ilerleyebilirsin. Onun için anladığın yerleri ve bölümleri hızlı geçmen hatta atlaman bile doğal olur.".p,
    "Öyleyse, başlayalım mı artık?".p,
    "Deyişler".h2,
    "Basit matematiksel deyişler pek şaşırtıcı gelmeyecektir. Tanıdık matematik işlemleri ve öncelikleri Scala'da da geçerlidir. İşlemleri daha açık açık sıralamak için parantez kullanılır.".p,
    "1+2".c,
    "3+4*(2-3)".c,
    """23%5  // Tabanlı aritmetik işlemi yani tabana bölünce ne kalıyorsa onu verir""".c,
    "6/4".c,
    "Bu son işlemde önemli bir nokta var. Tam sayılar bölününce sonuç yine bir tam sayı olur ve kalan dikkate alınmaz. Ufak bir değişiklikle kesirli ve daha doğru bir işlem yapabiliriz:".p,
    "6/4.0".c,
    "Biraz daha uzun bir işlem yapalım:".p,
    "3.5*9.4+7/5".c,
    """Scala tam ve kesirli gibi değişik bir kaç sayı tipini tanır. Hepsinin bir tür adı var. Örneğin Sayı, Uzun, İriSayı, Kesir, vb. Bunları ileride detaylı olarak inceleyeceğiz. Yukarıdaki iki örnekten 4.0 ve 3.5 kesir (İngilizcesi Double), 4 ve 6 ise birer tam sayıdır (İngilizcede Integer, kısaca Int). Eğer bir matematiksel deyiş bir kaç tür sayı içiriyorsa Scala derleyicisi mümkünse sayının türünü biraz zorlayarak değiştirebilir.""".p,
    """Bir deyişin sonucunu bir değişken (aslında çoğunlukla bir değişmez değer) kullanarak kaydedip daha sonra yine kullanabiliriz. Değişken ve değişmez değer isimleri (ve sonra göreceğimiz başka tür isimler) harf, sayı ve * / + - : = ! < > & ^ | gibi semboller kullanarak yazılır. Örneğin, "FutbolTopu", "BilardoTopuBeyaz1", "yardımHattı", "*+" and "res4" (result yani sonuç)...""".p,
    """Bunun için iki yöntem vardır: "var" ve "val" komut sözcükleri. "val" (İngilizce value sözcüğünün kısaltması) ile sabit ve hiç değişmeyecek değerleri ve sonuçları saklayabiliriz. Bunlara değişmez değer, ya da kısaca değişmez diyelim. Ya neden bir de "var" (ingilizce variable sözcüğünün kısaltması) komutu var (var var ama var'ı kullanma! 8-)? Aşağıda bir örnekle ikisinin farkını hemen anlayacağız. "val" ile tanımlanan değerlerin sabit olması (ingilizcede 'immutable value') aslında çok önemli bir işlevsel programlama (functional programming) kavramıdır, ama bunu daha sonra yeri gelince daha iyi anlayacağız. Şimdilik mümkün oldukça 'var' yerine 'val' komutunu kullanmaya dikkat edelim. Bu sayede programın başka bir yerindeki değişkenleri yanlışlıkla bozamayız.""".p,
    "val noktaSayısı=34+5".c,
    """Bir ya da daha fazla sayıda işlemin sonucunu çıktı gözüne 'satıryaz(deyiş1, deyiş2, deyişn)' komutunu kullanarak yazabiliriz. Deyişler arasına virgül koymayı unutmayalım. Gerekmez ama istersek virgülden sonra boşluk bırakarak yazılımın okunuşu biraz daha kolaylaştırabiliriz. Ama çıktı da durum başka. Orada boşluk bırakmak nasıl olur yakında göreceğiz.""".p,
    """satıryaz(noktaSayısı,3+2,noktaSayısı/2, 3.9/2.3)
var boy=noktaSayısı+4
satıryaz(boy)""".c,
    "Farkettiniz mi, çıktı gözü her değerin adını ve değerini yazmakla kalmıyor ikisinin arasında o değerin türünü de yazıyor, Int (Sayı), Double (Kesir) vb. Şimdi deneyelim,".p,
    """noktaSayısı=10 // bu hata verir. val ile tanımlanan değişkenlerin değerleri değiştirilemez""".c,
    """'error' hata demek. Scala derleyicisi (compiler), 'reassignment to val' yani sabit bir değeri değiştirmek hata olur diyor ve izin vermiyor.""".p,
    """boy+=4 // yani boyunu 4 nokta uzatalım
satıryaz(boy)""".c,
    """Okuyanları bilgilendirmek ve kendimize anımsatmak için satır sonlarına // yani iki taksim ya da bölüm işaretinden sonra bir açıklama yazabiliriz. Bir satıra sığmıyorsa /* ile başlayıp */ ile biten daha uzun açıklamalar ekleyebiliriz. Scala derleyicisi bunları göz ardı eder ve bu sayede bilgisayarın kafası karışmaz 8-).""".p,
    """/*
  Çok satırlı bir açıklama örneği
  Fahrenayttan santigrata çevirelim
*/
val dereceF = 98.4
// Tek satırda açıklama: 0C = 32F. 9F artış 5C artışa denk
satıryaz(dereceF, "derece Fahrenayt", (dereceF-32)*5/9, "derece Santigrat")  // satır sonu açıklama
""".c,
    "'satıryaz' komutuyla yazı da yazdık yukarıda gördüğün gibi. Böyle çift tırnaklar içine alınan yazıların türüne Yazı (İngilizcesi String) diyoruz. Bu tür, sadece yazı yazmak için değil, yazılarla işlemler yapmak için de kullanılabilir:".p,
    """val adım = "Mustafa Kemal"
val mesaj = "Merhaba " + adım
satıryaz(mesaj)
""".c,
    "Toplama işareti sanki toplama yaparmış gibi yazıları birbirine ekleyiveriyor. Mantıklı değil mi? Yazı türüyle daha neler yazılabilir neler! İki bölüm sonra başka örnekler de göreceğiz.".p,
    "Duymuşsundur eminim, bilgisayar devreleri aslında 2, 3, 4 gibi sayıları bile bilmez. Onun yerine sadece 0 ve 1 sayılarını tanır. Hatta tanımak dedik de aslında sadece voltaj değerlerini ve elektrik akımlarını tanır onlar. Bu uzun, ilginç ve çok keyifli bir öyküdür. Benim gibi elektrik mühendisi olmak istersen, bana emaille selam ve sorularını yollayabilirsin. Neyse, konumuza dönelim. Daha büyük sayılarla işlemler yapmak için bilgisayar onları bir 0 ve 1 dizisi olarak ele alır ve içindeki sayısız mantık devreleri sayesinde toplama, çıkarma, çarpma, bölme ve hatta türev ve integral alma gibi daha ileri matematik işlemlerini kolayca ve hiç üşenmeden halleder. Bunların detayı bilgisayar uzmanlarının işi. Biz 0 ve 1 dizilerine dönelim, çünkü herşey onlarla başlıyor! İngilizcede 'binary arithmetic' denir. Biz çift tabanlı sayma diyelim. Her sayı, 0 ve 1'lerden oluşan bir dizi olduğu için, direk onun parçacıkları üzerinde de işlemler yapabiliriz. Bu işlemlere 'bitwise' yani parçacık işlemi denir. Bu 0 ve 1 dizilerinin her bir elemanına İngilizcede 'bit' denir. Saçlarımızda yaşayan ve zararsız küçük böcekcikler değil elbet! İngilizcede azıcık, küçücük anlamlarına geliyor. Biz parçacık diyelim istersen. Çok uzattık. Kusura kalma. Şimdi parçacıkları teker teker nasıl işleme sokarız bir kaç örnek görelim:".p,
    """3&2 // mantıksal 've' işlemi de denir (ingilizcesi 'logic and'). Sadece 1 ve 1 sonuç olarak 1 verir. Girdilerden biri 0 olursa sonucu da 0 olur.""".c,
    """1|2 // mantıksal veya (or) işlemi. Sadece 0 veya 0 sonuç olarak 0 verir. Biri 1 olursa sonuç da 1 olur. """.c,
    """1^2 // mantıksal dışlayan veya işlemi (xor yani exclusive or). Sadece biri 1 öbürü 0 olunca 1 verir. """.c,
    """1<<2 // parçacıkları iki kere sola kaydır """.c,
    """-24>>2 // sağa kaydır ama eksiyse eksi kalsın """.c,
    """-14>>>2 // sağa kaydırıyor ama sonuca bakın! Ben anlamadım vallahi. Ya sen?""".c,
    "Bakın bu çok ilginç. Parçacıkları sola kaydırmak ikiyle çarpmaya denk! Sağa kaydırmaksa ikiye bölmeye benzemiyor mu? Bu daha önce de dediğim gibi uzmanlık konusu. Üzerinde yazılmış pekçok bilimsel makale ve ders kitapları var. Hatta bazıları çok azımızın anlayabileceği yüksek ihtisas kitapları! Bugünlük bu kadarı fazla bile. Ama sen istersen bu yazılımcığı kurcala. Dene. Bakarsın uzman olmak istersin. Neden olmasın?".p,
    "Farkında mısınız? Bu kadarcık bilgiyle bile artık çok güçlü bir hesap makinemiz oldu. Ama dahası var! Bir sonraki bölümde program akışı nasıl düzenlenir öğrenecek ve yazılımcıklarımızı çok daha becerikli hale getireceğiz.".p
  )
)

pages += Page(
  name = "Flow",
  body = tPage("Program Akışı, If, Else ve While Komutları",
    "Şu ana kadar yazdığımız yazılımcıkların komutları, baştan sona kadar, sırayla, satır satır ve teker teker çalıştılar. Ama pek çok durumda program akışını değiştirmek isteriz. Yani komutların çalışma sırasını duruma göre değiştiririz. Bu sayede, bazı komutları yineler, bazılarını atlarız.".p,
    "İlk önce bir komut dizisi (ya da komut bloğu da denir) oluşturuz. Bunun için komutları kıvrık parantezler, yani {} içine alıyoruz. Bu komut dizisi istediğimiz kadar satır ve hatta başka komut dizileri de içerebilir. Dizinin içindeki son komut dizinin değerini belirler.".p,
    "Program akışını değiştirmek için kullandığımız bir kaç değişik yöntem var. Gelin en önemlilerinden biri olan, eğer/yoksa anlamına gelen, koşullar oluşturmaya yarayan 'if' ve 'else' yapısıyla başlayalım.".p,"if".h3,
    """Genel olarak şöyle yazarız: "if (koşul) dizi1/deyiş1 else dizi2/deyiş2". Eğer koşul doğruysa ilk kısım yani dizi1/deyiş1 çalışır. Yoksa 'else' sözcüğünden sonra gelenler çalışır.""".p,
    """if(doğru) satıryaz("Doğru") else satıryaz("Doğru değil")""".c,
    """Bunu daha kısa da yazabiliriz. 'if/else' deyişi hep bir değer verir. Bakın bunu da satıryaz komutuyla yazıyoruz.""".p,
    """satıryaz(if(doğru) "Doğru" else "Doğru değil")""".c,
    """Kulladığımız koşulun geçerli olması için 'doğru' ya da 'yanlış' (İngilizcede 'true' veya 'false') değerlerinden birini vermesi gerekir. Bu değerlerin türüne biz İkil deriz (İngilizcesi Boolean). Bu koşulu sağlayan işlemlere karşılaştırma işlemi deriz. Bunlar matematiksel işlemler ya da benzerleri olabilir. İlk önce matematiksel olanları, yani sayıları karşılaştıran işlemleri görelim. Başka tür değerleri (örneğin sözcükleri) karşılaştırmayı sonraya bırakalım.""".p,
    """1>2 // büyüktür""".c,
    """1<2 // küçüktür""".c,
    """1==2 // eşittir""".c,
    """1>=2 // büyük ya da eşittir""".c,
    """1!=2 // eşit değil""".c,
    """1<=2 // küçük ya da eşittir""".c,
    "if/else yapısının nasıl çalıştığını anlamak çok kolay. Koşul doğruysa 'else' sözcüğünden önceki komutlar çalıştırılırlar. Yok, eğer koşul doğru değilse, o halde 'else' sözcüğünden sonraki komutlar çalıştırılır. Scala ve diğer işlevsel dillere benzemeyen programlama dilleri (örneğin, C, Java, Python), if/else yapısını sadece akışı belirlemek için kullanırlar. Ama Scala, Haskell ve diğer işlevsel diller gibi, if/else yapısından bir değer beklerler. Onun için 'if' genelde tek başına kullanılamaz. Arkasından hemen 'else' gelir ve iki durumda da bir değer geri bildirilir. Bunun bir istisnası da vardır, ama onu da sonraya bırakalım. Bakın bu örneklerde koşul olarak hep yalın sayıları karşılaştırıyoruz. Ama elbette başka 'val' değerler ve komutlar da kullanabiliriz. Yeter ki bir İkil, yani doğru ya da yanlış değeri olsun.".p,
    """if(1>2) 4 else 5 // büyüktür""".c,
    """if (1<2) 6 else 7 // küçüktür""".c,
    """val deneme1=if (1==2) 8 else 9 // eşittir""".c,
    """val kitapsa = 6>=3
val değer=16
val sayı=10
val satış=if (kitapsa)değer*sayı else {
  val birim = değer/sayı // { ve } içinde bir dizi deyiş ya da bir dizi komut. Tabii hem deyiş hem de komutlar da olabilir...
  birim*3  // dizinin değeri içindeki bu son deyişin değeri olacak
}
yaz(satış) // doğru mu?""".c,
    "Gerçek hayatta da olduğu gibi bazen bir kaç koşul bir araya gelir. Bu durumda iki tane mantıksal işlem kullanırız. Bunlardan birincisi '&&' mantıksal 'Ve' anlamına gelir. İkincisi de '||'  mantıksal 'Veya' anlamına gelir. Bunları sakın parçacıkları işleyen '&' ve '|' işlemleriyle karıştırmayalım. Aslında ilişkili ve benzer kavramlar. Ama dikkat edelim. Çok uzatmadan birkaç örnek görelim, göreceksin çok doğal gelecek. Ne de olsa mantık hepimizde var. ".p,
    """val kitapsa = 6>=3
val değer=16
val sayı=10
val satış=if (((kitapsa)&&(değer>5))||(sayı>30))değer*sayı else değer/sayı""".c,
    "while".h2,
    """İngilizce'de pek çok farklı anlama gelen 'while' sözcüğünü Türkçemize 'o halde' ya da 'o sırada' diye çevirebiliriz. Bu yapı programlama dillerinde çok kullanılır. Genel olarak, "while (koşul) dizi/deyiş" yapısı kullanarak komut dizisini tekrar tekrar çalıştırabiliriz. Koşul sağlandığı sürece yineleme devam eder. Koşul değişince, yani artık doğru olmadığında yineleme son bulur. Bir örnekle anlamak çok daha kolay olacak. Ama ilk önce bir değişkenle başlayalım:""".p,
    """var toplam=18
while(toplam < 15) toplam += 5
yaz(toplam)
""".c,
    """Kolay değil mi? Sizce kaç kere yinelenecek komut? Sonunda toplam kaç olacak? Bu biraz hileli bir soru oldu. Aslında yinelenecek mi, toplam değişecek mi diye mi sormalıydık? Toplamın ilk değerini ya da koşulu değiştirip tekrar çalıştırabiliriz elbet. Bu 'while' yapısını tersine de çevirebiliriz: "do dizi/deyiş while (koşul)" 'do' İngilizce'de de doremi gibi nota adıdır, ama daha yaygın anlamı 'Yap' ya da 'Yapmak' demek. Hatta emir kipi. Yani lütfen yap yani çalıştır diyoruz. Bakın burada ilk önce komut dizisi çalıştırılır sonra koşula bakılır. Doğruysa komut dizisi yinelenir:""".p,
    """var toplam=18
do toplam += 5
while (toplam < 15)
yaz(toplam)
""".c,
    "Gördük ki bu sefer toplam 23 oldu. Bir önceki örnekteki halbuki 18 olmuştu. Bakın bu yapıyı iki sayının ortak paydalarının en büyüğünü hesaplamak için kullanalım:".p,
    """// en büyük ortak paydayı bulalım
var x = 36
var y = 99
while (x != 0) {
    val yardımcı = x
    x = y % x
    y = yardımcı
}
satıryaz("ortak paydaların en büyüğü: " + y)
""".c,
    "for".h2,
    """Komut dizilerini kolayca yinelemek için kullanabileceğimiz bir yöntem daha var ki belki de en faydalısı. "for (aralık) dizi/deyiş" sayesinde verilen aralıktaki her bir değer için dizi/deyiş yinelenir. 'for' elbet yine ingilizce bir sözcük. Anlamı 'için' demek ("for you" senin için demek). Aralık da nedir mi? Hemen bir örnek görelim:""".p,
    """for ( i <- 1 |-| 4 ) yaz("merhaba!")""".c,
    """Bunun ingilizcesi de şöyle:""".p,
    """for ( i <- 1 to 4 ) yaz("merhaba!")""".c,
    "'to' sözcüğü de bizim yapım/çekim ekimiz gibi, birden dörde kadar derken dörde sözcüğündeki '-e' anlamında. İngilizceyle Türkçe ne kadar farklı sanki, değil mi? Bir de bana sorun. 22 yaşında ilk defa yaşamak için Amerika'ya gittiğimde o kadar zorluk çektim ki! Güya iyi biliyordum hem de İngilizceyi! İşimize dönelim: Aralık burada birden dörde kadar olan sayılar elbet. 'i' değişkeni 1 değeriyle başlıyor ve her tekrarda bir artıyor. Son sayı burada 4. Ama sonuncu sayıya gelmeden hemen önce durmak istersek '|-' yerine '|-|' imgesini kullanıyoruz:".p,
    """for ( i <- 1 |- 12 )  {
  val kare = i*i
  satıryaz(i, kare)
}""".c,
    """Bunun ingilizcesi de şöyle:""".p,
    """for (i <- 1 until 12) {
  val kare = i*i
  satıryaz(i, kare)
}""".c,
    """İngilizce'de 'until 12', 12'ye kadar anlamina geliyor.""".p,
    "Biliyor musun, bu yineleme işlemlerini birden çok boyutta yapmak bilgisayarla çok kolay. Birden fazla aralık vereceğiz ve her aralık için de bir değişken (aslında değişiyor gibi göründüğüne bakma, bunlar da değişmez). Tek dikkat etmemiz gereken ikisi arasına bir noktalı virgül koymak. Bakın ne kolay!".p,
    """for(i <- 1 |- 5 ; j <- "abc") satıryaz(i, j)""".c,
    """Bakın şu işe! Sayı yerine harfler kullandık! "for" yapısı içinde kullandığımız kümeler illa da sayılardan oluşmak zorunda değil yani. Genel olarak biz bunlara küme tekerleme diyebiliriz (İngilizcesi: iterating through a set or collection) yani teker teker her küme elemanını ele alıyoruz. "abc" yazısı da aslında bir harf kümesi ya da kolleksiyonu. Bakın hep küme ya da kolleksiyon dedim. Bu kavramlar yakın ama ufak farklılıkları var. Daha sonra bunlara verilen anlamı daha iyi anlayacağız. Şu anda çok da önemli değil gerçekten. Neyse. Harflerle tekerlemeye bir örnek daha verelim ve devam edelim:""".p,
    """for(c<-"merhaba!") satıryaz(c)""".c,
    "Şimdi de matematik, bilhassa kartezyen geometrisi sevenlere bir süprizimiz var. Kaplumbağacığı kullanarak bir eğri çizelim. Neyin eğrisi? İki boyutlu bir poligon. Genel olarak a*x^2 + b*x + c diye yazabiliriz. Yine bu çok faydalı olan 'for' yapısıyla:".p,
    """sil
def eğri(x: Kesir) = 0.001 * x * x + 0.5 * x + 10   // 'def' define yani tanımla demek. Bunu daha sonra daha iyi anlatacağız. 
gridiGöster(); eksenleriGöster() // kare çizgileri ve x ve y eksenlerini çizelim
val aralık = 200
atla(-aralık,eğri(-aralık))
for(x <- -aralık+10 |-| aralık; if (x % 10 == 0)) noktayaGit(x, eğri(x))
""".c,
    "Eksenleri silelim. Ve bir sonraki bölüme devam edelim!".p,
    "eksenleriGizle(); gridiGizle()".c
  )
)

pages += Page(
  name = "Literals",
  body = tPage("Yalın Değerler, Sayılar, Kesirler ve Yazılar",
    """Bütün hesaplar gibi, bütün programların da yapıtaşları 'yalın' değerlerdir. Nedir onlar? Örneğin 1, 2, 1/3, 3.14, 'a', 'b', 'ç', "Merhaba Güzel Dünya!", ve aklın alamayacağı kadar çok sabit yani değişmez değerler. Hepsini biliyorsun elbette. Tabii bu örnekler biraz karışık oldu. Bir harfle bir sayıyı karıştırmak iyi olmaz. Onun için bunları türlerine ayırmak Scala'nın çok iyi becerdiği bir konu. Başka pekçok dil de buna dikkkat eder. Ama göreceksin Scala bir başka. Uzatmadan en temelinden başlayalım.""".p,

    "Temel Türler".h2,
    "Scala kendiliğinden bazı temel türler tanımlar. Daha ilerde göreceğimiz gibi sen de kendi türlerini tanımlayıp Scala'dakilere ekleyebilirsin. Ama ilk önce bu temel türlerle tanışalım, işi kolay kılalım.".p,
    table(
      row("Lokma","""İngilizcesi Byte. -128'den başlar, -127 -126 ... -1 0 1 2 ... 127'e kadar gider. Neden seçip ayırmış bu sayıları acaba? Daha önce de konuştuk ya, bilgisayar, her sayıyı sıfır ve bir sayılarının dizisi olarak saklar ve işler. Bu sayı dizisilerindeki bir ve sıfır sayılarına parçacık demiştik. Sekiz tanesini bir arada ele alırsak, ona Lokma diyoruz. Eksi sayıları artılardan ayırmak için o parçacıklardan birine özel bir anlam veriyoruz. Geriye kalıyor 7 parçacık. İkinin üstlerini anımsayalım: 2 4 8 16 32 64 128. Yani 7 parçacıkla 128'e kadar sayabiliyoruz. İngilizcede buna "8-bit signed 2's complement" derler. Uzattık ama temelin temeli bu! Neyse, şimdilik bu kadar yetsin"""),
      row("Kısa","(Short). İki lokma yani 16 parçacık. -32,768'den başlayıp 32,767'yle biter"),
      row("Sayı","(Integer) Dört lokma yani 32 parçacık. -2,147,483,648'den 2,147,483,647'e kadar gider"),
      row("Uzun","(Long) İki sayı yani 64 parçacık. -2^63 ile 2^63-1 arası"),
      row("UfakKesir","(Float) 32 parçacık kullanarak kesirli sayıları ifade eder. İşini ciddiye alır ve IEEE'nin 754 no'lu standardına göre yapar. Bilirsin pi sayısının basamakları bitmek bilmez. UfakKesir olarak ama tam değeri şudur: 3.1415927"),
      row("Kesir","(Double) Ufak kesirden iki kat daha hassas. Bunu da 64 parçacık kullanarak sağlar. Pi sayısını şöyle bilir: 3.141592653589793. Aynı ufak kesir gibi ciddi standarda uyar."),
      row("Harf","Unicode karakterler dizisi. 16 parçacık kullanır. Elbette eksi artı sıkıntısı yoktur. (unsigned denir ingilizce)"),
      row("Dizi","Sınırsız bir harf dizisidir. Çok basit olsa da çok faydalıdır."),
      row("İkil","doğru ve yanlış değerlerini bilir başka da birşey bilmez.")),
    "Sayma Sayıları".h2,
    "Tabloda da gördüğümüz gibi dört temel tür var (bir de iri sayılar var. Çok büyük sayma sayılarıyla oynamak için tasarlanmış sayı türünün adı İriSayı (BigInt). Eğer canın mümkün olan bütün briç ellerini saymak isterse Uzun yetmez İriSayı gerekir. Merak edersen bana haber ver. Konuşuruz.) Bunları tekrar anımsayalım çünkü çok faydalılar: Lokma Kısa Sayı ve Uzun. Yalın sayı değerlerini girmenin tek yolu var sanma. Farklı tabanlar kullanabiliriz. Sadece onluk tabanla sınırlı değil becerimiz. Onaltılık taban da bazen işe yarar. Bunu belirtmenin yolu ilk rakamı 0 yapmak.".p,
    "Bize en doğal gelen onluk tabanın yalın sayıları 1 2 3 ... 8 ya da 9 ile başlar (decimal)".p,
    "17".c,
    "653".c,
    "val benimSayım = 653".c,
    "val benimSayım: Sayı = 653".c,
    "Onaltılık taban da bilgisayara ve bazı algoritmalara çok doğal gelir (hexadecimal): Sıfırla başlar ve x harfini ekleriz. Büyük X de olur. Farketmez. Sonra da onaltılık tabanın 16 sayısından seçeneklerle devam ederiz. İlk onunu herkes bilir, 0 1 2 ... 8 9. Sonra değeri onluk tabanda 10 olan a ya da A, sonra 11'e denk b/B ... en son da 15'e denk olan f/F. Ne ilginç değil mi? 13 yazmak yerine 0xd ile batıl inançlara da son verebiliriz belki!".p,
    """val x1 = 13
val x2 = 0xd
if (x1 == x2) satıryaz("kim korkar d'den?!")""".c,
    """0x23  // onaltılık taban. Yani onluk tabanda 35""".c,
    """0x01FF // onluk tabanda 511""".c,
    """0xcb17 // onluk tabanda 51991""".c,
    """Varsayılan bunların Sayı türünde olduğudur (İngilizce'de 'by default' denir). Ama bazen Uzun sayı olması gerekebilir. Onu ifade etmek de kolay. Sonuna "l" ya da "L" (yani ingilizce Long) koyarız. U veya u da iyi olurdu ama bunu Kojo'ya eklemeyi bilemedim ben. Belki de Scala, hatta Scala'nın atası olan Java'yı güncellemek gerekmesin?""".p,
    """0XFAF1L // Uzun sayı onluk tabanda 64241""".c,
    "Yalın değerlerin Kısa ve Lokma türlerinde olmasını isteriz baze. Ama, dikkat edelim ki geçerli aralığı aşmasın. Yoksa ne olur? Deneyerek daha iyi anlarsın sanıyorum.".p,
    "val birLokma: Lokma = 27".c,
    "val kısaSayı: Kısa = 1024".c,
    """val sorunYok: Lokma = 127
val sorunsuz: Lokma = -128
val hataVerir: Lokma = 128 // hata verir. "Error... type mismatch" gibi birşeyler der derleyici. Yani türler uyuşmadı diyor. 128 sayısı Lokma'ya sığmıyor.
""".c,
    "Kesirli sayılar".h3,
    "Kesirli sayıları yalın olarak ifade etmek için nokta kullanırız elbet. Sıfır dışında bir sayıyla başlarlar. İşin ilginci sonuna E veya e harfi ekleyebiliriz. O İngilizce exponent yani üssü anlamına gelir ve hemen arkasından bir tam sayı (eksi de olabilir) ekleriz. Örneklerle anlamak daha kolay olacak.:-".p,
    "9.876".c,
    "val çokKüçük = 1.2345e-5".c,
    "val epeyBüyük = 9.87E45".c,
    "Daha iyi anlamak için şu örneklere bakalım:".p,
    """2e3 // yani 2000. üç sıfır ekliyoruz yani
3e-2 // yani 0.03. yüzle çarparsak 3 oluyor, fark ettin mi?
0.5e1 // 5.0 yazsak daha açık olurdu. Sadece daha iyi anlamak için
7.8e9 // şu anda, yani 2020'nin son günlerinde, yeryüzündeki insan kardeşlerimizin sayısı buymuş!
""".c,
    "Nüfusumuz kaç oldu?".link("www.worldometers.info/world-population"),
    """Varsayılan, kesirli yalın sayıların Kesir türünde olmasıdır. Ama istersen UfakKesir de olabilirler. Sonuna f ya da F getirmen yeter. Bu İngilizce "float" sözcüğünden gelir o da bizim noktanın adı "floating point" olduğu için. Eğer Kesir olduğunu açık etmek istersen sonuna d veya D getiriver. O da "double" sözcüğünün baş harfi. Neden double? Çünkü UfakKesir 32 parçacık kullanırken, Kesir 64, yani iki katı parçacık yani daha çok bellek kullanır. Bellek büyüdükçe de hassasiyet artar. Öte yandan ne kadar çok bellek kullanırsak, hem belleğin sınırlarını aşma tehlikesi artar hem de program daha yavaş çalışır. Onun için bazı programları yazarken hangi tür sayıları seçeceğimize çok dikkat ederiz.""".p,
    "val ufakkesir1 = 1.5324F".c,
    "val ufak2 = 3e5f".c,
    "Harfler".h3,
    "Bilgisayar, okumayı yazmayı bilmese de pekçok karakteri ya da harfi tanır. Sadece Türkçe ve İngilizce alfabeler değil hem de. Daha başka pekçok tek harf dediğimiz sembol, ya da karakter var. İngilizce adı da zaten Char, yani character sözcüğünün kısaltması. Bu harfleri yalın olarak girmek kolay. Tek tırnak içine alıverelim:".p,
    "val harf = 'A'".c,
    "Bu değeri girmenin daha zor yolları da var! Şaka şaka. Daha genel yolları bilmekte fayda var:".p,
    "Evrensel kodlama şöyle oluyor. '\\u0000' ve '\\uFFFF' arasında yani onaltı tabanlı bir sayı seçiyoruz.".p,
    "val a = \'\\u0041\' // A oldu ".c,
    "val e = \'\\u0045\' // E oldu".c,
    "Evrensel Kod (bunların genel adı Unicode) hakkında wikipedi makalesi".link("tr.wikipedia.org/wiki/Unicode"),
    "En son olarak, 'kaçış' yalın harfi diye de bilinen bir kaç özel duruma bakalım. Bunlara harf demek doğru olmuyor ama ne yapalım. Buyurun:".p,
    "Referans kaynağından yalın kaçış harfleri listesi:".p,
    "Java'nın teknik özellilerini anlatan siteden".link("docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6"),
    "Her türlü programlama sorularına yanıt bulabildiğimiz meşhur bir siteden de benzer bilgiler edinebiliriz:".p,
    "stackoverflow.com'dan".link("stackoverflow.com/questions/1367322/what-are-all-the-escape-characters"),
    "Yazılar".h3,
    "Yalın bir yazı, çift tırnaklar arasına aldığımız harflerden oluşur. Birkaç tane örnek gördük bile. Birkaç tane daha olsun:".p,
    """val merhaba = "merhaba dünya!" """.c,
    "Bazı özel karakterler var ki onları yazıya eklemek için özel bir kaçış karakteri kullanmamız gerekiyor. Bunlarin hepsi iki karakterden oluşuyor. Birincisi \\ olan kaçış karakteri. Arkasından da bir özel karakter daha gelir. En yaygın olanlar ve işlevleri şöyle:".p,
    table(
      row("""\n""", "yeni satır","""\b""", "geri adım","""\t""", "büyük ara","""\f""", "bir sayfa at"),
      row("""\r""", "satır başı","""\" """, "çift tırnak", """\'""", "tek tırnak","""\\""", "geri taksim işareti")
    ),
    """val kaçışKarışıklığı = "\\\"\'" """.c,
    "Bu son örnekte gördüğümüz karışıklıktan kurtulmak için Scala üç tane çift tırnak (\"\"\") arasına aldığımız yazıları olduğu gibi yazar. Yani yeni satır, tek tırnak, çift tırnak ne varsa olduğu gibi alır. Bakın bir örnekle anlayalım:".p,
    "satıryaz(\"\"\"Kojo'ya hoşgeldin!\n\"Kaplumbağa\" ile çizim yapmak hakkında daha çok bilgi edinmek için\n\\Kaplumbağacığın Kullanılışı\\ adlı bölüme bakabilirsin.\"\"\")".c,
    "İkil".h3,
    "İngilizcesi boolean olan bu tür, mantıksal işlemlerde ve koşullar ifade etmek için kullanılır. Çok özel bir türdür. Sadece iki yalın değerle iş biter:".p,
    "val büyükOlsunMu = doğru".c,
    "val birazŞapşalMı = yanlış".c
  )
)

pages += Page(
  name = "Functions",
  body = tPage("İşlevler",
    """İşlevlerin başka bir adı da fonksiyon. Matematikte de kullanırız bu terimi. Programlama yaparken biraz farklı bir anlamı var. İşlevler, komut ve deyiş dizileri gibidirler. Tekrar tekrar kullanmak istediğimizde de çok faydalı olurlar. Onun için ilk önce bir ad vermek gerekir. Sonra da bir ya da daha fazla parametre girişi yapabiliriz. 'def' özel sözcüğüyle başlarız bir işlev tanımlamaya. İngilizce 'define' sözcüğünün kısaltılmışıdır. Def olsun değil! Arkasından da işlevin adını veririz. Yine en iyisi bir güzel örnekle başlayalım. Bakın işimiz şu. İki sayı arasından hangisinin daha büyük olduğunu bulmak.""".p,
    """def büyük(x: Sayı, y: Sayı): Sayı = {
    if (x > y) x
    else y
}
""".c,
    """İşlevin adını "büyük" koyduk. Hemen "def" anahtar sözcüğünden sonra geldi. Arkadan parantez içinde gerekli iki sayının adlarını tanımladık. Bunlara genelde işlevin değiştirge ya da parametreleri denir. Her değiştirge için türünü de belirtmemiz gerekir. Değiştirgenin adıyla türü arasına da iki nokta üstüste konur. Bu işlevimizin iki değiştirgesi var ve ikisi de Sayı türünde. Parantezden sonra da işlevimizin çıktısının türünü bildiriyoruz ki o da bir sayı. En son da bir eşittir işareti ve sonra kıvrık parantezler içinde işlevin bedeni geliyor.""".p,
    """Bu şekilde tanımladığımız anda artık onu çağırabilir ve kullanabiliriz:""".p,
    "büyük(6,7)".c,
    "Genelde her işlevin bir çıktısı vardır. Ama bazen çıktısız işlevler de olur. O tür işlevlerin sadece yan etkisi olacaktır. Örneğin ekrana yazı yazar ya da diske bir dosya yazar. O durumda çıktının türü Birim olur (İngilizcesi Unit) ve değer yok ya da boşluk (void) anlamına gelir. Bu tür işlevlere yöntem, prosedür ya da altprogram da denir bazen (procedure ve subroutine İngilizce'de iki ilişkili sözcük). Daha önce birkaç örnek yöntem görmüştük:".p,
    """sil
ileri(100)
satıryaz("bunu yazan işleve yöntem de deriz")
""".c,
    "Yan etkisi olmayan işlevlere ise arı işlevler deriz (pure functions). Bunlar matematikte kullandığımız işlevlere benzerler. Aynı girdi değerleri hep aynı çıktıyı verir. Arı işlevler, adını işlevsel programlama koyduğumuz bir programlama tarzının temel yapıtaşıdır.".p,
    "Özyineli (recursive) İşlevler".h3,
    "İşlevler bazen kendilerini çağırabilirler (recursion). Özyineli işlevlerle daha önce gördüğümüz küme tekerlemesi, yani kümenin her elemanını teker teker ele alma işlemini daha kısa şekilde ifade edebiliriz. Yine bir örnekle anlamak daha kolay olacak. Bakın hiç yardımcı değişken kullanmadan iki sayının bölenlerinin en büyüğünü bulabiliriz:".p,

    """// en büyük ortak payda
def enbop(x: Uzun, y: Uzun): Uzun = if (y == 0) x else enbop(y, x % y)
""".c,
    """Bu tanımı daha önce "while" komutuyla yaptığımız tanımla karşılaştırmanda fayda var.""".p,
    "enbop(96, 128)".c,
    "Şimdi de daha renkli bir özyineleme görelim. Bu işlev kendini iki kere çagırarak kaplumbağacığa bir ağacın dallarını çizdiriyor. Bu tür ağaçlara ikil ağaç (binary tree) deriz. Sen de beğendin mi? Özyineleme nasıl duruyor? Yukarıdaki 'enbop' işlevinde y'nin değeri sıfır olunca. Aşagıda ise uzaklık dört ya da daha küçük olduğunda.".p,
    """def ağaç(boy: Kesir) {
    // sayıya metodu kesirli sayıyı tam sayıya çeviriyor
    // yani boy 1.75 olursa boy.sayıya 1 oluyor
    def renk = Renk(boy.sayıya % 255, mutlakDeğer(255 - boy * 3).sayıya % 255, 125)
    if (boy > 4) {
        kalemKalınlığınıKur(boy / 7)
        kalemRenginiKur(renk)
        ileri(boy)
        sağ(25)
        ağaç(boy * 0.8 - 2)
        sol(45)
        ağaç(boy - 10)
        sağ(20)
        ileri(-boy)
    }
}
sil()
hızıKur(hızlı)
konumuKur(100, -200)
ağaç(90) // 100, 120 ve 150 gibi boyları da dene!
""".c
  )
)

pages += Page(
  name = "OandC",
  body = tPage("Nesneler ve Sınıflar",
    "Herşey bir Nesne!".h2,
    "Scala, nesneye yönelik bir programlama dili olarak tasarlanmıştır. İngilizce'de Object Oriented Programming, hatta kısaca OOP diye bilinen bu kavramı destekleyen pek çok dil var. Bu kavramın temelinde nesneler yatıyor. Nesne ne demek biliriz elbet. Ama yazılım yazmaya gelince nesne ne demek? Herhangi bir anda programın içinde bulunduğu evre (state) nesnelerin yapısında saklanıyor demek. Bu ne demek? Programdaki nesnelere bakarak program hakkındaki herşeyi bilebiliriz, bir. İki, programın çalışması ve gerekli değişiklikleri yerine getirmesi için de içindeki nesnelerden bir kısmının değişmesini sağlamak yeter. Üçüncü önemli nokta da şu: nesnelerin değişmesi ancak nesnelerin üzerinde tanımlanmış metodları kullanarak sağlanır. Kojo'da sadece bir nesne kümesinden ibaret! Epey çok nesne var elbette Kojo yazılımının içinde. Bunlar yazılımcık düzenleyicisine yazdığımız komutları işleyerek kaplumbağayı hareket ettiriyor, çizgiler çizdiriyor ya da çıktı gözüne yazılar yazıyor.".p,
    "Temel türleri hatırlıyorsun, değil mi? Sayı türleri, yazı türleri... Yeni nesneler tanımlamak için de yeni bir tür tanımlarız ilk önce. Yani yeni nesnenin nasıl bir evre ve davranışları olduğunu belirleyen bir sınıf oluştururuz (sınıf ve tür çok benzer kavramlar). Sınıfın tanımı, nesnelerinin ne değişkenler içereceğini ve ne metodlar (yani davranışlar) sunacağını belirler. Metod dediğimiz de aslında birer işlevdir ve nesnenin uzuvları gibidirler, nesnenin içindeki değişkenleri bilir, onlara göre davranır ve hatta o değişkenleri değiştirebilirler. Sınıfın içinde tanımlanan bu metodlar da 'def' anahtar komutu kullanarak tanımlanırlar. Aynı daha önce gördüğümüz işlevler gibi. Metodlar yöntem ya da arı işlev olabilirler. Sana ve ne yapmak istediğine bakar.".p,
    "Yeni bir sınıf tanımladığında yeni tür bir ya da daha çok nesne tanımlamış oluyorsun. Bu yeni tür, daha önce tanımlanmış, hatta Scala'yla hazır gelen Sayı, Kesir gibi türlerin hepsiyle bir tutulur. Yani Scala hiç ayırım yapmaz! Scala'nın türler ve sınıflar arasında taraf tutmaması, yani herşeyin bir nesne ve her nesnenin de bir türü olması ne kadar yararlıdır yakında göreceğiz.".p,
    "Haydi iki boyutlu bir nokta tanımı yaparak başlayalım yeni türler üretmeye! İki tane değişkeni olacak, x ve y. Sınıf adlarını da geleneksel olarak büyük harfle başlıyoruz, Sayı, Harf gibi temel türlerde olduğu gibi. Ama sınıf içindeki değerler, değişkenler ve metodlar küçük harflerle yazılıyor.".p,

    """// class sınıfın İngilizcesi. Anahtar kelimemiz class:
class Nokta {
    var x = 0
    var y = 0
}
// x ve y için başlangıç değeri gerekli. Ve 0 mantıklı gibi. Ama daha da iyi bir çözüm bulacağız yakında..
""".c,
    """Bakın nesnenin tanımı şimdilik bu kadar! Daha doğrusu sınıf belirlendi. Yeni bir tür yarattık bile. Bu sınıfın bir elemanı, yani bu türden yeni bir nesne oluşturmak da çok kolay. Yeni anlamına gelen İngilizce 'new' anahtar sözcüğünü kullanıyoruz:""".p,
    "val n = new Nokta".c,
    """Nesnenin iç değerlerini ele almak ya da değiştirmek için de nesnenin adına nokta yani '.' ekliyor arkasından da iç değişkenin adını yazıyoruz:""".p,
    """n.x = 3
n.y = 4
""".c,
    "İç değişkenlerin değerini okumak için de aynı şeyi yapıyoruz:".p,
    "satıryaz(n.x, n.y)".c,
    "Bu biraz zahmetli oldu yanlız. Her yeni nokta için x ve y değerlerini bu şekilde belirlememiz fazla zamanımızı alıyor. Bunun daha kolay bir yolu var. Nesneyi oluştururken noktanın koordinatlarını girmek daha kullanışlı olurdu. Bunun için yapmamız gereken değişiklik küçük:".p,
    "class Nokta(var x: Sayı, var y: Sayı)".c,
    "Sonrada yeni bir nokta tanımlayalım:".p,
    "val n = new Nokta(3, 4)".c,
    "Bakın ilk nokta tanımda 0 değerini vererek x ve y'nin Sayı türünde olduğunu belirlemiştik aslında. İkinci tanımda ise tür bilgisini açık açık verdik. Mantıklı, değil mi? Derleyici ne yapması gerektiğini biliyor iki durumda da. Herneyse, bu biraz ileri bir programlama kavramı oldu. Biz noktalara dönelim. Her nokta aslında iki boyutlu bir vektör demektir. İki noktayı toplamak demek iki vektörü birbirine eklemek demek. Nasıl yapacağız? Nokta sınıfına yeni bir metod eklemek en mantıklısı. Deneyelim:".p,
    """class Nokta(var x:Sayı, var y:Sayı) {
    def vektörToplama(yeniNokta: Nokta): Nokta = {
        new Nokta(x + yeniNokta.x, y + yeniNokta.y)
    }
}""".c,
    "Bakın bu basit bir işlev ama sınıf tanımının içinde olması onu özel bir metod haline getiriyor. Neden özel? Çünkü nesnenin iç değerlerini okuyabiliyor ve iç değişkenlerini değiştirebiliyor. Bakın şimdi iki vektörü birbirine ekleyelim, bakalım ne bulacağız:".p,
    """val n1 = new Nokta(3,4)
val n2 = new Nokta(7,1)
val n3 = n1.vektörToplama(n2)
satıryaz(n3.x, n3.y)
""".c,
    """Eğer Java programlamayı biliyorsan, bu çok tanıdık gelecektir. Ama "n1+n2" yazmak çok daha doğal olurdu, değil mi? Scala ile mümkün! Gelin deneyelim. Hatta vektör çıkarmayı da tanımlayıverelim:""".p,
    """class Nokta(var x: Sayı, var y:Sayı) {
    def +(yeniNokta: Nokta): Nokta = {
        new Nokta(x + yeniNokta.x, y + yeniNokta.y)
    }
    def -(yeniNokta: Nokta): Nokta = {
       new Nokta(x - yeniNokta.x, y - yeniNokta.y)
    }
    override def toString = "Nokta("+x+", "+y+")"
}
""".c,
"""'override' üstüne yazmak ya da yeniden tanımlamak anlamlarına geliyor. Neyin üzerine yazıyor ve tekrar tanımlıyoruz? 'toString' adlı işlevi. Peki aslı nereden geliyor ki tekrar tanımlayalım? Hani dedik ya herşey bir nesne. Gerçekten de adı Nesne olan bir tür var ve aslında bütün türlerin temelini oluşturuyor. Bu temel sınıf ne demek daha sonraya bırakalım. Ama bilmemiz gereken şey onun toString diye bir metodu olduğu ve bu metodun her nesneyle çalıştığı. Bu metod her nesneyi okunabilecek bir yazı olarak ifade ediyor:""".p,
    """val n0 = new Nesne()
// Türkçe'ye 'yazıya' diye çevirdik:
satıryaz(n0.yazıya)
// her tür kendisi için toString metodunu yeniden tanımlıyor:
val n1 = 9
satıryaz(n1.yazıya)

val n2 = Dizin(9, 99)
satıryaz(n2.yazıya)
// bu 'case class' nedir birazdan göreceğiz!
case class AkıllıSayı(val sayı: Sayı) {
    override def toString = "Ben bir sayıyım. Değerim de " + sayı + "'dur."
}

val n3 = AkıllıSayı(99)
satıryaz(n3.yazıya)
""".c,
    """Biz şimdi noktalarımıza dönelim. Nokta sınıfı hazır. Noktalar tanımlayalım:""".p,
    """val n1 = new Nokta(3,4)
val n2 = new Nokta(7,2)
val n3 = new Nokta(-2,2)
val n4 = n1+n2-n3
satıryaz(n4)
""".c,
    "Bu çok daha okunaklı, değil mi? Kocaman bir vektör cebiri yaratmak işten bile değil Scala ve Kojo'yla!".p,
    "Daha önce de söylediğimiz gibi, Scala'da her sınıfın varsayılan bir 'toString' metodu var (Türkçesi 'yazıya') ve nesnelerin yazıyla ifade edilmelerini sağlıyor ki, örneğin satıryaz komutuna girdi olabilsin. Varsayılan metod o nesneye özgü bir numara ve tür adı içerir. Biz onu tekrar tanımlamış ve daha okunur hale getirmiştik yukarıda.".p,

    """Yukarıda basit bir örneğini gördüğümüz 'case' anahtar sözcüğüyle sınıf tasarımı biraz daha kolaylaşıyor. Bu 'case' sözcüğü programlamada önemli bir kavrama işaret ediyor. İleride göreceğimiz 'match' anahtar sözcüğüyle beraber de kullanılıyor. İngilizce'de durum, olay ve olgu gibi anlamlara gelir. Kullandıkça daha iyi anlayacağız.""".p,
    """case class Nokta(x: Sayı, y: Sayı) {
    def +(yeniNokta: Nokta) = Nokta(x + yeniNokta.x, y + yeniNokta.y)
    def -(yeniNokta: Nokta) = Nokta(x - yeniNokta.x, y - yeniNokta.y)
    override def toString = "Nokta(" + x + "," + y + ")"
}
val n1 = Nokta(3, 4)
val n2 = Nokta(7, 2)
val n3 = Nokta(-2, 2)
""".c,
    "n1+n2-n3".c,
    """Burada 'case' sayesinde "new" anahtar sözcüğüne gerek kalmadı. n1, n2 ve n3 nesnelerini yaratmak için sadece 'Nokta(...)' yetti. Bu Scala derleyicisinin Java derleyicisinden daha becerili olduğunu ortaya koyan örneklerden biri.""".p,
    """İki husus daha var üzerinde durmamızda fayda olan. 1. + ve - işlevleri tanımlarken 'def' anahtar sözcüğünü kullandık ama eşittir işaretinden sonra kıvrık parantezlere gerek duymadık. Bunun nedeni basit. Sağ tarafta sadece bir tane deyiş ya da sadece bir tane komut varsa kıvrık parantezlere gerek kalmıyor. Bu sadece 'case class' içinde değil bütün 'def' işlevleri için geçerli. 2. + ve - metodlarının tanımında çıktı türü belirtmeye de gerek duymadık. Bu Scala derleyicisinin çok faydalı becerilerinden biridir: tanımda kullanılan deyiş ve komutlara bakarak çıktı türünü kendisi belirliyiveriyor! Buna İngilizce'de özel bir ad takılmıştır: "type inference" yani tür çıkarımı. """.p,
    "Herşey bir nesne demiştik. Bunu anımsamakta fayda var. Peki o zaman neden Java gibi nesneye yönelik dillerdeki biçimde yazmadık:".p,
    "n1.+(n2.-(n3)) // n1.+(n2).-(n3) ?".c,
    "Scala'nin sözdizimini kolaylaştıran ve kodun daha okunur hale gelmesini sağlayan becerilerinden biri sayesinde oldu. Parantezleri eklememize gerek kalmıyor çünkü derleyici onların nerede olması gerektiğini biliyor. Derleyicinin böyle becerikli olması bir tesadüf değil elbette. Scala DSL tasarımını desteklemeyi amaçlamış. DSL İngilizce Domain Specific Language sözcüklerinin başharfleri. Değişik uğraş alanlarının kendine özgü terimleri var elbet. Onları Scala kullanarak tanımlayıp uzmanlarının kolaylıkla kullanmalarını sağlayabiliyor uzman Scala mühendisleri. Şimdilik bilgin olsun. İlerde örneklerini gördükçe daha anlamlı olacak bu bahsettiklerimiz.".p,
    "Sayılar da herşey gibi birer nesne. Ayrıca matematik işlemleri de özel değil. Onlar da hep bir nesne türü üzerinde tanımlı birer işlev yani metod. Örneğin + metodunu 1 sayısı üzerinde kullanmanın ikinci bir yolu da nesne adı ve sonra nokta koyup metod adını eklemek. Scala için  '+' , '-' , '*' ve '/' gibi matematik işlemlerinin hepsi birer metod:".p,
    "1.+(2)".c,
    "Son kere anımsatalım: Scala'da herşey bir nesne ve herşeyin bir türü (ya da sınıfı) var. Bütün işlevler de bir metod ve hep bir nesne ve türe bağlı".p,
    """Bu arada son yazdığımız satıra dikkat. Scala derleyicisi "1." deyişini "1.0" yani kesirli bir sayı sanmadı. Yoksa, bir çift parantez daha gerekecekti ve şöyle yazacaktık:""".p,
    "(1).+(2)".c,
    """Tabii bu da hala çalışıyor ama böyle yazmak zorunda kalmamamız iyi birşey. Scala'nın daha yaşlı versiyonları bu kadar akıllı değillerdi sanıyorum. Hatta daha hala pekçok eski dil var ki bu konuda kafaları karışık olabiliyor. Bilmende fayda olabilir.""".p
  )
)

pages += Page(
  name = "PMS",
  body =tPage("Örüntü Eşleme ve Switch ve Case Komutları",
    "Örüntü Eşleme".h2,
    "Program akışını değiştirmenin bazı yollarını görmüştük. Bilhassa if/else yapısıyla akış nasıl dallandırılır biliyorsun. Şimdi de switch/case yapısıyla akışı aynı anda birden fazla dala ayırmayı görelim. C ve Java gibi daha eski dillerde de vardır switch/case. Scala bu kavramı daha da geneller ve cebirsel örüntülü eşleme yapmayı sağlar! Bunu böyle anlamak mümkün değil elbet! Gelin birkaç örnekle gizemi çözüverelim. İlk önce geleneksel ve daha basit kullanışıyla başlayalım: ".p,
    """def sayıdanYazıya(s: Sayı){
  s match {
    case 1 => satıryaz("Bir")
    case 2 => satıryaz("İki")
    case 5 => satıryaz("Beş")
    case _ => satıryaz("Hata")
  }
}""".c, 
    "sayıdanYazıya(5)".c,
    """Burada her 'case' satırında kullandığımız '=>' iminin adı kalın ok imi olsun. Bu imden önce gelen örüntü sonra gelen komut dizisi ya da deyişle eşleşiyor. Son örneğimizde 1 sayısı "Bir" yazısıyla eşleşiyor. Son örüntü olarak kullandığımız '_' imi herşey demek. Yani 's' sayısı ne olursa olsun artık eşini buluyor. Bu yapıda kullandığımız 'match' anahtar sözcüğü hemen hemen bütün işlevler gibi bir değere sahip. Onun için son örneği daha da kısa ve öz bir şekilde yazalım:""".p,
    """def sayıdanYazıya(s: Sayı) {
  satıryaz(s match {
    case 1 => "Bir"
    case 2 => "İki"
    case 5 => "Beş"
    case _ => "Hata"
  })
}
""".c,
    "sayıdanYazıya(3)".c,
    "İşin güzel tarafı yukarıdaki eşleşmeyi tam tersine çevirmek de mümkün (Java ve C'deyse yapamazdık bunu):".p,
    """def yazıdanSayıya(y: Yazı) {
  satıryaz(y match {
    case "Bir" => 1
    case "İki" => 2 
    case "Beş" => 5
    case _ => 0
    }
  )
}
""".c, 
    """yazıdanSayıya("Beş")""".c,
    """İngilizce'de açık açık okuyabildiğimiz bir yazıyı sayıya çevirmeye "encoding", tersine de "decoding" derler. Son örnekle encoding, ondan öncekiyle de decoding yapmış olduk. Haberin olsun. Örüntü eşlemeyle bir nesnenin türüne göre de farklı işlemler yapmak kolaylaşır. Hemen bir örnek görelim:""".p,
    """def nedir(a: Her): Yazı = {
    a match {
        case x: Sayı  => "Bir Sayı"
        case x: Yazı  => "Bir Yazı"
        case x: Kesir => "Bir Kesir"
        case _        => "Kim bilir ne türdür?"
    }
}

/* 
 * Aşağıda üç örnek var. Son örnek kesir
 * olsun diye sonuna F koyduk. 
 * Anımsadın mı? Float KısaKesir demek.
 */
satıryaz(nedir("text"), nedir(2), nedir(2F))
""".c,
    "Bakın bu yöntem 'case class' kullanarak yarattığımız türleri işlemekte o kadar faydalıdır ki, bir sonraki bölümü tamamen ona ayırdık.".p
  )
)

pages += Page(
  name = "BTree",
  body =tPage("İleri Eşleme Yöntemleri - İkil Ağaç",
    """Şimdiki örneğimizin adı ikil ağaç (binary tree). Neden böyle deniyor çok yakında anlayacağız. Ağaç diye adlandırdığımız veri yapılarının bilgisayar mühendisliğinde önemi büyük. Çünkü verileri saklamak ve hızlı bulmak için bire birler. İlk önce veri ile ne demek istiyoruz onu görelim. Pek çok tür veri olabilir elbet. Çok yaygın olan anahtar/değer çiftleri bu örneğimizde bizim işimizi görecek. Örneğin ("a" -> 30) "a" anahtarıyla saklayıp sonra da arayıp bulacağımız 30 değeri. Ağacın tasarımında iki gereksinim olacak: 1) ağacın çatallanarak dallanmasını sağlamak, 2) ağacın yapraklarıyla verileri saklamak. Unutmayalım ki arama işleminin çabuk çalışmasını istiyoruz. Bunun için de her seferinde bütün yapraklara bakmak yerine daha verimli bir düzen istiyoruz. Arama işlemini yapacak elbette bir işlevimiz olacak. Ağacı gezerken anahtara göre hangi dala gitmesi faydalı olur nasıl bilebilir bu işlev? Düşündüğünden kolay olacak. Bak gör! Çatalların özelliği iki dala ayrılmaları (onun için ikil dedik!) ve de bir anahtar tutmaları. Diyelim ki sağ daldaki veriler sözlükte hep bu anahtardan önce gelsin. O zaman daha sonra gelen bir anahtar arıyorsak sadece sol dala bakmamız yeter! O sayede arama hızlı çalışacak. Yapraklara gelince, durum daha basit. Her yaprak sadece bir anahtar ve onunla saklanan değeri tutacak (boş yere anahtar/değer çifti demedik).""".p,
    """/*
 *           [b]
 *          /  \
 *        [a]  c,20
 *       /  \
 *     a,30  b,10       
 */     
""".c,                
    "İlk görevimiz çatal ve yaprak türlerini tanımlamak. Şimdi sorun şu. Her çatal iki dala ayrılıyor ama her iki dal da ya başka bir çatal olabilir ya da bir yaprak olabilir! Yani çatalın iki çocuğu olmalı ve bu çocuklar sadece bu iki türden biri olmalılar, ya bir çatal ya da bir yaprak, ama başka da hiç bir tür olmamalılar. Yanlışlıkla bir sayı, yazı ya da dizin olursa başımız derdfe girer (buna type unsafe, yani tür güvensizliği derler ve eski çağlarda bilgisayar programcılarını çok terletmiştir). Scala bu zorluğu sınıf hiyerarşisi dediğimiz yöntemle çözer. İlk önce genel bir ağaç türü ya da sınıfı oluşturalım sonra da buna iki tane alt tür ekleyelim. Bakın nasıl da kolay:".p,
    """class Ağaç
case class Çatal(anahtar: Yazı, sol: Ağaç, sağ: Ağaç) extends Ağaç
case class Yaprak(anahtar: Yazı, değer: Sayı) extends Ağaç
""".c,
    """Çatal ve Yaprak için Ağaç türünün alt türü deriz. Bunu uzatmak anlamına gelen 'extend' özel sözcüğüyle belirliyoruz. Ağaç da üst tür olarak bilinecek bundan sonra. Çatal ve Yaprak alt türlerine ataları olan Ağaç üst türünün bütün özellikleri miras kalır, hem de Ağaç daha ölmeden! :-) Bakın çok ilginç birşey daha göreceğiz hemen şimdi. Bir değerin türü Ağaç olsun diyeceğiz ama onun gerçek değeri bir Yaprak ya da Çatal olabilecek:""".p,
    """val ağaç1: Ağaç = Yaprak("c", 24)""".c,
    "Bu nasıl oluyor? Yaprak Ağaç'ın uzantısıydı ya. O sayede Ağaç deyince genel olarak ya Çatal ya da Yaprak demiş oluyoruz. Bu sayede Çatal'ın tanımındaki sol ve sağ değerlerinin türü neden Ağaç oldu anladık değil mi? Ama tersini yapamayız ona göre. Neden? Çünkü Yaprak dedik mi artık yeterince özelleşmiş oluyor ve anahtar ve değeri olarak bir sayı gerekiyor. Ne Ağaç ne de Çatal'da bir sayı yok, değil mi?".p,
    "Şimdi en başta bahsettiğimiz arama işlevine geldi sıra. Örüntü eşleme değil mi bu kısmın adı?".p,
    "Bakın bu işlev ağacımızı alacak ve özyineleme yöntemiyle verimli arama işlemini gerçekleştirecek. Yani bir çatala geldiğinde kendi kendini sağ ya da sol küçük ağaç ile tekrar çağıracak. Ama eğer bir yaprak görürse elbette yineleme duracak. Örüntü nerede o zaman? Küçük ağaç bir çatal mı yoksa yaprak mı onu belirleyecek örüntülerimiz. Çok lafa gerek yok. Yazılımcık yalın ve kendi kendini anlatıveriyor:".p,
    """def bul(ağaç: Ağaç, anahtar: Yazı): Sayı = {
  ağaç match {
    case Yaprak(a, değer)   => if (a == anahtar) değer else 0
    case Çatal(a, sol, sağ) => bul((if (a >= anahtar) sol else sağ), anahtar)
  }
}

// Bir örnek gerek. İkil ağacımızı şöyle olsun:
val ağaç = Çatal("b", Çatal("a", Yaprak("a", 30), Yaprak("b", 10)), Yaprak("c", 20))
/*
 *           [b]
 *          /  \
 *        [a]  c,20
 *       /  \
 *     a,30  b,10       
 */     
""".c,
    "'case class' sayesinde Çatal ve Yaprak nesnelerini kolayca tanımladık. Haydi şimdi yapalım bir iki arama:".p,
    """bul(ağaç, "a")  // 30""".c,
    """bul(ağaç, "c")  // 20""".c,
    "Bu kadarla kalmaz elbet. Ağaca yeni anahtar/değer çiftleri eklemek için de bir işlev iyi olur. Ha, bir de yaprağı koparmak gerekebilir. Bütün bu işlevleri yeni bir İkilAğaç sınıfı tanımlayıp içine koymaya ne dersin? Onu sana bırakıyorum. Biraz düşün, Kojo'da birşeyler yazıp çiziştir bakalım. Çok daha iyi öğreneceksin o sayede. Kolay gelsin!".p,
    "Bu arada örüntüler yukarda gördüğümüz gibi değişken olmak zorunda değil. Yalın bir değer de kullanabiliriz örüntü olarak. Bir önceki bölümde de görmüştük hani sayıdan yazıya ve tersini yaparken.".p,
    "Bir örnek daha verelim yine de. Diyelim ki (c -> 20) çiftinin bulunmasını istemiyoruz. Nedense. Bakın nasıl kolay:".p,
    """def bul2(ağaç: Ağaç, anahtar: String): Sayı = {
  ağaç match {
    case Çatal(a, sol, sağ) => bul2(if (a >= anahtar) sol else sağ, anahtar)
    case Yaprak("c", _)     => 0
    case Yaprak(a, değer)   => if (a == anahtar) değer else 0
  }
}

bul2(ağaç, "c")  // 0
""".c,
    "Burada yine '_' imini joker gibi kullanarak bütün değerlerle eşleşmesini sağladık. Ayrıca bilelim ki bu eşleştirme işlemi yukarıdan aşağıya sırayla gidiyor. İlk eşleşme ile iş bitiyor. Bariz tabii ama yine de benden söylemesi.".p,
    "Burada örneğini gördüğümüz sınıf hierarşisi ve alt türlerin üst türü uzatması, OOP, yani nesneye yönelik programlamadaki en temel kavramlardan. Örüntü eşleme yöntemi sayesinde alt türleri birbirinden ayırıp gereğini yapabiliyoruz. Ne dahice, değil mi?!".p)
)
pages += Page(
  name = "STI",
  body = tPage("Dingin Türleme ve Tür Çıkarımı",
    """Scala dingin türleme (static typing) yapan bir dil. Bu ne demek? Bütün değişkenlerin, değişmezlerin ve işlevlerin türü, program çalışmadan önce, yani derleme sırasında belirleniyor. Bütün diller böyle değil. Örneğin pek çok yazılımcının favorilerinden olan Python ve Ruby, devingen türlemeli diller (dynamically typed languages). Dingin türleme sayesinde pek çok yazım ve mantık hatası programı çalıştırmadan önce yani derleme sırasında yakalanır. Örneğin, yanlışlıkla bir işleve uygun olmayan tür bir nesneyi girdi olarak sokarsak, derleyici hemen yakalar ve bizden hatayı düzeltmemizi ister. Kısaca, her değerin türünün önceden belli olması hem faydalı hem de gerekli. Ama, buna rağmen yazılımcık örneklerimizde tür tanımlarının pek az olduğunu farketmiş olabilirsin. Bu nasıl oluyor? Çünkü Scala tür çıkarımında çok becerikli. Tür çıkarımı (type inference) bir değerin türünü kullanıldığı yere bakarak belirlemek demek.""".p,

    """Örneğin, 'val x = 3' yazalım. Derleyici elbette 3'ün yalın bir değer olduğunu ve ayrıca ne tür bir sayı olduğunu biliyor. 3 (kesirsiz bir) Sayı. Onun için derleyici 'x' değişmezinin türünün de Sayı olması gerektiği çıkarımında bulunuyor ve elbette yanılmıyor. Az da olsa bir kaç durum var ki derleyici tür çıkarımını beceremiyebiliyor. İşte o zaman, kafam karıştı der ve bir hata verir. Hatanın türü "type ambiguity" gibi birşeylerdir, yani tür belirsizliği. O durumda gerekli tür bilgisini yazılımcığa ekleyiverirsin.""".p,

    "Genelde işlevlerin girdilerinin türünü tanımlarız. Ama çıktının türünü belirtmek gerekmez. Çünkü derleyici işlevin tanımından çıktının türünün çıkarımını yapıverir. Bunun istisnası özyineleyen, yani kendi kendini çağıran işlevler. Pek çok örneğini gördük elbet. Onların çıktısının türünü hep belirttik.".p,

    "Tür çıkarımı sayesinde programların doğru çalışması için yazmamız gereken sözcük ve harf sayısı epey azalır. Bu da elbette çok işimize yarar. Ayrıca yazılım daha okunur hale gelir ve anlaşılması da kolaylaşır. Bu becerisi sayesinde Scala da Python ve Ruby gibi diller gibi sade ve yalın gelir göze.".p

  )
)

pages += Page(
  name = "FAO",
  body = tPage("İşlevler de Birer Nesnedir",
    "İşlevler de Birer Nesnedir".h2,
    """Scala dilinde herşey bir nesnedir demiştik. İşlevler de aynen öyle! Bir işlevi başka işlevlere girdi yapabiliriz. Bir işlevin çıktısı da bir işlev olabilir. Ayrıca değişkenlerin ya da değişmezlerin değeri de bir işlevin kendisi olabilir. Bunların örneklerini biraz sonra göreceğiz. Bu özellikler Scala dilinin çok faydalı bir becerisidir. Karşımıza sık sık çıkan bazı zorlukları çok kısa ve güzel bir şekilde çözmemizi sağlarlar. Bunlar arasında program akışını yönlendirme teknikleri de var. Örneğin, Scala dilinde yazılmış eski adı "Actors" yeni adıyla "Akka" birimi (İngilizce'de module ya da library denen ve başka yazılımlar tarafından kullanılan bir yazılım bütününe birim denir) işlevleri nesne olarak kullanma yöntemini kullanarak eşzamanlı programlamayı destekler. Ama biz dizinlerin kullanılmasıyla başlayalım. Bakın göreceksiniz işlevlerin nesne olarak kullanılmasına güzel bir giriş olacak.""".p,
    """Nesneleri bir dizi olarak ele almak çok doğal ve faydalı, değil mi? Ne tür örnekler geliyor senin aklına? Her sözcük, örneğin, bir dizi harften oluşur. Gün içersinde yaptığımız şeyleri bir dizi eylem olarak düşünebiliriz. Scala dili Dizin adını verdiğimiz bir tür tanımlamıştır. Bunu daha önce pek görmedik. Ama aslında çok basit bir kavram. Dizin türü bir dizi nesneyi ele almayı ve işlemeyi çok kolaylaştırır ve hemen hemen her yazılımcık ve daha büyük yazılımlarda sık sık kullanılır. Dizin içindeki nesnelerin belli bir sırası vardır. Dizin türünün sunduğu pek çok metod ve komut sayesinde Dizin tanımlamak ve işlemek kolaylaşır. Daha önce Nokta sınıfıyla gördüğümüz gibi Dizin oluşturmanın bir yolu Dizin sınıfının yapıcı metodunu (constructor) kullanmak. Bu yapıcı metoda bir ya da daha çok girdi sokarız ve yapıcı o girdilerin oluşturduğu bir Dizin yapıverir. İlk örneğimizle hemen başlayalım. Bir dizi sayı oluşturalım. Bu dizinin türü "Dizin[Sayı]" olacak. Buradaki köşeli parantezler dizinin içindeki elemanların türünü belirliyor, yani Sayı. Her eleman bir Sayı olduğu için Scala tür çıkarımı yaparak 'dzn' değişmezinin türünü Dizin[Sayı] olarak belirler. Yani bizim bunu açık açık yazmak için zahmet etmemiz gerekmez.""".p,
    "val dzn = Dizin(1, 7, 2, 8, 5, 6, 3, 9, 14, 12, 4, 10)".c,
    "Tamam, şimdi elimizde bir dizin var. Dizinleri kullanmanın üç temel metodu var: 'başı, kuyruğu ve ::'. Her dizinin bir başı var. 'başı' metodu bize ilk elemanı verir. 'kuyruğu' metodu, dizinin başı hariç diğer elemanlarından oluşan kısmını verir. Ve son olarak da çift iki nokta üstüste yani '::' bir dizinin başına yeni bir eleman, yani yeni bir baş ekler. Bu baş ve kuyruk deyişi sana yılanları anımsattıysa haklısın. Dizinler yılanlara benziyor: hep başından tutmakta fayda var! Şaka bir yana Dizin türü çok gelişmiştir ve daha pek çok kullanışlı metodu vardır. 'Dizinlerin Kullanılışı' kısmında daha pek çok Dizin metodu göreceğiz.".p,
    "'başı' ilk yani en soldaki elemanı verir. dzn örneğinde bu '1' olacak.".p,
    "satıryaz(dzn.başı)".c,
    "'kuyruğu' başı yani ilk elemanı atlar ve dizinin gerisini verir. Bakın en baştaki 1 sayısı yok kuyrukta:".p,
    "satıryaz(dzn.kuyruğu)".c,
    "'::' soldaki elemanı sağdaki dizinin başına ekleyerek oluşan yeni dizini verir.".p,
    "satıryaz(23 :: dzn)".c,
    "Burada önemli bir gözlem yapalım. Bu metodlar dzn dizisini değiştirmez! Hep yeni bir Dizin üretirler. Bunun için Dizin türüne değişmez (immutable) bir veri yapısı da denir.".p,
    "Bu üç temel metodla aklına gelen her dizini tanımlayabilir ve dizinlerle yapılabilecek ne varsa yapabilirsin. Bir örnek olarak gel bizim örnek dizinimizin içindeki tek sayıları bulalım:".p,
    """def tek(girdi: Dizin[Sayı]): Dizin[Sayı] = {
    if (girdi == Boş) Boş
    else if (girdi.başı % 2 == 1) girdi.başı :: tek(girdi.kuyruğu)
    else tek(girdi.kuyruğu)
}
""".c,
    "Farkettin mi? Bu işlev dizinin başına bakar ve duruma göre kuyrukla özyineler, yani kendi kendini çağırır. Bunu yaparken 'kuyruğu' metodunu kullanır ve bu sayede dizinin elemanlarını teker teker ele alır. 'Boş' özel bir değer ve içi boş olan diziyi belirtiyor. Unutmadan, içi boş olan tek dizi var aynı yegane boş küme gibi. Boş yerine şöyle de yazabilirdik: Dizin[Sayı](). Neyseki Boş tanımlanmış. Daha kısa ve anlaşılır oldu değil mi? Dizinin sonuna gelince özyineleme son bulur ve tek sayılardan oluşan yeni bir Dizin çıkar ortaya.".p,
    "Haydi deneyelim.".p,
    "tek(dzn)".c,
    "Ne kadar yalın bir çözüm, değil mi? Tek yerine çift sayıları bulmak da artık çok kolay:".p,
    """def çift(girdi: Dizin[Sayı]): Dizin[Sayı] = {
    if (girdi == Boş) Boş
    else if (girdi.başı % 2 == 0) girdi.başı :: çift(girdi.kuyruğu)
    else çift(girdi.kuyruğu)
}""".c,
    "çift(dzn)".c,
    "Ama, dur, burada epey bir tekrarlama oldu. Görüyor musun? 'tek' ve 'çift' işlevleri neredeyse aynı. İkisi arasındaki farklılıkları bulalım: Elbette işlevlerin adı farklı. Tek ve çift. Onun dışındaki tek fark eleme için kullandığımız koşul. == iminin sağında 0 ya da 1 var. Bu farkı işlevin içinden çekip çıkarsak ortaya daha genel ve daha sade bir çözüm çıkıverecek! Onun için tek mi, çift mi sorusunu bir girdi olarak düşünelim.".p,
    "Bu eleme koşulunu bir işlev olarak tanımlamak çok basit:".p,
    "def tekMi(s: Sayı): İkil = s % 2 == 1".c,
    "% imini anımsadın umarım. Burada sayıyı ikiye bölüp kalanı buluyor.".p,
    "tekMi(13)".c,
    "tekMi(24)".c,
    "Bu arada tekMi adlı işlevin çıktı türünü 'İkil' diye açık açık belirtmeye gerek yok aslında. Sadece Türkçesini görmen ve anımsaman için yazdım. Neyse, biz bu işlevi şimdi nesne olarak nasıl kullanacağız onu görelim. Bunun için yeni bir işlev tanımlayacağız. Adı elemek sözcüğünün kökü olsun. Elemanları eleyerek bize istediğimiz elemanları versin. Ama neye göre eleyecek? Tabii ki yukarıdaki tekMi işlevi gibi bir işlevi kullanarak! Nasıl kullanacak? Yeni bir girdi olarak! İşte başta da bahsettiğimiz konuya geldik. Bir işlev başka bir işleve girdi olacak şimdi.".p,
    """def ele(girdi: Dizin[Sayı], koşul: (Sayı) => İkil): Dizin[Sayı] = {
    if (girdi == Boş) Boş
    else if (koşul(girdi.başı)) girdi.başı :: ele(girdi.kuyruğu, koşul)
    else ele(girdi.kuyruğu, koşul)
}
""".c,
    "ele(dzn, tekMi)".c,
    """'ele' işlevinin ilk girdisinin adı girdi ve bu bizim sayı dizimiz. İkinci arguman ise eleme koşulumuz, yani yeni tanımladığımız işlev. Türünü nasıl yazdık farkedelim: koşulun türü bir sayıyı girdi olarak alıp bir İkil veren, yani koşul sağlanıyorsa doğru yoksa da yanlış diyen bir işlev. 'ele' adlı metodumuz her işlevi girdi olarak kabul etmez. Sadece Sayı alıp İkil veren işlev türü ile çalışır. 'ele' metodunun içinde de 'koşul' adlı değeri aynı diğer işlevler gibi kullandık. Bu ele adlı işlevi daha önceki tek ve çift işlevleriyle karşılaştırmanı öneririm. Genellemenin nasıl yapıldığını iyice özümsemende fayda var. Bu genelleme günlük hayattaki istisnası bol onun için de bizi sık sık yanıltan genellemelere benzemez. Zararı yok sayılır (ikinci bir girdi girmemiz gerekiyor, yani zahmeti biraz arttı) ama kazancı çok. Birazdan bir kaç örnek daha görünce daha iyi anlayacaksın. Bu arada, eğer çok istersek bu zararı iyice yok edebiliriz. Örneğin, bu işlevi sık sık tek sayıları bulmak için kullanacaksak, şöyle yapalım ki iyice kolaylaylaşsın kullanımı:""".p,
    """def ele(girdi: Dizin[Sayı], koşul: (Sayı) => İkil = tekMi): Dizin[Sayı] = {
    if (girdi == Boş) Boş
    else if (koşul(girdi.başı)) girdi.başı :: ele(girdi.kuyruğu, koşul)
    else ele(girdi.kuyruğu, koşul)
}
""".c,
    """Bak bu sayede, eğer işlev girdisi vermezsek, ele adlı işlev tekMi koşulunu kullanacak. Yani varsayılan koşul tekMi ile ifade ettiğimiz tek sayı olmak olacak. Yeni bir örnek iyi olacak:""".p,
    """val ilkOnSayı = Aralık(1, 11).dizine""".c,
    "Daha önce görmediğimiz ama çok faydalı olan bu Aralık türünün İngilizce adı 'Range'. Bir Aralık yazmak yerine kısa bir yolu var. Şöyle:".p,
    """val ilkOnSayı = (1 |- 11).dizine""".c,    
    """İngilizce olarak '1 until 11' yazılır. Ya da onun yerine '1 |-| 10', İngilizce'si: '1 to 10' da yazabilirdik. Bunların daha genel hali de var: 'a |- b adım c' ve 'a until b by c' veya 'a |-| b adım c' ve 'a to b by c'' yani a sayısından b sayısına kadar c adımlarıyla sayıyoruz ama '|-' ya da 'until' olursa b sayısından hemen önce duruyoruz. Örneğin:""".p,
    "3 |- 22 adım 3".c,
    "Uzun ve işlev haliyle:".p, "Aralık(3, 22, 3)".c,
    "Geri geri de gidebiliriz:".p, "30 |-| -35 adım -5".c,
    """Aralık(30, -36, -5)
Aralık.kapalı(30, -35, -5)""".c,
    "Aralığın sonuna dizine metodunu ekle bakalım ne olacak. İngilizcesi toList. Aralıkları araya sıkıştırdık, umarım kafa karıştırmadık. Hemen konumuza dönelım. Nerede kalmıştık? Bir sayı dizisı içinden herhangi bir koşula göre bazı sayıları seçmek istiyorduk. Ama tek sayıları seçmek varsayılan koşulumuz olsun dedik. Deneyelim hemen:".p,
    """ele(ilkOnSayı)""".c,
    """Aynı 'tek' adını verdiğimiz işlev gibi çalıştı, değil mi? Ama, ele adlı işlevimiz hala daha genel. İstersek ikinci arguman olarak başka bir koşul girer, başka tür bir eleme yaparız. Hemen geliyor bir örnek!""".p,
    "Çift sayıları bulmak için de benzer bir yöntem kullanabiliriz elbet. Yani çiftMi diye yeni bir işlev tanımlar ve sonra kullanırız. Ama adsız (anonymous) bir işlev kullanarak çok daha kısaca ifade edebiliriz dileğimizi. Ne demek adsız? Yani 'def' özel sözcüğünü kullanmadan ve işlevin adını vermeden bir işlev tanımlayacağız. Bu işlev basitçe girdiyi alacak ve onu değerlendirecek. Bu özel yöntemde '=>' imini kullanıyoruz. Solunda girdiler, sağında da işlevin eylemleri gelecek.".p,
    "ele(dzn, (v: Sayı) => v % 2 == 0)".c,
    "'ele' komutunu böyle çağırarak çift sayıları buluverdik. Yeni bir çiftMi işlevi tanımlamamıza gerek kalmadı. Adsız işlevler hep böyle kullanılır ve işimizi çok kolaylaştırırlar. O kadar faydalılar ki özel bir adları ve matematik ve bilgisayar biliminde özel bir yerleri bile var. 'lambda kalkülüsü' (lambda calculus) diye bilinir. Bilgisayar bilimi okumak istersen ilerde görür ve daha iyi öğrenirsin.".p,
    "Genelleyici Programlamanın Tadına Bakalım".h3,
    "Şimdi diyelim ki yeni bir eleme koşulu tanımlamak istiyorsun, çünkü tam sayılarla yetinmek olmaz, kesirli sayıları da herhangi bir koşula göre eleyebilmek istiyorsun. Yukarıda 'def' ile tanımladığımız kısacık 'ele' işlevini kopyalar ve basitçe her gördüğün 'Sayı' sözcüğünü 'Kesir' sözcüğüyle değiştirirsin olur biter. Sonra da adını eleKesir gibi birşey koyarsın. Tabii Kesir de yetmez. Başka her hangi bir tür için eleHerneyse dersin ve bu sayede birbirine çok benzeyen, daha doğrusu neredeyse apaynı bir sürü işlevin olur: ele, eleKesir, eleYazı, vb.. Ne güzel, değil mi? Hiç de güzel değil! Çok tekrar oldu. Nerede çokluk orada çok ayıp...".p,

    "Scala'yla yazarken bu kadar tekrara, bu zahmete girmeye hiç gerek yok. İşi iyi bilenlerin yaptığı gibi bir 'tür değişkeni' (type variable ya da generic type diye de bilinir) kullanarak genel bir çözüm sayesinde işler kolaylaşıverir. Tür değişkenleri diğer 'val' değişmezlerine benzer. Gerçek değer yerine değişkenin adını kullanırız her yerde. Sonra Scala derleyicisi gerekli yerleştirmeleri yapıverir".p,

    "Aşağıda 'T' harfini kullanacağız Tür anlamında. Ama başka her hangi bir harf ya da sözcük de olur elbette. Nerede Sayı varsa onun yerine T gelecek. Bir de işlevin adının hemen arkasında [T] gelecek. Bu da yeni işlevimizin genelleyici bir işlev olduğunu belirtiyor. Peki o halde yeni halini görelim:".p,

    """def ele[T](girdi: Dizin[T], koşul: (T) => İkil): Dizin[T] = {
    if (girdi == Boş) Boş
    else if (koşul(girdi.başı)) girdi.başı :: ele(girdi.kuyruğu, koşul)
    else ele(girdi.kuyruğu, koşul)
}
""".c,
    "ele(dzn, (v: Sayı) => v % 2 == 0)".c,
    "Aynı eskisi gibi çalıştı! Ama işin iyi tarafı çok daha genel oldu bu haliyle. Bak şimdi kesirli sayılar arasından sadece beşten büyük olanları seçmek de çok kolay olacak:".p,
    """val kesirler = Dizin(1.5, 7.4, 2.3, 8.1, 5.6, 6.2, 3.5, 9.2, 14.6, 12.91, 4.23, 10.04)""".c,
    """ele(kesirler, (v: Kesir) => v > 5)""".c,
    "Ya da bir cümle içindeki uzunca sözcükleri seçmek için de kullanabiliriz aynı genellenmiş ele adlı işlevimizi:".p,
    """val dizinler = Dizin("Bugün", "sizi", "gördüğüme", "çok", "memnun", "oldum", "Nasılsınız")""".c,
    """ele(dizinler, (v: Yazı) => v.boyu > 4)""".c,
    "Yazı türünün 'boyu' adlı metodu yazıda kaç harf olduğunu gösterir. Dizinlerle ilgili bölümde de göreceğimiz gibi Dizin türünün 'ele' adlı bir metodu var. Onu kullanarak da benzer eleme işlemlerini yapabiliriz:".p,
    "dizinler.ele((v: Yazı) => v.boyu < 5)".c,
    "Bu genelleyici işlevlere daha sonra yine bakacağız ve başka özelliklerini keşfedeceğiz. Ama şimdilik konumuza dönelim ve işlevleri nesne olarak kullanmaya bakalım.".p,
    "'İşlevler De Birer Nesnedir' Hakkında Başka Birkaç Şey Daha".h3,
    "Biraz önce örneklerini gördüğümüz yönteme 'kapsamalar' (comprehensions) deniyor. Yani, dizin gibi bir sürü elemandan oluşan bir kümenin bütün elemanlarına bir işlevi uygulayıvermek. Kapsamaların ne kadar becerikli olduklarını ve onlar sayesinde kısacık ama çok iş beceren yazılımlar yapabildiğimizi bir kaç örnek daha görerek pekiştireceğiz şimdi.".p,

    "Yazılımcıklar yazdıkça, bazı çok basit ve çoğunlukla adı olmayan işlevleri başka işlevlere girdi yapmak isteyeceksin. O durumlarda bazı kısa yollar bilmende fayda olacak. Daha önce de üzerinde durduğumuz gibi tür çıkarımı bize fayda sağlıyor. Bakın bir önceki dizinler.ele örneğini tür çıkarımı sayesinde sadeleştirelim. Derleyici adsız işlevin girdisinin Yazı türünde olması gerektiğini biliyor (nasıl biliyor? Adı dizinler olan val değerinin türünden!). O sayede parantezleri ve tür bilgisini yazmaktan kurtuluyoruz.".p,
    "dizinler.ele(v => v.boyu > 3)".c,
    "Bu adsız işlev gibi iki ve ya bir girdili işlevler çok yaygın olduğu için, Scala dili bize bir kısa yol daha sunuyor. '(x, y) => x + y' adsız işlevini şöyle yazabiliyoruz: '_ + _'. Benzer şekilde 's=>s.Method' yerine '_.Method' da yazabiliriz. _ imi girdi yerine kullanılıyor yani. O sayede adsız işlevler içinde isimler uydurmak ve kullanmaktan kurtuluyoruz. Bu sayede, son örneğin en kısa halini görelim:".p,
    "dizinler.ele(_.boyu > 3)".c,
    "Başkalarının yazdığı kodları okumak çok faydalıdır. O durumlarda burada gördüğümüz kısa yolları görünce artık şaşırmazsın. Ama bazı durumlarda uzun ve açık hallerini kullanmak gerekir. Onları da sonra göreceğiz.".p,
    
    "Bir kaç tane daha yaygın kullanımı olan örnekler görelim. Bunlar dizinleri işlemekte çok faydalı olurlar. Sen de yakında göreceksin.".p,
    "düzİşle (flatMap)".h4,
    "dizinler.düzİşle(_.dizine)".c,
    "Flat İngilizce'de kat kat olmayan yani düz anlamında kullanılır. Bu örnekte bizim yazı dizinimizi aldık, içindeki her bir yazıyı ilk önce 'dizine' metodunu kullanarak birer harf dizisine çevirdik ve onlarin hepsini birleştirdik. Sonucunu gördün, değil mi? 'düzİşle' yerine 'işle' (map) metodunu kullanarak aradaki farkı daha iyi anlarsın. map İngilizce'de hem harita hem de eşlemek anlamlarına gelir. Ama matematikte ve bilgisayar biliminde yeni bir anlam kazanmıştır. Bir çerçeveden başka bir çerçeveye geçiş, birinci çerçeveyi işleyerek ikinci çerçeveye varış gibi anlamlara gelir. Ama anlatması zor. Nasıl çalıştığını görüp anlayıverelim:".p,
    "dizinler.işle(_.dizine)".c,
    "sıralamak (sort)".h4,
    "Sort da sıraya dizmek anlamına gelir. sırayaSok (sortWith) yöntemiyle kendi girdiğimiz bir koşul ile sıralıyoruz. Sözcükleri a'dan z'ye sıralayalım:".p,
    "dizinler.sırayaSok(_ < _)".c,
    "Bir de ters sıraya sokalım:".p,
    "dizinler.sırayaSok(_ > _)".c,
    "Farkettiysen büyük harfliler başta geliyor. Onun yerine harflerin büyük küçük olduğuna bakmadan sıralamak istersek şöyle yaparız:".p,
    "dizinler.sırayaSok(_.büyükHarfe < _.büyükHarfe)".c,
    "Yani hepsini büyük harfe çeviriverdik karşılaştırmadan önce. İştediğimiz koşulu girerek istediğimiz şekilde sıralama yapıverdik. Yani sıralama metodunu baştan yazmamız gerekmedi. İşte işlevlerin nesne olmasının faydaları!".p,
    "katlama (fold)".h4,
    "Soldan ve sağdan katlama bir dizinin elemanlarını bir araya getirmekte kullanılan çok yaygın ve faydalı metodlardır. Elemanları nasıl bir araya getirmek istediğimizi iki girdi alan bir işlev girerek belirtiriz. Bu birleştirme işlemi soldan ya da sağdan başlar. Ve başlarken de yine girdiğimiz bir değer kullanır. Yani iki tane girdisi var bu katlama metodlarının.".p,
    "Bu iki girdiyi daha kolay okunsun diye iki parantez grubuyla gireriz. Birazdan bunu 'def' ile işlevi tanımlarken nasıl yapıldığını göreceğiz.".p,
    "Yeni bir örnekle başlayalım.".p,
    """val dzn = Dizin(1, 7, 2, 8, 5, 6, 3, 9, 14, 12, 4, 10)""".c,
    """dzn.soldanKatla(0)(_ + _)""".c,
    "Soldan işleyeceğiz. İkinci girdimiz adsız bir işlev ve iki sayıyı topluyor: '_+_'. Bunun kapsama olduğunu da biliyoruz, yani dizinin içindeki bütün elemanların üstünden geçecek. İlk önce ilk girdisi olan 0 ile dizinin başındaki 1'i toplar ve 1 bulur. Sonra ona ikinci eleman olan 7'yi ekler ve böylece sonuna kadar gider.".p,
    "Tekrarlardan Kurtulalım".h4,
    "Diyelim ki bir cümlemiz var ve içinde hangi harfleri kullandığımızı bulmak istiyoruz. Örneğin,".p,
    """val dilek = Dizin("Haydi", "gelin", "bir", "oyun", "oynayalım", "hep", "beraber")""".c,
    """dilek.düzİşle(_.dizine).işle(_.büyükHarfe).yinelemesiz.sırayaSok(_ < _)""".c,
    "'düzİşle' sözcüklerin bütün harflerini tek bir dizine sokuyor. Daha önce de gördüğümüz 'işle' harfleri büyük harfe çeviriyor. 'yinelemesiz' yöntemi sadece farklı harfleri veriyor, tekrarları vermiyor. En sonunda da a'dan z'ye sıralıyoruz.".p,
    
    "Kendi akış yöntemimizi tanımlayalım".h4,
    "İşlevlere girdi olarak adlı ya da adsız başka işlevler girebildiğimiz gibi bir dizi komut da girebiliriz. Bu sayede kendi akış yöntemlerimizi yaratabiliriz. Örneğin, bir kare çizmek için kaplumbağa komutlarını yineleyebilmek iyi olur, değil mi? Daha önce gördüğümüz 'for' yapısını kullanabiliriz:".p,
    """sil
for (i <- 1 |-| 4) { ileri(100); sağ() }
""".c,
    "Ama, aşağıdaki gibi yazabilsek çok daha iyi olmaz mı?".p,
    "yinele(4) { ileri(115); sağ() }".c,
    "Scala dili 'if/else' ve 'while' gibi yapılar sunar ama 'yinele' yapısı Kojo tarafından tanımlanmıştır. Bunu sen de Scala ile yapabilirsin. Şöyle başlayalım:".p,
    """def yinele2(ys: Sayı, dk: => Her) {
    for (i <- 1 |-| ys) dk
}
""".c,
    "ys yineleme sayısının kısaltması. dk de dizi komut anlamında.".p,
    "Şu ana kadar işlevlere girdiğimiz bütün girdiler değer olarak yollandı (pass by value denir İngilizce). Yani işlev çağrılmadan önce değer hesaplanır ve işleve yollanır. yinele2 tanımında kullandığımız kalın ok yani => imi durumu değiştiriyor. dk yani dizi komut olduğu gibi (call by name denir) yollanıyor yinele2 işlevine. Yani dizi komutların değerlendirilmesi geciktirilir. Ancak işlevin işleme sırası geldiğinde işlevin içine girilince dizi komut çalıştırılır. Bu yinele2 işlevimizde for döngüsü ys sayısı kadar çalışacak ve her seferinde dk yani girilen dizi komut çalıştırılacak.".p,
    "yinele2(4, { ileri(130); sağ() })".c,
    "Hiç fena değil. Neredeyse istediğimiz hale geldi. Tek sorun 4 sayısından sonra gelen virgül. Scala istersek her girdiyi kendi parantezi içinde girmemizi destekler. Buna 'curried işlev' de denir. Haskell Curry adında bir matematikçi buna esin kaynağı olduğu için. Bakın şöyle değiştirelim tanımı:".p,
    """def yinele3(ys: Sayı)(dk: => Her) {
    for (i <- 1 |-| ys) dk
}
""".c,
    "Bu sayede tam istediğimiz gibi yazabiliriz:".p,
    "yinele3(4) { ileri(160); sağ() }".c,
    "Biraz önce soldanKatla örneğinde de bu tekniği kullanmıştık. Daha okunur hale geldi değil mi?".p,
    "Yazılıma hobi ya da işin olarak devam edersen işlevlerin nesne gibi kullanılabilmesinin daha pek çok programlama problemini çözmede faydalı olduğunu göreceksin. Başka yolları da var elbet ama buradaki örneklerde de gördüğümüz gibi bu teknikle kodumuz epey kısa ve okunur hale geldi. Daha kapsamlı ve tasarlaması, yazması ve idare etmesi esaslı bir iş olan bazı örnekleri de şimdiden duymuş olman için kısa bir liste vereyim. Merak ettikçe, yeri geldikçe incelersin. Kusura bakma bunlar önce İngilizce olsun: (1) passing callback functions in event driven IO, (2) passing tasks to Akka Actors in concurrent processing environments, (3) in scheduling work loads. Yani, (1) olgu güdümlü girdi/çıktı idaresinde geriçağırım yapan işlevleri dağıtmak (2) eşzamanlı işlemler olan ortamlarda, yani aynı anda birden fazla işlemin paralel yani yanyana çalışması durumunda bağımsız aktörlere yeni işlevler verilmesi, (3) uzun işlerin sıralamasını düzenlerken işlevleri dağıtmak. Bilgisayar bilimi ve mühendisliği artık o kadar gerekli bir hale geldi ki, bu temeli iyi bilmek ilerde çok helal kazanç kazanmana faydalı olacaktır. Tabii eğer gönlünü bu işe verirsen.".p,
    "Bir Tür Girdiyi Yinelemek".h4,
    "Bazen işlevimize kaç tane girdi gireceğini bilemeyiz. Daha doğrusu kaç tane girilirse girilsin çalışmasını isteriz. Örneğin bu kılavuzcukta çok kullandığımız 'satıryaz' komutu öyledir. Bir, iki, üç... ne sayıda girdi girersek girelim işini yapar. Bunun yolu da çok kolay:".p,
    """def topla(s: Sayı*) = s.indirge(_ + _)""".c,
    """topla(1, 2, 3)""".c,
    """topla(4, 5, 6, 7, 8, 9, 10)""".c,
    "Girdinin türünden sonra gelen yıldız imi, yani '*' sayesinde 's' girdisi tek bir sayı değil bir dizi sayı oluveriyor. Onun için de 'indirge' (ingilizcesi reduce) metodunu kullandık. Bu soldanKatla metoduna benzer ama daha basittir. Bakın girilen bütün sayıları toplamak bu kadar kolay. Bazı istisnalara da bakalım, ne olacak?".p,
    """topla(99) // pek toplamaya gerek olmasa da yine de tek değerle de çalışması güzel!""".c,
    """topla() // Bak ne oldu? Bunu onarabilir misin?""".c,
    "Bu yıldızlı girdiden önce yıldızsız yani normal girdiler de tanımlayabiliriz. Ama yıldızlı yani yinelenen girdi en son gelmelidir.".p
  )
)

pages += Page(
  name = "Tup",
  body = tPage("Sıralamalar (Tuple)",
    "Sıralamalar (Tuple)".h3,
    "Diyelim ki bir işlevimiz var ve birden çok çıktısı olsun istiyoruz. Ya da bir koleksiyon oluşturacağız ama her elemanı birden fazla değer tutsun istiyoruz. Sözle anlamak zor, sabır, hemen örnek vereceğiz aşagıda. Bu tür durumlarda yeni bir tür tanımlamak zor olmaz elbet. Yeni bir sınıf nasıl oluşturulur gördük. Ama biraz zahmetli elbette. Epey kod yazmak gerekebiliyor, 'case class' bile olsa. Sadece kod yazmak değil esas zorluk anlamlı isimler bulmak ve onları uzun uzun kullanmak aslında. Gerçekten gerekmedikçe yeni adlar üretmek zorunda kalmasak çok iyi olur, değil mi? Uzun lafın kısası, Scala dili, adsız değerleri gerektiği anda ve gerektiği yerde sıralamamıza izin vererek bu sorunu güzelce çözer. Sıralama dedik ya, İngilizce'si 'tuple' parantez içinde virgülle ayrılan bir ya da daha çok değerin bir araya gelmesinden ibaret. İşin güzel tarafı değerlerin türleri aynı olmak zorunda değil. Örneğin:".p,
    "(3, 'c')".c,
    """(3.14, "pi sayısının yaklaşık değeri, 22/7 olarak da bilinir")""".c,
    """(22/7, 22/7.0, 3.14, "pi")""".c,
    """Sıralamalar da birer nesnedir desem artık şaşırmazsın değil mi? Onun için nokta koyup arkasından içinden istediğimiz değere ulaşabiliriz. Ama bir sorun var. Bu iç değerlerin bir adı yok! Sorun değil. Sıralarını biliyoruz. Birinci elemanı okumak için "._1" dememiz yeter. İkinci için "._2" varsa üçüncü için "._3" vb... Hemen deneyelim.""".p,
    """satıryaz((3, 'c')._1)
satıryaz((3, 'c')._2)""".c,
    "Böyle birşey yapmak gereksiz geldi mi size de? Tabii genelde elimizde val değişmezleri olur. Örneğin:".p,
    """val sıralıDörtlü = ("En sevdiğim sayı", 5, "karesi", 25)
""".c,
    "sıralıDörtlü._1".c,
    "sıralıDörtlü._2".c,
    "sıralıDörtlü._3".c,
    "sıralıDörtlü._4".c,
    "sıralıDörtlü._5".c,    
    """Biraz fazla gittik! Hata verdi. "error: value _5 is not a member of (String, Int, String, Int)" yani 5. elemanı yok ki sıralamanın diyor bizim akıllı derleyici.""".p,
    "Bunları bilmekte fayda var ama esas sıralamaların içindeki değerleri toptan çözümlemeyle (tuple deconstruction) okumak çok daha yaygın ve faydalıdır. Örneğin:".p,
    "val (i, c) = (3, 'a')".c,
    "Birazdan bir yazılımcık yazacağız ve onun içinde bu çözümleme yöntemi nasıl işe yarıyor göreceğiz. Diyelim ki bir yazının içinde hangi harften kaçtane var bulmak istiyoruz. İlk önce bir dizi sözcük oluşturalım bir tümceden:".p,
    """val yazı = "Kojo ile oyun oynayarak Scala dilini öğrenmek ve hatta işlevsel ve nesneye yönelik programlama becerisi edinmek harika değil mi """".c,
    """val sözcükDizini = yazı.böl(" ")""".c,
    "Bu hesaplamayı yapmak için uygulamayı düşündüğümüz yöntem şu: ilk önce harfleri büyültelim sonra da sıraya sokalım.".p,
    "Ondan sonra da katlama (fold) metodunu kullanarak arka arkaya tekrar eden harfleri sayıvereceğiz. Daha önce ne görmüştük? Katlama işlevi iki girdi alıyor: bir başlangıç değeri, bir de bir dizin, yani bizim sıraya sokulmuş harf dizimiz. Katlama ilk başlangıç değerini dizinin ilk elemanıyla bir işleme sokacak. Ne işlemi mi? Biz ne istersek o! Burada teker teker ele aldığımız harfler değişmedikçe sayısını bir artıracağımız bir sayacımız olacak. Yeni gelen harf değişik olursa yeni bir sayaç tanımlayacağız. Her sayaç tabii ki birden başlayacak. Karmaşık mı geldi biraz? Çok doğal. Görüp biraz üstünde düşününce daha anlaşılır olacak. Ne de olsa ileri programlama tekniği bu!".p,
    "Kısaca söylemek gerekirse amacımız ikili sıralamalardan oluşan bir dizin oluşturmak. Her ikilinin birinci alanında bir harf ikinci alanında da kaç tane olduğunu tutan sayacı olacak. Bu dizin harflerin sıklığını sunacak bize.".p,
"""// Tek satırda epey iş var. Teker teker bak istersen
val harfler = sözcükDizini.düzİşle(_.dizine).işle(_.büyükHarfe).sırayaSok(_ < _)
// Yani şöyle yapabilirsin:
val g1 = sözcükDizini.düzİşle(_.dizine); satıryaz("g1", g1)
val g2 = g1.işle(_.büyükHarfe); satıryaz("g2", g2)
""".c,

    """Şimdi de katlama yöntemimiz gelsin bakalım. Başladığımızda sunum boş olacak elbet. Nasıl tanımlarız istediğimiz boş dizini? "Dizin[(Harf, Sayı)]()" yani bir dizi (harf, sayaç) çifti. soldanKatla metodumuz ikinci girdi olarak ne bekler anımsadın mı? Bir işlev! Nasıl bir işlev gerekiyor biraz daha iyi tahmin edebilirsin belki şimdi. Adsız işlev olacak, bir. İki tane girdisi olacak, iki. İlk girdisi bizim çift dizinimiz, ikinci girdi de harflerden biri.""".p,

"""val sıklık = harfler.soldanKatla(Dizin[(Harf, Sayı)]()) {
    case ((önceki, sayaç) :: kuyruk, harf) if (önceki == harf) => (önceki, sayaç + 1) :: kuyruk
    case (sunum, harf)                                         => (harf, 1) :: sunum
}
""".c,
    "Bunu anlayamadım diye üzülme sakın! Daha önce görmediğimiz bir kaç becerisi var Scala derleyicisinin burada!".p,

    "1) adsız işlevimizi tanımlarken 'case' yani örüntü eşleme yapısı kullanabiliriz. Bunun için normal parantez yerine kıvrık parantez kullanmamız yeter. 'match' özel sözcüğüne gerek kalmadı. Ondan önce gelen değişmezlere de! Yani bu epey faydalı bir kısa yol oluyor ve bunu iyi bilmekte fayda var! Normal, yani kısaltılmamış halini anımsayalım hemen:".p,
    "(a, b) => (a, b) match {case ... => ...; case ... => ...}".p,
    "match ve ondan önceki hiç birşeye gerek kalmıyor!".p,

    "2) örüntülü eşleme yapmak için kullanmıştık bu 'case' yöntemini. Burada da 'case' sözcüğünden hemen sonra gelen kısımda elimizdeki iki girdiyi çözümlüyoruz. Biraz önce de dediğimiz gibi ilk girdi ufak ufak oluşturduğumuz yeni dizinimiz. '(önceki, sayaç) :: kuyruk' yeni kurduğumuz dizinin başı ve kuyruğuyla eşleşiyor ve onların üçüne de isim takıveriyor. kuyruk bariz. baş eleman da bir önceki harf ve ondan şu ana kadar kaç tane saydığımızı tutan sayaç. 'kuyruk' değerinden sonra gelen 'harf' ise katlama işlemini yaptığımız 'harfler' dizinindeki harflerden biri. Katlama işlevi her harfin üstünden teker teker geçecek elbet.".p,

    "3) ve son! İlk örüntü eşleme satırında bir de koşul girdik 'if' diyerek. Bu çok önemli. Yeni bir harfe geçip geçmediğimize dikkat etmemiz gerek! Eğer en son saydığımız harften aynısı geldiyse sayaçı arttırmalıyız. Yoksa yeni bir sayaç başlatmalı.".p,

    "Şimdi, bilgisayın Scala derleyicisi sayesinde anladığı bu epey karmaşık görünen işlemi okuyalım bakalım daha iyi anlamış mıyız: esas girdimiz olan sıralanmış harfler dizisindeki her harf için teker teker şunu yapalım: eğer sıfırdan oluşturduğumuz sunum adlı yeni çift dizisinin başındaki çiftin harfi ile aynıysa, dizinin başını sayaçın bir arttığı yeni bir başla değiştirelim. Yoksa, sunum dizisine elimizdeki harf icin yeni bir baş ekleyelim ve harfin sayacını bire eşitleyelim. Çok da karmaşık değilmiş, öyle mi?".p,

    "İki üç satırda ifade edebildiğimiz bu işlem hem epey düzgün hem de epey etkileyici bence. Ya sen ne dersin?".p,

    "Sonucu okunur bir halde yazalım bakalım ne bulduk:".p,

    """satıryaz(sıklık.işle{p => s"${p._1}:${p._2}"}.yazıYap(" "))""".c,

    "Elbette ki isteyen daha eski dillerde yapıldığı gibi yazabilir, Basic, Fortran, C, C++, Java örneğin. Yani:".p,

"""def harfSıklığı(girdi: Dizin[Harf]): Dizin[(Harf, Sayı)] = {
    var sunum = Dizin[(Harf, Sayı)]()
    if (girdi.boşMu) sunum
    else {
        var önceki = girdi.başı
        var kuyruk = girdi.kuyruğu
        var sayaç = 1
        while (!kuyruk.boşMu) {
            if (kuyruk.başı == önceki) sayaç += 1
            else {
                sunum = (önceki, sayaç) :: sunum
                sayaç = 1
                önceki = kuyruk.başı
            }
            kuyruk = kuyruk.kuyruğu
        }
        (önceki, sayaç) :: sunum
    }
}

val yazı = "Kojo ile oyun oynayarak Scala dilini öğrenmek ve hatta işlevsel ve nesneye yönelik programlama becerisi edinmek harika değil mi "
val sözcükDizini = yazı.böl(" ")
val harfler = sözcükDizini.düzİşle(_.dizine).işle(_.büyükHarfe).sırayaSok(_ < _)
satıryaz(harfSıklığı(harfler))""".c,

    "Bu da çalıştı elbette. Ama bilmem sen hangisini daha çok beğendin. Bak biraz önce işlevsel programlama esaslarını kullanarak nasıl yaptığımızı hepsi bir arada tekrar görelim ve iki farklı yöntemi yan yana karşılaştıralım:".p,
    """val harfSıklığı = "Kojo ile oyun oynayarak Scala dilini öğrenmek ve hatta işlevsel ve nesneye yönelik programlama becerisi edinmek harika değil mi".
    böl(" ").düzİşle(_.dizine).işle(_.büyükHarfe).sırayaSok(_ < _).
    soldanKatla(Dizin[(Harf, Sayı)]()) {
        case ((önceki, sayaç) :: kuyruk, harf) if (önceki == harf) => (önceki, sayaç + 1) :: kuyruk
        case (sunum, harf)                                         => (harf, 1) :: sunum
    }

satıryaz(harfSıklığı.
    sırala(p => p._2).tersi.
    işle { p => s"${p._1}:${p._2}" }.
    yazıYap(" "))
""".c,
    "Bilmem farkettin mi ama sadece iki komut dizisi yeterli oldu. Yerilen cümlenin harflerinin sıklığını hesapladık ve sıraya sokup yazdırdık. Yukarıda adlarını saydığımız daha eski ve daha az becerikli dillerle bilgisayara istediğini yaptırmanın ne kadar zor olduğunu bilenlere bu kısacık yazılım o kadar etkileyici olur ki anlatamam!".p

  )
)

pages += Page(
  name = "MF",
  body = tPage("Matematiksel İşlevler",
    "Matematik"h3,
    "Matematik fonksiyonları bazen işimize çok yararlar. Birkaç tanesini görelim.".p,
    "Değişmez (sabit) Değerler".h3,
    "Matematik sınıfında iki çok meşhur sabit var:".p,
    table(
      row("eSayısı".c,"e, meşhur matematikçi Euler'den gelir adı 2.718282... değerindedir, doğal logaritmanın da tabanıdır."),
      row("piSayısı".c,"pi sayısı, 3.14159265 .... yani yarıçapı 1 olan dairenin çevre uzunluğunun yarısıdır.")
    ),
    "Trigonometri fonksiyonları".h3,
    "Trigonometrik işlevler girdi olarak radyan birimi kullanırlar. Radyan kavramını anlatan çok güzel bir örneğimiz var Kojo'da. Örnekler menüsünde en altta Matematik Öğrenme Birimleri menüsü var. Onun en altında 'Açı Nedir?' var. Ona tıklayıver. Günlük hayatta biz 90 derece, 180 derece gibi bize daha doğal gelen derece birimini kullanırız açıları ifade etmek için. Radyandan dereceye çevirmek için 'dereceye' metodunu, tersini yapmak için de 'radyana' metodunu kullanabiliriz. Bilmemiz gereken tek şey şu: 2*pi radyan 360 dereceye eşittir. Aşağıda sıraladığımız metodlardan başka yay metodları da var.".p,
    "Kısa not: aşağıdaki tanımlarda G1, G2, ... ile fonksiyona girilen değerleri ifade ediyoruz kısaca. Yani işlevAdı(G1, G2, G3, ...). ".p,
    table(
      row("sinüs(piSayısı/6)".c,"G1 girdisinin yani burada Pi/6 değerinin sinüsü."),
      row("kosinüs(piSayısı/6)".c,"G1 girdisinin kosinüsü."),
      row("tanjant(piSayısı/6)".c,"G1 girdisinin tanjantı."),
      row("radyana(45)".c,"G1 (derece olsun) girdisini radyana çevirir."),
      row("45.radyana".c,"Kesirlerin 'radyana' yöntemini de kullanabiliriz."),
      row("dereceye(piSayısı/2)".c,"G1 (radyan olsun) girdisini dereceye çevirir."),
      row("piSayısı.dereceye".c,"Bir değişik yöntem de bu aynı radyana yöntemi gibi."),
    ),
    "Üst bulma metodları".h3,
    "Logaritma ve üst bulmak için iki temel işlev var. İkisi de e tabanını kullanır (e sayısı)".p,
    table(
      row("eüssü(piSayısı)".c,"e (2.71...) sayısının G1 üssünü hesaplar."),
      row("gücü(6, 3)".c,"G1 girdisinin G2 üssünü bulur."),
      row("logaritması(10)".c,"G1'in E tabanina göre logaritmasını verir.")
    ),
    "Başka bazı fonksiyonlar".h3,
    table(
      row("karekökü(225)".c,"G1'in karekökü."),
      row("mutlakDeğer(-7)".c,"Mutlak değeri verir. Girdinin türü neyse, çıktı da aynı tür sayı olur, Sayı, Uzun, KısaKesir, Kesir."),
      row("enİrisi(8,3)".c,"G1 ve G2 arasında büyük olanı bulur."),
      row("enUfağı(8,3)".c,"G1 ve G2 arasında küçük olanı bulur.")
    ),
    "Sayı türüyle ilgili metodlar".h3,
    "Bu işlevler kesirli sayıları tam yani kesirsiz sayıya çevirir. Ama dikkat, çıktı türü hala Kesir olabilir.".p,
    table(
      row("taban(3.12)".c,"G1 girdisinden küçük ya da ona eşit olan en büyük tam sayıyı hesabeder."),
      row("tavan(3.12)".c,"G1 girdisinden büyük ya da ona eşit olan en küçük tam sayıyı verir."),
      row("yakını(3.51)".c,"G1 değerine en yakın tam sayıyı Kesir türünde bir çıktı olarak verir."),
      row("yakın(3.48)".c,"G1 kesirine en yakın sayıyı Uzun türünde çıktı olarak verir."),
      row("yakın(2.6F)".c,"G1 KısaKesir'ine en yakın sayıyı Sayı türünde çıktı olarak verir.")
    ),
    "Rastgele (Random) sayılar, rastgele başka şeyler...".h3,
    table(
      row("rasgele".c,"0.0 ile 1.0 arasında rastgele bir sayı verir. Çıktı türü Kesir olur. Gerçekten rastgele olmasa da, çok yakındır. Pseudo-random denir tam doğru anlamıyla. Birkaç kere tıkla bak ne olacak.. İşte rasgele ya da rastgele."),
      row("rastgele(2)".c,"G1 girdisinden küçük rastgele bir tam sayı verir, sıfır da dahil."),
      row("rastgele(2, 5)".c,"G1 girdisinden başlayarak G2 girdisinden küçük rastgele tam sayı verir."),
      row("rastgeleSayı".c,"Rastgele tam sayı, Sayı.EnUfağı ile Sayı.Enİrisi arasında."),
      row("rastgeleUzun".c,"Rastgele tam sayı, Uzun.EnUfağı ile Uzun.Enİrisi arasında."),
      row("rastgeleKesir(3.0)".c,"0'dan G1 girdisine kadar rastgele bir kesir."),
      row("rastgeleKesir(2.0, 3.0)".c,"G1 ve G2 girdileri arasında rastgele bir kesir"),
      row("rastgeleÇanEğrisinden".c,"Normal dağılım da derler. 0'a yakın sayılar daha çok rastlar, büyüdükçe ve küçüldükçe daha az gelirler. Diğer adları: rastgeleNormalKesir, rastgeleDoğalKesir."),
      row("rastgeleİkil".c,"Rastgele doğru (true) ya da yanlış (false). Bir diğer adı da: rastgeleSeçim."),
      row("rastgeleRenk".c,"Matematikle ilgisi yok gibi. Ama adı üstünde. Resim çizerken işe yarayabilir."),
      row("rastgeleŞeffafRenk".c,"Deneyerek daha iyi anlarsın."),
      row("rastgeleDiziden(Dizi(1, 3, 5))".c,"G1 girdisindeki diziden rastgele seçer."),
      row("rastgeleDiziden(Dizi(1.0, 10.0, 100.0), Dizi(0.9, 0.09, 0.01))".c,"G2 girdisinde verilen ağırlık oranlarını kullanarak G1'den rastgele seçer."),
      row("rastgeleKarıştır((1 |-| 6).dizine)".c,"Girilen dizini ya da diziyi karıştırır.")
    )
  )
)

pages += Page(
  name = "OPA",
  body = tPage("İşlem Önceliği ve Birleşmeliği",
    "İşlem Önceliği".h3,
    "Scala'nin işlemleri de birer nesne dersem artık hiç şaşırmazsın değil mi? Belki de şaşırman doğal aslında. Ben de hala hayret ediyorum biraz. Şöyle anımsayalım: Scala'nın sunduğu bütün işlemler de aslında birer metod, onun için de birer işlev, onun için de birer nesne! Dolayısıyla işlemlerin adı olarak da her hangi bir ad kullanabiliriz. Yeter ki geçerli bir ad olsun. Geçersiz adlar nasıl olur?".p,
    """val 0abc = "olur mu canım?" """.c,
    """val *olmaz = "bu da yanlış!" """.c,
    "Yani harf ya da özel bir karakterle başlamaması gerek. Ama bu kuralın da istisnaları var.".p,
    """val _0abc = "bak bu oldu" """.c,
    """val a*b = "yok yok bu hala problem" """.c,
    """val _a$b__c_ = "bak bunlar da oldu" """.c,
    "Gördüğümüz gibi bazı özel karakterleri değişken ve değişmez adlarında kullanamıyoruz. Matematik işlemlerinin imleri hiç olmuyor. Ama aşağıdaki yazılımda bir istisna daha göreceğiz.".p,
    "Peki geçerli ve geçersiz bazı adları görmemiz iyi oldu da esas konumuza dönelim, yani öncelik meselesi. Bakın aşagıdaki tablo öncelik sırasını gösteriyor. Üsttekiler önce geliyor. Eğer bir işlem iminde birden fazla karakter varsa o durumda da ilk karakter önem kazanıyor. Örneğin +* diye bir işlem tanımlarsak, onun önceliği + imininkiyle aynı olur.".p,
    table(
      row("diğer bütün özel karakterler","""Örneğin @  {  }  (  ) vb..."""),
      row("""*  /  %  +  -  :  =  !  <  >  &  ^  |""","""Soldakiler sağdakilerden önce gelir."""),
      row("bütün harfler","""O halde a, b'den önce gelir. Ya büyük A?"""),
      row("eşitlik işlemleri","""Örneğin =  +=  -=  *=  /= vb...""")
    ),
    "İşlem Birleşmeliği".h3,
    "Birkaç örnekle daha kolay olacak bu konuyu işlemek:".p,
    """3 * 5 * 7""".c,
    """(3 * 5) * 7""".c,
    """3 :: 5 :: Boş""".c,
    """3 :: (5 :: Boş)""".c,
    """(3 :: 5) :: Boş""".c,
    "Ne oldu? Burada iki değişik işlemle karşı karşıyayız. * imi ile :: imi farklı çalışıyor. Son örneğimiz çalışmadı çünkü :: yani bir dizinin kuyruğuyla başını birleştirip yeni bir dizin oluşturan birleştirme işlemi sağdaki değeri temel alıp onun metodu olan :: işlemine soldaki değeri girdi olarak giriyor ve sonunda yeni bir dizin çıktısı veriyor. Son örnekteki hata nereden kaynaklandı şimdi daha iyi anladık. Ama * işlemi öbür taraftan çalışıyor, yani soldaki değer temel alınıyor onun metodu olarak * çagırılıyor ve ona sağdaki değer girdi oluyor. Scala bu durumu düzenlemek ve programcıya iki seçenek de sunmak için şunu yapıyor: işlemin adına bakıyor ve son karakteri seçiyor. Eğer son karakter ':' ise, yani iki nokta üstüste ise, sağdan birleşme yapıyor ve sağdaki değerin üzerindeki metodu çağırıyor ve soldaki değeri giriyor o metoda. Adlarının son karakteri ':' olmayan metodlarsa öbür türlü çalışıyor, yani yukarıda gördüğümüz * örneğindeki gibi soldaki değerin metodu çağrılıyor ve sağdaki değer girdi oluyor. Yani a * b yazarsak derleyici a.*(b) görmüş gibi çalışıyor, ama a *: b yazarsak b.*:(a) gibi çalışıyor. Bir örnekle alıştırma yapalım ki tam pekişsin. Diyelim ki iki sayıyı önce toplayıp sonra toplamını ikinci sayıyla çarpmak istiyoruz. Bunu da çok yapacağız. O zaman bir nesne türü yani sınıf tanımlayıverelim:".p,
    """case class Deneme(s: Sayı) {
    def +*(x: Deneme) = (s + x.s) * x.s
    def +:(x: Deneme) = (s + x.s) * x.s
}
val (a,b) = (Deneme(5), Deneme(3))""".c,
    "Şimdi de bütün seçenekleri deneyelim:".p,
    "(a +* b, b +* a, a +: b, b +: a)".c,    
    "Umarım faydalı olmuştur. Sen de birşeyler dene, hem parmakların hem de beynin daha iyi öğrensin!".p,
    "Bu vesileyle daha büyük bir örnek görelim ve bilgisayarın temeli olan mantığa giriş yapalım. Mantık nedir bilir misin? Belki iyi tanımazsın ama bildiğinden eminim çünkü mantık bizim altıncı hissimiz gibidir. Mantıksızlık hiç hoşumuza gitmez. Bak şöyle yazabiliriz mantık işlemlerinin temelini. Bunu okurken + * x ve ! gibi imlerin tanımlarını nasıl yaptığımıza da dikkat!".p,
    """case class Önerge(doğruMu: İkil) {
    val bu = this
    def tersi = Önerge(!doğruMu)
    def ve(öbürü: Önerge) = if (doğruMu) öbürü else bu
    def veya(öbürü: Önerge) = if (doğruMu) bu else öbürü
    def yada(öbürü: Önerge) = (bu veya öbürü) ve (bu ve öbürü).tersi
    def eşittir(öbürü: Önerge) = doğruMu == öbürü.doğruMu

    def unary_!(): Önerge = bu.tersi
    def *(öbürü: Önerge) = bu ve öbürü
    def +(öbürü: Önerge) = bu veya öbürü
    def x(öbürü: Önerge) = bu yada öbürü
    def ==(öbürü: Önerge) = doğruMu == öbürü.doğruMu
    def ==>(öbürü: Önerge) = bu.tersi veya öbürü
    def <=>(öbürü: Önerge) = (bu ==> öbürü) ve (öbürü ==> bu)

    override def toString = if (doğruMu) "doğru" else "yanlış"
    def to01 = if (doğruMu) "1" else "0"
}

def tersi(x: Önerge) = x.tersi
def ve(x: Önerge, y: Önerge) = x ve y
def veya(x: Önerge, y: Önerge) = x veya y
def yada(x: Önerge, y: Önerge) = x yada y
def eşittir(x: Önerge, y: Önerge) = x eşittir y
def ise(x: Önerge, y: Önerge) = x ==> y
def gvy(x: Önerge, y: Önerge) = x <=> y // gerek ve yeter

def deneme() = {
    def çizgi = satıryaz("-" * 34)
    def çift = {çizgi; çizgi}
    val ara = " " * 10
    çift
    val (d, y) = (Önerge(doğru), Önerge(yanlış))
    val (dt, yt) = (tersi(d), tersi(y))
    satıryaz(s"  $d'nun tersi == $dt")
    satıryaz(s"  $y'ın tersi == $yt")
    çizgi
    satıryaz(s"$ara !${d.to01} == ${(!d).to01}")
    satıryaz(s"$ara !${y.to01} == ${(!y).to01}")
    çift
    val seçenek = Dizin(d, y)
    for ((bağlam, adı) <- Dizin((ve _, "ve"), (veya _, "veya"), (yada _, "ya da"))) {
        for (a <- seçenek; b <- seçenek) {
            satıryaz(f"  $a%6s $adı%6s $b%6s == ${bağlam(a, b)}")
        }
        çizgi
    }
    çizgi
    for ((bağlam, adı) <- Dizin((ve _, "*"), (veya _, "+"), (yada _, "x"), (ise _, "==>"), (gvy _, "<=>"))) {
        for (a <- seçenek; b <- seçenek) {
            val c = bağlam(a, b)
            satıryaz(f"$ara ${a.to01}%s $adı%s ${b.to01}%s == ${c.to01}")
        }
        çizgi
    }
    çizgi
    for (a <- seçenek; b <- seçenek) {
        val (ab, a_b, axb) = (a ve b, a veya b, a yada b)
        satıryaz(f"  $a%6s ve    $b%6s == $ab")
        satıryaz(f"  $a%6s veya  $b%6s == $a_b")
        satıryaz(f"  $a%6s ya da $b%6s == $axb")
        çizgi
    }
    çizgi
    for (a <- seçenek; b <- seçenek) {
        val (ab, a_b, axb) = (a * b, a + b, a x b)
        satıryaz(s"$ara ${a.to01} * ${b.to01} == ${ab.to01}")
        satıryaz(s"$ara ${a.to01} + ${b.to01} == ${a_b.to01}")
        satıryaz(s"$ara ${a.to01} x ${b.to01} == ${axb.to01}")
        çizgi
    }
    çizgi
}
çıktıyıSil
deneme
""".c
  )
)

pages += Page(
  name = "US",
  body = tPage("Yazıların (String) Kullanılışı",
    "Yazı Türü".h2,
    "Ekrana yazı yazmak bilgisayar programlamada epey sık karşılaşılan bir sorun! Bu bölümde bazı tanımlar ve yazı türünün faydalı metodlarından bazılarını göreceğiz. Bu metodların benzerleri Dizin türünde ve ona benzeyen sıra sıra elemanlar içeren başka türlerde de var, Dizim (Array), Yöney (Vector), Dizi (Seq) gibi hafifçe başkalaşmış ama benzer türler). Bir sonraki bölümde Dizin metodlarını görünce benzerliği farkedeceksin.".p,
    "Aşağıdaki tanımlarda G1, G2, ... ile fonksiyonlara girilen değerleri ifade ediyoruz kısaca. Yani G1.işlevAdı(G2, G3, ...).".p,
    "Yazılar için kaçış karakterleri".h3,
    table(
      row("""\n""", "yeni satır","""\b""", "geri adım","""\t""", "büyük ara","""\f""", "bir sayfa at"),
      row("""\r""", "satır başı","""\" """, "çift tırnak", """\'""", "tek tırnak","""\\""", "geri taksim işareti")
    ),
    "Ekleme".h3,
    "Yazıları aynı toplama yapıyormuş gibi + imiyle ekleyebiliriz. Girdi olan yazılar değişmez. Yeni bir çıktı oluşur sadece. Yazı değerlerini hep 'val' kullanarak sabit tutmakta fayda var. Ama çok istersek 'var' özel sözcüğü kullanarak değerini değiştirebileceğimiz yazı değişkenleri de tanımlayabiliriz.".p,
    """çıktıyıSil
val a = "Büyük"
val b = "Patlama"
val c = a + " " + b
satıryaz(a, b, c)
sil
var gerekYokAslında = "böyle değişkenlere\n"
gerekYokAslında += "pek de gerek yok"
yazı(gerekYokAslında)""".c,
    "Sanırım hemen hemen bütün nesnelerin yazıya diye bir metodu var. Bu metod nesnenin neye benzediğini yazı olarak ortaya koyar ve çok faydalıdır. Mantık önermeleri için tanımladığımız sınıfı anımsadın mı? Bir önceki bölüme bakıver istersen. Orada kendi yazıya metodumuzu tanımlamış ve kullanmıştık.".p,
    """val x = (2).yazıya + " " + (3.1F).yazıya
satıryaz(x)
""".c, 
    "Uzunluk veya Boy".h3,
    table(
      row(""""dört".boyu""".c,"G1 yazısının uzunluğu yani kaç karakterden oluştuğunu bildirir.")
    ),
    "Karşılaştırma".h3,
    table(
      row(""""uzun".kıyasla("uzuner")""".c,"G1 ile G2'yi karşılaştırır. Eğer G1 G2'den önce geliyorsa eksi bir sayı, aynıysa sıfır, sonra geliyorsa artı bir sayı verir. A ile a'yı karşılaştır istersen"),
      row(""""uzun".kıyaslaKüçükHarfBüyükHarfAyrımıYapmadan("Uzun")""".c,"Karşılaştırma yaparken harflerin büyük ya da küçük olmasını göz ardı eder"),
      row(""""kitap".eşitMi("film")""".c,"İki yazı aynı ise doğru der, yoksa yanlış"),
      row(""""kitap".eşitMiKüçükHarfBüyükHarfAyrımıYapmadan("KITAP")""".c,"Eşitliğe bakarken harflerin büyük ya da küçük olmasını göz ardı eder"),
      row(""""kitaplık".başındaMı("kitap")""".c,"G1 yazısının başında G2 var mı?"),
      row(""""kalınkitap".başındaMı("kitap", 5)""".c,"G1 yazısının G3. harfinden itibaren G2 geliyor mu?"),
      row(""""kitaparası".sonundaMı("arası")""".c,"G1'in sonunda G2 var mı?")
    ),
    "Aramak ve bulmak".h3,
    """"Bir yazı içinde başka bir harfi ya da yazıyı arayıp bulmak için kullanabileceğimiz metodlar bunlar. Eğer aradığımızı bulamazsa -1 çıkar. İlk harfin konumu 0 kabul edilir. Gariptir biraz ama bilgisayarda hep sıfırdan saymaya başlarız.""".p,
    table(
      row(""""imrendim".içeriyorMu("ren")""".c,"G2, G1'in içinde geçiyor mu?"),
      row(""""imrendim".sırası("nd")""".c,"G2, G1'in içinde kaçıncı konumda? Birden fazla varsa, ilk konumu verir."),
      row(""""imrendirdim".sırası("di", 7)""".c,"G2, G1'in G3'üncü harfinden sonra hangi konumda?"),
      row(""""imrendirdi".sırası('r')""".c,"G2 karakterinin G1 içindeki ilk konumu?"),
      row(""""imrendirdik".sırası('r', 4)""".c,"G2, G1'in G3'üncü harfinden sonra hangi konumda?"),
      row(""""imrendiler".sırasıSondan('e')""".c,"G2'nin G1 içindeki son konumu nedir?"),
      row(""""imrenerek".sırasıSondan('e', 6)""".c,"G2'nin G1 içindeki G3. harf veya daha önceki son konumu nedir?"),
      row(""""dipdirildi".sırasıSondan("di")""".c,"G2'n'n G1 içindeki son konumu"),
      row(""""dipdirildi".sırasıSondan("di", 5)""".c,"G2'n'n G1 içindeki G3. harf ya da daha önceki son konumu")
    ),
    "Yazıdan parça çıkarmak".h3,
    table(
      row(""""aslangibi".harf(3)""".c,"G1'in G2 konumundaki harfi"),
      row(""""aslangibi".parçası(3)""".c,"G1'in G2 konumundaki harfinden sonuna kadar olan parçası"),
      row(""""aslangibi".parçası(3, 5)""".c,"G1'in G2 konumundan G3'e kadarki parçası. G3. harf hariç.")
    ),
    "Yazıdan başka bir yazı türetmek".h3,
    table(
      row(""""Merhaba Kardeş".küçükHarfe""".c,"Bütün harfleri küçük olacak şekilde G1'in yeni bir kopyası"),
      row(""""Merhaba Kardeş".büyükHarfe""".c,"Bütün harfleri büyük olacak şekilde G1'in yeni bir kopyası"),
      row(""""  Merhaba Kardeş   ".kısalt""".c,"G1'in başındaki ve sonundaki boşlukların silinmiş kopyası"),
      row(""""Savar".değiştir('a', 'e')""".c,"G1'in içindeki bütün G2 harflerinin G3 ile değiştirilmiş kopyası"),
      row(""""Saye saye".değiştir("ay", "ev")""".c,"G1'in içindeki bütün G2 yazılarının G3 ile değiştirilmiş kopyası")
    ),
    "Yazıya çeviren metodlar".h3,
    table(
      row("Yazı.olarak(Dizin(1,2,3))".c,"G1'i yazıya çevirir. G1 herhangi temel bir tür ya da herhangi bir sınıfın nesnesi olabilir."),
      row("""Dizin(1,3,3,1).yazıYap("-ve-")""".c, "G1 dizisinin elemanlarını aralarına G2 ekleyerek yazıya çevirir"),
      row("""Küme(1, 3, 5, 3, 1).yazıYap("{", " ", "}")""".c, "Bir öncekinde olduğu gibi G1'in elemanlarınının aralarına G3'ü ekleyerek yazıya çevirir ama en başa G2, en sona da G4 yazılarını ekler.")
    )
  )
)

pages += Page(
  name = "UL",
  body = tPage("Dizinlerin (List) Kullanılışı",
    "Dizinler".h2,
    "Dizinler, algoritmaların işlevsel olarak tanımlanmasında en çok kullanılan veri yapısıdır. Bu bölümde dizinleri kolaylıkla tanımlamak ve kullanmak için en faydalı olan metodları göreceğiz. En başta bir örnek Dizin tanımlıyacağız. Sonra da onu diğer örnek yazılımcıklarda kullanacağız.".p,
    "Daha önce görmüştük ama anımsatmakta fayda var: sık sık göreceğimiz üç im:  _ + _  şu adsız işlevin kısa yolu: x,y => x + y. Böyle iki girdili işlemler çok yaygın. Onun için bu kısa yol işimize yarayacak. Tabii toplama olması şart değil. Çarpma da olur: _ * _. Benzer şekilde _.metod ile de e => e.metod adsız işlemini kısaca ifade ediyoruz. Bu kısa yollarda kullandığımız alt çizginin her biri yeni bir girdiye karşılık geliyor. Birinci altçizgi ilk girdiyi, varsa ikinci altçizgi ikinci girdiyi, üçüncü altçizgi üçüncü girdiyi, vb.".p,
    table(
      row("""val dzn = "Gün" :: "bu gün" :: "." ::
  "An" :: "bu an" :: "." :: Boş""".c,"""İçinde "Gün", "bu an", "." gibi 6 tane değer olan yeni bir Dizin[Yazı] tanımladık"""), 
      row("Dizin()".c,"Bomboş bir Dizin. Ya da bir önceki örnekteki Boş değerini de kullanabilirsin"),
      row("""Dizin("Zaman", "ok", "gibi", "uçar mı?")""".c,"""İçinde "Zaman" "ok" "gibi" ve "uçar mı?" elemanları olan yani dört elemanlı bir Dizin[Yazı] tanımladık" """),
      row("""Dizin("tik", "tok") ::: Dizin("ding", "dong")""".c,"Üç tane iki nokta üstüste işlemi dizinleri birleştirerek yeni bir dizin türetiyor. :: ile karşılaştır"),
      row("dzn(3)".c,"Dizinin 3. elemanını verir (sıfırdan başlarsak üçüncü.)"),
      row("dzn.say(söz => söz.boyu == 1)".c,"Dizinin içinde tek harften oluşan kaç sözcük var?"),
      row("""dzn.varMı(söz => söz == "bu an")""".c,"""Dizinin içinde "bu an" elemanı var mı?"""),
      row("dzn.düşür(3)".c,"""Dizinin ilk üç elemanı düşmüş kopyasını verir"""),
      row("dzn.düşürSağdan(4)".c,"""Dizinin son dört elemanı düşmüş kopyasını verir"""),
      row("dzn.ele(söz => 1 < söz.boyu && söz.boyu < 4)".c,"Dizinin 2 veya 3 harfli elemanlarını verir"),
      row("dzn.düzİşle(_.dizine)".c,"Verilen işlevi dizinin elemanlarına uygular sonra da hepsini birleştirir"),
      row("""dzn.hepsiİçinDoğruMu(söz => söz.sonundaMı("."))""".c,"""Dizinin bütün elemanları noktayla bitiyor olsaydı doğru derdi"""),
      row("dzn.herbiriİçin(söz => yaz(söz))".c,"Dizinin elemanlarını sırayla yazar"),
      row("dzn.herbiriİçin(yaz)".c,"Bir öncekinin kısa hali"),
      row("dzn.başı".c,"İlk elemanı verir"),
      row("dzn.kuyruğu".c,"İlk eleman hariç gerisini verir"),
      row("dzn.önü".c,"Son eleman hariç gerisini verir"),
      row("dzn.boşMu".c,"boş olsaydı doğru derdi"),
      row("dzn.sonu".c,"Son elemanı verir"),
      row("dzn.boyu".c,"Kaç eleman olduğunu söyler"),
      row("""dzn.işle(söz => söz + "?")""".c,"""dizinin her sözcüğünün sonuna soru işareti ekleyerek yeni bir dizi oluşturur"""),
      row("""dzn.işle { söz =>
    söz match {
        case "." => "?"
        case s   => s
    }
}.herbiriİçin(satıryaz)""".c,"""Bir önceki gibi ama noktaları soru işaretiyle değiştirip yazalım."""),
      row("""dzn.işle {
    case "." => "?"
    case s   => s
}.herbiriİçin(satıryaz)""".c, "Bir öncekinin kısa yazılışı"),
      row("""dzn.yazıYap(", ")""".c,"Diziden elemanları arasına virgül koyarak bir yazı yapar"),
      row("dzn.eleDeğilse(söz => söz.boyu == 1)".c,"Dizinin bir kopyasını verir ama bir harfli elemanları atlar"),
      row("Dizin(1,6,2,1,6,3).yinelemesiz".c,"Tekrar eden elemanları atlar. Tekrarları bulmak için == işlemini kullanır"),
      row("dzn.tersi".c,"Elemanlarını ters sırada olan bir kopya verir"),
      row("dzn.sırayaSok((söz, t) => söz.küçükHarfe < t.küçükHarfe)".c,"A'dan Z'ye sıraya sokulmuş bir kopya verir ama büyük küçük harf ayırımı yapmadan")
    ),
    "Katlama işlemleri de çok işe yarar! Bu sefer sayı dizisi kuralım:".p,
    "val sayılar=Dizin(1,7,2,8,5,6,3,9,14,12,4,10)".c,
    table(
      row("sayılar.soldanKatla(-81)(_ + _)".c,"Soldan sağa elemanları topluyoruz. Başlangıçta soldan -81 giriyoruz"),
      row("sayılar.topla".c, "Sağlamasını yapalım"),
      row("sayılar.sağdanKatla(0x20)(_ | _)".c,"Şimdi de sağdan sola mantıksal veya işlemiyle birleştiriyoruz sayıların parçacıklarını. Başlangıçta en sağdan onaltılık tabanda 20 giriyoruz")
    )
  )
)

pages += Page(
  name = "UT",
  body = tPage("Kaplumbağacığın Kullanılışı",
    "Kaplumbağacığı hareket ettirerek ona çizgi çizdirten pek çok komutumuz var. Çoğunu aşağıdaki tabloda bulacaksın. Onlara tıklayıver ki kaplumbağacık neler yapabiliyor göresin. Tablodaki sırayı izlemene gerek yok (sadece ilk örnek hariç. Ona ileride gerek olacak çünkü üçgen işlevini tanımlıyor). İstediklerine birkaç defa tıklayabilirsin. Tuvalimizi silmek için sağ tıklayıp Temizle komutuna tıkla.".p,
    "Aşağıdaki ilk örnekte üçgen adında bir işlev (ya da komut) tanımlıyoruz (def define yani tanımla demek). Ne yaptığını anladın mı? Tıklayınca göreceksin. Bir üçgen çiziyor. Bu komutu ilerdeki örneklerde de kullanacağız. Henüz tıklamadıysan şimdi tıkla. Farkettin mi bir satırda birden çok komut da çağırabiliyoruz. Aralarına noktalı virgül koymamız gerekiyor sadece. 'yinele' komutumuz da bir komut dizisini tekrar tekrar çağırmakta çok faydalı olur.".p,
    """def üçgen() = yinele(3){ ileri(100); sağ(120) }
sil()
üçgen()
sol()
üçgen()
""".c,
    table(
      row("ileri(100)".c, "100 adım ilerler"),
      row("geri(50)".c,"50 adım geriye gider"),
      row("konumuKur(150, 100)".c, "Çizgi çizmeden koordinatları verilen (x, y) noktasına gider. Ama baktığı doğrultu değişmez"),
      row("noktayaGit(20, 30)".c, "Doğrultusunu koordinatları verilen noktaya çevirir ve o noktaya kadar ilerier"),
      row("dön(30)".c, "Saat yönünün tersine doğru 30 derece döner. Eksi girersen saat yönünde döner"),
      row("sağ()".c, "Sağa döner. Yani saat yönünde 90 derece döner"),
      row("sağ(60)".c, "Sağa doğru 60 derece döner"),
      row("sol()".c, "Sola döner. Yani saat yönünün tersinde 90 derece döner"),
      row("sol(30)".c, "Sola doğru 30 derece döner"),
      row("noktayaDön(40, 60)".c, "Doğrultusunu koordinatları verilen (x, y) noktasına çevirir"),
      row("açıyaDön(30)".c, "Diyelim ki 0 derece ekranın sağı, 90 derece ekranın üstü olsun. Verilen açıya döner"),
      row("doğrultu".c, "Şu anda baktığımız doğrultuyu açı olarak çıktıyla bildirir. 180 sola bakıyor demek. 270 de aşağıya"),
      row("ev()".c, "Evine yani (0, 0) noktasına döner ve 90 dereceye yani yukarı bakar"),
      row("konum".c, "Şu andaki konumu çıktı olarak bildirir"),

      row("""sil
kalemiKaldır()
ileri(100)
kalemiİndir()
ileri(100)""".c, "kalem kalkıkken hareket ederse çizim yapmaz. Kalem inince çizmeye devam eder"), 
      row("""kalemRenginiKur(mavi)
üçgen()""".c, "Ne renk çizmesini istiyoruz"),
      row("""sil()
boyamaRenginiKur(kırmızı)
üçgen()
""".c, "Çizdiği şekillerin içini boyamasını istersek rengini seçiyoruz"),
      row("""sil()
kalemKalınlığınıKur(10)
üçgen()
kalemKalınlığınıKur(1)
  """.c, "Çizim yaptığı kalem kalınlığını giriyoruz"),
      row("ışınlarıAç()".c, "Dör yönü belirten farlar yansın"),
      row("ışınlarıKapat()".c, "Farları söndürelim"),
      row("""sil()
görünmez()
ileri(100)
görünür()
dön(120)
ileri(100)""".c, "görünmez ve görünür komutları da böyle"),
      row("""yazı("Merhaba Kardeş!"); ileri(); yazı(Dizin(3, 1, 4, 1, 5))""".c, "Bulunduğu noktaya yazı yazdırabiliriz"),
      row("""sil()
ileri(-100)
canlandırmaHızınıKur(10)
dön(120)
ileri(100)""".c, "Kaplumbağacığın hızını belirlemek için bir süre giriyoruz. 100 adımı girdiğimiz kadar milisaniyede atıveriyor. Epey hızlı canım! Başlangıçta 1000milisaniye, yani bir saniye alıyor 100 adım atmak. "),
      row("canlandırmaHızı".c, "100 adımı şu anda kaç milisaniyede attığını bildirir"),
      row("""yeniKaplumbağa(50, 50)
val yk1 = yeniKaplumbağa(100, 100)
yk1.geri(180)
""".c, "Verilen (x, y) noktasında yeni bir kaplumbağa canlandırır."),
      row("kaplumbağa0".c, "Başlangıçtaki kaplumbağamızın adı"),
      row("kaplumbağa0.geri(100)".c, "Onu böyle metodlarıyla da çağırabiliriz"),
      row("sil()".c, "Tuvali temizler ve başlangıç noktasına döndürür"),
      row("""sil()
üçgen()
yaklaş(0.5, 10, 10)""".c, "(oran, x, y) Tuvali verilen oran kadar büyültür ya da küçültür ve verilen noktayı tam tuvalin merkezine getirir"),
      row("gridiGöster()".c, "Tuvalin gridini çizer"),
      row("gridiGizle()".c, "Gridi gizler"),
      row("eksenleriGöster()".c, "X ve Y eksenlerini gösterir"),
      row("eksenleriGizle()".c, "Eksenleri saklar")
    )
  )
)

pages += Page(
  name = "GAG",
  body = tPage("Çizim ve Oyun",
    "Kojo'nun Scala üzerine yaptığı katkılardan biri kaplumbağacıklarla çizimler yapmak ve bilgisayar oyunları yazmak. Ama Kojo'nun bir de Resim adlı bir birimi var ki, onunla daha da kolaylıkla bilgisayar grafikleri çizip onları canlandırabilir ve istersen oyunlar programlayabilirsin. Kojo'ya bu becerileri kazandıranlardan birisi de Peter Lewerin adlı bilgisayar aşığı. Lalit ve Peter sayesinde bilgisayar oyunları yazman ve güzel bilgisayar çizimleri yapman çok daha kolay. Resim biriminin becerilerinin tarihçesi Processing adlı bir Java projesine kadar uzanır. Processing birimini de Kojo'ya Peter eklemişti. Bu Resim birimi o kadar becerikli ki sırf onun üzerine büyük bir kılavuz bile yazılabilir. Burada bir kaç örnekle neler yapılabilir görelim ve tadında bırakalım istersen. Kojo'nun Örnekler ve Sergi menülerinde başka pek çok örnek bulacaksın. Burdakileri deneyip anladıktan sonra onlara da bakmanda fayda var.".p,
    "Eğer ilgilenip daha çok örnek görmek istersen, Kojo'nun web sayfasına da bak. Henüz sadece İngilizce. Resim yerine Picture diyecek. Ama ordaki örnekler de sana bir fikir verecektir.".p,
    "Resim adlı birim birçok komut, işlem ve bir de çizim döngüsü sunuyor bize. Bunları kullanarak epey gelişmiş şekiller, resimler ve grafikler çizebiliriz. Çizim döngüsünü kullanarak grafikleri canlandırabilir ve istersek fare ve tuşlarla kontrol ekleyerek oyuna çevirebiliriz.".p,
    "İlk örnekle başlayalım. Önce ekranı temizliyoruz. Sonra bir top çiziyor ve canlandırma döngüsünün içinde topun nasıl hareket edeceğini tanımlıyoruz. Resim birimi, bu döngüyü yaklaşık yirmibeş milisaniyede bir yineleyerek çalıştırıyor. Bilgisayarın hızına göre saniyede yaklaşık olarak 40 kere tekrar tekrar resim çizebiliyor yani. Bu da bizim gözümüzün değişiklikleri görme hızından fazla olduğu için bize canlı ve harektli gibi görünüyor. Bu temel kavramları ve komutları kullanarak pekçok ilginç canlı, hareketli grafik çizebilirsin.".p,
    """silVeSakla(); gridiGöster(); eksenleriGöster()
val yç = 10 // topumuzun yarıçapı
val top = Resim.daire(yç) // resmi
çiz(top) // bu da tuvale çiziveriyor topu
// topun içinde kalmasını istediğimiz sahayı da tanımlayıp çizelim
val en = 400; val boy = 200 // çizmesek de çalışır ama görmek faydalı:
çiz(kalemRengi(siyah) * götür(-en / 2, -boy / 2) -> Resim.dikdörtgen(en, boy))
// topun şu ufak ve iri sayılar arasında kalmasını istiyoruz
val (ufakX, iriX) = (-en/2 + yç, en/2 - yç)
val (ufakY, iriY) = (-boy/2 + yç, boy/2 - yç)
var x = 0; var y = 0 // topun konumunu bu değişkenlerle belirleyeceğiz
var dx = 2; var dy = 4 // topun hızını da x ve y'deki değişikliklerle belirleyeceğiz
// Bu dx ve dy'i birazcık değiştirip tekrar çalıştır. Hemen anlarsın ne oluyor.
// Bize bir de döngü gerek. Bu komut çok becerikli:
canlandır { // İçindeki komutlar saniyede yaklaşık 40 kere yinelenir.
    top.kondur(x, y) // bu metod resimlerin yani burda bizim topun yerini değiştirir
    // topun konumunu yani x ve y'yi günceliyoruz:
    // Zıplama alanının dışına çıkmasın diye sınıra gelince
    // hızının doğrultusunu tersine çeviriyoruz:
    dx = if (x <= ufakX || x >= iriX) -dx else dx
    dy = if (y <= ufakY || y >= iriY) -dy else dy
    x += dx
    y += dy
}""".c,
    "İkinci örneğimiz küçük ve basit bir oyun. Duymuş olabilirsin: en eski bilgisayar oyunlarından duvara karşı pinpon (İngilizce adıyla Pong) oyunu. Tek kişilik bir oyun. Yapmamız gereken topa raketle vurup geri yollamak. Raketi fareyle yönetiyoruz. Top kaçarsa bir puan kaybediyorsun. İyi eğlenceler!".p,
    """silVeSakla()
çiz(götür(-200, -100) -> Resim.doğru(0, 200)) // Üç duvar çizelim. Bu dik ön duvar
çiz(götür(-200, -100) -> Resim.doğru(400, 0)) // Bu alt duvar
çiz(götür(-200,  100) -> Resim.doğru(400, 0)) // Bu da üst duvar
val rb = 80 // raketin boyu
val raket = kalemRengi(mavi) -> Resim.doğru(0, rb)
val top = kalemRengi(mavi) -> Resim.daire(5)
val skor = kalemRengi(siyah) * götür(-50, 150) -> Resim.yazı("Raketi fareyle yönet")
çiz(raket, top, skor)
var x = 0.0; var y = 0 // topun konumu
var dy = 8; var dx = -8.0 // topun hızı: d delta yani değişim ya da derivative yani türev demek
var rx = 0.0; var ry = 0.0 // raketin konumu
var ıska = 0 // top kaç kere kaçtı, saymak için
var vuruş = 0 // kaç kere raketle vurduğumuzu da sayalım
canlandır {
    raket.kondur(fareKonumu)  // raketi fareyle kontrol ediyoruz
    top.kondur(x, y) // topun yerini değiştirelim
    // top rakete çarpıyor mu?
    rx = fareKonumu.x; ry = fareKonumu.y
    dx = if ((dx > 0) && (mutlakDeğer(rx - x) < 10) &&
        (y > ry) && (y < ry + rb)) {vuruş += 1; -1.1*dx} else dx
    // topun konumunu güncelleyelim, duvarlara bakalım
    dx = if (x + dx < -200) -dx else dx  // ön duvardan sekti mi?
    dy = if ((y + dy < -100) || (y + dy > 100)) -dy else dy // üst ve alt duvarlardan sekti mi?
    if (x + dx > 200) { x = -200; dx = 8; ıska += 1 } // ıskaladı
    x += dx; y += dy // topun gideceği noktayı hesaplayalım
    if (ıska > 0 || vuruş > 0) {  // skoru güncelleyelim
        val mesaj = if (vuruş == 0) "Fareyi tuvale getir!" else s"$vuruş kere vurdun"
        skor.güncelle(s"$mesaj\n$ıska kere ıskaladın")
    }
}
oyunSüresiniGöster(60, "Süre bitti", yeşil) // oyun 60 saniye sürsün 
""".c,
    "Temel kavramlar ve komutlarla artık tanıştın. Bir kaç tane daha top eklemeye ne dersin? Topların hızını rastgele değiştirerek oyunu daha eğlenceli kılabilirsin istersen. Raketin boyunu kısaltabilir ya da uzatabilirsin. Bir de programımızın bir (belki kimbilir daha çok) hatası var! İngilizce'de bug yani böcek denir. Ama nerden çıktı deme. Programı yazanın hatası de. Sen de yapacaksın bol bol hata. Hiç dert etme. Her hata aslında daha usta olmak için bir fırsat. Fırsatları hiç kaçırma! Hatayı farkettin mi? Bazen top raketin içinden geçiyor sanki elektron tünelleme yapıyormuş gibi (kuvantum mekaniği değil ki bu)! Bazen de ıskalaması gerekirken geri yolluyor.. Bakalım yazılımcığın içinde nerede? Sen bulup düzeltebilecek misin?".p,
    "Klavye ve tuşlarla komut girişi".h3,
    "Pekçok oyun klaveyeden komut bekler. Minecraft oynadın mı hiç? Hem fare hem de klavye komutları oyunu iyice eğlenceli kılar. Bak bu küçük oyun sağ/sol/yukarı ve aşağı ok olan tuşlarla kaplumbağacığa yön veriyor.".p,

    "Her zamanki gibi aşağıdaki yazılımcığa tıkla ki çalışsın. Sonra da tuvale tıkla ki klavyeye bastığın zaman farketsin kaplumbağa ve söz dinlesin, senin komutlarını yerine getirsin.".p,
    """sil()
görünür()
canlandırmaHızınıKur(100)
var zıpladı = yanlış
tuşaBasınca { t =>
    t match {
        case tuşlar.VK_LEFT  => açıyaDön(180)  // sola git
        case tuşlar.VK_RIGHT => açıyaDön(0)    // sağa git
        case tuşlar.VK_UP    => açıyaDön(90)   // yukarı
        case tuşlar.VK_DOWN  => açıyaDön(270)  // aşağı
        case tuşlar.VK_Z     => zıpla(20); zıpladı = doğru    
        case _           => // diğer tuşlar sadece ilerletsin
    }
    if (zıpladı) {
        zıpladı = yanlış
    } else {
     ileri(20)
    }
}
tuvaliEtkinleştir()""".c,
    "Hayal gücünü kullan, yazılımcığı değiştir (örneğin 45 derece döndürmek için komut ekleyebilirsin), tekrar çalıştır. Yazılımcık düzenleme ekranında 'tuşlar.' yazdıktan sonra (ama tırnak işaretleri olmadan!) kontrol tuşunu basık tutup büyük boşluk tuşuna bas ki başka hangi tuşları kullanabileceğini gör.".p,

    "Saat".h3,
    "Bir saat yapalım mı?".p,
    "BuAn adlı birimi kullanacağız. O da içinde Java'nın Calendar yani takvim adlı kütüphane birimini kullanıyor.".p,
    """silVeSakla
val yç = 100 // saatin yarıçapı. Büyültmek ister misin?
val pi2 = 2.0 * piSayısı // 2*Pi radyan tam 360 derece dönüş demek
// saati çizelim
def saat = {
    Resim.sil() // eski saati silelim
    çiz(kalemRengi(kırmızı) -> Resim.daire(yç))
    for (i <- 0 |-| 59) { // // dakika ve saat çentikleri
        val ra = pi2 * i / 60
        val (x, y) = (yç * sinüs(ra), yç * kosinüs(ra))
        val çentikBoyu = if (i % 5 == 0) 0.9 else 0.95
        val (llx, lly, urx, ury) = (çentikBoyu * x, çentikBoyu * y, x, y)
        val (en, boy) = (urx - llx, ury - lly)
        çiz(kalemRengi(kırmızı) * götür(llx, lly) -> Resim.doğru(en, boy))
    }
}

canlandır { // bu döngü her saniyede yaklaşık 40 kere yinelenir
    val buan = BuAn()
    saat; çiz(kalemRengi(siyah) * götür(-yç - 5, -yç - 20) -> Resim.yazı(buan.yazıya))
    val s = pi2 * buan.saniye / 60 // saniyeKolu
    çiz(kalemRengi(mavi) -> Resim.doğru(0.9 * yç * sinüs(s), 0.9 * yç * kosinüs(s)))
    val m = pi2 * buan.dakika / 60 // dakika kolu
    çiz(kalemRengi(yeşil) -> Resim.doğru(0.8 * yç * sinüs(m), 0.8 * yç * kosinüs(m)))
    val h = pi2 * buan.saat / 12 + m / 12 // saat kolu
    çiz(kalemRengi(turuncu) -> Resim.doğru(0.6 * yç * sinüs(h), 0.6 * yç * kosinüs(h)))
}
""".c,
    "Saatin kaç tane çizimden oluştuğunu hesabedebilir misin? Yanıt pekçok ülkedeki emeklilik yaşı! Neyseki bu 65 çizimi yapmak hem de saniyede 40 kere tekrar tekrar bizim bilgisayarımızı hiç yormuyor! Peki belki de yok, yok 63 tane çizgi, bir çember ve bir kaç da yazı diyerek itiraz edeceksin. Çok haklısın. Ve tebrikler!".p,
    "Conway'in Yaşam Oyunu".h3,
    """İngilizce adıyla "The Game of Life" o kadar meşhur ki, bilgisayarcılar arasında basitçe Life yani Yaşam adıyla tanınır! Aslında o bir hücresel otomaton yani basit hücrelerden oluşan ve onların yerel etkileşimleri sayesinde kendi kendine devinen en basit program türlerinden biri. İngiltere doğumlu Amerika'da Princeton üniversitesinde matematik araştırmaları yapan John Horton Conway tarafından 1970 yılında icat edilmiş. Belki de keşfedilmiş demek lazım. Kimbilir. Sen nedersin?""".p,
    "Wikipedia ansiklopedisinden bakabilirsin".link("tr.wikipedia.org/wiki/Conway%27in_Hayat_Oyunu"),
    "Ana fikir çok basit. Basit bir avuç kurala göre hücreler canlanır ya da can verir. Her hücrenin sekiz komşusu var. Doğru mu? Canlı olanlara arkadaş diyelim. Bakın dört kural var:".p,
    table(row("Canlı bir hücrenin ikiden az arkadaşı varsa canı çıkar. Canı sıkılmış sanki."),
      row("Eğer iki ya da üç arkadaşı varsa hayatta kalır."),
      row("Eğer başına üçten fazla arkadaş toplanırsa canı çıkar! Bilmem neden. Sanki çok kalabalık olmuş gibi."),
      row("Cansız bir hücrenin tam üç tane arkadaşı varsa kendisi de canlanır. Allah'ın hakkı üç denir ya!")
    ),
    "Bu yazılımcık 'soldanKatla' adlı metodu kullanarak çok önemli bir kavram olan üst derece işlevlere örnek oluyor. Ne demek üst derece işlev? Başka işlevleri girdi olarak kabul eden onları kullanarak akıl almaz derecede becerikli olan komutlar. En başlarda gördüğümüz 'for' yapısı yerine 'soldanKatla' kullanarak bütün dünyayı baştan çiziveriyoruz. Bu arada bir de sağdanKatla var. O da esaslı bir işlev). Dünyayı temsil eden kümenin hücrelerinin hepsini işleyiveriyor.".p,
    "Bu yaşam ya da hayat oyunu sıfır oyuncuyla oynanıyor! Çok sıkıcı mı dedin? Yok, çok ilginç aslında. Aslında sen çok önemlisin. Çünkü bu oyunun başlaması için en başta canlı hücrelere gerek var. Bunları sen belirleyebilirsin. Ama önce hazır bazı desenlerle başlamak daha kolay olur. 'başlangıç' adlı komudu bul. Onun ikinci girdisi 'desen'. Deseni seçmek için yapman gereken tek şey 'seç' adındaki değeri değiştirmek. Birinciyle başlıyoruz. Ama sıfırdan ona kadar hepsini deneyebilirsin. Sonra hatta kendin de yeni desenler ekleyebilirsin.".p,

    "Bu simulasyonun hızını 'oran' değerini değiştirerek ayarlayabilirsin.".p,
    """çıktıyıSil; silVeSakla(); kalemRenginiKur(mavi)
// bu oyunun dünyası yani tahtası büyük bir kare. Kenarı KU uzunluğunda olsun
// Nasıl satranç tahtası 8x8, bu tahta da 128x128 kare.
val KU = 128
// karenın kenarı kaplumbanın on adımına denk

// ilk önce, bütün kareler cansız olmalı
var dünya = (0 |- KU * KU).soldanKatla(Sayılar())((x, y) => x :+ 0)
satıryaz(s"Dünyamızda $KU'in karesi yani ${dünya.boyu} tane hane var.")
yaz(s"Ekranımız ${(tuvalAlanı.eni / 10).sayıya} kare eninde ")
satıryaz(s"ve ${(tuvalAlanı.boyu / 10).sayıya} kare boyunda.")

val oran = 5 // canlandırmayı yavaşlatmak için bunu arttır.
// En hızlısı 1. 40'a eşitlersen saniyede bir nesil ilerliyor yaklaşık olarak.
// Nasıl mı? Aşağıdaki canlandır adlı döngü komutu saniyede yaklaşık 40 kere yineleniyor.

val gösterVeDur = yanlış // bunu doğru yaparsan deseni gösterip dururuz
val sonundaDur = doğru // her desenin bir durağı var. Ondan sonra fazla bir şey değişmiyor.
// Ama, yine de çalışmaya devam etsin isterse, bunu yanlışa çevir.

// deseni seçelim:
val seç = 1
// blok1 ve blok2 bir kaç füze yolluyor ve sonra 1000. nesil civarı gibi duruyor.
val (desen, adı, durak) = seç match {
    case 0 => (üçlüler, "üçlüler", 20)
    case 1 => (kayGit, "kayGit", 500) /* makineli tüfek gibi */
    case 2 => (esaslı, "esaslı", 1111) /* Yaklaşık 1000 nesil canlı sonra peryodik */
    case 3 => (dokuzcanlı, "dokuzcanlı", 130) /* 131 nesil sonra can kalmıyor */
    case 4 => (blok1, "blok1", 1200) //
    case 5 => (blok2, "blok2", 1200) //
    case 6 => (küçücük, "küçücük", 700) //
    case 7 => (ü2a, "ü2a", 60) // üçlülere ek
    case 8 => (ü2b, "ü2b", 60) // benzeri
    case 9 => (dörtlü, "dörtlü", 30) // üçlü üretiyor
    case _ => (tohum, "tohum", 2200) // ne muhteşem bir meşe palamudu!
}

dünya = başlangıç(dünya, desen)

yaz(s"$seç. desende ${desen.boyu} tane canlı kare var. Adı $adı.\nNesilleri sayalım: ")

var zaman = 0
val z0 = buSaniye // şimdiki zamanı (geçmişte bir ana göre) anımsayalım
canlandır {
    val nesil = zaman / oran + 1
    if (zaman % oran == 0) {
        Resim.sil()
        çizim(dünya)
        dünya = (0 |- KU * KU).soldanKatla(Sayılar())((x, y) => x :+ yeniNesil(dünya, y))
        yaz(s"$nesil ")
        if (gösterVeDur) durdur
    }
    zaman += 1
    if (sonundaDur && nesil == durak) {
        val z1 = buSaniye - z0
        satıryaz(s"\n${yuvarla(z1, 2)} saniye geçti. Durduk.")
        durdur()
    }
}

// deseni kuralım
def başlangıç(v: Sayılar, desen: Dizin[(Sayı, Sayı)]) = desen.
    soldanKatla(v) {
        (x, y) => x.değiştir((y._1 + KU / 2) * KU + y._2 + KU / 2, 1)
    }

// yeni nesli bulalım
def yeniNesil(v: Sayılar, ix: Sayı) = {
    val kural = Yöney(0, 0, 0, 1, 1, 0, 0, 0, 0, 0) // oyunun kuralları
    val x = ix / KU; val y = ix % KU
    val t = (0 |- 3).soldanKatla(0)((st, i) => {
        st + (0 |- 3).soldanKatla(0)((s, j) => {
            val xt = x + i - 1; val yt = y + j - 1
            s + (if ((xt < 0) || (xt >= KU) || (yt < 0) || (yt >= KU)) 0 else v(xt * KU + yt))
        })
    })
    if (v(ix) == 1) kural(t) else { if (t == 3) 1 else 0 }
}
// canlı kareleri çizelim. Can mavi çember içi kırmızı daire. Yarıçapı 5
val yarıçap = 5
def çizim(v: Sayılar) = for (i <- 0 |- KU * KU)
    if (v(i) == 1) çiz(götür(
        (i / KU) * 2 * yarıçap - KU * yarıçap,
        (i % KU) * 2 * yarıçap - KU * yarıçap
    ) * kalemRengi(mavi) * boyaRengi(kırmızı) -> Resim.daire(yarıçap))

// Meşhur olmuş desenlerden birkaçı
def esaslı = Dizin((0, 1), (1, 0), (1, 1), (1, 2), (2, 2)) // orijinal adı: fpent
// İki küçücük grup var ve kolay kolay ölmüyor
def dokuzcanlı = Dizin((0, 1), (1, 0), (1, 1), (5, 0), (6, 0), (7, 0), (6, 2)) // diehard
def tohum = Dizin((0, 0), (1, 0), (1, 2), (3, 1), (4, 0), (5, 0), (6, 0))
// glider adlı meşhur üretken desen
def kayGit = Dizin((-18, 3), (-18, 4), (-17, 3), (-17, 4), (-8, 2), (-8, 3), (-8, 4), (-7, 1), (-7, 5),
    (-6, 0), (-6, 6), (-5, 0), (-5, 6), (-4, 3), (-3, 1), (-3, 5), (-2, 2), (-2, 3), (-2, 4),
    (-1, 3), (2, 4), (2, 5), (2, 6), (3, 4), (3, 5), (3, 6), (4, 3), (4, 7),
    (6, 2), (6, 3), (6, 7), (6, 8), (16, 5), (16, 6), (17, 5), (17, 6))
def blok1 = Dizin((0, 0), (2, 0), (2, 1), (4, 2), (4, 3), (4, 4), (6, 3), (6, 4), (6, 5), (7, 4))
def blok2 = Dizin((0, 0), (0, 3), (0, 4), (1, 1), (1, 4), (2, 0), (2, 1), (2, 4), (3, 2), (4, 0),
    (4, 1), (4, 2), (4, 4))
def küçücük = Dizin((-18, 0), (-17, 0), (-16, 0), (-15, 0), (-14, 0), (-13, 0), (-12, 0), (-11, 0), (-9, 0), (-8, 0),
    (-7, 0), (-6, 0), (-5, 0), (-1, 0), (0, 0), (1, 0), (8, 0), (9, 0), (10, 0),
    (11, 0), (12, 0), (13, 0), (14, 0), (16, 0), (17, 0), (18, 0), (19, 0), (20, 0))
def üçlüler = Dizin((0, 2), (0, 3), (0, 4), (0, -2), (0, -3), (0, -4),
    (-2, 0), (-3, 0), (-4, 0), (2, 0), (3, 0), (4, 0))
// üçlülerden dikey olanları bağlayalım
def ü2a = Dizin((0, 0), (0, 1), (0, -1)) ++ üçlüler
// öbür türlü, yani yatay olanları bağlayalım
def ü2b = Dizin((0, 0), (1, 0), (-1, 0)) ++ üçlüler
def dörtlü = Dizin((0, 0), (1, 0), (-1, 0), (0, 2)) // dokuzcanlı'nın altkümesi

// sepet sepet yumurta
// sakın beni unutma
// şimdilik bu kadar
// yaşamın tadını çıkar
""".c,
    
    "Düğüm açma oyunu".h3,
    "Bu oyun bize iki şey gösterecek: 1) Scala'nın bize sunduğu veri yapılarından Yöney ne işlere yarıyor. 2) Fareye tıklayıp bırakmadan sürüklersek neler yapabiliyoruz. Tabii daha da güzeli eğlenceli bir oyun yazabiliyoruz bu sayede.".p,
    "Bu oyunu internetteki eski bir oyundan esinlenerek yazdık. Oyunun adı Planarity yani düzlemsellik. Mavi toplardan herhangi birinin üzerine tıklayıp bırakmadan tuvalde başka bir yere taşı. Göreceksin ki bağlı olduğu iki çizgi sanki lastik gibi hareket ediyor ve topu bırakmıyor. Bu bulmacanın amacı topları güzelce yerleştirerek çizgilerin birbirini kesmesine engel olmak. Yani bu düğümü çözmek. Çok zor sayılmaz. Biraz dene kolaylaşacak. Kırmızı kareye tıklarsan yeni bir düğüm oluşur.".p,
    "Yazılımcığa bakarsan en başta KS adında bir değişmez (val) göreceksin. Onu değiştirerek oyunun zorluğunu ayarlayabilirsin. Ne kadar büyütürsen o kadar zorlaşır!".p,
    "Bize ilham veren oyunun adı Planarity. Daha çok bilgi için buna tıkla".link("en.wikipedia.org/wiki/Planarity"),
    
    """// KS arttıkça oyun zorlaşır. Bir kenarda kaç tane nokta olsun?
val KS = 4; val AS = KS * KS
val YÇ = 20 // bu da noktaların yarıçapı
case class Çizgi(n1: Nokta, n2: Nokta) {  // her çizgi iki noktayı bağlar
    var çizgi = doğru(n1.x, n1.y, n2.x, n2.y) // bir doğru çizer
}
def doğru(llx: Kesir, lly: Kesir, urx: Kesir, ury: Kesir) = {
    val (en, boy) = (urx - llx, ury - lly)
    val r = götür(llx, lly) -> Resim.doğru(en, boy)
    çiz(r)
    r
}
// bütün çizgiler. boş küme olarak başlarız
var çizgiler = Yöney[Çizgi]()
// Noktayı tuvalde kaydıracağız. Yeri değişince ona bağlı çizgileri tekrar çizmemiz gerek
case class Nokta(var x: Kesir, var y: Kesir) {
    val n = götür(x, y) * boyaRengi(mavi) -> Resim.daire(YÇ)
    çiz(n)
    def yeniKonum(yeniX: Kesir, yeniY: Kesir) {
        x = yeniX; y = yeniY
        n.kondur(yeniX, yeniY)
    }
    // fareye tıklayıp çekince bu çalışacak
    n.fareyiSürükleyince { (mx, my) => { n.kondur(mx, my); x = mx; y = my; çizelim(çizgiler) } }
}
// Bütün noktaları (0,0) yani orijine üştüste koyalım. Merak etme birazdan dağıtacağız
silVeSakla()
val noktalar = (0 |- AS).soldanKatla(Yöney[Nokta]())((v, i) => { v :+ Nokta(0, 0) })

// çizgileri tanımlar ve noktalara bağlarız. Bir balık ağı gibi. KS * KS düğümlü
çizgiler = (0 |- AS).soldanKatla(Yöney[Çizgi]())(
    (çv, i) => {
        val (x, y) = (i / KS, i % KS)
        val çzg = if (y < KS - 1) { çv :+ Çizgi(noktalar(i), noktalar(i + 1)) } else çv
        if (x < KS - 1) { çzg :+ Çizgi(noktalar(i), noktalar(i + KS)) } else çzg
    })
serpiştir(noktalar) // noktaları yerleştir ve çizgileri çiz

// noktaları rastgele yerleştir
def serpiştir(hepsi: Yöney[Nokta]) {
    hepsi.herbiriİçin(nkt => nkt.yeniKonum(KS * YÇ * 6 * (rasgele - 0.5), KS * YÇ * 6 * (rasgele - 0.5)))
    çizelim(çizgiler)
}

// noktalar arasındaki çizgileri çizelim. Her çizgi, iki noktasının çemberine kadar gelsin
def çizelim(hepsi: Yöney[Çizgi]) {
    hepsi.herbiriİçin(çzg => {
        val (x1, y1) = (çzg.n1.x, çzg.n1.y)
        val (x2, y2) = (çzg.n2.x, çzg.n2.y)
        val boy = karekökü(karesi(x2 - x1) + karesi(y2 - y1))
        val (xr, yr) = (YÇ / boy * (x2 - x1), YÇ / boy * (y2 - y1))
        çzg.çizgi.sil
        çzg.çizgi = doğru(x1 + xr, y1 + yr, x2 - xr, y2 - yr)
    })
}

// kırmızı kareye basınca yeni bir düğümle baştan başlarız
def kare(x: Kesir, y: Kesir, en: Kesir) = {
    val k = götür(x, y) * boyaRengi(kırmızı) -> Resim.dikdörtgen(en, en)
    çiz(k)
    k
}
val b = kare(-KS * 35, -KS * 35, 20)
b.fareyeTıklayınca{ (x, y) => serpiştir(noktalar) }
""".c
  )
)

pages += Page(
  name = "LM",
  body = tPage("Daha Çok Öğrenelim",
    "Bundan sonra neler yapabilirsin".h2,

    "Bu kılavuzcukda, daha çok şeylere kadir, profesyönel yazılımcılar ve bilgisayar mühendislerinin de kulladığı ve çok derin bir programlama dili olan Scala'nın bazı temel becerilerini gördük. Artık bu dilin temellerini tanıyorsun. Oldukça sofistike, yani acemilerin kolay kolay anlayamayacağı programlar yazabilir, Kojo'nun görsel ortamında çizimler ve oyunlarla iyi vakit geçirebilirsin.".p,

    "Aranızda Java diliyle program yazmış olanlar varsa, Java'da kullandığınız birimleri (Java libraries), hatta Minecraft birimlerini de Scala kullanarak geliştirebilirsin. Scala ve Java'yı entegre etmek, yani birleştirmek işten değil.".p,

    "Daha iyi, daha çok öğrenmek için bence iyi bir kitap gibisi yoktur. İngilizce bilenler için çok iyi bir kitap olarak şunu öneririm: Programming in Scala. Yazarları Martin Odersky, Lex Spoon ve Bill Venners.".p,
    "Bu linke tıklayarak daha çok bilgi edinebilirsin.".link("www.artima.com/shop/programming_in_scala"),
    "Çok yeni ve yine çok güzel bir e-kitap da şu: Hands-on Scala Programming. Yazarı genç ama usta programcı Haoyi Li. Pratik ve proje-temelli bir yol izliyor. Ben ikisini de çok beğendim.".p,
    "O da burada.".link("www.handsonscala.com"), 
    "Scala'nın bir ailesi ve topluluğu var ve onlar da senin gibi yeni öğrenenlere yardımcı olmayı çok severler".p,
    "Buna tıklayarak scala-lang.org sitesinin belgelerine, bedava kitaplarına, dokümanlarına ulaşabilirsin".link("docs.scala-lang.org"),
    "Bu işin uzmanları, para kazanmak için yapanlar Scala, Akka ve Eclipse IDE (birleşik geliştirme ortamı) ve diğer açık kaynaklara bakabilirler.".p,
    "Hepsi burada.".link("www.lightbend.com/open-source"),
    "Netbeans IDE de Scala'yı destekliyor. Hatta Kojo'nun yazarı Lalit Pant, Kojo'yu Scala ile Netbeans kullanarak yazdı!".p,
    "Bu konuda daha çok bilgi ararsan buna tıkla.".link("wiki.netbeans.org/Scala"),
    "Bu kılavuzcukla Scala'nın kütüphanesinin çok küçük bir altkümesini gördük.".p,
    "Bütün kitaplığı görmek istemez misin? Buna tıklayıver.".link("www.scala-lang.org/api/current/index.html"),
    "Scala kullanırken hep çok keyif alman, güzel anlar, güzel günler yaşaman ve yaşatman dileğiyle!".p
  )
)

val story = Story(pages.toSeq: _*)
clearOutput()
stClear()
stAddLinkHandler("example", story) {idx: Int =>
  stSetScript(codeExamples(idx))
  stClickRunInterpreterButton()
  clearOutput
}
stPlayStory(story)
