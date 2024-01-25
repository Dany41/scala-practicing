package implicits

object CircleHelpers {

  extension (c: Circle) {
    def circumference: Double = c.radius * math.Pi * 2
  }
  
}
extension (c: Circle) {
  def area: Double = c.radius * c.radius * math.Pi
}
