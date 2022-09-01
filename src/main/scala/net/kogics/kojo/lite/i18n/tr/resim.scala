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
// translating picture utility

package net.kogics.kojo.lite.i18n.tr

import net.kogics.kojo.picture
import java.awt.Paint

import net.kogics.kojo.lite.{CoreBuiltins, Builtins}
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.lite.i18n.TurkishAPI.Kaplumbağa

// this is to hold names to go to top level of TurkishAPI (and kojo editor)
object res {
  // ../../../picture/package.scala
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
  def götür(n: Nokta) = Transc(n.x, n.y)
  def götür(yy: Yöney2B) = Transc(yy.v.x, yy.v.y)
  def kaydır(x: Kesir, y: Kesir) = Offsetc(x, y)
  def kaydır(n: Nokta) = Offsetc(n.x, n.y)
  def kaydır(yy: Yöney2B) = Offsetc(yy.v.x, yy.v.y)
  def yansıtY = FlipYc
  def yansıtX = FlipXc
  def eksenler = AxesOnc
  def boyaRengi(r: Boya) = Fillc(r)
  def kalemRengi(r: Boya) = Strokec(r)
  def kalemBoyu(b: Kesir) = StrokeWidthc(b)  // penThickness setPenThickness
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
  // ../../../core/Picture.scala

  def çiz(r: Resim) = Resim.çiz(r)
  def çiz(rler: Resim*) = richBuiltins.draw(rler.map(_.p): _*)
  def çiz(rler: collection.Seq[Resim]) = richBuiltins.draw(rler.map(_.p))
  def çizMerkezde(r: Resim) = richBuiltins.drawCentered(r.p)
  def çizSahne(boya: Paint) = richBuiltins.tCanvas.drawStage(boya)
  def çizMerkezdeYazı(mesaj: Yazı, renk: Renk, yazıBoyu: Sayı) = richBuiltins.drawCenteredMessage(mesaj, renk, yazıBoyu)
  def merkezeTaşı(resim: Resim) = richBuiltins.center(resim.p)

  def sahneKenarındanYansıtma(r: Resim, yöney: Yöney2B): Yöney2B =
    Yöney2B(richBuiltins.bouncePicOffStage(r.p, yöney.v))
  def engeldenYansıtma(r: Resim, yöney: Yöney2B, engel: Resim): Yöney2B =
    Yöney2B(richBuiltins.bouncePicOffPic(r.p, yöney.v, engel.p))

  def imge(boy: Sayı, en: Sayı) = builtins.image(boy, en)
  def imge(dosya: Yazı) = builtins.image(dosya)
  def imge(url: java.net.URL) = builtins.image(url)
  def imgeNoktası(imge: Bellekteİmge, x: Sayı, y: Sayı) = builtins.getImagePixel(imge, x, y)
  def imgeNoktasınıKur(imge: Bellekteİmge, x: Sayı, y: Sayı, r: Renk) = builtins.setImagePixel(imge, x, y, r)

  //
  // interface above
  // internals stuff below
  //

  // ../../../picture/transforms.scala
  abstract class ComposableTransformer extends Function1[Resim, Resim] { outer =>
    def apply(r: Resim): Resim
    def -> (r: Resim) = apply(r)
    def *(digeri: ComposableTransformer) = new ComposableTransformer {
      def apply(r: Resim): Resim = outer.apply(digeri.apply(r))
    }
  }
  // ../../../picture/picimage.scala
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
  // ../../../picture/effects.scala
  case class Spinc(n: Int) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Spin(n)(r.p)) }
  case class Reflectc(n: Int) extends ComposableTransformer { def apply(r: Resim) = new Resim(picture.Reflect(n)(r.p)) }
  // ../../../picture/picimage.scala
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

}

