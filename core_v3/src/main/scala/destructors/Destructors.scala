package destructors

object Destructors extends App {

  val s: String = "asasas"

  s match
    case ISBN13(ean, group, publisher, title, chkDigit) => println(title)
    case _ => println(1)

  val i: Int = 5

  i match
    case ISBN13(i2) => println("Custom Wrapper did a good job")
    case _ => println("Custom Wrapper did a bad job")

  val l: Long = 10L

  l match
    case ISBN13(l2) => println("Custom Wrapper did a bad job")
    case _ => println("Custom Wrapper did a good job")

}
