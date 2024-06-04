import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TiledMapTileSet}
import com.badlogic.gdx.math.Vector2

import java.awt.Rectangle
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

// x : bottomleft pos x
// y : bottomleft pos y


object Road {
  var ROAD_DOWN_TILE_ID = 1023
  var ROAD_UP_TILE_ID = 975
  var ROAD_CROSS_DOWN_TILE_ID = 1024
  var ROAD_CROSS_UP_TILE_ID = 976
}
class Road(x:Int,y:Int,tileset:TiledMapTileSet,var layer:TiledMapTileLayer) {
  var cars:ArrayBuffer[Car] = new ArrayBuffer[Car]()
  var tile_road:ArrayBuffer[ArrayBuffer[TiledMapTile]] = new ArrayBuffer[ArrayBuffer[TiledMapTile]]()
  var y_size:Int = 2
  private var is_started:Boolean = false
  build_road_tile()
  init_car()


  def is_touching(v:Vector2,width:Int,height:Int):Boolean = {
    for(car:Car <- cars){
      var rct1:Rectangle = LayerHelper.getRectangle(v.x.toInt-width/2,v.y.toInt-height/2,width,height)
      var rct2:Rectangle = LayerHelper.getRectangle(car.getPosition.x.toInt-Car.SPRITE_WIDTH/2,car.getPosition.y.toInt-Car.SPRITE_HEIGHT/2,Car.SPRITE_WIDTH,Car.SPRITE_HEIGHT)
      if(LayerHelper.checkOverlap(rct1,rct2,5)){
        println("car "+cars.indexOf(car) + "is touching")
        return true
      }
    }
    false
  }

  def extendLayer():TiledMapTileLayer = {
    layer = LayerHelper.extendLayer(layer,0,y_size)
    layer
  }

  def add_to_layer():Unit = {
    for(x <- 0 until layer.getWidth;y <- layer.getHeight-y_size until layer.getHeight ) {
      var layer_cell = layer.getCell(x,y)
      var new_cell:TiledMapTileLayer.Cell = new TiledMapTileLayer.Cell()
      var new_tile = tile_road(x)(y-(layer.getHeight-y_size))
      new_cell.setTile(new_tile)
      layer.setCell(x,y,new_cell)
    }
  }
  def start():Unit = {
    is_started = true
  }
  private def build_tiles_properties():Unit = {
    var road_tile_down = tileset.getTile(Road.ROAD_DOWN_TILE_ID)
    var road_tile_up = tileset.getTile(Road.ROAD_UP_TILE_ID)
    var cross_road_down_tile = tileset.getTile(Road.ROAD_CROSS_DOWN_TILE_ID)
    var cross_up_down_tile = tileset.getTile(Road.ROAD_CROSS_UP_TILE_ID)

    road_tile_up.getProperties.put("walkable",true)
    road_tile_down.getProperties.put("walkable",true)
    cross_road_down_tile.getProperties.put("walkable",true)
    cross_up_down_tile.getProperties.put("walkable",true)
  }
  private def build_road_tile():Unit = {
    build_tiles_properties()
    for(x <- 0 until layer.getWidth){
      var random = Random.between(0,7)
      var is_cross_road:Boolean = random == 6 // 1 chance sur 6 ?
      var tile = new ArrayBuffer[TiledMapTile]()
      if(is_cross_road){
        tile.append(tileset.getTile(Road.ROAD_CROSS_DOWN_TILE_ID))
        tile.append(tileset.getTile(Road.ROAD_CROSS_UP_TILE_ID))
      }else{
        tile.append(tileset.getTile(Road.ROAD_DOWN_TILE_ID))
        tile.append(tileset.getTile(Road.ROAD_UP_TILE_ID))
      }
      tile_road.append(tile)
    }
  }
  def init_car():Unit = {
    var rdm = Random.between(0,2)
    var rdm_speed = 0.5
    var car:SimpleCar = new SimpleCar(0,y+1,rdm_speed,1)
    cars.addOne(car)
    if(rdm == 1){
      rdm_speed = 0.5
      var car:SimpleCar = new SimpleCar(layer.getWidth-1,y+2,rdm_speed,-1)
      cars.addOne(car)
    }
  }

  private def animate_car(car:Car,g: GdxGraphics):Unit = {
    car.draw(g)
    car.animate(Gdx.graphics.getDeltaTime)
  }
  def drawCars(g: GdxGraphics):Unit = {
    if(is_started){
      for(car <- cars){
        if(!car.isMoving){
          if(car.direction == 1){
            var nextTile = LayerHelper.getTile(car.getPosition,car.direction,0,layer)
            if (nextTile == null) {
              car.setPosition(new Vector2(-Car.SPRITE_WIDTH, car.getPosition.y))
              animate_car(car, g)
              return
            }
            car.go(Car.Direction.RIGHT)
          }
          if(car.direction == -1){
            var nextTile = LayerHelper.getTile(car.getPosition,car.direction,0,layer)
            if (nextTile == null) {
              car.setPosition(new Vector2((layer.getWidth*layer.getTileWidth)-1, car.getPosition.y))
              animate_car(car, g)
              return
            }
            car.go(Car.Direction.LEFT)
          }
        }
        animate_car(car,g)
      }
    }

  }

}
