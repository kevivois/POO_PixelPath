import ch.hevs.gdx2d.components.bitmaps.{BitmapImage, Spritesheet}
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.math.{Interpolation, Vector2}

object Hero {
  object Direction extends Enumeration {
    type Direction = Value
    val UP, DOWN, RIGHT, LEFT, NULL = Value
  }

  val SPRITE_WIDTH = 32
  val SPRITE_HEIGHT = 32
}

class Hero(initialPosition: Vector2, spd: Float = 1) extends DrawableObject {
  private val ss:Spritesheet = new Spritesheet("data/images/lumberjack_sheet32.png", Hero.SPRITE_WIDTH, Hero.SPRITE_HEIGHT)
  private val FRAME_TIME:Float = 0.1f
  private val nFrames:Int = 4

  private var lastPosition:Vector2 = new Vector2(initialPosition)
  private var newPosition:Vector2 = new Vector2(initialPosition)
  private var position:Vector2 = new Vector2(initialPosition)
  private var textureY:Int = 1
  private var speed:Float = spd
  private var dt:Double = 0.0
  private var currentFrame:Int = 0
  private var currentDirection: Hero.Direction.Value = Hero.Direction.NULL
  private var move:Boolean = false

  def this() = this(new Vector2(0, 0))

  def this(x: Int, y: Int) = this(new Vector2(Hero.SPRITE_WIDTH * x, Hero.SPRITE_HEIGHT * y))

  def getPosition: Vector2 = position

  def animate(elapsedTime: Double): Unit = {
    val frameTime = FRAME_TIME / speed
    position = new Vector2(lastPosition)

    if (move) {
      dt += elapsedTime
      val alpha = ((dt + frameTime * currentFrame) / (frameTime * nFrames)).toFloat
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
        currentDirection = Hero.Direction.NULL
      }
    }
  }

  def isMoving: Boolean = move

  def setSpeed(speed: Float): Unit = this.speed = speed

  def setPosition(newVector: Vector2): Unit = {
    move = true
    lastPosition = new Vector2(newVector.x, newVector.y)
    newPosition = newVector
  }

  def go(direction: Hero.Direction.Value): Unit = {
    currentDirection = direction
    move = true
    direction match {
      case Hero.Direction.RIGHT => newPosition.add(Hero.SPRITE_WIDTH, 0)
      case Hero.Direction.LEFT => newPosition.add(-Hero.SPRITE_WIDTH, 0)
      case Hero.Direction.UP => newPosition.add(0, Hero.SPRITE_HEIGHT)
      case Hero.Direction.DOWN => newPosition.add(0, -Hero.SPRITE_HEIGHT)
      case _ =>
    }
    turn(direction)
  }

  def turn(direction: Hero.Direction.Value): Unit = {
    textureY = direction match {
      case Hero.Direction.RIGHT => 2
      case Hero.Direction.LEFT => 1
      case Hero.Direction.UP => 3
      case Hero.Direction.DOWN => 0
      case _ => textureY
    }
  }

  override def draw(g: GdxGraphics): Unit = {
    g.draw(ss.sprites(textureY)(currentFrame), position.x, position.y)
  }
}
