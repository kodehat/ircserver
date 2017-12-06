package de.codehat.ircserver.server

class IRCServer(private val host: String = "localhost",
                private val port: Int = 6667) : IServer {

    private var serverThread: ServerThread? = null

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