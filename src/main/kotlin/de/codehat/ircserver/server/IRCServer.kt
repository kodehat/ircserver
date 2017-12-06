package de.codehat.ircserver.server

import de.codehat.ircserver.client.ClientList

class IRCServer(val host: String,
                val port: Int) : IServer {

    var serverThread: ServerThread? = null
        private set
    val clientList = ClientList()

    override fun start(): Boolean {
        if (this.serverThread == null) {
            this.startServerThread(true) // Server thread object is null, create and start it
            return true
        } else if (!this.serverThread!!.isRunning) {
            this.startServerThread(false) // Server thread object exists and isn't running, just start it
            return true
        }

        return false
    }

    private fun startServerThread(newObject: Boolean) {
        if (newObject) {
            this.serverThread = ServerThread(this)
        }
        this.serverThread!!.isRunning = true
        this.serverThread!!.start()
    }

    override fun stop(): Boolean {
        if (this.serverThread == null || !this.serverThread!!.isRunning) {
            return false
        }

        this.serverThread!!.isRunning = false
        return true
    }

    override fun isRunning(): Boolean {
        return this.serverThread?.isRunning ?: false // If server thread != null return its status, else return false
    }

}