package de.codehat.ircserver.client

import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.CommandQueue
import java.net.Socket

class Client(private val socket: Socket, private val clientInfo: ClientInfo, val server: IRCServer) : IClient {

    private var clientThread = ClientThread(this, this.socket)

    override fun start(): Boolean {
        if (this.clientThread.isRunning) return false
        this.clientThread.isRunning = true
        this.clientThread.start()
        return true
    }

    override fun close(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun state(): ClientState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun queue(): CommandQueue {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun info(): ClientInfo {
        return this.clientInfo
    }
}