// ../../../core/Picture.scala
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
  def götür(n: Nokta) = p.translate(n.x, n.y)
  def götür(yy: Yöney2B) = p.translate(yy.v.x, yy.v.y)
  def hızınıDönüştür(yy: Yöney2B) = p.transv(yy.v)
  def kaydır(x: Kesir, y: Kesir) = p.offset(x, y)
  def kaydır(n: Nokta) = p.offset(n.x, n.y)
  def kaydır(yy: Yöney2B) = p.offset(yy.v.x, yy.v.y)
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
  def çarpışma(başkaları: Seq[Resim]): Option[Resim] =
    başkaları.find {this çarpıştı _}
  def kesişim(başkaResim: Resim): com.vividsolutions.jts.geom.Geometry =
    p.intersection(başkaResim.p)
  def içinde(başkaResim: Resim) = p.contains(başkaResim.p)
  def uzaklık(başkaResim: Resim) = p.distanceTo(başkaResim.p)
  def alan = p.area
  def çevre = p.perimeter
  def geometri = p.picGeom
  def konum = p.position
  def kondur(n: Nokta) = p.setPosition(n)
  def kondur(x: Kesir, y: Kesir) = p.setPosition(x, y)
  def konumuKur(n: Nokta) = p.setPosition(n)
  def konumuKur(x: Kesir, y: Kesir) = p.setPosition(x, y)
  def doğrultu = p.heading
  val doğrultuyuKur = açıyaDön _
  def açıyaDön(açı: Kesir) = p.setHeading(açı)
  def büyütmeOranı: (Kesir, Kesir) = p.scaleFactor
  def büyütmeOranınıKur(x: Kesir, y: Kesir) = p.setScaleFactor(x, y)
  def büyütmeyiKur(oran: Kesir) = p.setScale(oran)
  def dönüşüm = p.transform
  def kalemRenginiKur = p.setPenColor _
  def kalemKalınlığınıKur(kalınlık: Sayı) = p.setPenThickness(kalınlık)
  def kalemBoyunuKur(kalınlık: Sayı) = p.setPenThickness(kalınlık)  
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
  // ../../../picture/pics.scala
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
// ../../Builtins.scala  object Picture
object Resim {
  type Yazıyüzü = java.awt.Font
  def apply(işlev: => Birim): Resim = new Resim(richBuiltins.Picture(işlev))
  def çiz(r: Resim) = richBuiltins.draw(r.p)
  def düz(en: Kesir, boy: Kesir) = new Resim(richBuiltins.Picture.line(en, boy))
  def köşegen(en: Kesir, boy: Kesir) = new Resim(richBuiltins.Picture.line(en, boy))
  def yay(yarıçap: Kesir, açı: Kesir) = new Resim(richBuiltins.Picture.arc(yarıçap, açı))
  def daire(yarıçap: Kesir) = new Resim(richBuiltins.Picture.circle(yarıçap))
  def elips(xYarıçapı: Kesir, yYarıçapı: Kesir) = new Resim(richBuiltins.Picture.ellipse(xYarıçapı, yYarıçapı))
  def elipsDikdörtgenİçinde(en: Kesir, boy: Kesir) = new Resim(richBuiltins.Picture.ellipseInRect(en, boy))
  def yatay(boy: Kesir) = new Resim(richBuiltins.Picture.hline(boy))
  def dikey(boy: Kesir) = new Resim(richBuiltins.Picture.vline(boy))
  def dikdörtgen(en: Kesir, boy: Kesir) = new Resim(richBuiltins.Picture.rect(boy, en)) // they are swapped!
                                                                                        // ../../../Picture/package.scala
  def satır(r: => Resim, kaçTane: Sayı) = new Resim(picture.row(r.p, kaçTane))
  def sütun(r: => Resim, kaçTane: Sayı) = new Resim(picture.col(r.p, kaçTane))
  def yazı(içerik: Her, yazıBoyu: Sayı=15) = new Resim(richBuiltins.Picture.text(içerik, yazıBoyu))
  def yazı(içerik: Her, yazıyüzü: Yazıyüzü) = new Resim(richBuiltins.Picture.text(içerik, yazıyüzü))
  def yazı(içerik: Her, yazıyüzü: Yazıyüzü, renk: Renk) = new Resim(richBuiltins.Picture.textu(içerik, yazıyüzü, renk))
  def yazıRenkli(içerik: Her, yazıBoyu: Sayı, renk: Renk) = new Resim(richBuiltins.Picture.textu(içerik, yazıBoyu, renk))
  def imge(dosyaAdı: Yazı) = new Resim(richBuiltins.Picture.image(dosyaAdı))
  def imge(dosyaAdı: Yazı, zarf: Resim) = new Resim(richBuiltins.Picture.image(dosyaAdı, zarf.p))
  def imge(url: java.net.URL) = new Resim(richBuiltins.Picture.image(url))
  def imge(url: java.net.URL, zarf: Resim) = new Resim(richBuiltins.Picture.image(url, zarf.p))
  def imge(imge: İmge) = new Resim(richBuiltins.Picture.image(imge))
  def imge(imge: İmge, zarf: Resim) = new Resim(richBuiltins.Picture.image(imge, zarf.p))
  // Resim.düğme("Merhaba")(println(kg.x))
  def düğme(ad: Yazı)(işlev: => Birim) = new Resim(richBuiltins.Picture.button(ad)(işlev))
  // Resim.arayüz(Label("Merhaba"))
  // Resim.arayüz(Button("Merhaba")(println("Selam!")))
  def arayüz(parça: javax.swing.JComponent) = new Resim(richBuiltins.Picture.widget(parça))
  def yatayBoşluk(en:  Kesir) = new Resim(richBuiltins.Picture.hgap(en))
  def dikeyBoşluk(boy: Kesir) = new Resim(richBuiltins.Picture.vgap(boy))
  def yoldan(işlev: GeoYol => Birim) = new Resim(richBuiltins.Picture.fromPath(işlev))
  def noktadan(işlev: GeoNokta => Birim) = new Resim(richBuiltins.Picture.fromVertexShape(işlev))
  def kaplumbağadan(işlev: Kaplumbağa => Birim) = { // todo: does this work?
    val f = new Function1[Turtle, Unit] { def apply(t: Turtle): Unit = işlev(new Kaplumbağa(t)) }
    new Resim(richBuiltins.Picture.fromTurtle(f))
  }
  def tuvalden(en: Kesir, boy: Kesir)(işlev: Grafik2B => Birim) = new Resim(richBuiltins.Picture.fromCanvas(en, boy)(işlev))
  def karalamadan() = ??? // ../../Builtins.scala  ~/src/kojo/git/kojo/src/main/scala/net/kogics/kojo/lite/Builtins.scala
  def eksenleriGöster(r: Resim) = richBuiltins.Picture.showAxes(r.p)
  def eksenleriGöster(resimler: Resim*) = richBuiltins.Picture.showAxes(resimler.map(_.p): _*)
  def sınırlarıGöster(r: Resim) = richBuiltins.Picture.showBounds(r.p)
  def sınırlarıGöster(resimler: Resim*) = richBuiltins.Picture.showBounds(resimler.map(_.p): _*)

