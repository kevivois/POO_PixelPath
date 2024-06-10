package Cars

object WhiteCar {
  val FILEPATH = "data/images/characters/car/WhiteStrip.png"
}
class WhiteCar(x: Int, y: Int,car_speed:Double=1.5,initialDirection:Int=1) extends Car(x,y,WhiteCar.FILEPATH,car_speed,initialDirection){

}
