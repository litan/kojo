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

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class KeywordLangsTest extends FunSuite with Matchers with BeforeAndAfter {
  // the test JVM does not run in Turkish, so the registry is quiet unless forced
  after { KeywordLangs.forcedLang = None }

  test("with no language forced, the registry is empty (English and every other locale)") {
    KeywordLangs.forcedLang = None
    KeywordLangs.keywords should be(Nil)
    KeywordLangs.isKeyword("dez") should be(false)
    KeywordLangs.keywordTemplates should be(Map.empty)
    KeywordLangs.codeTemplates should be(Map.empty)
  }

  test("forcing Turkish exposes the Turkish keyword pack") {
    KeywordLangs.forcedLang = Some("tr")
    KeywordLangs.isKeyword("dez") should be(true) // val
    KeywordLangs.isKeyword("tanım") should be(true) // def
    KeywordLangs.isKeyword("nesne") should be(true) // object
    KeywordLangs.isKeyword("val") should be(false) // English is not in this set
    KeywordLangs.keywords should contain("eğer") // if
    KeywordLangs.keywordTemplates should not be empty
    KeywordLangs.codeTemplates should not be empty
  }

  test("forcing an unregistered language stays empty") {
    KeywordLangs.forcedLang = Some("xx")
    KeywordLangs.keywords should be(Nil)
    KeywordLangs.isKeyword("dez") should be(false)
  }

  test("the cached keyword set follows a change of active language") {
    KeywordLangs.forcedLang = Some("tr")
    KeywordLangs.isKeyword("dez") should be(true)
    KeywordLangs.forcedLang = None
    KeywordLangs.isKeyword("dez") should be(false) // cache invalidated, not stale
    KeywordLangs.forcedLang = Some("tr")
    KeywordLangs.isKeyword("dez") should be(true)
  }

  test("the registry and the launcher's language list stay in sync") {
    // Launcher codes come from lightweight LanguageRegistry metadata, which
    // must stay in sync with the editor's keyword packs.
    net.kogics.kojo.lite.ScalaToolchain.keywordLanguages should be(KeywordLangs.codes)
  }
}
