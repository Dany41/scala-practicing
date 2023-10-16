package org.fpis.exercises

import org.fpis.exercises.Currying.{compose, curry, uncurry}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class CurryingTest extends AnyFlatSpec with should.Matchers {

  "testCurry" should "work properly with functions" in {
    val fun = (a: Int, b: Int) => a + b
    val expected = fun(1, 2)

    val result = curry(fun)(1)(2)
    result should be (expected)
  }

  "testUncurry" should "work properly with functions" in {
    val fun = (a: Int) => (b: Int) => a + b
    val expected = fun(1)(2)

    val result = uncurry(fun)(1, 2)
    result should be(expected)
  }

  "compose" should "work properly with functions" in {
    val fun1 = (a: Int) => a + 5
    val fun2 = (b: Int) => b * 3
    val expected = fun2(fun1(2))

    val result = compose(fun2, fun1)(2)
    result should be(expected)
  }

}
