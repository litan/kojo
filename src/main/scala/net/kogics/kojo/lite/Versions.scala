package net.kogics.kojo.lite

object Versions {
  val KojoMajorVersion = "2.7"
  val KojoVersion = "2.7.04"
  val KojoRevision = "r10"
  val KojoBuildDate = "27 October 2018"
  val JavaVersion = {
    val jrv = System.getProperty("java.runtime.version")
    val arch = System.getProperty("os.arch")
    if (jrv == null) {
      val jv = System.getProperty("java.version")
      s"$jv; $arch"
    }
    else {
      s"$jrv; $arch"
    }
  }
  val ScalaVersion = scala.tools.nsc.Properties.versionString.substring("version ".length)
}