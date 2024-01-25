package adt

/**
 * ADT - Algebraic Data Type
 * Here is one of the types of ADT - sum
 * From types point of view the SumAlgebraic type is:
 *  SumAlgebraic = Example_1 + Example_2 + Example_3
 * Or in 'logical' terms - SumAlgebraic is one of the implementations
 * It is either Example_1 ot Example_2 or Example_3, but only one of them
 *  SumAlgebraic = Example_1 XOR Example_2 XOR Example_3
 */

sealed trait SumAlgebraic

case object Example_1 extends SumAlgebraic
case object Example_2 extends SumAlgebraic
case object Example_3 extends SumAlgebraic
