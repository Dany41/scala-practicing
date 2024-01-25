package destructors

object ISBN13 {
  def apply(ean: String, group: String, publisher: String, title: String, chkDigit: String): String =
    s"$ean-$group-$publisher-$title-$chkDigit"

  def unapply(s: String): Option[(String, String, String, String, String)] = {
    val parts = s.split("-")
    if (parts.length == 5) {
      Some(parts(0), parts(1), parts(2), parts(3), parts(4))
    } else {
      None
    }
  }


}
