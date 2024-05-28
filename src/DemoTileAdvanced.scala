
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.maps.tiled._
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import java.util


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

  private var car:Car = null
  // tiles management
  private var tiledMap: TiledMap = null
  private var tiledMapRenderer: TiledMapRenderer = null
  private var tiledLayer: TiledMapTileLayer = null
  private var zoom = .0
  private var count:Int = 0

  def onInit(): Unit = {
    // Create hero
    hero = new Hero(10, 20)
    car = new Car(10,20)
    // Set initial zoom
    zoom = 1
    // init keys status
    keyStatus.put(Input.Keys.UP, false)
    keyStatus.put(Input.Keys.DOWN, false)
    keyStatus.put(Input.Keys.LEFT, false)
    keyStatus.put(Input.Keys.RIGHT, false)
    // create map
    tiledMap = new TmxMapLoader().load("data/maps/desert.tmx")
    tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap)
    tiledLayer = tiledMap.getLayers.get(0).asInstanceOf[TiledMapTileLayer]
  }

  def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear
    // Hero activity
    manageHero()
    // Camera follows the hero
    g.zoom(zoom.toFloat)
    g.moveCamera(hero.getPosition.x, hero.getPosition.y, tiledLayer.getWidth * tiledLayer.getTileWidth, tiledLayer.getHeight * tiledLayer.getTileHeight)
    // Render the tilemap
    tiledMapRenderer.setView(g.getCamera)
    tiledMapRenderer.render
    // Draw the hero
    hero.animate(Gdx.graphics.getDeltaTime)
    hero.draw(g)
    car.animate(Gdx.graphics.getDeltaTime)
    car.draw(g)
    g.drawFPS
    g.drawSchoolLogo
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
   * Get the "speed" property of the given tile.
   *
   * @param tile
   * The tile to know the property
   * @return The value of the property
   */
  private def getSpeed(tile: TiledMapTile) = {
    val test = tile.getProperties.get("speed")
    test.toString.toFloat
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
      // Is the move valid ?
      if (isWalkable(nextCell)) {
        // Go
        hero.setSpeed(getSpeed(nextCell))
        hero.go(goalDirection)
      }
      else {
        // Face the wall
        hero.turn(goalDirection)
      }
      if (count == 1000) {
        count = 0
        var nextCell = getTile(car.getPosition, offsetX = 1, offsetY = 0)
        if (isWalkable(nextCell)) {
          car.setSpeed(getSpeed(nextCell))
          car.go(Car.Direction.LEFT)
        }
      }else{
        count+=1
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
