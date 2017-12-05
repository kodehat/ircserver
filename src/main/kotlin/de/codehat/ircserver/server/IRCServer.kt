package de.codehat.ircserver.server

class IRCServer(private val host: String = "localhost",
                private val port: Int = 6667) {

    private var serverThread: ServerThread? = null

    fun start(): Boolean {
        if (this.serverThread == null) {
            this.serverThread = ServerThread(this)
            this.serverThread!!.start()
        }
    }

}