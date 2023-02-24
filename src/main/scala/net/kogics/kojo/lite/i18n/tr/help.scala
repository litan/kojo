/*
 * Copyright (C) 2021 June
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
 *   Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.lite.i18n.tr

object help {
  val templates = Map(
    "englishTurtle" -> "englishTurtle",
    "yeniKaplumbağa" -> "yeniKaplumbağa(${x},${y},${kılık})",
    "a_kalıp" -> "a_kalıp()",
    "ileri" -> "ileri(${adım})",
    "geri" -> "geri(${adım})",
    "sağ" -> "sağ(${açı},${yarıçap})",
    "sol" -> "sol(${açı})",
    "sol" -> "sol(${açı},${yarıçap})",
    "atla" -> "atla(${x},${y})",
    "ilerle" -> "ilerle(${x},${y})",
    "zıpla" -> "zıpla(${adım})",
    "ev" -> "ev()",
    "noktayaDön" -> "noktayaDön(${x},${y})",
    "açıyaDön" -> "açıyaDön(${açı})",
    "doğrultu" -> "doğrultu()",
    "doğu" -> "doğu()",
    "batı" -> "batı()",
    "kuzey" -> "kuzey()",
    "güney" -> "güney()",
    "canlandırmaHızınıKur" -> "canlandırmaHızınıKur(${milisaniye})",
    "yazı" -> "yazı(${yazı})",
    "yazıBoyunuKur" -> "yazıBoyunuKur(${boyutKur})",
    "yay" -> "yay(${yarıçap},${açı})",
    "daire" -> "daire(${yarıçap})",
    "görünür" -> "görünür()",
    "görünmez" -> "görünmez()",
    "kalemiİndir" -> "kalemiİndir()",
    "kalemiKaldır" -> "kalemiKaldır()",
    "kalemİnikMi" -> "kalemİnikMi",
    "kalemRenginiKur" -> "kalemRenginiKur(${renk})",
    "boyamaRenginiKur" -> "boyamaRenginiKur(${renk})",
    "kalemKalınlığınıKur" -> "kalemKalınlığınıKur(${en})",
    "biçimleriBelleğeYaz" -> "biçimleriBelleğeYaz()",
    "biçimleriGeriYükle" -> "biçimleriGeriYükle()",
    "konumVeYönüBelleğeYaz" -> "konumVeYönüBelleğeYaz()",
    "konumVeYönüGeriYükle" -> "konumVeYönüGeriYükle()",
    "ışınlarıAç" -> "ışınlarıAç()",
    "ışınlarıKapat" -> "ışınlarıKapat()",
    "sil" -> "sil()",
    "çıktıyıSil" -> "çıktıyıSil()",
    "silVeSakla" -> "silVeSakla()",
    "çizimiSil" -> "çizimiSil()",
    "artalanıKur" -> "artalanıKur(${renk})",
    "artalanıKurDik" -> "artalanıKurDik(${renk1},${renk2})",
    "artalanıKurYatay" -> "artalanıKurYatay(${renk1},${renk2})",
    "konum" -> "konum",
    "yinele" -> "yinele(${sayı}) {\n    ${cursor}\n}",
    "yineleDizinli" -> "yineleDizinli(${sayı}) { i =>\n    ${cursor}\n}",
    "yineleDoğruysa" -> "yineleDoğruysa(${koşul}) {\n    ${cursor}\n}",
    "yineleOlanaKadar" -> "yineleOlanaKadar(${koşul}) {\n    ${cursor}\n}",
    "yineleKere" -> "yineleKere(${dizi}) { ${e} =>\n    ${cursor}\n}",
    "yineleİçin" -> "yineleİçin(${dizi}) { ${e} =>\n    ${cursor}\n}",
    "yineleİlktenSona" -> "yineleİlktenSona(${ilk},${son}) { s => \n    ${cursor}\n}",
    "satıryaz" -> "satıryaz(${yazı})",
    "satıroku" -> "satıroku(${istem})",
    "yuvarla" -> "yuvarla(${sayı},${basamaklar})",
    "rastgele" -> "rastgele(${üstSınır})",
    "rastgeleKesir" -> "rastgeleKesir(${üstSınır})",
    "giysiKur" -> "giysiKur(${dostaAdı})",
    "giysileriKur" -> "giysileriKur(${dostaAdı1},${dostaAdı2})",
    "birsonrakiGiysi" -> "birsonrakiGiysi()",
    "buAn" -> "buAn()",
    "buSaniye" -> "buSaniye()",
    "hızıKur" -> "hızıKur(${hız})",
    "def2" -> "def2()",
    "eksenler" -> "eksenler",
  )

  val content = Map(
    "englishTurtle" ->
      <div>
        <strong>englishTurtle</strong><br/><br/>
        Bu komut sadece İngilizce komutları bilen bir kaplumbağacık verir. Bu kaplumbağanın türü
        <tt>Turtle</tt>'dır. Türkçe komutları anlamaz. Türkçe bilen kaplumbağanın türü ise <tt>Kaplumbağa</tt>'dır.
        <br/>Bu 'Turtle', ki kaplumbağa demek, İngilizce komutları belirlemek ve kullanmak istenirse faydalı olabilir.
        <br/><em>Örnek:</em> <br/>
        <pre>
val ingilizceAnlayanKaplumbağa = englishTurtle
repeat(4){{
  ingilizceAnlayanKaplumbağa.forward(100)
  ingilizceAnlayanKaplumbağa.right()
}}
      </pre>
      Bu örnek İngilizce komutlarla bir kare çizer.
      <br/><br/>
      yeniKaplumbağa komutuna da bir bakıver.
      </div>.toString,
    "yeniKaplumbağa" ->
      <div>
      <strong>yeniKaplumbağa</strong>(x, y, kılık) - Bu komut x,y noktasında yeni bir kaplumbağa oluşturur ve verilmişse ona bır kılık giydirir. Kılık verilmemişse kaplumbağa olarak çizer. Onun için de adı yeniKaplumbağa!
      <pre>
val araba = yeniKaplumbağa(100, 200, Costume.car)
araba.canlandırmaHızınıKur(100)
araba.kalemRenginiKur(mavi)
araba.ilerle(0,0)
araba.kuzey()
      </pre>Bu örnek bir araba oluşturur, (100, 200) noktasından (0, 0) noktasına hızlıca giderek mavi bir doğru parçası, yani bir çizgi çizer.
      </div>.toString,
    "a_kalıp" -> <div>
      <strong>komut</strong>(g1, g2) - Açıklama ...
      <br/>
      Daha çok açıklama ...
      <br/><em>Örnek:</em> <br/>
      <pre>
val x = komut
x.metod
      </pre> Bu örnek ... açıklama ...
    </div>.toString,
    "ileri" -> <div><strong>ileri</strong>(adımSayısı) - Bu komut kaplumbağaya verilen sayı kadar adım atırarak baktığı doğrultuda ilerletir. Adım sayısı verilmemişse 25 adım atar.</div>.toString,
    "geri" -> "geri(${adımSayısı}) - ileri komutunun tersi",
    "sol" -> <div>
      <strong>sol</strong>() - Bu komut kaplumbağayı olduğu yerde sola doğru (saat yönünün tersine doğru) 90 derece döndürür. <br/>
      <strong>sol</strong>(derece) - Bu komut kaplumbağayı olduğu yerde sola doğru (saat yönünün tersine) verilen derece kadar döndürür. <br/>
      <strong>sol</strong>(derece, yarıçap) - Bu komut kaplumbağayı verilen yarıçaplı bir yay üzerinde sola doğru (saat yönünün tersine doğru) verilen derece kadar döndürerek ilerletir. <br/> </div>.toString,
    "sağ" -> <div> <strong>sağ</strong>() - Bu komut kaplumbağayı sağa doğru (saat yönününde) 90 derece döndürür. <br/> <strong>sağ</strong>(derece) - Bu komut kaplumbağayı sağa doğru (saat yönünde) verilen derece kadar döndürür. <br/> <strong>sağ</strong>(derece, yarıçap) - Bu komut kaplumbağayı verilen yarıçaplı bir yay üzerinde sağa doğru (saat yönünde) verilen derece kadar döndürerek ilerletir. <br/> </div>.toString,
    "atla" -> <div> <strong>atla</strong>(x, y) - Bu komut kaplumbağayı çizgi çizmeden (x, y) noktasına götürür. Kaplumbağanın yönü değişmez. <br/> </div>.toString,
    "ilerle" -> <div><strong>ilerle</strong>(x, y) - Bu komut kaplumbağanın yönünü (x, y) noktasına çevirir ve o noktaya kadar götürür. </div>.toString,
    "zıpla" -> <div> <strong>zıpla</strong>(adımSayısı) - Bu komut <em>kalemi kaldırıp</em> kaplumbağayı verilen adım kadar ilerletir, böylece çizgi çizilmemiş olur. Sonra da kalemi indirir ki arkadan gelen komutlar çizmeye devam etsin. <br/> </div>.toString,
    "ev" -> <div><strong>ev</strong>() - Bu komut kaplumbağayı başlangıç noktasına götürür ve yönünü kuzeye çevirir. </div>.toString,
    "noktayaDön" -> <div><strong>noktayaDön</strong>(x, y) - Bu komut kaplumbağayı (x, y) noktasına çevirir. </div>.toString,
    "açıyaDön" -> <div><strong>açıyaDön</strong>(angle) - Bu komut kaplumbağayı verilen açıya çevirir. (0 derece ekranın sağına bakar (<em>doğu</em>), 90 yukarı (<em>kuzey</em>)).</div>.toString,
    "doğrultu" -> <div><strong>doğrultu</strong> - Bu komut kaplumbağanın yönünü bildirir. (0 derece ekranın sağına bakar (<em>doğu</em>), 90 yukarı (<em>kuzey</em>)).</div>.toString,
    "doğu" -> <div><strong>doğu</strong>() - Bu komut kaplumbağayı doğuya çevirir. </div>.toString,
    "batı" -> <div><strong>batı</strong>() - Bu komut kaplumbağayı batıya çevirir. </div>.toString,
    "kuzey" -> <div><strong>kuzey</strong>() - Bu komut kaplumbağayı kuzeye çevirir. </div>.toString,
    "güney" -> <div><strong>güney</strong>() - Bu komut kaplumbağayı güneye çevirir. </div>.toString,
    "canlandırmaHızınıKur" -> <div> <strong>canlandırmaHızınıKur</strong>(süre) - Bu komut kaplumbağanın hızını belirler. Verilen süre milisaniye olarak kaplumbağanın yüz adım atması için gereken süreyi belirler.<br/> Başlangıç değeri 1000 milisaniye yani 1 saniyedir.<br/> </div>.toString,
    "yazı" -> <div><strong>yazı</strong>(nesne) - Bu komut kaplumbağanın durduğu yere verilen nesnenin yazı olarak karşılığını yazar. <br/>
      <tt>tuvaleYaz</tt> komutuyla eştir.</div>.toString,
    "yazıBoyunuKur" -> <div><strong>yazıBoyunuKur</strong>(sayı) - Bu komut kaplumbağanın yazı tipinin boyunu belirler. </div>.toString,
    "yay" -> <div> <strong>yay</strong>(yarıçap, açı) - Bu komut kaplumbağaya verilen yarıçaplı dairenin verilen açı büyüklüğündeki yayını çizdirir. <br/> Artı açılar sola doğru (saat yönünün tersine), eksi açılar da sağa doğru (saat yönünde) çizilir. <br/> </div>.toString,
    "daire" -> <div> <strong>daire</strong>(yarıçap) - Bu komut kaplumbağaya yarıçapı verilen daireyi çizdirir. <br/> <tt>daire(50)</tt> komutu <tt>yay(50, 360)</tt> komutuyla aynı işleve sahiptir (yani aynı işi yapar!).<br/> </div>.toString,
    "görünür" -> <div><strong>görünür</strong>() - Bu komut <tt>görünmez()</tt> komutuyla saklanan kaplumbağayı tekrar ortaya çıkarır. </div>.toString,
    "görünmez" -> <div><strong>görünmez</strong>() - Bu komut kaplumbağayı görünmez kılar. Kaplumbağamızı <tt>görünür()</tt> komutuyla tekrar ortaya çıkarabilirsiniz.</div>.toString,
    "kalemiİndir" -> <div> <strong>kalemiİndir</strong>() - Bu komut kaplumbağanın kalemini indirerek sonraki komutlarla ilerlediğinde çizgi çizmesini sağlar.<br/> Başlangıçta kalem inik durumdadır. br/> </div>.toString,
    "kalemiKaldır" -> <div><strong>kalemiKaldır</strong>() - Bu komut kaplumbağanın kalemini kaldır. Kaplumbağa bundan sonra ilerlerken çizgi çizmez. <br/></div>.toString,
    "kalemİnikMi" -> <div><strong>kalemİnikMi</strong> - Bu komut kalemin inik olup olmadığını bildirir. </div>.toString,
    "kalemRenginiKur" -> <div><strong>kalemRenginiKur</strong>(renk) - Bu komut kaplumbağanın çizim yapmakta kullandığı kalemin rengini belirler. <br/></div>.toString,
    "boyamaRenginiKur" -> <div><strong>boyamaRenginiKur</strong>(renk) - Bu komut kaplumbağanın çizdiği şekillerin içini boyamak için kullandığı kalemin rengini belirler. <br/></div>.toString,
    "kalemKalınlığınıKur" -> <div><strong>kalemKalınlığınıKur</strong>(thickness) - Bu komut kaplumbağanın çizim yapmakta kullandığı kalemin kalınlığını belirler.<br/></div>.toString,
    "biçimleriBelleğeYaz" -> <div> <strong>biçimleriBelleğeYaz</strong>() - Bu komut kaplumbağanın o anda kullandığı biçimleri belleğe yazarak daha sonra <tt>biçimleriGeriYükle()</tt> komutuyla kolaylıkla eski duruma dönülmesine yarar. Kaplumbağamızın biçimlerini kısa bir süre için değiştirip sonra eski hale kolayca dönmek için bu komutu kullanırız. Bu yolla iki farklı çizim biçimi arasında gidip gelmek kolaylaşır. <br/> <p> Kaplumbağanın belleğe yazılan biçimleri şunlardır: <ul> <li>Kalem Rengi</li> <li>Kalem Kalınlığı</li> <li>Boyama Rengi</li> <li>Kalem Yazısı</li> <li>Kalem İnik mi Kalkık mı</li> </ul> </p> </div>.toString,
    "biçimleriGeriYükle" -> <div> <strong>biçimleriGeriYükle</strong>() - Bu komut daha önce kullanılan <tt>biçimleriBelleğeYaz()</tt> komutuyla kaydedilen kaplumbağa biçimlerini geri yükler. <br/> <p> Kaplumbağanın bellekte yazılı olan biçimleri şunlardır: <ul> <li>Kalem Rengi</li> <li>Kalem Kalınlığı</li> <li>Boyama Rengi</li> <li>Kalem Yazısı</li> <li>Kalem İnik mi Kalkık mı</li> </ul> </p> </div>.toString,
    "konumVeYönüBelleğeYaz" -> <div> <strong>konumVeYönüBelleğeYaz</strong>() - Bu komut kaplumbağanın o anki konum ve yönünü belleğe kaydeder ki yerini ve yönünü değiştiren komutlarla gittiği yeni konumdan ve yönden <tt>konumVeYönüGeriYükle()</tt> komutuyla kolaylıkla geri dönebilelim. <br/> </div>.toString,
    "konumVeYönüGeriYükle" -> <div> <strong>konumVeYönüGeriYükle</strong>() - Bu komut kaplumbağayı daha önce kullanılan <tt>konumVeYönüBelleğeYaz()</tt> komutuyla kaydedilen konum ve doğrultuya geri götürür. <br/> </div>.toString,
    "ışınlarıAç" -> <div><strong>ışınlarıAç</strong>() - Bu komut kaplumbağanın önünü, arkasını, sağını ve solunu bir artı çizerek daha kolay seçmemizi sağlar.</div>.toString,
    "ışınlarıKapat" -> <div><strong>ışınlarıKapat</strong>() - Bu komut <tt>ışınlarıAç()</tt> komutuyla kaplumbağanın üstüne çizilen artıyı siler.</div>.toString,
    "sil" -> <div><strong>sil</strong>() - Bu komut kaplumbağanın tuvalini temizler, kaplumbağayı başlangıç konumuna geri getirir ve kuzey doğrultusuna çevirir.</div>.toString,
    "çıktıyıSil" -> <div><strong>çıktıyıSil</strong>() - Bu komut çıktı penceresindeki bütün çıktıları silerek temizler. </div>.toString,
    "silVeSakla" -> <div><strong>silVeSakla</strong>() - Bu komut tuvaldeki çizimleri siler ve kaplumbağayı görünmez kılar. </div>.toString,
    "çizimiSil" -> <div><strong>çizimiSil</strong>() - Bu komut tuvaldeki çizimleri siler. </div>.toString,
    "artalanıKur" -> <div><strong>artalanıKur</strong>(renk) - Bu komutla tuval verilen renge boyanır. Kojonun tanıdığı sarı, mavi ve siyah gibi renkleri kullanabilirsiniz ya da <tt>Renk</tt>, <tt>ColorHSB</tt> ve <tt>ColorG</tt> komutlarını kullanarak kendi renklerinizi yaratabilirsiniz. </div>.toString,
    "artalanıKurDik" -> <div><strong>artalanıKurDik</strong>(renk1, renk2) - Bu komutla tuval aşağıdan yukarı doğru birinci renkten ikinci renge derece derece değişerek boyanır. </div>.toString,
    "artalanıKurYatay" -> <div><strong>artalanıKurYatay</strong>(renk1, renk2) - Bu komutla tuval soldan sağa doğru birinci renkten ikinci renge derece derece değişerek boyanır. </div>.toString,
    "konum" -> <div><strong>konum</strong> - Bu komut kaplumbağacığın bulunduğu konumu nokta (Point) olarak bildirir. <tt>konum.x</tt> ve <tt>konum.y</tt> ile de x ve y koordinatları okunabilir. </div>.toString,
    "yinele" -> <div><strong>yinele</strong>(sayı){{ }} - Bu komut küme içine alınan komutları verilen sayı kadar tekrar tekrar çağırır. <br/></div>.toString,
    "yineleDizinli" -> <div><strong>yineleDizinli</strong>(sayı) {{i => }} - Bu komut, küme içine alılan komutları verilen sayı kadar tekrar tekrar çağırır. Kaçıncı yineleme olduğunu <tt>i</tt> değişkenini küme içinde kullanarak görebiliriz. </div>.toString,
    "yineleDoğruysa" -> <div><strong>yineleDoğruysa</strong>(koşul) {{ }} - Bu komut küme içine alılan komutları verilen koşul doğru oldukça tekrar çağırır. <br/></div>.toString,
    "yineleOlanaKadar" -> <div><strong>yineleOlanaKadar</strong>(koşul) {{ }} - Bu komut küme içine alılan komutları verilen koşul sağlanana kadar tekrar çağırır. <br/></div>.toString,
    "yineleKere" -> <div><strong>yineleKere</strong>(dizi){{ }} - Bu komut küme içine alılan komutları verilen dizideki her eleman için birer kere çağırır. <br/></div>.toString,
    "yineleİçin" -> <div><strong>yineleİçin</strong>(dizi){{ }} - Bu komut küme içine alılan komutları verilen dizideki her eleman için birer kere çağırır. yineleKere ile aynı işlevi görür.<br/></div>.toString,
    "yineleİlktenSona" -> <div><strong>yineleİlktenSona</strong>(ilk, son){{ }} - Bu komut küme içine alınan komutları ilk sayıdan son sayıya kadar tekrar çağırır.</div>.toString,
    "satıryaz" -> <div><strong>satıryaz</strong>(obj) - Bu komut verilen nesneyi harf dizisi olarak çıktı penceresine yazar ve yeni satıra geçer. </div>.toString,
    "satıroku" -> <div><strong>satıroku</strong>(istemDizisi) - Bu komut verilen istem dizisini çıktı penceresine yazar ve arkasından sizin yazdığınız bir satırı okur. </div>.toString,
    "yuvarla" -> <div><strong>yuvarla</strong>(sayı, basamak) - Bu komut verilen sayıyı noktadan sonra verilen basamak sayısına kadar yuvarlar. </div>.toString,
    "rastgele" -> <div><strong>rastgele</strong>(üstsınır) - Bu komut verilen üst sınırdan küçük rastgele bir doğal sayı verir. Sıfırdan küçük sayılar vermez. </div>.toString,
    "rastgeleKesir" -> <div><strong>rastgeleÇift</strong>() - Bu komut verilen üst sınırdan küçük rastgele bir kesirli sayı (çift çözünürlüklü) verir. Sıfırdan küçük sayılar vermez. </div>.toString,
    "giysiKur" -> <div><strong>giysiKur</strong>(giysiDosyası) - Kaplumbağanın görünüşünü verilen dosyadaki resimle değiştirir. </div>.toString,
    "giysileriKur" -> <div><strong>giysilerKur</strong>(giysiDosyası1, giysiDosyası2, ...) - Kaplumbağa için bir dizi giysi belirler ve giysiDosyası1 resmini giydirir. <tt>birSonrakiGiysi()</tt> komutuyla dizideki bir sonraki giysiyi giydirebiliriz. </div>.toString,
    "birsonrakiGiysi" -> <div><strong>birSonrakiGiysi</strong>() - Kaplumbağaya <tt>giysilerKur()</tt> komutuyla girilen giysi dizisindeki bir sonraki resmi giydirir. </div>.toString,
    "buAn" -> <div><strong>buAn</strong>() - Bu komut evrensel zamana (UTC) göre 1 Ocak 1970 tam geceyarısından bu ana kadar geçen zamanı kesirsiz milisaniye olarak verir.</div>.toString,
    "buSaniye" -> <div><strong>buSaniye</strong>() - Bu komut evrensel zamana (UTC) göre 1 Ocak 1970 tam geceyarısından bu ana kadar geçen zamanı kesirli saniye olarak verir.</div>.toString,
    "hızıKur" -> <div><strong>hızıKur</strong>(hız) - Kaplumbağacığın hızını belirler. yavaş, orta, hızlı ve çokHızlı değerlerinden birini dene.</div>.toString,
    "def_türkçe" -> <div><strong>def</strong> - Kıvrık parantez içindeki bir dizi komuta ya da deyişe bir ad takar. Bu yöntemle yeni bir işlev ya da komut tanımlamış olursun. <br/>
      <br/>
      <em>Örneğin:</em> <br/>
      <br/>
      <pre>
      // Kare adında yeni bir komut
      // Tek bir girdisi var
      def kare(kenar: Sayı) {{
          yinele(4) {{
              ileri(kenar)
              sağ()
          }}
      }}
      sil()
      // iki kere çagıralım yeni komutumuzu:
      kare(100)
      kare(200)

      // Topla adında yeni bir işlev tanımlayalım
      // İki girdisi, bir çıktısı var
      def topla(a: Sayı, b: Sayı) =
          a + b
      çıktıyıSil()
      // işlevi çağırıp çıktısını yazdıralım
      satıryaz(topla(3, 5))
      // bir toplama daha yapalım
      val toplam = topla(20, 7)
      satıryaz(toplam)
      </pre>
            </div>.toString,
    "eksenler" ->
      <div>
      <strong>eksenler -> resim</strong> - Verilen resmin yerel eksenlerini çizerek yeni bir resim oluşturur.
      <br/>
      Bu yöntem dönüştürücü bir yöntemdir ve resim oluşturmakta size yardımcı olur.
      <br/><em>Örnek:</em> <br/>
      <pre>
      def resim = Resim {{
        yinele(2) {{
          ileri(50); sağ()
          ileri(100); sağ()
        }}
      }}
      silVeSakla
      çiz(
        götür(-100, -50) * döndür(45) *
          boyaRengi(mavi) * eksenler -> resim
      )
      eksenleriGöster()
      </pre>
      Bu örnekte bir dikdörtgen çiziyor, onu (-100, -50) noktasına taşıyor, 45 derece döndürüyor ve içini maviye boyuyoruz. Bir de bu çizimin kendi eksenlerini yani yerel eksenlerini çiziyoruz. En sondaki eksenleriGöster komutuyla da genel ya da mutlak eksenleri gösteriyoruz.
    </div>.toString,
    "eksenleriGöster" ->
      <div>
      <strong>eksenleriGöster</strong> - Çizim tuvalinde yatay (X) ve dikey (Y) eksenlerini çizer.
      </div>.toString,
    "eksenleriGizle" ->
      <div>
      <strong>eksenleriGizle</strong> - Çizim tuvalindeki yatay (X) ve dikey (Y) eksenlerini siler.
      </div>.toString,
  )
}
