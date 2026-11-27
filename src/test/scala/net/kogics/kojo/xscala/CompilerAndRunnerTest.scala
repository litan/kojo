/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.kojo.xscala

import java.io.File
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files
import java.nio.file.Paths

import scala.sys.process.ProcessLogger

import org.junit.Assert._
import org.junit.Test

import net.kogics.kojo.core.Interpreter.IR
import net.kogics.kojo.lite.NoOpRunContext
import net.kogics.kojo.lite.ToolchainTestSupport._
import net.kogics.kojo.util.Utils

class CompilerAndRunnerTest extends CompilerAndRunnerTestBase {
  def makeRunner() = new CompilerAndRunner(
      {() => settings}, 
      Some("import TSCanvas._; import Tw._"), 
      listener,
      new NoOpRunContext
      )

  @Test
  def testNestedIncludesPreserveEditorErrorPositions(): Unit = withTempDir { root =>
    val first = root.toPath.resolve("include1.kojo")
    val second = root.toPath.resolve("include2.kojo")
    val third = root.toPath.resolve("include3.kojo")
    val template = root.toPath.resolve("template.kojo")
    val marker = "${usercode}"
    Files.write(first, s"// #include $second\nval x = inc2 + 2".getBytes(UTF_8))
    Files.write(second, s"// #include $third\nval inc2 = incl3 + 10".getBytes(UTF_8))
    Files.write(third, "val incl3 = 100".getBytes(UTF_8))
    Files.write(template, s"object Body {\n$marker\n}\n".getBytes(UTF_8))
    val plainCode = s"// #include $first\nval toplevel = x\n"
    for (code <- Seq(plainCode, s"// #include template $template\n$plainCode")) {
      assertEquals(IR.Success, runner.compileForRunning(code))
      val broken = code.replace("= x", "= missing")
      val offset = broken.indexOf("missing")
      assertEquals(IR.Error, runner.compileForRunning(broken))
      assertEquals(broken.take(offset).count(_ == '\n') + 1, errLine)
      assertEquals(offset - broken.lastIndexOf('\n', offset), errColumn)
      assertEquals(offset, errOffset)
    }
  }

  @Test
  def testSketchTemplateWithTemplatedInclude(): Unit = withTempDir { root =>
    val helper = root.toPath.resolve("size.kojo")
    val helperTemplate = root.toPath.resolve("size-template.kojo")
    val marker = "${usercode}"
    Files.write(helper, s"// #include template $helperTemplate\nval minDiameter = 5\n".getBytes(UTF_8))
    Files.write(helperTemplate, s"object Sizes {\n$marker\n}\n".getBytes(UTF_8))
    val code = s"""// #include template /sketch
                  |//  #include $helper
                  |
                  |def setup(surface: CanvasDraw): Unit = {
                  |  import surface._
                  |  stroke(gray)
                  |  strokeWeight(1)
                  |}
                  |
                  |def drawLoop(surface: CanvasDraw): Unit = {
                  |  import surface._
                  |  val xloc = random(cwidth)
                  |  val yloc = random(cheight)
                  |  fill(cm.rgba(0, 50, mathx.constrain(yloc, 100, 150).toInt, 50))
                  |  val dia = mathx.constrain(xloc / 10, Sizes.minDiameter, 100)
                  |  ellipse(xloc, yloc, dia, dia)
                  |}
                  |""".stripMargin
    val (expanded, linesBeforeEditor, charsBeforeEditor) = Utils.preProcessInclude(code)
    val sketchTemplate = Utils.absolutePath("/sketch.kojo")
    val helperPath = Utils.absolutePath(helper.toString)
    assertEquals("// #begin-usercode", expanded.linesIterator.drop(linesBeforeEditor - 1).next())
    assertEquals("// #Include template /sketch", expanded.linesIterator.drop(linesBeforeEditor).next())
    assertTrue(expanded.substring(charsBeforeEditor).startsWith(s"// #Include template /sketch\n//  #Include $helper\n"))
    assertTrue(expanded.substring(charsBeforeEditor + code.length).startsWith("// #end-usercode\n}"))
    assertTrue(expanded.startsWith(s"// #begin-template: $sketchTemplate\n// #begin-include: $helperPath\n"))
    assertTrue(expanded.contains(s"// #end-include: $helperPath\ncleari()"))
    assertTrue(expanded.endsWith(s"// #end-template: $sketchTemplate\n"))
    assertEquals(IR.Success, runner.compileForRunning(code))

    val broken = code.replace("ellipse(xloc", "missing(xloc")
    val offset = broken.indexOf("missing(xloc")
    assertEquals(IR.Error, runner.compileForRunning(broken))
    assertEquals(16, errLine)
    assertEquals(3, errColumn)
    assertEquals(offset, errOffset)
  }

