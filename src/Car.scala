
import ch.hevs.gdx2d.components.bitmaps.{BitmapImage, Spritesheet}
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.{Interpolation, Vector2}


/**
 * Character for the demo.
 *
 * @author Alain Woeffray (woa)
 * @author Pierre-André Mudry (mui)
 */
object Car {
  object Direction extends Enumeration {
    type Direction = Value
    val UP, DOWN, RIGHT, LEFT, NULL = Value
  }

  private val SPRITE_WIDTH = 32
  private val SPRITE_HEIGHT = 32
}

class Car(initialPosition: Vector2)

/**
 * Create the Car at the start position
 *
 * @param initialPosition Start position [px] on the map.
 */
  extends DrawableObject {
  lastPosition = new Vector2(initialPosition)
  newPosition = new Vector2(initialPosition)
  position = new Vector2(initialPosition)
  ss = new Spritesheet("data/images/lumberjack_sheet32.png", Car.SPRITE_WIDTH, Car.SPRITE_HEIGHT)
  /**
   * The currently selected sprite for animation
   */
  private val textureX:Int = 0
  private var textureY:Int = 1
  private var speed:Float = 1
  private var dt:Double = 0
  private var currentFrame:Int = 0
  private val nFrames:Int = 4
  final private val FRAME_TIME:Float = 0.1f // Duration of each frime

  private var ss: Spritesheet = new Spritesheet("data/images/lumberjack_sheet32.png", Car.SPRITE_WIDTH, Car.SPRITE_HEIGHT)
  private var lastPosition: Vector2 = new Vector2(initialPosition)
  private var newPosition: Vector2 = new Vector2(initialPosition)
  private var position: Vector2 = new Vector2(initialPosition)
  final private val img = new BitmapImage("data/images/pipe.png")
  private var move = false

  /**
   * Create the Car at the start position (0,0)
   */
  def this() {
    this(new Vector2(0, 0))
  }

  /**
   * Create the Car at the given start tile.
   *
   * @param x Column
   * @param y Line
   */
  def this(x: Int, y: Int) {
    this(new Vector2(Car.SPRITE_WIDTH * x, Car.SPRITE_HEIGHT * y))
  }

  /**
   * @return the current position of the Car on the map.
   */
  def getPosition: Vector2 = this.position

  /**
   * Update the position and the texture of the Car.
   *
   * @param elapsedTime The time [s] elapsed since the last time which this method was called.
   */
  def animate(elapsedTime: Double): Unit = {
    val frameTime = FRAME_TIME / speed
    position = new Vector2(lastPosition)
    if (isMoving) {
      dt += elapsedTime
      val alpha:Float = ((dt + frameTime * currentFrame) / (frameTime * nFrames)).toFloat
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
      }
    }
  }

  /**
   * @return True if the Car is actually doing a step.
   */
  def isMoving: Boolean = move

  /**
   * @param speed The new speed of the Car.
   */
  def setSpeed(speed: Float): Unit = {
    this.speed = speed
  }

  /**
   * Do a step on the given direction
   *
   * @param direction The direction to go.
   */
  def go(direction: Car.Direction.Value): Unit = {
    move = true
    direction match {
      case Car.Direction.RIGHT =>
        newPosition.add(Car.SPRITE_WIDTH, 0)

      case Car.Direction.LEFT =>
        newPosition.add(-Car.SPRITE_WIDTH, 0)

      case Car.Direction.UP =>
        newPosition.add(0, Car.SPRITE_HEIGHT)

      case Car.Direction.DOWN =>
        newPosition.add(0, -Car.SPRITE_HEIGHT)

      case _ =>

    }
    turn(direction)
  }

  /**
   * Turn the Car on the given direction without do any step.
   *
   * @param direction The direction to turn.
   */
  def turn(direction: Car.Direction.Value): Unit = {
    direction match {
      case Car.Direction.RIGHT =>
        textureY = 2

      case Car.Direction.LEFT =>
        textureY = 1

      case Car.Direction.UP =>
        textureY = 3

      case Car.Direction.DOWN =>
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
    var b = new Texture("data/images/characters/car/Blackout.png")
    g.draw(b, position.x, position.y)
  }
}
