package net.kogics.kojo.lite

import java.net.URLDecoder

object NewKojoInstance extends StubMain {
   lazy val classpath = {
    val urls = Thread.currentThread.getContextClassLoader match {
      case cl: java.net.URLClassLoader => cl.getURLs.toList
      case _                           => sys.error("classloader is not a URLClassLoader")
    }
    
    createCp(urls map { URLDecoder decode _.getPath })
  }

  def firstInstance = true
  def firstMain(args: Array[String]): Unit = {}
  def firstMainDone(): Unit = {}
  def nthMain(args: Array[String]) = throw new UnsupportedOperationException
  override def log(msg: String): Unit = {}
  override def done(): Unit = { }
}