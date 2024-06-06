import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TiledMapTileSet}
import com.badlogic.gdx.math.Vector2

import java.awt.Rectangle
import scala.collection.mutable.ArrayBuffer

object LayerHelper {

  def extendLayer(layer: TiledMapTileLayer, deltaX: Int, deltay: Int): TiledMapTileLayer = {
    var newLayer = new TiledMapTileLayer(layer.getWidth + deltaX, layer.getHeight + deltay, layer.getTileWidth.toInt, layer.getTileHeight.toInt)
    for (x: Int <- 0 until layer.getWidth; y: Int <- 0 until layer.getHeight) {
      newLayer.setCell(x, y, layer.getCell(x, y))
    }
    return newLayer
  }

    def getTile(position: Vector2, offsetX: Int, offsetY: Int,tiledLayer:TiledMapTileLayer,tileWidth:Int,tileHeight:Int) = try {
    val x = (position.x / tileWidth).asInstanceOf[Int] + offsetX
    val y = (position.y / tileHeight).asInstanceOf[Int] + offsetY
    tiledLayer.getCell(x, y).getTile
  } catch {
    case e: Exception =>
      null
  }

  def get_grass_tiles_of_length(tileSet:TiledMapTileSet,length:Int):ArrayBuffer[TiledMapTile] = {
    var grass_tile = tileSet.getTile(876)
    var out:ArrayBuffer[TiledMapTile] = new ArrayBuffer[TiledMapTile]()
    for(i <- 0 until length){
      out.append(grass_tile)
    }
    out
  }

  def checkOverlap(r1: Rectangle, r2: Rectangle,delta:Int=0): Boolean = {
    return !(r1.x + r1.width - delta < r2.x || r1.y + r1.height -delta < r2.y || r1.x - delta > r2.x + r2.width || r1.y + delta > r2.y + r2.height);
  }

  def getRectangle(x: Int, y: Int, width: Int, height: Int): Rectangle = {
    return new Rectangle(x, y, width, height)
  }

}
