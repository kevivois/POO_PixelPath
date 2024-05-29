import ch.hevs.gdx2d.lib.GdxGraphics

import scala.collection.mutable.ArrayBuffer

class CarManager {
  private var cars:ArrayBuffer[Car] = new ArrayBuffer[Car]()
  def add_car(start_position_x:Int = 0,start_position_y:Int = 0,imageFile:String=SimpleCar.FILEPATH):Car = {
    var newCar:Car = new Car(start_position_x,start_position_x,imageFile)
    cars.addOne(newCar)
    newCar
  }
  def get_cars:ArrayBuffer[Car] = cars


  def draw(g:GdxGraphics):Unit = {
    for(car:Car <- get_cars){
      car.draw(g)
    }
  }

}
