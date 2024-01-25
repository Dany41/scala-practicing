package object implicits {

  extension (c: Circle) {
    def diameter: Double = c.radius * 2
  }

}
