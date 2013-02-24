package net.kogics.kojo.lite

import java.net.ServerSocket
import java.net.Socket
import java.rmi.server.RMIClientSocketFactory
import java.rmi.server.RMIServerSocketFactory

import net.kogics.kojo.util.Utils

class RmiLocalhostSocketFactory extends RMIClientSocketFactory with RMIServerSocketFactory with Serializable {

  def createServerSocket(port: Int): ServerSocket = {
    new ServerSocket(port, 0, Utils.localHost)
  }

  def createSocket(host: String, port: Int): Socket = {
    new Socket(Utils.localHost, port)
  }

}