package types

import java.util
import scala.collection.{Traversable}

object TypeConstraints extends App {

  val x = new A {type B = Traversable[Int]}
  println(x.foo(Set(1)))

  // incompatible types
//  val y = new A {type B = Set[Int]}

  val aLower = new ALower {type B = List[Int]}
  println(aLower.sum(List(1,2,3)))

  def foo[F[_]](f: F[Int]): Unit = f

  type Callback[T] = T => Unit

  foo[Callback](x => println(3 + x))

  def foo2[F[_, _]](f: F[Int, Int]) = f

  foo2[Function1](a => a + 2)(4)

  val myFunc1 = new MyFunc[Any, String] {
    override def apply[A1 <: Any, B1 >: String](a: A1): B1 = a.toString
  }

  val myFunc2: MyFunc[String, Any] = myFunc1

  val list1: List[_] = List()
  val list2: List[x forSome { type x }] = list1

  val listWithType: ListWithType = new ListWithType { type T = Int }
//  println(listWithType.method(1))

    // Consumer[Object]
    // Consumer[String]
    // String extends Object
  // Consumer[Object] extends Consumer[String]
  // val cons: Consumer[String] = (s: Str) => s.substring(0,1)
  // val cons2: Consumer[Object] = cons

  val cons: MyConsumer[String] = new MyConsumer[String] {
    override def consume[T1 <: String](t: T1): Unit = t.substring(0,1)
  }
  val cons2: MyConsumer[Object] = new MyConsumer[Object] {
    override def consume[T1 <: Object](t: T1): Unit = t.hashCode()
  }
  val cons3: MyConsumer[String] = cons2

}

trait MyConsumer[-T] {
  def consume[T1 <: T](t: T1): Unit
}

class ListWithType {
  type T
  def method(t: T) = t.toString
}


class A {
  type B >: List[Int]
    def foo(a: B) = a
}

class ALower {
  type B <: Traversable[Int]
    def sum(b: B) = b.sum
}

trait MyFunc[-A, +B] {
  def apply[A1 <: A, B1 >: B](a: A1): B1
}
