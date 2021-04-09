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

import net.kogics.kojo.lite.{CoreBuiltins, Builtins}
import net.kogics.kojo.xscala.RepeatCommands
import net.kogics.kojo.core.Turtle
import java.awt.Color

object TurkishAPI {
  var builtins: CoreBuiltins = _ //unstable reference to module
  lazy val richBuiltins = builtins.asInstanceOf[Builtins]

  import net.kogics.kojo.lite.i18n.tr // todo: better here than at the top scope?
  // Imports are intransitive (not visibly transitive). So, we "export" the ones that should be on the interface
  // todo: metaprog on these?
  type Nesne=tr.Nesne
  type Birim=tr.Birim
  type Her=tr.Her
  type HerDeğer=tr.HerDeğer
  type HerGönder=tr.HerGönder
  type Yok=tr.Yok
  type Hiç=tr.Hiç
  type Renk=tr.Renk
  type Boya=tr.Boya
  type Hız=tr.Hız
  type Nokta=tr.Nokta
  type Dikdörtgen=tr.Dikdörtgen
  type Üçgen=tr.Üçgen
  type İkil=tr.İkil
  type Seçim=tr.Seçim
  type Lokma=tr.Lokma
  type Kısa=tr.Kısa
  type Sayı=tr.Sayı
  type Uzun=tr.Uzun
  type İriSayı=tr.İriSayı
  type UfakKesir=tr.UfakKesir
  type Kesir=tr.Kesir
  type İriKesir=tr.İriKesir
  type Harf=tr.Harf
  type Yazı=tr.Yazı

  type Belki[T]=tr.Belki[T]
  type Biri[T]=tr.Biri[T]
  val Hiçbiri=tr.Hiçbiri
  val Biri=tr.Biri
  // val varMı = tr.varMı _
  // örnek: if (varMı(resim.çarpışma(Resim.tuvalınSınırları))) {...} else {...}
  // .isDefined yerine
  def varMı[T](o: Belki[T]): İkil = o match {
    case None    => yanlış
    case Some(x) => doğru
  }
  def yokMu[T](o: Belki[T]): İkil = !varMı(o)

  type Dizi[T]=tr.Dizi[T]
  type Dizin[T]=tr.Dizin[T]
  type MiskinDizin[T]=tr.MiskinDizin[T]
  type Küme[T] = tr.Küme[T]
  type Yöney[T] = tr.Yöney[T]
  val Küme = tr.Küme
  val Yöney = tr.Yöney

  type Dizim[T]=tr.Dizim[T]
  type EsnekDizim[T]=tr.Dizim[T]
  val Dizim = tr.Dizim
  val EsnekDizim = tr.EsnekDizim

  type Sayılar=tr.Sayılar
  type UzunlukBirimi=tr.UzunlukBirimi

  type GeoYol=tr.GeoYol
  type GeoNokta=tr.GeoNokta
  type Grafik2B=tr.Grafik2B

  type Aralık = tr.Aralık
  val Aralık = tr.Aralık
  type Yığın[T] = tr.Yığın[T]
  val Yığın = tr.Yığın
  type Eşlem[A,D] = tr.Eşlem[A,D]
  val Eşlem = tr.Eşlem

  val (doğru, yanlış, yavaş, orta, hızlı, çokHızlı, noktaSayısı, santim, inç) = (tr.doğru, tr.yanlış, tr.yavaş, tr.orta, tr.hızlı, tr.çokHızlı, tr.noktaSayısı, tr.santim, tr.inç)

  val Nokta = tr.Nokta
  val Dizi = tr.Dizi
  val Dizin = tr.Dizin
  val Sayılar = tr.Sayılar
  val MiskinDizin = tr.MiskinDizin
  val Boş = tr.Boş

  def gerekli(gerekçe: İkil, mesaj: => Any): Birim = require(gerekçe, mesaj)

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

