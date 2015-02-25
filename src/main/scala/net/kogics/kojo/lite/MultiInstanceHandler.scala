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
  // make mih static so that it does not get garbage collected
  // we're trying to prevent the sporadic lack of activation of the first instance of Kojo from an nth instance 
  // and the accompanying 'Problem - no such object in table' message that shows up in the Java Console  
  val mih = new MultiInstanceHandlerImpl()
  def run() {
    try {
      val sf = new RmiLocalhostSocketFactory
      val stub = UnicastRemoteObject.exportObject(mih, 0, sf, sf)
      val registry = LocateRegistry.getRegistry(Utils.localHostString, Utils.RmiRegistryPort)
      registry.rebind(Name, stub)
    }
    catch {
      case t: Throwable =>
        System.out.println("Problem starting MultiInstanceHandler: " + t.getMessage)
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