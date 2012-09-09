name := "KojoLite"

version := "1.0"

scalaVersion := "2.9.2"

fork in run := true

javaOptions in run ++= Seq("-Dide.run=true", "-Xmx512m")
