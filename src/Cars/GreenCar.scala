package Cars

object GreenCar {
  val FILEPATH = "data/images/characters/car/GreenStrip.png"
}
class GreenCar(x: Int, y: Int,car_speed:Double=1.5,initialDirection:Int=1) extends Car(x,y,GreenCar.FILEPATH,car_speed,initialDirection){

}
