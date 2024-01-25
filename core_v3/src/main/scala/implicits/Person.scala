package implicits

case class Person(surname: String, name: String, age: Int)

object Person {

  given personOrdering: Ordering[Person] with {
    override def compare(x: Person, y: Person): Int = x.surname.compareTo(y.surname)
  }

  given personReverseOrdering: Ordering[Person] with {
    override def compare(x: Person, y: Person): Int = y.surname.compareTo(x.surname)
  }

  def listPeople(people: List[Person])(using Ordering[Person]): Seq[Person] = people.sorted

}