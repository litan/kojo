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
class HelpTest extends FunSuite with ShouldMatchers with BeforeAndAfter {
  
  after {
    Help.clearLangContent()
  }
  
  test("lang help miss and hit") {
    Help.langHelp("fram", "sv") should be (None)
    Help.addContent(
      "sv", 
      Map(
        "fram" -> "Help for fram"
      )
    )
    
    Help.langHelp("fram", "sv") should be (Some("Help for fram"))
  }

  test("common help hit") {
    Help("gridOn") should be ("gridOn() - Shows a grid on the turtle canvas.")
  }

  test("common help miss; Tw help hit") {
    Help("towards") should be ("towards(x, y) - Turns the turtle towards the point (x, y).")
  }

  test("common help miss; Tw help miss; lang help hit") {
    Help.addContent(
      "sv", 
      Map(
        "fram" -> "Help for fram"
      )
    )
    Help("fram") should be ("Coming Soon...")
    val oldLang = System.getProperty("user.language")
    System.setProperty("user.language", "sv")
    Help("fram") should be ("Help for fram")
    System.setProperty("user.language", oldLang)
  }

  test("lang help multiple adds") {
    Help.langHelp("fram", "sv") should be (None)
    Help.addContent(
      "sv", 
      Map(
        "fram" -> "Help for fram"
      )
    )
    
    Help.langHelp("fram", "sv") should be (Some("Help for fram"))
    Help.langHelp("bak", "sv") should be (None)

    Help.addContent(
      "sv", 
      Map(
        "fram" -> "double",
        "bak" -> "Help for bak"
      )
    )
    
    Help.langHelp("fram", "sv") should be (Some("double"))
    Help.langHelp("bak", "sv") should be (Some("Help for bak"))
  }

  test("lang help multiple adds - 2") {
    Help.langHelp("fram", "sv") should be (None)
    Help.addContent(
      "sv", 
      Map(
      )
    )
    
    Help.addContent(
      "sv", 
      Map(
        "fram" -> "Help for fram",
        "bak" -> "Help for bak"
      )
    )
    
    Help.langHelp("fram", "sv") should be (Some("Help for fram"))
    Help.langHelp("bak", "sv") should be (Some("Help for bak"))
  }

  test("lang help multiple adds - 3") {
    Help.langHelp("fram", "sv") should be (None)
    Help.addContent(
      "sv", 
      Map(
        "fram" -> "Help for fram",
        "bak" -> "Help for bak"
      )
    )
    
    Help.addContent(
      "sv", 
      Map(
        "fram" -> "Help for fram",
        "bak" -> "Help for bak"
      )
    )
    
    Help.langHelp("fram", "sv") should be (Some("Help for fram"))
    Help.langHelp("bak", "sv") should be (Some("Help for bak"))
  }
  
  test("lang help multiple adds - 4") {
    Help.langHelp("fram", "sv") should be (None)
    Help.addContent(
      "sv", 
      Map(
        "fram" -> "Help for fram",
        "bak" -> "Help for bak",
        "sudda" -> "Help for sudda"
      )
    )
    
    Help.addContent(
      "sv", 
      Map(
        "fram" -> "Help for fram",
        "bak" -> "Help for bak",
        "sudda2" -> "Help for sudda2"
      )
    )
    
    Help.langHelp("fram", "sv") should be (Some("Help for fram"))
    Help.langHelp("bak", "sv") should be (Some("Help for bak"))
    Help.langHelp("sudda", "sv") should be (Some("Help for sudda"))
    Help.langHelp("sudda2", "sv") should be (Some("Help for sudda2"))
  }
}
