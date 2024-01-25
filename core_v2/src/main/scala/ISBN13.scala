object ISBN13 {
  def apply(ean: String, group: String, publisher: String, title: String, chkDigit: String): String =
    s"$ean-$group-$publisher-$title-$chkDigit"

  def unapply(s: String): (String, String, String, String, String) = ("a", "a", "a", "a", "a")

}
