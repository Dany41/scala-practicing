package types

object TypeProjections extends App {

  val a = new Aaaa()
  val b: a.B = new a.B()

  var b2: Aaaa#B = new a.B()

  println(b.method())
  println(b2.method())

  val a2 = new Aaaa()
  b2 = new a2.B()

  println(b2.method())

  def foo[M[_]](f : M[Int]) = f

  foo[({type X[Y] = Y => Unit})#X]((shlapa : Int) => println(shlapa) )
//  foo[({type X[Y] = Function1[Y, Unit]})#X]((x : Int) => println(x) )
//  foo[Function1[Y, Unit]]((x : Int) => println(x) )
//  foo[Function1[Y, Unit]]((x : Int) => println(x) )

}

class Aaaa {

  class B {
    def method() = "Bb"
  }

}
