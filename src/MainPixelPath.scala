
import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.{Color, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.{SpriteBatch, TextureRegion}
import com.badlogic.gdx.maps.{MapObjects, MapProperties}
import com.badlogic.gdx.maps.tiled._
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.{Vector2, Vector3}

import java.awt.Rectangle
import java.util
import scala.collection.mutable.ArrayBuffer
import scala.util.Random


/**
 * Character walks on a small desert. (Tiled editor (http://www.mapeditor.org/)
 *
 * @author Alain Woeffray (woa)
 * @author Pierre-André Mudry (mui)
 * @author Marc Pignat (pim)
 */
object MainPixelPath {
  def main(args: Array[String]): Unit = {
    new MainPixelPath
  }
}

class MainPixelPath extends PortableApplication(950,600) {
  // key management
  private val screen_width:Int = 700
  private val screen_height = 600
  private val keyStatus = new util.TreeMap[Integer, Boolean]
  // character
  private var hero: Hero = null
  // tiles management
  private var tiledMap: TiledMap = null
  private var tiledMapRenderer: TiledMapRenderer = null
  private var tiledLayer: TiledMapTileLayer = null
  private var tiledSet:TiledMapTileSet = null
  private var zoom = .0
  private var roads:ArrayBuffer[Road] =null
  private var current_score:Int = 0
  private var max_score:Int = 0
  private var max_y_position:Double = 0
  private var current_max_y_position:Double = 0
  private var old_score:Int = 0
  private var summed_up:Boolean = false
  private var gameover:Boolean = false
  private var waiting_for_restart:Boolean  = false
  private var waiting_for_first_start:Boolean = true
  private var clicked_on_space:Boolean = false
  private var inital_layer_width:Int = 0
  private var inital_layer_height:Int = 0

  def onInit(): Unit = {

    setTitle("Pixel Path")

    zoom = 1
    // init keys status
    keyStatus.put(Input.Keys.UP, false)
    keyStatus.put(Input.Keys.DOWN, false)
    keyStatus.put(Input.Keys.LEFT, false)
    keyStatus.put(Input.Keys.RIGHT, false)
    tiledMap = new TmxMapLoader().load("data/maps/pixelPathMap.tmx")
    tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap)
    tiledLayer = tiledMap.getLayers.get(0).asInstanceOf[TiledMapTileLayer]
    inital_layer_width = tiledLayer.getWidth
    inital_layer_height = tiledLayer.getHeight
    tiledSet = tiledMap.getTileSets.getTileSet(1)
    roads = new ArrayBuffer[Road]()

    hero = new Hero(tiledLayer.getWidth/2,0)
    hero.setSpeed(1.5f)

    initCells()
  }
  var count:Int = 0
  def initCells():Unit = {
    for(xi <- 0 until tiledLayer.getWidth;yi:Int <- 0 until tiledLayer.getHeight){
      val new_cell:TiledMapTileLayer.Cell = new TiledMapTileLayer.Cell()
      new_cell.setTile(tiledSet.getTile(836))
      tiledLayer.setCell(xi,yi, new_cell)
    }
    var y: Int = 0
    while (y < tiledLayer.getHeight) {
      if (count % 2 == 0) {
        var grass_length:Int = Random.between(0,2)
          for(xi <- 0 until tiledLayer.getWidth;yi:Int <- 0 until grass_length){
            var new_cell:TiledMapTileLayer.Cell = new TiledMapTileLayer.Cell()
            new_cell.setTile(tiledSet.getTile(836))
            tiledLayer.setCell(xi,y+yi,new_cell)
        }
        y+=grass_length

      } else {
        var new_road:Road = new Road(0, y, tiledSet, tiledLayer)
        if (y + new_road.y_size < tiledLayer.getHeight) {
          new_road.add_to_layer()
          roads.addOne(new_road)
        }
        y += new_road.y_size+1
      }
      count += 1
    }
      for (r:Road <- roads) {
        r.start()
      }
      tiledMap.getLayers.remove(0)
      tiledMap.getLayers.add(tiledLayer)
      tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap)
  }

    def reset_game():Unit = {
      max_y_position = Math.max(current_max_y_position, max_y_position)
      max_score = Math.max(max_score, current_score)
      old_score = current_score
      current_max_y_position = 0
      current_score = 0
      tiledLayer = new TiledMapTileLayer(inital_layer_width, inital_layer_height, tiledLayer.getTileWidth.toInt, tiledLayer.getTileHeight.toInt)
      roads.clear()
      hero = new Hero(tiledLayer.getWidth / 2, 0)
      hero.setSpeed(1.5f)
      initCells()
    }

    def onGraphicRender(g: GdxGraphics): Unit = {
      g.clear()
      if(clicked_on_space){
        clicked_on_space=false
        if(waiting_for_first_start){
          waiting_for_first_start=false
        }else if(waiting_for_restart){
          waiting_for_restart=false
        }
      }
      if(gameover){
        gameover = false
        waiting_for_restart = true
        reset_game()
      }
      if(waiting_for_restart || waiting_for_first_start ){
        if(waiting_for_first_start){
          g.drawTransformedPicture(screen_width/2,screen_height/2,0,1f,new BitmapImage("data/images/main_screen.jpeg"))
        }
        if(waiting_for_restart){
          g.drawString(g.getCamera.position.x,g.getCamera.position.y,"current score : "+old_score.toString)
          g.drawString(g.getCamera.position.x,g.getCamera.position.y - 50,"all time record : "+ max_score.toString)
        }
      }else {
        if (g.getCamera.position.y + g.getScreenHeight / 2 == tiledLayer.getHeight * tiledLayer.getTileHeight) {
          val new_road = new Road((g.getCamera.position.x - tiledLayer.getWidth / 2).toInt, tiledLayer.getHeight - 1, tiledSet, tiledLayer)
          tiledLayer = new_road.extendLayer()
          new_road.add_to_layer()
          new_road.start()
          roads.addOne(new_road)

          // add grass

          var grass_length_random:Int = Random.between(0, 3)
          for (i: Int <- 0 until grass_length_random) {
            tiledLayer = LayerHelper.extendLayer(tiledLayer, 0, 1)
            var grass_tile = tiledSet.getTile(836)
            for (x: Int <- 0 until tiledLayer.getWidth; y: Int <- tiledLayer.getHeight - 1 until tiledLayer.getHeight) {
              var new_cell:TiledMapTileLayer.Cell = new TiledMapTileLayer.Cell()
              new_cell.setTile(grass_tile)
              tiledLayer.setCell(x, y, new_cell)
            }
          }
          tiledMap.getLayers.remove(0)
          tiledMap.getLayers.add(tiledLayer)
          tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap)
        }

        manageHero()
        // Camera follows the hero
        g.zoom(1)
        g.moveCamera(hero.getPosition.x, hero.getPosition.y, tiledLayer.getWidth * tiledLayer.getTileWidth, tiledLayer.getHeight * tiledLayer.getTileHeight)
        tiledMapRenderer.setView(g.getCamera)
        tiledMapRenderer.render()
        // Draw the hero
        hero.animate(Gdx.graphics.getDeltaTime)
        hero.draw(g)
        if(tiledLayer.getHeight*tiledLayer.getTileHeight > max_y_position && max_y_position != 0 && hero.getPosition.y < max_y_position){
          g.drawString((g.getCamera.position.x-50),max_y_position.toFloat+15,s"your all time record  y=${max_y_position}  !")
          g.drawLine(0,max_y_position.toFloat,((tiledLayer.getTileWidth*tiledLayer.getWidth)-1),max_y_position.toFloat,Color.BLUE)
        }
        g.drawString(g.getCamera.position.x+g.getScreenWidth/2 - 160,g.getCamera.position.y+g.getScreenHeight/2,s"current score : ${current_score}")
        for (road <- roads) {
          road.draw(g)
          if (road.is_touching(hero.getPosition, Hero.SPRITE_WIDTH, Hero.SPRITE_HEIGHT)) {
            gameover = true
            waiting_for_restart = true
          }
        }
      }
    }

  /**
   * exemple : getTile(myPosition,0,1) get the tile over myPosition
   *
   * @param position
   * The position on map (not on screen)
   * @param offsetX
   * The number of cells at right of the given position.
   * @param offsetY
   * The number of cells over the given position.
   * @return The tile around the given position | null
   */
  private def getTile(position: Vector2, offsetX: Int, offsetY: Int) = try {
    val x = (position.x / tiledLayer.getTileWidth).asInstanceOf[Int] + offsetX
    val y = (position.y / tiledLayer.getTileHeight).asInstanceOf[Int] + offsetY
    tiledLayer.getCell(x, y).getTile
  } catch {
    case e: Exception =>
      null
  }

  /**
   * Get the "walkable" property of the given tile.
   *
   * @param tile
   * The tile to know the property
   * @return true if the property is set to "true", false otherwise
   */


  /**
   * Manage the movements of the hero using the keyboard.
   */
  private def manageHero(): Unit = {
    // Do nothing if hero is already moving
      // Compute direction and next cell
      if(!hero.isMoving){
        if(hero.getPosition.y > current_max_y_position){
          summed_up = false
          current_max_y_position = hero.getPosition.y
        }
        if(!summed_up) {
          var is_on_a_road = false
          for (r <- roads) {
            val current_max_y_position_without_tile = current_max_y_position / tiledLayer.getTileHeight
            if (current_max_y_position_without_tile > r.y && current_max_y_position_without_tile <= r.y + r.y_size) {
              is_on_a_road = true
            }
          }
          if (is_on_a_road) {
            current_score += 1
            summed_up = true
          }
        }
      }
      if(hero.isMoving || gameover||waiting_for_restart||waiting_for_first_start) return
      var nextCell: TiledMapTile = null
      var goalDirection = Hero.Direction.NULL
      if (keyStatus.get(Input.Keys.D)) {
        goalDirection = Hero.Direction.RIGHT
        nextCell = getTile(hero.getPosition, 1, 0)
      }
      else if (keyStatus.get(Input.Keys.A)) {
        goalDirection = Hero.Direction.LEFT
        nextCell = getTile(hero.getPosition, -1, 0)
      }
      else if (keyStatus.get(Input.Keys.W)) {
        goalDirection = Hero.Direction.UP
        nextCell = getTile(hero.getPosition, 0, 1)
      }
      else if (keyStatus.get(Input.Keys.S)) {
        goalDirection = Hero.Direction.DOWN
        nextCell = getTile(hero.getPosition, 0, -1)
      }
      if(nextCell != null) {
        hero.go(goalDirection)
      }
    }

  // Manage keyboard events
  override def onKeyUp(keycode: Int): Unit = {
    super.onKeyUp(keycode)
    keyStatus.put(keycode, false)
  }

  override def onKeyDown(keycode: Int): Unit = {
    super.onKeyDown(keycode)
    keycode match {
      case Input.Keys.Z =>
        if (zoom == 1.0) zoom = .5f
        else if (zoom == .5) zoom = 2
        else zoom = 1
        return
      case _ =>

    }
    if(keycode == Input.Keys.SPACE){
      clicked_on_space=true
    }
    keyStatus.put(keycode, true)
  }
}
