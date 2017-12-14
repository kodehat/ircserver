package de.codehat.ircserver.server

import de.codehat.ircserver.client.Client
import de.codehat.ircserver.client.ClientInfo
import de.codehat.ircserver.client.ClientList
import de.codehat.ircserver.command.Message
import de.codehat.ircserver.util.Entry
import de.codehat.ircserver.util.Log
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.SocketException
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.TimeUnit

class ServerThread(private val server: IRCServer) : Thread() {

    var isRunning = false
    private val serverSocket = ServerSocket()

    init {
        this.serverSocket.bind(InetSocketAddress(this.server.host, this.server.port))
    }

    fun stopServer() {
        this.isRunning = false
        this.serverSocket.close()
    }

    override fun start() {
        this.isRunning = true
        super.start()
    }

    override fun run() {
        try {
            while (this.isRunning) {
                val clientSocket = this.serverSocket.accept()
                val clientInfo = ClientInfo(
                        id = ClientList.getNextId(),
                        host = clientSocket.inetAddress.hostName,
                        port = clientSocket.port,
                        connectedSince = System.currentTimeMillis()
                )
                Log.info(this.javaClass, "Accepting connection from ${clientInfo.host}:${clientInfo.port}")
                val client = Client(clientSocket, clientInfo, this.server)
                ClientList.addClient(client)
                client.start()
                this.server.clientThreadPool.execute(client.clientThread)
            }
        } catch (e: SocketException) {
            Log.info(this.javaClass, "Server closed. Disconnecting all clients...")
            ClientList.getAllClients().forEach { it.close() }
        }

    }


}