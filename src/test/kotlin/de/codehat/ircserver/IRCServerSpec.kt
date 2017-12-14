package de.codehat.ircserver

import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Log
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertNull

object IRCServerSpec: Spek({

    val host = "localhost"
    val port = 6667
    val maxClients = 20

    given("an IRC server") {
        Log.DEBUG = true
        val server = IRCServer(host, port, maxClients)
        server.start()
        Util.sleep(250) // Wait for server to start

        it("should return nothing after valid NICK") {
            val (socket, bufferedReader, printWriter) = Util.connect(host, port)

            printWriter.println("NICK eric")
            Util.sleep(250) // Wait for server to respond
            server.stop()
            val actual: String? = bufferedReader.readLine()

            assertNull(actual)
        }
    }

})