  def tuval = tuvalSınırları  // stageBorder
  def tuvalinSınırları = tuvalSınırları
  def tuvalSınırları = new Resim(richBuiltins.tCanvas.stage)
  def tuvalinSolu = new Resim(richBuiltins.tCanvas.stageLeft)
  def tuvalinSağı = new Resim(richBuiltins.tCanvas.stageRight)
  def tuvalinTavanı = new Resim(richBuiltins.tCanvas.stageTop)
  def tuvalinTabanı = new Resim(richBuiltins.tCanvas.stageBot)
  def tuvalBölgesi = new Resim(richBuiltins.tCanvas.stageArea)

  /* old                  new
   =====                  ===
   resimleriSil           Resim.sil
   resimDizisi            Resim.dizi
   resimDikeyDizi         Resim.diziDikey
   resimYatayDizi         Resim.diziYatay
   resimDüzenliDizi       Resim.diziDüzenli
   resimDikeyDüzenliDizi  Resim.diziDikeyDüzenli
   resimYatayDüzenliDizi  Resim.diziYatayDüzenli
   resimKümesi            Resim.küme
   resmiSüz               Resim.süz
   resimDosyasınıSüz      Resim.süz   */
  def sil() = richBuiltins.tCanvas.erasePictures()
  def dizi(rd: Resim*) = new Resim(richBuiltins.picStack(rd.map(_.p)))
  def diziDikey(rd: Resim*) = new Resim(richBuiltins.picCol(rd.map(_.p)))
  def diziYatay(rd: Resim*) = new Resim(richBuiltins.picRow(rd.map(_.p)))
  def diziDüzenli(rd: Resim*) = new Resim(richBuiltins.picStackCentered(rd.map(_.p)))
  def diziDikeyDüzenli(rd: Resim*) = new Resim(richBuiltins.picColCentered(rd.map(_.p)))
  def diziYatayDüzenli(rd: Resim*) = new Resim(richBuiltins.picRowCentered(rd.map(_.p)))
  def küme(rd: Resim*) = new Resim(richBuiltins.picBatch(rd.map(_.p)))
  def dizi(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picStack(rd.map(_.p)))
  def diziDikey(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picCol(rd.map(_.p)))
  def diziYatay(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picRow(rd.map(_.p)))
  def diziDüzenli(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picStackCentered(rd.map(_.p)))
  def diziDikeyDüzenli(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picColCentered(rd.map(_.p)))
  def diziYatayDüzenli(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picRowCentered(rd.map(_.p)))
  def küme(rd: collection.Seq[Resim]) = new Resim(richBuiltins.picBatch(rd.map(_.p)))
  def süz(r: Resim, süzgeç: Bellekteİmgeİşlemi): Resim = new Resim(richBuiltins.filterPicture(r.p, süzgeç))
  def süz(rd: Bellekteİmge, süzgeç: Bellekteİmgeİşlemi) = richBuiltins.filterImage(rd, süzgeç)
  // todo: more
}
