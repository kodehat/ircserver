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
    //Executors.newFixedThreadPool(this.maxClients) as ThreadPoolExecutor
    val serverThread = ServerThread(this)
    private val commandThread = CommandWorkerThread(this.queue, {
        this.commandRegistry.execute(it.split(" +")[0], it)
    })

    override fun start() {
        if (this.serverThread.isRunning) throw ServerAlreadyStartedException()
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