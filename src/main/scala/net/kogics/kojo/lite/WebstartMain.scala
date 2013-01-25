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
import java.lang.reflect.InvocationTargetException
import java.net.JarURLConnection
import java.util.jar.JarFile

import javax.jnlp.ServiceManager
import javax.jnlp.SingleInstanceListener
import javax.jnlp.SingleInstanceService

import scala.collection.mutable.ListBuffer

import com.sun.jnlp.JNLPClassLoader

import net.kogics.kojo.util.Utils

object WebstartMain extends StubMain with RmiMultiInstance {
  val classpath: String = {
    try {
      val jnlpLoader = JNLPClassLoader.getInstance
      val jds = jnlpLoader.getLaunchDesc.getResources.getEagerOrAllJarDescs(true)
      val lb = new ListBuffer[String]
      jds.foreach { jd =>
        val jarFile = jnlpLoader.getJarFile(jd.getLocation)
        processJar(jarFile, lb)
      }
      createCp(lb.toList)
    }
    catch {
      case ex: ClassNotFoundException    => alternativeClasspath
      case ex: NoClassDefFoundError      => alternativeClasspath
      case ex: InvocationTargetException => alternativeClasspath
      case t: Throwable                  => throw t
    }
  }

  def processJar(jarFile: JarFile, lb: ListBuffer[String]) {
    val tempFile = File.createTempFile("kojolite-", ".jar");
    // need to use symlinks on jdk1.7 
    Utils.copyFile(new File(jarFile.getName()), tempFile);
    tempFile.deleteOnExit()
    lb += tempFile.getAbsolutePath()
  }

  def alternativeClasspath: String = {
    println("Not running on an Oracle JVM. I'm gonna try to do my best to make this work...")
    val jarUrls = Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF")
    val lb = new ListBuffer[String]
    while (jarUrls.hasMoreElements) {
      val jarFile = jarUrls.nextElement.openConnection.asInstanceOf[JarURLConnection].getJarFile
      processJar(jarFile, lb)
    }
    createCp(lb.toList)
  }

  override def firstMain(args: Array[String]) {
    super.firstMain(args)
    val sis = ServiceManager.lookup("javax.jnlp.SingleInstanceService").asInstanceOf[SingleInstanceService]
    val sisl = new SingleInstanceListener {
      def newActivation(params: Array[String]) {
        nthMain(params)
      }
    }
    sis.addSingleInstanceListener(sisl)
    Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
      def run() {
        sis.removeSingleInstanceListener(sisl)
      }
    }))
  }
}