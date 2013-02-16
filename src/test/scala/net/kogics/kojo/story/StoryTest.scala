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

class StoryTest {

  @Test
  def test1 {
    val html1 =
      <div style="color:green; font-size=18px">
        Page 1
      </div>
    val pg1 = Page(name="", body = html1)

    val story = Story(pg1)
    assertFalse(story.hasNextView)
    assertFalse(story.hasPrevView)
    assertEquals(html1, story.view)
    assertEquals((1, 1), story.location)
  }

  @Test
  def test2 {
    val para1 =
      <p>
        Para 1
      </p>

    val pg1 = IncrPage(
      name="",
      style="",
      body = List(
        Para(para1)
      )
    )

    val story = Story(pg1)

    val html1 =
      <body style="">
        {para1}
      </body>

    assertFalse(story.hasNextView)
    assertFalse(story.hasPrevView)

    val pp = new xml.PrettyPrinter(2, 2)
    assertEquals(pp.format(html1), pp.format(story.view))
    assertEquals((1, 1), story.location)
  }

  @Test
  def test3 {
    val para1 =
      <p >
        Para 1
      </p>

    val para2 =
      <p >
        Para 2
      </p>

    val pg1 = IncrPage(
      name="",
      style="color:green",
      body = List(
        Para(para1),
        Para(para2)
      )
    )


    val story = Story(pg1)

    val html1 =
      <body style="color:green">
        {para1}
      </body>

    assertTrue(story.hasNextView)
    assertFalse(story.hasPrevView)

    val pp = new xml.PrettyPrinter(2, 2)
    assertEquals(pp.format(html1), pp.format(story.view))
    assertEquals((1, 1), story.location)

    story.forward()
    assertEquals((1, 2), story.location)

    val html2 =
      <body style="color:green">
        {para1}{para2}
      </body>

    assertFalse(story.hasNextView)
    assertTrue(story.hasPrevView)
    assertEquals(pp.format(html2), pp.format(story.view))

    story.back()
    assertEquals((1, 1), story.location)
    assertTrue(story.hasNextView)
    assertFalse(story.hasPrevView)
    assertEquals(pp.format(html1), pp.format(story.view))
  }

  @Test
  def test4 {
    val para1 =
      <p >
        Para 1
      </p>

    val para2 =
      <p >
        Para 2
      </p>

    val pg1 = IncrPage(
      name="",
      style="color:green",
      body = List(
        Para(para1),
        Para(para2)
      )
    )

    val pgHhtml =
      <div style="color:green; font-size=18px">
        Page 1
      </div>
    val pg2 = Page(name="", body = pgHhtml)


    val story = Story(pg1, pg2)

    val html1 =
      <body style="color:green">
        {para1}
      </body>

    assertTrue(story.hasNextView)
    assertFalse(story.hasPrevView)

    val pp = new xml.PrettyPrinter(2, 2)
    assertEquals(pp.format(html1), pp.format(story.view))
    assertEquals((1, 1), story.location)

    story.forward()
    assertEquals((1, 2), story.location)

    val html2 =
      <body style="color:green">
        {para1}{para2}
      </body>

    assertTrue(story.hasNextView)
    assertTrue(story.hasPrevView)
    assertEquals(pp.format(html2), pp.format(story.view))

    story.forward()
    assertEquals((2, 1), story.location)
    assertFalse(story.hasNextView)
    assertTrue(story.hasPrevView)
    assertEquals(pp.format(pgHhtml), pp.format(story.view))

    story.back()
    assertEquals((1, 2), story.location)
    assertTrue(story.hasNextView)
    assertTrue(story.hasPrevView)
    assertEquals(pp.format(html2), pp.format(story.view))

    story.back()
    assertEquals((1, 1), story.location)
    assertTrue(story.hasNextView)
    assertFalse(story.hasPrevView)
    assertEquals(pp.format(html1), pp.format(story.view))
  }

  @Test
  def testLocation {
    val para1_1 =
      <p >
        Para 1
      </p>

    val para1_2 =
      <p >
        Para 2
      </p>

    val para2_1 =
      <p >
        Para 1
      </p>


    val pg1 = IncrPage(
      name="", 
      style="color:green",
      body = List(
        Para(para1_1),
        Para(para1_2)
      )
    )

    val pg3 = IncrPage(
      name="",
      style="color:green",
      body = List(
        Para(para2_1)
      )
    )

    val pg2Hhtml =
      <div style="color:green; font-size=18px">
        Page 1
      </div>
    val pg4Hhtml =
      <div style="color:green; font-size=18px">
        Page 2
      </div>
    val pg2 = Page(name="", body = pg2Hhtml)
    val pg4 = Page(name="", body = pg4Hhtml)

    val story = Story(pg1, pg2, pg3, pg4)

    assertFalse(story.hasView(0, 1))
    assertTrue(story.hasView(1, 1))
    assertTrue(story.hasView(1, 2))
    assertFalse(story.hasView(1, 3))
    assertFalse(story.hasView(1, 0))
    assertTrue(story.hasView(2, 1))
    assertFalse(story.hasView(2, 0))
    assertFalse(story.hasView(2, 2))
    assertTrue(story.hasView(3, 1))
    assertFalse(story.hasView(3, 0))
    assertFalse(story.hasView(3, 2))
    assertTrue(story.hasView(4, 1))
    assertFalse(story.hasView(4, 0))
    assertFalse(story.hasView(4, 2))

    assertEquals((1,1), story.location)
    story.forward()
    assertEquals((1,2), story.location)
    story.forward()
    assertEquals((2,1), story.location)
    story.forward()
    assertEquals((3,1), story.location)
    story.forward()
    assertEquals((4,1), story.location)
    story.back()
    assertEquals((3,1), story.location)
    story.back()
    assertEquals((2,1), story.location)
    story.back()
    assertEquals((1,2), story.location)
    story.back()
    assertEquals((1,1), story.location)
  }
}
