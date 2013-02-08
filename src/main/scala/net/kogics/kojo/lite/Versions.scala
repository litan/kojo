package net.kogics.kojo.lite

object Versions {
    val KojoVersion = "080213-5"
    val JavaVersion = geogebra.main.AppD.getJavaVersion
    val ScalaVersion = scala.tools.nsc.Properties.versionString.substring("version ".length)
}