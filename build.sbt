name := "KojoLite"

version := "1.0"

scalaVersion := "2.9.2"

fork in run := true

javaOptions in run ++= Seq("-Dide.run=true", "-Xmx512m")

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % "2.9.2",
    "org.scala-lang" % "scala-swing" % "2.9.2",
    "org.scalariform" % "scalariform_2.9.2" % "0.1.2",
    "org.piccolo2d" % "piccolo2d-core" % "1.3.1",
    "org.piccolo2d" % "piccolo2d-extras" % "1.3.1",
    "com.vividsolutions" % "jts" % "1.12" intransitive(),
    "com.h2database" % "h2" % "1.3.168",
    "junit" % "junit" % "4.10" % "test",
    "org.jmock" % "jmock" % "2.5.1" % "test",
    "org.jmock" % "jmock-legacy" % "2.5.1" % "test",
    ("org.jmock" % "jmock-junit4" % "2.5.1" intransitive()) % "test",
    "cglib" % "cglib-nodep" % "2.1_3" % "test",
    "org.objenesis" % "objenesis" % "1.0" % "test",
    "org.hamcrest" % "hamcrest-core" % "1.1" % "test",
    "org.hamcrest" % "hamcrest-library" % "1.1" % "test",
    ("org.scalacheck" % "scalacheck_2.9.2" % "1.10.0" intransitive()) % "test",
    "org.scalatest" % "scalatest_2.9.2" % "1.8" % "test"
)