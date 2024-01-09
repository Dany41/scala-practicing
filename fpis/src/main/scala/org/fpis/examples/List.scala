package org.fpis.examples


enum List[+A]:
  case Nil
  case Cons(head: A, tail: List[A])

object List:
  def apply[A](as: A*): List[A] =
    if as.isEmpty then Nil
    else Cons(as.head, apply(as.tail*))

  def sum(ints: List[Int]): Int = ints match
    case List.Nil => 0
    case List.Cons(head, tail) => head + sum(tail)

  def product(doubles: List[Double]): Double = doubles match
    case List.Nil => 1
    case List.Cons(0.0, _) => 0.0
    case List.Cons(head, tail) => head * product(tail)

  def tail[A](l: List[A]): List[A] = l match
    case List.Cons(_, tail) => tail
    case List.Nil => sys.error("Nil has no elements")

  def setHead[A](h: A, xs: List[A]): List[A] = xs match
    case List.Cons(_, t) => Cons(h, t)
    case List.Nil => sys.error("Nil has no elements")

  def drop[A](as: List[A], n: Int): List[A] =
    if n == 0 then as
    else as match
      case List.Cons(head, tail) => drop(tail, n - 1)
      case List.Nil => as

  def dropWhile[A](as: List[A], f: A => Boolean): List[A] = as match
      case List.Cons(head, tail) if f(head) => dropWhile(tail, f)
      case List.Nil | List.Cons(_, _) => as

  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match
      case Nil => a2
      case Cons(h, t) => Cons(h, append(t, a2))

  def init[A](as: List[A]): List[A] = as match
    case List.Nil | List.Cons(_, Nil) => List.Nil
    case List.Cons(head, Cons(_, Nil)) => List.Cons(head, Nil)
    case List.Cons(head, tail) => List.Cons(head, init(tail))

  val result = List(1, 2, 3, 4, 5) match
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
