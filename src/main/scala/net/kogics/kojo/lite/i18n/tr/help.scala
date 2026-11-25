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
  // ../../../xscala/CodeTemplates.scala
  val templates = Map(
    "rastgeleİkil" -> "rastgeleİkil",
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
    "silipSakla" -> "silipSakla()",
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
    "eksenler" -> "eksenler",
    "götür" -> "götür(${x}, ${y})",
    "döndür" -> "döndür(${açı})",
    "döndürMerkezli" -> "döndürMerkezli(${açı}, ${x}, ${y})",
    "büyüt" -> "büyüt(${oran})",
    "büyütXY" -> "büyütXY(${xOranı}, ${yOranı})",
    "boyaRengi" -> "döndür(${boya})",
    "Nokta" -> "Nokta(${x}, ${y})",
    "Aralık" -> "Aralık(${ilki}, ${sonuncu}, ${adım})",
    "yazıTamamlamaSeçenekleriniYazdırmayıAçKapa" -> "yazıTamamlamaSeçenekleriniYazdırmayıAçKapa()",
    "RenkKYM" -> "RenkKYM(${kırmızı}, ${yeşil}, ${mavi}, ${saydamlıkDerecesi})",
    "RenkADA" -> "RenkADA(${arıRenk}, ${doygunluk}, ${aydınlıkAçıklık})", 
    "RenkDD" -> "RenkDD(${x1}, ${y1}, ${renk1}, ${x2}, ${y2}, ${renk2}, ${dönüşlü})", 
    "Yazıyüzü" -> "Yazıyüzü(${adı}, ${boyu})",

    // todo more
  )

  // NOTE: We can't use less than operator! < is meaningful to the xml/html stuff! Instead, use &lt;
  // https://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references
  private val elleYazılanİçerik = Map(
    "a_kalıp" -> <div>
      <strong>komut</strong>(g1, g2) - Açıklama ... <br/>
      Daha çok açıklama ... <br/>
      <br/><em>Örnek:</em> <br/>
      <pre>

      dez x = komut
      x.yöntem

      </pre> Bu örnekten sonra açıklama ...
      </div>.toString,

    "Matematik" -> <div> Matematiksel işlevlerin hepsini bunun altına koyduk. 
    <br/><br/> Örnek: <pre>

dez n1 = Nokta(0, 0)
dez n2 = Nokta(10, 10)
Matematik.açı(n1, n2)
 // ön eksiz de kullanabilirsin
açı(n2, n1)

    </pre> Diğer işlevler şunlar: <br/><br/>dereceye<br/>değişim<br/>doğalLogu<br/>eSayısı<br/>enUfakOrtakKat<br/>enUfağı<br/>enİriOrtakPayda<br/>enİrisi<br/>eüssü<br/>gücü<br/>işareti<br/>karekökü<br/>karesi<br/>kosinüs<br/>kosinüsünAçısı<br/>kuvveti<br/>log2tabanlı<br/>logTabanlı<br/>logaritması<br/>mutlakDeğer<br/>onlukTabandaLogu<br/>ortalama<br/>piSayısı<br/>radyana<br/>rasgele<br/>sayıya<br/>sinüs<br/>sinüsünAçısı<br/>taban<br/>tanjant<br/>tanjantınAçısı<br/>tavan<br/>uzaklık<br/>uzaklık<br/>yakın<br/>yakın<br/>yakını<br/>yuvarla<br/>
      </div>.toString,

    "yazıTamamlamaSeçenekleriniYazdırmayıAçKapa" -> <div>
      Yazı tamamlama seçeneklerini çıktı penceresine yazdırmak için bu komudu kullanabilirsin. Yazdırma açıksa, tekrar çağırırarak yazdırmayı kapatabilirsin. <br/>
      <br/><em>Örnek:</em> <br/>
      <pre>

      dez piSayısınınBaşı = Dizi(3, 1, 4, 1, 5)
      yazıTamamlamaSeçenekleriniYazdırmayıAçKapa()
      // şimdi Ctrl-return'le kodu çalıştır
      // Sonra, aşağıya piS yaz ve Ctrl-boşluk'la yazı tamamlama özelliğini kullan

      </pre> Tekrar tekrar Ctrl-return ile bu komudu çalıştırarak yazı tamamlamalarının çıktı penceresine yazdırılmalarını kapatıp açabilirsin.
      </div>.toString,

    "dez" -> <div>
      <strong>dez</strong> - Değişmez bir değere ad takar. Bu sayede yazılım daha anlaşılır olur. Ayrıca sonradan değiştirmek kolaylaşır. <br/> <br/>
      <em>Örnek:</em> <br/> <br/> <pre>
      sil()
      dez adım = 20 // Bunu 10 ya da 30 yapıp tekrar çalıştırabilirsin
      daire(adım)
      yinele(4) {{
        ileri(adım)
        sol()
        ileri(adım)
      }}
      </pre>
      </div>.toString,

    "den" -> <div>
      <strong>den</strong> - Bir değişken tanımlar ve ona bir değer takar. Değişkenin değerini daha sonra değiştirebilirsin. <br/> <br/>
      <em>Örnek:</em> <br/> <br/> <pre>
      çıktıyıSil
      den x = 3
      x = x + 4
      satıryaz(x)
      x = x * 6
      satıryaz(x)
      </pre>
      </div>.toString,

    "tanım" -> <div>
      <strong>tanım</strong> - Yeni bir komut ya da yeni bir işlev tanımlar. <br/> <br/>
      <em>Örnek 1:</em> <br/> <br/> <pre>
      // Yeni bir komut yazalım. Adı kare olsun.
      // Bir tane de girdisi olsun. Girdinin adı da
      // kenar olsun ve kenarın uzunluğunu versin
      tanım kare(kenar: Sayı) {{
        yinele(4) {{
          ileri(kenar)
          sağ()
        }}
      }}
      sil()
      hızıKur(orta)

      // komudumuzu iki kere çağıralım
      kare(100)
      kare(200)
      // Komutların bir "yan etkisi" olur, ama çıktısı olmaz.
      // Bu komutun yan etkisi de tuvalimize bir kare çizmek.
      // Çıktısı olmadığını şöyle de açık açık yazabilirdik:
      tanım kare2(kenar: Sayı): Birim = {{
        yinele(4) {{
          ileri(kenar)
          sağ()
        }}
      }}
      // Birim türünün amacı çıktı olmadığını belirtmek.
      kalemRenginiKur(mavi)
      kare2(80)
      kare2(120)
      </pre><br/>
      <em>Örnek 2:</em> <br/> <br/> <pre>
      // topla adında bir işlev yazalım (fonksiyon da denir)
      // İki girdisi bir de çıktısı var
      tanım topla(s1: Sayı, s2: Sayı) =
        s1 + s2

      çıktıyıSil()
      // işlevimizi satıryaz komudu içinden çağıralım
      satıryaz(topla(3, 5))
      // başka bir toplam bulmak istersek yine çağırabiliriz
      satıryaz(topla(20, 7))
      // yine çağıralım ama bu sefer çıktısına değişmez
      // bir ad takalım
      dez çıktı = topla(23, 19)
      // Kaplumbağaya yazdıralım
      kalemRenginiKur(mor)
      zıpla(-50)
      yazı(çıktı)
      gizle

      // Scala derleyicisi, işlevlerin çıktısının türünü genellikle belirler.
      // Ama yine de açık açık yazmakta da fayda var:
      tanım topla2(s1: Sayı, s2: Sayı): Sayı = s1 + s2
      belirt(topla(-5, 15) == topla2(-5, 15), "iki tanım eşit olmalı")

      </pre><br/>
      <em>Örnek 3:</em> <br/> <br/> <pre>
      // köşegen adında bir işlev tanımlayalım
      // eni ve boyu verilen bir dikdörtenin (ya da karenin) köşegen uzunluğunu bulsun
      tanım köşegen(en: Kesir, boy: Kesir): Kesir = karekökü(karesi(en) + karesi(boy))
      // Çıktının türünü de açıkca yazdık. Gerek yoktu ama yine de iyi bir alışkanlık bu
      satıryaz(köşegen(1, 1)) // birim karenin köşegeni bize 2'nin karekökünü verir
      satıryaz(köşegen(3, 4)) // çok meşhur bir dik üçgen
      satıryaz(köşegen(5, 12)) // bu da kenarları tam sayı olan başka bir dik üçgen

      </pre><br/>
      <em>Örnek 4:</em> <br/> <br/> <pre>
      // açı adında bir komut tanımlayalım
      // kaplumbağaya renkli bir açı çizdirsin
      tanım açı(açı: Kesir = 90, renk: Renk = kırmızı, boy: Sayı = 150): Birim = {{
        kalemRenginiKur(renk)
        ileri(boy)
        geri(boy)
        sol(açı)
        kalemRenginiKur(renk)
        ileri(boy)
        yazı(açı)
        geri(boy)
        sağ(açı)
      }}

      // Ufak birkaç ayarlama
      zıpla(50)
      sol; zıpla; sağ
      hızıKur(orta)
      kalemKalınlığınıKur(1)

      açı() // varsayılan açı 90 derece yani dik açı
      açı(60, yeşil, 200)
      açı(45, mavi, 175)
      açı(30) // varsayılan renk kırmızı

      </pre>
      </div>.toString,

    "için" -> <div> Çok faydalı bir anahtar sözcük. İki değişik kullanımı vardır: <br/>
      <br/>

      1- [komutlarla]:<br/>
    <br/>

      <strong>için</strong> (sayı &lt;- 1 |-| n) {{ komutlar }} - komutları n kere yineler. Bu arada 
       sayı da 1'den n'ye kadar değişir ve komutlar tarafından kullanılabilir <br/>
      <br/>

    <pre>
      silipSakla
      hızıKur(hızlı)
      kalemKalınlığınıKur(5)
      <strong>için</strong> (sayı &lt;- 1 |-| 10) {{
          dez yarıçap = 19 + sayı * 10
          kalemRenginiKur(rastgeleRenk)
          kalemiKaldır
          ileri(sayı * 2.2); sol; ileri(10); sağ
          kalemiİndir
          daire(yarıçap)
      }}
    </pre>

      2- [deyişlerle]:<br/>
    <br/>

      <strong>için</strong> (sayı &lt;- 1 |-| n) ver {{ deyiş }} - Her yinelemede verilen deyişi ve sayının o andaki değerini kullanarak 
        bir öge oluşturur ve bunların hepsini bir arada çıktı olarak verir. <br/>
      <br/>

    <pre>
      çıktıyıSil
      dez dizi = <strong>için</strong> (sayı &lt;- 0 |- 21) ver (sayı * sayı)
      satıryaz(dizi)
      // ya da şöyle yazdırabilirsin:
      satıryaz("ilk 20 kare:" ++ dizi.yazıYap("{{", " ", "}}"))
    </pre>

    </div>.toString,

    "eğer" -> <div>
      <strong>eğer</strong>(koşul) - Programın işleyişinde bir karar aşaması tanımlar.
      <br/> Arkasından <strong>yoksa</strong> da gelebilir.
      <br/>
      <br/><em>Örnek:</em> <br/>
      <pre>
      çıktıyıSil
      dez koşulSağlandıMı = rastgeleİkil
      eğer (koşulSağlandıMı) satıryaz("Koşul sağlandı")
      yoksa satıryaz("Hayır, koşul sağlanmadı")
      satıryaz("\nKontrolla beraber return tuşuna basarak tekrar tekrar çalıştır")
      </pre> <strong>rastgeleİkil</strong> komudu yazı tura atmaya benzer. Yüzde elli ihtimalle doğru yüzde elli ihtimalle de yanlış çıkar.
      <strong>yoksa</strong> anahtar sözcüğüne de bakın.

    <br/>
    <br/> <em>Büyük örnek:</em> <br/>    <br/>
      Bak yazılım nasıl ciddi bir iştir ve dikkatsizliğe gelmez, onu da burada görelim. 
      Her türlü olasılığı değerlendirmek epey emek işi. Sevmeden olmaz.

    <pre>

      çıktıyıSil
      dez yazı = satıroku("10 ile 20 arasında bir sayı girer misin?")
      // hemen alttaki kodu şimdilik atla ve
      // eğer'le başlayan satırdan itibaren okumaya devam et.
      // Girdinin yazı olmaması durumu idare etmemiz için gerekli. Ama
      // henüz bilmediğin birkaç anahtar sözcük ve Belki türünü kullanıyor.

      dez girdi = yazı.sayıyaBelki eşle {{
        durum Biri(sayı) => sayı
        durum Hiçbiri    => 0
      }}

      // Buradan itibaren okumaya devam!
      eğer (girdi == 0)
          eğer (yazı.boşMu)
            satıryaz("Oyun bozancılık yaptın. Hiçbir şey girmedin.")
          yoksa eğer (yazı == "0") satıryaz("Oyun bozancılık yaptın ve 0 girdin!")
          yoksa satıryaz("Oyun bozancılık yaptın. Sayı girmedin.")
      yoksa {{
          eğer (girdi > 15) satıryaz("15'ten büyük bir sayı seçtin")
          eğer (girdi &lt; 15) satıryaz("15'ten küçük bir sayı seçtin")
          eğer (girdi == 15) satıryaz("15'i seçtin. Bak şu tesadüfe!")
          yoksa eğer (girdi > 20 || girdi &lt; 10)
            satıryaz("Neden 10 ile 20 arasında bir sayı girmedin?")
      }} // birden çok komut olunca burada gerekli oldu kıvırcık parantezler.

      satıryaz("\nKontrolla return tuşuna basıp tekrar oynayabilirsin")
      yazılımcıkDüzenleyicisiniEtkinleştir()

      </pre>
      </div>.toString,


    "rastgeleİkil" -> <div>
      <strong>rastgeleİkil</strong> - Girdi almayan bir komut. Çıktısının türü İkil, değeri de ya yanlış ya da doğru olur. İki seçeneğin de olasılığı yüzde ellidir.
      <br/>
      <br/><em>Örnek:</em> <br/>
      <pre>
      çıktıyıSil
      dez yirmiTane = (1 |-| 10).işle(s => (s, rastgeleİkil))
      yirmiTane.herbiriİçin(satıryaz)

      // Bir örnek daha. doğru tura, yanlış da yazı olsun.
      // Yüz kere para atsak, kaç tanesi tura gelir acaba?
      silipSakla
      dez say = (1 |-| 100).ele(s => rastgeleİkil == doğru).boyu
      yazı(say)

      </pre>
      </div>.toString,


    "yoksa" -> <div>
      <strong>yoksa</strong> - Sadece <strong>eğer</strong> anahtar sözcüğünden sonra kullanılır. Koşul sağlanmazsa etkinleşir.
      <br/>
      <br/><em>Örnek:</em> <br/>
      <pre>
      çıktıyıSil
      silipSakla
      dez para = rastgeleİkil
      dez iddia = eğer (para) "Tura" yoksa "Yazı"
      yazı(iddia ++ " geldi!")
      satıryaz("Ctrl-return ile tekrar tekrar çalıştır")
      </pre>
      </div>.toString,


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
      val araba = yeniKaplumbağa(100, 200, Görünüş.araba)
      araba.canlandırmaHızınıKur(100)
      araba.kalemRenginiKur(mavi)
      araba.ilerle(0,0)
      araba.kuzey()
      </pre>Bu örnek bir araba oluşturur, (100, 200) noktasından (0, 0) noktasına hızlıca giderek mavi bir doğru parçası, yani bir çizgi çizer.
      </div>.toString,

    "ileri" -> <div><strong>ileri</strong>(adımSayısı) - Bu komut kaplumbağaya verilen sayı kadar adım atırarak baktığı doğrultuda ilerletir. Adım sayısı verilmemişse 25 adım atar.</div>.toString,

    "geri" -> <div><strong>geri</strong>(adımSayısı) - ileri komutunun tersi. Kaplumbağayı verilen sayı kadar geri götürür. Adım sayısı verilmemişse 25 adım atar.</div>.toString,

    "sol" -> <div>
      <strong>sol</strong>() - Bu komut kaplumbağayı olduğu yerde sola doğru (saat yönünün tersine doğru) 90 derece döndürür. <br/>
      <strong>sol</strong>(derece) - Bu komut kaplumbağayı olduğu yerde sola doğru (saat yönünün tersine) verilen derece kadar döndürür. <br/>
      <strong>sol</strong>(derece, yarıçap) - Bu komut kaplumbağayı verilen yarıçaplı bir yay üzerinde sola doğru (saat yönünün tersine doğru) verilen derece kadar döndürerek ilerletir. <br/> </div>.toString,

    "sağ" -> <div> <strong>sağ</strong>() - Bu komut kaplumbağayı sağa doğru (saat yönününde) 90 derece döndürür. <br/> <strong>sağ</strong>(derece) - Bu komut kaplumbağayı sağa doğru (saat yönünde) verilen derece kadar döndürür. <br/> <strong>sağ</strong>(derece, yarıçap) - Bu komut kaplumbağayı verilen yarıçaplı bir yay üzerinde sağa doğru (saat yönünde) verilen derece kadar döndürerek ilerletir. <br/> </div>.toString,
    "atla" -> <div> <strong>atla</strong>(x, y) - Bu komut kaplumbağayı çizgi çizmeden (x, y) noktasına götürür. Kaplumbağanın yönü değişmez. <br/> </div>.toString,

    "ilerle" -> <div><strong>ilerle</strong>(x, y) - Bu komut kaplumbağanın yönünü (x, y) noktasına çevirir ve o noktaya kadar götürür. </div>.toString,

    "zıpla" -> <div> <strong>zıpla</strong>(adımSayısı) - Bu komut <em>kalemi kaldırıp</em> kaplumbağayı verilen adım kadar ilerletir, böylece çizgi çizilmemiş olur. Sonra da kalemi indirir ki arkadan gelen komutlar çizmeye devam etsin. <br/> </div>.toString,

    "ev" -> <div><strong>ev</strong>() - Bu komut kaplumbağayı başlangıç noktasına götürür ve yönünü kuzeye çevirir. </div>.toString,

    "noktayaDön" -> <div>
      <strong>noktayaDön</strong>(x, y) - Bu komut kaplumbağayı (x, y) noktasına çevirir. <br/>
      <strong>noktayaDön</strong>(nokta) - Bu komut kaplumbağayı verilen noktaya çevirir. <br/>
      <br/><em>Örnek:</em> <br/>      <pre>
      dez n = Nokta(100, 100)
      noktayaDön(n)
      </pre>
      </div>.toString,

    "açıyaDön" -> <div><strong>açıyaDön</strong>(angle) - Bu komut kaplumbağayı verilen açıya çevirir. (0 derece ekranın sağına bakar (<em>doğu</em>), 90 yukarı (<em>kuzey</em>)).</div>.toString,

    "doğrultu" -> <div><strong>doğrultu</strong> - Bu komut kaplumbağanın yönünü bildirir. (0 derece ekranın sağına bakar (<em>doğu</em>), 90 yukarı (<em>kuzey</em>)).</div>.toString,

    "Nokta" -> <div><strong>Nokta(x, y)</strong>() - Yeni bir nokta tanımlar. </div>.toString,

    "Aralık" -> <div><strong>Aralık(ilki, sonuncu, adım)</strong> - Yeni bir aralık tanımlar. <br/>
      <br/><em>Örnek:</em> <br/>      <pre>
    Aralık(1, 100, 7).herbiriİçin {{ x =>
        dez kare = x*x
        satıryaz(x, kare, kare*x, kare*kare)
    }} </pre>
 </div>.toString,

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
    "silipSakla" -> <div><strong>silipSakla</strong>() - Bu komut tuvaldeki çizimleri siler ve kaplumbağayı görünmez kılar. </div>.toString,
    "çizimiSil" -> <div><strong>çizimiSil</strong>() - Bu komut tuvaldeki çizimleri siler. </div>.toString,

    // todo: Renk* üçlüsü için help yazılmıyor
    "RenkKYM" -> <div><strong>RenkKYM</strong>(kırmızı, yeşil, mavi, saydamlık) - Verilen kırmızı, yeşil, mavi ve saydamlık değerlerini kullanarak bir renk oluşturur. Saydamlık verilmezse tam saydam olur. Her eğer 0-255 arasında bir sayı olmalı. </div>.toString,

    "RenkADA" -> <div><strong>RenkADA</strong>(arıRenk, doygunluk, aydınlıkAçıklık) - Verilen arı renk (0-360), doygunluk (0-100), ve aydınlık/açıklık derecesi (0-100) değerlerini kullanarak bir renk oluşturur. </div>.toString,

    "RenkDD" -> <div><strong>RenkDD</strong>(x1, y1, renk1, x2, y2, renk2, dönüşlü) - Verilen iki renk ve iki nokta arasında doğrusal değişimle boyayan bir renk yelpazesi oluşturur. Son girdi olmasa da olur ve dönüşümsüz olur. </div>.toString,

    "Yazıyüzü" -> <div><strong>Yazıyüzü</strong>(adı, boyu) - Verilen yazı yüzü adı ve boyunu kullanarak yeni bir yazı yüzü oluşturur. </div>.toString,

    "artalanıKur" -> <div><strong>artalanıKur</strong>(renk) - Bu komutla tuval verilen renge boyanır. Kojonun tanıdığı sarı, mavi ve siyah gibi renkleri kullanabilirsiniz ya da <tt>RenkKYM</tt>, <tt>RenkADA</tt> ve <tt>RenkDD</tt> komutlarını kullanarak kendi renklerinizi yaratabilirsiniz. </div>.toString,

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

    "satıryaz" -> <div><strong>satıryaz</strong>(obj) - Bu komut verilen nesneyi yazı olarak çıktı penceresine yazar ve yeni satıra geçer. </div>.toString,

    "satıroku" -> <div><strong>satıroku</strong>(yazı) - Bu komut verilen yazıyı istem olarak çıktı penceresine yazar ve arkasından sizin yazdığınız bir satırı okur ve çıktı olarak verir. </div>.toString,

    "yuvarla" -> <div><strong>yuvarla</strong>(sayı, basamak) - Bu komut verilen sayıyı noktadan sonra verilen basamak sayısına kadar yuvarlar. </div>.toString,

    "rastgele" -> <div><strong>rastgele</strong>(üstsınır) - Bu komut verilen üst sınırdan küçük rastgele bir doğal sayı verir. Sıfırdan küçük sayılar vermez. </div>.toString,

    "rastgeleKesir" -> <div><strong>rastgeleÇift</strong>() - Bu komut verilen üst sınırdan küçük rastgele bir kesirli sayı (çift çözünürlüklü) verir. Sıfırdan küçük sayılar vermez. </div>.toString,

    "giysiKur" -> <div><strong>giysiKur</strong>(giysiDosyası) - Kaplumbağanın görünüşünü verilen dosyadaki resimle değiştirir. </div>.toString,

    "giysileriKur" -> <div><strong>giysilerKur</strong>(giysiDosyası1, giysiDosyası2, ...) - Kaplumbağa için bir dizi giysi belirler ve giysiDosyası1 resmini giydirir. <tt>birSonrakiGiysi()</tt> komutuyla dizideki bir sonraki giysiyi giydirebiliriz. </div>.toString,

    "birsonrakiGiysi" -> <div><strong>birSonrakiGiysi</strong>() - Kaplumbağaya <tt>giysilerKur()</tt> komutuyla girilen giysi dizisindeki bir sonraki resmi giydirir. </div>.toString,

    "buAn" -> <div><strong>buAn</strong>() - Bu komut evrensel zamana (UTC) göre 1 Ocak 1970 tam geceyarısından bu ana kadar geçen zamanı kesirsiz milisaniye olarak verir.</div>.toString,

    "buSaniye" -> <div><strong>buSaniye</strong>() - Bu komut evrensel zamana (UTC) göre 1 Ocak 1970 tam geceyarısından bu ana kadar geçen zamanı kesirli saniye olarak verir.</div>.toString,

    "hızıKur" -> <div><strong>hızıKur</strong>(hız) - Kaplumbağacığın hızını belirler. yavaş, orta, hızlı ve çokHızlı değerlerinden birini dene.</div>.toString,

    "eksenler" ->
      <div>
      <strong>eksenler -> resim</strong> - Verilen resmin yerel eksenlerini çizerek yeni bir resim oluşturur.
      <br/>
      Bu ve benzeri yöntemler bir resimde değişiklik yapmak için kullanılır. Ayrıca benzer yöntemlerle birleştirilebilirler. Onun için türün adını BirleşebilenDönüştürücü koyduk. BD diye de kısalttık.
      <br/><em>Örnek:</em> <br/>
      <pre>
      tanım resim = Resim {{
        yinele(2) {{
          ileri(50); sağ()
          ileri(100); sağ()
        }}
      }}
      silVeSakla
      // dört tane BD * ile birleşiyorlar, ve -> ile resmi dönüştürüyorlar
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

    "götür" -> <div>

      <strong>götür</strong>(x, y) <br/>
      <strong>götür</strong>(nokta) <br/>
      <strong>götür</strong>(yöney2b) <br/> <br/>

      Bir resmi çizmeden önce verilen koordinatlara götürür. <br/>
      <br/><em>Örnek:</em> <br/>
      <pre>

      tanım kare = Resim {{
          yinele(4) {{
              ileri(50)
              sağ()
          }}
      }}

      silipSakla()
      eksenleriGöster
      dez r1 = götür(50, 100) -> kare
      r1.çiz

      dez r2 = götür(100, 50) * boyaRengi(mavi) -> kare
      dez r3 = götür(-50, 50) * boyaRengi(yeşil) -> kare
      dez r4 = götür(50, -50) * boyaRengi(kırmızı) -> kare
      çiz(r2, r3, r4)

    </pre> 'boyaRengi' ve 'götür' yöntemlerine birleşebilen bir dönüştürücü (BD) deriz, çünkü * ile birleştirebilir ve resmi dönüştürürler. Bir de 'döndür'e bak. <br/>

      </div>.toString,

    "döndür" -> <div>
      <strong>döndür</strong>(açı) - Bir resmi çizmeden önce verilen açı kadar saat yönünün tersine döndürür. <br/>
      360 derece döndürmek tam dönüş olur ve etkisi olmaz. <br/>
      <br/><em>Örnek:</em> <br/>
      <pre>

        silipSakla
        dez d1 = döndür(30) -> Resim.dikdörtgen(100, 20)
        d1.çiz

      </pre> Bu örnekten sonra açıklama ...
      </div>.toString,

    "sürüm" -> "sürüm - Çıktıya kullanılan Scala sürümünü yazar.",

    // todo: much more
  )

  // Aynı ad hem elle yazılmış bir komutta hem bir koleksiyon yönteminde
  // olabilir: sil (tuvali sil / yığını boşalt), yazı (tuvale yaz / aralığı
  // yazıya çevir). `elleYazılanİçerik ++ koleksiyonYardımı` deseydik koleksiyon
  // girdisi ötekini SESSİZCE ezerdi -- nitekim bir süre ezdi. Çakışanları
  // birleştiriyoruz, ikisi de görünüyor.
  //
  // lazy: koleksiyonYardımı aşağıdaki koleksiyonYöntemleri tablosunu okuyor,
  // o da bu satırdan SONRA tanımlı (Scala'nın val ilklendirme sırası tuzağı).
  private val ayraç =
    "<hr/><em>Bu ad başka bir türde de kullanılıyor:</em><br/>"

  lazy val content: Map[String, String] =
    koleksiyonYardımı.foldLeft(elleYazılanİçerik) {
      case (harita, (ad, metin)) =>
        harita.get(ad) match {
          case Some(eski) => harita + (ad -> (eski + ayraç + metin))
          case None       => harita + (ad -> metin)
        }
    }

  // Koleksiyon yöntemlerinin yardım metinleri: (ad, imza, açıklama, örnek, örneğin sonucu).
  //
  // Anahtar SADE ad -- "katla" girdisi Dizi'de de Dizin'de de Küme'de de aynı
  // metni gösteriyor, çünkü yardım araması sarmalayıcının adına değil yöntemin
  // adına bakıyor (bkz. KojoCompletionProvider.knownCompletion / specialOwner).
  //
  // Örneklerin HEPSİ KoleksiyonYardımıTest tarafından çalıştırılıp buradaki
  // sonuçla karşılaştırılıyor. Yani belge kodla birlikte doğru kalıyor:
  // bir yöntemin davranışı değişirse test kırılır.
  // 6. alan: yöntemin BULUNDUĞU türler. Boşsa yöntem cümlede sayılan beşlinin
  // (Dizi, Dizin, Yöney, Küme, Kuyruk) hepsinde var demektir. Bu alan
  // KoleksiyonYardımıTest bu alanı lite/i18n/tr/*.scala'yı tarayarak yeniden
  // türetip karşılaştırıyor: bir sarmalayıcıya yöntem eklenip yardım metni
  // unutulursa test düşer ve doğru listeyi yazar.
  val koleksiyonYöntemleri: List[(String, String, String, String, String, String)] = List(
    ("""başı""", """başı""", """Topluluğun ilk ögesi. Boşsa hata verir; güvenlisi başıBelki.""", """Dizin(3, 1, 2).başı""", """3""", """"""),
    ("""sonu""", """sonu""", """Topluluğun son ögesi.""", """Dizin(3, 1, 2).sonu""", """2""", """"""),
    ("""kuyruğu""", """kuyruğu""", """İlk öge dışında kalan her şey.""", """Dizin(3, 1, 2).kuyruğu""", """Dizin(1, 2)""", """"""),
    ("""önü""", """önü""", """Son öge dışında kalan her şey.""", """Dizin(3, 1, 2).önü""", """Dizin(3, 1)""", """"""),
    ("""boyu""", """boyu""", """Kaç öge var.""", """Dizin(3, 1, 2).boyu""", """3""", """"""),
    ("""boşMu""", """boşMu""", """Hiç öge yoksa doğru.""", """Dizin[Sayı]().boşMu""", """doğru""", """"""),
    ("""doluMu""", """doluMu""", """En az bir öge varsa doğru.""", """Dizin(1).doluMu""", """doğru""", """"""),
    ("""başıBelki""", """başıBelki""", """İlk öge, ama Belki içinde: boşsa Hiçbiri verir, hata vermez.""", """Dizin[Sayı]().başıBelki""", """Hiçbiri""", """"""),
    ("""sonuBelki""", """sonuBelki""", """Son öge, Belki içinde.""", """Dizin(3, 1, 2).sonuBelki""", """Biri(2)""", """"""),
    ("""sıralar""", """sıralar""", """Ögelerin sıra numaraları: 0, 1, 2, ...""", """Dizin(3, 1, 2).sıralar.dizine""", """Dizin(0, 1, 2)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""ele""", """ele(deneme)""", """Denemeden doğru dönen ögeleri tutar, ötekileri atar.""", """Dizin(1, 2, 3, 4).ele(_ % 2 == 0)""", """Dizin(2, 4)""", """"""),
    ("""eleDeğilse""", """eleDeğilse(deneme)""", """ele'nin tersi: denemeye UYMAYANLARI tutar.""", """Dizin(1, 2, 3, 4).eleDeğilse(_ % 2 == 0)""", """Dizin(1, 3)""", """"""),
    ("""işle""", """işle(işlev)""", """Her ögeyi işlevden geçirip yeni bir topluluk yapar.""", """Dizin(1, 2, 3).işle(_ * 10)""", """Dizin(10, 20, 30)""", """"""),
    ("""düzİşle""", """düzİşle(işlev)""", """Her öge için bir topluluk üretir, sonra hepsini tek düzeye serer.""", """Dizin(1, 2).düzİşle(x => Dizin(x, x))""", """Dizin(1, 1, 2, 2)""", """"""),
    ("""herbiriİçin""", """herbiriİçin(komut)""", """Her öge için bir komut çalıştırır. Değer döndürmez.""", """{ den t = 0; Dizin(1, 2, 3).herbiriİçin(x => t += x); t }""", """6""", """"""),
    ("""seçİşle""", """seçİşle(kısmiİşlev)""", """Hem eler hem işler: yalnız işlevin tanımlı olduğu ögeleri alır.""", """Dizin(1, 2, 3, 4).seçİşle { durum x eğer x % 2 == 0 => x * 10 }""", """Dizin(20, 40)""", """"""),
    ("""seçİşleİlk""", """seçİşleİlk(kısmiİşlev)""", """seçİşle gibi, ama yalnız ilk uyanı verir, Belki içinde.""", """Dizin(1, 2, 3).seçİşleİlk { durum x eğer x > 1 => x * 10 }""", """Biri(20)""", """"""),
    ("""bul""", """bul(deneme)""", """Denemeye uyan İLK ögeyi Belki içinde verir.""", """Dizin(1, 2, 3).bul(_ > 1)""", """Biri(2)""", """"""),
    ("""bulSondan""", """bulSondan(deneme)""", """Denemeye uyan SON ögeyi verir.""", """Dizin(1, 2, 3).bulSondan(_ < 3)""", """Biri(2)""", """Aralık, Dizi, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""varMı""", """varMı(deneme)""", """En az bir öge denemeye uyuyor mu.""", """Dizin(1, 2, 3).varMı(_ > 2)""", """doğru""", """"""),
    ("""hepsiDoğruMu""", """hepsiDoğruMu(deneme)""", """Ögelerin HEPSİ denemeye uyuyor mu.""", """Dizin(1, 2, 3).hepsiDoğruMu(_ > 0)""", """doğru""", """"""),
    ("""say""", """say(deneme)""", """Denemeye uyan kaç öge var.""", """Dizin(1, 2, 3, 4).say(_ % 2 == 0)""", """2""", """"""),
    ("""içeriyorMu""", """içeriyorMu(öge)""", """Bu öge içinde var mı.""", """Dizin(1, 2, 3).içeriyorMu(2)""", """doğru""", """"""),
    ("""sırası""", """sırası(öge)""", """Ögenin kaçıncı sırada olduğu. Yoksa -1.""", """Dizin(3, 1, 2).sırası(1)""", """1""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yineleyici, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""nerede""", """nerede(deneme)""", """Denemeye uyan ilk ögenin sırası. Yoksa -1.""", """Dizin(3, 1, 2).nerede(_ < 2)""", """1""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yineleyici, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""başındaMı""", """başındaMı(dizi)""", """Topluluk bu dizi ile başlıyor mu.""", """Dizin(1, 2, 3).başındaMı(Dizin(1, 2))""", """doğru""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""sonundaMı""", """sonundaMı(dizi)""", """Topluluk bu dizi ile bitiyor mu.""", """Dizin(1, 2, 3).sonundaMı(Dizin(2, 3))""", """doğru""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""karşılıklıMı""", """karşılıklıMı(öbürü)(deneme)""", """İki topluluğu öge öge karşılaştırır.""", """Dizin(1, 2).karşılıklıMı(Dizin(2, 4))((a, b) => b == a * 2)""", """doğru""", """Aralık, Dizi, Dizin, EsnekDizik, Eşlek, Kuyruk, MiskinDizin, Yineleyici, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""sıralı""", """sıralı""", """Ögeleri küçükten büyüğe dizer.""", """Dizin(3, 1, 2).sıralı""", """Dizin(1, 2, 3)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""sırala""", """sırala(iş)""", """İşlevin verdiği değere göre sıralar.""", """Dizin("aaa", "a", "aa").sırala(_.boyu)""", """Dizin(a, aa, aaa)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""sırayaSok""", """sırayaSok(önce)""", """Hangi öge önce gelsin, sen söyle.""", """Dizin(1, 3, 2).sırayaSok(_ > _)""", """Dizin(3, 2, 1)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""tersi""", """tersi""", """Ögeleri ters sıraya çevirir.""", """Dizin(1, 2, 3).tersi""", """Dizin(3, 2, 1)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, ÖncelikSırası, İkiUçluKuyruk"""),
    ("""yinelemesiz""", """yinelemesiz""", """Yinelenen ögelerin yalnız ilkini tutar.""", """Dizin(1, 2, 1, 3).yinelemesiz""", """Dizin(1, 2, 3)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yineleyici, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""indirge""", """indirge(işlem)""", """Ögeleri ikişer ikişer birleştirip tek değere indirir. Boşsa hata verir.""", """Dizin(1, 2, 3).indirge(_ + _)""", """6""", """"""),
    ("""katla""", """katla(başlangıç)(işlem)""", """Topluluklarda: indirge gibi, ama bir başlangıç değeri verirsin; boş toplulukta da çalışır. İkisindenBiri'nde katla(solİşlev, sağİşlev) demek: hangi taraftaysa ona uygun işlevi çalıştırır.""", """Dizin(1, 2, 3).katla(10)(_ + _)""", """16""", """"""),
    ("""soldanKatla""", """soldanKatla(başlangıç)(işlem)""", """Soldan sağa katlar. Sonuç ögelerden başka türde olabilir.""", """Dizin(1, 2, 3).soldanKatla("")((y, s) => y + s)""", """123""", """"""),
    ("""sağdanKatla""", """sağdanKatla(başlangıç)(işlem)""", """Sağdan sola katlar.""", """Dizin(1, 2, 3).sağdanKatla("")((s, y) => y + s)""", """321""", """"""),
    ("""tara""", """tara(başlangıç)(işlem)""", """Katlar ama ARA sonuçların hepsini verir.""", """Dizin(1, 2, 3).tara(0)(_ + _)""", """Dizin(0, 1, 3, 6)""", """"""),
    ("""taraSoldan""", """taraSoldan(başlangıç)(işlem)""", """tara'nın başka türde sonuç verebilen biçimi.""", """Dizin(1, 2, 3).taraSoldan("")((y, s) => y + s)""", """Dizin(, 1, 12, 123)""", """"""),
    ("""topla""", """topla""", """Sayıların toplamı.""", """Dizin(1, 2, 3).topla""", """6""", """"""),
    ("""çarp""", """çarp""", """Sayıların çarpımı.""", """Dizin(2, 3, 4).çarp""", """24""", """"""),
    ("""enUfağı""", """enUfağı""", """En küçük öge. Boşsa hata verir.""", """Dizin(3, 1, 2).enUfağı""", """1""", """"""),
    ("""enİrisi""", """enİrisi""", """En büyük öge.""", """Dizin(3, 1, 2).enİrisi""", """3""", """"""),
    ("""enUfağıBelki""", """enUfağıBelki""", """En küçük öge, Belki içinde: boş toplulukta hata vermez.""", """Dizin[Sayı]().enUfağıBelki""", """Hiçbiri""", """"""),
    ("""enİrisiBelki""", """enİrisiBelki""", """En büyük öge, Belki içinde.""", """Dizin(3, 1, 2).enİrisiBelki""", """Biri(3)""", """"""),
    ("""al""", """al(kaçTane)""", """Baştan bu kadar öge alır.""", """Dizin(1, 2, 3, 4).al(2)""", """Dizin(1, 2)""", """"""),
    ("""alSağdan""", """alSağdan(kaçTane)""", """Sondan bu kadar öge alır.""", """Dizin(1, 2, 3, 4).alSağdan(2)""", """Dizin(3, 4)""", """"""),
    ("""alDoğruKaldıkça""", """alDoğruKaldıkça(deneme)""", """Baştan başlar, deneme bozulunca durur.""", """Dizin(1, 2, 3, 1).alDoğruKaldıkça(_ < 3)""", """Dizin(1, 2)""", """"""),
    ("""düşür""", """düşür(kaçTane)""", """Baştan bu kadar ögeyi atar.""", """Dizin(1, 2, 3, 4).düşür(2)""", """Dizin(3, 4)""", """"""),
    ("""düşürSağdan""", """düşürSağdan(kaçTane)""", """Sondan bu kadar ögeyi atar.""", """Dizin(1, 2, 3, 4).düşürSağdan(2)""", """Dizin(1, 2)""", """"""),
    ("""düşürDoğruKaldıkça""", """düşürDoğruKaldıkça(deneme)""", """Baştan başlar, denemeye uyanları atar, ilk uymayanda durur.""", """Dizin(1, 2, 3, 1).düşürDoğruKaldıkça(_ < 3)""", """Dizin(3, 1)""", """"""),
    ("""dilim""", """dilim(nereden, nereye)""", """Verilen iki sıra arasındaki parçayı alır. nereye dahil DEĞİL.""", """Dizin(1, 2, 3, 4).dilim(1, 3)""", """Dizin(2, 3)""", """"""),
    ("""böl""", """böl(deneme)""", """İkiye ayırır: uyanlar ve uymayanlar.""", """Dizin(1, 2, 3, 4).böl(_ % 2 == 0)""", """(Dizin(2, 4),Dizin(1, 3))""", """"""),
    ("""bölDoğruKaldıkça""", """bölDoğruKaldıkça(deneme)""", """alDoğruKaldıkça ile düşürDoğruKaldıkça'yı birlikte verir.""", """Dizin(1, 2, 3, 1).bölDoğruKaldıkça(_ < 3)""", """(Dizin(1, 2),Dizin(3, 1))""", """"""),
    ("""bölYerinden""", """bölYerinden(yeri)""", """Verilen sıradan ikiye böler.""", """Dizin(1, 2, 3, 4).bölYerinden(2)""", """(Dizin(1, 2),Dizin(3, 4))""", """"""),
    ("""öbekle""", """öbekle(anahtar)""", """Ögeleri anahtara göre öbeklere ayırır; sonuç bir eşlek.""", """Dizin(1, 2, 3, 4).öbekle(_ % 2)(0)""", """Dizin(2, 4)""", """"""),
    ("""öbekli""", """öbekli(boy)""", """Bu boyda ardışık öbeklere böler.""", """Dizin(1, 2, 3, 4).öbekli(2).dizine""", """Dizin(Dizin(1, 2), Dizin(3, 4))""", """"""),
    ("""kayarÖbekli""", """kayarÖbekli(boy)""", """Bu boyda KAYAN bir pencere gezdirir.""", """Dizin(1, 2, 3).kayarÖbekli(2).dizine""", """Dizin(Dizin(1, 2), Dizin(2, 3))""", """"""),
    ("""kombinasyonlar""", """kombinasyonlar(kaçTane)""", """Bu kadar ögeli tüm alt seçimler (sıra önemsiz).""", """Dizin(1, 2, 3).kombinasyonlar(2).dizine""", """Dizin(Dizin(1, 2), Dizin(1, 3), Dizin(2, 3))""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""permütasyonlar""", """permütasyonlar""", """Ögelerin tüm sıralanışları.""", """Dizin(1, 2).permütasyonlar.dizine""", """Dizin(Dizin(1, 2), Dizin(2, 1))""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""ikile""", """ikile(öbürü)""", """İki topluluğu karşılıklı eşleştirir; kısa olan bitince durur.""", """Dizin(1, 2).ikile(Dizin("a", "b"))""", """Dizin((1,a), (2,b))""", """"""),
    ("""ikileSırayla""", """ikileSırayla""", """Her ögeyi sıra numarasıyla eşler.""", """Dizin("a", "b").ikileSırayla""", """Dizin((a,0), (b,1))""", """"""),
    ("""ikiliyiAç""", """ikiliyiAç""", """İkililer topluluğunu iki ayrı topluluğa açar. ikile'nin tersi.""", """Dizin((1, "a"), (2, "b")).ikiliyiAç""", """(Dizin(1, 2),Dizin(a, b))""", """"""),
    ("""düzleştir""", """düzleştir""", """İç içe toplulukları tek düzeye serer.""", """Dizin(Dizin(1, 2), Dizin(3)).düzleştir""", """Dizin(1, 2, 3)""", """"""),
    ("""devrik""", """devrik""", """Satırlarla sütunları yer değiştirir.""", """Dizin(Dizin(1, 2), Dizin(3, 4)).devrik""", """Dizin(Dizin(1, 3), Dizin(2, 4))""", """"""),
    ("""sonunaEkle""", """sonunaEkle(öge)""", """Sonuna bir öge eklenmiş YENİ topluluk verir.""", """Dizin(1, 2).sonunaEkle(3)""", """Dizin(1, 2, 3)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""önüneEkle""", """önüneEkle(öge)""", """Başına bir öge eklenmiş YENİ topluluk verir.""", """Dizin(2, 3).önüneEkle(1)""", """Dizin(1, 2, 3)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""bileşim""", """bileşim(öbürü)""", """İki topluluğu uç uca ekler.""", """Dizin(1, 2).bileşim(Dizin(3))""", """Dizin(1, 2, 3)""", """"""),
    ("""fark""", """fark(öbürü)""", """Ötekinde olan ögeleri çıkarır.""", """Dizin(1, 2, 3).fark(Dizin(2))""", """Dizin(1, 3)""", """"""),
    ("""kesişim""", """kesişim(öbürü)""", """İkisinde de olan ögeler.""", """Dizin(1, 2, 3).kesişim(Dizin(2, 3, 4))""", """Dizin(2, 3)""", """"""),
    ("""uzat""", """uzat(boy, öge)""", """Bu boya erişene dek sonuna ögeyi ekler.""", """Dizin(1, 2).uzat(4, 0)""", """Dizin(1, 2, 0, 0)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yineleyici, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""yama""", """yama(nereden, yenisi, kaçTane)""", """Bir parçayı söküp yerine başkasını koyar.""", """Dizin(1, 2, 3).yama(1, Dizin(8, 9), 1)""", """Dizin(1, 8, 9, 3)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yineleyici, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""yazıYap""", """yazıYap(ara)""", """Ögeleri araya bu yazıyı koyarak tek yazı yapar.""", """Dizin(1, 2, 3).yazıYap("-")""", """1-2-3""", """"""),
    ("""dizine""", """dizine""", """Dizin'e (List) çevirir.""", """Dizin(1, 2, 3).dizine""", """Dizin(1, 2, 3)""", """"""),
    ("""diziye""", """diziye""", """Dizi'ye (Seq) çevirir.""", """Küme(1).diziye""", """Dizin(1)""", """"""),
    ("""kümeye""", """kümeye""", """Küme'ye çevirir; yinelenenler tekleşir.""", """Dizin(1, 2, 1).kümeye""", """Küme(1, 2)""", """"""),
    ("""yöneye""", """yöneye""", """Yöney'e (Vector) çevirir.""", """Dizin(1, 2).yöneye""", """Yöney(1, 2)""", """"""),
    ("""dizime""", """dizime""", """Dizim'e (Array) çevirir. DİKKAT: dizime'nin hemen ardına (0) yazamazsın -- Scala onu örtük ClassTag listesi sanıyor; araya bir ad ya da diziye koy.""", """Dizin(7, 8, 9).dizime.diziye.başı""", """7""", """"""),
    // --- 2. parti: türe özgü adlar (Eşlek/Eşlem, Küme, Yığın, Kuyruk,
    // ÖncelikSırası, Belki, Yazı, Aralık, MiskinDizin, Yineleyici) ---
    ("""hepsiİçinDoğruMu""", """hepsiİçinDoğruMu(deneme)""", """hepsiDoğruMu ile aynı; kitapçıkta ikisi de geçiyor.""", """Dizin(2, 4).hepsiİçinDoğruMu(_ % 2 == 0)""", """doğru""", """"""),
    ("""indirgeSoldan""", """indirgeSoldan(işlem)""", """Soldan sağa indirger. indirge ile aynı sonucu verir ama sırayı sen bilirsin.""", """Dizin(1, 2, 3).indirgeSoldan(_ - _)""", """-4""", """"""),
    ("""indirgeSağdan""", """indirgeSağdan(işlem)""", """Sağdan sola indirger.""", """Dizin(1, 2, 3).indirgeSağdan(_ - _)""", """2""", """"""),
    ("""indirgeBelki""", """indirgeBelki(işlem)""", """indirge'nin hata vermeyeni: boş toplulukta Hiçbiri verir.""", """Dizin[Sayı]().indirgeBelki(_ + _)""", """Hiçbiri""", """"""),
    ("""indirgeSoldanBelki""", """indirgeSoldanBelki(işlem)""", """indirgeSoldan'ın Belki veren biçimi.""", """Dizin(1, 2, 3).indirgeSoldanBelki(_ + _)""", """Biri(6)""", """"""),
    ("""indirgeSağdanBelki""", """indirgeSağdanBelki(işlem)""", """indirgeSağdan'ın Belki veren biçimi.""", """Dizin[Sayı]().indirgeSağdanBelki(_ + _)""", """Hiçbiri""", """"""),
    ("""taraSağdan""", """taraSağdan(başlangıç)(işlem)""", """Sağdan sola tarar; ara sonuçların hepsini verir.""", """Dizin(1, 2, 3).taraSağdan(0)(_ + _)""", """Dizin(6, 5, 3, 0)""", """"""),
    ("""öbekleİşle""", """öbekleİşle(anahtar)(değer)""", """Öbekle, ama her ögeyi de işlevden geçir.""", """Dizin(1, 2, 3, 4).öbekleİşle(_ % 2)(_ * 10)(0)""", """Dizin(20, 40)""", """"""),
    ("""öbekleİşleİndirge""", """öbekleİşleİndirge(anahtar)(değer)(indirge)""", """Öbekle, işle, sonra her öbeği tek değere indir.""", """Dizin(1, 2, 3, 4).öbekleİşleİndirge(_ % 2)(x => x)(_ + _)(0)""", """6""", """"""),
    ("""kuyruklar""", """kuyruklar""", """Baştan birer birer kısalan bütün kuyrukları verir.""", """Dizin(1, 2, 3).kuyruklar.dizine""", """Dizin(Dizin(1, 2, 3), Dizin(2, 3), Dizin(3), Dizin())""", """"""),
    ("""önler""", """önler""", """Sondan birer birer kısalan bütün önleri verir.""", """Dizin(1, 2, 3).önler.dizine""", """Dizin(Dizin(1, 2, 3), Dizin(1, 2), Dizin(1), Dizin())""", """"""),
    ("""ikileHepsini""", """ikileHepsini(öbürü, buDolgu, oDolgu)""", """ikile gibi, ama kısa olan bitince dolgu değerini kullanır.""", """Dizin(1, 2, 3).ikileHepsini(Dizin("a"), 0, "-")""", """Dizin((1,a), (2,-), (3,-))""", """"""),
    ("""ikileKonumla""", """ikileKonumla""", """ikileSırayla ile aynı (kitapçık adı).""", """Dizin("a", "b").ikileKonumla""", """Dizin((a,0), (b,1))""", """"""),
    ("""yinelemesizİşlevle""", """yinelemesizİşlevle(işlev)""", """İşlevin verdiği değere göre yinelenenleri atar.""", """Dizin("al", "at", "be").yinelemesizİşlevle(_.harf(0))""", """Dizin(al, be)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yineleyici, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""sonunaEkleHepsini""", """sonunaEkleHepsini(öbürü)""", """Sonuna bir topluluğun tümünü ekler.""", """Dizin(1, 2).sonunaEkleHepsini(Dizin(3, 4))""", """Dizin(1, 2, 3, 4)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""önüneEkleHepsini""", """önüneEkleHepsini(öbürü)""", """Başına bir topluluğun tümünü ekler.""", """Dizin(3, 4).önüneEkleHepsini(Dizin(1, 2))""", """Dizin(1, 2, 3, 4)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""neredeSondan""", """neredeSondan(deneme)""", """Denemeye uyan SON ögenin sırası.""", """Dizin(1, 2, 3, 2).neredeSondan(_ == 2)""", """3""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""sırasıSondan""", """sırasıSondan(öge)""", """Ögenin son geçtiği sıra.""", """Dizin(1, 2, 3, 2).sırasıSondan(2)""", """3""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""içeriyorMuDilim""", """içeriyorMuDilim(dilim)""", """Bu alt dizi topluluğun içinde geçiyor mu.""", """Dizin(1, 2, 3).içeriyorMuDilim(Dizin(2, 3))""", """doğru""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""dilimSırası""", """dilimSırası(dilim)""", """Alt dizinin başladığı sıra. Yoksa -1.""", """Dizin(1, 2, 3).dilimSırası(Dizin(2, 3))""", """1""", """Aralık, Dizi, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""elekle""", """elekle(deneme)""", """ele gibi ama tembel. Doğrudan pek çağrılmaz: `için` döngüsündeki `eğer` süzgecini derleyici buna çevirir.""", """için (x <- Biri(5) eğer x > 1) ver x * 10""", """Biri(50)""", """Aralık, Belki"""),
    ("""herÖgeİçin""", """herÖgeİçin(komut)""", """herbiriİçin ile aynı (kitapçık adı).""", """{ den t = 0; Aralık.kapalı(1, 3).herÖgeİçin(x => t += x); t }""", """6""", """Aralık, Eşlek, Eşlem"""),
    ("""tersİşle""", """tersİşle(işlev)""", """Önce ters çevirir, sonra işler.""", """Dizin(1, 2, 3).tersİşle(_ * 10)""", """Dizin(30, 20, 10)""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Kuyruk, MiskinDizin, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""alSırayla""", """alSırayla(kaçTane)""", """Baştan bu kadar ikili/öge alır.""", """Eşlek("a" -> 1).alSırayla(1).sayı""", """1""", """Eşlek, Eşlem"""),
    ("""eşli""", """eşli(anahtar)""", """Bu anahtar eşlemde var mı.""", """Eşlek("a" -> 1).eşli("a")""", """doğru""", """Eşlek, Eşlem"""),
    ("""alYoksa""", """alYoksa(anahtar, varsayılan)""", """Anahtarın değerini verir; yoksa varsayılanı.""", """Eşlek("a" -> 1).alYoksa("b", 0)""", """0""", """Belki, Eşlek, Eşlem, İkisindenBiri"""),
    ("""anahtarlar""", """anahtarlar""", """Bütün anahtarlar.""", """Eşlek("a" -> 1).anahtarlar.dizine""", """Dizin(a)""", """Eşlek, Eşlem"""),
    ("""değerler""", """değerler""", """Bütün değerler.""", """Eşlek("a" -> 1).değerler.dizine""", """Dizin(1)""", """Eşlek, Eşlem"""),
    ("""anahtarKümesi""", """anahtarKümesi""", """Anahtarlar, küme olarak.""", """Eşlek("a" -> 1, "b" -> 2).anahtarKümesi.boyu""", """2""", """Eşlek, Eşlem"""),
    ("""sayı""", """sayı""", """Kaç ikili var.""", """Eşlek("a" -> 1, "b" -> 2).sayı""", """2""", """EsnekDizim, Eşlek, Eşlem"""),
    ("""eşEkle""", """eşEkle(ikili)""", """Eşlem'e bir ikili ekler. Eşlem'in KENDİSİ değişir.""", """{ dez e = Eşlem("a" -> 1); e.eşEkle("b" -> 2); e.sayı }""", """2""", """Eşlem"""),
    ("""koy""", """koy(anahtar, değer)""", """Eşlem'e koyar; o anahtarda eskiden ne varsa Belki içinde verir. (Yığın'da koy = it demek.)""", """Eşlem("a" -> 1).koy("a", 9)""", """Biri(1)""", """Eşlem, Kuyruk, Yığın, ÖncelikSırası, İkiUçluKuyruk"""),
    ("""güncelle""", """güncelle(anahtar, değer)""", """Eşlem'de o anahtarın değerini değiştirir. Dizilerde güncelle(sıra, öge) demek.""", """{ dez e = Eşlem("a" -> 1); e.güncelle("a", 9); e("a") }""", """9""", """Eşlem, Kuyruk, Yığın"""),
    ("""alYoksaEkle""", """alYoksaEkle(anahtar, değer)""", """Varsa değerini verir; yoksa önce ekler sonra verir.""", """{ dez e = Eşlem("a" -> 1); e.alYoksaEkle("b", 2); e.sayı }""", """2""", """Eşlem"""),
    ("""değerleriİşle""", """değerleriİşle(işlev)""", """Anahtarlara dokunmadan yalnız değerleri işler.""", """Eşlek("a" -> 1).değerleriİşle(_ * 10)""", """Eşlek(a -> 10)""", """Eşlek, Eşlem"""),
    ("""anahtarlarıEle""", """anahtarlarıEle(deneme)""", """Anahtarına bakarak eler.""", """Eşlek("a" -> 1, "b" -> 2).anahtarlarıEle(_ == "a")""", """Eşlek(a -> 1)""", """Eşlek, Eşlem"""),
    ("""herİkiliİçin""", """herİkiliİçin(komut)""", """Her anahtar-değer çifti için komut çalıştırır; ikiliyi ayrı ayrı verir.""", """{ den t = 0; Eşlek("a" -> 1, "b" -> 2).herİkiliİçin((a, d) => t += d); t }""", """3""", """Eşlek, Eşlem"""),
    ("""değiştirilmiş""", """değiştirilmiş(anahtar, değer)""", """Bir anahtarı değiştirilmiş YENİ eşlek verir; eskisi durur.""", """Eşlek("a" -> 1).değiştirilmiş("a", 9)""", """Eşlek(a -> 9)""", """Eşlek"""),
    ("""çıkarılmış""", """çıkarılmış(anahtar)""", """O anahtar çıkarılmış YENİ eşlek/küme verir.""", """Eşlek("a" -> 1, "b" -> 2).çıkarılmış("a")""", """Eşlek(b -> 2)""", """Eşlek, Küme"""),
    ("""hepsiÇıkarılmış""", """hepsiÇıkarılmış(anahtarlar)""", """Verilenlerin hepsi çıkarılmış yeni eşlek/küme.""", """Eşlek("a" -> 1, "b" -> 2).hepsiÇıkarılmış(Dizin("a", "b")).sayı""", """0""", """Eşlek, Küme"""),
    ("""kaldır""", """kaldır""", """Eşleği bir işleve çevirir: anahtarı verirsin, Belki alırsın.""", """Eşlek("a" -> 1).kaldır("yok")""", """Hiçbiri""", """Eşlek, Eşlem"""),
    ("""varsayılanDeğerle""", """varsayılanDeğerle(değer)""", """Olmayan anahtarlar için bu değeri veren bir eşlek.""", """Eşlek("a" -> 1).varsayılanDeğerle(0)("yok")""", """0""", """Eşlek, Eşlem"""),
    ("""dönüştür""", """dönüştür(işlev)""", """Anahtarı da değeri de görerek yeni değerler üretir.""", """Eşlek("a" -> 1).dönüştür((a, d) => a + d)""", """Eşlek(a -> a1)""", """Eşlek"""),
    ("""anahtarYineleyici""", """anahtarYineleyici""", """Anahtarları gezen yineleyici.""", """Eşlek("a" -> 1).anahtarYineleyici.dizine""", """Dizin(a)""", """Eşlek, Eşlem"""),
    ("""değerYineleyici""", """değerYineleyici""", """Değerleri gezen yineleyici.""", """Eşlek("a" -> 1).değerYineleyici.dizine""", """Dizin(1)""", """Eşlek, Eşlem"""),
    ("""eşleğe""", """eşleğe""", """İkililer topluluğunu Eşlek'e çevirir.""", """{ dez e = Dizin((1, "bir")).eşleğe; e(1) }""", """bir""", """"""),
    ("""eşleme""", """eşleme""", """İkililer topluluğunu (değişebilen) Eşlem'e çevirir.""", """{ dez e = Dizin((1, "bir")).eşleme; e(1) }""", """bir""", """"""),
    ("""boşalt""", """boşalt()""", """Değişebilen bir topluluğun içini boşaltır.""", """{ dez e = Eşlem("a" -> 1); e.boşalt(); e.sayı }""", """0""", """EsnekDizik, Eşlem, Kuyruk"""),
    ("""ekli""", """ekli(öge)""", """Öge eklenmiş YENİ küme verir.""", """Küme(1, 2).ekli(3).boyu""", """3""", """Küme"""),
    ("""altKümesiMi""", """altKümesiMi(öbürü)""", """Bu küme ötekinin içinde tümüyle var mı.""", """Küme(1, 2).altKümesiMi(Küme(1, 2, 3))""", """doğru""", """Küme"""),
    ("""altKümeleri""", """altKümeleri(ögeSayısı)""", """Kümenin bu kadar ögeli bütün alt kümeleri.""", """Küme(1, 2, 3).altKümeleri(2).dizine.boyu""", """3""", """Küme"""),
    ("""it""", """it(öge)""", """Yığının TEPESİNE koyar. koy ile aynı.""", """Yığın(1, 2).it(3).tepe""", """3""", """Yığın"""),
    ("""çek""", """çek()""", """Yığının tepesindeki ögeyi alır ve çıkarır. al ile aynı.""", """{ dez y = Yığın(1, 2, 3); (y.çek(), y.tane) }""", """(3,2)""", """Yığın"""),
    ("""tepe""", """tepe""", """Tepedeki ögeyi çıkarmadan gösterir. tepesi ile aynı.""", """Yığın(1, 2, 3).tepe""", """3""", """Yığın"""),
    ("""tane""", """tane""", """Kaç öge var. boyu ile aynı (kitapçık adı).""", """Yığın(1, 2, 3).tane""", """3""", """Yığın, İkiUçluKuyruk"""),
    ("""dizi""", """dizi""", """İçeriği Dizi olarak verir. Yığında tepeden dibe.""", """Yığın(1, 2, 3).dizi""", """Dizin(3, 2, 1)""", """EsnekDizim, Eşlek, Eşlem, Yığın, İkiUçluKuyruk"""),
    ("""sil""", """sil()""", """Yığını/kuyruğu tümüyle boşaltır.""", """{ dez y = Yığın(1, 2); y.sil(); y.tane }""", """0""", """EsnekDizim, EsnekYazı, Kuyruk, Yığın, ÖncelikSırası, İkiUçluKuyruk"""),
    ("""itHepsini""", """itHepsini(ögeler)""", """Verilen ögelerin hepsini sırayla iter. koyHepsini ile aynı.""", """Yığın.boş[Sayı].itHepsini(Dizin(1, 2, 3)).tepe""", """3""", """Yığın"""),
    ("""kuyruğaEkle""", """kuyruğaEkle(öge)""", """Kuyruğun SONUNA ekler (ilk giren ilk çıkar).""", """Kuyruk(1, 2).kuyruğaEkle(3).dizine""", """Dizin(1, 2, 3)""", """Kuyruk"""),
    ("""kuyruğaEkleHepsini""", """kuyruğaEkleHepsini(ögeler)""", """Hepsini sırayla kuyruğun sonuna ekler.""", """Kuyruk(1).kuyruğaEkleHepsini(Dizin(2, 3)).dizine""", """Dizin(1, 2, 3)""", """Kuyruk"""),
    ("""baştanÇıkar""", """baştanÇıkar()""", """Kuyruğun BAŞINDAKİ ögeyi alır ve çıkarır. baştanAl ile aynı.""", """{ dez k = Kuyruk(1, 2, 3); (k.baştanÇıkar(), k.dizine) }""", """(1,Dizin(2, 3))""", """Kuyruk"""),
    ("""baştanÇıkarBelki""", """baştanÇıkarBelki""", """baştanÇıkar'ın hata vermeyeni: boş kuyrukta Hiçbiri.""", """Kuyruk.boş[Sayı].baştanÇıkarBelki""", """Hiçbiri""", """Kuyruk"""),
    ("""baştanÇıkarDoğruKaldıkça""", """baştanÇıkarDoğruKaldıkça(deneme)""", """Baştan başlar, koşul bozulunca durur; aldıklarını verir.""", """{ dez k = Kuyruk(1, 2, 5); k.baştanÇıkarDoğruKaldıkça(_ < 3) }""", """Dizin(1, 2)""", """Kuyruk"""),
    ("""baştanÇıkarKoşulla""", """baştanÇıkarKoşulla(deneme)""", """Koşula uyan İLK ögeyi bulup çıkarır (baştaki olmak zorunda değil).""", """{ dez k = Kuyruk(1, 2, 3); k.baştanÇıkarKoşulla(_ > 1) }""", """Biri(2)""", """Kuyruk"""),
    ("""ekle""", """ekle(öge)""", """Kuyruğa/öncelik sırasına öge ekler.""", """Kuyruk(1).ekle(2).dizine""", """Dizin(1, 2)""", """EsnekDizik, EsnekDizim, EsnekYazı, Kuyruk, ÖncelikSırası, İkiUçluKuyruk"""),
    ("""ikizle""", """ikizle()""", """Topluluğun bir kopyasını verir; özgünü bozulmaz.""", """{ dez ö = ÖncelikSırası(1, 5); dez k = ö.ikizle(); k.baştanAl(); ö.boyu }""", """2""", """Kuyruk, ÖncelikSırası, İkiUçluKuyruk"""),
    ("""baştanAl""", """baştanAl()""", """Öncelik sırasında EN İRİ ögeyi alır ve çıkarır.""", """ÖncelikSırası(3, 9, 5).baştanAl()""", """9""", """Kuyruk, ÖncelikSırası, İkiUçluKuyruk"""),
    ("""baştanAlHepsini""", """baştanAlHepsini""", """Hepsini öncelik sırasına göre alır: en iriden en ufağa.""", """ÖncelikSırası(3, 9, 5).baştanAlHepsini""", """Dizin(9, 5, 3)""", """Kuyruk, ÖncelikSırası"""),
    ("""kuyruğa""", """kuyruğa""", """Öncelik sırasını Kuyruk'a çevirir.""", """ÖncelikSırası(3, 9).kuyruğa.boyu""", """2""", """ÖncelikSırası"""),
    ("""yokMu""", """yokMu""", """İçinde bir şey yok mu (Hiçbiri mi).""", """Hiçbiri.yokMu""", """doğru""", """Belki"""),
    ("""boşsaÖbürü""", """boşsaÖbürü(öbürü)""", """Doluysa kendini, boşsa ötekini verir. (İngilizcesi orElse; yoksa bir anahtar sözcük olduğu için bu ad.)""", """Hiçbiri.boşsaÖbürü(Biri(5))""", """Biri(5)""", """Belki, İkisindenBiri"""),
    ("""sola""", """sola(sağdaki)""", """Belki'yi Sol/Sağ ikilisine çevirir: doluysa Sol.""", """Biri(1).sola("yok")""", """Sol(1)""", """Belki"""),
    ("""sağa""", """sağa(soldaki)""", """Belki'yi Sol/Sağ ikilisine çevirir: doluysa Sağ.""", """Biri(1).sağa("yok")""", """Sağ(1)""", """Belki"""),
    ("""büyükHarfe""", """büyükHarfe""", """Bütün harfleri büyütür. Türkçe i/İ kuralına dikkat!""", """"kojo".büyükHarfe""", """KOJO""", """Harf, Yazı"""),
    ("""küçükHarfe""", """küçükHarfe""", """Bütün harfleri küçültür.""", """"KOCO".küçükHarfe""", """koco""", """Harf, Yazı"""),
    ("""kısalt""", """kısalt""", """Baştaki ve sondaki boşlukları atar.""", """"  merhaba  ".kısalt""", """merhaba""", """Yazı"""),
    ("""değiştir""", """değiştir(eski, yeni)""", """Yazıdaki bütün eski parçaları yenisiyle değiştirir.""", """"ali ali".değiştir("ali", "veli")""", """veli veli""", """Aralık, Dizi, Dizik, Dizin, EsnekDizik, Eşlem, Kuyruk, MiskinDizin, Yazı, Yöney, Yığın, İkiUçluKuyruk"""),
    ("""değiştirİlkini""", """değiştirİlkini(kalıp, yeni)""", """Yalnız ilk eşleşmeyi değiştirir (düzenli deyiş kullanır).""", """"ali ali".değiştirİlkini("ali", "veli")""", """veli ali""", """Yazı"""),
    ("""harf""", """harf(sıra)""", """Yazının o sıradaki harfi. 0'dan başlar.""", """"merhaba".harf(0)""", """m""", """EsnekYazı, Yazı"""),
    ("""parçası""", """parçası(nereden, nereye)""", """Yazının bir parçasını keser. nereye dahil değil.""", """"merhaba".parçası(0, 3)""", """mer""", """EsnekYazı, Yazı"""),
    ("""satırlar""", """satırlar""", """Yazıyı satır satır gezdiren yineleyici.""", """"bir\niki".satırlar.dizine""", """Dizin(bir, iki)""", """Yazı"""),
    ("""başındanAt""", """başındanAt(önek)""", """Yazı bu önekle başlıyorsa onu atar.""", """"merhabaDünya".başındanAt("merhaba")""", """Dünya""", """Yazı"""),
    ("""sonundanAt""", """sonundanAt(sonek)""", """Yazı bu sonekle bitiyorsa onu atar.""", """"deneme.txt".sonundanAt(".txt")""", """deneme""", """Yazı"""),
    ("""ilkHarfiBüyült""", """ilkHarfiBüyült""", """Yalnız ilk harfi büyütür.""", """"merhaba".ilkHarfiBüyült""", """Merhaba""", """Yazı"""),
    ("""eşlenirMi""", """eşlenirMi(kalıp)""", """Yazı bu düzenli deyişe uyuyor mu.""", """"abc123".eşlenirMi("[a-z]+[0-9]+")""", """doğru""", """Yazı"""),
    ("""ikiyeAyır""", """ikiyeAyır(deneme)""", """Harfleri denemeye uyanlar ve uymayanlar diye ikiye ayırır.""", """"merhaba".ikiyeAyır(_ == 'a')""", """(aa,merhb)""", """Yazı"""),
    ("""sayıya""", """sayıya""", """Yazıyı Sayı'ya çevirir. Sayı değilse hata verir.""", """"42".sayıya""", """42""", """EsnekYazı, Harf, Yazı"""),
    ("""sayıyaBelki""", """sayıyaBelki""", """sayıya'nın hata vermeyeni: olmuyorsa Hiçbiri.""", """"kırk".sayıyaBelki""", """Hiçbiri""", """Yazı"""),
    ("""kesire""", """kesire""", """Yazıyı Kesir'e (ondalıklı sayı) çevirir.""", """"3.5".kesire""", """3.5""", """EsnekYazı, Harf, Yazı"""),
    ("""kıyasla""", """kıyasla(öbürü)""", """İki yazıyı alfabe sırasına göre kıyaslar: eksi, sıfır ya da artı.""", """"a".kıyasla("b") < 0""", """doğru""", """Yazı"""),
    ("""eşitMiKüçükHarfBüyükHarfAyrımıYapmadan""", """eşitMiKüçükHarfBüyükHarfAyrımıYapmadan(öbürü)""", """Büyük/küçük harfe bakmadan eşit mi.""", """"Koco".eşitMiKüçükHarfBüyükHarfAyrımıYapmadan("kOCO")""", """doğru""", """Yazı"""),
    ("""ilki""", """ilki""", """Aralıkta: başladığı sayı. Kuyrukta: baştaki ögeyi çıkarmadan gösterir.""", """Aralık(1, 10).ilki""", """1""", """Aralık, Kuyruk"""),
    ("""sonuncu""", """sonuncu""", """Aralığın bittiği sayı. Aralık(1, 10)'da 10 dahil DEĞİL.""", """Aralık(1, 10).sonuncu""", """10""", """Aralık"""),
    ("""adımı""", """adımı""", """Aralığın kaçar kaçar gittiği. adım ile aynı.""", """(Aralık.kapalı(1, 10) adım 3).adımı""", """3""", """Aralık"""),
    ("""uzunluğu""", """uzunluğu""", """Aralıkta kaç sayı var.""", """Aralık(1, 10).uzunluğu""", """9""", """Aralık"""),
    ("""içindeMi""", """içindeMi(sayı)""", """Bu sayı aralığın içinde mi.""", """Aralık(1, 10).içindeMi(5)""", """doğru""", """Aralık"""),
    ("""yazı""", """yazı()""", """Aralığı okunur bir yazıya çevirir (uzunsa kısaltır).""", """Aralık.kapalı(1, 3).yazı()""", """Aralık(1, 2, 3)""", """Aralık"""),
    ("""hepsiniHesapla""", """hepsiniHesapla""", """Miskin dizinin bütün ögelerini hesaplatır. Sonsuz dizide ASLA kullanma!""", """MiskinDizin.sayalım(1).al(3).hepsiniHesapla""", """MiskinDizin(1, 2, 3)""", """MiskinDizin"""),
    ("""dahaVarMı""", """dahaVarMı""", """Yineleyicide daha öge kaldı mı.""", """Dizin(1, 2).yineleyici.dahaVarMı""", """doğru""", """Yineleyici"""),
    ("""sıradaki""", """sıradaki""", """Sıradaki ögeyi verir ve yineleyiciyi bir adım ilerletir.""", """{ dez y = Dizin(7, 8).yineleyici; (y.sıradaki, y.sıradaki) }""", """(7,8)""", """Yineleyici"""),
    ("""sıradakiBelki""", """sıradakiBelki""", """sıradaki'nin hata vermeyeni: bitmişse Hiçbiri.""", """Dizin[Sayı]().yineleyici.sıradakiBelki""", """Hiçbiri""", """Yineleyici"""),
    ("""ikizYap""", """ikizYap""", """Aynı ögeleri gezen İKİ yineleyici verir. Bir yineleyici tek kullanımlıktır, iki kez gezmek gerekince bunu kullan.""", """{ dez (a, b) = Dizin(1, 2).yineleyici.ikizYap; (a.dizine, b.dizine) }""", """(Dizin(1, 2),Dizin(1, 2))""", """Yineleyici"""),
    ("""bellekli""", """bellekli""", """Tüketmeden önden bakabilmek için: başı okumak ilerletmez.""", """{ dez b = Dizin(1, 2).yineleyici.bellekli; (b.başı, b.dizine) }""", """(1,Dizin(1, 2))""", """Yineleyici"""),
    ("""gösterdikleriAynıMı""", """gösterdikleriAynıMı(öbürü)""", """İki yineleyici aynı ögeleri aynı sırada mı veriyor.""", """Dizin(1, 2).yineleyici.gösterdikleriAynıMı(Dizin(1, 2))""", """doğru""", """Yineleyici"""),
    ("""yineleyici""", """yineleyici""", """Topluluğu bir kez gezdiren Yineleyici verir.""", """Küme(1).yineleyici.dizine""", """Dizin(1)""", """"""),
    // --- 3. parti: İkisindenBiri (Either) ---
    ("""bölİşle""", """bölİşle(işlev)""", """Her ögeyi Sol/Sağ diye etiketler, sonra ikiye ayırır. ele ile işle'yi tek geçişte yapar.""", """Dizin(1, 2, 3, 4).bölİşle(x => eğer (x % 2 == 0) Sağ(x * 10) yoksa Sol(x))""", """(Dizin(1, 3),Dizin(20, 40))""", """"""),
    ("""solMu""", """solMu""", """İkisindenBiri sol tarafta mı.""", """Sol("hata").solMu""", """doğru""", """İkisindenBiri"""),
    ("""sağMı""", """sağMı""", """İkisindenBiri sağ tarafta mı. Gelenek: sağ, işin yolunda gittiği taraftır.""", """Sağ(5).sağMı""", """doğru""", """İkisindenBiri"""),
    ("""takasla""", """takasla""", """Sol ile sağı yer değiştirir.""", """Sağ(5).takasla""", """Sol(5)""", """İkisindenBiri"""),
    ("""birleştir""", """birleştir""", """İki taraf aynı türdeyse, hangisiyse o değeri verir. (Dizik'te birleştir = uç uca ekle.)""", """Sol("hata").birleştir""", """hata""", """EsnekDizik, İkisindenBiri"""),
    ("""belkiye""", """belkiye""", """Sağdaysa Biri, soldaysa Hiçbiri verir.""", """Sol("hata").belkiye""", """Hiçbiri""", """İkisindenBiri"""),
    ("""koşulla""", """İkisindenBiri.koşulla(koşul, sağdaki, soldaki)""", """Koşul doğruysa Sağ, değilse Sol kurar.""", """İkisindenBiri.koşulla(3 > 2, "oldu", "olmadı")""", """Sağ(oldu)""", """İkisindenBiri"""),
  )

  private def koleksiyonYardımı: Map[String, String] = koleksiyonYöntemleri.map {
    case (ad, imza, açıklama, örnek, sonuç, türler) =>
      // Eskiden bu cümle KOŞULSUZ basılıyordu; tablo yalnız ortak dizi
      // çekirdeğinden ibaretken doğruydu, türe özgü girdiler gelince 113
      // satırda YANLIŞ bilgi verir olmuştu (Sol("hata").birleştir'i okuyup
      // Dizin(1,2).birleştir yazan öğrenci bambaşka bir şey buluyordu).
      val nerede =
        if (türler.isEmpty) "Dizi, Dizin, Yöney, Küme, Kuyruk gibi topluluklarda aynı biçimde çalışır."
        else s"Şu türlerde var: $türler."
      ad -> <div>
        <strong>{ imza }</strong> — { açıklama }<br/>
        <br/><em>Örnek:</em>
        <pre>
{ örnek }
// { sonuç }
        </pre>
        { nerede }
      </div>.toString
  }.toMap
}
