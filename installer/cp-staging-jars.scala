import io.Source
import java.io.File
import sys.process.stringToProcess

val jars = Source.fromFile("./jarlist.txt")
jars.getLines.foreach { j =>
  val srcDest = j split " -> "
  val src = srcDest(0)
  val dest = if (srcDest.size > 1) srcDest(1) else ""
  s"cp -v $src ../installerbuild/lib/$dest"!
}