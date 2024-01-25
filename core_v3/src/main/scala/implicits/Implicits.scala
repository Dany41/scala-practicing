package implicits

/**
 * import StandardValues._ -> import all members except of givens
 * import StandardValues.{given _} -> import all givens (but it seems like it does not work :))
 */

import Givens.{given Ordering[_], given Ordering[Option[_]]}

import GivenConversions.stringToPerson

import CircleHelpers._

/**
 * In Scala 2 there were 3 main types of 'implicits':
 *  - val implicit -> passed to method calls (in Scala 3 reworked as givens)
 *  - def implicit -> used in type conversions
 *  - implicit class -> used as method extensions
 *
 * Givens Pros:
 *  - they must be explicitly imported unlike 'val implicit'
 *  - they are injected only in 'with' clauses, so readability improved
 *  - they can be anonymous (do not have a name)
 *  - using clause can also be anonymous (do no have a name)
 *  - to call a method with explicit 'given' we must type 'using', it protects us from overriding 'given' with usual params
 *
 * Implicit Conversion:
 *  - in Scala 3 it is given of Conversion[A, B] instance
 *
 * Extension:
 *  - it is added instead of implicit classes
 *  - you can declare extension methods wherever you want, if it is defined in scope of package object or just outside
 *    classes/objects (see extension#area and extension#diameter methods) - they are in scope without imports, otherwise
 *    you must explicitly or implicitly import them (for example CircleHelpers._ and CircleHelpers.circumference) works
 *    in the same way
 *  - it is still possible to have a lot of trash in the scope if lots of extensions defined outside classes/objects
 */

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

  assert("Aaa".age == 20)

  assert(Circle(1.0, 2.0, 3.0).diameter > 5)
  assert(Circle(1.0, 2.0, 3.0).area > 5)
  assert(Circle(1.0, 2.0, 3.0).circumference > 5)

}
