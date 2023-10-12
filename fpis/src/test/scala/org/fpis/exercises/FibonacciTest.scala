package org.fpis.exercises

import org.fpis.examples.Fibonacci
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class FibonacciTest extends AnyFlatSpec with should.Matchers {

  "fib" should "produce valid result" in {
    val result = Fibonacci.fib(5)

    result should be (3)
  }

  it should "return 0 if negative number is provided" in {
    val result = Fibonacci.fib(-1)

    result should be (0)
  }

}
