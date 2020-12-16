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
val codeExamples = new Array[String](1000)
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
    <p>Bu kılavuzcuk simplyscala.com'dan Kojo'ya uyarlanarak yazılmıştır. Kılavuzda ileri veya geri gitmek için en aşağıdaki üçgenlere tıkla. Ya da istediğin bölüme atlamak için aşağıdaki başlıklardan birine tıkla.</p> <br/>
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
    <a href={link("UT")}>Kamplumbağacığın Kullanılışı</a> <br/>
    <a href={link("GAG")}>Çizim ve Oyun</a> <br/>
    <a href={link("LM")}>Daha Çok Öğrenelim</a> <br/>
    <br/>
    <p>Not: Anthony Bagwell'in simplyscala.com sitesi artık yaşamıyor. Arşivini şurada bulabilirsin: <a href="http://web.archive.org/web/20130305041026/http://www.simplyscala.com">web.archive.org'dan simplyscala.com</a>.</p>
    </div>
    </body>
)

pages += Page(
  name = "GS",
  body = tPage("Başlayalım",
    "Başlayalım".h2,
    "Kılavuzcuğumuzda pek çok yazılımcık örneği bulacaksın. Onları buradan kolaylıkla çalıştırabilirsin. Herhangi birinin üstüne tıkladığında o örnek, yazılımcık düzenleyicisine taşınır ve çalışır. Yani hepsini yazmana gerek yok. Hatta yazılımcığı çalıştırma düğmesine bile tıklamaya gerek kalmıyor. Ayrıca, işin bir de güzel yanı şu: istediğin değişiklileri orada yapıp tekrar çalıştırabilirsin. Bunu için düzenleyicinin hemen üzerindeki yeşil üçgene tıklaman yeter. Haydi şimdi bunu deneyelim. Aşağıdaki mesajı Bülent yerine kendi adını yazıp tekrar çalıştırıver. Bu arada benim adım Bülent. Bu satırları siz Türkçe severler için severek çevirdim.".p,
    """satıryaz("merhaba dünya! kaplumbağacık ve Bülent'ten hepinize selamlar, sevgiler")""".c,
    "Yaptığın değişikliğin sonucunu Çıktı Gözünde ya da Çizim Tuvalinde hemen göreceksin.".p,
    "Bu kılavuzcuk ilk sayfada gördüğün bölümlerden oluşuyor. Bir sonraki bölüme geçmek için, ya da bir önceki bölüme dönmek için bu pencerenin altındaki sağa ve sola bakan mavi daire içindeki beyaz üçgenlere tıkla. İlgini çeken bölüme de kolaylıkla atlayabilirsin istersen. Bunun için ilk sayfaya geri dönüp ordaki mavi ve altı çizili başlıklardan herhangi birine tıklayabilirsin. İlk sayfaya dönmek için en üstte solda menü yazıyor ya, ona tıklayıver. En alttaki mavi daire içindeki kareye basarsan bu kılavuzcuktan çıkıp Kojo'nun normal düzenine dönebilirsin. Çıkınca kılavuzcukta son seçtiğin yazılımcık kapanmaz. Onun üzerinde değişikler yapıp çalıştırmaya odaklanabilirsin.".p,
    "Daha önce çalışan bir yazılıma dönmek de kolay: yazılım düzenleyicisinin üst kısmındaki menünün ortasında sağa ve sola bakan mavi oklara tıkla. Seçtiğin yazılımı düzenlemeye kaldığın yerden devam edebilirsin. Her örneği deneyebilir, istediğine geri dönüp değişiklikler yapıp tekrar çalıştırabilirsin. Değişik fikirler dener, programlama dilini daha iyi tanıyıp yazım, gramer kurallarını daha çabuk öğrenebilirsin. Yazılımcık düzenleyicisindeki programları diske kaydedebilir ve sonra oradan geri yükleyebilirsin. Bunun için Dosya menüsüdeki komutları kullan.".p,
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
    """Bir deyişin sonucunu bir değişken kullanarak kaydedip daha sonra yine kullanabiliriz. Değişken isimleri (ve sonra göreceğimiz başka tür isimler) harf, sayı ve * / + - : = ! < > & ^ | gibi semboller kullanarak yazılır. Örneğin, "FutbolTopu", "BilardoTopuBeyaz1", "yardımHattı", "*+" and "Res4" (Result yani sonuç)...""".p,
    """Bunun için iki yöntem vardır: "var" ve "val" komut sözcükleri. "val" (İngilizce value sözcüğünün kısaltması) ile sabit ve hiç değişmeyecek değerleri ve sonuçları saklayabiliriz. Ya neden bir de "var" (ingilizce variable sözcüğünün kısaltması) komutu var (var var ama varı kullanma! 8-)? Aşağıda bir örnekle ikisinin farkını hemen anlayacağız. "val" ile tanımlanan değerlerin sabit olması (ingilizcede 'immutable value') aslında çok önemli bir işlevsel programlama (functional programming) kavramıdır, ama bunu daha sonra yeri gelince daha iyi anlayacağız. Şimdilik mümkün oldukça 'var' yerine 'val' komutunu kullanmaya dikkat edelim. Bu sayede programın başka bir yerindeki değişkenleri yanlışlıkla bozamayız.""".p,
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
    """Okuyanları bilgilendirmek ve kendimize anımsatmak için satır sonlarına // yani iki taksim ya da bölüm işaretinden sonra bir açıklama yazabiliriz. Bir satıra sığmıyorsa /* ile başlayıp */ ile biten daha uzun açıklamalar ekleyebiliriz. Scala derleyicisi bunları göz ardı eder ve bu sayesede bilgisayarın kafası karışmaz 8-).""".p,
    """/*
  Çok satırlı bir açıklama örneği
  Fahrenayttan santigrata çevirelim
*/
val dereceF = 98.4
// Tek satırda açıklama: 0S = 32F. 9F artış 5C artışa denk
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
    "Bakın bu çok ilginç. Parçacıkları sola kaydırmak ikiyle çarpmaya denk! Sola kaydırmaksa ikiye bölmeye benzemiyor mu? Bu daha önce de dediğim gibi uzmanlık konusu. Üzerinde yazılmış pekçok bilimsel makale ve ders kitapları var. Hatta bazıları çok azımızın anlayabileceği yüksek ihtisas kitapları! Bugünlük bu kadarı fazla bile. Ama sen istersen bu yazılımcığı kurcala. Dene. Bakarsın uzman olmak istersin. Neden olmasın?".p,
    "Farkında mısınız? Bu kadarcık bilgiyle bile artık çok güçlü bir hesap makinemiz oldu. Ama dahası var! Bir sonraki bölümde program akışı nasıl düzenlenir öğrenecek ve yazılımcıklarımızı çok daha becerikli hale getireceğiz.".p
  )
)

pages += Page(
  name = "Flow",
  body = tPage("Program Akışı If, Else ve While Komutları",
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
    """for(i <- 1 to 4) yaz("merhaba!")""".c,
    "'to' sözcüğü de bizim yapım/çekim ekimiz gibi, birden dörde kadar derken dörde sözcüğündeki '-e' anlamında. İngilizceyle Türkçe ne kadar çok farklı sanki, değil mi? Bir de bana sorun. 22 yaşında ilk defa yaşamak için Amerika'ya gittiğimde o kadar zorluk çektim ki! Güya iyi biliyordum hem de İngilizceyi! İşimize dönelim: Aralık burada birden dörde kadar olan sayılar elbet. 'i' değişkeni 1 değeriyle başlıyor ve her tekrarda bir artıyor. Son sayı burada 4. Ama sonuncu sayıya gelmeden hemen önce durmak istersek 'to' yerine 'until' sözcüğünü kullanıyoruz, yani kadar anlamına gelen İngilizce sözcük:".p,
    """for (i <- 1 until 12) {
  val kare = i*i
  satıryaz(i, kare)
}""".c,
    "Biliyor musun, bu yineleme işlemlerini birden çok boyutta yapmak bilgisayarla çok kolay. Birden fazla aralık vereceğiz ve her aralık için de bir değişken. Tek dikkat etmemiz gereken ikisi arasına bir noktalı virgül koymak. Bakın ne kolay!".p,
    """for(i <- 1 until 5 ; j <- "abc") satıryaz(i, j)""".c,
    """Bakın şu işe! Sayı yerine harfler kullandık! "for" yapısı içinde kullandığımız kümeler illa da sayılardan oluşmak zorunda değil yani. Genel olarak biz bunlara küme tekerleme diyebiliriz (İngilizcesi: iterating through a set or collection) yani teker teker her küme elemanını ele alıyoruz. "abc" yazısı da aslında bir harf kümesi ya da kolleksiyonu. Bakın hep küme ya da kolleksiyon dedim. Bu kavramlar yakın ama ufak farklılıkları var. Daha sonra bunlara verilen anlamı daha iyi anlayacağız. Şu anda çok da önemli değil gerçekten. Neyse. Harflerle tekerlemeye bir örnek daha verelim ve devam edelim:""".p,
    """for(c<-"merhaba!") satıryaz(c)""".c,
    "Şimdi de matematik, bilhassa kartezyen geometrisi sevenlere bir süprizimiz var. Kaplumbağacığı kullanarak bir eğri çizelim. Neyin eğrisi? İki boyutlu bir poligon. Genel olarak a*x^2 + b*x + c diye yazabiliriz. Yine bu çok faydalı olan 'for' yapısıyla:".p,
    """sil
def eğri(x: Kesir) = 0.001 * x * x + 0.5 * x + 10   // 'def' define yani tanımla demek. Bunu daha sonra daha iyi anlatacağız. 
gridOn();axesOn() // kare çizgileri ve x ve y eksenlerini çizelim
val aralık = 200
atla(-aralık,eğri(-aralık))
for(x <- -aralık+10 to aralık; if (x % 10 == 0)) lineTo(x, eğri(x))
""".c,
    "Eksenleri silelim. Ve bir sonraki bölüme devam edelim!".p,
    "axesOff(); gridOff()".c
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
    """def ağaç(uzaklık: Kesir) {
    if (uzaklık > 4) {
        kalemKalınlığınıKur(uzaklık/7)
        // toInt metodu kesirli sayıyı tam sayıya çeviriyor 
        // yani uzaklık 1.75 olsa uzaklık.toInt 1 oluyor
        kalemRenginiKur(Renk(uzaklık.toInt, math.abs(255-uzaklık*3).toInt, 125))
        ileri(uzaklık)
        sağ(25)
        ağaç(uzaklık*0.8-2)
        left(45)
        ağaç(uzaklık-10)
        sağ(20)
        ileri(-uzaklık)
    }
}

sil()
hızıKur(hızlı)
zıpla(-200)
ağaç(90)
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
"""'override' üstüne yazmak ya da tekrar tanımlamak anlamlarına geliyor. Neyin üzerine yazıyor ve tekrar tanımlıyoruz? toString adlı işlevi. Peki aslı nereden geliyor ki tekrar tanımlayalım? Hani dedik ya herşey bir nesne. Gerçekten de adı Nesne olan bir tür var ve aslında bütün türlerin temelini oluşturuyor. Bu temel sınıf ne demek daha sonraya bırakalım. Ama bilmemiz gereken şey onun toString diye bir metodu olduğu ve bu metodun her nesneyle çalıştığı. String İngilizce'de yazi demek. Bu metod her nesneyi okunabilecek bir yazı olarak ifade ediyor:""".p,
    """val n0 = new Nesne()
satıryaz(n0.toString())

val n1 = 9
satıryaz(n1.toString())

val n2 = Dizin(9, 99)
satıryaz(n2.toString())
// bu 'case class' nedir birazdan göreceğiz!
case class AkıllıSayı(val sayı: Sayı) {
    override def toString() = "Ben bir sayıyım. Değerim de " + sayı + "'dur."
}

val n3 = AkıllıSayı(99)
satıryaz(n3.toString())
""".c,
    """Biz şimdi noktalarımıza dönelim. Nokta sınıfı hazır. Noktalar tanımlayalım:""".p,
    """val n1 = new Nokta(3,4)
val n2 = new Nokta(7,2)
val n3 = new Nokta(-2,2)
val n4 = n1+n2-n3
satıryaz(n4)
""".c,
    "Bu çok daha okunaklı, değil mi? Kocaman bir vektör cebiri yaratmak işten bile değil Scala ve Kojo'yla!".p,
    "Daha önce de söylediğimiz gibi, Scala'da her sınıfın varsayılan bir 'toString' metodu var ve nesnelerin yazıyla ifade edilmelerini sağlıyor ki, örneğin satıryaz komutuna girdi olabilsin. Varsayılan metod o nesneye özgü bir numara ve tür adı içerir. Biz onu tekrar tanımlamış ve daha okunur hale getirmiştik yukarıda.".p,

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
    """Scala dingin türleme (static typing) yapan bir dil. Bu ne demek? Bütün değişkenlerin ve işlevlerin türü, program çalışmadan önce, yani derleme sırasında belirleniyor. Bütün diller böyle değil. Örneğin pek çok yazılımcının favorilerinden olan Python ve Ruby, devingen türlemeli diller (dynamically typed languages). Dingin türleme sayesinde pek çok yazım ve mantık hatası programı çalıştırmadan önce yani derleme sırasında yakalanır. Örneğin, yanlışlıkla bir işleve uygun olmayan tür bir nesneyi girdi olarak sokarsak, derleyici hemen yakalar ve bizden hatayı düzeltmemizi ister. Kısaca, her değerin türünün önceden belli olması hem faydalı hem de gerekli. Ama, buna rağmen yazılımcık örneklerimizde tür tanımlarının pek az olduğunu farketmiş olabilirsin. Bu nasıl oluyor? Çünkü Scala tür çıkarımında çok becerikli. Tür çıkarımı (type inference) bir değerin türünü kullanıldığı yere bakarak belirlemek demek.""".p,

    """Örneğin, 'val x = 3' yazalım. Derleyici elbette 3'ün yalın bir değer olduğunu ve ayrıca ne tür bir sayı olduğunu biliyor. 3 (kesirsiz bir) Sayı. Onun için derleyici 'x' değişkeninin türünün de Sayı olması gerektiği çıkarımında bulunuyor ve elbette yanılmıyor. Az da olsa bir kaç durum var ki derleyici tür çıkarımını beceremiyebiliyor. İşte o zaman, kafam karıştı der ve bir hata verir. Hatanın türü "type ambiguity" gibi birşeylerdir, yani tür belirsizliği. O durumda gerekli tür bilgisini yazılımcığa ekleyiverirsin.""".p,

    "Genelde işlevlerin girdilerinin türünü tanımlarız. Ama çıktının türünü belirtmek gerekmez. Çünkü derleyici işlevin tanımından çıktının türünün çıkarımını yapıverir. Bunun istisnası özyineleyen, yani kendi kendini çağıran işlevler. Pek çok örneğini gördük elbet. Onların çıktısının türünü hep belirttik.".p,

    "Tür çıkarımı sayesinde programların doğru çalışması için yazmamız gereken sözcük ve harf sayısı epey azalır. Bu da elbette çok işimize yarar. Ayrıca yazılım daha okunur hale gelir ve anlaşılması da kolaylaşır. Bu becerisi sayesinde Scala da Python ve Ruby gibi diller gibi sade ve yalın gelir göze.".p

  )
)

