package org.fpis.exercises

import org.fpis.exercises.SortedList.isSorted
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class SortedListTest extends AnyFlatSpec with should.Matchers {

  "testIsSorted" should "return correct results" in {
    isSorted(Array(1, 2, 3), _ < _) should be (true)
    isSorted(Array(1, 2, 1), _ < _) should be (false)
    isSorted(Array(3, 2, 1), _ > _) should be (true)
    isSorted(Array(1, 2, 3), _ > _) should be (false)
  }

}
