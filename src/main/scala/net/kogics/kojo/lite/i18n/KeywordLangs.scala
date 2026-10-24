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
 * The one place the editor asks "what are the localized Scala keywords for the
 * language the user is running in?" — for syntax highlighting
 * (`lexer.ScalariformTokenMaker`), code completion (`xscala.CodeCompletionUtils`)
 * and code templates (`xscala.CodeTemplates`).
 *
 * This is Level 4 of localization (see localization.md): a child writes
 * `dez x = 5` (Turkish) instead of `val x = 5`. Turkish is the reference
 * implementation. To add a language:
 *   1. build its keyword-patched Scala toolchain (see scala-tr/, scala-sv/),
 *   2. register its toolchain in `LanguageRegistry` (launcher side), and
 *   3. add one `Pack(...)` to `packs` below (editor side).
 * None of the three consumers above needs to change.
 *
 * A `Pack` carries the language's *raw* (locale-independent) data; this object
 * exposes only the pack matching the current user language, so the consumers
 * automatically go quiet for every other language.
 */
object KeywordLangs {
  final case class Pack(
      code: String,
      keywords: List[String],
      keywordTemplates: Map[String, String],
      codeTemplates: Map[String, String]
  )

  // The registry. Adding a language means adding one line here.
  private val packs: Map[String, Pack] = Seq(
    Pack("tr", tr.keywordList, tr.keywordTemplateMap, tr.codeTemplateMap),
    Pack("sv", sv.keywordList, sv.keywordTemplateMap, sv.codeTemplateMap)
  ).map(p => p.code -> p).toMap

  /**
   * The registered language codes. Must equal ScalaToolchain.keywordLanguages -
   * that set comes from lightweight metadata so the launcher need not load these
   * packages, and KeywordLangsTest asserts the two stay in sync.
   */
  val codes: Set[String] = packs.keySet

  /** Tests can force a language regardless of the JVM's `user.language`. */
  @volatile var forcedLang: Option[String] = None

  // Called per token on the highlighting path; the system-property read is a
  // synchronized lookup, accepted because token counts per repaint are small.
  private def activeCode: Option[String] =
    forcedLang.orElse(Option(System.getProperty("user.language"))).filter(packs.contains)

  private def active: Option[Pack] = activeCode.flatMap(packs.get)

  /** The current language's keyword list, or empty when the language has none. */
  def keywords: List[String] = active.map(_.keywords).getOrElse(Nil)

  // isKeyword is on the highlighter's hot path (called per token), so cache the
  // set and rebuild only when the active language actually changes. Volatile,
  // read without further synchronization: the race is benign - a stale read
  // costs at most one redundant rebuild of an immutable set.
  @volatile private var cache: Option[(Option[String], Set[String])] = None
  private def keywordSet: Set[String] = {
    val code = activeCode
    cache match {
      case Some((c, s)) if c == code => s
      case _ =>
        val s = code.flatMap(packs.get).map(_.keywords.toSet).getOrElse(Set.empty)
        cache = Some((code, s))
        s
    }
  }

  /** True when `word` is a localized keyword in the current language. */
  def isKeyword(word: String): Boolean = keywordSet.contains(word)

  def keywordTemplates: Map[String, String] = active.map(_.keywordTemplates).getOrElse(Map.empty)
  def codeTemplates: Map[String, String] = active.map(_.codeTemplates).getOrElse(Map.empty)
}