pages += Page(
  name = "FAO",
  body = tPage("İşlevler de Birer Nesnedir",
    "İşlevler de Birer Nesnedir".h2,
    """Scala dilinde herşey bir nesnedir demiştik. İşlevler de aynen öyle! Bir işlevi başka işlevlere girdi yapabiliriz. Bir işlevin çıktısı da bir işlev olabilir. Ayrıca değişkenlerin değeri de bir işlevin kendisi olabilir. Bunların örneklerini biraz sonra göreceğiz. Bu özellikler Scala dilinin çok faydalı bir becerisidir. Karşımıza sık sık çıkan bazı zorlukları çok kısa ve güzel bir şekilde çözmemizi sağlarlar. Bunlar arasında program akışını yönlendirme teknikleri de var. Örneğin, Scala dilinde yazılmış eski adı "Actors" yeni adıyla "Akka" birimi (İngilizce'de module ya da library denen ve başka yazılımlar tarafından kullanılan bir yazılım bütününe birim denir) işlevleri nesne olarak kullanma yöntemini kullanarak eşzamanlı programlamayı destekler. Ama biz dizinlerin kullanılmasıyla başlayalım. Bakın göreceksiniz işlevlerin nesne olarak kullanılmasına güzel bir giriş olacak.""".p,
    """Nesneleri bir dizi olarak ele almak çok doğal ve faydalı, değil mi? Ne tür örnekler geliyor senin aklına? Her sözcük, örneğin, bir dizi harften oluşur. Gün içersinde yaptığımız şeyleri bir dizi eylem olarak düşünebiliriz. Scala dili Dizin adını verdiğimiz bir tür tanımlamıştır. Bunu daha önce pek görmedik. Ama aslında çok basit bir kavram. Dizin türü bir dizi nesneyi ele almayı ve işlemeyi çok kolaylaştırır ve hemen hemen her yazılımcık ve daha büyük yazılımlarda sık sık kullanılır. Dizin içindeki nesnelerin belli bir sırası vardır. Dizin türünün sunduğu pek çok metod ve komut sayesinde Dizin tanımlamak ve işlemek kolaylaşır. Daha önce Nokta sınıfıyla gördüğümüz gibi Dizin oluşturmanın bir yolu Dizin sınıfının yapıcı metodunu (constructor) kullanmak. Bu yapıcı metoda bir ya da daha çok girdi sokarız ve yapıcı o girdilerin oluşturduğu bir Dizin yapıverir. İlk örneğimizle hemen başlayalım. Bir dizi sayı oluşturalım. Bu dizinin türü "Dizin[Sayı]" olacak. Buradaki köşeli parantezler dizinin içindeki elemanların türünü belirliyor, yani Sayı. Her eleman bir Sayı olduğu için Scala tür çıkarımı yaparak 'dzn' değişkeninin türünü Dizin[Sayı] olarak belirler. Yani bizim bunu açık açık yazmak için zahmet etmemiz gerekmez.""".p,
    "val dzn = Dizin(1, 7, 2, 8, 5, 6, 3, 9, 14, 12, 4, 10)".c,
    "Tamam, şimdi elimizde bir dizin var. Dizinleri kullanmanın üç temel metodu var. Onları görelim. Her dizinin bir başı olduğu için 'head' yani İngilizcede baş anlamına gelen metod bize ilk elemanı verir. 'tail' kuyruk anlamına gelir, dizinin başı hariç diğer elemanlarından oluşan kısmını verir. Ve son olarak da çift iki nokta üstüste yani '::' bir dizinin başına yeni bir eleman, yani yeni bir baş ekler. Bu baş ve kuyruk deyişi sana yılanları anımsattıysa haklısın. Dizinler yılanlara benziyor: hep başından tutmakta fayda var! Şaka bir yana Dizin türü çok gelişmiştir ve daha pek çok kullanışlı metodu vardır. 'Dizinlerin Kullanılışı' kısmında daha pek çok Dizin metodu göreceğiz.".p,
    "'head' ilk yani en soldaki elemanı verir. dzn örneğinde bu '1' olacak.".p,
    "satıryaz(dzn.head)".c,
    "'tail' başı yani ilk elemanı atlar ve dizinin gerisini verir. Bakın en baştaki 1 sayısı yok kuyrukta:".p,
    "satıryaz(dzn.tail)".c,
    "'::' soldaki elemanı sağdaki dizinin başına ekleyerek oluşan yeni dizini verir.".p,
    "satıryaz(23 :: dzn)".c,
    "Burada önemli bir gözlem yapalım. Bu metodlar dzn dizisini değiştirmez! Hep yeni bir Dizin üretirler. Bunun için Dizin türüne değişmez (immutable) bir veri yapısı da denir.".p,
    "Bu üç temel metodla aklına gelen her dizini tanımlayabilir ve dizinlerle yapılabilecek ne varsa yapabilirsin. Bir örnek olarak gel bizim örnek dizinimizin içindeki tek sayıları bulalım:".p,
    """def tek(girdi: Dizin[Sayı]): Dizin[Sayı] = {
    if (girdi == Boş) Boş
    else if (girdi.head % 2 == 1) girdi.head :: tek(girdi.tail)
    else tek(girdi.tail)
}
""".c,
    "Farkettin mi? Bu işlev dizinin başına bakar ve duruma göre kuyrukla özyineler, yani kendi kendini çağırır. Bunu yaparken 'tail' yani kuyruğu veren metodu kullar ve bu sayede dizinin elemanlarını teker teker ele alır. 'Boş' özel bir değer ve içi boş olan diziyi belirtiyor. Unutmadan, içi boş olan tek dizi var aynı yegane boş küme gibi. Boş yerine şöyle de yazabilirdik: Dizin[Sayı](). Neyseki Boş tanımlanmış. Daha kısa ve anlaşılır oldu değil mi? Dizinin sonuna gelince özyineleme son bulur ve tek sayılardan oluşan yeni bir Dizin çıkar ortaya.".p,
    "Haydi deneyelim.".p,
    "tek(dzn)".c,
    "Ne kadar yalın bir çözüm, değil mi? Tek yerine çift sayıları bulmak da artık çok kolay:".p,
    """def çift(girdi: Dizin[Sayı]): Dizin[Sayı] = {
    if (girdi == Boş) Boş
    else if (girdi.head % 2 == 0) girdi.head :: çift(girdi.tail)
    else çift(girdi.tail)
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
    else if (koşul(girdi.head)) girdi.head :: ele(girdi.tail, koşul)
    else ele(girdi.tail, koşul)
}
""".c,
    "ele(dzn, tekMi)".c,
    """'ele' işlevinin ilk girdisinin adı girdi ve bu bizim sayı dizimiz. İkinci arguman ise eleme koşulumuz, yani yeni tanımladığımız işlev. Türünü nasıl yazdık farkedelim: koşulun türü bir sayıyı girdi olarak alıp bir İkil veren, yani koşul sağlanıyorsa doğru yoksa da yanlış diyen bir işlev. 'ele' adlı metodumuz her işlevi girdi olarak kabul etmez. Sadece Sayı alıp İkil veren işlev türü ile çalışır. 'ele' metodunun içinde de 'koşul' adlı değeri aynı diğer işlevler gibi kullandık. Bu ele adlı işlevi daha önceki tek ve çift işlevleriyle karşılaştırmanı öneririm. Genellemenin nasıl yapıldığını iyice özümsemende fayda var. Bu genelleme günlük hayattaki istisnası bol onun için de bizi sık sık yanıltan genellemelere benzemez. Zararı yok sayılır (ikinci bir girdi girmemiz gerekiyor, yani zahmeti biraz arttı) ama kazancı çok. Birazdan bir kaç örnek daha görünce daha iyi anlayacaksın. Bu arada, eğer çok istersek bu zararı iyice yok edebiliriz. Örneğin, bu işlevi sık sık tek sayıları bulmak için kullanacaksak, şöyle yapalım ki iyice kolaylaylaşsın kullanımı:""".p,
    """def ele(girdi: Dizin[Sayı], koşul: (Sayı) => İkil = tekMi): Dizin[Sayı] = {
    if (girdi == Boş) Boş
    else if (koşul(girdi.head)) girdi.head :: ele(girdi.tail, koşul)
    else ele(girdi.tail, koşul)
}
""".c,
    """Bak bu sayede, eğer işlev girdisi vermezsek, ele adlı işlev tekMi koşulunu kullanacak. Yani varsayılan koşul tekMi ile ifade ettiğimiz tek sayı olmak olacak. Yeni bir örnek iyi olacak:""".p,
    """val ilkOnSayı = (1 to 10).toList""".c,
    """İngilizce 'toList' dilimizde dizine çevir anlamına gelir. Ama dizine çevrilebilen '(1 to 10)' nedir diye merak ediyor olabilirsin. 1'den 10'a kadar sayılar anlamına geliyor. Daha önce görmediğimiz ama çok faydalı olan bu türün İngilizce adı 'Range'. Genel hali şu: '(a to b by c)' yani a sayısından b sayısına kadar c adımlarıyla sayıyoruz. Örneğin:""".p,
    "(3 to 22 by 3)".c,
    "(30 to -33 by -5)".c,
    "Sonuna .toList ekle bakalım ne olacak. Aralıkları araya sıkıştırdık, umarım kafa karıştırmadık. Hemen konumuza dönelım. Nerede kalmıştık? Bir sayı dizisı içinden herhangi bir koşula göre bazı sayıları seçmek istiyorduk. Ama tek sayıları seçmek varsayılan koşulumuz olsun dedik. Deneyelim hemen:".p,
    """ele(ilkOnSayı)""".c,
    """Aynı 'tek' adını verdiğimiz işlev gibi çalıştı, değil mi? Ama, ele adlı işlevimiz hala daha genel. İstersek ikinci arguman olarak başka bir koşul girer, başka tür bir eleme yaparız. Hemen geliyor bir örnek!""".p,
    "Çift sayıları bulmak için de benzer bir yöntem kullanabiliriz elbet. Yani çiftMi diye yeni bir işlev tanımlar ve sonra kullanırız. Ama adsız (anonymous) bir işlev kullanarak çok daha kısaca ifade edebiliriz dileğimizi. Ne demek adsız? Yani 'def' özel sözcüğünü kullanmadan ve işlevin adını vermeden bir işlev tanımlayacağız. Bu işlev basitçe girdiyi alacak ve onu değerlendirecek. Bu özel yöntemde '=>' imini kullanıyoruz. Solunda girdiler, sağında da işlevin eylemleri gelecek.".p,
    "ele(dzn, (v: Sayı) => v % 2 == 0)".c,
    "'ele' komutunu böyle çağırarak çift sayıları buluverdik. Yeni bir çiftMi işlevi tanımlamamıza gerek kalmadı. Adsız işlevler hep böyle kullanılır ve işimizi çok kolaylaştırırlar. O kadar faydalılar ki özel bir adları ve matematik ve bilgisayar biliminde özel bir yerleri bile var. 'lambda kalkülüsü' (lambda calculus) diye bilinir. Bilgisayar bilimi okumak istersen ilerde görür ve daha iyi öğrenirsin.".p,
    "Genelleyici Programlamanın Tadına Bakalım".h3,
    "Şimdi diyelim ki yeni bir eleme koşulu tanımlamak istiyorsun, çünkü tam sayılarla yetinmek olmaz, kesirli sayıları da herhangi bir koşula göre eleyebilmek istiyorsun. Yukarıda 'def' ile tanımladığımız kısacık 'ele' işlevini kopyalar ve basitçe her gördüğün 'Sayı' sözcüğünü 'Kesir' sözcüğüyle değiştirirsin olur biter. Sonra da adını eleKesir gibi birşey koyarsın. Tabii Kesir de yetmez. Başka her hangi bir tür için eleHerneyse dersin ve bu sayede birbirine çok benzeyen, daha doğrusu neredeyse apaynı bir sürü işlevin olur: ele, eleKesir, eleYazı, vb.. Ne güzel, değil mi? Hiç de güzel değil! Çok tekrar oldu. Nerede çokluk orada çok ayıp...".p,

    "Scala'yla yazarken bu kadar tekrara, bu zahmete girmeye hiç gerek yok. İşi iyi bilenlerin yaptığı gibi bir 'tür değişkeni' (type variable ya da generic type diye de bilinir) kullanarak genel bir çözüm sayesinde işler kolaylaşıverir. Tür değişkenleri diğer 'val' değişkenlerine benzer. Gerçek değer yerine değişkenin adını kullanırız her yerde. Sonra Scala derleyicisi gerekli yerleştirmeleri yapıverir".p,

    "Aşağıda 'T' harfini kullanacağız Tür anlamında. Ama başka her hangi bir harf ya da sözcük de olur elbette. Nerede Sayı varsa onun yerine T gelecek. Bir de işlevin adının hemen arkasında [T] gelecek. Bu da yeni işlevimizin genelleyici bir işlev olduğunu belirtiyor. Peki o halde yeni halini görelim:".p,

    """def ele[T](girdi: Dizin[T], koşul: (T) => İkil): Dizin[T] = {
    if (girdi == Boş) Boş
    else if (koşul(girdi.head)) girdi.head :: ele(girdi.tail, koşul)
    else ele(girdi.tail, koşul)
}
""".c,
    "ele(dzn, (v: Sayı) => v % 2 == 0)".c,
    "Aynı eskisi gibi çalıştı! Ama işin iyi tarafı çok daha genel oldu bu haliyle. Bak şimdi kesirli sayılar arasından sadece beşten büyük olanları seçmek de çok kolay olacak:".p,
    """val kesirler = Dizin(1.5, 7.4, 2.3, 8.1, 5.6, 6.2, 3.5, 9.2, 14.6, 12.91, 4.23, 10.04)""".c,
    """ele(kesirler, (v: Kesir) => v > 5)""".c,
    "Ya da bir cümle içindeki uzunca sözcükleri seçmek için de kullanabiliriz aynı genellenmiş ele adlı işlevimizi:".p,
    """val dizinler = Dizin("Bugün", "sizi", "gördüğüme", "çok", "memnun", "oldum", "Nasılsınız")""".c,
    """ele(dizinler, (v: Yazı) => v.length > 4)""".c,
    "length uzunluk demek İngilizce'de. Yazı türünün metodu olarak kaç harf olduğunu gösterir. Dizinlerle ilgili bölümde de göreceğimiz gibi Dizin türünün 'filter' adlı bir metodu var. Onu kullanarak da benzer eleme işlemlerini yapabiliriz:".p,
    "dizinler.filter((v: Yazı) => v.length < 5)".c,
    "Bu genelleyici işlevlere daha sonra yine bakacağız ve başka özelliklerini keşfedeceğiz. Ama şimdilik konumuza dönelim ve işlevleri nesne olarak kullanmaya bakalım.".p,
    "'İşlevler De Birer Nesnedir' Hakkında Başka Birkaç Şey Daha".h3,
    "Biraz önce örneklerini gördüğümüz yönteme 'kapsamalar' (comprehensions) deniyor. Yani, dizin gibi bir sürü elemandan oluşan bir kümenin bütün elemanlarına bir işlevi uygulayıvermek. Kapsamaların ne kadar becerikli olduklarını ve onlar sayesinde kısacık ama çok iş beceren yazılımlar yapabildiğimizi bir kaç örnek daha görerek pekiştireceğiz şimdi.".p,

    "Yazılımcıklar yazdıkça, bazı çok basit ve çoğunlukla adı olmayan işlevleri başka işlevlere girdi yapmak isteyeceksin. O durumlarda bazı kısa yollar bilmende fayda olacak. Daha önce de üzerinde durduğumuz gibi tür çıkarımı bize fayda sağlıyor. Bakın bir önceki dizinler.filter örneğini tür çıkarımı sayesinde sadeleştirelim. Derleyici adsız işlevin girdisinin Yazı türünde olması gerektiğini biliyor (nasıl biliyor? Adı dizinler olan val değerinin türünden!). O sayede parantezleri ve tür bilgisini yazmaktan kurtuluyoruz.".p,
    "dizinler.filter(v => v.length > 3)".c,
    "Bu adsız işlev gibi iki ve ya bir girdili işlevler çok yaygın olduğu için, Scala dili bize bir kısa yol daha sunuyor. '(x, y) => x + y' adsız işlevini şöyle yazabiliyoruz: '_ + _'. Benzer şekilde 's=>s.Method' yerine '_.Method' da yazabiliriz. _ imi girdi yerine kullanılıyor yani. O sayede adsız işlevler içinde isimler uydurmak ve kullanmaktan kurtuluyoruz. Bu sayede, son örneğin en kısa halini görelim:".p,
    "dizinler.filter(_.length > 3)".c,
    "Başkalarının yazdığı kodları okumak çok faydalıdır. O durumlarda burada gördüğümüz kısa yolları görünce artık şaşırmazsın. Ama bazı durumlarda uzun ve açık hallerini kullanmak gerekir. Onları da sonra göreceğiz.".p,
    
    "Bir kaç tane daha yaygın kullanımı olan örnekler görelim. Bunlar dizinleri işlemekte çok faydalı olurlar. Sen de yakında göreceksin.".p,
    "flatMap".h4,
    "dizinler.flatMap(_.toList)".c,
    "Flat İngilizce'de kat kat olmayan yani düz anlamında kullanılır. Bu örnekte bizim yazı dizinimizi aldık, içindeki her bir yazıyı ilk önce toList metodunu kullanarak birer harf dizisine çevirdik ve onlarin hepsini birleştirdik. Sonucunu gördün, değil mi? flatMap yerine map metodunu kullanarak aradaki farkı daha iyi anlarsın. map İngilizce'de hem harita hem de eşlemek anlamlarına gelir. Burada ikinci anlamını kullanıyoruz:".p,
    "dizinler.map(_.toList)".c,
    "sort".h4,
    "Sort da sıraya dizmek anlamına gelir. sortWith yani kendi girdiğimiz bir koşul ile sıralıyoruz. Sözcükleri a'dan z'ye sıralayalım:".p,
    "dizinler.sortWith(_ < _)".c,
    "Bir de ters sıraya sokalım:".p,
    "dizinler.sortWith(_ > _)".c,
    "Farkettiysen büyük harfliler başta geliyor. Onun yerine harflerin büyük küçük olduğuna bakmadan sıralamak istersek şöyle yaparız:".p,
    "dizinler.sortWith(_.toUpperCase < _.toUpperCase)".c,
    "Yani hepsini büyük harfe çeviriverdik karşılaştırmadan önce. İştediğimiz koşulu girerek istediğimiz şekilde sıralama yapıverdik. Yani sıralama metodunu baştan yazmamız gerekmedi. İşte işlevlerin nesne olmasının faydaları!".p,
    "fold".h4,
    "fold İngilizce'de katlamak demek. Soldan ve sağdan katlamak anlamına gelen foldLeft ve foldRight bir dizinin elemanlarını bir araya getirmekte kullanılan çok yaygın ve faydalı metodlardır. Elemanları nasıl bir araya getirmek istediğimizi iki girdi alan bir işlev girerek belirtiriz. Bu birleştirme işlemi soldan ya da sağdan başlar. Ve başlarken de yine girdiğimiz bir değer kullanır. Yani iki tane girdisi var bu fold metodlarının.".p,
    "Bu iki girdiyi daha kolay okunsun diye iki parantez grubuyla gireriz. Birazdan bunu 'def' ile işlevi tanımlarken nasıl yapıldığını göreceğiz.".p,
    "Yeni bir örnekle başlayalım.".p,
    """val dzn = Dizin(1, 7, 2, 8, 5, 6, 3, 9, 14, 12, 4, 10)""".c,
    """dzn.foldLeft(0)(_ + _)""".c,
    "Soldan işleyeceğiz. İkinci girdimiz adsız bir işlev ve iki sayıyı topluyor: '_+_'. Bunun kapsama olduğunu da biliyoruz, yani dizinin içindeki bütün elemanların üstünden geçecek. İlk önce ilk girdisi olan 0 ile dizinin başındaki 1'i toplar ve 1 bulur. Sonra ona ikinci eleman olan 7'yi ekler ve böylece sonuna kadar gider.".p,
    "Tekrarlardan Kurtulalım".h4,
    "Diyelim ki bir cümlemiz var ve içinde hangi harfleri kullandığımızı bulmak istiyoruz. Örneğin,".p,
    """val dilek = Dizin("Haydi", "gelin", "bir", "oyun", "oynayalım", "hep", "beraber")""".c,
    """dilek.flatMap(_.toList).map(_.toUpper).distinct.sortWith(_ < _)""".c,
    "'flatMap' sözcüklerin bütün harflerini tek bir dizine sokuyor. Daha önce de gördüğümüz 'map' harfleri büyük harfe çeviriyor. distinct farklı demek, yani sadece farklı harfleri veriyor, tekrarları vermiyor. En sonunda da a'dan z'ye sıralıyoruz.".p,
    
    "Kendi akış yöntemimizi tanımlayalım".h4,
    "İşlevlere girdi olarak adlı ya da adsız başka işlevler girebildiğimiz gibi bir dizi komut da girebiliriz. Bu sayede kendi akış yöntemlerimizi yaratabiliriz. Örneğin, bir kare çizmek için kaplumbağa komutlarını yineleyebilmek iyi olur, değil mi? Daha önce gördüğümüz 'for' yapısını kullanabiliriz:".p,
    """sil
for (i <- 1 to 4) { ileri(100); sağ() }
""".c,
    "Ama, aşağıdaki gibi yazabilsek çok daha iyi olmaz mı?".p,
    "yinele(4) { ileri(115); sağ() }".c,
    "Scala dili 'if/else' ve 'while' gibi yapılar sunar ama 'yinele' yapısı Kojo tarafından tanımlanmıştır. Bunu sen de Scala ile yapabilirsin. Şöyle başlayalım:".p,
    """def yinele2(ys: Sayı, dk: => Her) {
    for (i <- 1 to ys) dk
}
""".c,
    "ys yineleme sayısının kısaltması. dk de dizi komut anlamında.".p,
    "Şu ana kadar işlevlere girdiğimiz bütün girdiler değer olarak yollandı (pass by value denir İngilizce). Yani işlev çağrılmadan önce değer hesaplanır ve işleve yollanır. yinele2 tanımında kullandığımız kalın ok yani => imi durumu değiştiriyor. dk yani dizi komut olduğu gibi (call by name denir) yollanıyor yinele2 işlevine. Yani dizi komutların değerlendirilmesi geciktirilir. Ancak işlevin işleme sırası geldiğinde işlevin içine girilince dizi komut çalıştırılır. Bu yinele2 işlevimizde for döngüsü ys sayısı kadar çalışacak ve her seferinde dk yani girilen dizi komut çalıştırılacak.".p,
    "yinele2(4, { ileri(130); sağ() })".c,
    "Hiç fena değil. Neredeyse istediğimiz hale geldi. Tek sorun 4 sayısından sonra gelen virgül. Scala istersek her girdiyi kendi parantezi içinde girmemizi destekler. Buna 'curried işlev' de denir. Haskell Curry adında bir matematikçi buna esin kaynağı olduğu için. Bakın şöyle değiştirelim tanımı:".p,
    """def yinele3(ys: Sayı)(dk: => Her) {
    for (i <- 1 to ys) dk
}
""".c,
    "Bu sayede tam istediğimiz gibi yazabiliriz:".p,
    "yinele3(4) { ileri(160); sağ() }".c,
    "Biraz önce foldLeft örneğinde de bu tekniği kullanmıştık. Daha okunur hale geldi değil mi?".p,
    "Yazılıma hobi ya da işin olarak devam edersen işlevlerin nesne gibi kullanılabilmesinin daha pek çok programlama problemini çözmede faydalı olduğunu göreceksin. Başka yolları da var elbet ama buradaki örneklerde de gördüğümüz gibi bu teknikle kodumuz epey kısa ve okunur hale geldi. Daha kapsamlı ve tasarlaması, yazması ve idare etmesi esaslı bir iş olan bazı örnekleri de şimdiden duymuş olman için kısa bir liste vereyim. Merak ettikçe, yeri geldikçe incelersin. Kusura bakma bunlar önce İngilizce olsun: (1) passing callback functions in event driven IO, (2) passing tasks to Akka Actors in concurrent processing environments, (3) in scheduling work loads. Yani, (1) olgu güdümlü girdi/çıktı idaresinde geriçağırım yapan işlevleri dağıtmak (2) eşzamanlı işlemler olan ortamlarda, yani aynı anda birden fazla işlemin paralel yani yanyana çalışması durumunda bağımsız aktörlere yeni işlevler verilmesi, (3) uzun işlerin sıralamasını düzenlerken işlevleri dağıtmak. Bilgisayar bilimi ve mühendisliği artık o kadar gerekli bir hale geldi ki, bu temeli iyi bilmek ilerde çok helal kazanç kazanmana faydalı olacaktır. Tabii eğer gönlünü bu işe verirsen.".p,
    "Bir Tür Girdiyi Yinelemek".h4,
    "Bazen işlevimize kaç tane girdi gireceğini bilemeyiz. Daha doğrusu kaç tane girilirse girilsin çalışmasını isteriz. Örneğin bu kılavuzcukta çok kullandığımız 'satıryaz' komutu öyledir. Bir, iki, üç... ne sayıda girdi girersek girelim işini yapar. Bunun yolu da çok kolay:".p,
    """def topla(s: Sayı*) = s.reduce(_ + _)""".c,
    """topla(1, 2, 3)""".c,
    """topla(4, 5, 6, 7, 8, 9, 10)""".c,
    "Girdinin türünden sonra gelen yıldız imi, yani '*' sayesinde 's' girdisi tek bir sayı değil bir dizi sayı oluveriyor. Onun için de 'reduce' metodunu kullandık. Bu foldLeft metoduna benzer ama daha basittir. Bakın girilen bütün sayıları toplamak bu kadar kolay. Bazı istisnalara da bakalım, ne olacak?".p,
    """topla(99) // pek toplamaya gerek olmasa da yine de tek değerle de çalışması güzel!""".c,
    """topla() // Bak ne oldu? Bunu tamir edebilir misin?""".c,
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
    "Böyle birşey yapmak gereksiz geldi mi size de? Tabii genelde elimizde val değişkenler olur. Örneğin:".p,
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
    """val sözcükDizini = yazı.split(" ").toList""".c,
    "Bu hesaplamayı yapmak için uygulamayı düşündüğümüz yöntem şu: ilk önce harfleri büyültelim sonra da sıraya sokalım.".p,
    "Ondan sonra da katlama (fold) metodunu kullanarak arka arkaya tekrar eden harfleri sayıvereceğiz. Daha önce ne görmüştük? Katlama işlevi iki girdi alıyor: bir başlangıç değeri, bir de bir dizin, yani bizim sıraya sokulmuş harf dizimiz. Katlama ilk başlangıç değerini dizinin ilk elemanıyla bir işleme sokacak. Ne işlemi mi? Biz ne istersek o! Burada teker teker ele aldığımız harfler değişmedikçe sayısını bir artıracağımız bir sayacımız olacak. Yeni gelen harf değişik olursa yeni bir sayaç tanımlayacağız. Her sayaç tabii ki birden başlayacak. Karmaşık mı geldi biraz? Çok doğal. Görüp biraz üstünde düşününce daha anlaşılır olacak. Ne de olsa ileri programlama tekniği bu!".p,
    "Kısaca söylemek gerekirse amacımız ikili sıralamalardan oluşan bir dizin oluşturmak. Her ikilinin birinci alanında bir harf ikinci alanında da kaç tane olduğunu tutan sayacı olacak. Bu dizin harflerin sıklığını sunacak bize.".p,
"""// Tek satırda epey iş var. Teker teker bak istersen
// flat: düz, map: eşleme, toList: dizine çevir, toUpper: yüksek harfe çevir, sortWith: işlev ile sırala
val harfler = sözcükDizini.flatMap(_.toList).map(_.toUpper).sortWith(_ < _)
// Yani şöyle yapabilirsin:
val g1 = sözcükDizini.flatMap(_.toList); satıryaz("g1", g1)
val g2 = g1.map(_.toUpper); satıryaz("g2", g2)
""".c,

    """Şimdi de katlama yöntemimiz gelsin bakalım. Başladığımızda sunum boş olacak elbet. Nasıl tanımlarız istediğimiz boş dizini? "Dizin[(Harf, Sayı)]()" yani bir dizi (harf, sayaç) çifti. Katlama metodumuz (foldLeft) ikinci girdi olarak ne bekler anımsadın mı? Bir işlev! Nasıl bir işlev gerekiyor biraz daha iyi tahmin edebilirsin belki şimdi. Adsız işlev olacak, bir. İki tane girdisi olacak, iki. İlk girdisi bizim çift dizinimiz, ikinci girdi de harflerden biri.""".p,

"""val sıklık = harfler.foldLeft(Dizin[(Harf, Sayı)]()) {
    case ((önceki, sayaç) :: kuyruk, harf) if (önceki == harf) => (önceki, sayaç + 1) :: kuyruk
    case (sunum, harf)                                         => (harf, 1) :: sunum
}
""".c,
    "Bunu anlayamadım diye üzülme sakın! Daha önce görmediğimiz bir kaç becerisi var Scala derleyicisinin burada!".p,

    "1) adsız işlevimizi tanımlarken 'case' yani örüntü eşleme yapısı kullanabiliriz. Bunun için normal parantez yerine kıvrık parantez kullanmamız yeter. 'match' özel sözcüğüne gerek kalmadı. Ondan önce gelen değişkenlere de! Yani bu epey faydalı bir kısa yol oluyor ve bunu iyi bilmekte fayda var! Normal, yani kısaltılmamış halini anımsayalım hemen:".p,
    "(a, b) => (a, b) match {case ... => ...; case ... => ...}".p,
    "match ve ondan önceki hiç birşeye gerek kalmıyor!".p,

    "2) örüntülü eşleme yapmak için kullanmıştık bu 'case' yöntemini. Burada da 'case' sözcüğünden hemen sonra gelen kısımda elimizdeki iki girdiyi çözümlüyoruz. Biraz önce de dediğimiz gibi ilk girdi ufak ufak oluşturduğumuz yeni dizinimiz. '(önceki, sayaç) :: kuyruk' yeni kurduğumuz dizinin başı ve kuyruğuyla eşleşiyor ve onların üçüne de isim takıveriyor. kuyruk bariz. baş eleman da bir önceki harf ve ondan şu ana kadar kaç tane saydığımızı tutan sayaç. 'kuyruk' değerinden sonra gelen 'harf' ise katlama işlemini yaptığımız 'harfler' dizinindeki harflerden biri. Katlama işlevi her harfin üstünden teker teker geçecek elbet.".p,

    "3) ve son! İlk örüntü eşleme satırında bir de koşul girdik 'if' diyerek. Bu çok önemli. Yeni bir harfe geçip geçmediğimize dikkat etmemiz gerek! Eğer en son saydığımız harften aynısı geldiyse sayaçı arttırmalıyız. Yoksa yeni bir sayaç başlatmalı.".p,

    "Şimdi, bilgisayın Scala derleyicisi sayesinde anladığı bu epey karmaşık görünen işlemi okuyalım bakalım daha iyi anlamış mıyız: esas girdimiz olan sıralanmış harfler dizisindeki her harf için teker teker şunu yapalım: eğer sıfırdan oluşturduğumuz sunum adlı yeni çift dizisinin başındaki çiftin harfi ile aynıysa, dizinin başını sayaçın bir arttığı yeni bir başla değiştirelim. Yoksa, sunum dizisine elimizdeki harf icin yeni bir baş ekleyelim ve harfin sayacını bire eşitleyelim. Çok da karmaşık değilmiş, öyle mi?".p,

    "İki üç satırda ifade edebildiğimiz bu işlem hem epey düzgün hem de epey etkileyici bence. Ya sen ne dersin?".p,

    "Sonucu okunur bir halde yazalım bakalım ne bulduk:".p,

    """satıryaz(sıklık.map{p => s"${p._1}:${p._2}"}.mkString(" "))""".c,

    "Elbette ki isteyen daha eski dillerde yapıldığı gibi yazabilir, Basic, Fortran, C, C++, Java örneğin. Yani:".p,

"""def harfSıklığı(girdi: Dizin[Harf]): Dizin[(Harf, Sayı)] = {
    var sunum = Dizin[(Char, Sayı)]()
    if (girdi.isEmpty) sunum
    else {
        var önceki = girdi.head
        var kuyruk = girdi.tail
        var sayaç = 1
        while (!kuyruk.isEmpty) {
            if (kuyruk.head == önceki) sayaç += 1
            else {
                sunum = (önceki, sayaç) :: sunum
                sayaç = 1
                önceki = kuyruk.head
            }
            kuyruk = kuyruk.tail
        }
        (önceki, sayaç) :: sunum
    }
}

satıryaz(harfSıklığı(harfler))""".c,

    "Bu da çalıştı elbette. Ama bilmem sen hangisini daha çok beğendin. Bak biraz önce işlevsel programlama esaslarını kullanarak nasıl yaptığımızı hepsi bir arada tekrar görelim ve iki farklı yöntemi yan yana karşılaştıralım:".p,
    """val harfSıklığı = "Kojo ile oyun oynayarak Scala dilini öğrenmek ve hatta işlevsel ve nesneye yönelik programlama becerisi edinmek harika değil mi".
    split(" ").toList.flatMap(_.toList).map(_.toUpper).sortWith(_ < _).
    foldLeft(Dizin[(Harf, Sayı)]()) {
        case ((önceki, sayaç) :: kuyruk, harf) if (önceki == harf) => (önceki, sayaç + 1) :: kuyruk
        case (sunum, harf)                                         => (harf, 1) :: sunum
    }

satıryaz(harfSıklığı.
    sortBy(p => p._2).reverse.
    map { p => s"${p._1}:${p._2}" }.
    mkString(" "))
""".c,
    "Bilmem farkettin mi ama sadece iki komut dizisi yeterli oldu. Yerilen cümlenin harflerinin sıklığını hesapladık ve sıraya sokup yazdırdık. Yukarıda adlarını saydığımız daha eski ve daha az becerikli dillerle bilgisayara istediğini yaptırmanın ne kadar zor olduğunu bilenlere bu kısacık yazılım o kadar etkileyi olur ki anlatamam!".p

  )
)

