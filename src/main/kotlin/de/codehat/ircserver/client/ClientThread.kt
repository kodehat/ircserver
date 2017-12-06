package de.codehat.ircserver.client

import de.codehat.ircserver.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ClientThread(private val client: Client, private val socket: Socket) : Thread() {

    var isRunning = false
    private val inputStream = BufferedReader(InputStreamReader(this.socket.getInputStream()))
    private val outputStream = PrintWriter(this.socket.getOutputStream(), true)

    override fun run() {
        Log.info(this.javaClass, "${getIpAndPort()} started his thread")
        this.inputStream.forEachLine {
            Log.info(this.javaClass, "${getIpAndPort()} sent '$it'")
        }
        Log.info(this.javaClass, "${getIpAndPort()} has disconnected")
        this.client.server.clientList.removeClient(this.client.info().id)
    }

    private fun getIpAndPort() = "{${this.client.info().host}:${this.client.info().port}}"

}