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
import java.nio.file.Files
import java.nio.file.Paths

import scala.sys.process.ProcessLogger

import org.junit.Assert._
import org.junit.Test

import net.kogics.kojo.core.Interpreter.IR
import net.kogics.kojo.lite.NoOpRunContext
import net.kogics.kojo.lite.ToolchainTestSupport._

class CompilerAndRunnerTest extends CompilerAndRunnerTestBase {
  def makeRunner() = new CompilerAndRunner(
      {() => settings}, 
      Some("import TSCanvas._; import Tw._"), 
      listener,
      new NoOpRunContext
      )

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
        assertEquals(IR.Success, execRunner.compileForExecing(code))
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