  @Test
  def testIncludeTemplateRunsInProcess(): Unit = withTempDir { root =>
    val template = root.toPath.resolve("run.kojo")
    val output = root.toPath.resolve("result.txt")
    val marker = "${usercode}"
    Files.write(template, s"object Body {\r\n  $marker\r\n}\r\nBody.run()\r\n".getBytes(UTF_8))
    val inProcessRunner = new CompilerAndRunner(() => settings, None, listener, new NoOpRunContext {
      override val compilerPrefix = " {\n  class UserCode {\n"
    })
    val code = s"""// #include template $template
                  |def run(): Unit = {
                  |  java.nio.file.Files.write(java.nio.file.Paths.get(java.net.URI.create("${output.toUri}")),
                  |    "ran".getBytes(java.nio.charset.StandardCharsets.UTF_8))
                  |}
                  |""".stripMargin
    assertEquals(IR.Success, inProcessRunner.compileAndRun(code))
    assertEquals("ran", new String(Files.readAllBytes(output), UTF_8))
  }

  @Test
  def testExecWithRelativeClasspathAndDifferentWorkingDirectory(): Unit = {
    val root = Files.createTempDirectory(Paths.get("").toAbsolutePath, ".kojo-exec-test-").toFile
    try {
      val childDir = new File(root, "child working directory")
      assertTrue(childDir.mkdir())
      val library = new File(root, "scala library.jar")
      Files.copy(Paths.get(classOf[scala.Option[_]].getProtectionDomain.getCodeSource.getLocation.toURI),
        library.toPath)
      withProperty("java.io.tmpdir", root.getPath) {
        val execRunner = new CompilerAndRunner(() => settings, None, listener, new NoOpRunContext {
          override def baseDir: String = childDir.getPath
        })
        val code = """object Launcher {
          |  def main(args: Array[String]): Unit = {
          |    println(List("Hello").head)
          |    println(new java.io.File(".").getCanonicalPath)
          |    println(System.getProperty("java.class.path"))
          |  }
          |}
          |""".stripMargin
        val template = root.toPath.resolve("exec.kojo")
        val marker = "${usercode}"
        Files.write(template, s"// Exec template\n$marker\n".getBytes(UTF_8))
        assertEquals(IR.Success, execRunner.compileForExecing(s"// #exec template $template\n$code"))
        val stdout = new StringBuilder
        val stderr = new StringBuilder
        val logger = ProcessLogger(line => { stdout.append(line).append('\n'); () },
          line => { stderr.append(line).append('\n'); () })
        val parentDir = Paths.get("").toAbsolutePath
        val relativeLibrary = parentDir.relativize(library.toPath).toString
        // Empty entries mean the parent's working directory, even when the child changes directory.
        withProperty("java.class.path", File.pathSeparator + relativeLibrary + File.pathSeparator) {
          val child = execRunner.execCompiled(logger)
          try assertEquals(stderr.toString, 0, child.exitValue())
          finally if (child.isAlive()) child.destroy()
        }
        val output = stdout.toString.linesIterator.toList
        assertEquals("Hello", output.head)
        assertEquals(childDir.getCanonicalPath, output(1))
        val childClasspath = output(2).split(File.pathSeparator, -1).toList
        assertTrue(childClasspath.forall(entry => new File(entry).isAbsolute))
        assertTrue(childClasspath.contains(library.getAbsolutePath))
        assertTrue(childClasspath.contains(parentDir.toString))
      }
    }
    finally deleteRecursively(root)
  }
}
