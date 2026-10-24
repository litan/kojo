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
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.security.MessageDigest
import java.util.zip.ZipFile

import scala.io.Source
import scala.util.control.NonFatal

/**
 * Fetches a keyword-patched Scala toolchain on demand, so that a Kojo package
 * does not have to carry ~20 MB of patched compiler that only the speakers of
 * one language ever load. Language-agnostic: `lang` is the toolchain code
 * (`tr`, `sv`, ...); the assets live in a `v<version>-<lang>` release.
 *
 * The caller supplies an installation-owned destination. Nothing is fetched
 * unless an alternative is selected. Existing installations are verified offline
 * without writes, so administrators can provision protected installations too.
 *
 * Every downloaded jar must clear three checks before it is used:
 *   - its SHA-256 matches the published SHA256SUMS
 *   - it opens as a zip (a truncated download fails here, not at runtime)
 *   - library, reflect and compiler report the launcher's Scala release,
 *     so a toolchain from a different Scala release can never be paired
 *     with Kojo's classes
 *
 * -Dkojo.toolchain.url=<base> overrides the download location, which is how
 * the tests drive the whole path from a local directory, offline.
 */
object ScalaToolchainFetcher {
  val jarNames = Seq("scala-library.jar", "scala-reflect.jar", "scala-compiler.jar", "scalariform.jar")
  val checksumFileName = "SHA256SUMS"

  private val defaultBaseUrl = "https://github.com/bulent2k2/scala-2/releases/download"

  // A dead or stalled connection must not hang the launcher: connect fast,
  // and give a stalled read a minute before giving up and falling back.
  private val ConnectTimeoutMs = 15 * 1000
  private val ReadTimeoutMs = 60 * 1000

  def baseUrlFor(version: String, lang: String): String =
    System.getProperty("kojo.toolchain.url", s"$defaultBaseUrl/v$version-$lang").stripSuffix("/")

  def isComplete(dir: File): Boolean =
    dir.isDirectory && jarNames.forall(n => new File(dir, n).isFile) && new File(dir, checksumFileName).isFile

  /** Validate the entire set, including locally retained checksums, before use. */
  private def validate(dir: File, version: String): Unit = {
    require(isComplete(dir), s"incomplete toolchain in $dir (four jars and $checksumFileName required)")
    val expected = checksums(new File(dir, checksumFileName).toURI.toString)
    jarNames.foreach { name =>
      val jar = new File(dir, name)
      val want = expected.getOrElse(name, throw new IllegalStateException(s"$checksumFileName has no entry for $name"))
      val digest = MessageDigest.getInstance("SHA-256")
      val in = Files.newInputStream(jar.toPath)
      try {
        val buf = new Array[Byte](64 * 1024)
        var n = in.read(buf)
        while (n != -1) {
          digest.update(buf, 0, n)
          n = in.read(buf)
        }
      }
      finally in.close()
      val got = digest.digest().map(b => f"$b%02x").mkString
      require(got.equalsIgnoreCase(want), s"checksum mismatch for $jar (got $got, expected $want)")
      verifyZip(jar, name)
      name match {
        case "scala-library.jar"  => verifyScalaVersion(jar, "library.properties", version)
        case "scala-reflect.jar"  => verifyScalaVersion(jar, "reflect.properties", version)
        case "scala-compiler.jar" => verifyScalaVersion(jar, "compiler.properties", version)
        case _ =>
      }
    }
  }

  /** A damaged existing installation is reported, never silently trusted or overwritten. */
  def ensureAvailable(dir: File, version: String, lang: String,
      progress: FetchProgress = ConsoleProgress): Option[File] = {
    try {
      if (dir.exists()) {
        validate(dir, version)
        Some(dir)
      }
      else fetchInto(dir, version, lang, progress)
    }
    catch {
      case NonFatal(e) =>
        progress.message(s"[WARNING] Could not prepare the '$lang' Scala toolchain $version in $dir: ${e.getMessage}")
        None
    }
    finally progress.finished()
  }

  private def discardStaging(dir: File): Unit = {
    Option(dir.listFiles()).foreach(_.foreach(_.delete()))
    dir.delete()
  }

