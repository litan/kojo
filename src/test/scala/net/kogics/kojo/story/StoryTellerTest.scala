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
import util.Read._
import net.kogics.kojo.lite.NoOpKojoCtx

class StoryTellerTest {

  val kojoCtx = new NoOpKojoCtx
  val st = new StoryTeller(kojoCtx)

  @Test
  def testIntFieldValue {
    val tf = st.addField("f1", "")
    val fv = st.fieldValue("f1", 3)
    assertTrue(fv.isInstanceOf[Int] && fv == 3)

    tf.setText("5")
    val fv2 = st.fieldValue("f1", 3)
    assertTrue(fv2.isInstanceOf[Int] && fv2 == 5)
  }

  @Test
  def testDoubleFieldValue {
    val tf = st.addField("f1", "")
    val fv = st.fieldValue("f1", 3.0)
    assertTrue(fv.isInstanceOf[Double] && fv == 3)

    tf.setText("5")
    val fv2 = st.fieldValue("f1", 3.0)
    assertTrue(fv2.isInstanceOf[Double] && fv2 == 5)
  }

  @Test
  def testStringFieldValue {
    val tf = st.addField("f1", "")
    val fv = st.fieldValue("f1", "abc")
    assertTrue(fv.isInstanceOf[String] && fv == "abc")

    tf.setText("5")
    val fv2 = st.fieldValue("f1", "abc")
    assertTrue(fv2.isInstanceOf[String] && fv2 == "5")
  }
}
