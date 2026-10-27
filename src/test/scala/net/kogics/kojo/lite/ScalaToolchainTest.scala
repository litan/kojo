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
import java.nio.file.Files

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ScalaToolchainTest extends FunSuite with Matchers {
  import ToolchainTestSupport._

  val version = ScalaToolchain.scalaRelease.get

  def launcherCp(root: File, maven: Boolean): List[String] = {
    val lib = new File(root, "lib")
    lib.mkdirs()
    val rithica = new File(lib, "rithica.jar")
    rithica.createNewFile()
    val jars = if (maven) List(
      "/coursier/org/scala-lang/scala-library/2.13.18/scala-library-2.13.18.jar",
      "/coursier/org/scala-lang/scala-reflect/2.13.18/scala-reflect-2.13.18.jar",
      "/coursier/org/scala-lang/scala-compiler/2.13.18/scala-compiler-2.13.18.jar",
      "/coursier/org/scalariform/scalariform_2.13/0.2.10/scalariform_2.13-0.2.10.jar"
    ) else ScalaToolchainFetcher.jarNames.map(n => new File(lib, n).getPath).toList
    List("/kojo/classes", rithica.getPath) ::: jars ::: List("/lib/scala-swing.jar", "/extra/helper.jar")
  }

  for (code <- List("tr", "sv", "hi"); maven <- List(false, true)) {
    test(s"$code substitution preserves unrelated dependency order (Maven layout: $maven)") {
      withTempDir { root =>
        withProperty("kojo.install.dir", null) {
          val dir = provision(ScalaToolchain.toolchainDir(root, code, version), version)
          val cp = launcherCp(root, maven)
          val selected = ScalaToolchain.select(cp, code)
          selected.take(4) should be(ScalaToolchainFetcher.jarNames.map(n => new File(dir, n).getPath).toList)
          selected.drop(4) should be(List("/kojo/classes", new File(root, "lib/rithica.jar").getPath,
            "/lib/scala-swing.jar", "/extra/helper.jar"))
        }
      }
    }
  }

  test("stock and unknown selections do not resolve or download alternatives") {
    withTempDir { root =>
      withProperty("kojo.install.dir", new File(root, "missing").getPath) {
        val cp = List("/some/scala-library-2.13.18.jar", "/kojo/classes")
        List("en", "de", "xx").foreach(code => ScalaToolchain.select(cp, code) should be(cp))
        root.listFiles().toList should be(Nil)
      }
    }
  }

  test("installation override supports launches without rithica and takes precedence over discovery") {
    withTempDir { root =>
      val overrideRoot = new File(root, "override")
      overrideRoot.mkdirs()
      val dir = provision(ScalaToolchain.toolchainDir(overrideRoot, "sv", version), version)
      withProperty("kojo.install.dir", overrideRoot.getPath) {
        ScalaToolchain.installationRoot(launcherCp(root, true)) should be(Some(overrideRoot))
        ScalaToolchain.select(List("/kojo/classes"), "sv").head should be(new File(dir, "scala-library.jar").getPath)
      }
    }
  }

  test("discovery has no home or working-directory fallback") {
    withProperty("kojo.install.dir", null) {
      val cp = List("/some/place/scala-library.jar", "/some/place/kojo.jar")
      ScalaToolchain.installationRoot(cp) should be(None)
      ScalaToolchain.select(cp, "tr") should be(cp)
    }
  }

  test("invalid explicit installation does not use a discovered installation") {
    withTempDir { root =>
      withProperty("kojo.install.dir", new File(root, "missing").getPath) {
        val cp = launcherCp(root, true)
        ScalaToolchain.select(cp, "tr") should be(cp)
        new File(root, "toolchains").exists should be(false)
      }
    }
  }

  for (damage <- List("missing", "checksum", "wrongversion", "no-checksums")) {
    test(s"$damage toolchain retains the entire original classpath") {
      withTempDir { root =>
        withProperty("kojo.install.dir", root.getPath) {
          val dir = provision(ScalaToolchain.toolchainDir(root, "tr", version),
            if (damage == "wrongversion") "2.12.20" else version)
          damage match {
            case "missing" => new File(dir, "scala-compiler.jar").delete()
            case "checksum" => Files.write(new File(dir, "scala-reflect.jar").toPath, Array[Byte](1, 2, 3))
            case "no-checksums" => new File(dir, "SHA256SUMS").delete()
            case _ =>
          }
          val cp = launcherCp(root, true)
          ScalaToolchain.select(cp, "tr") should be(cp)
        }
      }
    }
  }

  test("separate installations, versions and languages have separate destinations") {
    withTempDir { root =>
      val one = new File(root, "one")
      val two = new File(root, "two")
      val paths = List(ScalaToolchain.toolchainDir(one, "tr", version),
        ScalaToolchain.toolchainDir(two, "tr", version), ScalaToolchain.toolchainDir(one, "sv", version),
        ScalaToolchain.toolchainDir(one, "tr", "2.13.99"))
      paths.distinct.size should be(4)
    }
  }

  test("system override, app property and UI language keep their precedence") {
    ScalaToolchain.chooseCode(Some("tr"), Some("sv"), "en") should be("tr")
    ScalaToolchain.chooseCode(Some("en"), Some("tr"), "tr") should be("en")
    ScalaToolchain.chooseCode(None, Some("sv"), "tr") should be("sv")
    ScalaToolchain.chooseCode(None, None, "tr") should be("tr")
    ScalaToolchain.chooseCode(None, None, "hi") should be("hi")
    ScalaToolchain.chooseCode(Some("hi"), Some("tr"), "en") should be("hi")
    ScalaToolchain.chooseCode(None, Some("hi"), "en") should be("hi")
    ScalaToolchain.chooseCode(Some("en"), None, "hi") should be("en")
    ScalaToolchain.chooseCode(Some("unknown"), Some("sv"), "tr") should be("sv")
    ScalaToolchain.chooseCode(None, None, "de") should be("en")
    withProperty("kojo.toolchain", "tr") { ScalaToolchain.selectedCode should be("tr") }
    withProperty("kojo.toolchain", "en") { ScalaToolchain.selectedCode should be("en") }
  }

  test("scratchpads retain the running toolchain even when selection would choose another language") {
    withTempDir { root =>
      val trDir = provision(ScalaToolchain.toolchainDir(root, "tr", version), version)
      val svDir = provision(ScalaToolchain.toolchainDir(root, "sv", version), version)
      withProperty("kojo.install.dir", root.getPath) {
        val runningCp = ScalaToolchain.select(launcherCp(root, true), "tr")
        withProperty("kojo.toolchain", "sv") {
          ScalaToolchain.selectedCode should be("sv")
          withProperty("java.class.path", runningCp.mkString(File.pathSeparator)) {
            val scratchpadCp = NewKojoInstance.classpath.split(File.pathSeparatorChar).toList
            scratchpadCp.takeRight(runningCp.size) should be(runningCp)
            scratchpadCp should contain(new File(trDir, "scala-compiler.jar").getPath)
            scratchpadCp should not contain(new File(svDir, "scala-compiler.jar").getPath)
          }
        }
      }
    }
  }

  test("metadata uses stock defaults without initializing language implementations") {
    LanguageRegistry.forLanguage("en").alternativeToolchain should be(None)
    LanguageRegistry.forLanguage("de").alternativeToolchain should be(None)
    LanguageRegistry.forLanguage("hi").alternativeToolchain should be(Some("hi"))
    LanguageRegistry.alternativeToolchainCodes should be(Set("tr", "sv", "hi"))
    LanguageRegistry.downloadMessage("hi") should include("हिंदी")
    LanguageRegistry.downloadFailurePrompt("hi").continueLabel should be("जारी रखें")
    LanguageRegistry.downloadMessage("tr") should include("Türkçe")
    LanguageRegistry.downloadMessage("sv") should include("svenska")
    LanguageRegistry.downloadMessage("xx") should be(LanguageRegistry.defaultDownloadMessage)
    LanguageRegistry.downloadFailurePrompt("xx") should be(LanguageRegistry.defaultDownloadFailurePrompt)
  }
}
