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

final case class DownloadFailurePrompt(message: String, continueLabel: String = "Continue")

/** Launcher metadata only. Construction must not initialize editor/API/UI code or do I/O. */
final case class LanguageDescriptor(
    code: String,
    alternativeToolchain: Option[String] = None,
    downloadMessage: String = LanguageRegistry.defaultDownloadMessage,
    downloadFailurePrompt: DownloadFailurePrompt = LanguageRegistry.defaultDownloadFailurePrompt
)

object LanguageRegistry {
  val defaultDownloadMessage = "Downloading the Scala toolchain"
  val defaultDownloadFailurePrompt = DownloadFailurePrompt(
    "Localised keyword support couldn't be downloaded.\n" +
      "Kojo can continue using standard Scala keywords.")

  val registrations: List[LanguageDescriptor] = List(
    LanguageDescriptor("en"),
    LanguageDescriptor("hi", Some("hi"), "हिंदी Scala टूलचेन डाउनलोड हो रहा है", DownloadFailurePrompt(
      "हिंदी कुंजीशब्द समर्थन डाउनलोड नहीं हो सका।\n" +
        "कोजो Scala के मानक कुंजीशब्दों के साथ जारी रह सकता है।",
      "जारी रखें")),
    LanguageDescriptor("tr", Some("tr"), "Türkçe Scala derleyicisi indiriliyor", DownloadFailurePrompt(
      "Türkçe anahtar sözcük desteği indirilemedi.\n" +
        "Kojo, standart Scala anahtar sözcükleriyle devam edebilir.",
      "Devam et")),
    LanguageDescriptor("sv", Some("sv"), "Laddar ner Scala-verktygen för svenska", DownloadFailurePrompt(
      "Stödet för svenska nyckelord kunde inte laddas ner.\n" +
        "Kojo kan fortsätta med vanliga Scala-nyckelord.",
      "Fortsätt"))
  )

  def forLanguage(code: String): LanguageDescriptor =
    registrations.find(_.code == code).getOrElse(LanguageDescriptor(code))

  val alternativeToolchainCodes: Set[String] = registrations.flatMap(_.alternativeToolchain).toSet

  def downloadMessage(toolchain: String): String =
    registrations.find(_.alternativeToolchain.contains(toolchain))
      .map(_.downloadMessage).getOrElse(defaultDownloadMessage)

  def downloadFailurePrompt(toolchain: String): DownloadFailurePrompt =
    registrations.find(_.alternativeToolchain.contains(toolchain))
      .map(_.downloadFailurePrompt).getOrElse(defaultDownloadFailurePrompt)
}
