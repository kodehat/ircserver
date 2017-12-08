package de.codehat.ircserver

import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Log
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.on
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.TimeUnit
import kotlin.test.assertNull

object IRCServerSpec: Spek({

    val host = "localhost"
    val port = 6667
    val maxClients = 20

    given("an IRC server") {
        Log.DEBUG = true
        val server = IRCServer(host, port, maxClients)
        server.serverThread.start()
        sleep(250) // Wait for server to start

        on("sending valid NICK command first") {
            val (socket, bufferedReader, printWriter) = connect(host, port)

            printWriter.println("NICK eric")
            sleep(250) // Wait for server to respond
            val actual: String? = bufferedReader.readLine()

            assertNull(actual)
        }
    }

})

fun connect(host: String, port: Int): Triple<Socket, BufferedReader, PrintWriter> {
    val socket = Socket(host, port)
    val bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
    val printWriter = PrintWriter(socket.getOutputStream(), true)
    return Triple(socket, bufferedReader, printWriter)
}

fun sleep(timeout: Long) = TimeUnit.MILLISECONDS.sleep(timeout)