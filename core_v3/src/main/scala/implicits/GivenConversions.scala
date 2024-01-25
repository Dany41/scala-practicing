package implicits

object GivenConversions {
  
  given stringToPerson: Conversion[String, Person] with {
    def apply(s: String): Person = Person(s, "Ccc", 20)
  } 

}
