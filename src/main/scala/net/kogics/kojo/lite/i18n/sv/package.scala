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
package net.kogics.kojo.lite.i18n

/**
 * Swedish Level-4 (keyword) localization — a STUB, mirroring `i18n.tr`.
 *
 * Only the piece the editor needs to recognize Swedish keywords is here: the
 * keyword list, registered by `SvInit.preInit()` for highlighting/completion. The
 * words are the exact strings encoded in `scala-sv/swedish-keywords.patch` and
 * are AI-generated drafts pending native review (see scala-sv/KEYWORDS.md).
 *
 * Not here yet (unlike `i18n.tr`): keyword completion templates, code
 * templates, and a Swedish Level-1/2 API. `keywordTemplateMap` and
 * `codeTemplateMap` are therefore empty; completion simply stays quiet for
 * Swedish until a native speaker fills them in.
 */
package object sv {
  // Scala keyword -> Swedish, kept in sync with swedish-keywords.patch.
  val keywordList: List[String] = List(
    "abstrakt", "fall", "fånga", "klass", "definiera", "utför", "annars",
    "ärver", "falskt", "slutlig", "slutligen", "för", "förNågra", "om",
    "implicit", "importera", "lat", "matcha", "ny", "ingen", "objekt",
    "åsidosätt", "paket", "privat", "skyddad", "returnera", "förseglad",
    "super", "denna", "kasta", "egenskap", "sant", "försök", "typ",
    "värde", "variabel", "medan", "med", "ge"
  )

  val keywordTemplateMap: Map[String, String] = Map.empty
  val codeTemplateMap: Map[String, String] = Map.empty
}
