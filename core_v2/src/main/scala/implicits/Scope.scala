package implicits
object Scope {

  def main(args: Array[String]): Unit = {

    testSamePackage()
    testWildcardImports()
    testExplicitImport()
    testInlineDefinition()

  }

  private def testInlineDefinition() = {
    val x = "Inline definition x"
    import Explicit.x
    import Wildcard._
    println(x)
  }

  private def testExplicitImport() = {
    import Explicit.x
    import Wildcard._
    println(x)
  }

  private def testWildcardImports() = {
    import Wildcard._
    println(x)
  }

  private def testSamePackage() = println(x)

}

object Wildcard {
  def x = "Wildcard Import x"
}

object Explicit {
  def x = "Explicit Import x"

}

