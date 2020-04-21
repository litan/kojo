package net.kogics.kojo.lite

import java.io.File

object NewKojoInstance extends StubMain {
   lazy val classpath = {
     val cp = System.getProperty("java.class.path").split(File.pathSeparatorChar).toList
     createCp(cp)
  }

  def firstInstance = true
  def firstMain(args: Array[String]): Unit = {}
  def firstMainDone(): Unit = {}
  def nthMain(args: Array[String]) = throw new UnsupportedOperationException
  override def log(msg: String): Unit = {}
  override def done(): Unit = { }
}