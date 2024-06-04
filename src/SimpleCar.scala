import com.badlogic.gdx.math.Vector2
object SimpleCar {
  val FILEPATH = "data/images/characters/car/Blackout.png"
}
class SimpleCar(x: Int, y: Int,car_speed:Double=1.5,initialDirection:Int=1) extends Car(x,y,SimpleCar.FILEPATH,car_speed,initialDirection){

}
