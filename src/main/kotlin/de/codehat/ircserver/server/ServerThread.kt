package de.codehat.ircserver.server

import de.codehat.ircserver.client.Client
import de.codehat.ircserver.client.ClientInfo
import de.codehat.ircserver.util.Log
import java.net.InetSocketAddress
import java.net.ServerSocket

class ServerThread(private val server: IRCServer) : Thread() {

    var isRunning = false
    private val serverSocket = ServerSocket()

    init {
        this.serverSocket.bind(InetSocketAddress(this.server.host, this.server.port))
    }

    override fun run() {
        while (this.isRunning) {
            val clientSocket = this.serverSocket.accept()
            val clientInfo = ClientInfo(
                    id = this.server.clientList.getNextId(),
                    host = clientSocket.inetAddress.hostName,
                    port = clientSocket.port,
                    connectedSince = System.currentTimeMillis()
            )
            Log.Companion.info(this.javaClass, "Accepting connection from ${clientInfo.host}:${clientInfo.port}")
            val client = Client(clientSocket, clientInfo, this.server)
            this.server.clientList.addClient(client)
            client.start()
        }
    }


}