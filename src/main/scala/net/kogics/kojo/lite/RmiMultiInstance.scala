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

import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject

import net.kogics.kojo.util.Utils

trait RmiMultiInstance {
  @volatile var registry: Option[Registry] = None
  def firstInstance: Boolean = {
    try {
      registry = Some(LocateRegistry.getRegistry(Utils.localHostString, Utils.RmiRegistryPort))
      registry.get.lookup(MultiInstanceHandler.Name).asInstanceOf[MultiInstanceHandler]
      // registry is up and name is bound. Not the first instance
      false
    }
    catch {
      case ex: Exception =>
        // first instance
        true
    }
  }

  def firstMain(args: Array[String]) {
    try {
      val sf = new RmiLocalhostSocketFactory
      registry = Some(LocateRegistry.createRegistry(Utils.RmiRegistryPort, sf, sf))
    }
    catch {
      case e: Throwable =>
        println("Unable to create RMI registry [%s]. Multi instance support will not work" format (e.getMessage()))
        registry = None
    }
  }

  def firstMainDone() {
    try {
      println("[INFO] Removing RMI Listener")
      registry.foreach { UnicastRemoteObject.unexportObject(_, true) }
    }
    catch {
      case e: Throwable =>
    }
  }

  def nthMain(args: Array[String]): Unit = Utils.safeProcess {
    registry foreach { r =>
      val mih = r.lookup(MultiInstanceHandler.Name).asInstanceOf[MultiInstanceHandler]
      println(s"[INFO] Connecting (via RMI) with args: ${args.mkString("[", ", ", "]")} to already running Kojo instance")
      mih.newInstance(args)
    }
  }
}