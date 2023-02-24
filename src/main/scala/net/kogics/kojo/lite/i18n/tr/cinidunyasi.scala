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
package net.kogics.kojo.lite.i18n.tr

import net.kogics.kojo.core.SCanvas
// ../../../tiles/package.scala
import net.kogics.kojo.tiles

// used in Demo Platformer (Mağara Oyunu in Turkish):
//   ../../../../../../../../../installer/examples/tiledgame/game_tr.kojo.installed
//   ~/src/kojo/git/kojo/installer/examples/tiledgame/game_tr.kojo.installed
object ÇiniDünyası {
  type ÇiniXY = tiles.TileXY
  val ÇiniXY = tiles.TileXY
}

class ÇiniDünyası(dosya: Yazı)(implicit canvas: SCanvas) {
  type ÇiniXY = tiles.TileXY
  val ÇiniXY = tiles.TileXY

  val tw = new tiles.TileWorld(dosya)
  def çinidenKojoya(xy: ÇiniXY): Nokta = tw.tileToKojo(xy)
  def kojodanÇiniye(x: Kesir, y: Kesir): ÇiniXY = tw.kojoToTile(x, y)

  def yukarıdaÇiniVarMı(resim: Resim, düzey: Sayı): İkil = tw.hasTileAbove(resim.p, düzey)
  def soldaÇiniVarMı(resim: Resim, düzey: Sayı): İkil = tw.hasTileAtLeft(resim.p, düzey)
  def sağdaÇiniVarMı(resim: Resim, düzey: Sayı): İkil = tw.hasTileAtRight(resim.p, düzey)
  def aşağıdaÇiniVarMı(resim: Resim, düzey: Sayı): İkil = tw.hasTileBelow(resim.p, düzey)
  def altındaÇiniVarMı(resim: Resim, düzey: Sayı): İkil = tw.hasTileUnder(resim.p, düzey)

  def yukarıdakiÇiniyeTaşı(resim: Resim): Birim = tw.moveToTileAbove(resim.p)
  def aşağıdakiÇiniyeTaşı(resim: Resim): Birim = tw.moveToTileBelow(resim.p)
  def soldakiÇiniyeTaşı(resim: Resim): Birim = tw.moveToTileLeft(resim.p)
  def sağdakiÇiniyeTaşı(resim: Resim): Birim = tw.moveToTileRight(resim.p)

  def birAdımİleri(): Birim = tw.step()
  def çiz(): Birim = tw.draw()

  // todo: more api to translate..
}

case class BirSayfaKostüm(dosya: Yazı, çiniX: Sayı, çiniY: Sayı) {
  val ss = tiles.SpriteSheet(dosya, çiniX, çiniY)
  def resimSeç(x: Sayı, y: Sayı): Bellekteİmge = ss.imageAt(x, y)
}
