package de.codehat.ircserver.server

class ServerThread(private val server: IRCServer) : Thread() {

    var isRunning = false

    override fun run() {
        while (this.isRunning) {

        }
    }


}