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
import java.util.prefs.Preferences

import scala.util.control.NonFatal

import net.kogics.kojo.util.Utils

/** Selects installation-owned alternatives for the desktop Kojo child JVM.
 * The desktop launcher/build retain stock Scala dependencies, including IDE/Maven jars.
 * Scratchpads inherit the running Kojo's selected dependencies without selecting again.
 */
object ScalaToolchain {
  val keywordLanguages: Set[String] = LanguageRegistry.alternativeToolchainCodes
  val prefsNodeName = "Kojolite-Prefs" // keep in sync with KojoCtx.prefs

  def userLanguage: String = {
    val default = System.getProperty("user.language", "en")
    try Preferences.userRoot().node(prefsNodeName).get("user.language", default)
    catch { case NonFatal(_) => default }
  }

  private def parseOverride(value: String, source: String): Option[String] = value match {
    case "en"                          => Some("en")
    case lang if keywordLanguages(lang) => Some(lang)
    case other =>
      val expected = ("en" +: keywordLanguages.toList.sorted).mkString(", ")
      println(s"[WARNING] Ignoring unknown kojo.toolchain '$other' (from $source); expected $expected.")
      None
  }

  def selectedCode: String =
    chooseCode(Option(System.getProperty("kojo.toolchain")), Utils.appProperty("kojo.toolchain"), userLanguage)

  private[lite] def chooseCode(system: Option[String], app: => Option[String], language: => String): String = {
    val fromSysProp = system.flatMap(parseOverride(_, "system property"))
    def fromAppProp = app.flatMap(parseOverride(_, "kojo.properties"))
    def fromLanguage = LanguageRegistry.forLanguage(language).alternativeToolchain.getOrElse("en")
    fromSysProp.orElse(fromAppProp).getOrElse(fromLanguage)
  }

  /** Read the release from the launcher compiler, never scalariform's bundled library.properties. */
  def scalaRelease: Option[String] =
    try """(\d+\.\d+\.\d+)""".r.findFirstIn(scala.tools.nsc.Properties.versionNumberString)
    catch {
      case _: LinkageError => None
      case NonFatal(_) => None
    }

  /** Follow Kojo's lib/rithica.jar convention, with no home/current-directory fallback. */
  def installationRoot(cp: List[String]): Option[File] = {
    val explicit = Option(System.getProperty("kojo.install.dir"))
    explicit match {
      case Some(path) =>
        require(path.trim.nonEmpty, "kojo.install.dir is empty")
        val root = new File(path).getCanonicalFile
        require(root.isDirectory, s"kojo.install.dir is not an installation directory: $root")
        Some(root)
      case None =>
        cp.iterator.map(p => new File(p).getAbsoluteFile)
          .filter(f => f.getName == "rithica.jar" && f.isFile)
          .flatMap(f => Option(f.getParentFile).filter(_.getName == "lib"))
          .flatMap(lib => Option(lib.getParentFile)).take(1).toList.headOption.map(_.getCanonicalFile)
    }
  }

  def toolchainDir(root: File, code: String, version: String): File =
    new File(root, s"toolchains/scala-$code/$version")

  def select(cp: List[String]): List[String] = select(cp, selectedCode)

  def select(cp: List[String], code: String): List[String] = {
    if (!keywordLanguages(code)) return cp
    var release = "<unknown>"
    var location = "<unresolved>"
    def fallback(reason: String): List[String] = {
      println(s"[WARNING] Scala toolchain '$code', Scala $release, location $location: $reason. " +
        "Localized keyword compilation is unavailable; retaining the original classpath. " +
        "Provision a valid toolchain in the installation or use a writable installation (-Dkojo.install.dir).")
      cp
    }
    try {
      release = scalaRelease.getOrElse(throw new IllegalStateException("cannot determine the launcher Scala release"))
      val root = installationRoot(cp).getOrElse(throw new IllegalStateException(
        "cannot discover installation from lib/rithica.jar; set -Dkojo.install.dir"))
      val dir = toolchainDir(root, code, release)
      location = dir.getPath
      ScalaToolchainFetcher.ensureAvailable(dir, release, code,
        FetchProgress.forLauncher(LanguageRegistry.downloadMessage(code))) match {
        case Some(available) =>
          val jars = ScalaToolchainFetcher.jarNames.map(n => new File(available, n).getPath).toList
          println(s"[INFO] Scala toolchain '$code', Scala $release, location $available " +
            s"(user language '$userLanguage', kojo.toolchain=${System.getProperty("kojo.toolchain", "<unset>")})")
          jars ::: cp.filterNot(e => ScalaToolchainCheck.isToolchainJarName(new File(e).getName))
        case None => fallback("toolchain download or validation failed (see preceding diagnostic)")
      }
    }
    catch { case NonFatal(e) => fallback(e.getMessage) }
  }
}
