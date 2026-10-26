/*
 * Copyright (C) 2013-2025
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
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.xscala.RepeatCommands

// Keep in alphabetical order!
object TurkishAPI
    extends tr.CharMethodsInTurkish
    with tr.CoreTypeMethodsInTurkish
    with tr.DizikYöntemleri
    with tr.DizinYöntemleri
    with tr.DosyaÇevirisi
    with tr.EsnekDizikYöntemleri
    with tr.FutureMethodsInTurkish
    with tr.ÇizimYöntemleri
    with tr.GeoMethodsInTurkish
    with tr.KeyCodesInTurkish
    with tr.Kumanda
    with tr.LazyListMethodsInTurkish
    with tr.MapMethodsInTurkish
    with tr.MatematikYöntemleri
    with tr.MusicUtilsInTurkish
    with tr.OptionMethodsInTurkish
    with tr.ParalelDiziYöntemleri
    with tr.PartialFunctionMethodsInTurkish
    with tr.QueueMethodsInTurkish
    with tr.StackMethodsInTurkish
    with tr.RangeMethodsInTurkish
    with tr.RenkYöntemleri
    with tr.SayıYöntemleri
    with tr.SoundUtilsInTurkish
    with tr.SeqMethodsInTurkish
    with tr.SetMethodsInTurkish
    with tr.StringMethodsInTurkish
    with tr.arayuz.SwingWidgetMethodsInTurkish
    with tr.TakvimVeZamanYöntemleri
    with tr.TuvalÇizimYöntemleri
    with tr.TürÇevirileri
    with tr.YineleyiciYöntemleri
    with tr.İkisindenBiriYöntemleri
    with tr.UrlInTurkish
    with tr.VectorMethodsInTurkish {

  var bi: CoreBuiltins = _ // unstable reference to module. Used to be builtins
  lazy val richBuiltins = bi.asInstanceOf[Builtins]
  lazy val rb = richBuiltins // todo: refactor code below to use rb

  import net.kogics.kojo.lite.i18n.tr // todo: better here than at the top scope?
  // Imports are intransitive (not visibly transitive). So, we "export" the ones that should be on the interface
  // todo: metaprog on these?
  type Nesne = tr.Nesne
  type Birim = tr.Birim
  type Her = tr.Her
  type HerDeğer = tr.HerDeğer
  type HerGönder = tr.HerGönder
  type Yok = tr.Yok
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
  type Yinelenebilir[K] = tr.Yinelenebilir[K]
  type YinelenebilirBirKere[K] = tr.YinelenebilirBirKere[K]
  type Yapıcıdan[DiziTürü, GirdiTürü, ÇıktıTürü] = collection.BuildFrom[DiziTürü, GirdiTürü, ÇıktıTürü]

  type UzunlukBirimi = tr.UzunlukBirimi

  type Biçim = tr.Biçim
  type GeoYol = tr.GeoYol
  type GeoNokta = tr.GeoNokta
  type Grafik2B = tr.Grafik2B

  type Aralık = tr.Aralık
  val Aralık = tr.Aralık
  type Yığın[T] = tr.Yığın[T]
  val Yığın = tr.Yığın
  type Eşlem[A, D] = tr.Eşlem[A, D]
  val Eşlem = tr.Eşlem

  val (yavaş, orta, hızlı, çokHızlı, noktaSayısı, santim, inç) =
    (tr.yavaş, tr.orta, tr.hızlı, tr.çokHızlı, tr.noktaSayısı, tr.santim, tr.inç)

  val Nokta = tr.Nokta

  // just for doc purposes (in code completion), we provide a container of all math methods
  object Matematik extends tr.MatematikYöntemleri

  type ÇiniDünyası = tr.ÇiniDünyası
  val ÇiniDünyası = tr.ÇiniDünyası
  type ÇiniXY = ÇiniDünyası.ÇiniXY
  val ÇiniXY = ÇiniDünyası.ÇiniXY
  type BirSayfaKostüm = tr.BirSayfaKostüm
  val BirSayfaKostüm = tr.BirSayfaKostüm

  def belirt(belit: İkil, mesaj: => Any = ""): Birim = assert(belit, mesaj)
  def gerekli(gerekçe: İkil, mesaj: => Any = ""): Birim = require(gerekçe, mesaj)
  def yeniMp3Çalar = new tr.Mp3Çalar(rb.newMp3Player)

  trait TurkishTurtle {
    def englishTurtle: Turtle
    def sil(): Birim = englishTurtle.clear() // bbx: does this do anything? See sil def below..
    def göster() = görünür()
    def gizle() = görünmez()
    def görünür(): Birim = englishTurtle.visible()
    def görünmez(): Birim = englishTurtle.invisible()
    def ileri(adım: Kesir): Birim = englishTurtle.forward(adım)
    def ileri(): Birim = englishTurtle.forward(25)
    def geri(adım: Kesir): Birim = englishTurtle.back(adım)
    def geri(): Birim = englishTurtle.back(25)
    def sağ(açı: Kesir, yarıçap: Kesir): Birim = englishTurtle.right(açı, yarıçap)
    def sağ(açı: Kesir): Birim = englishTurtle.right(açı)
    def sağ(): Birim = englishTurtle.right(90)
    def sol(açı: Kesir, yarıçap: Kesir): Birim = englishTurtle.left(açı, yarıçap)
    def sol(açı: Kesir): Birim = englishTurtle.left(açı)
    def sol(): Birim = englishTurtle.left(90)
    def atla(x: Kesir, y: Kesir): Birim = englishTurtle.jumpTo(x, y)
    def ilerle(x: Kesir, y: Kesir): Birim = englishTurtle.moveTo(x, y)
    def zıpla(n: Kesir): Birim = {
      englishTurtle.saveStyle() // to preserve pen state
      englishTurtle.hop(n) // hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def zıpla(): Birim = zıpla(25)
    def ev(): Birim = englishTurtle.home()
    def noktayaDön(n: Nokta): Birim = englishTurtle.towards(n)
    def noktayaDön(x: Kesir, y: Kesir): Birim = englishTurtle.towards(x, y)
    def noktayaGit(x: Kesir, y: Kesir): Birim = englishTurtle.lineTo(x, y)
    def noktayaGit(n: Nokta): Birim = englishTurtle.lineTo(n)
    def açıyaDön(açı: Kesir): Birim = englishTurtle.setHeading(açı)
    def doğrultu: Kesir = englishTurtle.heading
    def doğu(): Birim = englishTurtle.setHeading(0)
    def batı(): Birim = englishTurtle.setHeading(180)
    def kuzey(): Birim = englishTurtle.setHeading(90)
    def güney(): Birim = englishTurtle.setHeading(-90)
    def canlandırmaHızınıKur(n: Uzun): Birim = englishTurtle.setAnimationDelay(n)
    def canlandırmaHızı: Uzun = englishTurtle.animationDelay
    // yaz overlaps with (satır)yaz
    def tuvaleYaz(t: Her): Birim = yazı(t)
    def yazı(t: Her): Birim = englishTurtle.write(t)
    // ~/src/kojo/git/kojo/src/main/scala/net/kogics/kojo/turtle/Turtle.scala
    // ../../turtle/Turtle.scala
    def yazıBoyunuKur(boy: Sayı): Birim = englishTurtle.setPenFontSize(boy)
    def yazıYüzünüKur(yy: Yazıyüzü): Birim = englishTurtle.setPenFont(yy)
    def yay(yarıçap: Kesir, açı: Kesir): Birim = englishTurtle.arc(yarıçap, math.round(açı).toInt)
    def dön(açı: Kesir, yarıçap: Kesir): Birim = englishTurtle.turn(açı, yarıçap)
    def dön(açı: Kesir): Birim = englishTurtle.turn(açı)
    def üçgen(en: Kesir = 25): Birim = { yinele(3) { ileri(en); sağ(120) } }
    def daire(yarıçap: Kesir = 25): Birim = englishTurtle.circle(yarıçap)
    def kare(en: Kesir = 25): Birim = { yinele(4) { ileri(en); sağ(90) } }
    def konumuKur(x: Kesir, y: Kesir): Birim = englishTurtle.setPosition(x, y)
    // ../../xscala/help.scala
    // ../../core/TurtleMover.scala
    def konumuDeğiştir(x: Kesir, y: Kesir): Birim = englishTurtle.changePosition(x, y)
    def konum: Nokta = englishTurtle.position
    def kalemiİndir(): Birim = englishTurtle.penDown()
    def kalemiKaldır(): Birim = englishTurtle.penUp()
    def kalemİnikMi: İkil = englishTurtle.style.down
    def kalemRenginiKur(renk: Renk): Birim = englishTurtle.setPenColor(renk)
    def boyamaRenginiKur(boya: Boya): Birim = englishTurtle.setFillColor(boya)
    def kalemKalınlığınıKur(n: Kesir): Birim = englishTurtle.setPenThickness(n)
    def biçimleriBelleğeYaz(): Birim = englishTurtle.saveStyle()
    def biçimleriGeriYükle(): Birim = englishTurtle.restoreStyle()
    def konumVeYönüBelleğeYaz(): Birim = englishTurtle.savePosHe()
    def konumVeYönüGeriYükle(): Birim = englishTurtle.restorePosHe()
    def ışınlarıAç(): Birim = englishTurtle.beamsOn()
    def ışınlarıKapat(): Birim = englishTurtle.beamsOff()
    def giysiKur(imge: İmge): Birim = englishTurtle.setCostumeImage(imge)
    def giysiKur(dosyaAdı: Yazı): Birim = englishTurtle.setCostume(dosyaAdı)
    def giysileriKur(dosyaAdı: Yazı*): Birim = englishTurtle.setCostumes(dosyaAdı: _*)
    def birsonrakiGiysi(): Birim = englishTurtle.nextCostume()
    def giysiyiBüyült(oran: Kesir): Birim = englishTurtle.scaleCostume(oran)
    def hızıKur(hız: Hız): Birim = englishTurtle.setSpeed(hız)
    def nokta(çap: Sayı): Birim = englishTurtle.dot(çap)
    def nokta(): Birim = englishTurtle.dot(25)
  }

  class Kaplumbağa(override val englishTurtle: Turtle) extends TurkishTurtle {
    def this(startX: Kesir, startY: Kesir, costumeFileName: Yazı) =
      this(bi.TSCanvas.newTurtle(startX, startY, costumeFileName))
    def this(startX: Kesir, startY: Kesir) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0, 0)
    def uzaklık(öbürü: Kaplumbağa): Kesir = englishTurtle.distanceTo(öbürü.englishTurtle)
    def çevir(öbürü: Kaplumbağa): Birim = englishTurtle.towards(öbürü.englishTurtle)
    // get f: Turtle => Unit from g: Kaplumbağa => Birim
    val buİşlev = this // Function1 has its own this
    def davran(işlev: Kaplumbağa => Birim): Birim = {
      val f = new Function1[Turtle, Unit] { def apply(t: Turtle) = işlev(buİşlev) }
      englishTurtle.act(f)
    }
    def canlan = tepkiVer _
    def tepkiVer(işlev: Kaplumbağa => Birim): Birim = {
      val f = new Function1[Turtle, Unit] { def apply(t: Turtle) = işlev(buİşlev) }
      englishTurtle.react(f)
    }
  }
  class Kaplumbağa0(t0: => Turtle) extends TurkishTurtle { // by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object kaplumbağa extends Kaplumbağa0(bi.TSCanvas.turtle0)
  def sil(): Birim = bi.TSCanvas.clear()
  def silipSakla(): Birim = silVeSakla()
  def silVeSakla(): Birim = { bi.TSCanvas.clear(); kaplumbağa.görünmez() } // cleari
  def çizimiSil(): Birim = bi.TSCanvas.clearStepDrawing()
  def çıktıyıSil(): Birim = bi.clearOutput()
  def silVeÇizimBiriminiKur(ub: UzunlukBirimi) = bi.TSCanvas.clearWithUL(ub)

  // lazy val renkler = bi.cm // ColorMaker in ../../staging/color.scala and ../../doodle/Color.scala
  // lazy val tuşlar = bi.Kc // Key Codes

  def artalanıKur(r: Renk): Birim = bi.setBackground(r)
  def artalanıKur(b: Boya): Birim = bi.setBackground(b)
  def artalanıKurDik(r1: Renk, r2: Renk): Birim = bi.TSCanvas.setBackgroundV(r1, r2)
  def artalanıKurYatay(r1: Renk, r2: Renk): Birim = bi.TSCanvas.setBackgroundH(r1, r2)

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

  def yineleKere[T](dizi: Yinelenebilir[T])(diziKomut: T => Birim): Birim = {
    RepeatCommands.repeatFor(dizi) { diziKomut }
  }
  def yineleİçin[T](dizi: Yinelenebilir[T])(diziKomut: T => Birim): Birim = {
    RepeatCommands.repeatFor(dizi) { diziKomut }
  }

  def yineleİlktenSona(ilki: Sayı, sonu: Sayı)(diziKomut: Sayı => Birim): Birim = {
    RepeatCommands.repeatFor(ilki to sonu) { diziKomut }
  }

  // simple IO
  def satıroku(istem: Yazı = ""): Yazı = bi.readln(istem)
  def satıryaz(): Birim = println()
  def satıryaz(veri: Her): Birim = println(veri) // Transferred here from sv.tw.kojo.
  def yaz(veri: Her): Birim = print(veri)

  // ../CoreBuiltins.scala
  // bir de rasgele: Kesir var matematik trait'inden geliyor
  def rastgele(üstSınır: Sayı): Sayı = bi.random(üstSınır)
  def rastgele(altSınır: Sayı, üstSınır: Sayı): Sayı = bi.random(altSınır, üstSınır)
  def rastgeleSayı = bi.randomInt
  def rastgeleUzun = bi.randomLong
  def rastgeleKesir(üstSınır: Kesir) = bi.randomDouble(üstSınır)
  def rastgeleKesir(altSınır: Kesir, üstSınır: Kesir) = bi.randomDouble(altSınır, üstSınır)
  def rastgeleÇanEğrisinden = rastgeleDoğalKesir
  def rastgeleNormalKesir = rastgeleDoğalKesir
  def rastgeleDoğalKesir = bi.randomNormalDouble
  def rastgeleTohumunuKur(tohum: Uzun = rastgeleUzun) = bi.initRandomGenerator(tohum)
  def rastgeleİkil = rastgeleSeçim
  def rastgeleSeçim = bi.randomBoolean
  def rastgeleRenk: Renk = bi.randomColor
  def rastgeleŞeffafRenk: Renk = bi.randomTransparentColor
  def rastgeleDiziden[T](dizi: Dizi[T]) = bi.randomFrom(dizi)
  def rastgeleDiziden[T](dizi: Dizi[T], ağırlıklar: Dizi[Kesir]) = bi.randomFrom(dizi, ağırlıklar)
  // def diziKarıştır[T](xs: Dizi[T]): Dizi[T] = util.Random.shuffle(xs)
  def rastgeleKarıştır[T, C](xLer: YinelenebilirBirKere[T])
    (implicit yapıcı: Yapıcıdan[xLer.type, T, C]): C = util.Random.shuffle(xLer)

  def durakla(saniye: Kesir): Birim = bi.pause(saniye)
  def duraklaMiliSaniye(miliSaniye: Uzun): Birim = bi.pauseMillis(miliSaniye)

  def üçgenDöşeme(noktalar: Dizi[Nokta]): Diz[Üçgen] = bi.triangulate(noktalar)

  // todo: klasör?
  def evDizini: Yazı = bi.homeDir
  def buDizin: Yazı = bi.currentDir
  def kurulumDizini: Yazı = bi.installDir
  def yazıyüzleri: Dizin[Yazı] = bi.availableFontNames
  def yazıyüzü(adı: Yazı, boyu: Sayı): Yazıyüzü = bi.Font(adı, boyu)
  def yazıyüzü(adı: Yazı, boyu: Sayı, biçem: Sayı): Yazıyüzü = bi.Font(adı, biçem, boyu)
  def yazıÇerçevesi(yazı: Yazı, yazıBoyu: Sayı, yazıyüzüAdı: Yazı = null): Dikdörtgen =
    bi.textExtent(yazı, yazıBoyu, yazıyüzüAdı)

  val kaplumbağa0 = kaplumbağa
  def yeniKaplumbağa(x: Kesir, y: Kesir) = new Kaplumbağa(x, y)
  def yeniKaplumbağa(x: Kesir, y: Kesir, giysiDosyası: Yazı) = new Kaplumbağa(x, y, giysiDosyası)

  def buradaDur = burdaDur _
  def burdaDur(mesaj: Her): Birim = {
    val pauseMessage = "Duruş Noktası"
    val resumeMsg = "Devam etmek için Giriş (Return), çıkmak için Kaçış (Esc) tuşuna bas"
    rb.breakpoint(mesaj, pauseMessage, resumeMsg)
  }
  def buradaDurma = burdaDurma _
  def burdaDurma(farketmez: Her) = {}

  def sayıOku(istem: Yazı = ""): Sayı = rb.readInt(istem)
  def kesirOku(istem: Yazı = ""): Kesir = rb.readDouble(istem)

  def resimİndir(httpAdresi: Yazı): Birim = rb.preloadImage(httpAdresi)
  def müzikİndir(httpAdresi: Yazı): Birim = rb.preloadMp3(httpAdresi)

  def müzikMp3üÇal(mp3dosyası: Yazı): Birim = rb.playMp3(mp3dosyası)
  def sesMp3üÇal(mp3dosyası: Yazı): Birim = rb.playMp3Sound(mp3dosyası)
  def müzikMp3üÇalDöngülü(mp3dosyası: Yazı): Birim = rb.playMp3Loop(mp3dosyası)

  def Mp3ÇalıyorMu: İkil = müzikMp3üÇalıyorMu
  def Mp3üDurdur(): Birim = müzikMp3üKapat()
  def Mp3DöngüsünüDurdur(): Birim = müzikMp3DöngüsünüKapat()
  def müzikMp3üÇalıyorMu: İkil = rb.isMp3Playing
  def müzikÇalıyorMu: İkil = rb.isMusicPlaying
  def müzikMp3üKapat(): Birim = rb.stopMp3()
  def müzikMp3DöngüsünüKapat(): Birim = rb.stopMp3Loop()
  def müziğiDurdur(): Birim = müziğiKapat()
  def müziğiKapat(): Birim = rb.stopMusic()

  def kojoVarsayılanBakışaçısınıKur(): Birim = rb.switchToDefaultPerspective()
  def kojoVarsayılanİkinciBakışaçısınıKur(): Birim = rb.switchToDefault2Perspective()
  def kojoYazılımcıkBakışaçısınıKur(): Birim = rb.switchToScriptEditingPerspective()
  def kojoÇalışmaSayfalıBakışaçısınıKur(): Birim = rb.switchToWorksheetPerspective()
  def kojoÖyküBakışaçısınıKur(): Birim = rb.switchToStoryViewingPerspective()
  def kojoGeçmişBakışaçısınıKur(): Birim = rb.switchToHistoryBrowsingPerspective()
  def kojoÇıktılıÖyküBakışaçısınıKur(): Birim = rb.switchToOutputStoryViewingPerspective()

  def tümEkranÇıktı(): Birim = rb.toggleFullScreenOutput()
  def tümEkranTuval(): Birim = tümEkran()
  def tümEkran(): Birim = rb.toggleFullScreenCanvas()
  object tuvalAlanı {
    def ta = rb.canvasBounds
    def eni = en
    def boyu = boy
    def en: Kesir = ta.width
    def boy: Kesir = ta.height
    def x: Kesir = ta.x
    def y: Kesir = ta.y
    // Sağ ve üst kenar. getMaxX/getMaxY ile aynı şey (x + en, y + boy); İngilizce
    // karşılıkları olsun diye onların üstünden yazıldı -- çevirmen kuralı buna dayanıyor.
    def X = ta.getMaxX
    def Y = ta.getMaxY
    // todo: more..
  }
  def yatayMerkezKonumu(uzunluk: Kesir): Kesir = tuvalAlanı.x + (tuvalAlanı.en - uzunluk) / 2
  def dikeyMerkezKonumu(uzunluk: Kesir): Kesir = tuvalAlanı.y + (tuvalAlanı.boy - uzunluk) / 2

  def ekranTazelemeHızınıKur(saniyedeKaçKere: Sayı): Birim = rb.setRefreshRate(saniyedeKaçKere)
  def ekranTazelemeHızınıGöster(renk: Renk, yazıBoyu: Sayı = 15): Birim = rb.showFps(renk, yazıBoyu)

  // ../DrawingCanvasAPI.scala
  def yaklaş(oran: Kesir): Birim = rb.tCanvas.zoom(oran)
  def yaklaş(oran: Kesir, xMerkez: Kesir, yMerkez: Kesir): Birim = rb.tCanvas.zoom(oran, xMerkez, yMerkez)
  def yaklaşXY(xOran: Kesir, yOran: Kesir, xMerkez: Kesir, yMerkez: Kesir): Birim =
    rb.tCanvas.zoomXY(xOran, yOran, xMerkez, yMerkez)
  def tuvaliYakınlaştır(xOran: Kesir, yOran: Kesir, xMerkez: Kesir, yMerkez: Kesir): Birim =
    rb.tCanvas.zoomXY(xOran, yOran, xMerkez, yMerkez)
  def yaklaşmayıSil(): Birim = rb.tCanvas.resetPanAndZoom()
  def yaklaşmayaİzinVerme(): Birim = rb.tCanvas.disablePanAndZoom()
  def tuvaliSabitle(): Birim = yaklaşmayaİzinVerme()
  def tuvaliKaydır(x: Kesir, y: Kesir): Birim = rb.tCanvas.scroll(x, y)
  def tuvaliDöndür(açı: Kesir): Birim = rb.tCanvas.viewRotate(açı)

  def tuşaBasılıMı(tuş: Sayı): İkil = rb.isKeyPressed(tuş)
  // ikojo'da (canlı) bu adla duruyor; iki tarafta da ikisi birden çalışsın.
  def tuşBasılıMı(tuş: Sayı): İkil = tuşaBasılıMı(tuş)
  def tuşaBasınca(iş: Sayı => Birim): Birim = rb.tCanvas.onKeyPress(iş)
  def tuşuBırakınca(iş: Sayı => Birim): Birim = rb.tCanvas.onKeyRelease(iş)
  def fareyeTıklayınca(iş: (Kesir, Kesir) => Birim): Birim = rb.tCanvas.onMouseClick(iş)
  def fareyeTıklıyınca(iş: (Kesir, Kesir) => Birim): Birim = rb.tCanvas.onMouseClick(iş)
  def fareyiSürükleyince(iş: (Kesir, Kesir) => Birim): Birim = rb.tCanvas.onMouseDrag(iş)
  def fareKımıldayınca(iş: (Kesir, Kesir) => Birim): Birim = rb.tCanvas.onMouseMove(iş)

  def gridiGöster(): Birim = rb.tCanvas.gridOn()
  def gridiGizle(): Birim = rb.tCanvas.gridOff()
  def ızgarayıGöster() = gridiGöster()
  def ızgarayıGizle() = gridiGizle()
  def eksenleriGöster(): Birim = rb.tCanvas.axesOn()
  def eksenleriGizle(): Birim = rb.tCanvas.axesOff()
  def açıÖlçeriGöster(): Resim = açıÖlçeriGöster(-tuvalAlanı.en / 2, -tuvalAlanı.boy / 2)
  def açıÖlçeriGöster(x: Kesir, y: Kesir): Resim = new Resim(rb.tCanvas.showProtractor(x, y))
  def açıÖlçeriGizle(): Birim = rb.tCanvas.hideProtractor()
  def cetveliGöster(): Resim = cetveliGöster(-tuvalAlanı.en / 2, tuvalAlanı.boy / 2)
  def cetveliGöster(x: Kesir, y: Kesir): Resim = new Resim(rb.tCanvas.showScale(x, y))

  def çizimiKaydet(dosyaAdı: Yazı): Birim = rb.tCanvas.exportImage(dosyaAdı)
  def çizimiKaydet(dosyaAdı: Yazı, en: Sayı, boy: Sayı): Birim = rb.tCanvas.exportImage(dosyaAdı, en, boy)
  def çizimiKaydetBoy(dosyaAdı: Yazı, boy: Sayı): Birim = rb.tCanvas.exportImageH(dosyaAdı, boy)
  def çizimiKaydetEn(dosyaAdı: Yazı, en: Sayı): Birim = rb.tCanvas.exportImageW(dosyaAdı, en)
  def çizimiPulBoyundaKaydet(dosyaAdı: Yazı, boy: Sayı): Birim = rb.tCanvas.exportThumbnail(dosyaAdı, boy)

  type Canlandırma = rb.Animation

  // todo: help doc
  def Geçiş(
      süreSaniyeOlarak: Kesir,
      ilkEvre: Dizi[Kesir],
      sonEvre: Dizi[Kesir],
      kolaylaştırma: Resim.YumuşakGeçiş,
      resimci: Dizi[Kesir] => Resim,
      bitinceGizle: İkil
  ): Canlandırma = {
    val resimci2 = new Function1[Dizi[Kesir], rb.Picture] { def apply(d: Dizi[Kesir]) = resimci(d).p }
    rb.Transition(süreSaniyeOlarak, ilkEvre, sonEvre, kolaylaştırma, resimci2, bitinceGizle)
  }
  implicit class trForReverse(a: Canlandırma) {
    def tersten = a.reversed
    def sonsuzYinelenme = a.repeatedForever
    // todo: more to come
  }
  object YumuşakGeçiş {
    val DörtlüGirdiÇıktı = Resim.yumuşakGeçiş.QuadInOut
    val Doğrusal = Resim.yumuşakGeçiş.Linear
    // more to come. See: ~/src/kojo/git/kojo/src/main/scala/net/kogics/kojo/kmath/easing.scala
  }
  def canlandırmaDizisi(canlandırmalar: Canlandırma*): Canlandırma = rb.animSeq(canlandırmalar)
  def canlandırmaDizisi(canlandırmalar: Diz[Canlandırma]): Canlandırma =
    rb.animSeq(canlandırmalar.toSeq)
  def canlandırmaEşzamanlı(canlandırmalar: Canlandırma*) = rb.animPar(canlandırmalar)
  def canlandırmaEşzamanlı(canlandırmalar: Diz[Canlandırma]) =
    rb.animPar(canlandırmalar.toSeq)
  def oynat(canlandırma: Canlandırma): Birim = rb.run(canlandırma)
  def artalandaOynat(kod: => Birim): Birim = rb.runInBackground(kod)
  def fareKonumu: Nokta = rb.mousePosition
  def yorumla(komutDizisi: Yazı): Birim = rb.interpret(komutDizisi)
  def yineleSayaçla(miliSaniye: Uzun)(işlev: => Birim): JGelecek[PEtkinlik] = rb.tCanvas.timer(miliSaniye)(işlev)
  def canlandır(işlev: => Birim) = rb.tCanvas.animate(işlev)
  def canlandırEvreyle[Evre](ilkEvre: Evre)(işlev: Evre => Evre): JGelecek[PEtkinlik] =
    rb.tCanvas.animateWithState(ilkEvre)(işlev)
  def canlandırmayıDurdur(etkinlik: JGelecek[PEtkinlik]) = rb.tCanvas.stopAnimationActivity(etkinlik)
  def canlandırYenidenÇizerek[Evre](ilkEvre: Evre, sonrakiEvre: Evre => Evre, işlev: Evre => Resim): Birim = {
    val işlev2 = new Function1[Evre, rb.Picture] { def apply(e: Evre) = işlev(e).p }
    rb.animateWithRedraw(ilkEvre, sonrakiEvre, işlev2)
  }
  def canlandırTuvalÇizimle(görüntüyüÇiz: TuvalÇizim => Birim): Birim = rb.animateWithCanvasDraw(görüntüyüÇiz)
  def durdur() = rb.stopAnimation()
  def canlandırmaBaşlayınca(işlev: => Birim) = rb.tCanvas.onAnimationStart(işlev)
  def canlandırmaBitince(işlev: => Birim) = rb.tCanvas.onAnimationStop(işlev)
  def tuvaliEtkinleştir() = rb.activateCanvas()
  def yazılımcıkDüzenleyicisiniEtkinleştir() = rb.activateEditor()

  def çıktıArtalanınıKur(renk: Renk) = rb.setOutputBackground(renk)
  def çıktıYazıRenginiKur(renk: Renk) = rb.setOutputTextColor(renk)
  def çıktıYazıYüzüBoyunuKur(boy: Sayı) = rb.setOutputTextFontSize(boy)
  def tuvalBoyutOranınıKur(oran: Kesir) = rb.setDrawingCanvasAspectRatio(oran)
  def tuvalBoyutlarınıKur(en: Sayı, boy: Sayı) = rb.setDrawingCanvasSize(en, boy)

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
    rb.showGameTime(süreSaniyeOlarak, mesaj, renk, yazıBoyu, kx, ky)
  def oyunSüresiniGeriyeSayarakGöster(
      süreSaniyeOlarak: Sayı,
      mesaj: Yazı,
      renk: Renk = siyah,
      yazıBoyu: Sayı = 15,
      kx: Kesir = 10,
      ky: Kesir = 50
  ) = rb.showGameTimeCountdown(süreSaniyeOlarak, mesaj, renk, yazıBoyu, kx, ky)

  def sırayaSok(kaçSaniyeSonra: Kesir)(komut: => Birim) = rb.schedule(kaçSaniyeSonra)(komut)
  def sırayaSok(n: Sayı, kaçSaniyeSonra: Kesir)(komut: => Birim) = rb.scheduleN(n, kaçSaniyeSonra)(komut)

  type Yöney2B = tr.Yöney2B
  type Resim = tr.Resim
  type İmge = tr.İmge
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
  // `ötele` = `götür`; `öteleme` eskitildi (bkz. tr/resim.scala)
  def ötele(n: Nokta) = r.ötele(n)
  def ötele(x: Kesir, y: Kesir) = r.ötele(x, y)
  def ötele(yy: Yöney2B) = r.ötele(yy)
  @deprecated("eylemle başlayan ada geçildi: ötele ya da götür kullanın", "Eylül 2026")
  def öteleme(x: Kesir, y: Kesir) = r.götür(x, y)
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
  def çiz(rler: Diz[Resim]) = r.çiz(rler)
  def çizVeSakla(resimler: Resim*) = rb.drawAndHide(resimler.map(_.p): _*)
  val (çizMerkezde, çizSahne, çizMerkezdeYazı, merkezeTaşı) =
    (r.çizMerkezde _, r.çizSahne _, r.çizMerkezdeYazı _, r.merkezeTaşı _)
  val (sahneKenarındanYansıtma, engeldenYansıtma) = (r.sahneKenarındanYansıtma _, r.engeldenYansıtma _)

  def imge(boy: Sayı, en: Sayı) = r.imge(boy, en)
  def imge(dosya: Yazı) = r.imge(dosya)
  def imge(bkk: BKK) = r.imge(bkk)
  val (imgeNoktası, imgeNoktasınıKur) = (r.imgeNoktası _, r.imgeNoktasınıKur _)

  import tr.arayuz
  val ay = arayuz
  type Yazıyüzü = ay.Yazıyüzü
  object Yazıyüzü {
    def apply(adı: Yazı, boyu: Sayı): Yazıyüzü = bi.Font(adı, boyu)
    def apply(adı: Yazı, boyu: Sayı, biçem: Sayı): Yazıyüzü = bi.Font(adı, biçem, boyu)
    val (sade, vurgulu, kalın) = (bi.PlainFont, bi.ItalicFont, bi.BoldFont)
  }

  def zamanTut[T](başlık: Yazı = "Zaman ölçümü:")(işlev: => T)(bitiş: Yazı = "sürdü."): T = { // timeit in ../Builtins.scala
    val t0 = buSaniye
    val çıktı = işlev
    val delta = buSaniye - t0
    val words = List(başlık, f"$delta%.3f saniye", bitiş).filter(_.nonEmpty)
    println(words.mkString(" "))
    çıktı
  }

  def DokumaBoya(dosya: Yazı, x: Kesir, y: Kesir) = rb.TexturePaint(dosya, x, y)

  def a_kalıp() = println("Kalıbı kullan") // todo: geçici. Bakınız tr/help.scala

  def başlangıçNoktasıÜstSolKöşeOlsun() = rb.originTopLeft()
  def başlangıçNoktasıAltSolKöşeOlsun() = rb.originBottomLeft()
  def notaÇal(frekans: Sayı, süreMiliSaniye: Sayı, ses: Sayı = 80): Birim = rb.playNote(frekans, süreMiliSaniye, ses)
  def notaÇalgısınıKur(çalgı: Sayı): Birim = rb.setNoteInstrument(çalgı)

  // to help facilitate testing of turkish keyword hiliting in:
  // ~/kojo-repo/src/test/scala/net/kogics/kojo/lexer/ScalariformTokenMakerTest.scala
  var testTrKeywords = false // used in tr/package.scala

  def yazıTamamlamaSeçenekleriniYazdırmayıAçKapa() = tr.dumpCompletions = !tr.dumpCompletions

  def sürüm = rb.version

  def kumandaKolu(yarıçap: Kesir): KumandaKolu = rb.joystick(yarıçap)

  import tr.{Yardımcı => yardımcı}
  val Yardımcı = yardımcı

  // pong.kojo için. canlandır komutu içinde sıfırdan büyük olur:
  def ikiÇizimArasıSüre: Kesir = bi.TSCanvas.frameDeltaTime

  // todo: more to come (:-)
}

object TurkishInit {
  def preInit(): Unit = LanguageEditorDataRegistry.register(
    LanguageEditorData(
      tr.keywordList,
      tr.keywordTemplateMap,
      tr.codeTemplateMap,
      Map(net.kogics.kojo.core.TwMode -> tr.KeywordHelp.content)
    )
  )

  def init(builtins: CoreBuiltins): Unit = {
    // initialize unstable values:
    TurkishAPI.bi = builtins
    tr.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Kaplumbağalı Kojo'ya Hoşgeldin!")
        if (b.isScratchPad) {
          println("Kojo Deneme Tahtasını kapatınca geçmiş silinir.")
        }

        // b.setEditorTabSize(2)

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
