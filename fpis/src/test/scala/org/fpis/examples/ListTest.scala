package org.fpis.examples

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest._

class ListTest extends AnyFlatSpec {

  "drop" should "return the same list if requested number to drop is 0" in {
    val list: List[Int] = List(1, 2 ,3)
    val resultedList: List[Int] = List.drop(list, 0)
    assert(list == resultedList)
  }

  it should "return Nil if all values are dropped" in {
    val list: List[Int] = List(1, 2, 3)
    val resultedList: List[Int] = List.drop(list, 3)
    assert(resultedList == List.Nil)
  }

  it should "correctly drop values" in {
    val list: List[Int] = List(1, 2, 3)
    val resultedList: List[Int] = List.drop(list, 2)
    assert(resultedList == List(3))
  }

  "sum" should "return zero for Nil" in {
    assert(List.sum(List()) == 0)
  }

  it should "return correct sums for List[Int]" in {
    assert(List.sum(List(1)) == 1)
    assert(List.sum(List(1, 2)) == 3)
    assert(List.sum(List(1, 2, 3)) == 6)
  }

  "product" should "return one for Nil" in {
    assert(List.product(List()) == 1)
  }

  it should "return correct products for List[Double]" in {
    assert(List.product(List(1)) == 1)
    assert(List.product(List(1, 2)) == 2)
    assert(List.product(List(1, 2, 0)) == 0)
  }

}
