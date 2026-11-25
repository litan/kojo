import io.Source
import sys.process._

val jars = Source.fromFile("./jarlist.txt")
try {
  jars.getLines.foreach { j =>
    val srcDest = j split " -> "
    val src = srcDest(0)
    val dest = if (srcDest.size > 1) srcDest(1) else ""
    val status = Seq("cp", "-v", src, s"../installerbuild/lib/$dest").!
    require(status == 0, s"Could not stage $src (cp exited with status $status)")
  }
}
finally jars.close()
