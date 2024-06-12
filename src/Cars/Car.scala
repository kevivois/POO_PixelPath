package Cars

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.math.{Interpolation, Vector2}

import scala.util.Random

object Car {
  object Direction extends Enumeration {
    type Direction = Value
    val UP, DOWN, RIGHT, LEFT, NULL = Value
  }

  val SPRITE_WIDTH = 64
  val SPRITE_HEIGHT = 32

  def get_random_car(x: Int, y: Int, car_speed: Double = 1.5, initialDirection: Int = 1): Car = {
    Random.between(0, 6) match {
      case 0 => new SimpleCar(x, y, car_speed, initialDirection)
      case 1 => new GreenCar(x, y, car_speed, initialDirection)
      case 2 => new RedCar(x, y, car_speed, initialDirection)
      case 3 => new PinkCar(x, y, car_speed, initialDirection)
      case 4 => new WhiteCar(x, y, car_speed, initialDirection)
      case 5 => new BlueCar(x, y, car_speed, initialDirection)
      case _ => new SimpleCar(x, y, car_speed, initialDirection)
    }
  }
}

class Car(initialPosition: Vector2, imageFile: String, sp: Double = 1.5, initialDirection: Int = 1) extends DrawableObject {
  private val carBitmap:BitmapImage = new BitmapImage(imageFile)
  private var lastPosition:Vector2 = new Vector2(initialPosition)
  private var newPosition:Vector2 = new Vector2(initialPosition)
  private var position:Vector2 = new Vector2(initialPosition)
  private var speed: Float = sp.toFloat
  private var dt: Double = 0
  private var currentFrame: Int = 0
  private val nFrames: Int = 4
  private val FRAME_TIME: Float = 0.1f
  private var move:Boolean = false
  var rotationAngle:Int = 0
  var direction:Int = initialDirection

  def this(x: Int, y: Int, imageFile: String, speed: Double, initialDirection: Int) {
    this(new Vector2(Car.SPRITE_WIDTH * x, Car.SPRITE_HEIGHT * y), imageFile, speed, initialDirection)
  }

  def this(x: Int, y: Int, imageFile: String, speed: Double) {
    this(x, y, imageFile, speed, 1)
  }

  def this(x: Int, y: Int, imageFile: String) {
    this(x, y, imageFile, 1.5)
  }

  def getPosition: Vector2 = position

  def setPosition(newVector: Vector2): Unit = {
    move = true
    lastPosition = new Vector2(newVector.x - (direction * Car.SPRITE_WIDTH), newVector.y)
    newPosition = newVector
  }

  def animate(elapsedTime: Double): Unit = {
    val frameTime = FRAME_TIME / speed
    position = new Vector2(lastPosition)
    if (move) {
      dt += elapsedTime
      val alpha: Float = ((dt + frameTime * currentFrame) / (frameTime * nFrames)).toFloat
      position.interpolate(newPosition, alpha, Interpolation.linear)
    } else {
      dt = 0
    }
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

  def isMoving: Boolean = move

  def setSpeed(speed: Float): Unit = this.speed = speed

  def go(direction: Car.Direction.Value): Unit = {
    move = true
    direction match {
      case Car.Direction.RIGHT =>
        newPosition.add(Car.SPRITE_WIDTH, 0)
        rotationAngle = 0
      case Car.Direction.LEFT =>
        newPosition.add(-Car.SPRITE_WIDTH, 0)
        rotationAngle = 180
      case Car.Direction.UP =>
        newPosition.add(0, Car.SPRITE_HEIGHT)
        rotationAngle = 90
      case Car.Direction.DOWN =>
        newPosition.add(0, -Car.SPRITE_HEIGHT)
        rotationAngle = 270
      case _ =>
    }
  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawTransformedPicture(position.x + Car.SPRITE_WIDTH / 2.0f, position.y + Car.SPRITE_HEIGHT / 2.0f, Car.SPRITE_WIDTH / 2.0f, Car.SPRITE_HEIGHT, rotationAngle, 1.0f, carBitmap)
  }
}
