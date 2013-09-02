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

package net.kogics.kojo.xscala

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers


@RunWith(classOf[JUnitRunner])
class CodeCompletionUtilsTest extends FunSuite with ShouldMatchers with BeforeAndAfter {
  
  before {
    
  }

  after {
    CodeCompletionUtils.clearLangTemplates()
  }
  
  test("lang templates miss and hit") {
    CodeCompletionUtils.langMethodTemplate("fram", "sv") should be (None)
    CodeCompletionUtils.addTemplates(
      "sv", 
      Map(
        "fram" -> "fram(nsteps)"
      )
    )
    
    CodeCompletionUtils.langMethodTemplate("fram", "sv") should be (Some("fram(nsteps)"))
  }

  test("builtins templates hit") {
    CodeCompletionUtils.methodTemplate("activateCanvas") should be ("activateCanvas()")
  }

  test("builtins templates miss; Tw templates hit") {
    CodeCompletionUtils.methodTemplate("Picture") should be ("Picture {\n    ${cursor}\n}")
  }

  test("builtins templates miss; Tw templates miss; lang templates hit") {
    CodeCompletionUtils.addTemplates(
      "sv", 
      Map(
        "fram" -> "fram(nsteps)"
      )
    )
    CodeCompletionUtils.methodTemplate("fram") should be (null)
    val oldLang = System.getProperty("user.language")
    System.setProperty("user.language", "sv")
    CodeCompletionUtils.methodTemplate("fram") should be ("fram(nsteps)")
    System.setProperty("user.language", oldLang)
  }

  test("lang templates multiple add") {
    CodeCompletionUtils.langMethodTemplate("fram", "sv") should be (None)
    CodeCompletionUtils.addTemplates(
      "sv", 
      Map(
        "fram" -> "fram(nsteps)"
      )
    )
    
    CodeCompletionUtils.langMethodTemplate("fram", "sv") should be (Some("fram(nsteps)"))
    CodeCompletionUtils.langMethodTemplate("bak", "sv") should be (None)

    CodeCompletionUtils.addTemplates(
      "sv", 
      Map(
        "fram" -> "fram(nsteps2)",
        "bak" -> "bak(nsteps)"
      )
    )
    CodeCompletionUtils.langMethodTemplate("fram", "sv") should be (Some("fram(nsteps2)"))
    CodeCompletionUtils.langMethodTemplate("bak", "sv") should be (Some("bak(nsteps)"))
  }
}
