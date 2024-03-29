package de.codehat.ircserver.server

import de.codehat.ircserver.antlr4.ParsedMessage
import de.codehat.ircserver.command.CommandRegistry
import de.codehat.ircserver.command.CommandWorkerThread
import de.codehat.ircserver.util.CommandQueue
import java.io.File
import java.util.*
import java.util.concurrent.*
import java.util.stream.Collectors

class IRCServer(val host: String,
                val port: Int,
                private val maxClients: Int,
                val servername: String,
                val version: String,
                val date: Date = Date()) : IServer {

    val queue = CommandQueue()
    val commandRegistry = CommandRegistry(this)
    val clientThreadPool: ThreadPoolExecutor = Executors.newFixedThreadPool(this.maxClients) as ThreadPoolExecutor
    val serverThread = ServerThread(this)
    var motdLines: MutableList<String>? = loadMotd()
        private set

    private fun loadMotd(): MutableList<String>? {
        val motdFile = File("./motd.txt")
        if (!motdFile.exists()) return null

        return motdFile.inputStream().bufferedReader().lines().collect(Collectors.toList())
    }

    private val commandThread = CommandWorkerThread(this.queue) { client, cmd ->
        this.commandRegistry.execute(client, cmd as ParsedMessage)
    }

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