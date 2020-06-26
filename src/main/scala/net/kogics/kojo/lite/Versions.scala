package net.kogics.kojo.lite

object Versions {
  val KojoMajorVersion = "2.7"
  val KojoVersion = "2.9.04"
  val KojoRevision = "r6"
  val KojoBuildDate = "26 June 2020"
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