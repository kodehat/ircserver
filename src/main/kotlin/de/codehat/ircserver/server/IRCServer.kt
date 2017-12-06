package de.codehat.ircserver.server

class IRCServer(private val host: String = "localhost",
                private val port: Int = 6667) : IServer {

    private var serverThread: ServerThread? = null

    override fun start(): Boolean {
        if (this.serverThread == null) {
            this.serverThread = ServerThread(this)
            this.serverThread!!.start()
        }
    }

    override fun stop(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isRunning(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




}