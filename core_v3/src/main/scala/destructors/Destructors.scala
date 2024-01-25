package destructors

object Destructors extends App {

  val s: String = "asasas"

  s match
    case ISBN13(ean, group, publisher, title, chkDigit) => println(title)
    case _ => println(1)

}
