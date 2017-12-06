package de.codehat.ircserver.client

import de.codehat.ircserver.util.CommandQueue
import java.net.Socket

class Client(private val socket: Socket) : IClient {

    private var clientThread = ClientThread(this, this.socket)

    init {
        this.clientThread.start()
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
}