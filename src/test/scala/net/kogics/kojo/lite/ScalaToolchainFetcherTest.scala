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
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.URL
import java.nio.file.Files

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer

import org.apache.hc.client5.http.DnsResolver
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ScalaToolchainFetcherTest extends FunSuite with Matchers {
  import ToolchainTestSupport._
  val version = "2.13.18"
  val lang = "sv" // proves the fetcher is language-neutral

  test("Hindi releases use scala-indic while other languages retain the Scala fork") {
    withProperty("kojo.toolchain.url", null) {
      ScalaToolchainFetcher.baseUrlFor(version, "hi") should be(
        s"https://github.com/litan/scala-indic/releases/download/v$version-hi")
      for (code <- List("tr", "sv", "xx")) {
        ScalaToolchainFetcher.baseUrlFor(version, code) should be(
          s"https://github.com/bulent2k2/scala-2/releases/download/v$version-$code")
      }
    }
  }

  test("explicit toolchain URL overrides language defaults and removes the trailing slash") {
    withProperty("kojo.toolchain.url", "https://example.com/toolchain/") {
      for (code <- List("hi", "tr", "sv")) {
        ScalaToolchainFetcher.baseUrlFor(version, code) should be("https://example.com/toolchain")
      }
    }
  }

  def withRelease(body: (File, File, Recorder) => Unit): Unit = withTempDir { root =>
    val remote = provision(new File(root, "remote"), s"$version-test-build")
    val dest = new File(root, "installation/toolchains/scala-sv/2.13.18")
    withProperty("kojo.toolchain.url", remote.toURI.toString) { body(remote, dest, new Recorder) }
  }

  def withHttpServer(handler: HttpExchange => Unit)(body: String => Unit): Unit = {
    val server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0)
    server.createContext("/", exchange => {
      try handler(exchange)
      finally exchange.close()
    })
    server.start()
    try body(s"http://127.0.0.1:${server.getAddress.getPort}")
    finally server.stop(0)
  }

  test("published toolchain is fetched as a complete set with retained checksums and progress") {
    withRelease { (remote, dir, recorder) =>
      ScalaToolchainFetcher.ensureAvailable(dir, version, lang, recorder) should be(Some(dir))
      recorder.connectingCalls should be(1)
      ScalaToolchainFetcher.isComplete(dir) should be(true)
      recorder.jars.map(_._1) should be(ScalaToolchainFetcher.jarNames)
      recorder.jars.foreach { case (_, total) => total should be > 0L }
      recorder.received should be(ScalaToolchainFetcher.jarNames.map(n => new File(dir, n).length).sum)
      recorder.finishedCalls should be(1)
      recorder.failureCalls should be(0)
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
      recorder.connectingCalls should be(0)
      recorder.finishedCalls should be(1)
    }
  }

  test("HTTP downloads follow redirects and accept checksums and jars without content negotiation failures") {
    withRelease { (remote, dir, recorder) =>
      val requested = new java.util.concurrent.ConcurrentLinkedQueue[String]()
      val progressBeforeRequest = new java.util.concurrent.atomic.AtomicBoolean(true)
      withHttpServer { exchange =>
        if (recorder.connectingCalls != 1) progressBeforeRequest.set(false)
        val path = exchange.getRequestURI.getPath
        if (exchange.getRequestHeaders.getFirst("Accept") != "*/*") {
          exchange.sendResponseHeaders(406, -1)
        }
        else if (!path.startsWith("/assets/")) {
          exchange.getResponseHeaders.set("Location", s"http://localhost:${exchange.getLocalAddress.getPort}/assets$path")
          exchange.sendResponseHeaders(302, -1)
        }
        else {
          val name = path.stripPrefix("/assets/")
          requested.add(name)
          val bytes = Files.readAllBytes(new File(remote, name).toPath)
          exchange.sendResponseHeaders(200, bytes.length.toLong)
          exchange.getResponseBody.write(bytes)
        }
      } { base =>
        withProperty("kojo.toolchain.url", base) {
          val result = ScalaToolchainFetcher.ensureAvailable(dir, version, lang, recorder)
          withClue(recorder.messages.mkString("; ")) { result should be(Some(dir)) }
        }
        requested.toArray.toSeq should be(ScalaToolchainFetcher.checksumFileName +: ScalaToolchainFetcher.jarNames)
        progressBeforeRequest.get should be(true)
        ScalaToolchainFetcher.isComplete(dir) should be(true)
        recorder.received should be(ScalaToolchainFetcher.jarNames.map(n => new File(dir, n).length).sum)
        recorder.jars.foreach { case (_, total) => total should be > 0L }
        recorder.finishedCalls should be(1)
      }
    }
  }

  test("HTTP connections try the next resolved IP when the first address cannot connect") {
    withHttpServer { exchange =>
      exchange.sendResponseHeaders(200, 2)
      exchange.getResponseBody.write("ok".getBytes("UTF-8"))
    } { base =>
      val resolver = new DnsResolver {
        def resolve(host: String): Array[InetAddress] = Array(
          InetAddress.getByAddress(Array[Byte](127, 0, 0, 2)),
          InetAddress.getByAddress(Array[Byte](127, 0, 0, 1)))
        def resolveCanonicalHostname(host: String): String = host
      }
      val client = ScalaToolchainFetcher.newHttpClient(resolver)
      try {
        // The server listens only on 127.0.0.1; 127.0.0.2 must fail first.
        val url = new URL(base.replace("127.0.0.1", "toolchain.test"))
        val text = ScalaToolchainFetcher.withInputStream(url, client) { (in, _) =>
          scala.io.Source.fromInputStream(in, "UTF-8").mkString
        }
        text should be("ok")
      }
      finally client.close()
    }
  }

  for (status <- List(404, 406, 500)) {
    test(s"HTTP $status reports the status and cleans staging without publishing a toolchain") {
      withRelease { (_, dir, recorder) =>
        withHttpServer(_.sendResponseHeaders(status, -1)) { base =>
          withProperty("kojo.toolchain.url", base) {
            ScalaToolchainFetcher.ensureAvailable(dir, version, lang, recorder) should be(None)
          }
          dir.exists should be(false)
          dir.getParentFile.listFiles().toList should be(Nil)
          recorder.messages.exists(_.contains(s"HTTP $status")) should be(true)
          recorder.finishedCalls should be(1)
          recorder.failureCalls should be(1)
        }
      }
    }
  }

  test("download failure is acknowledged after staging cleanup and before finishing") {
    withRelease { (remote, dir, _) =>
      Files.write(new File(remote, "scala-reflect.jar").toPath, "damaged".getBytes("UTF-8"))
      val recorder = new Recorder {
        override def downloadFailed(): Unit = {
          super.downloadFailed()
          failureCalls should be(1)
          finishedCalls should be(0)
          dir.exists should be(false)
          dir.getParentFile.listFiles().toList should be(Nil)
        }
      }
      ScalaToolchainFetcher.ensureAvailable(dir, version, lang, recorder) should be(None)
      recorder.connectingCalls should be(1)
      recorder.failureCalls should be(1)
      recorder.finishedCalls should be(1)
      dir.exists should be(false)
      dir.getParentFile.listFiles().toList should be(Nil)
    }
  }

  for (phase <- List("checksum headers", "checksum body", "jar headers", "jar body")) {
    test(s"HTTP cancellation aborts while waiting for $phase") {
      withRelease { (remote, dir, recorder) =>
        val waiting = new java.util.concurrent.CountDownLatch(1)
        val release = new java.util.concurrent.CountDownLatch(1)
        val pool = java.util.concurrent.Executors.newSingleThreadExecutor()
        try {
          withHttpServer { exchange =>
            val name = exchange.getRequestURI.getPath.stripPrefix("/")
            val isChecksum = name == ScalaToolchainFetcher.checksumFileName
            if (isChecksum == phase.startsWith("checksum")) {
              if (phase.endsWith("body")) {
                exchange.sendResponseHeaders(200, 1024 * 1024)
                exchange.getResponseBody.write(1)
                exchange.getResponseBody.flush()
              }
              waiting.countDown()
              release.await(10, java.util.concurrent.TimeUnit.SECONDS)
            }
            else {
              val bytes = Files.readAllBytes(new File(remote, name).toPath)
              exchange.sendResponseHeaders(200, bytes.length.toLong)
              exchange.getResponseBody.write(bytes)
            }
          } { base =>
            withProperty("kojo.toolchain.url", base) {
              try {
                val result = pool.submit(new java.util.concurrent.Callable[Option[File]] {
                  def call(): Option[File] = ScalaToolchainFetcher.ensureAvailable(dir, version, lang, recorder)
                })
                waiting.await(5, java.util.concurrent.TimeUnit.SECONDS) should be(true)
                recorder.connectingCalls should be(1)
                recorder.cancel()
                result.get(5, java.util.concurrent.TimeUnit.SECONDS) should be(None)
                withClue(recorder.messages.mkString("; ")) {
                  recorder.messages.exists(_.contains("download cancelled")) should be(true)
                }
                recorder.finishedCalls should be(1)
                recorder.failureCalls should be(0)
                dir.exists should be(false)
                dir.getParentFile.listFiles().toList should be(Nil)
              }
              finally release.countDown()
            }
          }
        }
        finally {
          release.countDown()
          pool.shutdownNow()
        }
      }
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
      cancelled.failureCalls should be(0)
    }
  }
}
