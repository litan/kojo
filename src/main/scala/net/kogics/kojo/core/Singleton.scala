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
package net.kogics.kojo.core

trait Singleton[T] {
  var _instance: T = _
  protected def newInstance(): T
  protected def instanceCheck() {}
  
  def instance(): T = synchronized {
    instanceCheck()
    if (_instance == null) {
      _instance = newInstance()
    }
    _instance
  }
}

trait InitedSingleton[T] extends Singleton[T] {

  private var instanceInited = false

  protected def instanceInit() {
    instanceInited = true
  }

  protected override def instanceCheck() {
    if (!instanceInited) throw new IllegalStateException("Instance not initialized")
  }
}


