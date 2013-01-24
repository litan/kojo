package net.kogics.kojo.lite

object Versions {
    val KojoVersion = "240113-1"
    val JavaVersion = geogebra.main.AppD.getJavaVersion
    val ScalaVersion = scala.tools.nsc.Properties.versionString.substring("version ".length)
}