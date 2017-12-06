package de.codehat.ircserver.client

import java.net.Socket

class ClientThread(private val client: Client, private val socket: Socket) : Thread() {

    override fun run() {
        super.run()
    }

}