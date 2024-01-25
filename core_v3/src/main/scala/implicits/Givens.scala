package implicits

object Givens {

  given personOrderingByAge: Ordering[Person] with {
    override def compare(x: Person, y: Person): Int = x.age.compareTo(y.age)
  }

  given optionOrdering[T](using ordering: Ordering[T]): Ordering[Option[T]] with {
    override def compare(x: Option[T], y: Option[T]): Int =
      (x, y) match
        case (None, None) => 0
        case (None, _) => -1
        case (_, None) => 1
        case (Some(xVal), Some(yVal)) => ordering.compare(xVal, yVal)
  }

}
