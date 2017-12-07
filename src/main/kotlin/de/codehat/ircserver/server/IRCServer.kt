package de.codehat.ircserver.server

import de.codehat.ircserver.command.CommandRegistry
import de.codehat.ircserver.command.CommandWorkerThread
import de.codehat.ircserver.util.CommandQueue
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class IRCServer(val host: String,
                val port: Int,
                private val maxClients: Int) : IServer {

    val queue = CommandQueue()
    val commandRegistry = CommandRegistry(this)
    val clientThreadPool: ThreadPoolExecutor = ThreadPoolExecutor(
            this.maxClients,
            this.maxClients,
            0L,
            TimeUnit.MILLISECONDS,
            SynchronousQueue<Runnable>())
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