  import tr.{matematik => m}
  val matematik = m // so that coders can look up math functions using code completion in Kojo
  def piSayısı: Kesir = math.Pi
  def eSayısı: Kesir = math.E
  val gücü = m.kuvveti _
  val (karesi, karekökü, kuvveti, onlukTabandaLogu, doğalLogu, logaritması, sinüs, kosinüs, tanjant, sinüsünAçısı, kosinüsünAçısı, tanjantınAçısı) = (m.karesi _, m.karekökü _, m.kuvveti _, m.onlukTabandaLogu _, m.doğalLogu _, m.logaritması _, m.sinüs _, m.kosinüs _, m.tanjant _, m.sinüsünAçısı _, m.kosinüsünAçısı _, m.tanjantınAçısı _)
  val (eüssü, radyana, dereceye, taban, tavan, yakını) = (m.eüssü _, m.radyana _, m.dereceye _, m.taban _, m.tavan _, m.yakını _)
  val (işareti, sayıya, logTabanlı) = (m.işareti _, m.sayıya _, m.logTabanlı _)
  // todo: can we use Number instead? 
  def rastgele = m.rastgele
  def yuvarla(sayı: Number, basamaklar: Sayı = 0): Kesir = m.yuvarla(sayı, basamaklar)
  def mutlakDeğer(x: Sayı): Sayı = m.mutlakDeğer(x)
  def mutlakDeğer(x: Uzun): Uzun = m.mutlakDeğer(x)
  def mutlakDeğer(x: Kesir): Kesir = m.mutlakDeğer(x)
  def mutlakDeğer(x: UfakKesir): UfakKesir = m.mutlakDeğer(x)
  def yakın(x: Kesir): Uzun = m.yakın(x)
  def yakın(x: UfakKesir): Sayı = m.yakın(x)
  def enİrisi(x: Sayı, y: Sayı): Sayı = m.enİrisi(x, y)
  def enUfağı(x: Sayı, y: Sayı): Sayı = m.enUfağı(x, y)
  def enİrisi(x: Uzun, y: Uzun): Uzun = m.enİrisi(x, y)
  def enUfağı(x: Uzun, y: Uzun): Uzun = m.enUfağı(x, y)
  def enİrisi(x: Kesir, y: Kesir): Kesir = m.enİrisi(x, y)
  def enUfağı(x: Kesir, y: Kesir): Kesir = m.enUfağı(x, y)
  def enİrisi(x: UfakKesir, y: UfakKesir): UfakKesir = m.enİrisi(x, y)
  def enUfağı(x: UfakKesir, y: UfakKesir): UfakKesir = m.enUfağı(x, y)
  //
  // ../CoreBuiltins.scala
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
  // def diziKarıştır[T](xs: Dizi[T]): Dizi[T] = util.Random.shuffle(xs)
  def rastgeleKarıştır[T, C](xs: IterableOnce[T])(implicit bf: collection.BuildFrom[xs.type, T, C]): C = util.Random.shuffle(xs)

  def durakla(saniye: Kesir) = builtins.pause(saniye)

  def üçgenDöşeme(noktalar: Dizi[Nokta]): Dizi[Üçgen] = builtins.triangulate(noktalar)

  def evDizini = builtins.homeDir
  def buDizin = builtins.currentDir
  def kurulumDizini = builtins.installDir
  def yazıyüzleri = builtins.availableFontNames
  def yazıyüzü(adı: Yazı, boyu: Sayı) = builtins.Font(adı, boyu)
  def yazıyüzü(adı: Yazı, boyu: Sayı, biçem: Sayı) = builtins.Font(adı, biçem, boyu)
  def yazıÇerçevesi(yazı: Yazı, yazıBoyu: Sayı, yazıAdı: Yazı = null): Dikdörtgen = builtins.textExtent(yazı, yazıBoyu, yazıAdı)

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

  def müzikMp3üÇalıyorMu = richBuiltins.isMp3Playing
  def müzikÇalıyorMu = richBuiltins.isMusicPlaying
  def müzikMp3üKapat() = richBuiltins.stopMp3()
  def müzikMp3DöngüsünüKapat() = richBuiltins.stopMp3Loop()
  def müziğiKapat() = richBuiltins.stopMusic()
  def yeniMp3Çalar = richBuiltins.newMp3Player

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
    def boy:Kesir = ta.height
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

  def oyunSüresiniGöster(süreSaniyeOlarak: Sayı, mesaj: Yazı, renk: Renk = siyah, yazıBoyu: Sayı = 15, kx: Kesir = 10, ky: Kesir = 50) =
    richBuiltins.showGameTime(süreSaniyeOlarak, mesaj, renk, yazıBoyu, kx, ky)

  def sırayaSok(kaçSaniyeSonra: Kesir)(komut: => Birim) = richBuiltins.schedule(kaçSaniyeSonra)(komut)
  def sırayaSok(n: Sayı, kaçSaniyeSonra: Kesir)(komut: => Birim) = richBuiltins.scheduleN(n, kaçSaniyeSonra)(komut)

