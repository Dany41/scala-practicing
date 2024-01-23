package adt

/**
 * MixedAlgebraic is Hybrid ADT
 * It is either SumAlgebraic or ProductAlgebraic
 *  MixedAlgebraic = SumAlgebraic XOR ((all possible values for Long) * (all possible values for String))
 */

sealed trait HybridAlgebraic

case object SumAlgebraic extends HybridAlgebraic

case class ProductAlgebraic(number: Long, uuid: String) extends HybridAlgebraic
