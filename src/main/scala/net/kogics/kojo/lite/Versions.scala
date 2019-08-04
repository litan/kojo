package net.kogics.kojo.lite

object Versions {
  val KojoMajorVersion = "2.7"
  val KojoVersion = "2.7.06"
  val KojoRevision = "r5"
  val KojoBuildDate = "4 August 2019"
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