package Cars

object PinkCar {
  val FILEPATH = "data/images/characters/car/PinkStrip.png"
}
class PinkCar(x: Int, y: Int,car_speed:Double=1.5,initialDirection:Int=1) extends Car(x,y,PinkCar.FILEPATH,car_speed,initialDirection){

}
