package adt

/**
 * MixedAlgebraic is Hybrid ADT
 * It is either SumADT xor ProductADT
 *  MixedAlgebraic = SumADT XOR ((all possible values for Long) * (all possible values for String))
 */

sealed trait HybridAlgebraic

case object SumADT extends HybridAlgebraic

case class ProductADT(number: Long, uuid: String) extends HybridAlgebraic
