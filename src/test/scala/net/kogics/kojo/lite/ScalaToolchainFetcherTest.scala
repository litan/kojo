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
class ScalaToolchainFetcherTest extends FunSuite with Matchers {
  import ToolchainTestSupport._
  val version = "2.13.18"
  val lang = "sv" // proves the fetcher is language-neutral

  def withRelease(body: (File, File, Recorder) => Unit): Unit = withTempDir { root =>
    val remote = provision(new File(root, "remote"), s"$version-test-build")
    val dest = new File(root, "installation/toolchains/scala-sv/2.13.18")
    withProperty("kojo.toolchain.url", remote.toURI.toString) { body(remote, dest, new Recorder) }
  }

  test("published toolchain is fetched as a complete set with retained checksums and progress") {
    withRelease { (remote, dir, recorder) =>
      ScalaToolchainFetcher.ensureAvailable(dir, version, lang, recorder) should be(Some(dir))
      ScalaToolchainFetcher.isComplete(dir) should be(true)
      recorder.jars.map(_._1) should be(ScalaToolchainFetcher.jarNames)
      recorder.jars.foreach { case (_, total) => total should be > 0L }
      recorder.received should be(ScalaToolchainFetcher.jarNames.map(n => new File(dir, n).length).sum)
      recorder.finishedCalls should be(1)
      dir.getParentFile.listFiles().map(_.getName).toList should be(List(version))
      Files.readAllBytes(new File(dir, "SHA256SUMS").toPath).toSeq should be(
        Files.readAllBytes(new File(remote, "SHA256SUMS").toPath).toSeq)
    }
  }

  test("verified provisioned toolchain is reused offline without downloads or writes") {
    withRelease { (_, dir, recorder) =>
      provision(dir, version)
      val timestamps = dir.listFiles().map(f => f.getName -> f.lastModified()).toMap
      withProperty("kojo.toolchain.url", "file:///unreachable/") {
        ScalaToolchainFetcher.ensureAvailable(dir, version, lang, recorder) should be(Some(dir))
      }
      dir.listFiles().map(f => f.getName -> f.lastModified()).toMap should be(timestamps)
      recorder.jars should be(empty)
      recorder.finishedCalls should be(1)
    }
  }

  for (damage <- List("checksum", "zip", "library-version", "reflect-version", "compiler-version", "missing", "missing-checksum")) {
    test(s"download rejects $damage and exposes no partial destination") {
      withRelease { (remote, dir, recorder) =>
        damage match {
          case "checksum" => Files.write(new File(remote, "scala-reflect.jar").toPath, "tampered".getBytes("UTF-8"))
          case "zip" =>
            Files.write(new File(remote, "scalariform.jar").toPath, "not a zip".getBytes("UTF-8"))
            writeChecksums(remote)
          case "missing" => new File(remote, "scala-compiler.jar").delete()
          case "missing-checksum" => Files.write(new File(remote, "SHA256SUMS").toPath, Array.emptyByteArray)
          case other =>
            val jar = Map("library-version" -> "scala-library.jar", "reflect-version" -> "scala-reflect.jar",
              "compiler-version" -> "scala-compiler.jar")(other)
            makeJar(remote, jar, "2.13.15")
            writeChecksums(remote)
        }
        ScalaToolchainFetcher.ensureAvailable(dir, version, lang, recorder) should be(None)
        dir.exists should be(false)
        dir.getParentFile.listFiles().toList should be(Nil)
        recorder.messages.exists(_.contains("[WARNING]")) should be(true)
        recorder.finishedCalls should be(1)
      }
    }
  }

  for (damage <- List("checksum", "missing", "version", "checksums")) {
    test(s"reused toolchain is validated offline ($damage)") {
      withRelease { (_, dir, recorder) =>
        provision(dir, if (damage == "version") "2.12.20" else version)
        damage match {
          case "checksum" => Files.write(new File(dir, "scala-library.jar").toPath, Array[Byte](1))
          case "missing" => new File(dir, "scalariform.jar").delete()
          case "checksums" => new File(dir, "SHA256SUMS").delete()
          case _ =>
        }
        ScalaToolchainFetcher.ensureAvailable(dir, version, lang, recorder) should be(None)
        recorder.jars should be(empty)
        recorder.messages.exists(_.contains("[WARNING]")) should be(true)
      }
    }
  }

  test("offline fetch reports the reason and cleans its staging directory") {
    withRelease { (_, dir, recorder) =>
      withProperty("kojo.toolchain.url", "file:///definitely/not/here/") {
        ScalaToolchainFetcher.ensureAvailable(dir, version, lang, recorder) should be(None)
      }
      dir.exists should be(false)
      dir.getParentFile.listFiles().toList should be(Nil)
      recorder.finishedCalls should be(1)
      recorder.messages.exists(_.contains("[WARNING]")) should be(true)
    }
  }

  test("an unwritable destination reports failure without publishing jars") {
    withRelease { (_, dir, recorder) =>
      // A file in place of a parent is a deterministic write failure, even as root.
      val blocked = new File(dir.getParentFile, "blocked")
      blocked.getParentFile.mkdirs()
      blocked.createNewFile()
      val target = new File(blocked, version)
      ScalaToolchainFetcher.ensureAvailable(target, version, lang, recorder) should be(None)
      target.exists should be(false)
      recorder.messages.exists(_.contains("[WARNING]")) should be(true)
      recorder.finishedCalls should be(1)
    }
  }

  test("concurrent downloads publish one complete set and clean only their own staging") {
    withRelease { (_, dir, _) =>
      val ready = new java.util.concurrent.CountDownLatch(2)
      val pool = java.util.concurrent.Executors.newFixedThreadPool(2)
      try {
        val tasks = (1 to 2).map { _ =>
          pool.submit(new java.util.concurrent.Callable[Option[File]] {
            def call(): Option[File] = {
              val progress = new Recorder {
                override def startJar(name: String, total: Long): Unit = {
                  if (name == ScalaToolchainFetcher.jarNames.head) {
                    ready.countDown()
                    require(ready.await(10, java.util.concurrent.TimeUnit.SECONDS), "second download did not start")
                  }
                  super.startJar(name, total)
                }
              }
              val result = ScalaToolchainFetcher.ensureAvailable(dir, version, lang, progress)
              require(result.isDefined, progress.messages.mkString("; "))
              result
            }
          })
        }
        tasks.foreach(_.get(20, java.util.concurrent.TimeUnit.SECONDS) should be(Some(dir)))
        ScalaToolchainFetcher.isComplete(dir) should be(true)
        dir.getParentFile.listFiles().toList should be(List(dir))
      }
      finally pool.shutdownNow()
    }
  }

  test("cancellation publishes no partial set and leaves other invocations alone") {
    withRelease { (_, dir, _) =>
      dir.getParentFile.mkdirs()
      val otherStaging = new File(dir.getParentFile, ".another-launcher.part")
      otherStaging.createNewFile()
      val cancelled = new Recorder { override def cancelled: Boolean = true }
      ScalaToolchainFetcher.ensureAvailable(dir, version, lang, cancelled) should be(None)
      dir.exists should be(false)
      dir.getParentFile.listFiles().toList should be(List(otherStaging))
      cancelled.finishedCalls should be(1)
    }
  }
}