  type Yöney2B = tr.Yöney2B
  type Resim = tr.Resim
  type İmge = tr.İmge
  type Bellekteİmge = tr.Bellekteİmge
  type Bellekteİmgeİşlemi = tr.Bellekteİmgeİşlemi
  val Yöney2B = tr.Yöney2B
  val Resim = tr.Resim

  import tr.{res => r}
  val (döndür, döndürMerkezli, filtre, gürültü, örgü) = (r.döndür _, r.döndürMerkezli _, r.filtre _, r.gürültü _, r.örgü _)
  val (büyütXY, saydamlık, ton, parlaklık, aydınlık, götür, kaydır, yansıtY, yansıtX, eksenler, boyaRengi, kalemRengi, kalemBoyu, çizimÖncesiİşlev, çizimSonrasıİşlev, çevir, yansıt, soluk, bulanık, noktaIşık, sahneIşığı) = (r.büyütXY _, r.saydamlık _, r.ton _, r.parlaklık _, r.aydınlık _, r.götür _, r.kaydır _, r.yansıtY, r.yansıtX, r.eksenler, r.boyaRengi _, r.kalemRengi _, r.kalemBoyu _, r.çizimÖncesiİşlev _, r.çizimSonrasıİşlev _, r.çevir _, r.yansıt _, r.soluk _, r.bulanık _, r.noktaIşık _, r.sahneIşığı _)
  def büyüt(oran: Kesir) = r.büyüt(oran)
  def büyüt(xOranı: Kesir, yOranı: Kesir) = r.büyüt(xOranı, yOranı)
  def ışıklar(ışıklar: com.jhlabs.image.LightFilter.Light*) = r.ışıklar(ışıklar: _*)
  def birEfekt(isim: Symbol, özellikler: Tuple2[Symbol, Any]*) = r.birEfekt(isim, özellikler: _*)
  def NoktaIşık(x: Kesir, y: Kesir, yön: Kesir, yükseklik: Kesir, uzaklık: Kesir) = r.NoktaIşık(x, y, yön, yükseklik, uzaklık)
  def SahneIşığı(x: Kesir, y: Kesir, yön: Kesir, yükseklik: Kesir, uzaklık: Kesir) = r.SahneIşığı(x, y, yön, yükseklik, uzaklık)
  def çiz(r2: Resim) = r.çiz(r2)
  def çiz(rler: Resim*) = r.çiz(rler: _*)
  def çiz(rler: collection.Seq[Resim]) = r.çiz(rler)
  def çizVeSakla(resimler: Resim*) = richBuiltins.drawAndHide(resimler.map(_.p): _*)
  val (çizMerkezde, çizSahne, çizMerkezdeYazı, merkezeTaşı) = (r.çizMerkezde _, r.çizSahne _, r.çizMerkezdeYazı _, r.merkezeTaşı _)
  val (sahneKenarındanYansıtma, engeldenYansıtma) = (r.sahneKenarındanYansıtma _, r.engeldenYansıtma _)

  def imge(boy: Sayı, en: Sayı) = r.imge(boy, en)
  def imge(dosya: Yazı) = r.imge(dosya)
  def imge(url: java.net.URL) = r.imge(url)
  val (imgeNoktası, imgeNoktasınıKur) = (r.imgeNoktası _, r.imgeNoktasınıKur _)

  import tr.arayuz
  val ay = arayuz
  /*
  type Parça = ay.Parça
  type Sıra = ay.Sıra
  type Satır = ay.Satır
  type Sütun = ay.Sütun
  val Sütun = ay.Sütun
  type Yazıgirdisi[T] = ay.Yazıgirdisi[T]
  val Yazıgirdisi = ay.Yazıgirdisi
  type Yazıalanı = ay.Yazıalanı
  type Tanıt = ay.Tanıt
  type Düğme = ay.Düğme
  type Açkapa = ay.Açkapa
  type Salındıraç[T] = ay.Salındıraç[T] 
  type Kaydıraç = ay.Kaydıraç
   */

  def zamanTut(başlık: Yazı = "Zaman ölçümü:")(işlev: => Birim)(bitiş: Yazı = "sürdü."): Birim = { // timeit in Builtins.scala
    val t0 = buSaniye
    işlev
    val delta = buSaniye - t0
    println(f"$başlık $delta%.3f saniye $bitiş")
  }

  // more to come (:-)
}

object TurkishInit {
  def init(builtins: CoreBuiltins): Unit = {
    //initialize unstable values:
    TurkishAPI.builtins = builtins
    tr.builtins = builtins
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

