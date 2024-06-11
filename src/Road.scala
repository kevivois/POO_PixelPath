import Cars.Car
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
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
class Road(val x:Int,val y:Int,tileset:TiledMapTileSet,var layer:TiledMapTileLayer) {
  var cars:ArrayBuffer[Car] = new ArrayBuffer[Car]()
  var tile_road:ArrayBuffer[ArrayBuffer[TiledMapTile]] = new ArrayBuffer[ArrayBuffer[TiledMapTile]]()
  var y_size:Int = 2
  private var is_started:Boolean = false
  build_road_tile()
  initCars()


  def is_touching(v:Vector2,width:Int,height:Int):Boolean = {
    for(car:Car <- cars){
      var rct1:Rectangle = LayerHelper.getRectangle(v.x.toInt-width/2,v.y.toInt-height/2,width,height)
      var rct2:Rectangle = LayerHelper.getRectangle(car.getPosition.x.toInt-Cars.Car.SPRITE_WIDTH/2,car.getPosition.y.toInt-Cars.Car.SPRITE_HEIGHT/2,Cars.Car.SPRITE_WIDTH,Cars.Car.SPRITE_HEIGHT)
      if(LayerHelper.checkOverlap(rct1,rct2,3)){
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
    for(x_pos <- 0 until layer.getWidth;y_pos <- y+(y_size-1) to y+y_size ) {
      var layer_cell = layer.getCell(x_pos,y_pos)
      var new_cell:TiledMapTileLayer.Cell = new TiledMapTileLayer.Cell()
      var new_tile = tile_road(x_pos)(y_pos-(y+(y_size-1)))
      new_cell.setTile(new_tile)
      layer.setCell(x_pos,y_pos,new_cell)
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
    for (_ <- 0 until layer.getWidth) {
      val isCrossRoad = Random.between(0, 7) == 6
      val tile = ArrayBuffer[TiledMapTile]()
      if (isCrossRoad) {
        tile.append(tileset.getTile(Road.ROAD_CROSS_DOWN_TILE_ID))
        tile.append(tileset.getTile(Road.ROAD_CROSS_UP_TILE_ID))
      } else {
        tile.append(tileset.getTile(Road.ROAD_DOWN_TILE_ID))
        tile.append(tileset.getTile(Road.ROAD_UP_TILE_ID))
      }
      tile_road.append(tile)
    }
  }

  def initCars():Unit = {
    var rdm_y_1 = Random.between(y+1,y+3)
    var rdm_y_2 = if(rdm_y_1 == y+1) y+2 else y+1
    var rdm_car = Random.between(0,2)
    var rdm_speed = Random.between(1,6)
    var car:Car = Cars.Car.get_random_car(0,rdm_y_1,rdm_speed,1)
    cars.addOne(car)
    if(rdm_car == 1) {
      rdm_speed = Random.between(1,6)
      car = Cars.Car.get_random_car(layer.getWidth, rdm_y_2, rdm_speed, -1)
      cars.addOne(car)
    }
  }

  private def animate_car(car:Car,g: GdxGraphics):Unit = {
    car.draw(g)
    car.animate(Gdx.graphics.getDeltaTime)
  }
  def draw(g: GdxGraphics):Unit = {
    if (is_started) {
      for (car <- cars) {
        if (!car.isMoving) {
          if (car.direction == 1) {
            //var nextTile = LayerHelper.getTile(car.getPosition, car.direction, 0, layer,Car.SPRITE_WIDTH,Car.SPRITE_HEIGHT)
            if (car.getPosition.x == (layer.getWidth*layer.getTileWidth)) {
              car.setPosition(new Vector2(-Cars.Car.SPRITE_WIDTH, car.getPosition.y))
              animate_car(car, g)
              return
            }
            car.go(Cars.Car.Direction.RIGHT)
          }
          if (car.direction == -1) {
            //var nextTile = LayerHelper.getTile(car.getPosition, car.direction, 0, layer,Car.SPRITE_WIDTH,Car.SPRITE_HEIGHT)
            if (car.getPosition.x+Cars.Car.SPRITE_WIDTH <= 0) {
              car.setPosition(new Vector2((layer.getWidth * layer.getTileWidth) - 1, car.getPosition.y))
              animate_car(car, g)
              return
            }
            car.go(Cars.Car.Direction.LEFT)
          }
        }
        animate_car(car, g)
      }
    }
  }
}