pages += Page(
  name = "MF",
  body = tPage("Matematiksel İşlevler",
    "Matematik"h3,
    "Matematik fonksiyonları bazen işimize çok yararlar. Birkaç tanesini görelim. Önce matematik birimindeki komutları çağırmak gerekiyor:".p,
    "import math._".c,
    "Değişmez (sabit) Değerler".h3,
    "Matematik sınıfında iki çok meşhur sabit var:".p,
    table(
      row("E".c,"e, meşhur matematikçi Euler'den gelir adı 2.718282... değerindedir, doğal logaritmanın da tabanıdır."),
      row("Pi".c,"pi sayısı, 3.14159265 .... yani yarıçapı 1 olan dairenin çevre uzunluğunun yarısıdır.")
    ),
    "Trigonometri fonksiyonları".h3,
    "Trigonometrik işlevler girdi olarak radyan birimi kullanırlar. Radyan kavramını anlatan çok güzel bir örneğimiz var Kojo'da. Örnekler menüsünde en altta Matematik Öğrenme Birimleri menüsü var. Onun en altında 'Açı Nedir?' var. Ona tıklayıver. Günlük hayatta biz 90 derece, 180 derece gibi bize daha doğal gelen derece birimini kullanırız açıları ifade etmek için. Radyandan dereceye çevirmek için 'toDegrees' metodunu, tersini yapmak için de 'toRadians' metodunu kullanabiliriz. Bilmemiz gereken tek şey şu: 2*PI radyan 360 dereceye eşittir. Aşağıda sıraladığımız metodlardan başka yay metodları da var. İngilizce arc diye geçer.".p,
    "Kısa not: aşağıdaki tanımlarda G1, G2, ... ile fonksiyona girilen değerleri ifade ediyoruz kısaca. Yani işlevAdı(G1, G2, G3, ...). ".p,
    table(
      row("sin(Pi/6)".c,"G1 girdisinin yani burada Pi/6 değerinin sinüsü."),
      row("cos(Pi/6)".c,"G1 girdisinin cosinüsü."),
      row("tan(Pi/6)".c,"G1 girdisinin tanjantı."),
      row("toRadians(45)".c,"G1 (derece olsun) girdisini radyana çevirir."),
      row("toDegrees(Pi/2)".c,"G1 (radyan olsun) girdisini dereceye çevirir.")
    ),
    "Üst bulma metodları".h3,
    "Logaritma ve üst bulmak için iki temel işlev var. İkisi de E tabanını kullanır (Math.E)".p,
    table(
      row("exp(Pi)".c,"e (2.71...) sayısının G1 üssünü hesaplar."),
      row("pow(6,3)".c,"G1 girdisinin G2 üssünü bulur."),
      row("log(10)".c,"G1'in E tabanina göre logaritmasını verir.")
    ),
    "Başka bazı fonksiyonlar".h3,
    table(
      row("sqrt(225)".c,"G1'in karekökü."),
      row("abs(-7)".c,"abs absolute sözcüğünün kısaltması. Mutlak değeri verir. Girdinin türü neyse, çıktı da aynı tür sayı olur, Sayı, Uzun, KısaKesir, Kesir."),
      row("max(8,3)".c,"maximum sözcüğünden. G1 ve G2 arasında büyük olanı bulur."),
      row("min(8,3)".c,"minimum sözcüğünden. G1 ve G2 arasında küçük olanı bulur.")
    ),
    "Sayı türüyle ilgili metodlar".h3,
    "Bu işlevler kesirli sayıları tam yani kesirsiz sayıya çevirir. Ama dikkat, çıktı türü hala Kesir olabilir.".p,
    table(
      row("floor(3.12)".c,"Yer anlamına gelir. G1 girdisinden küçük ya da ona eşit olan en büyük tam sayıyı hesabeder."),
      row("ceil(3.12)".c,"Tavan anlamına gelir. G1 girdisinden büyük ya da ona eşit olan en küçük tam sayıyı verir."),
      row("rint(3.51)".c,"RoundInteger'dan. G1 değerine en yakın tam sayıyı Kesir türünde bir çıktı olarak verir."),
      row("round(3.48)".c,"G1 kesirine en yakın sayıyı Uzun türünde çıktı olarak verir."),
      row("round(2.6F)".c,"G1 KısaKesir'ine en yakın sayıyı Sayı türünde çıktı olarak verir.")
    ),
    "Rasgele (Random) sayılar".h3,
    table(
      row("random".c,"0.0 ile 1.0 arasında rastgele bir sayı verir. Çıktı türü Kesir olur. Gerçekten rasgele olmasa da, çok yakındır. Pseudo-random denir tam doğru anlamıyla. Birkaç kere tıkla bak ne olacak.. İşte rastgele.")
    )
  )
)

