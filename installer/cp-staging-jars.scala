import io.Source
import java.io.File
import sys.process.stringToProcess

val jars = Source.fromFile("/home/lalit/work/KojoDev/KojoLite/installer/jarlist.txt")
jars.getLines.foreach { j =>
  val srcDest = j split " -> "
  val src = srcDest(0)
  val dest = if (srcDest.size > 1) srcDest(1) else ""
  s"cp -v $src /home/lalit/work/KojoDev/KojoLite/installerbuild/lib/$dest"!
}