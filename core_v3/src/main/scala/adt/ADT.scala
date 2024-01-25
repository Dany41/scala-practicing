package adt

import scala.collection.immutable.{AbstractSeq, LinearSeq}

object ADT extends App {

  /**
   * Cons:
   *  No possibility to create object with illegal state. Used only for data transfer, no additional logic
   *  Compositionality - it is easy to compose ADTs
   *  Immutable
   *  Convenient for pattern matching, because compiler can guarantee type safety
   */

  // Pattern Matching examples on case class and Seq
  val pa = ProductAlgebraic(1, "A")
  pa match
    case ProductAlgebraic(1, "as") => println(1)
    case ProductAlgebraic(_, _) => println(2)

  val seq = Seq(1, 2, 3, 4)

  seq match
    case Seq(1, _) => println(1)
    case Seq(_, _, _, 3) => println(2)
    case Seq(_, _, _, 4) => println(3)

}
