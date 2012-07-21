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
package lite

import net.kogics.kojo.util.Utils
import java.io.File
import bibliothek.gui.dock.common.CLocation

object KojoCtx extends core.Singleton[KojoCtx] {
  protected def newInstance = new KojoCtx
}

class KojoCtx extends core.KojoCtx {

  var topcs: TopCs = _
  
  // Todo KojoLite - support activateCanvas, now that requestFocusInWindow has been commented out below 

  def makeTurtleWorldVisible() {
    topcs.dch.toFront
//    topcs.dch.dc.requestFocusInWindow
  }

  def makeStagingVisible() = makeTurtleWorldVisible()

  def makeMathWorldVisible() {
    topcs.mwh.toFront
  }

  def makeStoryTellerVisible() {
    topcs.sth.setLocation(CLocation.base.normalWest(0.5))
  }

  def baseDir: String = System.getProperty("user.dir")

  def stopAnimation() = Utils.runInSwingThread {
    CodeExecutionSupport.instance.stopAnimation()
  }

  def stopInterpreter() = Utils.runInSwingThread {
    CodeExecutionSupport.instance.stopInterpreter()
  }
}