pages += Page(
  name = "OPA",
  body = tPage("İşlem Önceliği ve Birleşmeliği",
    "İşlem Önceliği".h3,
    "Operators are any valid identifier, but their precedence within expressions is according to the table below, highest precedence first. The precedence of multi-character operators is defined by the first character. For example an operator +* would have the precedence given by the + sign.".p,
    "(all other special characters)".p,
    """( * / % , + - : , = ! , < > , & , ^ , | ) highest precedence on left""".p,
    "(all letters)".p,
    "(all assignment operators) eg = += -= *= /= etc".p,
    "İşlem Birleşmeliği".h3,
    "The associativity of an operator in Scala is determined by its last character. Any method that ends in a ':' character is invoked on its right operand, passing in the left operand. Methods that end in any other character are the other way around. They are invoked on their left operand, passing in the right operand. So a * b yields a.*(b), but a ::: b yields b.:::(a).".p
  )
)

pages += Page(
  name = "US",
  body = tPage("Yazıların (String) Kullanılışı",
    "Yazı Türü".h2,
    "Ekrana yazı yazmak bilgisayar programlamada epey sık karşılaşılan bir sorun! Here are some useful functions and definitions. You will find that other sequences like lists have similar methods.".p,
    "Note once again that - in the descriptions that follow, all the function parameters are labeled in order, and so will be of the form G1.functionName(G2, G3...).".p,
    "Escape characters for strings.".h3,
    table(
      row("""\n""", "line feed","""\b""", "backspace","""\t""",      "tab","""\f""", "form feed"),
      row("""\r""", "carriage return","""\" """, "double quote", """\'""", "single quote","""\\""", "backslash")
    ),
    "Concatenation".h3,
    "Yazıs can be concatenated using the + symbol. The original strings are left unaffected. Yazıs are immutable.".p,
    """val a = "Big"
val b = "Bang"
val c = a + " " + b        
satıryaz( a,b,c)
""".c,
    "Nearly all objects have a toString method to create a character representation.".p,
    """val x = (2).toString + " " + (3.1F).toString
satıryaz(x)
""".c, 
    "Length".h3,
    table(
      row(""""four".length""".c,"length of the string G1.")
    ),
    "Comparison".h3,
    table(
      row(""""high".compareTo("higher")""".c,"compares to G1. returns <0 if G1 < G2, 0 if ==, >0 if G1>G2"),
      row(""""high".compareToIgnoreCase("High")""".c,"same as above, but upper and lower case are same"),
      row(""""book".equals("loot")""".c,"true if the two strings have equal values"),
      row(""""book".equalsIgnoreCase("BOOK")""".c,"same as above ignoring case"),
      row(""""book".startsWith("bo")""".c,"true if G1 starts with G2"),
      row(""""bookkeeper".startsWith("keep",4)""".c,"true if G2 occurs starting at index G3"),
      row(""""bookmark".endsWith("ark")""".c,"true if G1 ends with G2")
    ),
    "Searching".h3,
    """Note: All "indexOf" methods return -1 if the string/char is not found. Indexes are all zero base.""".p,
    table(
      row(""""rerender".contains("ren")""".c,"True if G2 can be found in G1."),
      row(""""rerender".indexOf("nd")""".c,"index of the first occurrence of Yazı G2 in G1."),
      row(""""rerender".indexOf("er",5)""".c,"index of Yazı G2 at or after position G3 in G1."),
      row(""""rerender".indexOf('r')""".c,"index of the first occurrence of char G2 in G1."),
      row(""""rerender".indexOf('r',4)""".c,"index of char G2 at or after position i in G1."),
      row(""""rerender".lastIndexOf('e')""".c,"index of last occurrence of G2 in G1."),
      row(""""rerender".lastIndexOf('e',4)""".c,"index of last occurrence of G2 on or before position G3 in G1."),
      row(""""rerender".lastIndexOf("er")""".c,"index of last occurrence of G2 in G1."),
      row(""""rerender".lastIndexOf("er",5)""".c,"index of last occurrence of G2 on or before position G3 in G1.")
    ),
    "Getting parts".h3,
    table(
      row(""""polarbear".charAt(3)""".c,"char at position G2 in G1."),
      row(""""polarbear".substring(5)""".c,"substring from index G2 to the end of G1."),
      row(""""polarbear".substring(3,5)""".c,"substring from index G2 to BEFORE index G3 of G1.")
    ),
    "Creating a new string from the original".h3,
    table(
      row(""""Toni".toLowerCase""".c,"new Yazı with all chars lowercase"),
      row(""""Toni".toUpperCase""".c,"new Yazı with all chars uppercase"),
      row(""""  Toni   ".trim""".c,"new Yazı with whitespace deleted from front and back"),
      row(""""similar".replace('i','e')""".c,"new Yazı with all G2 characters replaced by character G3."),
      row(""""ToniHanson".replace("on","er")""".c,"new Yazı with all G2 substrings replaced by G3.")
    ),
    "Methods for Converting to Yazı".h3,
    table(
      row("String.valueOf(Dizin(1,2,3))".c,"Converts G1 to Yazı, where G1 is any value (primitive or object).")
    )
  )
)

