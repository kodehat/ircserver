package de.codehat.ircserver.server

import de.codehat.ircserver.command.CommandRegistry
import de.codehat.ircserver.command.CommandWorkerThread
import de.codehat.ircserver.util.CommandQueue
import java.util.concurrent.*

class IRCServer(val host: String,
                val port: Int,
                private val maxClients: Int) : IServer {

    val queue = CommandQueue()
    val commandRegistry = CommandRegistry(this)
    val clientThreadPool: ThreadPoolExecutor = Executors.newFixedThreadPool(this.maxClients) as ThreadPoolExecutor
    val serverThread = ServerThread(this)

    private val commandThread = CommandWorkerThread(this.queue, { client, cmd ->
        this.commandRegistry.execute(client, cmd.split(Regex(" +"))[0], cmd)
    })

    override fun start() {
        if (this.serverThread.isRunning) throw ServerAlreadyStartedException()
        this.commandThread.start()
        this.serverThread.start()
    }

    override fun stop() {
        if (!this.serverThread.isRunning) throw ServerAlreadyStoppedException()
        this.serverThread.stopServer()
    }

    override fun isRunning(): Boolean {
        return this.serverThread.isRunning
    }

}