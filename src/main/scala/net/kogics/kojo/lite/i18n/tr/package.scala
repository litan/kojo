/*
 * Copyright (C) 2021
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

package net.kogics.kojo.lite.i18n

import java.awt.Color
import java.awt.Paint

import io.github.jdiemke.triangulation.Triangle2D
import net.kogics.kojo.core.Cm
import net.kogics.kojo.core.Fast
import net.kogics.kojo.core.Inch
import net.kogics.kojo.core.Medium
import net.kogics.kojo.core.Pixel
import net.kogics.kojo.core.Point
// ../../../core/shapes.scala
// ~/src/kojo/git/kojo/src/main/scala/net/kogics/kojo/core/shapes.scala
import net.kogics.kojo.core.Rectangle
import net.kogics.kojo.core.Slow
import net.kogics.kojo.core.Speed
import net.kogics.kojo.core.SuperFast
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.core.UnitLen
import net.kogics.kojo.core.VertexShape
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.util.Utils

package object tr {
  lazy val isTurkish = "tr" == System.getProperty("user.language")
  var builtins: CoreBuiltins = _ // unstable reference to module
  lazy val richBuiltins = builtins.asInstanceOf[Builtins]

  // some type aliases in Turkish -- Ctrl-t to return type info will also be in turkish
  type Nesne = Object
  type Birim = Unit
  type Her   = Any
  type HerDeğer  = AnyVal
  type HerGönder = AnyRef  // Gönderge, gönderme todo...
  type Yok   = Null
  type Hiç   = Nothing

  type Boya = Paint
  type Renk = Color

  type Hız  = Speed
  type Nokta = Point
  type Dikdörtgen = Rectangle
  type Üçgen = Triangle2D

  // Ref: https://docs.scala-lang.org/overviews/scala-book/built-in-types.html
  type İkil = Boolean
  type Seçim = Boolean
  // no type for Bit. But if there were, how about Parçacık?

  // duplicated from sayi.scala as they are used in other traits
  type Lokma   = Byte
  type Kısa    = Short
  type Sayı    = Int
  type Uzun    = Long
  type İriSayı = BigInt
  type UfakKesir = Float
  type Kesir = Double
  type İriKesir = BigDecimal

  type Diz[B] = collection.Seq[B]
  type Dizi[B] = Seq[B]
  type Dizin[A] = List[A]
  // Aralık artık bir tür takma adı (bkz. aralik.scala); eskiden Range'i saran
  // bir case class'tı ve `1 |-| 10` ile `Aralık(1, 11)` farklı yöntemler görüyordu.
  type Aralık = Range
  // Yığın da bir tür takma adı (bkz. kuyruk.scala); eskiden Stack'i saran bir
  // case class'tı ve Diz sarmalayıcısının yöntemlerini hiç görmüyordu.
  type Yığın[T] = collection.mutable.Stack[T]
  type SıralıDizi[A] = IndexedSeq[A]
  type Yineleyici[Col] = Iterator[Col]
  type Yinelenebilir[Col] = Iterable[Col]
  type YinelenebilirBirKere[Col] = IterableOnce[Col]
  type Yapıcıdan[DiziTürü, GirdiTürü, ÇıktıTürü] = collection.BuildFrom[DiziTürü, GirdiTürü, ÇıktıTürü]
  type UzunlukBirimi = UnitLen

  // ../../../core/Picture.scala
  type Biçim = java.awt.Shape
  // ../../../core/vertexShapeSupport.scala
  type GeoYol = java.awt.geom.GeneralPath
  type GeoNokta = VertexShape
  type Grafik2B = scala.swing.Graphics2D
  object Nokta {
    type Kesir = Double
    def apply(x: Kesir, y: Kesir): Nokta = new Nokta(x, y)
    def unapply(p: Nokta) = Some((p.x, p.y))
  }

  val (yavaş, orta, hızlı, çokHızlı) = (Slow, Medium, Fast, SuperFast)
  val (noktaSayısı, santim, inç) = (Pixel, Cm, Inch)

  type İmge = richBuiltins.Image // java.awt.Image

  // duplicate in turler.scala
  type Bellekteİmge = java.awt.image.BufferedImage
  type Bellekteİmgeİşlemi = java.awt.image.BufferedImageOp
  type İmgeİşlemi = net.kogics.kojo.picture.ImageOp

  type Yazı = String // duplicate in yazi.scala

  // Note: also add any new types in ../trInit.scala and also turler.scala in kilavuz

  class Mp3Çalar(p: net.kogics.kojo.music.KMp3) {
    def çalıyorMu = p.isMp3Playing
    def sesMp3üÇal(mp3dosyası: Yazı) = p.playMp3Sound(mp3dosyası)
    def çal(mp3dosyası: Yazı) = p.playMp3(mp3dosyası)
    def durdur() = p.stopMp3()
    
    def önyükle(mp3dosyası: Yazı) = p.preloadMp3(mp3dosyası)
    
    def döngülüÇal(mp3dosyası: Yazı) = p.playMp3Loop(mp3dosyası)
    def döngüyüDurdur() = p.stopMp3Loop()
  }

  // used in ~/kojo-repo/src/main/scala/net/kogics/kojo/xscala/CodeCompletionUtils.scala
  // ../../../xscala/CodeCompletionUtils.scala
  val turkishKeywordTemplates = if (!isTurkish) Map() else tr.templates.keywordTemplates
  private val _trKeywords = List(
    "baskın", "bazı", "bildir", "birlikte", "bu",
    "damgalı", "den", "dene","deste", "dez", "doğru", "durum",
    "eğer", "eşle",    "geriDön", "getir", "gizli",
    "için", "koru", "miskin", "nesne",    "örtük", "özellik",
    "son", "sonunda", "soyut", "sınıf",    "tanım", "tür",
    "üst", "ver", "verilen",
    "yakala", "yanlış", "yap", "yayar",
    "yeni", "yineleDoğruKaldıkça", "yok", "yoksa"
  )
  val turkishKeywords = if (!isTurkish) List() else _trKeywords
  // Raw, locale-independent editor data registered by TurkishInit.preInit().
  val keywordList: List[String] = _trKeywords
  val keywordTemplateMap: Map[String, String] = templates.keywordTemplates
  val codeTemplateMap: Map[String, String] = templates.codeTemplatesRaw
  val trKeywordSet = turkishKeywords.toSet
  // used in ../../../lexer/ScalariformTokenMaker.scala
  def isTurkishKeyword(word: String) = trKeywordSet.contains(word) || (TurkishAPI.testTrKeywords && _trKeywords.toSet.contains(word))

  // used in .translate.scala and in ../trInit.scala
  object Yardımcı {
    val y = net.kogics.kojo.util.Utils
    val kütüpDizini =  y.libDir
    val eklentiDizini = y.extensionsDir
    // Yardımcı.yerelYazı("S_Loading") == "Yükleniyor..."
    def yerelYazı(ingilizceAdı: Yazı): Yazı = y.loadString(ingilizceAdı)
    // todo more...
  }

  // used to update result in the OutputPane and worksheet (codePane):
  //   ../../OutputPane.scala
  //   ../../CodeExecutionSupport.scala
  // todo: worksheet: val x = "String"
  // todo: println("String")
  def updateResult(str: String): String = if (!isTurkish) str else translate.result(str)
  // used in ../../ScriptEditor.scala to translate type information (Ctrl-t)
  // also used in ../../KojoCompletionProvider.scala to translate type info of completions (Ctrl-space)
  def updateTypes(str: String): String = if (!isTurkish) str else translate.typeInfo(str)

  // ../../KojoCompletionProvider.scala kullanıyor.
  //
  // Tamamlama açılır listesinde yardım metni yalnız "bilinen sahip"lerin
  // üyeleri için gösteriliyor. Türkçe koleksiyon yöntemlerinin sahibi ise
  // örtük sarmalayıcı sınıflar -- ölçtük:
  //   Dizi(1,2,3).katla   -> net.kogics.kojo.lite.i18n.tr.SeqMethodsInTurkish.SeqYöntemleri
  //   Yığın(1,2).çekHepsini -> net.kogics.kojo.lite.i18n.tr.StackMethodsInTurkish.YığınYöntemleri
  //   Eşlem("a" -> 1).eşEkle -> net.kogics.kojo.lite.i18n.tr.Eşlem
  // Hiçbiri knownOwners listesinde değil, o yüzden help.scala'ya yazılan
  // metinler görünmüyordu. Sınıf sınıf saymak yerine desteye bakıyoruz:
  // tr destesinden gelen her üye Türkçe yardımı hak ediyor.
  // Yerel bağımsız kısım -- testten böyle çağrılıyor (isTurkish bir lazy val ve
  // sistem özelliğine bakıyor; testte onu kurcalamak kırılgan olurdu).
  def trDestesiMi(sahip: Yazı): İkil =
    sahip != null && sahip.startsWith("net.kogics.kojo.lite.i18n.tr.")

  def türkçeSahipMi(sahip: Yazı): İkil = isTurkish && trDestesiMi(sahip)

  // also used in ../../KojoCompletionProvider.scala
  var dumpCompletions = false
}
