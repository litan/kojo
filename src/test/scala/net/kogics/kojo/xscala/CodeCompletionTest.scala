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
package net.kogics.kojo.xscala

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

class CodeCompletionTest {

  @Test
  def test1 = {
    assertEquals((Some("abc"), None), CodeCompletionUtils.findIdentifier("abc."))
  }

  @Test
  def test2 = {
    assertEquals((Some("abc"), None), CodeCompletionUtils.findIdentifier(" abc."))
  }

  @Test
  def test3 = {
    assertEquals((None, Some("abc")), CodeCompletionUtils.findIdentifier("abc"))
  }

  @Test
  def test4 = {
    assertEquals((None, Some("abc")), CodeCompletionUtils.findIdentifier(" abc"))
  }


  @Test
  def test5 = {
    assertEquals((Some("def"), None), CodeCompletionUtils.findIdentifier(" abc.def."))
  }


  @Test
  def test6 = {
    assertEquals((Some("def"), None), CodeCompletionUtils.findIdentifier("abc.def."))
  }


  @Test
  def test7 = {
    assertEquals((Some("abc"), Some("def")), CodeCompletionUtils.findIdentifier("abc.def"))
  }

  @Test
  def test8 = {
    assertEquals((Some("abc"), Some("def")), CodeCompletionUtils.findIdentifier(" abc.def"))
  }

  @Test
  def test9 = {
    assertEquals((Some("def"), Some("ghi")), CodeCompletionUtils.findIdentifier("abc.def.ghi"))
  }

  @Test
  def test10 = {
    assertEquals((Some("s"), Some("ab")), CodeCompletionUtils.findIdentifier("x.filter {s => s.ab"))
  }

  @Test
  def test11 = {
    assertEquals((Some("s"), Some("ab")), CodeCompletionUtils.findIdentifier("x.filter {s=>s.ab"))
  }

  @Test
  def test12 = {
    assertEquals((None, Some("abc")), CodeCompletionUtils.findIdentifier("forward(abc"))
  }

  @Test
  def test13 = {
    assertEquals((None, Some("fo")), CodeCompletionUtils.findIdentifier("fo"))
  }

  @Test
  def test14 = {
    assertEquals((None, Some("fo")), CodeCompletionUtils.findIdentifier("\nfo"))
  }

  @Test
  def test15 = {
    assertEquals((None, Some("ab")), CodeCompletionUtils.findIdentifier("s.map {abc => ab"))
  }
}
