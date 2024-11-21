package net.kogics.kojo.lite

object Versions {
  val KojoMajorVersion = "2.9"
  val KojoVersion = "2.9.30"
  val KojoRevision = "r9"
  val KojoBuildDate = "21 Nov 2024"
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
