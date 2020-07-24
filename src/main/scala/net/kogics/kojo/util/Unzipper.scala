package net.kogics.kojo.util

import java.io.{BufferedOutputStream, File, FileInputStream, FileOutputStream, InputStream}
import java.util.zip.{ZipEntry, ZipInputStream}

object Unzipper {

  def unzip(zipFilePath: String, destDirectory: String): Unit = {
    unzip(new FileInputStream(zipFilePath), destDirectory)
  }

  def unzipResource(zipFileResource: String, destDirectory: String): Unit = {
    unzip(getClass.getResourceAsStream(zipFileResource), destDirectory)
  }

  def unzip(inputStream: InputStream, outDir: String): Unit = {
    val BufSize = 4096
    val destDir = new File(outDir)
    if (!destDir.exists) {
      destDir.mkdirs()
    }

    val zin = new ZipInputStream(inputStream)
    var entry = zin.getNextEntry
    while (entry != null) {
      val filePath = s"$outDir${File.separator}${entry.getName}"
      if (!entry.isDirectory) {
        //        println(s"Unzipping: $filePath")
        val bos = new BufferedOutputStream(new FileOutputStream(filePath))
        val bytesIn = new Array[Byte](BufSize)
        var read = zin.read(bytesIn)
        while (read != -1) {
          bos.write(bytesIn, 0, read)
          read = zin.read(bytesIn)
        }
        bos.close()
      }
      else {
        val dir = new File(filePath)
        dir.mkdir
      }
      zin.closeEntry()
      entry = zin.getNextEntry
    }
    zin.close()
  }
}
