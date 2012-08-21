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

package net.kogics.kojo.core

trait SpriteListener {
  /**
   * Tell Listener that Sprite has pending commands in its queue
   */
  def hasPendingCommands(): Unit

  /**
   * Tell Listener that Sprite has no more pending commands.
   */
  def pendingCommandsDone(): Unit
}

abstract class AbstractSpriteListener extends SpriteListener {
  def hasPendingCommands: Unit = {}
  def pendingCommandsDone(): Unit = {}
}

object NoopSpriteListener extends AbstractSpriteListener {}

class DelegatingSpriteListener extends SpriteListener {
  @volatile var realListener: SpriteListener = NoopSpriteListener
  def setRealListener(l: SpriteListener) {
    realListener = l
  }
  def hasPendingCommands = realListener.hasPendingCommands()
  def pendingCommandsDone() = realListener.pendingCommandsDone()
}

