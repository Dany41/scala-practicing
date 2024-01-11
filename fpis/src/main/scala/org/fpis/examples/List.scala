package org.fpis.examples

import scala.annotation.tailrec


enum List[+A]:
  case Nil
  case Cons(head: A, tail: List[A])

object List:
  def apply[A](as: A*): List[A] =
    if as.isEmpty then Nil
    else Cons(as.head, apply(as.tail*))

  def sum(ints: List[Int]): Int = foldLeft(ints, 0, _ + _)

  def product(doubles: List[Double]): Double = foldLeft(doubles, 1, _ * _)

  def tail[A](l: List[A]): List[A] = l match
    case List.Cons(_, tail) => tail
    case List.Nil => sys.error("Nil has no elements")

  def setHead[A](h: A, xs: List[A]): List[A] = xs match
    case List.Cons(_, t) => Cons(h, t)
    case List.Nil => Cons(h, Nil)

  @tailrec
  def drop[A](as: List[A], n: Int): List[A] =
    if n == 0 then as
    else as match
      case List.Cons(head, tail) => drop(tail, n - 1)
      case List.Nil => as

  @tailrec
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

  def foldRight[A, B](as: List[A], acc: B, f: (A, B) => B): B = foldLeft(reverse(as), acc, f)

  def length[A](as: List[A]): Int = foldLeft(as, 0, (_, i) => i + 1)

  @tailrec
  def foldLeft[A, B](as: List[A], acc: B, f: (A, B) => B): B = as match
    case List.Nil => acc
    case List.Cons(head, tail) => foldLeft(tail, f(head, acc), f)

  def reverse[A](as: List[A], acc: List[A] = List()): List[A] = foldLeft(as, List[A](), (el, l) => Cons(el, l))

  val result = List(1, 2, 3, 4, 5) match
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
