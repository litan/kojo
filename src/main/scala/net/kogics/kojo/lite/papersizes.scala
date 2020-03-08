package net.kogics.kojo.lite

trait PaperSize {
  def name: String
  def widthMm: Double
  def heightMm: Double
  def widthInches = widthMm / 25.4
  def heightInches = heightMm / 25.4
  def aspectRatio = widthMm / heightMm
}

case object A4 extends PaperSize {
  val name = "A4"
  val widthMm = 210.0
  val heightMm = 297.0
}

case object A4Landscape extends PaperSize {
  val name = "A4 Landscape"
  val widthMm = 297.0
  val heightMm = 210.0
}

case object A3 extends PaperSize {
  val name = "A3"
  val widthMm = 297.0
  val heightMm = 420.0
}

case object A3Landscape extends PaperSize {
  val name = "A3 Landscape"
  val widthMm = 420.0
  val heightMm = 297.0
}

case object A2 extends PaperSize {
  val name = "A2"
  val widthMm = 420.0
  val heightMm = 594.0
}

case object A2Landscape extends PaperSize {
  val name = "A2 Landscape"
  val widthMm = 594.0
  val heightMm = 420.0
}

case object A1 extends PaperSize {
  val name = "A1"
  val widthMm = 594.0
  val heightMm = 841.0
}

case object A1Landscape extends PaperSize {
  val name = "A1 Landscape"
  val widthMm = 841.0
  val heightMm = 594.0
}

case object A0 extends PaperSize {
  val name = "A0"
  val widthMm = 841.0
  val heightMm = 1189.0
}

case object A0Landscape extends PaperSize {
  val name = "A0 Landscape"
  val widthMm = 1189.0
  val heightMm = 841.0
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
        case "a3"           => Some(A3)
        case "a3 landscape" => Some(A3Landscape)
        case "a2"           => Some(A2)
        case "a2 landscape" => Some(A2Landscape)
        case "a1"           => Some(A1)
        case "a1 landscape" => Some(A1Landscape)
        case "a0"           => Some(A0)
        case "a0 landscape" => Some(A0Landscape)
        case _              => None
      }
    }
  }

  def allSizes: Seq[String] = {
    List(A4, A4Landscape, A3, A3Landscape, A2, A2Landscape, A1, A1Landscape, A0, A0Landscape).map(_.name)
  }
}
