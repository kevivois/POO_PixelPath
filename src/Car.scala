
import ch.hevs.gdx2d.components.bitmaps.{BitmapImage, Spritesheet}
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.graphics.{Pixmap, Texture}
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch, TextureRegion}
import com.badlogic.gdx.math.{Interpolation, Vector2}

import java.awt.Image
import java.awt.image.{BufferedImage, Raster}
import java.io.ByteArrayInputStream


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
  val FILEPATH = ""

   val SPRITE_WIDTH = 32
   val SPRITE_HEIGHT = 32
}

class Car(initialPosition: Vector2,imageFile:String,sp:Double=1.5,initialDirection:Int=1)

/**
 * Create the Car at the start position
 *
 * @param initialPosition Start position [px] on the map.
 */
  extends DrawableObject {
  lastPosition = new Vector2(initialPosition)
  newPosition = new Vector2(initialPosition)
  position = new Vector2(initialPosition)
  var rotation_angle = 0
  private var car_bitmap = new BitmapImage(imageFile)
  /**
   * The currently selected sprite for animation
   */
  private val textureX:Int = 0
  private var textureY:Int = 1
  private var speed:Float = sp.toFloat
  private var dt:Double = 0
  private var currentFrame:Int = 0
  private val nFrames:Int = 4
  final private val FRAME_TIME:Float = 0.1f // Duration of each frime

  private var lastPosition: Vector2 = new Vector2(initialPosition)
  private var newPosition: Vector2 = new Vector2(initialPosition)
  private var position: Vector2 = new Vector2(initialPosition)
  final private val img = new BitmapImage("data/images/pipe.png")
  private var move = false
  var direction = initialDirection

  /**
   * Create the Car at the start position (0,0)
   */

  /**
   * Create the Car at the given start tile.
   *
   * @param x Column
   * @param y Line
   */
  def this(x: Int, y: Int,imageFile:String,initialDirection:Int) {
    this(new Vector2(Car.SPRITE_WIDTH * x, Car.SPRITE_HEIGHT * y),imageFile,initialDirection)
  }

  def this(x: Int, y: Int, imageFile: String) {
    this(new Vector2(Car.SPRITE_WIDTH * x, Car.SPRITE_HEIGHT * y), imageFile)
  }

  def this(x: Int, y: Int, imageFile: String, speed: Double) {
    this(new Vector2(Car.SPRITE_WIDTH * x, Car.SPRITE_HEIGHT * y), imageFile, speed)
  }

  def this(x: Int, y: Int, imageFile: String, speed: Double,initialDirection:Int) {
    this(new Vector2(Car.SPRITE_WIDTH * x, Car.SPRITE_HEIGHT * y), imageFile, speed,initialDirection)
  }

  /**
   * @return the current position of the Car on the map.
   */
  def getPosition: Vector2 = this.position

  def setPosition(new_vector:Vector2):Unit = {
    this.lastPosition = new_vector
    this.newPosition = new_vector
  }

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
        rotation_angle = 0
        textureY=2
      case Car.Direction.LEFT =>
        rotation_angle = 180
        textureY = 1

      case Car.Direction.UP =>
        rotation_angle = 90
        textureY = 3

      case Car.Direction.DOWN =>
        rotation_angle = 270
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
    g.drawTransformedPicture(position.x+Car.SPRITE_WIDTH/2, position.y+Car.SPRITE_HEIGHT/2, Car.SPRITE_WIDTH, Car.SPRITE_HEIGHT, rotation_angle, 1.0f,car_bitmap)
  }
}
