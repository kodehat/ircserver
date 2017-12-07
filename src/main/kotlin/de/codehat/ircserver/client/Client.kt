package de.codehat.ircserver.client

import de.codehat.ircserver.command.CommandWorkerThread
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.CommandQueue
import de.codehat.ircserver.util.Log
import java.io.PrintWriter
import java.net.Socket

class Client(private val socket: Socket, private val clientInfo: ClientInfo, val server: IRCServer) : IClient {

    val clientThread = ClientThread(this, this.socket)
    private val queue = CommandQueue()
    private val outputStream = PrintWriter(this.socket.getOutputStream(), true)
    private val commandThread = CommandWorkerThread(this.queue, {
        Log.Companion.info(this.javaClass, "Sending '$it' to client with id ${this.clientInfo.id}")
        this.outputStream.println(it)
    })

    override fun start() {
        if (this.commandThread.isRunning && this.clientThread.isRunning) throw ClientAlreadyStartedException()
        this.clientThread.isRunning = true
        this.commandThread.start()
    }

    override fun close() {
        if (this.socket.isClosed && this.clientInfo.state == ClientState.CLOSED) throw ClientAlreadyClosedException()
        this.clientInfo.state = ClientState.CLOSED
        this.socket.close()
    }

    override fun state(): ClientState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun queue(): CommandQueue {
        return this.queue
    }

    override fun info(): ClientInfo {
        return this.clientInfo
    }
}