pages += Page(
  name = "UL",
  body = tPage("Dizinlerin (List) Kullanılışı",
    "Dizinler".h2,
    "Dizins provide a common sequence structure that is used for many functional style algorithms. The following functions enable Dizins to be manipulated easily and effectively. The first example creates the Dizin that is used for other examples.".p,
    
    "Note:  _+_ is a shorthand for an anonymous function x,y=>x+y. Since binary operators are frequently used, this is a nice abbreviation. Similarly _.method is a shorthand for v => v.method. When there is more than one argument, the first underscore represents the first argument, the second underscore the second one, and so on.".p,
    
    table(
      row("""val dzn = "Tempus" :: "fugit" ::
  "irreparabile" :: Boş""".c,"""Creates a new Dizin[Yazı] with the three values "Tempus", "fugit", and "irreparabile" """), 
      row("Dizin()".c,"or use Boş for the empty Dizin"),
      row("""Dizin("Time", "flys", "irrecoverably")""".c,"""Creates a new Dizin[Yazı] with the three entries "Time", "flys", and "irrecoverably" """),
      row("""Dizin("tick", "tock") ::: Dizin("cuk", "oo")""".c,"Operator that concatenates two lists"),
      row("dzn(2)".c,"Returns the item at 0 based index 2 in dzn"),
      row("dzn.count(str => str.length == 5)".c,"Counts the string elements in dzn that are of length 5"),
      row("""dzn.exists(str => str == "irreparabile")""".c,"""Determines whether a string element exists in dzn that has the value "irreparabile" """),
      row("dzn.drop(2)".c,"""Returns dzn without the first 2 elements (returns Dizin("irreparabile"))"""),
      row("dzn.dropRight(2)".c,"""Returns dzn without the rightmost 2 elements (returns Dizin("Tempus"))"""),
      row("dzn.filter(str => str.length == 5)".c,"Returns a list of all elements, in order, from dzn that have length 5"),
      row("dzn.flatMap(_.toList)".c,"Applies the given function f to each element of this list, then concatenates the results"),
      row("""dzn.forall(str =>str.endsWith("e"))""".c,"""true if all elements in dzn end with the letter "e" else false"""),
      row("dzn.foreach(str => print(str))".c,"Executes the print function for each of the strings in the dzn"),
      row("dzn.foreach(print)".c,"Same as the previous, but more concise"),
      row("dzn.head".c,"Returns the first item in dzn"),
      row("dzn.tail".c,"Returns a list that is dzn without its first item"),
      row("dzn.init".c,"Returns a list of all but the last element in dzn"),
      row("dzn.isEmpty".c,"true if dzn is empty"),
      row("dzn.last".c,"Returns the last item in dzn"),
      row("dzn.length".c,"Returns the number of items in the dzn"),
      row("""dzn.map(str => str + "?")""".c,"""Returns a list created by adding "?" to each string item in dzn"""),
      row("""dzn.mkString(", ")""".c,"Makes a string with the elements of the list"),
      row("dzn.filterNot(str => str.length == 4)".c,"Returns a list of all items in dzn, in order, excepting any of length 4"),
      row("Dizin(1,6,2,1,6,3).distinct".c,"Removes redundant elements from the list. Uses the method == to decide. "),
      row("dzn.reverse".c,"Returns a list containing all elements of the dzn list in reverse order"),
      row("dzn.sortWith((str, t) => str.toLowerCase < t.toLowerCase)".c,"Returns a list containing all items of dzn in alphabetical order in lowercase.")
    ),
    "Some more useful list operations. First define a list of integers to use.".p,
    "val sayılar=Dizin(1,7,2,8,5,6,3,9,14,12,4,10)".c,
    table(
      row("sayılar.foldLeft(0)(_+_)".c,"Combines elements of list using a binary function starting from left, initial one with a 0 in this case."),
      row("sayılar.foldRight(0x20)(_|_)".c,"Combines elements of list using a binary function starting from Right, initial one with a hex 20 in this case.")
    )
  )
)

