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

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ScalaToolchainCheckTest extends FunSuite with Matchers {
  import ScalaToolchainCheck._

  test("toolchain jar names are recognized, versioned or not") {
    isToolchainJarName("scala-library.jar") should be(true)
    isToolchainJarName("scala-library-2.13.3.jar") should be(true)
    isToolchainJarName("scala-reflect-2.13.18.jar") should be(true)
    isToolchainJarName("scala-compiler.jar") should be(true)
    isToolchainJarName("scalariform.jar") should be(true)
    isToolchainJarName("scalariform_2.13-0.2.10.jar") should be(true)
    isToolchainJarName("scala-library_2.13-x.jar") should be(true)
  }

  test("jars that merely start with the same letters are left alone") {
    isToolchainJarName("scala-swing_2.13-2.1.1.jar") should be(false)
    isToolchainJarName("scala-xml_2.13-1.2.0.jar") should be(false)
    isToolchainJarName("scala-parser-combinators_2.13-1.1.2.jar") should be(false)
    isToolchainJarName("scala-parallel-collections_2.13-1.2.0.jar") should be(false)
    isToolchainJarName("scalatest_2.13-3.0.8.jar") should be(false)
    isToolchainJarName("scalactic_2.13-3.0.8.jar") should be(false)
    isToolchainJarName("kojo.jar") should be(false)
    isToolchainJarName("scala-library") should be(false) // not a jar
  }

  test("stray toolchain jars are separated from the rest of a user jar directory") {
    val entries = List("myextension.jar", "scala-library-2.13.3.jar", "scalatest_2.13-3.0.8.jar", "scalariform.jar")
    val (strays, rest) = partitionStrays(entries)
    strays should be(List("scala-library-2.13.3.jar", "scalariform.jar"))
    rest should be(List("myextension.jar", "scalatest_2.13-3.0.8.jar"))
  }

  test("entries are matched on the file name, not the path") {
    val nested = List(
      s"${File.separator}home${File.separator}u${File.separator}.kojo${File.separator}lite${File.separator}libk${File.separator}scala-compiler-2.13.3.jar",
      s"${File.separator}opt${File.separator}scala-library${File.separator}mylib.jar"
    )
    val (strays, rest) = partitionStrays(nested)
    strays.size should be(1)
    strays.head should endWith("scala-compiler-2.13.3.jar")
    rest.size should be(1) // a directory named scala-library must not trip the check
  }

  test("versionMismatch is quiet on a consistent toolchain") {
    // the test JVM runs on one toolchain, so this must not fire
    versionMismatch should be(None)
  }
}
