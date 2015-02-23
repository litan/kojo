/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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
package history

import org.jmock.Expectations
import org.jmock.integration.junit4.JMock
import org.jmock.integration.junit4.{JUnit4Mockery => Mockery}
import org.jmock.lib.legacy.ClassImposteriser
import org.junit.After
import org.junit.Assert._
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import net.kogics.kojo.core.HistoryListener
import net.kogics.kojo.core.HistorySaver

@RunWith(classOf[JMock])
class CommandHistoryTest {

  val context: Mockery = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE)
    }
  }

  val commandHistory = new CommandHistory(new NoopHistorySaver)

//  def mockSaver: HistorySaver = {
//    val saver = (context.mock(classOf[HistorySaver])).asInstanceOf[HistorySaver]
//    context.checking (new Expectations {
//        allowing(saver).append(`with`(any(classOf[String])))
//      })
//    saver
//  }

  @Before
  def setUp: Unit = {
  }

  @After
  def tearDown: Unit = {
  }

  @Test
  def testAdd {
    assertEquals(0, commandHistory.size)
    commandHistory.add("12")
    assertEquals(1, commandHistory.size)
    assertEquals("12", commandHistory(0).script)
  }

  @Test
  def testAddListener {
    val listener = (context.mock(classOf[HistoryListener])).asInstanceOf[HistoryListener]
    context.checking (new Expectations {
        one(listener).itemAdded
      })

    commandHistory.setListener(listener)
    commandHistory.add("12")
  }

  @Test
  def testPrev {
    commandHistory.add("12"); commandHistory.add("13"); commandHistory.add("14")
    assertEquals(3, commandHistory.size)
    assertEquals(3, commandHistory.hIndex)

    assertEquals(Some("14"), commandHistory.previous)
    assertEquals(Some("13"), commandHistory.previous)
    assertEquals(Some("12"), commandHistory.previous)
    assertEquals(None, commandHistory.previous)
  }

  @Test
  def testPrevListener {
    commandHistory.add("12"); commandHistory.add("13"); commandHistory.add("14")

    val listener = (context.mock(classOf[HistoryListener])).asInstanceOf[HistoryListener]
    context.checking (new Expectations {
        one(listener).selectionChanged(2)
        one(listener).selectionChanged(1)
        one(listener).selectionChanged(0)
      })

    commandHistory.setListener(listener)
    
    commandHistory.previous
    commandHistory.previous
    commandHistory.previous
    commandHistory.previous // should not trigger a call to the listener
  }

  @Test
  def testToPosition {
    commandHistory.add("12"); commandHistory.add("13"); commandHistory.add("14")

    var str = commandHistory.toPosition(1)
    assertEquals(Some("13"), str)
    assertEquals(1, commandHistory.hIndex)

    str = commandHistory.toPosition(0)
    assertEquals(Some("12"), str)
    assertEquals(0, commandHistory.hIndex)

    str = commandHistory.toPosition(2)
    assertEquals(Some("14"), str)
    assertEquals(2, commandHistory.hIndex)
  }

  @Test
  def testToPositionListener {
    commandHistory.add("12"); commandHistory.add("13"); commandHistory.add("14")

    val listener = (context.mock(classOf[HistoryListener])).asInstanceOf[HistoryListener]
    context.checking (new Expectations {
        one(listener).selectionChanged(1)
        one(listener).selectionChanged(0)
        one(listener).selectionChanged(2)
      })

    commandHistory.setListener(listener)

    commandHistory.toPosition(1)
    commandHistory.toPosition(1) // listener should not be called
    commandHistory.toPosition(0)
    commandHistory.toPosition(0) // listener should not be called
    commandHistory.toPosition(2)
  }

  @Test
  def testNext {
    commandHistory.add("12"); commandHistory.add("13"); commandHistory.add("14")
    assertEquals(3, commandHistory.size)
    assertEquals(3, commandHistory.hIndex)

    commandHistory.toPosition(0)

    assertEquals(Some("13"), commandHistory.next)
    assertEquals(Some("14"), commandHistory.next)
    assertEquals(None, commandHistory.next)
  }

  @Test
  def testNextListener {
    commandHistory.add("12"); commandHistory.add("13"); commandHistory.add("14")

    val listener = (context.mock(classOf[HistoryListener])).asInstanceOf[HistoryListener]
    context.checking (new Expectations {
        one(listener).selectionChanged(1)
        one(listener).selectionChanged(2)
        one(listener).selectionChanged(3) // one past the end
      })

    commandHistory.toPosition(0)
    commandHistory.setListener(listener)

    commandHistory.next
    commandHistory.next
    commandHistory.next
  }

  @Test
  def testEmptyPrev {
    assertEquals(None, commandHistory.previous)
  }

}
