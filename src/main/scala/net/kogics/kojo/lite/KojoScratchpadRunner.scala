package net.kogics.kojo.lite

import java.io.File

import scala.sys.process.Process
import scala.sys.process.ProcessLogger

object KojoScratchpadRunner {
  def newScratchPad(): Unit = {
    val classpath = System.getProperty("java.class.path")
    val javaHome = System.getProperty("java.home")
    val javaExec =
      if (new File(javaHome + "/bin/javaw.exe").exists)
        javaHome + "/bin/javaw"
      else
        javaHome + "/bin/java"

    val command =
      Seq(
        javaExec,
        "-cp",
        classpath,
        "net.kogics.kojo.lite.NewKojoInstance",
        "subKojo"
      )

    val processLogger = new ProcessLogger {
      override def out(s: => String): Unit = {}

      override def err(s: => String): Unit = {}

      override def buffer[T](f: => T): T = ???
    }

    val pb = Process(command)
    pb.run(processLogger)
  }

}
