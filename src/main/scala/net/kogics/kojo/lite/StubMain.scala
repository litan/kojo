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

import javax.swing.JOptionPane

import scala.collection.mutable.ArrayBuffer
import scala.sys.process.Process

import net.kogics.kojo.util.Utils

trait StubMain {
  import language.postfixOps
  def classpath: String
  def firstInstance: Boolean
  def firstMain(args: Array[String]): Unit
  def firstMainDone(): Unit
  def nthMain(args: Array[String]): Unit
  def log(msg: String) = println(msg)
  def done(): Unit = {
    log("[INFO] Kojo Launcher Done.")
    System.exit(0)
  }

  def main(args: Array[String]): Unit = {
    val javaVersion = System.getProperty("java.version")
    println(javaVersion)
    if (javaVersion != null) {
      if (!javaVersion.startsWith("1.8")) {
        JOptionPane.showMessageDialog(
          null,
          "Incompatible Java version - Kojo requires Java 8.\nVisit www.kogics.net/kojo-download for more information.",
          "Kojo startup error",
          JOptionPane.ERROR_MESSAGE
        )
        System.exit(1)
      }
    }

    Utils.safeProcess {
      if (firstInstance) {
        log(s"[INFO] Running first Kojo instance with args: ${args.mkString("[", ", ", "]")}")
        firstMain(args)
        try {
          realMain(args)
        }
        finally {
          firstMainDone()
        }
      }
      else {
        log(s"[INFO] Running > first Kojo instance with args: ${args.mkString("[", ", ", "]")}")
        nthMain(args)
      }
    }
    done()
  }

  def realMain(args: Array[String]): Unit = {
    val javaHome = System.getProperty("java.home")
    log("[INFO] Java Home: " + javaHome)
    val javaExec = {
      if (new File(javaHome + "/bin/javaw.exe").exists) {
        log("[INFO] Using javaw")
        javaHome + "/bin/javaw"
      }
      else {
        javaHome + "/bin/java"
      }
    }
    def extraCmds = {
      if (Utils.isLinux && System.getProperty("java.version").startsWith("1.8")) {
        "-Dsun.java2d.xrender=false "
      }
      else {
        ""
      }
    }
    def maxMem = {
      Utils.appProperty("memory.max") match {
        case Some(d) => d
        case None    => if (System.getProperty("sun.arch.data.model", "32") == "64") "2g" else "768m"
      }
    }
    def maybeMarlin =
      if (System.getProperty("java.vendor", "").toLowerCase.contains("jetbrains"))
        "-Dsun.java2d.renderer=sun.java2d.marlin.MarlinRenderingEngine " else ""

    def libPath = {
      val libraryPath = new StringBuilder(Utils.libDir)
      Utils.appProperty("library.path") match {
        case Some(path) => libraryPath.append(File.pathSeparator + path)
        case None       =>
      }
      Utils.appProperty("python.home") match {
        case Some(phome) =>
          libraryPath.append(File.pathSeparator + s"$phome/lib" +
            File.pathSeparatorChar + s"$phome/lib/python3.8/site-packages/jep")
        case None =>
      }
      libraryPath.toString
    }

    def extraEnv: Seq[(String, String)] = {
      if (Utils.isLinux) {
        val env = ArrayBuffer("LD_LIBRARY_PATH" -> libPath)
        Utils.appProperty("python.home") match {
          case Some(phome) => env.append("PYTHONHOME" -> phome)
          case None        =>
        }
        env.toSeq
      }
      else {
        Seq()
      }
    }

    val cmdPart = s"-client -Xms128m -Xmx${maxMem} " +
      "-Xss1m -Dapple.laf.useScreenMenuBar=true " +
      s"-Dawt.useSystemAAFontSettings=lcd ${maybeMarlin}" +
      "-Dapple.awt.graphics.UseQuartz=true -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled " +
      extraCmds +
      "net.kogics.kojo.lite.Main %s" format (args.mkString(" "))
    val commandSeq =
      Seq(
        javaExec,
        "-cp", classpath,
        s"-Djava.library.path=${libPath}"
      ) ++ cmdPart.split(' ')

    log(s"Java VM args: ${cmdPart}")
    Process(commandSeq, None, extraEnv: _*)!
  }

  def createCp(xs: List[String]): String = {
    val ourCp = new StringBuilder
    //    Bad stuff on the classpath can clobber the launch of the Real Kojo     
    //    val oldCp = System.getenv("CLASSPATH")
    //    if (oldCp != null) {
    //      ourCp.append(oldCp)
    //      ourCp.append(File.pathSeparatorChar)
    //    }

    // allow another way to customize classpath
    val kojoCp = System.getenv("KOJO_CLASSPATH")
    if (kojoCp != null) {
      ourCp.append(kojoCp)
      ourCp.append(File.pathSeparatorChar)
    }

    // add all jars in user's kojo lib dir to classpath
    Utils.libJars.foreach { x =>
      ourCp.append(Utils.libDir)
      ourCp.append(File.separatorChar)
      ourCp.append(x)
      ourCp.append(File.pathSeparatorChar)
    }

    ourCp.append(xs.mkString(File.pathSeparator))
    ourCp.toString
  }
}