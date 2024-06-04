import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer}

class Road(x:Int,y:Int,tile:TiledMapTile,layer:TiledMapTileLayer) {
  var added:Boolean = false
  var car:Car = null

  def set_added():Unit = {
    added = true
  }
  def is_added:Boolean = added
  def drawCar(g: GdxGraphics):Unit = {

  }

}
