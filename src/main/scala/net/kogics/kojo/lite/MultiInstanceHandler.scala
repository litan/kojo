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
package net.kogics.kojo.lite

import java.rmi.Remote
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.server.UnicastRemoteObject

import net.kogics.kojo.util.Utils

trait MultiInstanceHandler extends Remote {
  @throws(classOf[RemoteException])
  def newInstance(args: Array[String]): Unit
}

object MultiInstanceHandler {
  val Name = "MultiInstanceHandler"
  val Port = 27468
  def run() {
    val mih = new MultiInstanceHandlerImpl()
    try {
      val stub = UnicastRemoteObject.exportObject(mih, 0)
      val registry = LocateRegistry.getRegistry(Port)
      registry.rebind(Name, stub)
    }
    catch {
      case t: Throwable =>
//        println("Problem starting MultiInstanceHandler: " + t)
    }
  }
}

class MultiInstanceHandlerImpl extends MultiInstanceHandler {
  def newInstance(args: Array[String]) {
    Utils.runInSwingThread {
      Main.frame.toFront()
      if (args.length > 0) {
        Main.loadAndRunUrl(args(0))
      }
    }
  }
}