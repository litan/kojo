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
import java.net.JarURLConnection

import javax.jnlp.ServiceManager
import javax.jnlp.SingleInstanceListener
import javax.jnlp.SingleInstanceService

import scala.collection.mutable.ListBuffer

import net.kogics.kojo.util.Utils

object WebstartMain extends StubMain with RmiMultiInstance {
  val classpath: String = alternativeClasspath

  def processJar(jarName: String, lb: ListBuffer[String]) {
    val tempFile = File.createTempFile("kojolite-", ".jar");
    // need to use symlinks on jdk1.7 
    Utils.copyFile(new File(jarName), tempFile);
    //    ZipUtils.copyJar(new File(jarFile.getName()), tempFile, Set("META-INF/MANIFEST.MF", "META-INF/LALIT.SF", "META-INF/LALIT.RSA"))
    tempFile.deleteOnExit()
    lb += tempFile.getAbsolutePath()
  }

  def alternativeClasspath: String = {
    val ignore = Set("javaws.jar", "deploy.jar", "plugin.jar", "netx.jar")
    println("Processing Kojo jars...")
    val t0 = System.currentTimeMillis
    val jarUrls = Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF")
    val lb = new ListBuffer[String]
    while (jarUrls.hasMoreElements) {
      val jarFile = jarUrls.nextElement.openConnection.asInstanceOf[JarURLConnection].getJarFile
      val jarName = jarFile.getName
      val justJarName = jarName.substring(jarName.lastIndexOf(File.separatorChar) + 1, jarName.length)
      if (!ignore.contains(justJarName)) {
        processJar(jarName, lb)
      }
    }
    val t1 = System.currentTimeMillis
    println(s"Time taken to process jars: ${(t1 - t0) / 1000.0} seconds")
    createCp(lb.toList)
  }

  @volatile var sis: SingleInstanceService = _
  @volatile var sisl: SingleInstanceListener = _
  override def firstMain(args: Array[String]) {
    super.firstMain(args)
    sis = ServiceManager.lookup("javax.jnlp.SingleInstanceService").asInstanceOf[SingleInstanceService]
    sisl = new SingleInstanceListener {
      def newActivation(params: Array[String]) {
        println(s"[INFO] Connected (via WS) with args: ${params.mkString("[", ", ", "]")} to already running Kojo Launcher")
        nthMain(params)
      }
    }
    sis.addSingleInstanceListener(sisl)
  }

  override def firstMainDone() {
    super.firstMainDone()
    println("[INFO] Removing WS Listener")
    sis.removeSingleInstanceListener(sisl)
  }
}