package net.kogics.kojo.lite

trait PaperSize {
  def name: String
  def width: Double
  def height: Double
}

case object A4 extends PaperSize {
  val name = "A4"
  val width = 8.268
  val height = 11.693
}

case object A4Landscape extends PaperSize {
  val name = "A4 Landscape"
  val width = 11.693
  val height = 8.268
}

object PaperSize {
  def fromString(s: String): Option[PaperSize] = {
    if (s == null) {
      None
    }
    else {
      s.toLowerCase.trim match {
        case "a4"           => Some(A4)
        case "a4 landscape" => Some(A4Landscape)
        case _              => None
      }
    }
  }

  def allSizes: Seq[String] = {
    List(A4, A4Landscape).map(_.name)
  }
}
