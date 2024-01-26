package destructors

sealed trait CustomWrapper[+T] {
  def isEmpty: Boolean
  def get: T
}

class CustomWrapperFrilled[T](val value: T) extends CustomWrapper[T] {
  def isEmpty = false
  def get: T = value
}

case object CustomWrapperEmpty extends CustomWrapper[Nothing] {
  def isEmpty = true
  def get = throw new UnsupportedOperationException()
}
