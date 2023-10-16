package org.fpis.exercises

import scala.annotation.tailrec

class SortedList

object SortedList:

  def isSorted[A](as: Array[A], gt: (A, A) => Boolean): Boolean =
    @tailrec
    def helper(n: Int): Boolean =
      if n == as.length - 2 then
        !gt(as(n), as.last)
      else if gt(as(n), as(n + 1)) then
        false
      else helper(n + 1)

    helper(0)