pages += Page(
  name = "UT",
  body = tPage("Kaplumbağacığın Kullanılışı",
    "The Turtle can be moved with a set of commands, many of which are listed below. Just try them out to see what they make the Turtle do. You can clear the Turtle Canvas at any time by right-clicking on it and then clicking Clear".p,
    "The following example defines a procedure (or command) that draws a triangle. This will be used in other examples further on, so try it first. Notice that multiple commands can be used on one line if they are separated by a semi-colon. Also 'repeat' is a useful command for carrying out the same set of commands a number of times.".p,
    """def üçgen() = yinele(3){ ileri(100); sağ(120) }
sil()
üçgen()
sol()
üçgen()
""".c,
    table(
      row("ileri(100)".c, "Moves the turtle ileri( a 100 steps."),
      row("geri(50)".c,"Moves the turtle back 50 steps."),
      row("konumuKur(100, 100)".c, "Sends the turtle to the point (x, y) without drawing a line. The turtle's heading is not changed."),
      row("noktayaGit(20, 30)".c, "Turns the turtle towards (x, y) and moves the turtle to that point."),
      row("dön(30)".c, "Turns the turtle through a specified angle. Angles are positive for counter-clockwise turns."),
      row("sağ()".c, "Turns the turtle 90 degrees right (clockwise)."),
      row("sağ(60)".c, "Turns the turtle 60 degrees right (clockwise)."),
      row("sol()".c, "Turns the turtle 90 degrees left (counter-clockwise)."),
      row("sol(30)".c, "Turns the turtle angle degrees left (counter-clockwise)."),
      row("noktayaDön(40, 60)".c, "Turns the turtle towards the point (x, y)."),
      row("açıyaDön(30)".c, "Sets the turtle's heading to angle (0 is towards the right side of the screen ('east'), 90 is up ('north'))."),
      row("doğrultu".c, "Queries the turtle's heading (0 is towards the right side of the screen ('east'), 90 is up ('north"),
      row("ev()".c, "Moves the turtle to its original location, and makes it point north."),
      row("konum".c, "Queries the turtle's position."),

      row("""kalemiKaldır()
ileri(100)
kalemiİndir()
ileri(100)""".c, "penDown makes the turtle draw lines as it moves while with penUp the Turtle moves without drawing a line."), 
      row("""kalemRenginiKur(mavi)
üçgen()""".c, "Specifies the color of the pen that the turtle draws with."),
      row("""sil()
boyamaRenginiKur(kırmızı)
üçgen()
""".c, "Specifies the fill color of the figures drawn by the turtle."),
      row("""sil()
kalemKalınlığınıKur(10)
üçgen()
kalemKalınlığınıKur(1)
  """.c, "Specifies the width of the pen that the turtle draws with."),
      row("ışınlarıAç()".c, "Shows crossbeams centered on the turtleto help with solving puzzles."),
      row("ışınlarıKapat()".c, "Hides the turtle crossbeams."),
      row("""sil()
görünmez()
ileri(100)
görünür()
dön(120)
ileri(100)""".c, "görünmez hides the turtle while görünür makes it görünür again."),
      row("""yazı("Merhaba Kardeş!")""".c, "Makes the turtle write the specified object as a string at its current location."),
      row("""sil()
ileri(-100)
canlandırmaHızınıKur(10)
dön(120)
ileri(100)""".c, "Sets the turtle's speed. The specified delay is the amount of time (in milliseconds) taken by the turtle to move through a distance of one hundred steps. The default is 1000."),
      row("canlandırmaHızı".c, "Queries the turtle's delay setting."),
      row("newTurtle(50, 50)".c, "Makes a new turtle located at the point (x, y)."),
      row("turtle0".c, "Gives you a handle to the default turtle."),
      row("sil()".c, "Clears the screen, and brings the turtle to the center of the window."),
      row("""sil()
üçgen()
zoom(0.5, 10, 10)""".c, "Zooms in by the given factor, and positions (cx, cy) at the center of the turtle canvas."),
      row("gridOn()".c, "Shows a grid on the canvas."),
      row("gridOff()".c, "Hides the grid."),
      row("axesOn()".c, "Shows the X and Y axes on the canvas."),
      row("axesOff()".c, "Hides the X and Y axes.")
    )
  )
)

