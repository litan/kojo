lazy val scalaVer = "2.13.18"
name := "Kojo"
version := "2.9"
scalaVersion := scalaVer
run / fork := true
scalacOptions := Seq("-feature", "-deprecation")
// The CMS flags target Java 8. Java 14 removed them, and a JVM given them there
// refuses to start at all ("Unrecognized VM option"), so `sbt run` and `sbt test`
// both fail on a modern JDK. Add them only on a JVM that still understands them.
lazy val cmsFlags = {
  val major = System.getProperty("java.specification.version").split('.').last.toInt
  if (major < 14) Seq("-XX:+UseConcMarkSweepGC", "-XX:+CMSClassUnloadingEnabled") else Seq.empty[String]
}
run / javaOptions ++= Seq("-Xmx1024m", "-Xss1m") ++ cmsFlags

Test / fork := true
Test / parallelExecution := false
// Java 9+ needs these opens for the cglib/jmock based tests; harmless on Java 8.
lazy val testAddOpens =
  if (System.getProperty("java.specification.version").split('.').last.toInt >= 9)
    Seq("--add-opens", "java.base/java.lang=ALL-UNNAMED", "--add-opens", "java.base/java.util=ALL-UNNAMED")
  else Seq.empty[String]
Test / javaOptions ++= Seq("-Xmx1024m", "-Xss1m") ++ cmsFlags ++ testAddOpens
testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-s")

autoScalaLibrary := false

val refreshHindiBundle = taskKey[File]("Refresh the checked-in Hindi resource bundle from its UTF-8 source")

refreshHindiBundle := {
    val log = streams.value.log
    val source = baseDirectory.value / "src/main/i18n/Bundle_hi.properties"
    val cached = baseDirectory.value / "src/main/resources/net/kogics/kojo/lite/Bundle_hi.properties"
    val header = "# Generated from src/main/i18n/Bundle_hi.properties; do not edit this copy.\n" +
        "# Run ./sbt.sh refreshHindiBundle after editing the UTF-8 source.\n"
    val sourceText = IO.read(source, java.nio.charset.StandardCharsets.UTF_8)
    val escaped = sourceText.flatMap { c =>
        if (c > 0x7f) "\\u%04X".format(c.toInt) else c.toString
    }
    val generated = header + escaped
    if (!cached.exists || IO.read(cached, java.nio.charset.StandardCharsets.US_ASCII) != generated) {
        IO.write(cached, generated, java.nio.charset.StandardCharsets.US_ASCII)
        log.info("Refreshed the cached Hindi resource bundle")
    }
    cached
}

Compile / compile := ((Compile / compile) dependsOn refreshHindiBundle).value
Compile / unmanagedResources := ((Compile / unmanagedResources) dependsOn refreshHindiBundle).value

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-library" % scalaVer,
    "org.scala-lang" % "scala-compiler" % scalaVer,
    "org.scala-lang" % "scala-reflect" % scalaVer,
    "com.typesafe.akka" %% "akka-actor" % "2.6.16",
    "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
    "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
    "org.piccolo2d" % "piccolo2d-core" % "1.3.1",
    "org.piccolo2d" % "piccolo2d-extras" % "1.3.1",
    "com.formdev" % "flatlaf" % "3.4",
    "com.vividsolutions" % "jts" % "1.13" intransitive(),
    "com.h2database" % "h2" % "1.3.168",
    "org.apache.commons" % "commons-math3" % "3.6.1",
    "javax.xml.bind" % "jaxb-api" % "2.2",
    "com.sun.xml.bind" % "jaxb-impl" % "2.2",
    "org.apache.httpcomponents.client5" % "httpclient5" % "5.1.3",
    "org.slf4j" % "slf4j-jdk14" % "1.7.25",
    "org.scalatest" %% "scalatest" % "3.0.8" intransitive(),
    "org.scalactic" %% "scalactic" % "3.0.8" intransitive(),
    "junit" % "junit" % "4.10" % "test",
    "com.novocode" % "junit-interface" % "0.11" % "test",
    "org.jmock" % "jmock" % "2.5.1" % "test",
    "org.jmock" % "jmock-legacy" % "2.5.1" % "test",
    ("org.jmock" % "jmock-junit4" % "2.5.1" intransitive()) % "test",
    "cglib" % "cglib-nodep" % "2.1_3" % "test",
    "org.objenesis" % "objenesis" % "1.0" % "test",
    "org.hamcrest" % "hamcrest-core" % "1.1" % "test",
    "org.hamcrest" % "hamcrest-library" % "1.1" % "test",
    ("org.scalacheck"  %% "scalacheck" % "1.14.3" intransitive()) % "test",
    // the Turkish API exposes parallel sequences (ParDizi, in i18n/tr/dizin.scala),
    // and user scripts gain .par with it
    "org.scala-lang.modules" %% "scala-parallel-collections" % "1.2.0"
)

//Build distribution
val distOutpath             = settingKey[File]("Where to copy all dependencies and kojo")
val buildDist  = taskKey[Unit]("Copy runtime dependencies and built kojo to 'distOutpath'")

lazy val dist = project
  .in(file("."))
  .settings(
    distOutpath              := baseDirectory.value / "dist",
    buildDist   := {
      val allLibs:                List[File]          = (Runtime / dependencyClasspath).value.map(_.data).filter(_.isFile).toList
      val buildArtifact:          File                = (Runtime / packageBin).value
      val jars:                   List[File]          = buildArtifact :: allLibs
      val `mappings src->dest`:   List[(File, File)]  = jars.map(f => (f, distOutpath.value / f.getName))
      val log                                         = streams.value.log
      log.info(s"Copying to ${distOutpath.value}:")
      log.info(s"${`mappings src->dest`.map(f => s" * ${f._1}").mkString("\n")}")
      IO.copy(`mappings src->dest`)
    }
  )

ThisBuild / publishMavenStyle := false
