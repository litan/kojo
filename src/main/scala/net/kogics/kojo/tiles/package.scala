package net.kogics.kojo

import java.awt.image.BufferedImage

import scala.collection.mutable.ArrayBuffer

import org.mapeditor.core.TileLayer

import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.Point
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.util.Utils

package object tiles {

  case class SpriteSheet(fileName: String, tileX: Int, tileY: Int) {
    import java.awt.image.BufferedImage
    val sheet = Utils.loadBufImage(fileName)

    def imageAt(x: Int, y: Int): BufferedImage = {
      sheet.getSubimage(x * tileX, y * tileY, tileX, tileY)
    }
  }

  case class TileXY(x: Int, y: Int)

  case class Level(world: TileWorld)(implicit canvas: SCanvas) {
    // Create list of layers for map
    val layers = ArrayBuffer.empty[Layer]

    // Create layers for each layer in tile map
    for (layer <- 0 to world.layerCount) {
      layers.append(Layer(index = layer, world))
    }

    def draw() {
      layers.foreach { layer =>
        layer.draw()
      }
    }
  }

  case class Layer(index: Int, world: TileWorld)(implicit canvas: SCanvas) {
    // Create group of tiles for this layer
    val tiles = ArrayBuffer.empty[Tile]
    val mapLayer = world.layer(index)

    // Create tiles in the right position for each layer
    for (x <- 0 until world.width) {
      for (y <- 0 until world.height) {
        val img = mapLayer match {
          case tl: TileLayer =>
            val tile = tl.getTileAt(x, y)
            if (tile != null) tile.getImage else null
          case _ => null
        }
        if (img != null) {
          val kxy = world.tileToKojo(TileXY(x, y))
          tiles += Tile(image = img, x = kxy.x, y = kxy.y)
        }
      }
    }

    // Draw layer
    def draw() {
      tiles.foreach { tile =>
        tile.draw()
      }
    }
  }

  // Tile class with an image, x and y
  case class Tile(image: BufferedImage, x: Double, y: Double)(implicit canvas: SCanvas) {
    val tile = picture.image(image, None)
    tile.setPosition(x, y)
    def draw() {
      tile.draw()
    }
  }

  class TileWorld(fileName: String)(implicit canvas: SCanvas) {
    import org.mapeditor.io.TMXMapReader
    val mr = new TMXMapReader
    val tiledMap = mr.readMap(Utils.absolutePath(fileName))

    val layerCount = tiledMap.getLayerCount
    val tileHeight = tiledMap.getTileHeight
    val tileWidth = tiledMap.getTileWidth
    val width = tiledMap.getWidth
    val height = tiledMap.getHeight

    def layer(idx: Int) = tiledMap.getLayer(idx)

    val yShift = 0
    def tileToKojo(xy: TileXY): Point = {
      val kx = xy.x * tileWidth
      val ky = yShift - (xy.y + 1) * tileHeight + 1
      Point(kx, ky)
    }

    def kojoToTile(x: Double, y: Double): TileXY = {
      //        val pty = (b.y / tileHeight).toInt
      val tx = (x / tileWidth).toInt
      val ty = ((yShift - y) / tileHeight).toInt
      TileXY(tx, ty)
    }

    def tileAt(xy: TileXY, layer: Int) =
      tiledMap.getLayer(layer).asInstanceOf[TileLayer].getTileAt(xy.x, xy.y)

    def tileLeft(pic: Picture, layer: Int) = {
      val b = pic.bounds
      val txy = kojoToTile(b.x, b.y + b.height / 2)
      tileAt(txy, layer) != null
    }

    def tileRight(pic: Picture, layer: Int) = {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width, b.y + b.height / 2)
      tileAt(txy, layer) != null
    }

    def tileAbove(pic: Picture, layer: Int) = {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width / 2, b.y + b.height)
      tileAt(txy, layer) != null
    }

    def tileBelow(pic: Picture, layer: Int) = {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width / 2, b.y)
      tileAt(txy, layer) != null
    }

    def moveToTileLeft(pic: Picture) {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width, b.y + b.height / 2)
      val p = tileToKojo(TileXY(txy.x - 1, txy.y))
      val widthDiff = b.width - tileWidth
      pic.setPosition(p.x - widthDiff, b.y)
    }

    def moveToTileRight(pic: Picture) {
      val b = pic.bounds
      val txy = kojoToTile(b.x, b.y + b.height / 2)
      val p = tileToKojo(TileXY(txy.x + 1, txy.y))
      pic.setPosition(p.x, b.y)
    }

    def moveToTileAbove(pic: Picture) {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width / 2, b.y)
      val p = tileToKojo(TileXY(txy.x, txy.y - 1))
      pic.setPosition(b.x, p.y)
    }

    def moveToTileBelow(pic: Picture) {
      val b = pic.bounds
      val txy = kojoToTile(b.x + b.width / 2, b.y + b.height)
      val p = tileToKojo(TileXY(txy.x, txy.y + 1))
      val heightDiff = b.height - tileHeight
      pic.setPosition(b.x, p.y - heightDiff)
    }

    val currentLevelNumber = 0

    val levels = Array(Level(this))
    val currentLevel = levels(currentLevelNumber)

    // Draw aesthetic overlay
    //    val overlay = pygame.image.load("resources/overlay.png")

    def step() {
    }

    // Draw level, player, overlay
    def draw() {
      currentLevel.draw()
    }
  }

}