pages += Page(
  name = "GAG",
  body = tPage("Çizim ve Oyun",
    "Peter Lewerin has contributed 'Staging' to Kojo. Staging gives you some neat graphics and the potential to make games. These Staging features originated in a Java project called Processing and were ported to Kojo by Peter. This capability is worth a whole kılavuzcuk in itself. However to give you a taste of what is possible and a starting point for experimentation, here are a couple examples.".p,
    "The section on Staging will be expanded in a later version of the kılavuzcuk. You can find a more complete list of Staging features and examples at:".p,
    "Commands or Methods".link("lewerin.se/peter/kojo/staging.html"),
    "Examples and Description".link("code.google.com/p/kojo/wiki/StagingModule"),
    "The package contains commands and functions that allow you to draw sophisticated shapes and images, and a frame loop that allows you to animate the graphics.".p,
    "In the first example you can see that a Staging environment is initialized, and the screen cleared. A ball is created, and then the ball bouncing movement is defined in the animation loop. Staging causes this loop to be executed every 20 to 32 milli-seconds giving a frame rate of around 30 to 50 frames per second depending on your computer performance. Using these principles you can create sophisticated animated graphics.".p,
    
    """import Staging._
import Staging.{circle, clear, animate} // explicitly import names that clash
clear()
gridOn()
val ball = circle(-200, -100, 5)

var y = 0 ; var x = 0 // ball position
var dy = 10; var dx = 3 // ball speed
// animation is around 30 - 50 frames per second depending on the computer system 
animate {
    ball.setPosition(x,y)
    // update ball position, detect end of bounce area
    dx =  if(x < 0 || x > 200) -dx else dx
    x += dx
    dy =  if(y < 0 || y > 100) -dy else dy
    y += dy  
}
""".c,
    "The next example is a simple game - a single player version of what must be one of the oldest games ever played on computers called 'Pong'. The idea is to hit the ball back with a paddle which you can move with the mouse. Each of your misses will be recorded. Have fun!".p,
    """import Staging._
import Staging.{circle, clear, animate, setFillColor, wipe, mouseX, mouseY} // explicitly import names that clash
clear()
var x = 0 ; var y = 0  // ball position
var dy = 10 ; var dx = 3 // ball speed
var padx = 0.0 ; var pady = 0.0 // paddle position
val padl = 80 // paddle length
var miss = 0
// Court
line(-200,-100,-200,100)
line(-200,-100,200,-100)
line(-200,100,200,100)
// the ball
setFillColor(blue)
val ball=circle(-200, -100, 5) 
// animation is about 30 frames per second or 32 milliseconds per frame
animate {
    wipe()
    padx=mouseX; pady=mouseY
    line(padx, pady, padx, pady+padl) // the paddle
    // detect a hit
    dx =if((dx>0)&&(padx-x<15)&&(x-padx<15)&&(y>pady)&&(y<pady+padl)) -dx else dx
    ball.setPosition(x,y)
    // update ball position and check for walls
    dx =  if(x+dx < -200) -dx else dx
    if(x+dx>200){x= -200;miss+=1}  // a miss
    x += dx
    dy =  if((y+dy < -100 )|| (y + dy > 100)) -dy else dy
    y += dy
    // Keep Score
    text(miss.toString + " missed",0,0)
}
""".c,
    "Now that you have the basics, try adding more balls, randomising their speed or changing the paddle size. Also see if you can fix the bug - sometimes the ball appears to pass through the paddle.".p,
    "Keyboard Input".h3,
    "It is very useful in games to use the keyboard to get player commands. Here is a simple example that allows you to draw using the left/right/up/down arrows to steer the Turtle.".p,

    "Run the example by clicking on the following. The example associates a set of actions to keyboard events. Don't forget to click on the Turtle canvas to give it focus first.".p,
    """sil(); görünür(); setAnimationDelay(100)
onKeyPress{ k  => k match {
        case Kc.VK_LEFT => setHeading(180)
        case Kc.VK_RIGHT => setHeading(0)
        case Kc.VK_UP => setHeading(90)
        case Kc.VK_DOWN => setHeading(270)
        case _ => // Any other character just move ileri(
    }
    ileri(20)            
}
activateCanvas()
""".c,
    "You can modify the actions and re-run to see what happens. Type Kc. to find out what other key events can be recognised.".p,

    "Clock".h3,
    "Here is a short example that illustrates how to use the staging graphics to display a clock.".p,
    "The Date library functions are used to find current time and date.".p,
    """import Staging._
import Staging.{circle, clear, animate, wipe, setPenColor} // explicitly import names that clash
clear
val Sc=100
val Pi2=2.0*math.Pi // 2*Pi radians in a circle
def clkFace={
  circle(0,0,Sc)
  for(i<-0 to 59){
    val ra=Pi2*i/60
    val x=Sc*sin(ra);val y=Sc*cos(ra)
    val tks=if(i%5==0) 0.9 else 0.95
    line(tks*x,tks*y,x,y)
    }
}
// the animate function runs around 30-50 times a second
animate{
  var d=new java.util.Date
  wipe
  setPenColor(red)
  clkFace
  setPenColor(blue)

  val s=Pi2*d.getSeconds/60
  line(0,0,0.9*Sc*sin(s),0.9*Sc*cos(s))

  val m=Pi2*d.getMinutes/60
  line(0,0,0.8*Sc*sin(m),0.8*Sc*cos(m))
  
  val h=Pi2*d.getHours/12+m/12
  line(0,0,0.6*Sc*sin(h),0.6*Sc*cos(h))
  
  text(d.toString, -Sc, -Sc-20)
}
""".c,

    "Conway'in Yaşam Oyunu".h3,
    """İngilizce adıyla "The Game of Life" o kadar meşhur ki, bilgisayarcılar arasında basitçe Life yani Yaşam adıyla tanınır! Aslında o bir hücresel otomaton yani basit hücrelerden oluşan ve onların yerel etkileşimleri sayesinde kendi kendine devinen en basit program türlerinden biri. İngiltere doğumlu Amerika'da Princeton üniversitesinde matematik araştırmaları yapan John Horton Conway tarafından 1970 yılında icat edilmiş. Belki de keşfedilmiş demek lazım. Kimbilir. Sen nedersin?""".p,
    "Wikipedia ansiklopedisinden bakabilirsin".link("tr.wikipedia.org/wiki/Conway%27in_Hayat_Oyunu"),
    "Ana fikir çok basit. Basit bir avuç kurala göre hücreler canlanır ya da can verir. Her hücrenin sekiz komşusu var. Doğru mu? Canlı olanlara arkadaş diyelim. Bakın dört kural var:".p,
    table(row("Canlı bir hücrenin ikiden az arkadaşı varsa canı çıkar. Canı sıkılmış sanki."),
      row("Eğer iki ya da üç arkadaşı varsa hayatta kalır."),
      row("Eğer başına üçten fazla arkadaş toplanırsa canı çıkar! Bilmem neden. Sanki çok kalabalık olmuş gibi."),
      row("Cansız bir hücrenin tam üç tane arkadaşı varsa kendisi de canlanır. Allah'ın hakkı üç denir ya!")
    ),
    "Bu yazılımcık 'foldLeft' adlı metodu kullanarak çok önemli bir kavram olan üst derece işlevlere örnek oluyor. Ne demek üst derece işlev? Başka işlevleri girdi olarak kabul eden onları kullanarak akıl almaz derecede becerikli olan komutlar. En başlarda gördüğümüz 'for' yapısı yerine 'foldLeft' kullanarak bütün dünyayı baştan çiziveriyoruz. 'foldLeft' soldan katla gibi bir anlama geliyor (bu arada bir de sağdan katlayan foldRight var. O da esaslı bir işlev). Dünyayı temsil eden kümenin hücrelerinin hepsini işleyiveriyor.".p,
    "Bu yaşam ya da hayat oyunu sıfır oyuncuyla oynanıyor! Çok sıkıcı mı dedin? Yok, çok ilginç aslında. Aslında sen çok önemlisin. Çünkü bu oyunun başlaması için en başta canlı hücrelere gerek var. Bunları sen belirleyebilirsin. Ama önce hazır bazı desenlerle başlamak daha kolay olur. 'başlangıç' adlı komudu bul. Onun ikinci girdisi 'desen'. Deseni seçmek için yapman gereken tek şey 'seç' adındaki değeri değiştirmek. Birinciyle başlıyoruz. Ama sıfırdan ona kadar hepsini deneyebilirsin. Sonra hatta kendin de yeni desenler ekleyebilirsin.".p,

    "Bu simulasyonun hızını 'oran' değerini değiştirerek ayarlayabilirsin.".p,
    """import Staging._ // Staging birimindeki komutları kullanmak için çağıralım.
// Farklı birimlerin bazı benzer komutları oluyor ve aynı adı kullanıyorlar.
// Onun için hangisini kullanmak istiyoruz açık açık belirtmemiz gerek:
import Staging.{ animate, circle, clear, setFillColor, wipe }

çıktıyıSil; clear(); gridOn(); axesOn(); setFillColor(mavi)

// bu yazılımcıkta hızıKur gibi kaplumba komutları bir işe yaramıyor,
// çünkü çizimleri yapan kaplumba değil Staging birimini

// bu oyunun dünyası yani tahtası büyük bir kare. Kenarı KU uzunluğunda olsun
// Nasıl satranç tahtası 8x8, bu tahta da 128x128 kare.
val KU = 128
// karenın kenarı kaplumbanın on adımına denk

// ilk önce, bütün kareler cansız olmalı
var dünya = (0 until KU * KU).foldLeft(Sayılar())((x, y) => x :+ 0)
satıryaz(s"Dünyamızda $KU'in karesi yani ${dünya.size} tane hane var.")
yaz(s"Ekranımız ${(canvasBounds.width / 10).toInt} kare eninde ")
satıryaz(s"ve ${(canvasBounds.height / 10).toInt} kare boyunda.")

val oran = 5 // canlandırmayı yavaşlatmak için bunu arttır.
// En hızlısı 1. 40'a eşitlersen saniyede bir nesil ilerliyor yaklaşık olarak.
// Nasıl mı? Canlandırma komutu (adı animate) bir saniyede 30 kere çalıştırılıyor. 

val gösterVeDur = yanlış // bunu doğru yaparsan deseni gösterip dururuz
val sonundaDur = doğru   // her desenin bir durağı var. Ondan sonra fazla bir şey değişmiyor.
// Ama, yine de çalışmaya devam etsin isterse, bunu yanlışa çevir.

// deseni seçelim:
val seç = 1
// block1 ve block2 bir kaç füze yolluyor ve sonra 1000. nesil civarı gibi duruyor.
val (desen, adı, durak) = seç match {
    case 0 => (üçlüler, "üçlüler", 20)
    case 1 => (kayGit, "kayGit", 500) /* makineli tüfek gibi */
    case 2 => (esaslı, "esaslı", 1111) /* Yaklaşık 1000 nesil canlı sonra peryodik */
    case 3 => (dokuzcanlı, "dokuzcanlı", 130) /* 131 nesil sonra can kalmıyor */
    case 4 => (blok1, "block1", 1200) //
    case 5 => (blok2, "block2", 1200) //
    case 6 => (küçücük, "tiny", 700) // küçücük
    case 7 => (ü2a, "ü2a", 60) // üçlülere ek
    case 8 => (ü2b, "ü2b", 60) // benzeri
    case 9 => (dörtlü, "dörtlü", 30) // üçlü üretiyor
    case _ => (tohum, "tohum", 2200) // ne muhteşem bir meşe palamudu!
}

dünya = başlangıç(dünya, desen)

yaz(s"$seç. desende ${desen.size} tane canlı kare var. Adı $adı.\nNesilleri sayalım: ")

var zaman = 0
val z0 = epochTime // şimdiki zamanı (geçmişte bir ana göre) anımsayalım
animate {
    val nesil = zaman / oran + 1
    if (zaman % oran == 0) {
        wipe() // sil
        çiz(dünya)
        dünya = (0 until KU * KU).foldLeft(Sayılar())((x, y) => x :+ yeniNesil(dünya, y))
        yaz(s"$nesil ")
        if (gösterVeDur) stopAnimation
    }
    zaman += 1
    if (sonundaDur && nesil == durak) {
        val z1 = epochTime - z0
        satıryaz(s"\n${round(z1, 2)} saniye geçti. Durduk.")
        stopAnimation()
    }
}

// deseni kuralım
def başlangıç(v: Sayılar, desen: Dizin[(Sayı, Sayı)]) = desen.foldLeft(v)((x, y) => x.updated((y._1 + KU / 2) * KU + y._2 + KU / 2, 1))

// yeni nesli bulalım
def yeniNesil(v: Sayılar, ix: Sayı) = {
    val kural = Vector(0, 0, 0, 1, 1, 0, 0, 0, 0, 0) // oyunun kuralları
    val x = ix / KU; val y = ix % KU
    val t = (0 until 3).foldLeft(0)((st, i) => {
        st + (0 until 3).foldLeft(0)((s, j) => {
            val xt = x + i - 1; val yt = y + j - 1
            s + (if ((xt < 0) || (xt >= KU) || (yt < 0) || (yt >= KU)) 0 else v(xt * KU + yt))
        })
    })
    if (v(ix) == 1) kural(t) else { if (t == 3) 1 else 0 }
}
// canlı kareleri çizelim. Can mavi çember içi kırmızı daire. Yarıçapı 5
val yarıçap = 5
def çiz(v: Sayılar) = for (i <- 0 until KU * KU)
    if (v(i) == 1) circle(
        (i / KU) * 2 * yarıçap - KU * yarıçap,
        (i % KU) * 2 * yarıçap - KU * yarıçap, yarıçap)

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
    
    "Tangle".h3,
    "Here is a game that illustrates how you can use the Scala collections and mouse drag-and-drop to create a fun game to play.".p,
    "The game was based on a game called Planarity. The idea is to use the mouse to re-arrange the circles so that none of the joining lines cross one and other. Press the left mouse button on a circle to drag it. A new game is started by clicking on the red square.".p,
    "You can increase the difficulty of the game by changing the value ES in the program. Larger values make it more difficult.".p,
    "The inspiration for Tangle is Planarity".link("en.wikipedia.org/wiki/Planarity"),
    
    """import Staging._
import Staging.{circle, clear, animate} // explicitly import names that clash
import math.pow,math.random
// Tangle based on Planarity
clear()
// ES sets difficulty level
val ES=4;val AS=ES*ES
val Ra=10
// Edge is a line between two nodes
case class EdgeP(n1:NodeP,n2:NodeP){
var e=line(n1.x,n1.y,n2.x,n2.y)
}
// edges is all the edges, initially empty
var edges=Vector[EdgeP]()
// Node is a circle which is dragable. Redraws edges when dragged
case class NodeP(var x:Kesir,var y:Kesir){
  val n=circle(x,y,Ra)
  n.setFillColor(blue)
  def goTo(gx:Kesir,gy:Kesir){
   x=gx ; y=gy
   n.setPosition(gx,gy)   
  }
  n.onMouseDrag{(mx, my) => {n.setPosition(mx, my);x=mx;y=my;drawEdges(edges)}}
}
// Create and link all nodes topologically in a square 
val p=(0 until AS).foldLeft(Vector[NodeP]())((v,i)=>{v :+ NodeP(0,0)})

// Create all edges, link to adjacent nodes   
edges=(0 until AS).foldLeft(Vector[EdgeP]())(
    (ev,i)=>{
        val x=i/ES; val y=i%ES 
        val te=if(y<ES-1) {ev :+ EdgeP(p(i),p(i+1))} else ev
        if(x<ES-1) {te :+ EdgeP(p(i),p(i+ES))} else te
    })
// draw all edges
putRand(p)
// Button for new game
val b=square(-ES*35,-ES*35, 20)
b.setFillColor(red)
b.onMouseClick { (x, y) =>putRand(p)}
// randomise node positions
def putRand(p:Vector[NodeP]){
   p.foreach(tn=>tn.goTo(ES*Ra*6*(random - 0.5),ES*Ra*6*(random - 0.5)))    
   drawEdges(edges) 
   }    
//draw edges between nodes and start line from circumference of circle
def drawEdges(ev:Vector[EdgeP]){ 
   ev.foreach(te=>{
     val x1=te.n1.x ; val y1=te.n1.y
     val x2=te.n2.x ; val y2=te.n2.y
     val len=sqrt(pow(x2-x1,2) + pow(y2-y1,2))
     val xr=Ra/len*(x2-x1) ; val yr=Ra/len*(y2-y1) 
     te.e.erase;
     te.e=line(x1+xr,y1+yr,x2-xr,y2-yr)
     }) 
   }
""".c
  )
)

pages += Page(
  name = "LM",
  body = tPage("Daha Çok Öğrenelim",

    "Next Steps".h2,
    "This kılavuzcuk has covered part of what is a very deep language. By now, you are familiar with the essential Scala language features. Already you can write quite sophisticated programs in Scala and have fun with the graphical environment offered by Kojo.".p,
    "If you are already a Java programmer you can no doubt already see how you can use Scala with all the libraries from your existing Java environment too. Scala and Java integrate seamlessly.".p,

    "To learn more a good book is invaluable. Programming in Scala by Martin Odersky, Lex Spoon and Bill Venners is one excellent place to continue.".p,
    "It can be found by clicking here.".link("www.artima.com/shop/programming_in_scala"),
    "The Scala community site has a lot of good material and references to other learning materials.".p,
    "Click here to go to Scala-lang.org.".link("www.scala-lang.org"),
    "For the professional programmer, you can also download Scala, Akka, and the Eclipse IDE.".p,
    "They can be found here.".link("typesafe.com"),
    "The Netbeans IDE also has good Scala support (and Kojo itself has been written, in Scala, using Netbeans).".p,
    "More information here".link("wiki.netbeans.org/Scala"),
    "A small subset of the Scala library functions have been used in this kılavuzcuk".p,
    "Click here to see a complete list of all the Scala library available.".link("www.scala-lang.org/api/current/index.html"),
    "We wish you lots of fun using Scala!".p
      
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
