package net.kogics.kojo.lite

import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/** Offline fixtures; all files belong to a temporary root cleaned even on assertion failure. */
object ToolchainTestSupport {
  def withTempDir(body: File => Unit): Unit = {
    val root = Files.createTempDirectory("kojo-toolchain-test-").toRealPath().toFile
    try body(root)
    finally deleteRecursively(root)
  }

  def deleteRecursively(file: File): Unit = {
    if (file.isDirectory) Option(file.listFiles()).foreach(_.foreach(deleteRecursively))
    Files.deleteIfExists(file.toPath)
  }

  // System properties are JVM-global. Serialize changes across these test suites.
  def withProperty(key: String, value: String)(body: => Unit): Unit = synchronized {
    val old = System.getProperty(key)
    try {
      if (value == null) System.clearProperty(key) else System.setProperty(key, value)
      body
    }
    finally {
      if (old == null) System.clearProperty(key) else System.setProperty(key, old)
    }
  }

  def sha256(file: File): String =
    MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(file.toPath)).map(b => f"$b%02x").mkString

  def makeJar(dir: File, name: String, version: String): Unit = {
    val out = new ZipOutputStream(new FileOutputStream(new File(dir, name)))
    try {
      out.putNextEntry(new ZipEntry("marker.txt"))
      out.write(name.getBytes("UTF-8"))
      out.closeEntry()
      val props = Map("scala-library.jar" -> "library.properties", "scala-reflect.jar" -> "reflect.properties",
        "scala-compiler.jar" -> "compiler.properties")
      props.get(name).foreach { path =>
        out.putNextEntry(new ZipEntry(path))
        out.write(s"version.number=$version\n".getBytes("UTF-8"))
        out.closeEntry()
      }
    }
    finally out.close()
  }

  def writeChecksums(dir: File): Unit = {
    val sums = ScalaToolchainFetcher.jarNames.map(n => s"${sha256(new File(dir, n))}  $n").mkString("", "\n", "\n")
    Files.write(new File(dir, ScalaToolchainFetcher.checksumFileName).toPath, sums.getBytes("UTF-8"))
  }

  def provision(dir: File, version: String): File = {
    dir.mkdirs()
    ScalaToolchainFetcher.jarNames.foreach(n => makeJar(dir, n, version))
    writeChecksums(dir)
    dir
  }

  class Recorder extends FetchProgress {
    @volatile var connectingCalls = 0
    val messages = collection.mutable.ListBuffer.empty[String]
    val jars = collection.mutable.ListBuffer.empty[(String, Long)]
    var received = 0L
    var finishedCalls = 0
    var failureCalls = 0
    override def connecting(): Unit = { connectingCalls += 1 }
    def message(msg: String): Unit = { messages += msg }
    def startJar(name: String, totalBytes: Long): Unit = { jars += (name -> totalBytes) }
    def bytes(n: Long): Unit = { received += n }
    def finished(): Unit = { finishedCalls += 1 }
    override def downloadFailed(): Unit = {
      failureCalls += 1
    }
  }
}
