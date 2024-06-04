
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
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
object DemoTileAdvanced {
  def main(args: Array[String]): Unit = {
    new DemoTileAdvanced
  }
}

class DemoTileAdvanced extends PortableApplication {
  // key management
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
  private var count:Int = 0

  def onInit(): Unit = {
    // Create hero
    hero = new Hero(10,0)
    // Set initial zoom
    zoom = 1
    // init keys status
    keyStatus.put(Input.Keys.UP, false)
    keyStatus.put(Input.Keys.DOWN, false)
    keyStatus.put(Input.Keys.LEFT, false)
    keyStatus.put(Input.Keys.RIGHT, false)
    // create map
    // tiledMap = new TiledMap()
    tiledMap = new TmxMapLoader().load("data/maps/pixelPathMap.tmx")
    tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap)
    tiledLayer = tiledMap.getLayers.get(0).asInstanceOf[TiledMapTileLayer]
    tiledSet = tiledMap.getTileSets.getTileSet(1)
    roads = new ArrayBuffer[Road]()
    initCells()
  }
  def initCells():Unit = {
    for(i:Int <- 0 until tiledLayer.getTileWidth.toInt;y:Int <- 0 until tiledLayer.getTileHeight.toInt){
      val cell = tiledLayer.getCell(i,y)
      if(cell!=null) {
        cell.getTile.getProperties.put("walkable", true)
        cell.getTile.getProperties.put("speed", 1.5)
      }
    }
  }

  def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    if(g.getCamera.position.y + g.getScreenHeight / 2 == tiledLayer.getHeight * tiledLayer.getTileHeight)
    {
      val new_road = new Road((g.getCamera.position.x-tiledLayer.getWidth/2).toInt,tiledLayer.getHeight-1,tiledSet,tiledLayer)
      tiledLayer = new_road.extendLayer()
      new_road.add_to_layer()
      new_road.start()
      roads.addOne(new_road)

      // add grass

      var grass_length_random = Random.between(1, 4)
      for(i:Int <- 0 until grass_length_random){
        tiledLayer = LayerHelper.extendLayer(tiledLayer, 0, 1)
        var grass_tile = tiledSet.getTile(836)
        for(x:Int <- 0 until tiledLayer.getWidth;y:Int <- tiledLayer.getHeight-1 until tiledLayer.getHeight ){
          var new_cell = new TiledMapTileLayer.Cell()
          new_cell.setTile(grass_tile)
          new_cell.getTile.getProperties.put("walkable",true)
          tiledLayer.setCell(x,y,new_cell)
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

    for (road <- roads) {
      road.drawCars(g)
      road.is_touching(hero.getPosition,Hero.SPRITE_WIDTH,Hero.SPRITE_HEIGHT)
    }
    g.drawFPS()
    g.drawSchoolLogo()
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
  private def isWalkable(tile: TiledMapTile): Boolean = {
    if (tile == null) return false
    val test = tile.getProperties.get("walkable")
    test.toString.toBoolean
  }


  /**
   * Manage the movements of the hero using the keyboard.
   */
  private def manageHero(): Unit = {
    // Do nothing if hero is already moving
    if (!hero.isMoving) {
      // Compute direction and next cell
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
      if (isWalkable(nextCell)) {
        // God
        hero.go(goalDirection)
      }
      else {
        // Face the wall
        hero.turn(goalDirection)
      }
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
    keyStatus.put(keycode, true)
  }
}
