package org.fpis.examples

import scala.annotation.tailrec

class Fibonacci

object Fibonacci:
  def fib(n: Int): Int =
    @tailrec
    def helper(n: Int, current: Int, next: Int): Int =
      if n <= 1 then
        current
      else
        helper(n - 1, next, current + next)
  
    helper(n, 0, 1)

