/*
 * Copyright (C) 2026
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo.lite

import java.io.File

/**
 * Guards against a stray Scala toolchain jar shadowing the one Kojo ships.
 *
 * `StubMain.createCp` puts user-supplied jars - from ~/.kojo/lite/libk, the
 * extension dirs and KOJO_CLASSPATH - ahead of Kojo's own lib directory. A
 * scala-library, scala-reflect, scala-compiler or scalariform jar sitting in
 * one of those places therefore wins over Kojo's copy, and the mismatched
 * pair fails much later, deep inside the compiler, with an inscrutable error
 * (a scala-library-2.13.3.jar left in libk produced
 * `NoSuchMethodError: MurmurHash3$.caseClassHash(...)` at startup).
 *
 * Two guards live here: a name test used to skip such jars while the
 * classpath is being built, and a startup check that reports a toolchain
 * that is already mixed, naming the jar each part came from.
 */
object ScalaToolchainCheck {
  private val toolchainJarPrefixes = Seq("scala-library", "scala-reflect", "scala-compiler", "scalariform")

  /**
   * True for scala-library.jar, scala-library-2.13.3.jar, and Maven-style names
   * like scalariform_2.13-0.2.10.jar - but not for scala-swing_2.13-*.jar,
   * scalatest_2.13-*.jar and other jars that merely start with the same letters.
   */
  def isToolchainJarName(name: String): Boolean =
    name.endsWith(".jar") && toolchainJarPrefixes.exists { p =>
      name == s"$p.jar" || name.startsWith(s"$p-") || name.startsWith(s"${p}_")
    }

  /** Splits classpath entries into (toolchain jars, everything else). */
  def partitionStrays(entries: List[String]): (List[String], List[String]) =
    entries.partition(e => isToolchainJarName(new File(e).getName))


  private def versionAt(url: String): Option[String] =
    try {
      val is = new java.net.URL(url).openStream()
      try {
        val props = new java.util.Properties()
        props.load(is)
        Option(props.getProperty("version.number")).filter(_.nonEmpty)
      }
      finally is.close()
    }
    catch { case _: Throwable => None }

  private def part(name: String, propsFile: String, preferInUrl: String): Option[(String, String, String)] = {
    val urls = collection.mutable.ListBuffer.empty[String]
    val e = getClass.getClassLoader.getResources(propsFile)
    while (e.hasMoreElements) urls += e.nextElement.toString
    // upstream's scalariform.jar bundles its own (2.13.0) library.properties, so the
    // first hit for a properties file is not always the artifact we are asking about
    val url = urls.find(_.contains(preferInUrl)).orElse(urls.headOption)
    url.flatMap(u => versionAt(u).map(v => (name, v, u)))
  }

  /**
   * Detects a mixed Scala toolchain - a scala-library, scala-reflect and
   * scala-compiler of different versions resolving from different jars. A
   * version match is necessary but not sufficient: two builds can report the
   * same version yet differ in generated code, and that mix stays invisible
   * here - the name-based guards above are the defense for that case.
   * Reports the jar each part came from, without loading the compiler's
   * heavyweight classes just to ask.
   */
  def versionMismatch: Option[String] =
    try {
      val parts = Seq(
        part("scala-library", "library.properties", "scala-library"),
        part("scala-reflect", "reflect.properties", "scala-reflect"),
        part("scala-compiler", "compiler.properties", "scala-compiler")
      ).flatten

      if (parts.map(_._2).distinct.size <= 1) None
      else
        Some(
          "Mixed Scala toolchain on the classpath - Kojo will likely fail to compile scripts:\n" +
            parts.map { case (name, version, source) => s"  $name $version from $source" }.mkString("\n") +
            "\nRemove the stray jar(s) - check ~/.kojo/lite/libk, ~/.kojo/extension, the KOJO_CLASSPATH " +
            "environment variable, and old scala jars in the install's lib directory."
        )
    }
    catch {
      // a broken classpath is exactly what this check looks for; it must not
      // fail startup itself while trying to describe one
      case _: Throwable => None
    }

}
