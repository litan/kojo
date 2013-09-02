/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.kojo
package story

import org.junit.Test
import org.junit.Assert._
import net.kogics.kojo.lite.NoOpKojoCtx

class LinkListenerTest {

  val kojoCtx = new NoOpKojoCtx
  val st = new StoryTeller(kojoCtx)
  val ll = new LinkListener(st)
  
  @Test
  def test1 {
    assertEquals((2,1), ll.localpageLocation("http://localpage/2"))
  }

  @Test
  def test2 {
    assertEquals((2,3), ll.localpageLocation("http://LOCALPAGE/2#3"))
  }

  @Test
  def test3 {
    assertEquals((12,11), ll.localpageLocation("http://localpage/12#11"))
  }

  @Test
  def test4 {
    val pg = Page(
      name = "",
      body =
        <body>
        </body>,
      code = {}
    )

    ll.setStory(Story(pg))
    try {
      ll.localpageLocation("http://localpage/a#11")
      fail("Invalid location not detected")
    }
    catch {
      case ex: IllegalArgumentException =>
        assertTrue(true)
    }
  }

  @Test
  def test5 {
    try {
      ll.localpageLocation("http://localpage/5#x")
      fail("Invalid location not detected")
    }
    catch {
      case ex: IllegalArgumentException =>
        assertTrue(true)
    }
  }

  @Test
  def test6 {
    val story = Story(
      Page(
        name = "pg1",
        body = 
          <body>
          </body>,
        code = {}
      ),
      IncrPage(
        name = "pg2",
        style = "",
        body = List(
          Para(
            <p>
                Para1
            </p>
          ),
          Para(
            <p>
                Para2
            </p>
          )
        )
      ),      
      Page(
        name = "pg3",
        body = 
          <body>
          </body>,
        code = {}
      )
    )
    
    ll.setStory(story)
    assertEquals((1,1), ll.localpageLocation("http://localpage/pg1"))
    assertEquals((2,1), ll.localpageLocation("http://localpage/pg2"))
    assertEquals((2,1), ll.localpageLocation("http://localpage/pg2#1"))
    assertEquals((2,2), ll.localpageLocation("http://localpage/pg2#2"))
    assertEquals((3,1), ll.localpageLocation("http://localpage/pg3#1"))

    try {
      ll.localpageLocation("http://localpage/nopage")
      fail("Invalid location not detected")
    }
    catch {
      case ex: IllegalArgumentException =>
        assertTrue(true)
    }
  }

  @Test
  def testHandlerData {
    assertEquals(("code","5"), ll.handlerData("http://runHandler/code/5"))
  }

  @Test
  def testHandlerData2 {
    assertEquals(("code2","7"), ll.handlerData("http://runhandler/code2/7 "))
  }

  @Test
  def testHandlerData3 {
    assertEquals(("code3",""), ll.handlerData("http://runhandler/code3"))
  }
}
