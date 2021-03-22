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
import io.github.jdiemke.triangulation.Triangle2D

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
  type Üçgen = Triangle2D

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

  type Dizi[B] = collection.Seq[B]
  object Dizi {
    def apply[B](elems: B*): Seq[B] = Seq.from(elems)
  }
  type Dizin[A] = List[A]
  object Dizin {
    def apply[A](elems: A*): List[A] = List.from(elems)
  }
  val Boş = scala.collection.immutable.Nil
  type MiskinDizin[C] = LazyList[C]
  object MiskinDizin {
    def sayalım(başlangıç: Sayı, kaçarKaçar: Sayı = 1) = LazyList.from(başlangıç, kaçarKaçar)
  }

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
    def göster = görünür _
    def gizle = görünmez _
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
    def noktayaDön(p: Nokta) = englishTurtle.towards(p)
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
    // ../../xscala/help.scala
    // ../../core/TurtleMover.scala
    def konumuDeğiştir(x: Kesir, y: Kesir) = englishTurtle.changePosition(x, y)
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
    def giysiKur(dosyaAdı: Yazı) = englishTurtle.setCostume(dosyaAdı)
    def giysileriKur(dosyaAdı: Yazı*) = englishTurtle.setCostumes(dosyaAdı: _*)
    def birsonrakiGiysi() = englishTurtle.nextCostume()
    def giysiyiBüyült(oran: Kesir) = englishTurtle.scaleCostume(oran)
    def hızıKur(hız: Hız) = englishTurtle.setSpeed(hız)
    def nokta(çap: Sayı): Birim = englishTurtle.dot(çap)
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
    def canlan = tepkiVer _
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
  // ../CoreBuiltins.scala
  lazy val renkler = builtins.cm  // ColorMaker
  lazy val tuşlar = builtins.Kc // Key Codes

  def Renk(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı, saydam: Sayı = 255): Renk = new Color(kırmızı, yeşil, mavi, saydam)
  def Renk(rgbHex: Sayı): Renk = new Color(rgbHex, yanlış)
  def Renk(rgbHex: Sayı, alfaDahilMi: İkil): Renk = new Color(rgbHex, alfaDahilMi)
  def artalanıKur(r: Renk) = builtins.setBackground(r)
  def artalanıKurDik  (r1: Renk, r2: Renk) = builtins.TSCanvas.setBackgroundV(r1, r2)
  def artalanıKurYatay(r1: Renk, r2: Renk) = builtins.TSCanvas.setBackgroundH(r1, r2)

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
  // ../CoreBuiltins.scala
  def rastgele() = math.random()
  def rastgele(üstSınır: Sayı) = builtins.random(üstSınır)
  def rastgele(altSınır: Sayı, üstSınır: Sayı) = builtins.random(altSınır, üstSınır)
  def rastgeleSayı = builtins.randomInt
  def rastgeleUzun = builtins.randomLong
  def rastgeleKesir(üstSınır: Kesir) = builtins.randomDouble(üstSınır)
  def rastgeleKesir(altSınır: Kesir, üstSınır: Kesir) = builtins.randomDouble(altSınır, üstSınır)
  def rastgeleÇanEğrisinden = rastgeleDoğalKesir
  def rastgeleNormalKesir = rastgeleDoğalKesir
  def rastgeleDoğalKesir = builtins.randomNormalDouble
  def rastgeleTohumunuKur(tohum: Uzun = rastgeleUzun) = builtins.initRandomGenerator(tohum)
  def rastgeleİkil = rastgeleSeçim
  def rastgeleSeçim = builtins.randomBoolean
  def rastgeleRenk = builtins.randomColor
  def rastgeleŞeffafRenk = builtins.randomTransparentColor
  def rastgeleDiziden[T](dizi: Dizi[T]) = builtins.randomFrom(dizi)
  def rastgeleDiziden[T](dizi: Dizi[T], ağırlıklar: Dizi[Kesir]) = builtins.randomFrom(dizi, ağırlıklar)
  def durakla(saniye: Kesir) = builtins.pause(saniye)

  def üçgenDöşeme(noktalar: Dizi[Nokta]): Dizi[Üçgen] = builtins.triangulate(noktalar)

  def evDizini = builtins.homeDir
  def buDizin = builtins.currentDir
  def kurulumDizini = builtins.installDir
  def yazıyüzleri = builtins.availableFontNames

  val kaplumbağa0 = kaplumbağa
  def yeniKaplumbağa(x: Kesir, y: Kesir) = new Kaplumbağa(x, y)
  def yeniKaplumbağa(x: Kesir, y: Kesir, giysiDosyası: Yazı) = new Kaplumbağa(x, y, giysiDosyası)

  lazy val richBuiltins = builtins.asInstanceOf[Builtins]

  type ResimDosyası = richBuiltins.Image

  def buradaDur = burdaDur _
  def burdaDur(mesaj: Any) = richBuiltins.breakpoint(mesaj)

  def sayıOku(istem: Yazı = "") = richBuiltins.readInt(istem)
  def kesirOku(istem: Yazı = "") = richBuiltins.readDouble(istem)

  def resimİndir(httpAdresi: Yazı) = richBuiltins.preloadImage(httpAdresi)
  def müzikİndir(httpAdresi: Yazı) = richBuiltins.preloadMp3(httpAdresi)

  def müzikMp3üÇal(mp3dosyası: Yazı) = richBuiltins.playMp3(mp3dosyası)
  def sesMp3üÇal(mp3dosyası: Yazı) = richBuiltins.playMp3Sound(mp3dosyası)
  def müzikMp3üÇalDöngülü(mp3dosyası: Yazı) = richBuiltins.playMp3Loop(mp3dosyası)

  def müzikMp3üÇalıyorMu = richBuiltins.isMp3Playing
  def müzikÇalıyorMu = richBuiltins.isMusicPlaying
  def müzikMp3üKapat() = richBuiltins.stopMp3()
  def müzikMp3DöngüsünüKapat() = richBuiltins.stopMp3Loop()
  def müziğiKapat() = richBuiltins.stopMusic()
  def yeniMp3Çalar = richBuiltins.newMp3Player

  def kojoVarsayılanBakışaçısınıKur() = richBuiltins.switchToDefaultPerspective()
  def kojoVarsayılanİkincıBakışaçısınıKur() = richBuiltins.switchToDefault2Perspective()
  def kojoYazılımcıkBakışaçısınıKur() = richBuiltins.switchToScriptEditingPerspective()
  def kojoÇalışmaSayfalıBakışaçısınıKur() = richBuiltins.switchToWorksheetPerspective()
  def kojoÖyküBakışaçısınıKur() = richBuiltins.switchToStoryViewingPerspective()
  def kojoGeçmişBakışaçısınıKur() = richBuiltins.switchToHistoryBrowsingPerspective()
  def kojoÇıktılıÖyküBakışaçısınıKur() = richBuiltins.switchToOutputStoryViewingPerspective()

  def tümEkranÇıktı() = richBuiltins.toggleFullScreenOutput()
  def tümEkranTuval() = tümEkran()
  def tümEkran() = richBuiltins.toggleFullScreenCanvas()
  object tuvalAlanı {
    def ta = richBuiltins.canvasBounds
    def en = ta.width
    def boy = ta.height
    def x = ta.x
    def y = ta.y
    // todo: more..
  }
  def yatayMerkezKonumu(uzunluk: Kesir): Kesir = tuvalAlanı.x + (tuvalAlanı.en - uzunluk) / 2
  def dikeyMerkezKonumu(uzunluk: Kesir): Kesir = tuvalAlanı.y + (tuvalAlanı.boy - uzunluk) / 2

  // ../../picture/transforms.scala
  abstract class ComposableTransformer extends Function1[Resim, Resim] { outer =>
    def apply(r: Resim): Resim
    def -> (r: Resim) = apply(r)
    def *(digeri: ComposableTransformer) = new ComposableTransformer {
      def apply(r: Resim): Resim = outer.apply(digeri.apply(r))
    }
  }
  // ../../picture/picimage.scala
  abstract class ComposableImageEffect extends ComposableTransformer {
    def epic(r: Resim) = r.p match {
      case ep: picture.EffectablePicture => ep
      case _                             => new picture.EffectableImagePic(r.p)(r.p.canvas)
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
  case class PreDrawTransformc(fn: Resim => Birim) extends ComposableTransformer {
    def apply(r: Resim) = {
      val f2 = new Function1[richBuiltins.Picture, Unit] {
        def apply(p: richBuiltins.Picture): Unit = fn(r)
      }
      new Resim(picture.PreDrawTransform(f2)(r.p))
    }
  }
  case class PostDrawTransformc(fn: Resim => Birim) extends ComposableTransformer {
    def apply(r: Resim) = {
      val f2 = new Function[richBuiltins.Picture, Unit] {
        def apply(p: richBuiltins.Picture): Unit = fn(r)
      }
      new Resim(picture.PostDrawTransform(f2)(r.p))
    }
  }
  // ../../picture/effects.scala
  case class Spinc(n: Int) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Spin(n)(r.p)) }
  case class Reflectc(n: Int) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Reflect(n)(r.p)) }
  // ../../picture/picimage.scala
  case class Fadec(n: Int) extends ComposableImageEffect {
    def apply(r: Resim) = new Resim(picture.Fade(n)(epic(r)))
  }
  case class Blurc(n: Int) extends ComposableImageEffect {
    def apply(r: Resim) = new Resim(picture.Blur(n)(epic(r)))
  }
  case class PointLightc(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) extends ComposableImageEffect {
    def apply(r: Resim) = new Resim(picture.PointLightEffect(x, y, direction, elevation, distance)(epic(r)))
  }
  case class SpotLightc(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) extends ComposableImageEffect {
    def apply(r: Resim) = new Resim(picture.SpotLightEffect(x, y, direction, elevation, distance)(epic(r)))
  }
  case class Lightsc(lights: com.jhlabs.image.LightFilter.Light*) extends ComposableImageEffect {
    def apply(r: Resim) = new Resim(picture.Lights(lights: _*)(epic(r)))
  }
  case class Noisec(amount: Int, density: Double) extends ComposableImageEffect {
    def apply(r: Resim) = new Resim(picture.Noise(amount, density)(epic(r)))
  }
  case class Weavec(xWidth: Double, xGap: Double, yWidth: Double, yGap: Double) extends ComposableImageEffect {
    def apply(r: Resim) = new Resim(picture.Weave(xWidth, xGap, yWidth, yGap)(epic(r)))
  }
  case class SomeEffectc(name: Symbol, props: Tuple2[Symbol, Any]*) extends ComposableImageEffect {
    def apply(r: Resim) = new Resim(picture.SomeEffect(name, props: _*)(epic(r)))
  }
  case class ApplyFilterc(filter: java.awt.image.BufferedImageOp) extends ComposableImageEffect {
    def apply(r: Resim) = new Resim(picture.ApplyFilter(filter)(epic(r)))
  }
  // ../../picture/package.scala
  def döndür(açı: Kesir) = Rotc(açı)
  def döndürMerkezli(açı: Kesir, x: Kesir, y: Kesir) = Rotpc(açı, x, y)
  def büyüt(oran: Kesir) = Scalec(oran)
  def büyüt(xOranı: Kesir, yOranı: Kesir) = büyütXY(xOranı, yOranı)
  def büyütXY(xOranı: Kesir, yOranı: Kesir) = ScaleXYc(xOranı, yOranı)
  def saydamlık(oran: Kesir) = Opacc(oran)
  def ton(t: Kesir) = Huec(t)
  def parlaklık(p: Kesir) = Satc(p)
  def aydınlık(a: Kesir) = Britc(a)
  def götür(x: Kesir, y: Kesir) = Transc(x, y)
  def kaydır(x: Kesir, y: Kesir) = Offsetc(x, y)
  def yansıtY = FlipYc
  def yansıtX = FlipXc
  def eksenler = AxesOnc
  def boyaRengi(r: Boya) = Fillc(r)
  def kalemRengi(r: Boya) = Strokec(r)
  def kalemBoyu(b: Kesir) = StrokeWidthc(b)
  def çizimÖncesiİşlev(iv: Resim => Birim) = PreDrawTransformc(iv)
  def çizimSonrasıİşlev(iv: Resim => Birim) = PostDrawTransformc(iv)
  def çevir(sayı: Sayı) = Spinc(sayı)
  def yansıt(sayı: Sayı) = Reflectc(sayı)
  def soluk(n: Sayı) = Fadec(n)
  def bulanık(n: Sayı) = Blurc(n)
  def noktaIşık(x: Kesir, y: Kesir, yön: Kesir, yükseklik: Kesir, uzaklık: Kesir) = PointLightc(x, y, yön, yükseklik, uzaklık)
  def sahneIşığı(x: Kesir, y: Kesir, yön: Kesir, yükseklik: Kesir, uzaklık: Kesir) = SpotLightc(x, y, yön, yükseklik, uzaklık)
  def ışıklar(ışıklar: com.jhlabs.image.LightFilter.Light*) = Lightsc(ışıklar: _*)
  def birEfekt(isim: Symbol, özellikler: Tuple2[Symbol, Any]*) = SomeEffectc(isim, özellikler: _*)
  def filtre(filtre: java.awt.image.BufferedImageOp) = ApplyFilterc(filtre)
  def gürültü(miktar: Sayı, yoğunluk: Kesir) = Noisec(miktar, yoğunluk)
  def örgü(xBoyu: Kesir, xAra: Kesir, yBoyu: Kesir, yAra: Kesir) = Weavec(xBoyu, xAra, yBoyu, yAra)
  def NoktaIşık(x: Kesir, y: Kesir, yön: Kesir, yükseklik: Kesir, uzaklık: Kesir) = picture.PointLight(x, y, yön, yükseklik, uzaklık)
  def SahneIşığı(x: Kesir, y: Kesir, yön: Kesir, yükseklik: Kesir, uzaklık: Kesir) = picture.SpotLight(x, y, yön, yükseklik, uzaklık)
  // ../../core/Picture.scala
  class Resim(val p: richBuiltins.Picture) {
    def tuval = p.canvas
    def pnode = p.pnode // ??
    def tnode = p.tnode // ??
    def çiz() = p.draw()
    def çizili: İkil = p.isDrawn
    def sınırlar = p.bounds
    def döndür(açı: Kesir): Birim = p.rotate(açı)
    def döndürMerkezli(açı: Kesir, x: Kesir, y: Kesir): Birim = p.rotateAboutPoint(açı, x, y)
    def büyüt(oran: Kesir) = p.scale(oran)
    def büyüt(x: Kesir, y: Kesir) = p.scale(x, y)
    def götür(x: Kesir, y: Kesir) = p.translate(x, y)
    def götür(v: richBuiltins.Vector2D) = p.translate(v.x, v.y)
    def kaydır(x: Kesir, y: Kesir) = p.offset(x, y)
    def kaydır(v: richBuiltins.Vector2D) = p.offset(v.x, v.y)
    def yansıtX() = p.flipX()
    def yansıtY() = p.flipY()
    def saydamlık(oran: Kesir) = p.opacityMod(oran)
    def ton(t: Kesir) = p.hueMod(t)
    def parlaklık(f: Kesir) = p.satMod(f)
    def aydınlık(f: Kesir) = p.britMod(f)
    def benzerDönüşümUygula(bd: java.awt.geom.AffineTransform) = p.transformBy(bd)
    def benzerDönüşümKur(bd: java.awt.geom.AffineTransform) = p.setTransform(bd)
    def bilgiVer() = p.dumpInfo()
    def kopya: Resim = new Resim(p.copy)
    def eksenleriGöster() = p.axesOn()
    def eksenleriGizle() = p.axesOff()
    def sil() = p.erase()
    def göster() = p.visible()
    def gizle() = p.invisible()
    def görünürlüğüTersineÇevir() = p.toggleV()
    def görünür: İkil = p.isVisible
    def kesişir(başkaResim: Resim): İkil = p.intersects(başkaResim.p)
    def çarptıMı = çarpıştı _
    def çarpıştı(başkaResim: Resim): İkil = p.intersects(başkaResim.p)
    def çarpışmalar(başkaları: Set[Resim]): Set[Resim] = 
      başkaları.filter {this çarpıştı _}
    def çarpışma(başkaları: Set[Resim]): Option[Resim] =
      başkaları.find {this çarpıştı _}
    def kesişim(başkaResim: Resim): com.vividsolutions.jts.geom.Geometry =
      p.intersection(başkaResim.p)
    def içinde(başkaResim: Resim) = p.contains(başkaResim.p)
    def uzaklık(başkaResim: Resim) = p.distanceTo(başkaResim.p)
    def alan = p.area
    def çevre = p.perimeter
    def geometri = p.picGeom
    def konum = p.position
    val konumuKur = kondur _
    def kondur(x: Kesir, y: Kesir) = p.setPosition(x, y)
    def doğrultu = p.heading
    val doğrultuyuKur = açıyaDön _
    def açıyaDön(açı: Kesir) = p.setHeading(açı)
    def büyütmeOranı: (Kesir, Kesir) = p.scaleFactor
    def büyütmeOranınıKur(x: Kesir, y: Kesir) = p.setScaleFactor(x, y)
    def büyütmeyiKur(oran: Kesir) = p.setScale(oran)
    def dönüşüm = p.transform
    def kalemRenginiKur = p.setPenColor _
    def kalemKalınlığınıKur(kalınlık: Sayı) = p.setPenThickness(kalınlık)
    def kalemiKapa() = p.setNoPen()
    //def setPenCapJoin(capJoin: (Int, Int)): Unit = setPenCapJoin(capJoin._1, capJoin._2)
    //def setPenCapJoin(cap: Int, join: Int): Unit
    def boyamaRenginiKur(renk: Boya) = p.setFillColor(renk)
    def saydamlık = p.opacity
    def saydamlığıKur(s: Kesir) = p.setOpacity(s)
    def canlan = tepkiVer _
    def tepkiVer(fn: Resim => Birim) = {
      val bu = this
      p.react(new Function1[richBuiltins.Picture, Unit] {
        def apply(p: richBuiltins.Picture) = fn(bu)
      })

    }
    def tepkileriDurdur() = p.stopReactions()
    def dönüştür(fn: Seq[richBuiltins.PolyLine] => Seq[richBuiltins.PolyLine]) = p.morph(fn)
    def yinele(fn: richBuiltins.PolyLine => Unit) = p.foreachPolyLine(fn)
    // ../../picture/pics.scala
    def boşluk(uzunluk: Kesir): Resim = p match {
      case rd: picture.BasePicList => new Resim(rd.withGap(uzunluk))
      case _ => this
    }
    def imge = p.toImage
    def girdiyiAktar(r: Resim) = p.forwardInputTo(r.p)
    def öneAl() = p.moveToFront()
    def ardaAl() = p.moveToBack()
    def arkayaAl() = ardaAl()
    def sonrakiniGöster(ara: Uzun = 100) = p.showNext(ara)
    def güncelle(yeniVeri: Her) = p.update(yeniVeri)
    def çizimiKontrolEt(mesaj: Yazı) = p.checkDraw(mesaj)
    def yana(başka: Resim) = p.beside(başka.p)
    def üste(başka: Resim) = p.above(başka.p)
    def alta(başka: Resim) = p.below(başka.p)
    def konumaDoğruGit(x: Kesir, y: Kesir, süre: Uzun)(sonİşlev: => Birim) =
      p.animateToPosition(x, y, süre)(sonİşlev)
    def mesafeGit(mx: Kesir, my: Kesir, süre: Uzun)(sonİşlev: => Birim) =
      p.animateToPositionDelta(mx, my, süre)(sonİşlev)
    def fareyeTıklayınca(iş: (Kesir, Kesir) => Birim) = p.onMouseClick(iş)
    def fareyiSürükleyince(iş: (Kesir, Kesir) => Birim) = p.onMouseDrag(iş)
    def fareKımıldayınca(iş: (Kesir, Kesir) => Birim) = p.onMouseMove(iş)
    def fareyeBasınca(iş: (Kesir, Kesir) => Birim) = p.onMousePress(iş)
    def fareyiBırakınca(iş: (Kesir, Kesir) => Birim) = p.onMouseRelease(iş)
    def fareGirince(iş: (Kesir, Kesir) => Birim) = p.onMouseEnter(iş)
    def fareÇıkınca(iş: (Kesir, Kesir) => Birim) = p.onMouseExit(iş)
    // todo: more? see: https://docs.kogics.net/reference/picture.html
  }
  // ../../picture/package.scala 
  // ../Builtins.scala  object Picture
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
    def dikdörtgen(en: Kesir, boy: Kesir) = new Resim(richBuiltins.Picture.rect(boy, en)) // they are swapped!
    // ../../Picture/package.scala
    def satır(r: => Resim, kaçTane: Sayı) = new Resim(picture.row(r.p, kaçTane))
    def sütun(r: => Resim, kaçTane: Sayı) = new Resim(picture.col(r.p, kaçTane))
    def yazı(içerik: Her, yazıBoyu: Sayı=15) = new Resim(richBuiltins.Picture.text(içerik, yazıBoyu))
    def yazıRenkli(içerik: Her, yazıBoyu: Sayı, renk: Renk) = new Resim(richBuiltins.Picture.textu(içerik, yazıBoyu, renk))
    def imge(dosyaAdı: Yazı) = new Resim(richBuiltins.Picture.image(dosyaAdı))
    def imge(dosyaAdı: Yazı, zarf: Resim) = new Resim(richBuiltins.Picture.image(dosyaAdı, zarf.p))
    def imge(url: java.net.URL) = new Resim(richBuiltins.Picture.image(url))
    def imge(url: java.net.URL, zarf: Resim) = new Resim(richBuiltins.Picture.image(url, zarf.p))
    def imge(imge: ResimDosyası) = new Resim(richBuiltins.Picture.image(imge))
    def imge(imge: ResimDosyası, zarf: Resim) = new Resim(richBuiltins.Picture.image(imge, zarf.p))
    // Resim.düğme("Merhaba")(println(kg.x))
    def düğme(ad: Yazı)(işlev: => Birim) = new Resim(richBuiltins.Picture.button(ad)(işlev))
    // Resim.arayüz(Label("Merhaba"))
    // Resim.arayüz(Button("Merhaba")(println("Selam!")))
    def arayüz(parça: javax.swing.JComponent) = new Resim(richBuiltins.Picture.widget(parça))
    def yatayBoşluk(en:  Kesir) = new Resim(richBuiltins.Picture.hgap(en))
    def dikeyBoşluk(boy: Kesir) = new Resim(richBuiltins.Picture.vgap(boy))
    def eksenleriGöster(r: Resim) = richBuiltins.Picture.showAxes(r.p)
    def eksenleriGöster(resimler: Resim*) = richBuiltins.Picture.showAxes(resimler.map(_.p): _*)
    def sınırlarıGöster(r: Resim) = richBuiltins.Picture.showBounds(r.p)
    def sınırlarıGöster(resimler: Resim*) = richBuiltins.Picture.showBounds(resimler.map(_.p): _*)
    // todo: more..
    def tuval = tuvalSınırları  // stageBorder
    def tuvalinSınırları = tuvalSınırları
    def tuvalSınırları = new Resim(richBuiltins.tCanvas.stage)
    def tuvalinSolu = new Resim(richBuiltins.tCanvas.stageLeft)
    def tuvalinSağı = new Resim(richBuiltins.tCanvas.stageRight)
    def tuvalinTavanı = new Resim(richBuiltins.tCanvas.stageTop)
    def tuvalinTabanı = new Resim(richBuiltins.tCanvas.stageBot)
    def tuvalBölgesi = new Resim(richBuiltins.tCanvas.stageArea)
  }
  def çiz(r: Resim) = Resim.çiz(r)
  def çizMerkezde(r: Resim) = richBuiltins.drawCentered(r.p)
  def çizSahne(boya: Paint) = richBuiltins.tCanvas.drawStage(boya)
  def çizMerkezdeYazı(mesaj: Yazı, renk: Renk, yazıBoyu: Sayı) = richBuiltins.drawCenteredMessage(mesaj, renk, yazıBoyu)
  def çizVeSakla(resimler: Resim*) = richBuiltins.drawAndHide(resimler.map(_.p): _*)
  def merkezeTaşı(resim: Resim) = richBuiltins.center(resim.p)
  def resimleriSil() = richBuiltins.tCanvas.erasePictures()
  def ekranTazelemeHızınıKur(saniyedeKaçKere: Sayı) = richBuiltins.setRefreshRate(saniyedeKaçKere)
  def ekranTazelemeHızınıGöster(renk: Renk, yazıBoyu: Sayı = 15): Birim = { // richBuiltins.showFps(renk, yazıBoyu)
    val cb = richBuiltins.canvasBounds
    @volatile var frameCnt = 0
    val fpsLabel = richBuiltins.Picture.textu("eth: ", yazıBoyu, renk)
    fpsLabel.setPosition(cb.x + 10, cb.y + cb.height - 10)
    richBuiltins.draw(fpsLabel)
    fpsLabel.forwardInputTo(richBuiltins.TSCanvas.stageArea)

    richBuiltins.TSCanvas.timer(1000) {
      fpsLabel.update(s"eth: $frameCnt")
      frameCnt = 0
    }
    fpsLabel.react { self =>
      frameCnt += 1
    }
  }

  def resimDizisi(rd: Resim*) = new Resim(richBuiltins.picStack(rd.map(_.p)))
  def resimDikeyDizi(rd: Resim*) = new Resim(richBuiltins.picCol(rd.map(_.p)))
  def resimYatayDizi(rd: Resim*) = new Resim(richBuiltins.picRow(rd.map(_.p)))
  def resimDüzenliDizi(rd: Resim*) = new Resim(richBuiltins.picStackCentered(rd.map(_.p)))
  def resimDikeyDüzenliDizi(rd: Resim*) = new Resim(richBuiltins.picColCentered(rd.map(_.p)))
  def resimYatayDüzenliDizi(rd: Resim*) = new Resim(richBuiltins.picRowCentered(rd.map(_.p)))
  def resimKümesi(rd: Resim*) = new Resim(richBuiltins.picBatch(rd.map(_.p)))
  def resimDizisi(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picStack(rd.map(_.p)))
  def resimDikeyDizi(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picCol(rd.map(_.p)))
  def resimYatayDizi(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picRow(rd.map(_.p)))
  def resimDüzenliDizi(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picStackCentered(rd.map(_.p)))
  def resimDikeyDüzenliDizi(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picColCentered(rd.map(_.p)))
  def resimYatayDüzenliDizi(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picRowCentered(rd.map(_.p)))
  def resimKümesi(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picBatch(rd.map(_.p)))

  // ../DrawingCanvasAPI.scala
  def yaklaş(oran: Kesir) = richBuiltins.tCanvas.zoom(oran)
  def yaklaşXY(xOran: Kesir, yOran: Kesir, xMerkez: Kesir, yMerkez: Kesir) =
    richBuiltins.tCanvas.zoomXY(xOran, yOran, xMerkez, yMerkez)
  def yaklaşmayıSil() = richBuiltins.tCanvas.resetPanAndZoom()
  def yaklaşmayaİzinVerme() = richBuiltins.tCanvas.disablePanAndZoom()
  def tuşaBasınca(iş: Sayı => Birim) = richBuiltins.tCanvas.onKeyPress(iş)
  def tuşuBırakınca(iş: Sayı => Birim) = richBuiltins.tCanvas.onKeyRelease(iş)
  def fareyeTıklıyınca(iş: (Kesir, Kesir) => Birim) = richBuiltins.tCanvas.onMouseClick(iş)
  def fareyiSürükleyince(iş: (Kesir, Kesir) => Birim) = richBuiltins.tCanvas.onMouseDrag(iş)
  def fareKımıldayınca(iş: (Kesir, Kesir) => Birim) = richBuiltins.tCanvas.onMouseMove(iş)

  def gridiGöster() = richBuiltins.tCanvas.gridOn()
  def gridiGizle() = richBuiltins.tCanvas.gridOff()
  def eksenleriGöster() = richBuiltins.tCanvas.axesOn()
  def eksenleriGizle() = richBuiltins.tCanvas.axesOff()
  def açıÖlçeriGöster():richBuiltins.Picture = açıÖlçeriGöster(-tuvalAlanı.en/2, -tuvalAlanı.boy/2)
  def açıÖlçeriGöster(x: Kesir, y: Kesir): richBuiltins.Picture = richBuiltins.tCanvas.showProtractor(x, y)
  def açıÖlçeriGizle() = richBuiltins.tCanvas.hideProtractor()
  def cetveliGöster():richBuiltins.Picture = cetveliGöster(-tuvalAlanı.en/2, tuvalAlanı.boy/2)
  def cetveliGöster(x: Kesir, y: Kesir):richBuiltins.Picture = richBuiltins.tCanvas.showScale(x, y)

  def çizimiKaydet(dosyaAdı: Yazı) = richBuiltins.tCanvas.exportImage(dosyaAdı)
  def çizimiKaydet(dosyaAdı: Yazı, en: Sayı, boy: Sayı) = richBuiltins.tCanvas.exportImage(dosyaAdı, en, boy)
  def çizimiKaydetBoy(dosyaAdı: Yazı, boy: Sayı) = richBuiltins.tCanvas.exportImageH(dosyaAdı, boy)
  def çizimiKaydetEn(dosyaAdı: Yazı, en: Sayı) = richBuiltins.tCanvas.exportImageW(dosyaAdı, en)
  def çizimiPulBoyundaKaydet(dosyaAdı: Yazı, boy: Sayı) = richBuiltins.tCanvas.exportThumbnail(dosyaAdı, boy)

  // todo: help doc
  def artalandaOynat(kod: => Unit) = richBuiltins.runInBackground(kod)
  def fareKonumu = richBuiltins.mousePosition
  def yorumla(komutDizisi: Yazı) = richBuiltins.interpret(komutDizisi)
  def yineleSayaçla(miliSaniye: Uzun)(işlev: => Birim) = richBuiltins.tCanvas.timer(miliSaniye)(işlev)
  def canlandır(işlev: => Birim) = richBuiltins.tCanvas.animate(işlev)
  def durdur() = richBuiltins.stopAnimation()
  def canlandırmaBaşlayınca(işlev: => Birim) = richBuiltins.tCanvas.onAnimationStart(işlev)
  def canlandırmaBitince(işlev: => Birim) = richBuiltins.tCanvas.onAnimationStop(işlev)
  def tuvaliEtkinleştir() = richBuiltins.activateCanvas()
  def yazılımcıkDüzenleyicisiniEtkinleştir() = richBuiltins.activateEditor()
  def sahneKenarındanYansıtma(r: Resim, yöney: richBuiltins.Vector2D): richBuiltins.Vector2D =
    richBuiltins.bouncePicOffStage(r.p, yöney)
  def engeldenYansıtma(r: Resim, yöney: richBuiltins.Vector2D, engel: Resim): richBuiltins.Vector2D =
    richBuiltins.bouncePicOffPic(r.p, yöney, engel.p)
  def tuşaBasılıMı(tuş: Sayı) = richBuiltins.isKeyPressed(tuş)

  def çıktıArtalanınıKur(renk: Renk) = richBuiltins.setOutputBackground(renk)
  def çıktıYazıRenginiKur(renk: Renk) = richBuiltins.setOutputTextColor(renk)
  def çıktıYazıYüzüBoyunuKur(boy: Sayı) = richBuiltins.setOutputTextFontSize(boy)
  def tuvalBoyutOranınınKur(oran: Kesir) = richBuiltins.setDrawingCanvasAspectRatio(oran)
  def tuvalBoyutlarınıKur(en: Sayı, boy: Sayı) = richBuiltins.setDrawingCanvasSize(en, boy)

  def resmiSüz(r: Resim, süzgeç: java.awt.image.BufferedImageOp): Resim = new Resim(richBuiltins.filterPicture(r.p, süzgeç))
  def resimDosyasınıSüz(rd: java.awt.image.BufferedImage, süzgeç: java.awt.image.BufferedImageOp) = richBuiltins.filterImage(rd, süzgeç)

  def süreTut(komut: => Birim): Birim = {
    val t0 = buSaniye
    komut
    val delta = buSaniye - t0
    println("Komudun çalışması $delta%.3f saniye sürdü.")
  }

  def oyunSüresiniGöster(süreSaniyeOlarak: Sayı, mesaj: Yazı, renk: Renk = siyah, yazıBoyu: Sayı = 15) =
    richBuiltins.showGameTime(süreSaniyeOlarak, mesaj, renk, yazıBoyu)

  def sırayaSok(kaçSaniyeSonra: Kesir)(komut: => Birim) = richBuiltins.schedule(kaçSaniyeSonra)(komut)
  def sırayaSok(n: Sayı, kaçSaniyeSonra: Kesir)(komut: => Birim) = richBuiltins.scheduleN(n, kaçSaniyeSonra)(komut)

  /* ../../widget/swingwrappers.scala
   Some are used in addition*.scala sample and others:
   RowPanel
   ColPanel
   TextField
   TextArea
   Label
   Button
   ToggleButton
   DropDown
   Slider
   */

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
    "artalanıKur" -> "artalanıKur(${renk})",
    "artalanıKurDik" -> "artalanıKurDik(${renk1},${renk2})",
    "artalanıKurYatay" -> "artalanıKurYatay(${renk1},${renk2})",
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
