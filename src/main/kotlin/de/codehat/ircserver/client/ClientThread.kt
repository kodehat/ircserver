package de.codehat.ircserver.client

import de.codehat.ircserver.command.Message
import de.codehat.ircserver.util.Entry
import de.codehat.ircserver.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import java.net.SocketException

class ClientThread(private val client: Client, private val socket: Socket) : Runnable {

    var isRunning = false
    private val inputStream = BufferedReader(InputStreamReader(this.socket.getInputStream()))

    override fun run() {
        Log.info(this.javaClass, "${getId()} started his thread")
        try {
            this.inputStream.forEachLine {
                val commandParts = it.trim().split(Regex(" +"))
                Log.info(this.javaClass, "Got command '${commandParts[0]}'")
                if (commandParts[0] == "PONG") return@forEachLine // silently drop "PONG" command
                if (!this.client.server.commandRegistry.commandExists(commandParts[0])) {
                    val response = Message.ERR_UNKNOWNCOMMAND.getTemplate()
                            .add("nick", this.client.info().nickname ?: "*")
                            .add("command", it)
                            .render()
                    Log.info(this.javaClass, "${getId()} sent unknown command '$it'")
                    this.client.queue().put(Entry(this.client, response))
                } else {
                    Log.info(this.javaClass, "${getId()} sent command '${commandParts[0]}'")
                    this.client.server.queue.put(Entry(this.client, it))
                }
            }
        } catch (e: SocketException) {
            // Socket timeout
        }

        Log.info(this.javaClass, "${getId()} has disconnected")
        ClientList.removeClient(this.client.info().id)
        if (this.client.info().state != ClientState.CLOSED) this.client.close()
    }

    private fun getId() = "{${this.client.info().id}}"

}