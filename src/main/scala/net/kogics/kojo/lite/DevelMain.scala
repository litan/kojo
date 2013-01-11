/*
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
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
import java.net.URLDecoder

import scala.collection.mutable.ListBuffer

import net.kogics.kojo.util.Utils

object DevelMain extends StubMain with RmiMultiInstance {
 val classpath = {
    val urls = Thread.currentThread.getContextClassLoader match {
      case cl: java.net.URLClassLoader => cl.getURLs.toList
      case _                           => sys.error("classloader is not a URLClassLoader")
    }
    val classp = urls map { URLDecoder decode _.getPath }
    // simulate stuff that happens with webstart
    val lb = new ListBuffer[String]
    classp.foreach { fp =>
      val cpFile = new File(fp)
      if (cpFile.isFile()) {
        val tempFile = File.createTempFile("kojolite-", ".jar");
        Utils.copyFile(cpFile, tempFile);
        tempFile.deleteOnExit()
        lb += tempFile.getAbsolutePath()
      }
      else {
        lb += fp
      }
    }
    createCp(lb.toList)
  }
}