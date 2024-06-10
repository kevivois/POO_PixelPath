package Cars

object BlueCar {
  val FILEPATH = "data/images/characters/car/BlueStrip.png"
}
class BlueCar(x: Int, y: Int,car_speed:Double=1.5,initialDirection:Int=1) extends Car(x,y,BlueCar.FILEPATH,car_speed,initialDirection){

}
