/*
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo

import java.awt.image.BufferedImage
import java.io.File
import java.net.URL

import net.kogics.kojo.core.{Picture, Point, SCanvas}
import net.kogics.kojo.util.Utils
import org.mapeditor.core.{MapLayer, TileLayer, Tile => MapTile}

package object tiles {

  case class SpriteSheet(fileName: String, tileX: Int, tileY: Int) {
    import java.awt.image.BufferedImage
    val sheet = {
      if (fileName.startsWith("http")) {
        Utils.loadUrlImageC(new URL(fileName))
      }
      else {
        Utils.loadBufImage(fileName)
      }
    }

    def imageAt(x: Int, y: Int): BufferedImage = {
      sheet.getSubimage(x * tileX, y * tileY, tileX, tileY)
    }
  }

  case class TileXY(x: Int, y: Int)

  // Kojo drawing specific view of a level in a tmx file
  case class Level(world: TileWorld)(implicit canvas: SCanvas) {
    // Create list of layers for map
    val layers = (0 until world.layerCount).map { layerIdx => Layer(layerIdx, world) }

    def draw(): Unit = {
      layers.foreach { layer =>
        layer.draw()
      }
    }
  }

  // Kojo drawing specific view of a layer in a level
  case class Layer(index: Int, world: TileWorld)(implicit canvas: SCanvas) {
    val mapLayer = world.layerAt(index) match {
      case tl: TileLayer => tl
      case _@ l          => println(s"Ignoring non-tile layer: $l"); null
    }

    val tiles = Array.ofDim[Tile](world.height, world.width)

    if (mapLayer != null) {
      for (y <- 0 until world.height) {
        for (x <- 0 until world.width) {
          val img = {
            val tile = mapLayer.getTileAt(x, y)
            if (tile != null) tile.getImage else null
          }

          if (img != null) {
            val kxy = world.tileToKojo(TileXY(x, y))
            tiles(y)(x) = Tile(kxy.x, kxy.y, x, y, img)
          }
        }
      }
    }

    def draw(): Unit = {
      for (y <- 0 until world.height) {
        for (x <- 0 until world.width) {
          val tile = tiles(y)(x)
          if (tile != null) {
            tile.draw()
          }
        }
      }
    }

    def removeTileAt(txy: TileXY): Unit = {
      val tile = tiles(txy.y)(txy.x)
      if (tile != null) {
        tile.tilePic.erase()
        tiles(txy.y)(txy.x) = null
      }
      else {
        println("Trying to remove non-existent tile.")
      }
    }
  }

  // Kojo picture tile
  case class Tile(kx: Double, ky: Double, tx: Int, ty: Int, image: BufferedImage)(implicit canvas: SCanvas) {
    val tilePic = picture.image(image, None)
    tilePic.setPosition(kx, ky)
    def draw(): Unit = {
      tilePic.draw()
    }
  }

  class TileWorld(fileName: String)(implicit canvas: SCanvas) {

    val absolutePath = {
      val p = Utils.absolutePath(fileName)
      // forward slashes don't work on Windows for linked tilesets (with Tiled loader)
      if (File.separatorChar == '\\') p.replace("/", "\\") else p
    }

    import org.mapeditor.io.TMXMapReader
    val mr = new TMXMapReader
    val tiledMap = mr.readMap(absolutePath)
    val layerCount = tiledMap.getLayerCount
    val tileHeight = tiledMap.getTileHeight
    val tileWidth = tiledMap.getTileWidth
    val width = tiledMap.getWidth
    val height = tiledMap.getHeight

    // drawing specific view of the level
    val currentLevel = Level(this)

    def layerAt(idx: Int): MapLayer = tiledMap.getLayer(idx)

    def tileToKojo(xy: TileXY): Point = {
      val kx = xy.x * tileWidth
      val ky = -(xy.y + 1) * tileHeight
      Point(kx, ky)
    }

    def kojoToTile(x: Double, y: Double): TileXY = {
      val tx = (x / tileWidth).toInt
      val ty = (-y / tileHeight).toInt
      TileXY(tx, ty)
    }

    def tileAt(xy: TileXY, layerIdx: Int): MapTile =
      layerAt(layerIdx).asInstanceOf[TileLayer].getTileAt(xy.x, xy.y)

    def tileAtLeft(pic: Picture, layerIdx: Int) = {
      val b = pic.bounds
      val txy = kojoToTile(b.x, b.y + b.height / 2)
      tileAt(txy, layerIdx)
    }

    def hasTileAtLeft(pic: Picture, layerIdx: Int): Boolean = {
      tileAtLeft(pic, layerIdx) != null
    }

    def tileAtRight(pic: Picture, layerIdx: Int): MapTile = {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width, b.y + b.height / 2)
      tileAt(txy, layerIdx)
    }

    def hasTileAtRight(pic: Picture, layerIdx: Int): Boolean = {
      tileAtRight(pic, layerIdx) != null
    }

    def tileAbove(pic: Picture, layerIdx: Int): MapTile = {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width / 2, b.y + b.height)
      tileAt(txy, layerIdx)
    }

    def hasTileAbove(pic: Picture, layerIdx: Int): Boolean = {
      tileAbove(pic, layerIdx) != null
    }

    def tileBelow(pic: Picture, layerIdx: Int): MapTile = {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width / 2, b.y)
      tileAt(txy, layerIdx)
    }

    def hasTileBelow(pic: Picture, layerIdx: Int): Boolean = {
      tileBelow(pic, layerIdx) != null
    }

    def tileUnder(pic: Picture, layerIdx: Int): MapTile = {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width / 2, b.y + b.height / 2)
      tileAt(txy, layerIdx)
    }

    def hasTileUnder(pic: Picture, layerIdx: Int): Boolean = {
      tileUnder(pic, layerIdx) != null
    }

    def moveToTileLeft(pic: Picture): Unit = {
      val b = pic.bounds
      val picp = pic.position
      val delta = picp.x - b.x
      // delta is non-zero for vector pictures with a non-zero pen width
      // here the assumptions is that deltax and deltay are the same, so we just use deltax
      // the above assumption applies to pictures made in the first quadrant (top-right from origin)
      val txy = kojoToTile(b.x + b.width, b.y + b.height / 2)
      val b2xy = tileToKojo(TileXY(txy.x - 1, txy.y))
      val widthDiff = b.width - tileWidth
      pic.setPosition(b2xy.x - widthDiff + delta, b.y + delta)
    }

    def moveToTileRight(pic: Picture): Unit = {
      val b = pic.bounds
      val picp = pic.position
      val delta = picp.x - b.x
      val txy = kojoToTile(b.x, b.y + b.height / 2)
      val b2xy = tileToKojo(TileXY(txy.x + 1, txy.y))
      pic.setPosition(b2xy.x + delta, b.y + delta)
    }

    def moveToTileAbove(pic: Picture): Unit = {
      val b = pic.bounds
      val picp = pic.position
      val delta = picp.x - b.x
      val txy = kojoToTile(b.x + b.width / 2, b.y)
      val b2xy = tileToKojo(TileXY(txy.x, txy.y - 1))
      pic.setPosition(b.x + delta, b2xy.y + delta)
    }

    def moveToTileBelow(pic: Picture): Unit = {
      val b = pic.bounds
      val picp = pic.position
      val delta = picp.x - b.x
      val txy = kojoToTile(b.x + b.width / 2, b.y + b.height)
      val b2xy = tileToKojo(TileXY(txy.x, txy.y + 1))
      val heightDiff = b.height - tileHeight
      pic.setPosition(b.x + delta, b2xy.y - heightDiff + delta)
    }

    def removeTileAt(xy: TileXY, layerIdx: Int): Unit =
      layerAt(layerIdx).asInstanceOf[TileLayer].setTileAt(xy.x, xy.y, null)

    def removeTileUnder(pic: Picture, layerIdx: Int): Unit = {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width / 2, b.y + b.height / 2)
      // remove tiled tile
      removeTileAt(txy, layerIdx)
      // remove picture tile
      currentLevel.layers(layerIdx).removeTileAt(txy)
    }

    def step(): Unit = {
    }

    // Draw level, player, overlay
    def draw(): Unit = {
      currentLevel.draw()
    }
  }

}
