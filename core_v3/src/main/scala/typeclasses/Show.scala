package typeclasses

import scala.annotation.tailrec
import scala.compiletime.{constValue, erasedValue, summonFrom, summonInline}
import scala.deriving.Mirror

// scala 2 approach
trait Show[A]:
  def show(a: A): String

implicit class RichPerson(person: Person):
  def show()(implicit sp: Show[Person]): String = s"Person's name is ${sp.show(person)}"

case class Person(name: String)
object Person:
  implicit val showPerson: Show[Person] = (a: Person) => s"name = ${a.name}"


// scala 3 approach
trait Show2[A]:
  extension (a: A) def show2(): String

object Show2:
  given Show2[Person] with
    extension (p: Person) def show2(): String = s"Person's name is ${p.name}"

  given Show2[String] with
    extension (s: String) def show2(): String = s"""$s"""

  given Show2[Int] with
    extension (i: Int) def show2(): String = i.toString

//  given Show2[Any] with
//    extension (a: Any) def show2(): String = a.toString

  inline def derived[A](using m: Mirror.Of[A]): Show2[A] = new Show2[A]:
    extension (a: A)
      override def show2(): String = inline m match
        case p: Mirror.ProductOf[A] => showProduct(p, a)
        case s: Mirror.SumOf[A] => showSum(s, a)

  private inline def elemLabels[T <: Tuple]: List[String] = inline erasedValue[T] match
    case _: EmptyTuple => Nil
    case _:(t *: ts) => constValue[t].asInstanceOf[String] :: elemLabels[ts]

  private inline def summonAll[T <: Tuple]: List[Show2[_]] = inline erasedValue[T] match
    case _: EmptyTuple => Nil
    case _:(t *: ts) => summonInline[Show2[t]] :: summonAll[ts] // won't compile without 'inline' keyword

  private inline def showProduct[A](p: Mirror.ProductOf[A], a: A): String =
    val productName: String = constValue[p.MirroredLabel] // class name

    val names: List[String] = elemLabels[p.MirroredElemLabels]
    val instances: List[Show2[_]] = summonAll[p.MirroredElemTypes]
    val values: List[Any] = a.asInstanceOf[Product].productIterator.toList

    val fields = (names zip (instances zip values)).map {
      case (name, (instance, value)) =>
        s"$name = ${instance.asInstanceOf[Show2[Any]].show2(value)()}"
    }

    s"$productName(${fields.mkString(", ")})"

//  @tailrec // won't compile as inline functions cannot be tailrec......
  private inline def showCase[A, T](n: Int, ord: Int, a: A): String =
    inline erasedValue[T] match
      case _: (t *: ts) =>
        if n == ord then
          summonFrom {
            case p: Mirror.ProductOf[`t`] => showProduct[t](p, a.asInstanceOf[t])
          }
        else
          showCase[A, ts](n + 1, ord, a)
      case _: EmptyTuple => ""

  private inline def showSum[A](s: Mirror.SumOf[A], a: A): String =
    val ord = s.ordinal(a)
    showCase[A, s.MirroredElemTypes](0, ord, a)


case class Person2(name: String) derives Show2
case class Person3(name: String, age: Int) derives Show2

enum O derives Show2:
  case O1(someCoolFieldGuyNotGay: String)
  case O2

enum A derives Show2:
  case B(in: O)
  case C
  case D
  case K

@main def main(): Unit = {
  // to write person.show you need to define implicit classes
  println(Person("Chris").show())

  // in scala 3 it is now more convenient
  import Show2.given
  println(Person("Chris").show2())

  println(Person2("Somebody").show2())

  println(Person3("Hello", 2).show2())
  println(A.B(O.O1("actually straight")).show2())

  val d = Seq(1,2,3)

  List(1,2,3)

  val im: Mirror.ProductOf[Person2] = implicitly[Mirror.ProductOf[Person2]]

}
