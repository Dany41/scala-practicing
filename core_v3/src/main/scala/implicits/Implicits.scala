package implicits

/**
 * import StandardValues._ -> import all members except of givens
 * import StandardValues.{given _} -> import all givens (but it seems like it does not work :))
 */

import Givens.{given Ordering[_], given Ordering[Option[_]]}

object Implicits extends App {

  val sortedPeople = Person.listPeople(List(Person("Ccc", "Cc", 1), Person("Bbb", "Bb", 2), Person("Aaa", "Aa", 3)))(using Person.personOrdering)
  val reverseSortedPeople = Person.listPeople(List(Person("Aaa", "Aa", 1), Person("Bbb", "Bb", 2), Person("Ccc", "Cc", 3)))(using Person.personReverseOrdering)
  assert(sortedPeople.head.surname == "Aaa")
  assert(reverseSortedPeople.head.surname == "Ccc")

  val sortedPeopleByAge = Person.listPeople(List(Person("Ccc", "Cc", 3), Person("Bbb", "Bb", 2), Person("Aaa", "Aa", 1)))
  assert(sortedPeopleByAge.head.age == 1)

  val sortedOptionPeopleByAge = sortThing(List(Option(Person("Ccc", "Cc", 3)), None, Option(Person("Bbb", "Bb", 2)), Option(Person("Aaa", "Aa", 1))))(using optionOrdering)
  assert(sortedOptionPeopleByAge.head.isEmpty)


  def sortThing[T](things: List[T])(using ordering: Ordering[T]) = things.sorted

}
