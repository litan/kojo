import io.Source
import java.io.File
import sys.process.stringToProcess
val jars = Source.fromFile("/home/lalit/work/KojoDev/KojoLite/installer/jarlist.txt")
jars.getLines.foreach { j =>
    s"cp -v $j /home/lalit/work/KojoDev/KojoLite/installerbuild/lib/"!
}