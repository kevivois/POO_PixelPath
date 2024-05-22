package ch.hevs.gdx2d.demos.tilemap.advanced

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

  private val SPRITE_WIDTH = 32
  private val SPRITE_HEIGHT = 32
}

class Hero(initialPosition: Vector2)

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
  private[advanced] val textureX = 0
  private[advanced] var textureY = 1
  private[advanced] var speed = 1
  private[advanced] var dt = 0
  private[advanced] var currentFrame = 0
  private[advanced] val nFrames = 4
  final private[advanced] val FRAME_TIME = 0.1f // Duration of each frime

  private[advanced] var ss: Spritesheet = null
  private[advanced] var lastPosition: Vector2 = null
  private[advanced] var newPosition: Vector2 = null
  private[advanced] var position: Vector2 = null
  final private[advanced] val img = new BitmapImage("data/images/pipe.png")
  private var move = false

  /**
   * Create the hero at the start position (0,0)
   */
  def this {
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
      val alpha = (dt + frameTime * currentFrame) / (frameTime * nFrames)
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
  def go(direction: Hero.Direction): Unit = {
    move = true
    direction match {
      case RIGHT =>
        newPosition.add(Hero.SPRITE_WIDTH, 0)

      case LEFT =>
        newPosition.add(-Hero.SPRITE_WIDTH, 0)

      case UP =>
        newPosition.add(0, Hero.SPRITE_HEIGHT)

      case DOWN =>
        newPosition.add(0, -Hero.SPRITE_HEIGHT)

      case _ =>

    }
    turn(direction)
  }

  /**
   * Turn the hero on the given direction without do any step.
   *
   * @param direction The direction to turn.
   */
  def turn(direction: Hero.Direction): Unit = {
    direction match {
      case RIGHT =>
        textureY = 2

      case LEFT =>
        textureY = 1

      case UP =>
        textureY = 3

      case DOWN =>
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
    g.draw(ss.sprites(textureY)(currentFrame), position.x, position.y)
  }
}
