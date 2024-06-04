import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2

object LayerHelper {

  def extendLayer(layer: TiledMapTileLayer, deltaX: Int, deltay: Int): TiledMapTileLayer = {
    var newLayer = new TiledMapTileLayer(layer.getWidth + deltaX, layer.getHeight + deltay, layer.getTileWidth.toInt, layer.getTileHeight.toInt)
    for (x: Int <- 0 until layer.getWidth; y: Int <- 0 until layer.getHeight) {
      newLayer.setCell(x, y, layer.getCell(x, y))
    }
    return newLayer
  }

    def getTile(position: Vector2, offsetX: Int, offsetY: Int,tiledLayer:TiledMapTileLayer) = try {
    val x = (position.x / tiledLayer.getTileWidth).asInstanceOf[Int] + offsetX
    val y = (position.y / tiledLayer.getTileHeight).asInstanceOf[Int] + offsetY
    tiledLayer.getCell(x, y).getTile
  } catch {
    case e: Exception =>
      null
  }

}
