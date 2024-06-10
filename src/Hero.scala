
import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.components.bitmaps.Spritesheet
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2


/**
 * Character for the demo.
 *
 * @author Alain Woeffray (woa)
 * @author Pierre-André Mudry (mui)
 */
object Hero {
  object Direction extends Enumeration {
    type Direction = Value
    val UP, DOWN, RIGHT, LEFT, NULL = Value
  }

  val SPRITE_WIDTH = 32
  val SPRITE_HEIGHT = 32
}

class Hero(initialPosition: Vector2,spd:Float=1)

/**
 * Create the hero at the start position
 *
 * @param initialPosition Start position [px] on the map.
 */
  extends DrawableObject {
  lastPosition = new Vector2(initialPosition)
  newPosition = new Vector2(initialPosition)
  position = new Vector2(initialPosition)
  ss = new Spritesheet("data/images/lumberjack_sheet32.png", Hero.SPRITE_WIDTH, Hero.SPRITE_HEIGHT)
  /**
   * The currently selected sprite for animation
   */
  private val textureX: Int = 0
  private var textureY: Int = 1
  private var speed: Float = spd
  private var dt: Double = 0
  private var currentFrame: Int = 0
  private val nFrames: Int = 4
  final private val FRAME_TIME: Float = 0.1f // Duration of each frime

  private var ss: Spritesheet = new Spritesheet("data/images/lumberjack_sheet32.png", Hero.SPRITE_WIDTH, Hero.SPRITE_HEIGHT)
  private var lastPosition: Vector2 = new Vector2(initialPosition)
  private var newPosition: Vector2 = new Vector2(initialPosition)
  private var position: Vector2 = new Vector2(initialPosition)
  final private val img = new BitmapImage("data/images/pipe.png")
  var current_direction: Hero.Direction.Value = null
  private var move = false

  /**
   * Create the hero at the start position (0,0)
   */
  def this() {
    this(new Vector2(0, 0))
  }

  /**
   * Create the hero at the given start tile.
   *
   * @param x Column
   * @param y Line
   */
  def this(x: Int, y: Int) {
    this(new Vector2(Hero.SPRITE_WIDTH * x, Hero.SPRITE_HEIGHT * y))
  }

  /**
   * @return the current position of the hero on the map.
   */
  def getPosition: Vector2 = this.position

  /**
   * Update the position and the texture of the hero.
   *
   * @param elapsedTime The time [s] elapsed since the last time which this method was called.
   */
  def animate(elapsedTime: Double): Unit = {
    val frameTime = FRAME_TIME / speed
    position = new Vector2(lastPosition)
    if (isMoving) {
      dt += elapsedTime
      val alpha: Float = ((dt + frameTime * currentFrame) / (frameTime * nFrames)).toFloat
      position.interpolate(newPosition, alpha, Interpolation.linear)
    }
    else dt = 0
    if (dt > frameTime) {
      dt -= frameTime
      currentFrame = (currentFrame + 1) % nFrames
      if (currentFrame == 0) {
        move = false
        lastPosition = new Vector2(newPosition)
        position = new Vector2(newPosition)
        current_direction = null
      }
    }
  }

  /**
   * @return True if the hero is actually doing a step.
   */
  def isMoving: Boolean = move

  /**
   * @param speed The new speed of the hero.
   */
  def setSpeed(speed: Float): Unit = {
    this.speed = speed
  }

  /**
   * Do a step on the given direction
   *
   * @param direction The direction to go.
   */
  def go(direction: Hero.Direction.Value): Unit = {
      current_direction = direction
      move = true
      direction match {
        case Hero.Direction.RIGHT =>
          newPosition.add(Car.SPRITE_WIDTH, 0)

        case Hero.Direction.LEFT =>
          newPosition.add(-Car.SPRITE_WIDTH, 0)

        case Hero.Direction.UP =>
          newPosition.add(0, Car.SPRITE_HEIGHT)

        case Hero.Direction.DOWN =>
          newPosition.add(0, -Car.SPRITE_HEIGHT)

        case _ =>

      }
      turn(direction)
  }

  /**
   * Turn the hero on the given direction without do any step.
   *
   * @param direction The direction to turn.
   */
  def turn(direction: Hero.Direction.Value): Unit = {
    direction match {
      case Hero.Direction.RIGHT =>
        textureY = 2

      case Hero.Direction.LEFT =>
        textureY = 1

      case Hero.Direction.UP =>
        textureY = 3

      case Hero.Direction.DOWN =>
        textureY = 0

      case _ =>

    }
  }

  /**
   * Draw the character on the graphic object.
   *
   * @param g Graphic object.
   */
  override def draw(g: GdxGraphics): Unit = {
    g.drawRectangle(position.x+Hero.SPRITE_WIDTH/2,position.y+Hero.SPRITE_HEIGHT/2,Hero.SPRITE_WIDTH.toFloat,Hero.SPRITE_HEIGHT.toFloat,0)
    g.draw(ss.sprites(textureY)(currentFrame), position.x, position.y)
  }
}