  private def fetchInto(dir: File, version: String, lang: String, progress: FetchProgress): Option[File] = {
    val base = baseUrlFor(version, lang)
    progress.message(s"[INFO] Fetching the '$lang' Scala toolchain $version from $base")
    val parent = dir.getAbsoluteFile.getParentFile.toPath
    Files.createDirectories(parent)
    // Each invocation owns a separate sibling directory; failed/concurrent downloads
    // cannot expose partial sets or delete another launcher's work.
    val staged = Files.createTempDirectory(parent, s".${dir.getName}-").toFile
    try {
      val expected = checksums(s"$base/$checksumFileName")
      jarNames.foreach { name =>
        val want = expected.getOrElse(name, throw new IllegalStateException(s"$checksumFileName has no entry for $name"))
        val actual = download(new URL(s"$base/$name"), new File(staged, name), name, progress)
        require(actual.equalsIgnoreCase(want), s"checksum mismatch for $name (got $actual, expected $want)")
      }
      val sums = jarNames.map(n => s"${expected(n)}  $n").mkString("", "\n", "\n")
      Files.write(new File(staged, checksumFileName).toPath, sums.getBytes("UTF-8"))
      validate(staged, version)
      // Publish the whole validated directory in one same-filesystem move. Never
      // replace an existing directory; another launcher may have just installed it.
      try Files.move(staged.toPath, dir.toPath)
      catch {
        // Linux can report DirectoryNotEmptyException when the other rename
        // wins between the provider's existence check and its rename syscall.
        case _: java.nio.file.FileSystemException if dir.isDirectory => validate(dir, version)
      }
      progress.message(s"[INFO] '$lang' Scala toolchain ready in $dir")
      Some(dir)
    }
    finally discardStaging(staged)
  }

  private def checksums(url: String): Map[String, String] = {
    val conn = new URL(url).openConnection()
    conn.setConnectTimeout(ConnectTimeoutMs)
    conn.setReadTimeout(ReadTimeoutMs)
    val src = Source.fromInputStream(conn.getInputStream, "UTF-8")
    try
      src
        .getLines()
        .map(_.trim)
        .filter(_.nonEmpty)
        .flatMap { line =>
          // "<sha256>  <filename>", as produced by sha256sum
          line.split("\\s+").toList match {
            case sum :: name :: Nil => Some(new File(name).getName -> sum)
            case _                  => None
          }
        }
        .toMap
    finally src.close()
  }

  /** Downloads `url` into `target`, returning the SHA-256 of what arrived. */
  private def download(url: URL, target: File, name: String, progress: FetchProgress): String = {
    val digest = MessageDigest.getInstance("SHA-256")
    val conn = url.openConnection()
    conn.setConnectTimeout(ConnectTimeoutMs)
    conn.setReadTimeout(ReadTimeoutMs)
    progress.startJar(name, conn.getContentLengthLong)
    val in: InputStream = conn.getInputStream
    try {
      val out = new FileOutputStream(target)
      try {
        val buf = new Array[Byte](64 * 1024)
        var n = in.read(buf)
        while (n > 0) {
          if (progress.cancelled) throw new RuntimeException("download cancelled")
          digest.update(buf, 0, n)
          out.write(buf, 0, n)
          progress.bytes(n.toLong)
          n = in.read(buf)
        }
      }
      finally out.close()
    }
    finally in.close()
    digest.digest().map(b => f"$b%02x").mkString
  }

  private def verifyZip(jar: File, name: String): Unit = {
    val zf = new ZipFile(jar)
    try if (zf.size == 0) throw new RuntimeException(s"$name is empty")
    finally zf.close()
  }

  private def verifyScalaVersion(jar: File, propertiesFile: String, wanted: String): Unit = {
    val zf = new ZipFile(jar)
    try {
      val entry = Option(zf.getEntry(propertiesFile))
        .getOrElse(throw new RuntimeException(s"${jar.getName} has no $propertiesFile"))
      val props = new java.util.Properties()
      val is = zf.getInputStream(entry)
      try props.load(is)
      finally is.close()
      val got = props.getProperty("version.number", "<unknown>")
      // the pack build stamps a suffix (2.13.18-20260823-...), so accept the
      // exact release or the release followed by a suffix - but not a longer
      // release that merely begins with the same digits
      if (!(got == wanted || got.startsWith(wanted + "-") || got.startsWith(wanted + "+"))) {
        throw new RuntimeException(s"${jar.getName} is Scala $got, but Kojo needs $wanted")
      }
    }
    finally zf.close()
  }
}
