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

import java.awt.Color
import java.util.concurrent.Future // todo

import edu.umd.cs.piccolo.activities.PActivity // todo
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.kmath.KEasing
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.xscala.RepeatCommands

// Keep in alphabetical order
object TurkishAPI
    extends tr.ArrayMethodsInTurkish
    with tr.CalendarAndTimeUtilsInTurkish
    with tr.CharMethodsInTurkish
    with tr.ColorMethodsInTurkish
    with tr.CoreTypeMethodsInTurkish
    with tr.GeoMethodsInTurkish
    with tr.LazyListMethodsInTurkish
    with tr.ListMethodsInTurkish
    with tr.MapMethodsInTurkish
    with tr.MathMethodsInTurkish
    with tr.NumMethodsInTurkish
    with tr.OptionMethodsInTurkish
    with tr.PartialFunctionMethodsInTurkish
    with tr.QueueMethodsInTurkish
    with tr.RangeMethodsInTurkish
    with tr.SeqMethodsInTurkish
    with tr.SetMethodsInTurkish
    with tr.StringMethodsInTurkish
    with tr.arayuz.SwingWidgetMethodsInTurkish
    with tr.VectorMethodsInTurkish {

  var builtins: CoreBuiltins = _ // unstable reference to module
  lazy val richBuiltins = builtins.asInstanceOf[Builtins]

  import net.kogics.kojo.lite.i18n.tr // todo: better here than at the top scope?
  // Imports are intransitive (not visibly transitive). So, we "export" the ones that should be on the interface
  // todo: metaprog on these?
  type Nesne = tr.Nesne
  type Birim = tr.Birim
  type Her = tr.Her
  type HerDeğer = tr.HerDeğer
  type HerGönder = tr.HerGönder
  type Yok = tr.Yok
  val yok: Yok = tr.yok
  type Hiç = tr.Hiç
  type Boya = tr.Boya
  type Hız = tr.Hız
  type Nokta = tr.Nokta
  type Dikdörtgen = tr.Dikdörtgen
  type Üçgen = tr.Üçgen
  type İkil = tr.İkil
  type Seçim = tr.Seçim

  type KuralDışı = Exception
  type ÇalışmaSırasıKuralDışı = RuntimeException

  type Diz[T] = tr.Diz[T]
  type Dizi[B] = tr.Dizi[B]
  type Dizin[A] = tr.Dizin[A]
  type Dizim[T] = tr.Dizim[T]
  type EsnekDizim[T] = tr.EsnekDizim[T]
  val Dizim = tr.Dizim
  val EsnekDizim = tr.EsnekDizim

  type UzunlukBirimi = tr.UzunlukBirimi

  type GeoYol = tr.GeoYol
  type GeoNokta = tr.GeoNokta
  type Grafik2B = tr.Grafik2B

  type Aralık = tr.Aralık
  val Aralık = tr.Aralık
  type Yığın[T] = tr.Yığın[T]
  val Yığın = tr.Yığın
  type Eşlem[A, D] = tr.Eşlem[A, D]
  val Eşlem = tr.Eşlem

  // use tuples, case classes or other structure types when more args seem to be needed
  type İşlev1[D, R] = tr.İşlev1[D, R]
  type İşlev2[D1, D2, R] = tr.İşlev2[D1, D2, R]
  type İşlev3[D1, D2, D3, R] = tr.İşlev3[D1, D2, D3, R]

  val (doğru, yanlış, yavaş, orta, hızlı, çokHızlı, noktaSayısı, santim, inç) =
    (tr.doğru, tr.yanlış, tr.yavaş, tr.orta, tr.hızlı, tr.çokHızlı, tr.noktaSayısı, tr.santim, tr.inç)

  // todo
  // val Harf = tr.Harf
  val Nokta = tr.Nokta

  type ÇiniDünyası = tr.ÇiniDünyası
  val ÇiniDünyası = tr.ÇiniDünyası
  type ÇiniXY = ÇiniDünyası.ÇiniXY
  val ÇiniXY = ÇiniDünyası.ÇiniXY
  type BirSayfaKostüm = tr.BirSayfaKostüm
  val BirSayfaKostüm = tr.BirSayfaKostüm

  def belirt(belit: İkil, mesaj: => Any): Birim = assert(belit, mesaj)
  def gerekli(gerekçe: İkil, mesaj: => Any): Birim = require(gerekçe, mesaj)
  def yeniMp3Çalar = new tr.Mp3Çalar(richBuiltins.newMp3Player)

  trait TurkishTurtle {
    def englishTurtle: Turtle
    def sil(): Birim = englishTurtle.clear() // bbx: does this do anything? See sil def below..
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
      englishTurtle.saveStyle() // to preserve pen state
      englishTurtle.hop(n) // hop change state to penDown after hop
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
    // yaz overlaps with (satır)yaz
    def tuvaleYaz(t: Her) = yazı(t)
    def yazı(t: Her) = englishTurtle.write(t)
    // ~/src/kojo/git/kojo/src/main/scala/net/kogics/kojo/turtle/Turtle.scala
    // ../../turtle/Turtle.scala
    def yazıBoyunuKur(boyutKur: Sayı) = englishTurtle.setPenFontSize(boyutKur)
    def yazıYüzünüKur(yy: Yazıyüzü) = englishTurtle.setPenFont(yy)
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
    def this(startX: Kesir, startY: Kesir, costumeFileName: Yazı) =
      this(builtins.TSCanvas.newTurtle(startX, startY, costumeFileName))
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
  class Kaplumbağa0(t0: => Turtle) extends TurkishTurtle { // by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object kaplumbağa extends Kaplumbağa0(builtins.TSCanvas.turtle0)
  def sil(): Birim = builtins.TSCanvas.clear()
  def silVeSakla(): Birim = { builtins.TSCanvas.clear(); kaplumbağa.görünmez() } // cleari
  def çizimiSil(): Birim = builtins.TSCanvas.clearStepDrawing()
  def çıktıyıSil(): Birim = builtins.clearOutput()
  def silVeÇizimBiriminiKur(ub: UzunlukBirimi) = builtins.TSCanvas.clearWithUL(ub)
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
  lazy val renkler = builtins.cm // ColorMaker
  lazy val tuşlar = builtins.Kc // Key Codes

  def artalanıKur(r: Renk) = builtins.setBackground(r)
  def artalanıKur(b: Boya) = builtins.setBackground(b)
  def artalanıKurDik(r1: Renk, r2: Renk) = builtins.TSCanvas.setBackgroundV(r1, r2)
  def artalanıKurYatay(r1: Renk, r2: Renk) = builtins.TSCanvas.setBackgroundH(r1, r2)

  //  object KcSwe { //Key codes for Swedish keys
  //    lazy val VK_Å = 197
  //    lazy val VK_Ä = 196
  //    lazy val VK_Ö = 214
  //  }

  // loops
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

  // simple IO
  def satıroku(istem: Yazı = "") = builtins.readln(istem)
  def satıryaz(data: Her) = println(data) // Transferred here from sv.tw.kojo.
  def satıryaz() = println()
  def yaz(data: Her) = print(data)

  // ../CoreBuiltins.scala
  // bir de rasgele: Kesir var matematik trait'inden geliyor
  def rastgele(üstSınır: Sayı): Sayı = builtins.random(üstSınır)
  def rastgele(altSınır: Sayı, üstSınır: Sayı): Sayı = builtins.random(altSınır, üstSınır)
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
  def rastgeleRenk: Renk = builtins.randomColor
  def rastgeleŞeffafRenk: Renk = builtins.randomTransparentColor
  def rastgeleDiziden[T](dizi: Dizi[T]) = builtins.randomFrom(dizi)
  def rastgeleDiziden[T](dizi: Dizi[T], ağırlıklar: Dizi[Kesir]) = builtins.randomFrom(dizi, ağırlıklar)
  // def diziKarıştır[T](xs: Dizi[T]): Dizi[T] = util.Random.shuffle(xs)
  def rastgeleKarıştır[T, C](xs: IterableOnce[T])(implicit bf: collection.BuildFrom[xs.type, T, C]): C =
    util.Random.shuffle(xs)

  def durakla(saniye: Kesir) = builtins.pause(saniye)

  def üçgenDöşeme(noktalar: Dizi[Nokta]): Diz[Üçgen] = builtins.triangulate(noktalar)

  // todo: klasör?
  def evDizini = builtins.homeDir
  def buDizin = builtins.currentDir
  def kurulumDizini = builtins.installDir
  def yazıyüzleri = builtins.availableFontNames
  def yazıyüzü(adı: Yazı, boyu: Sayı): Yazıyüzü = builtins.Font(adı, boyu)
  def yazıyüzü(adı: Yazı, boyu: Sayı, biçem: Sayı): Yazıyüzü = builtins.Font(adı, biçem, boyu)
  def yazıÇerçevesi(yazı: Yazı, yazıBoyu: Sayı, yazıyüzüAdı: Yazı = yok): Dikdörtgen =
    builtins.textExtent(yazı, yazıBoyu, yazıyüzüAdı)

  val kaplumbağa0 = kaplumbağa
  def yeniKaplumbağa(x: Kesir, y: Kesir) = new Kaplumbağa(x, y)
  def yeniKaplumbağa(x: Kesir, y: Kesir, giysiDosyası: Yazı) = new Kaplumbağa(x, y, giysiDosyası)

  def buradaDur = burdaDur _
  def burdaDur(mesaj: Any) = richBuiltins.breakpoint(mesaj)

  def sayıOku(istem: Yazı = "") = richBuiltins.readInt(istem)
  def kesirOku(istem: Yazı = "") = richBuiltins.readDouble(istem)

  def resimİndir(httpAdresi: Yazı) = richBuiltins.preloadImage(httpAdresi)
  def müzikİndir(httpAdresi: Yazı) = richBuiltins.preloadMp3(httpAdresi)

  def müzikMp3üÇal(mp3dosyası: Yazı) = richBuiltins.playMp3(mp3dosyası)
  def sesMp3üÇal(mp3dosyası: Yazı) = richBuiltins.playMp3Sound(mp3dosyası)
  def müzikMp3üÇalDöngülü(mp3dosyası: Yazı) = richBuiltins.playMp3Loop(mp3dosyası)

  def Mp3ÇalıyorMu = müzikMp3üÇalıyorMu
  def Mp3üDurdur() = müzikMp3üKapat()
  def Mp3DöngüsünüDurdur() = müzikMp3DöngüsünüKapat()
  def müzikMp3üÇalıyorMu = richBuiltins.isMp3Playing
  def müzikÇalıyorMu = richBuiltins.isMusicPlaying
  def müzikMp3üKapat() = richBuiltins.stopMp3()
  def müzikMp3DöngüsünüKapat() = richBuiltins.stopMp3Loop()
  def müziğiDurdur() = müziğiKapat()
  def müziğiKapat() = richBuiltins.stopMusic()

  def kojoVarsayılanBakışaçısınıKur() = richBuiltins.switchToDefaultPerspective()
  def kojoVarsayılanİkinciBakışaçısınıKur() = richBuiltins.switchToDefault2Perspective()
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
    def eni = en
    def boyu = boy
    def en: Kesir = ta.width
    def boy: Kesir = ta.height
    def x: Kesir = ta.x
    def y: Kesir = ta.y
    def X = ta.x + ta.width
    def Y = ta.y + ta.height
    // todo: more..
  }
  def yatayMerkezKonumu(uzunluk: Kesir): Kesir = tuvalAlanı.x + (tuvalAlanı.en - uzunluk) / 2
  def dikeyMerkezKonumu(uzunluk: Kesir): Kesir = tuvalAlanı.y + (tuvalAlanı.boy - uzunluk) / 2

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

  // ../DrawingCanvasAPI.scala
  def yaklaş(oran: Kesir) = richBuiltins.tCanvas.zoom(oran)
  def yaklaş(oran: Kesir, xMerkez: Kesir, yMerkez: Kesir) = richBuiltins.tCanvas.zoom(oran, xMerkez, yMerkez)
  def yaklaşXY(xOran: Kesir, yOran: Kesir, xMerkez: Kesir, yMerkez: Kesir) =
    richBuiltins.tCanvas.zoomXY(xOran, yOran, xMerkez, yMerkez)
  def yaklaşmayıSil() = richBuiltins.tCanvas.resetPanAndZoom()
  def yaklaşmayaİzinVerme() = richBuiltins.tCanvas.disablePanAndZoom()
  def tuvaliKaydır(x: Kesir, y: Kesir) = richBuiltins.tCanvas.scroll(x, y)
  def tuvaliDöndür(açı: Kesir) = richBuiltins.tCanvas.viewRotate(açı)

  def tuşaBasılıMı(tuş: Sayı) = richBuiltins.isKeyPressed(tuş)
  def tuşaBasınca(iş: Sayı => Birim) = richBuiltins.tCanvas.onKeyPress(iş)
  def tuşuBırakınca(iş: Sayı => Birim) = richBuiltins.tCanvas.onKeyRelease(iş)
  def fareyeTıklıyınca(iş: (Kesir, Kesir) => Birim) = richBuiltins.tCanvas.onMouseClick(iş)
  def fareyiSürükleyince(iş: (Kesir, Kesir) => Birim) = richBuiltins.tCanvas.onMouseDrag(iş)
  def fareKımıldayınca(iş: (Kesir, Kesir) => Birim) = richBuiltins.tCanvas.onMouseMove(iş)

  def gridiGöster() = richBuiltins.tCanvas.gridOn()
  def gridiGizle() = richBuiltins.tCanvas.gridOff()
  def eksenleriGöster() = richBuiltins.tCanvas.axesOn()
  def eksenleriGizle() = richBuiltins.tCanvas.axesOff()
  def açıÖlçeriGöster(): richBuiltins.Picture = açıÖlçeriGöster(-tuvalAlanı.en / 2, -tuvalAlanı.boy / 2)
  def açıÖlçeriGöster(x: Kesir, y: Kesir): richBuiltins.Picture = richBuiltins.tCanvas.showProtractor(x, y)
  def açıÖlçeriGizle() = richBuiltins.tCanvas.hideProtractor()
  def cetveliGöster(): richBuiltins.Picture = cetveliGöster(-tuvalAlanı.en / 2, tuvalAlanı.boy / 2)
  def cetveliGöster(x: Kesir, y: Kesir): richBuiltins.Picture = richBuiltins.tCanvas.showScale(x, y)

  def çizimiKaydet(dosyaAdı: Yazı) = richBuiltins.tCanvas.exportImage(dosyaAdı)
  def çizimiKaydet(dosyaAdı: Yazı, en: Sayı, boy: Sayı) = richBuiltins.tCanvas.exportImage(dosyaAdı, en, boy)
  def çizimiKaydetBoy(dosyaAdı: Yazı, boy: Sayı) = richBuiltins.tCanvas.exportImageH(dosyaAdı, boy)
  def çizimiKaydetEn(dosyaAdı: Yazı, en: Sayı) = richBuiltins.tCanvas.exportImageW(dosyaAdı, en)
  def çizimiPulBoyundaKaydet(dosyaAdı: Yazı, boy: Sayı) = richBuiltins.tCanvas.exportThumbnail(dosyaAdı, boy)

  // todo: help doc
  def Geçiş(
      süreSaniyeOlarak: Kesir,
      ilkEvre: Dizi[Kesir],
      sonEvre: Dizi[Kesir],
      kolaylaştırma: KEasing,
      resimci: Dizi[Kesir] => Resim,
      bitinceGizle: İkil
  ) = {
    val resimci2 = new Function1[Dizi[Kesir], richBuiltins.Picture] { def apply(d: Dizi[Kesir]) = resimci(d).p }
    richBuiltins.Transition(süreSaniyeOlarak, ilkEvre, sonEvre, kolaylaştırma, resimci2, bitinceGizle)
  }
  implicit class trForReverse(a: richBuiltins.Animation) {
    def tersten = a.reversed
    def sonsuzYinelenme = a.repeatedForever
    // todo: more to come
  }
  def canlandırmaDizisi(canlandırmalar: richBuiltins.Animation*) = richBuiltins.animSeq(canlandırmalar)
  def canlandırmaDizisi(canlandırmalar: collection.Seq[richBuiltins.Animation]) =
    richBuiltins.animSeq(canlandırmalar.toSeq)
  def oynat(canlandırma: richBuiltins.Animation) = richBuiltins.run(canlandırma)
  def artalandaOynat(kod: => Unit) = richBuiltins.runInBackground(kod)
  def fareKonumu = richBuiltins.mousePosition
  def yorumla(komutDizisi: Yazı) = richBuiltins.interpret(komutDizisi)
  def yineleSayaçla(miliSaniye: Uzun)(işlev: => Birim) = richBuiltins.tCanvas.timer(miliSaniye)(işlev)
  def canlandır(işlev: => Birim) = richBuiltins.tCanvas.animate(işlev)
  def canlandırEvreyle[Evre](ilkEvre: Evre)(işlev: Evre => Evre): Future[PActivity] =
    richBuiltins.tCanvas.animateWithState(ilkEvre)(işlev)
  def canlandırmayıDurdur(etkinlik: Future[PActivity]) = richBuiltins.tCanvas.stopAnimationActivity(etkinlik)
  def canlandırYenidenÇizerek[Evre](ilkEvre: Evre, sonrakiEvre: Evre => Evre, işlev: Evre => Resim): Birim = {
    val işlev2 = new Function1[Evre, richBuiltins.Picture] { def apply(e: Evre) = işlev(e).p }
    richBuiltins.animateWithRedraw(ilkEvre, sonrakiEvre, işlev2)
  }
  def durdur() = richBuiltins.stopAnimation()
  def canlandırmaBaşlayınca(işlev: => Birim) = richBuiltins.tCanvas.onAnimationStart(işlev)
  def canlandırmaBitince(işlev: => Birim) = richBuiltins.tCanvas.onAnimationStop(işlev)
  def tuvaliEtkinleştir() = richBuiltins.activateCanvas()
  def yazılımcıkDüzenleyicisiniEtkinleştir() = richBuiltins.activateEditor()

  def çıktıArtalanınıKur(renk: Renk) = richBuiltins.setOutputBackground(renk)
  def çıktıYazıRenginiKur(renk: Renk) = richBuiltins.setOutputTextColor(renk)
  def çıktıYazıYüzüBoyunuKur(boy: Sayı) = richBuiltins.setOutputTextFontSize(boy)
  def tuvalBoyutOranınınKur(oran: Kesir) = richBuiltins.setDrawingCanvasAspectRatio(oran)
  def tuvalBoyutlarınıKur(en: Sayı, boy: Sayı) = richBuiltins.setDrawingCanvasSize(en, boy)

  def süreTut(komut: => Birim): Birim = {
    val t0 = buSaniye
    komut
    val delta = buSaniye - t0
    println("Komudun çalışması $delta%.3f saniye sürdü.")
  }

  def oyunSüresiniGöster(
      süreSaniyeOlarak: Sayı,
      mesaj: Yazı,
      renk: Renk = siyah,
      yazıBoyu: Sayı = 15,
      kx: Kesir = 10,
      ky: Kesir = 50
  ) =
    richBuiltins.showGameTime(süreSaniyeOlarak, mesaj, renk, yazıBoyu, kx, ky)
  def oyunSüresiniGeriyeSayarakGöster(
      süreSaniyeOlarak: Sayı,
      mesaj: Yazı,
      renk: Renk = siyah,
      yazıBoyu: Sayı = 15,
      kx: Kesir = 10,
      ky: Kesir = 50
  ) = richBuiltins.showGameTimeCountdown(süreSaniyeOlarak, mesaj, renk, yazıBoyu, kx, ky)

  def sırayaSok(kaçSaniyeSonra: Kesir)(komut: => Birim) = richBuiltins.schedule(kaçSaniyeSonra)(komut)
  def sırayaSok(n: Sayı, kaçSaniyeSonra: Kesir)(komut: => Birim) = richBuiltins.scheduleN(n, kaçSaniyeSonra)(komut)

  type Yöney2B = tr.Yöney2B
  type Resim = tr.Resim
  type İmge = tr.İmge
  type Bellekteİmge = tr.Bellekteİmge
  type Bellekteİmgeİşlemi = tr.Bellekteİmgeİşlemi
  val Yöney2B = tr.Yöney2B
  val Resim = tr.Resim

  import tr.{ res => r }
  val (döndür, döndürMerkezli, filtre, gürültü, örgü) =
    (r.döndür _, r.döndürMerkezli _, r.filtre _, r.gürültü _, r.örgü _)
  val (
    büyütXY,
    saydamlık,
    ton,
    parlaklık,
    aydınlık,
    yansıtY,
    yansıtX,
    eksenler,
    boyaRengi,
    kalemRengi,
    kalemBoyu,
    çizimÖncesiİşlev,
    çizimSonrasıİşlev,
    çevir,
    yansıt,
    soluk,
    bulanık,
    noktaIşık,
    sahneIşığı
  ) = (
    r.büyütXY _,
    r.saydamlık _,
    r.ton _,
    r.parlaklık _,
    r.aydınlık _,
    r.yansıtY,
    r.yansıtX,
    r.eksenler,
    r.boyaRengi _,
    r.kalemRengi _,
    r.kalemBoyu _,
    r.çizimÖncesiİşlev _,
    r.çizimSonrasıİşlev _,
    r.çevir _,
    r.yansıt _,
    r.soluk _,
    r.bulanık _,
    r.noktaIşık _,
    r.sahneIşığı _
  )
  def götür(n: Nokta) = r.götür(n)
  def götür(x: Kesir, y: Kesir) = r.götür(x, y)
  def götür(yy: Yöney2B) = r.götür(yy)
  def kaydır(n: Nokta) = r.kaydır(n)
  def kaydır(x: Kesir, y: Kesir) = r.kaydır(x, y)
  def kaydır(yy: Yöney2B) = r.kaydır(yy)
  def büyüt(oran: Kesir) = r.büyüt(oran)
  def büyüt(xOranı: Kesir, yOranı: Kesir) = r.büyüt(xOranı, yOranı)
  def ışıklar(ışıklar: com.jhlabs.image.LightFilter.Light*) = r.ışıklar(ışıklar: _*)
  def birEfekt(isim: Symbol, özellikler: Tuple2[Symbol, Any]*) = r.birEfekt(isim, özellikler: _*)
  def NoktaIşık(x: Kesir, y: Kesir, yön: Kesir, yükseklik: Kesir, uzaklık: Kesir) =
    r.NoktaIşık(x, y, yön, yükseklik, uzaklık)
  def SahneIşığı(x: Kesir, y: Kesir, yön: Kesir, yükseklik: Kesir, uzaklık: Kesir) =
    r.SahneIşığı(x, y, yön, yükseklik, uzaklık)
  def çiz(r2: Resim) = r.çiz(r2)
  def çiz(rler: Resim*) = r.çiz(rler: _*)
  def çiz(rler: collection.Seq[Resim]) = r.çiz(rler)
  def çizVeSakla(resimler: Resim*) = richBuiltins.drawAndHide(resimler.map(_.p): _*)
  val (çizMerkezde, çizSahne, çizMerkezdeYazı, merkezeTaşı) =
    (r.çizMerkezde _, r.çizSahne _, r.çizMerkezdeYazı _, r.merkezeTaşı _)
  val (sahneKenarındanYansıtma, engeldenYansıtma) = (r.sahneKenarındanYansıtma _, r.engeldenYansıtma _)

  def imge(boy: Sayı, en: Sayı) = r.imge(boy, en)
  def imge(dosya: Yazı) = r.imge(dosya)
  def imge(url: java.net.URL) = r.imge(url)
  val (imgeNoktası, imgeNoktasınıKur) = (r.imgeNoktası _, r.imgeNoktasınıKur _)

  import tr.arayuz
  val ay = arayuz
  type Yazıyüzü = ay.Yazıyüzü
  object Yazıyüzü {
    def apply(adı: Yazı, boyu: Sayı): Yazıyüzü = builtins.Font(adı, boyu)
    def apply(adı: Yazı, boyu: Sayı, biçem: Sayı): Yazıyüzü = builtins.Font(adı, biçem, boyu)
  }

  def zamanTut[T](başlık: Yazı = "Zaman ölçümü:")(işlev: => T)(bitiş: Yazı = "sürdü."): T = { // timeit in Builtins.scala
    val t0 = buSaniye
    val çıktı = işlev
    val delta = buSaniye - t0
    val words = List(başlık, f"$delta%.3f saniye", bitiş).filter(_.nonEmpty)
    println(words.mkString(" "))
    çıktı
  }

  def DokumaBoya(dosya: Yazı, x: Kesir, y: Kesir) = richBuiltins.TexturePaint(dosya, x, y)

  def a_kalıp() = println("Kalıbı kullan") // todo: geçici. Bakınız tr/help.scala
  def def_türkçe() = println("def")

  // more to come (:-)
}

object TurkishInit {
  def init(builtins: CoreBuiltins): Unit = {
    // initialize unstable values:
    TurkishAPI.builtins = builtins
    tr.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Kaplumbağalı Kojo'ya Hoşgeldin!")
        if (b.isScratchPad) {
          println("Kojo Deneme Tahtasını kapatınca geçmiş silinir.")
        }

        //        b.setEditorTabSize(2)

        // code completion
        b.addCodeTemplates(
          "tr",
          codeTemplates
        )
        // help texts
        b.addHelpContent(
          "tr",
          helpContent
        )

      case _ =>
    }
  }

  import tr.help
  val codeTemplates = help.templates
  val helpContent = help.content
}
