package Cars

object RedCar {
  val FILEPATH = "data/images/characters/car/RedStrip.png"
}
class RedCar(x: Int, y: Int,car_speed:Double=1.5,initialDirection:Int=1) extends Car(x,y,RedCar.FILEPATH,car_speed,initialDirection){

}
