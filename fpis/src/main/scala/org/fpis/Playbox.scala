package org.fpis

import org.fpis.examples.List
import List.*

object Playbox extends App:

  @annotation.nowarn // Scala gives a hint here via a warning, so let's disable that
  val result = List(1, 2, 3, 4, 5) match
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101

  // todo: rewrite via tests
//  println(List.result)

//  println(drop(List(1, 2, 3, 4, 5), 0))
//  println(drop(List(1, 2, 3, 4, 5), 1))
//  println(drop(List(1, 2, 3, 4, 5), 2))
//  println(drop(List(1, 2, 3, 4, 5), 3))
//  println(drop(List(1, 2, 3, 4, 5), 4))
//  println(drop(List(1, 2, 3, 4, 5), 5))
//  println(drop(List(1, 2, 3, 4, 5), 6))

//  println(dropWhile(List(1, 2, 3, 4, 5), _ < 1))
//  println(dropWhile(List(1, 2, 3, 4, 5), _ < 2))
//  println(dropWhile(List(1, 2, 3, 4, 5), _ < 3))
//  println(dropWhile(List(1, 2, 3, 4, 5), _ < 4))
//  println(dropWhile(List(1, 2, 3, 4, 5), _ < 5))
//  println(dropWhile(List(1, 2, 3, 4, 5), _ < 6))

  println(init(List(1, 2, 3, 4, 5)))
  println(init(List(1, 2, 3, 4)))
  println(init(List(1, 2, 3)))
  println(init(List(1, 2)))
  println(init(List(1)))
  println(init(List()))
