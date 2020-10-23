/*
 * Copyright (C) 2013
 *   Bjorn Regnell <bjorn.regnell@cs.lth.se>,
 *   Lalit Pant <pant.lalit@gmail.com>
 *   Bulent Basaran <bulent2k2@gmail.com>
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

// Turkish Turtle wrapper for Kojo

package net.kogics.kojo.lite.i18n

import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.xscala.RepeatCommands

object TurkishAPI {
  import net.kogics.kojo.core.Turtle
  import java.awt.Color
  var builtins: net.kogics.kojo.lite.CoreBuiltins = _ //unstable reference to module

  trait TurkishTurtle {
    def englishTurtle: Turtle
    def sil() = englishTurtle.clear()
    def görünür() = englishTurtle.visible()
    def görünmez() = englishTurtle.invisible()
    def ileri(adım: Double) = englishTurtle.forward(adım)
    def ileri() = englishTurtle.forward(25)
    def sağ(açı: Double) = englishTurtle.right(açı)
    def sağ() = englishTurtle.right(90)
    def sol(açı: Double) = englishTurtle.left(açı)
    def sol() = englishTurtle.left(90)
    def atla(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    def ilerle(x: Double, y: Double) = englishTurtle.moveTo(x, y)
    def zıpla(n: Double) = {
      englishTurtle.saveStyle() //to preserve pen state
      englishTurtle.hop(n) //hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def zıpla(): Unit = zıpla(25)
    def ev() = englishTurtle.home()
    def noktayaDön(x: Double, y: Double) = englishTurtle.towards(x, y)
    def açıyaDön(açı: Double) = englishTurtle.setHeading(açı)
    def doğrultu = englishTurtle.heading
    def doğu() = englishTurtle.setHeading(0)
    def batı() = englishTurtle.setHeading(180)
    def kuzey() = englishTurtle.setHeading(90)
    def güney() = englishTurtle.setHeading(-90)
    def canlandırmaHızınıKur(n: Long) = englishTurtle.setAnimationDelay(n)
    def yazı(t: Any) = englishTurtle.write(t)
    def yazıBoyunuKur(boyutKur: Int) = englishTurtle.setPenFontSize(boyutKur)
    def yay(yarıçap: Double, açı: Double) = englishTurtle.arc(yarıçap, math.round(açı).toInt)
    def daire(yarıçap: Double) = englishTurtle.circle(yarıçap)
    def konum = englishTurtle.position
    def kalemiİndir() = englishTurtle.penDown()
    def kalemiKaldır() = englishTurtle.penUp()
    def kalemİnikMi = englishTurtle.style.down
    def kalemRenginiKur(renk: java.awt.Color) = englishTurtle.setPenColor(renk)
    def boyamaRenginiKur(renk: java.awt.Color) = englishTurtle.setFillColor(renk)
    def kalemKalınlığınıKur(n: Double) = englishTurtle.setPenThickness(n)
    def biçimleriBelleğeYaz() = englishTurtle.saveStyle()
    def biçimleriGeriYükle() = englishTurtle.restoreStyle()
    def konumVeYönüBelleğeYaz() = englishTurtle.savePosHe()
    def konumVeYönüGeriYükle() = englishTurtle.restorePosHe()
    def ışınlarıAç() = englishTurtle.beamsOn()
    def ışınlarıKapat() = englishTurtle.beamsOff()
    def giysiKur(dostaAdı: String) = englishTurtle.setCostume(dostaAdı)
    def giysileriKur(dostaAdı: String*) = englishTurtle.setCostumes(dostaAdı: _*)
    def birsonrakiGiysi() = englishTurtle.nextCostume()
  }
  class Kaplumbağa(override val englishTurtle: Turtle) extends TurkishTurtle {
    def this(startX: Double, startY: Double, costumeFileName: String) = this(builtins.TSCanvas.newTurtle(startX, startY, costumeFileName))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0, 0)
  }
  class Kaplumbağa0(t0: => Turtle) extends TurkishTurtle { //by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object kaplumbağa extends Kaplumbağa0(builtins.TSCanvas.turtle0)
  def sil() = builtins.TSCanvas.clear()
  def çıktıyıSil() = builtins.clearOutput()
  lazy val mavi = builtins.blue
  lazy val kırmızı = builtins.red
  lazy val sarı = builtins.yellow
  lazy val yeşil = builtins.green
  lazy val mor = builtins.purple
  lazy val pembe = builtins.pink
  lazy val kahverengi = builtins.brown
  lazy val siyah = builtins.black
  lazy val beyaz = builtins.white
  lazy val renksiz = builtins.noColor
  def arkaplanıKur(c: Color) = builtins.setBackground(c)
  def arkaplanıKurDik(c1: Color, c2: Color) = builtins.TSCanvas.setBackgroundV(c1, c2)

  //  object KcSwe { //Key codes for Swedish keys
  //    lazy val VK_Å = 197
  //    lazy val VK_Ä = 196
  //    lazy val VK_Ö = 214
  //  }

  //loops
  def yinele(n: Int)(block: => Unit) {
    RepeatCommands.repeat(n) { block }
  }

  def yineleDizinli(n: Int)(block: Int => Unit) {
    RepeatCommands.repeati(n) { i => block(i) }
  }

  def yineleDogruysa(koşul: => Boolean)(block: => Unit) {
    RepeatCommands.repeatWhile(koşul) { block }
  }

  def yineleKere[T](dizi: Iterable[T])(block: T => Unit) {
    RepeatCommands.repeatFor(dizi) { block }
  }

  //simple IO
  def satıroku(istem: String = "") = builtins.readln(istem)

  def satıryaz(data: Any) = println(data) //Transferred here from sv.tw.kojo.
  def satıryaz() = println()

  //math functions
  def yuvarla(sayı: Number, basamaklar: Int = 0): Double = {
    val faktor = math.pow(10, basamaklar).toDouble
    math.round(sayı.doubleValue * faktor).toLong / faktor
  }
  def rasgele(üstSınır: Int) = builtins.random(üstSınır)
  def rasgeleÇift(üstSınır: Int) = builtins.randomDouble(üstSınır)

  //some type aliases in Swedish
  type Doğal = Int
  type Çift = Double
  type Dizi = String
}

object TurkishInit {
  def init(builtins: CoreBuiltins) {
    //initialize unstable value
    TurkishAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Kaplumbağalı Kojo'ya Hoşgeldin!")
        if (b.isScratchPad) {
          println("Kojo Deneme Tahtasını kapatınca geçmiş silinir.")
        }

        //        b.setEditorTabSize(2)

        //code completion
        b.addCodeTemplates(
          "tr",
          codeTemplates
        )
        //help texts
        b.addHelpContent(
          "tr",
          helpContent
        )

      case _ =>
    }
  }

  val codeTemplates = Map(
    "ileri" -> "ileri(${adım})",
    "sağ" -> "sağ(${açı})",
    "sol" -> "sol(${açı})",
    "atla" -> "atla(${x},${y})",
    "ilerle" -> "ilerle(${x},${y})",
    "zıpla" -> "zıpla(${adım})",
    "ev" -> "ev()",
    "noktayaDön" -> "noktayaDön(${x},${y})",
    "açıyaDön" -> "açıyaDön(${açı})",
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
    "arkaplanıKur" -> "arkaplanıKur(${renk})",
    "arkaplanıKurDik" -> "arkaplanıKurDik(${renk1},${renk2})",
    "yinele" -> "yinele(${say}) {\n    ${cursor}\n}",
    "yineleDizinli" -> "yineleDizinli(${say}) { i =>\n    ${cursor}\n}",
    "yineleDogruysa" -> "yineleDogruysa(${koşul}) {\n    ${cursor}\n}",
    "yineleKere" -> "yineleKere(${dizi}) { ${e} =>\n    ${cursor}\n}",
    "satıryaz" -> "satıryaz(${yazı})",
    "satıroku" -> "satıroku(${istem})",
    "yuvarla" -> "yuvarla(${sayı},${basamaklar})",
    "rasgele" -> "rasgele(${üstSınır})",
    "rasgeleÇift" -> "rasgeleÇift(${üstSınır})",
    "giysiKur" -> "giysiKur(${dostaAdı})",
    "giysileriKur" -> "giysileriKur(${dostaAdı1},${dostaAdı2})",
    "birsonrakiGiysi" -> "birsonrakiGiysi()"
  )

  val helpContent = Map(
    "ileri" -> <div><strong>ileri</strong>(adımSayısı) - Bu komut kaplumbağaya verilen sayı kadar adım attırır ve baktığı doğrultuda ilerletir. </div>.toString,
    "sol" -> <div> <strong>sol</strong>() - Bu komut kaplumbağayı olduğu yerde sola doğru (saat yönünün tersine doğru) 90 derece döndürür. <br/> <strong>sol</strong>(derece) - Bu komut kaplumbağayı olduğu yerde sola doğru (saat yönünün tersine) verilen derece kadar döndürür. <br/> <strong>sol</strong>(derece, yarıçap) - Bu komut kaplumbağayı verilen yarıçaplı bir yay üzerinde sola doğru (saat yönünün tersine doğru) verilen derece kadar döndürerek ilerletir. <br/> </div>.toString,
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
    "yazı" -> <div><strong>yaz</strong>(nesne) - Bu komut kaplumbağanın durduğu yere verilen nesnenin yazı olarak karşılığını yazar. </div>.toString,
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
    "arkaplanıKur" -> <div> <strong>arkaplanıKur</strong>(renk) - Bu komutla tuval verilen renge boyanır. Kojo'nun bildiği sarı, mavi ve siyah gibi renkleri kullanabilirsiniz ya da <tt>Color</tt>, <tt>ColorHSB</tt> ve <tt>ColorG</tt> komutlarını kullanarak kendi renklerinizi yaratabilirsiniz. </div> .toString,
    "arkaplanıKurDik" -> <div><strong>arkaplanıKurDik</strong>(renk1, renk2) - Bu komutla tuval aşağıdan yukarı doğru birinci renkten ikinci renge derece derece değişerek boyanır. </div>.toString,
    "yinele" -> <div><strong>yinele</strong>(sayı){{ }} - Bu komut küme içine alınan komutları verilen sayı kadar tekrar tekrar çağırır. <br/></div>.toString,
    "yineleDizinli" -> <div><strong>yineleDizinli</strong>(sayı) {{i => }} - Bu komut, küme içine alılan komutları verilen sayı kadar tekrar tekrar çağırır. Kaçıncı yineleme olduğunu <tt>i</tt> değişkenini küme içinde kullanarak görebiliriz. </div>.toString,
    "yineleDogruysa" -> <div><strong>yineleDogruysa</strong>(koşul) {{ }} - Bu komut küme içine alılan komutları verilen koşul doğru oldukça tekrar çağırır. <br/></div>.toString,
    "yineleKere" -> <div><strong>yineleKere</strong>(dizi){{ }} - Bu komut küme içine alılan komutları verilen dizideki her eleman için birer kere çağırır. <br/></div>.toString,
    "satıryaz" -> <div><strong>satıryaz</strong>(obj) - Bu komut verilen nesneyi harf dizisi olarak çıktı penceresine yazar ve yeni satıra geçer. </div>.toString,
    "satıroku" -> <div><strong>satıroku</strong>(istemDizisi) - Bu komut verilen istem dizisini çıktı penceresine yazar ve arkasından sizin yazdığınız bir satırı okur. </div>.toString,
    "yuvarla" -> <div><strong>yuvarla</strong>(sayı, basamak) - Bu komut verilen sayıyı noktadan sonra verilen basamak sayısına kadar yuvarlar. </div>.toString,
    "rasgele" -> <div><strong>rasgele</strong>(üstsınır) - Bu komut verilen üst sınırdan küçük rastgele bir doğal sayı verir. Sıfırdan küçük sayılar vermez. </div>.toString,
    "rasgeleÇift" -> <div><strong>rasgeleÇift</strong>() - Bu komut verilen üst sınırdan küçük rastgele bir kesirli sayı (çift çözünürlüklü) verir. Sıfırdan küçük sayılar vermez. </div>.toString,
    "giysiKur" -> <div><strong>giysiKur</strong>(giysiDosyası) - Kaplumbağanın görünüşünü verilen dosyadaki resimle değiştirir. </div>.toString,
    "giysileriKur" -> <div><strong>giysilerKur</strong>(giysiDosyası1, giysiDosyası2, ...) - Kaplumbağa için bir dizi giysi belirler ve giysiDosyası1 resmini giydirir. <tt>birSonrakiGiysi()</tt> komutuyla dizideki bir sonraki giysiyi giydirebiliriz. </div>.toString,
    "birsonrakiGiysi" -> <div><strong>birSonrakiGiysi</strong>() - Kaplumbağaya <tt>giysilerKur()</tt> komutuyla girilen giysi dizisindeki bir sonraki resmi giydirir. </div>.toString
  )
}
