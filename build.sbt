lazy val scalaVer = "2.13.16"
name := "Kojo"
version := "2.9"
scalaVersion := scalaVer
run / fork := true
scalacOptions := Seq("-feature", "-deprecation")
run / javaOptions ++= Seq("-Xmx1024m", "-Xss1m", "-XX:+UseConcMarkSweepGC", "-XX:+CMSClassUnloadingEnabled")

Test / fork := true
Test / javaOptions ++= Seq("-Xmx1024m", "-Xss1m", "-XX:+UseConcMarkSweepGC", "-XX:+CMSClassUnloadingEnabled")
testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-s")

autoScalaLibrary := false

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
    "org.jmock" % "jmock" % "2.5.1" % "test",
    "org.jmock" % "jmock-legacy" % "2.5.1" % "test",
    ("org.jmock" % "jmock-junit4" % "2.5.1" intransitive()) % "test",
    "cglib" % "cglib-nodep" % "2.1_3" % "test",
    "org.objenesis" % "objenesis" % "1.0" % "test",
    "org.hamcrest" % "hamcrest-core" % "1.1" % "test",
    "org.hamcrest" % "hamcrest-library" % "1.1" % "test",
    ("org.scalacheck"  %% "scalacheck" % "1.14.3" intransitive()) % "test"
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
