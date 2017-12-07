package de.codehat.ircserver.client

import de.codehat.ircserver.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import java.net.SocketException

class ClientThread(private val client: Client, private val socket: Socket) : Runnable {

    var isRunning = false
    private val inputStream = BufferedReader(InputStreamReader(this.socket.getInputStream()))

    override fun run() {
        Log.info(this.javaClass, "${getIpAndPort()} started his thread")
        try {
            this.inputStream.forEachLine {
                //TODO: Trim 'it'
                if (!this.client.server.commandRegistry.commandExists(it.split(" +")[0])) {
                    Log.info(this.javaClass, "${getIpAndPort()} sent unknown command '$it'")
                    this.client.queue().put("${it.split(" +")[0]} :Unknown command")
                }
                Log.info(this.javaClass, "${getIpAndPort()} sent command '$it'")
                this.client.server.queue.put(it)
            }
        } catch (e: SocketException) {
            // Socket timeout
        }

        Log.info(this.javaClass, "${getIpAndPort()} has disconnected")
        ClientList.removeClient(this.client.info().id)
        this.client.close()
    }

    private fun getIpAndPort() = "{${this.client.info().host}:${this.client.info().port}}"

}