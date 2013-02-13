package net.kogics.kojo.lite

object Versions {
    val KojoVersion = "120213-2"
    val JavaVersion = geogebra.main.AppD.getJavaVersion
    val ScalaVersion = scala.tools.nsc.Properties.versionString.substring("version ".length)
}