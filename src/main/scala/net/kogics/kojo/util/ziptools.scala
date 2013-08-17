/*
 * Copyright (C) 2013 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.util

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry

import scala.collection.convert.WrapAsScala.enumerationAsScalaIterator

import org.apache.commons.compress.archivers.zip.Zip64Mode
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.apache.commons.compress.archivers.zip.ZipFile

class ZipFileEx(file: File) extends ZipFile(file) {
  def getRawInputStream(ze: ZipArchiveEntry) = {
    val storedMethod = ze.getMethod
    ze.setMethod(ZipEntry.STORED)
    val ret = getInputStream(ze)
    ze.setMethod(storedMethod)
    ret
  }
}

class ZipArchiveOutputStreamEx(fileOut: FileOutputStream)
  extends ZipArchiveOutputStream(new BufferedOutputStream(fileOut)) {

  def this(file: File) = this(new FileOutputStream(file))
  def this(file: String) = this(new FileOutputStream(file))

  def writeRaw(b: Array[Byte], offset: Int, length: Int) {
    val ze = getCurrentEntry.entry
    val storedMethod = ze.getMethod
    ze.setMethod(ZipEntry.STORED)
    write(b, offset, length)
    ze.setMethod(storedMethod)
  }

  override def handleSizesAndCrc(bytesWritten: Long, crc: Long,
                                 effectiveMode: Zip64Mode): Boolean = {
    false
  }
}

object ZipUtils {

  def dumpJar(src: File) {
    println(s"Dumping: $src")
    val srcZip = new ZipFileEx(src)
    srcZip.getEntries.foreach { entry =>
      println(s"Entry: ${entry.getName}")
      println(s"Size: ${entry.getSize}")
      println(s"Compressed Size: ${entry.getCompressedSize}")
      val entryIn = srcZip.getInputStream(entry)
      val buffer = new Array[Byte](1024)
      var rcount = entryIn.read(buffer)
      while (rcount != -1) {
        print(new String(buffer, 0, rcount))
        rcount = entryIn.read(buffer)
      }
      entryIn.close()
      println("---")
    }
    srcZip.close()
  }

  def copyJar(src: File, dest: File, filter: Set[String]) {
    val srcZip = new ZipFileEx(src)
    val destZip = new ZipArchiveOutputStreamEx(dest)
    val entries = srcZip.getEntries
    while (entries.hasMoreElements) {
      val entry = entries.nextElement
      if (!filter.contains(entry.getName)) {
        destZip.putArchiveEntry(entry)
        if (entry.getSize > 0) {
          val entryIn = srcZip.getRawInputStream(entry)
          val buffer = new Array[Byte](1024)
          var rcount = entryIn.read(buffer)
          //            var read = 0
          while (rcount != -1) {
            //                read += rcount
            destZip.writeRaw(buffer, 0, rcount)
            rcount = entryIn.read(buffer)
          }
          entryIn.close()
        }
        destZip.closeArchiveEntry()
      }
    }
    srcZip.close()
    destZip.close()
  }
}