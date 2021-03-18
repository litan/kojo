/*
 * Copyright (C) 2013-2021
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
import net.kogics.kojo.picture
import net.kogics.kojo.xscala.RepeatCommands

object TurkishAPI {
  // some type aliases in Turkish -- Ctrl-t to return type info will also be in turkish
  type Nesne = Object
  type Birim = Unit
  type Her   = Any
  type Hiç   = Nothing

  import net.kogics.kojo.core.Turtle
  import java.awt.{Color, Paint}
  type Renk = Color
  type Boya = Paint

  import net.kogics.kojo.core.{Speed, Slow, Medium, Fast, SuperFast, Point}
  type Hız  = Speed
  type Nokta = Point
  object Nokta {
    def apply(x: Kesir, y: Kesir) = new Point(x, y)
    def unapply(p: Nokta) = Some((p.x, p.y))
  }

  // Ref: https://docs.scala-lang.org/overviews/scala-book/built-in-types.html
  type İkil = Boolean
  val (doğru, yanlış) = (true, false)
  // Bit? Parçacık olsun mu adı?

  // We have Byte/Short/Int/Long which all default to Int and BigInt
  // val n = 1
  // Sayma sayıları
  type Lokma   = Byte
  type Kısa    = Short
  type Sayı    = Int
  type Uzun    = Long
  type İriSayı = BigInt
  // We have Float/Double which default to Double and BigDecimal
  //   val x = 1.0
  // Kesirli sayılar
  type UfakKesir = Float
  type Kesir = Double
  type İriKesir = BigDecimal
  // Yazı
  type Harf = Char
  type Yazı = String

  type Dizin[A] = List[A]
  object Dizin {
    def apply[A](elems: A*): List[A] = List.from(elems)
  }
  val Boş = scala.collection.immutable.Nil

  // Used in Conway's game of life code in the tutorial
  type Sayılar = Vector[Sayı]
  object Sayılar {
    def apply(elemanlar: Sayı*): Sayılar = Vector.from(elemanlar)
  }

  // 
  val (yavaş, orta, hızlı, çokHızlı) = (Slow, Medium, Fast, SuperFast)

  var builtins: net.kogics.kojo.lite.CoreBuiltins = _ //unstable reference to module

  trait TurkishTurtle {
    def englishTurtle: Turtle
    def sil(): Birim = englishTurtle.clear()  // bbx: does this do anything? See sil def below..
    def görünür() = englishTurtle.visible()
    def görünmez() = englishTurtle.invisible()
    def ileri(adım: Kesir) = englishTurtle.forward(adım)
    def ileri() = englishTurtle.forward(25)
    def geri(adım: Kesir) = englishTurtle.back(adım)
    def geri() = englishTurtle.back(25)
    def sağ(açı: Kesir, yarıçap: Kesir) = englishTurtle.right(açı, yarıçap)
    def sağ(açı: Kesir) = englishTurtle.right(açı)
    def sağ() = englishTurtle.right(90)
    def sol(açı: Kesir, yarıçap: Kesir) = englishTurtle.left(açı, yarıçap)
    def sol(açı: Kesir) = englishTurtle.left(açı)
    def sol() = englishTurtle.left(90)
    def atla(x: Kesir, y: Kesir) = englishTurtle.jumpTo(x, y)
    def ilerle(x: Kesir, y: Kesir) = englishTurtle.moveTo(x, y)
    def zıpla(n: Kesir) = {
      englishTurtle.saveStyle() //to preserve pen state
      englishTurtle.hop(n) //hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def zıpla(): Birim = zıpla(25)
    def ev() = englishTurtle.home()
    def noktayaDön(p: Point) = englishTurtle.towards(p)
    def noktayaDön(x: Kesir, y: Kesir) = englishTurtle.towards(x, y)
    def noktayaGit(x: Kesir, y: Kesir) = englishTurtle.lineTo(x, y)
    def noktayaGit(n: Nokta) = englishTurtle.lineTo(n)
    def açıyaDön(açı: Kesir) = englishTurtle.setHeading(açı)
    def doğrultu = englishTurtle.heading
    def doğu() = englishTurtle.setHeading(0)
    def batı() = englishTurtle.setHeading(180)
    def kuzey() = englishTurtle.setHeading(90)
    def güney() = englishTurtle.setHeading(-90)
    def canlandırmaHızınıKur(n: Uzun) = englishTurtle.setAnimationDelay(n)
    def canlandırmaHızı: Uzun = englishTurtle.animationDelay
    def yazı(t: Her) = englishTurtle.write(t)
    // ~/src/kojo/git/kojo/src/main/scala/net/kogics/kojo/turtle/Turtle.scala
    // ../../turtle/Turtle.scala
    def yazıBoyunuKur(boyutKur: Sayı) = englishTurtle.setPenFontSize(boyutKur)
    def yazıYüzünüKur(f: java.awt.Font) = englishTurtle.setPenFont(f)
    def yay(yarıçap: Kesir, açı: Kesir) = englishTurtle.arc(yarıçap, math.round(açı).toInt)
    def dön(açı: Kesir, yarıçap: Kesir) = englishTurtle.turn(açı, yarıçap)
    def dön(açı: Kesir) = englishTurtle.turn(açı)
    def daire(yarıçap: Kesir) = englishTurtle.circle(yarıçap)
    def konumuKur(x: Kesir, y: Kesir) = englishTurtle.setPosition(x, y)
    def konum: Nokta = englishTurtle.position
    def kalemiİndir() = englishTurtle.penDown()
    def kalemiKaldır() = englishTurtle.penUp()
    def kalemİnikMi: İkil = englishTurtle.style.down
    def kalemRenginiKur(renk: Renk) = englishTurtle.setPenColor(renk)
    def boyamaRenginiKur(boya: Boya) = englishTurtle.setFillColor(boya)
    def kalemKalınlığınıKur(n: Kesir) = englishTurtle.setPenThickness(n)
    def biçimleriBelleğeYaz() = englishTurtle.saveStyle()
    def biçimleriGeriYükle() = englishTurtle.restoreStyle()
    def konumVeYönüBelleğeYaz() = englishTurtle.savePosHe()
    def konumVeYönüGeriYükle() = englishTurtle.restorePosHe()
    def ışınlarıAç() = englishTurtle.beamsOn()
    def ışınlarıKapat() = englishTurtle.beamsOff()
    def giysiKur(dostaAdı: Yazı) = englishTurtle.setCostume(dostaAdı)
    def giysileriKur(dostaAdı: Yazı*) = englishTurtle.setCostumes(dostaAdı: _*)
    def birsonrakiGiysi() = englishTurtle.nextCostume()
    def hızıKur(hız: Hız) = englishTurtle.setSpeed(hız)
  }

  class Kaplumbağa(override val englishTurtle: Turtle) extends TurkishTurtle {
    def this(startX: Kesir, startY: Kesir, costumeFileName: Yazı) = this(builtins.TSCanvas.newTurtle(startX, startY, costumeFileName))
    def this(startX: Kesir, startY: Kesir) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0, 0)
    def uzaklık(öbürü: Kaplumbağa) = englishTurtle.distanceTo(öbürü.englishTurtle)
    def çevir(öbürü: Kaplumbağa) = englishTurtle.towards(öbürü.englishTurtle)
    // get f: Turtle => Unit from g: Kaplumbağa => Birim
    val bu = this // Function1 has its own this
    def davran(işlev: Kaplumbağa => Birim) = {
      val f = new Function1[Turtle, Unit] { def apply(t: Turtle) = işlev(bu) }
      englishTurtle.act(f)
    }
    def tepkiVer(işlev: Kaplumbağa => Birim) = {
      val f = new Function1[Turtle, Unit] { def apply(t: Turtle) = işlev(bu) }
      englishTurtle.react(f)
    }
  }
  class Kaplumbağa0(t0: => Turtle) extends TurkishTurtle { //by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object kaplumbağa extends Kaplumbağa0(builtins.TSCanvas.turtle0)
  def sil(): Birim = builtins.TSCanvas.clear()
  def silVeSakla(): Birim = { builtins.TSCanvas.clear(); kaplumbağa.görünmez() } // cleari
  def çizimiSil(): Birim = builtins.TSCanvas.clearStepDrawing()
  def çıktıyıSil(): Birim = builtins.clearOutput()
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
  lazy val gri = builtins.gray
  lazy val koyuGri = builtins.darkGray
  lazy val açıkGri = builtins.lightGray
  lazy val turuncu = builtins.orange
  lazy val morumsu = builtins.magenta
  lazy val camgöbeği = builtins.cyan

  // TODO: other Color* constructors -- and Help Content
  lazy val renkler = builtins.cm
  def Renk(r: Sayı, g: Sayı, b: Sayı, o: Sayı = 255): Renk = new Color(r, g, b, o)
  def arkaplanıKur(r: Renk) = builtins.setBackground(r)
  def arkaplanıKurDik  (r1: Renk, r2: Renk) = builtins.TSCanvas.setBackgroundV(r1, r2)
  def arkaplanıKurYatay(r1: Renk, r2: Renk) = builtins.TSCanvas.setBackgroundH(r1, r2)

  def buAn: Uzun = builtins.epochTimeMillis
  def buSaniye: Kesir = builtins.epochTime

  //  object KcSwe { //Key codes for Swedish keys
  //    lazy val VK_Å = 197
  //    lazy val VK_Ä = 196
  //    lazy val VK_Ö = 214
  //  }

  //loops
  def yinele(n: Sayı)(diziKomut: => Birim): Birim = {
    RepeatCommands.repeat(n) { diziKomut }
  }

  def yineleDizinli(n: Sayı)(diziKomut: Sayı => Birim): Birim = {
    RepeatCommands.repeati(n) { i => diziKomut(i) }
  }

  def yineleDoğruysa(koşul: => İkil)(diziKomut: => Birim): Birim = {
    RepeatCommands.repeatWhile(koşul) { diziKomut }
  }

  def yineleOlanaKadar(koşul: => İkil)(diziKomut: => Birim): Birim = {
    RepeatCommands.repeatUntil(koşul) { diziKomut }
  }

  def yineleKere[T](dizi: Iterable[T])(diziKomut: T => Birim): Birim = {
    RepeatCommands.repeatFor(dizi) { diziKomut }
  }
  def yineleİçin[T](dizi: Iterable[T])(diziKomut: T => Birim): Birim = {
    RepeatCommands.repeatFor(dizi) { diziKomut }
  }

  def yineleİlktenSona(ilk: Sayı, son: Sayı)(diziKomut: Sayı => Birim): Birim = {
    RepeatCommands.repeatFor(ilk to son) { diziKomut }
  }

  //simple IO
  def satıroku(istem: Yazı = "") = builtins.readln(istem)

  def satıryaz(data: Her) = println(data) //Transferred here from sv.tw.kojo.
  def satıryaz() = println()
  def yaz(data: Her) = print(data)

  //math functions
  def yuvarla(sayı: Number, basamaklar: Sayı = 0): Kesir = {
    val faktor = math.pow(10, basamaklar).toDouble
    math.round(sayı.doubleValue * faktor).toLong / faktor
  }
  def rasgele(üstSınır: Sayı) = builtins.random(üstSınır)
  val rastgele = rasgele _
  def rasgeleKesir(üstSınır: Sayı) = builtins.randomDouble(üstSınır)
  val rastgeleKesir = rasgeleKesir _
  def rastgeleSeçim = builtins.randomBoolean
  def rastgeleRenk = builtins.randomColor
  def rastgeleŞeffafRenk = builtins.randomTransparentColor

  val kaplumbağa0 = kaplumbağa
  def yeniKaplumbağa(x: Kesir, y: Kesir) = new Kaplumbağa(x, y)

  lazy val richBuiltins = builtins.asInstanceOf[Builtins]

  def tümEkran() = richBuiltins.toggleFullScreenCanvas()
  object tuvalAlanı {
    def ta = richBuiltins.canvasBounds
    def en = ta.width
    def boy = ta.height
    def x = ta.x
    def y = ta.y
    // todo: more..
  }

  // ../../picture/transforms.scala
  abstract class ComposableTransformer extends Function1[Resim, Resim] { outer =>
    def apply(r: Resim): Resim
    def -> (r: Resim) = apply(r)
    def *(digeri: ComposableTransformer) = new ComposableTransformer {
      def apply(r: Resim): Resim = outer.apply(digeri.apply(r))
    }
  }

  case class Rotc(angle: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Rot(angle)(r.p)) }
  case class Rotpc(angle: Double, x: Double, y: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Rotp(angle, x, y)(r.p)) }
  case class Scalec(factor: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Scale(factor)(r.p)) }
  case class ScaleXYc(x: Double, y: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.ScaleXY(x, y)(r.p)) }
  case class Opacc(f: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Opac(f)(r.p)) }
  case class Huec(f: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Hue(f)(r.p)) }
  case class Satc(f: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Sat(f)(r.p)) }
  case class Britc(f: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Brit(f)(r.p)) }
  case class Transc(x: Double, y: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Trans(x, y)(r.p)) }
  case class Offsetc(x: Double, y: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Offset(x, y)(r.p)) }
  case object FlipYc extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.FlipY(r.p)) }
  case object FlipXc extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.FlipX(r.p)) }
  case object AxesOnc extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.AxesOn(r.p)) }
  case class Fillc(color: Paint) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Fill(color)(r.p)) }
  case class Strokec(color: Paint) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Stroke(color)(r.p)) }
  case class StrokeWidthc(w: Double) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.StrokeWidth(w)(r.p)) }
  case class PreDrawTransformc(fn: richBuiltins.Picture => Unit) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.PreDrawTransform(fn)(r.p)) }
  case class PostDrawTransformc(fn: richBuiltins.Picture => Unit) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.PostDrawTransform(fn)(r.p)) }

  def döndür(açı: Double) = Rotc(açı)
  def döndürMerkez(açı: Double, x: Double, y: Double) = Rotpc(açı, x, y)
  def büyüt(oran: Double) = Scalec(oran)
  def büyütXY(xOranı: Double, yOranı: Double) = ScaleXYc(xOranı, yOranı)
  def saydamlık(oran: Double) = Opacc(oran)
  def ton(t: Double) = Huec(t)
  def parlaklık(p: Double) = Satc(p)
  def aydınlık(a: Double) = Britc(a)
  def götür(x: Double, y: Double) = Transc(x, y)
  def kaydır(x: Double, y: Double) = Offsetc(x, y)
  def yansıtY = FlipYc
  def yansıtX = FlipXc
  def eksenler = AxesOnc
  def boyaRengi(r: Paint) = Fillc(r)
  def kalemRengi(r: Paint) = Strokec(r)
  def kalemBoyu(b: Double) = StrokeWidthc(b)
  def çizimÖncesiİşlev(iv: richBuiltins.Picture => Unit) = PreDrawTransformc(iv)
  def çizimSonraıİşlev(iv: richBuiltins.Picture => Unit) = PostDrawTransformc(iv)

  class Resim(val p: richBuiltins.Picture) {
    def kopya: Resim = new Resim(p.copy)
    val konumuKur = kondur _
    def kondur(x: Kesir, y: Kesir) = p.setPosition(x, y)
    def kalemKalınlığınıKur(kalınlık: Sayı) = p.setPenThickness(kalınlık)
    def kalemRenginiKur = p.setPenColor _
    def alan() = p.area
    // todo: more..
  }
  object Resim {
    def apply(işlev: => Birim): Resim = new Resim(richBuiltins.Picture(işlev))
    def çiz(r: Resim) = richBuiltins.draw(r.p)
    def köşegen(en: Kesir, boy: Kesir) = new Resim(richBuiltins.Picture.line(en, boy))
    def yay(yarıçap: Kesir, açı: Kesir) = new Resim(richBuiltins.Picture.arc(yarıçap, açı))
    def daire(yarıçap: Kesir) = new Resim(richBuiltins.Picture.circle(yarıçap))
    def elips(xYarıçapı: Kesir, yYarıçapı: Kesir) = new Resim(richBuiltins.Picture.ellipse(xYarıçapı, yYarıçapı))
    def elipsDikdörtgenİçinde(en: Kesir, boy: Kesir) = new Resim(richBuiltins.Picture.ellipseInRect(en, boy))
    def yatay(boy: Kesir) = new Resim(richBuiltins.Picture.hline(boy))
    def dikey(boy: Kesir) = new Resim(richBuiltins.Picture.vline(boy))
    def dikdörtgen(en: Kesir, boy: Kesir) = new Resim(richBuiltins.Picture.rect(en, boy))
    def yazı(içerik: Her, yazıBoyu: Sayı) = new Resim(richBuiltins.Picture.text(içerik, yazıBoyu))
    def imge(dosyaAdı: Yazı) = new Resim(richBuiltins.Picture.image(dosyaAdı))
    // Resim.düğme("Merhaba")(println(kg.x))
    def düğme(ad: Yazı)(işlev: => Birim) = new Resim(richBuiltins.Picture.button(ad)(işlev))
    // Resim.arayüz(Label("Merhaba"))
    // Resim.arayüz(Button("Merhaba")(println("Selam!")))
    def arayüz(parça: javax.swing.JComponent) = new Resim(richBuiltins.Picture.widget(parça))
    // todo: more..
  }
  def çiz(r: Resim) = Resim.çiz(r)
  def çizMerkezde(r: Resim) = richBuiltins.drawCentered(r.p)

  def resimDizisi(rd: Resim*) = new Resim(richBuiltins.picStack(rd.toList.map(_.p)))
  def resimDikeyDizi(rd: Resim*) = new Resim(richBuiltins.picCol(rd.toList.map(_.p)))
  def resimYatayDizi(rd: Resim*) = new Resim(richBuiltins.picRow(rd.toList.map(_.p)))
  def resimDüzenliDizi(rd: Resim*) = new Resim(richBuiltins.picStackCentered(rd.toList.map(_.p)))
  def resimDikeyDüzenliDizi(rd: Resim*) = new Resim(richBuiltins.picColCentered(rd.toList.map(_.p)))
  def resimYatayDüzenliDizi(rd: Resim*) = new Resim(richBuiltins.picRowCentered(rd.toList.map(_.p)))

  def gridiGöster() = richBuiltins.tCanvas.gridOn()
  def gridiGizle() = richBuiltins.tCanvas.gridOff()
  def eksenleriGöster() = richBuiltins.tCanvas.axesOn()
  def eksenleriGizle() = richBuiltins.tCanvas.axesOff()

  // todo: help doc
  def artalandaOynat(kod: => Unit) = richBuiltins.runInBackground(kod)
  def fareKonumu = richBuiltins.mousePosition

  // more to come (:-)
}

object TurkishInit {
  def init(builtins: CoreBuiltins): Unit = {
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
    "def" -> "def $adı (${girdiler}){\n    ${cursor}\n})",
    "ileri" -> "ileri()",
    "ileri" -> "ileri(${adım})",
    "geri" -> "geri()",
    "geri" -> "geri(${adım})",
    "sağ" -> "sağ()",
    "sağ" -> "sağ(${açı})",
    "sağ" -> "sağ(${açı},${yarıçap})",
    "sol" -> "sol()",
    "sol" -> "sol(${açı})",
    "sol" -> "sol(${açı},${yarıçap})",
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
    "arkaplanıKurYatay" -> "arkaplanıKurYatay(${renk1},${renk2})",
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
    "rasgele" -> "rasgele(${üstSınır})",
    "rasgeleKesir" -> "rasgeleKesir(${üstSınır})",
    "giysiKur" -> "giysiKur(${dostaAdı})",
    "giysileriKur" -> "giysileriKur(${dostaAdı1},${dostaAdı2})",
    "birsonrakiGiysi" -> "birsonrakiGiysi()",
    "buAn" -> "buAn()",
    "buSaniye" -> "buSaniye()",
    "hızıKur" -> "hızıKur(${hız})"
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
    "arkaplanıKur" -> <div><strong>arkaplanıKur</strong>(renk) - Bu komutla tuval verilen renge boyanır. Kojonun tanıdığı sarı, mavi ve siyah gibi renkleri kullanabilirsiniz ya da <tt>Renk</tt>, <tt>ColorHSB</tt> ve <tt>ColorG</tt> komutlarını kullanarak kendi renklerinizi yaratabilirsiniz. </div>.toString,
    "arkaplanıKurDik" -> <div><strong>arkaplanıKurDik</strong>(renk1, renk2) - Bu komutla tuval aşağıdan yukarı doğru birinci renkten ikinci renge derece derece değişerek boyanır. </div>.toString,
    "arkaplanıKurYatay" -> <div><strong>arkaplanıKurYatay</strong>(renk1, renk2) - Bu komutla tuval soldan sağa doğru birinci renkten ikinci renge derece derece değişerek boyanır. </div>.toString,
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
    "rasgele" -> <div><strong>rasgele</strong>(üstsınır) - Bu komut verilen üst sınırdan küçük rastgele bir doğal sayı verir. Sıfırdan küçük sayılar vermez. </div>.toString,
    "rasgeleKesir" -> <div><strong>rasgeleÇift</strong>() - Bu komut verilen üst sınırdan küçük rastgele bir kesirli sayı (çift çözünürlüklü) verir. Sıfırdan küçük sayılar vermez. </div>.toString,
    "giysiKur" -> <div><strong>giysiKur</strong>(giysiDosyası) - Kaplumbağanın görünüşünü verilen dosyadaki resimle değiştirir. </div>.toString,
    "giysileriKur" -> <div><strong>giysilerKur</strong>(giysiDosyası1, giysiDosyası2, ...) - Kaplumbağa için bir dizi giysi belirler ve giysiDosyası1 resmini giydirir. <tt>birSonrakiGiysi()</tt> komutuyla dizideki bir sonraki giysiyi giydirebiliriz. </div>.toString,
    "birsonrakiGiysi" -> <div><strong>birSonrakiGiysi</strong>() - Kaplumbağaya <tt>giysilerKur()</tt> komutuyla girilen giysi dizisindeki bir sonraki resmi giydirir. </div>.toString,
    "çıktıyıSil" -> <div><strong>çıktıyıSil</strong>() - Bu komut çıktı penceresindeki bütün çıktıları silerek temizler. </div>.toString,
    "silVeSakla" -> <div><strong>silVeSakla</strong>() - Bu komut tuvaldeki çizimleri siler ve kaplumbağayı görünmez kılar. </div>.toString,
    "çizimiSil" -> <div><strong>çizimiSil</strong>() - Bu komut tuvaldeki çizimleri siler. </div>.toString,
    "buAn" -> <div><strong>buAn</strong>() - Bu komut evrensel zamana (UTC) göre 1 Ocak 1970 tam geceyarısından bu ana kadar geçen zamanı kesirsiz milisaniye olarak verir.</div>.toString,
    "buSaniye" -> <div><strong>buSaniye</strong>() - Bu komut evrensel zamana (UTC) göre 1 Ocak 1970 tam geceyarısından bu ana kadar geçen zamanı kesirli saniye olarak verir.</div>.toString,
    "hızıKur" -> <div><strong>hızıKur</strong>(hız) - Kaplumbağacığın hızını belirler. yavaş, orta, hızlı ve çokHızlı değerlerinden birini dene.</div>.toString,
    "def" -> <div><strong>def</strong> - Kıvrık parantez içindeki bir dizi komuta ya da deyişe bir ad takar. Bu yöntemle yeni bir işlev ya da komut tanımlamış olursun. <br/>
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
</div>.toString

  )
}


