/*
 * Copyright (C) 2022
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

trait GeoMethodsInTurkish {
  implicit class RectYöntemleri(d: Dikdörtgen) {
    def boyu = d.height
    def eni  = d.width
    // todo: more to come
  }

  // https://docs.oracle.com/javase/10/docs/api/java/awt/geom/GeneralPath.html
  implicit class GeoYolYöntemleri(yol: GeoYol) {
    def kondur(x: Kesir, y: Kesir) = yol.moveTo(x, y)
    def doğruÇiz(x: Kesir, y: Kesir) = yol.lineTo(x, y)
    def eğriÇiz(x: Kesir, y: Kesir, araX: Kesir, araY: Kesir) = yol.quadTo(x, y, araX, araY)
    def yayÇiz(x: Kesir, y: Kesir, yayınAçısı: Kesir) = {
      // arc comes from ../../../core/Rich2DPath.scala
      // see: implicit def p2rp(path: GeneralPath): Rich2DPath = new Rich2DPath(path) ../../Builtins.scala
      val rp = new net.kogics.kojo.core.Rich2DPath(yol)
      rp.arc(x, y, yayınAçısı)
    }
    def başaDön() = yol.closePath()
    // todo: more to come
  }

  // ../../../core/vertexShapeSupport.scala
  implicit class GeoNoktaYöntemleri(gn: GeoNokta) {
    def başla() = gn.beginShape()
    def bitir() = gn.endShape()
    def nokta(x: Kesir, y: Kesir) = gn.vertex(x, y)
    def ikinciDereceNokta(mx: Kesir, my: Kesir, x2: Kesir, y2: Kesir) = gn.quadraticVertex(mx, my, x2, y2)
    def bezierNoktası(mx1: Kesir, my1: Kesir, mx2: Kesir, my2: Kesir, x2: Kesir, y2: Kesir) = gn.bezierVertex(mx1, my1, mx2, my2, x2, y2)
    def eğriNoktası(x: Kesir, y: Kesir) = gn.curveVertex(x, y)
    def açısalNokta(boyu: Kesir, açısı: Kesir) = gn.vertexRt(boyu, açısı)
    def açısalEğriNoktası(boyu: Kesir, açısı: Kesir) = gn.curveVertexRt(boyu, açısı)
  }
}
