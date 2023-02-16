package net.kogics.kojo.lite

object Versions {
  val KojoMajorVersion = "2.9"
  val KojoVersion = "2.9.24"
  val KojoRevision = "r27"
  val KojoBuildDate = "15 February 2023"
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
