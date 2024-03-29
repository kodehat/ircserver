package de.codehat.ircserver

import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Log
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import kotlin.concurrent.thread

class IRCServerTest {

    companion object {
        val host = "localhost"
        val port = 6667
        val maxClients = 20
    }

    init {
        Log.DEBUG = true
    }

    @Test
    fun testServerCanStart() {
        val server = IRCServer(host, port, maxClients, "TestServer", "1.0")
        server.start()
        Thread.sleep(250)
        assertTrue(server.isRunning())
        server.stop()
    }

    @Test
    fun testSameNickTwice() {
        val server = IRCServer(host, port, maxClients,"TestServer", "1.0")
        server.start()
        val (socketOne, bufferedReaderOne, printWriterOne) = Util.connect(host, port)
        val (socketTwo, bufferedReaderTwo, printWriterTwo) = Util.connect(host, port)
        val outputs = mutableListOf<String>()

        Thread({
            bufferedReaderTwo.forEachLine {
                Log.info(this.javaClass, "Got: $it")
                outputs.add(it)
            }
        }).start()

        printWriterOne.println("NICK eric")
        printWriterTwo.println("NICK eric")
        Thread.sleep(2000)

        val expected = ":localhost 433 * eric :Nickname is already in use"

        server.stop()

        assertTrue(outputs.contains(expected))
    }

    @Test
    fun testWelcomeMessage() {
        val server = IRCServer(host, port, maxClients, "TestServer", "1.0")
        server.start()
        val (socketOne, bufferedReaderOne, printWriterOne) = Util.connect(host, port)
        val outputs = mutableListOf<String>()

        Thread({
            bufferedReaderOne.forEachLine {
                Log.info(this.javaClass, "Got: $it")
                outputs.add(it)
            }
        }).start()

        printWriterOne.println("NICK eric")
        printWriterOne.println("USER eric * * :Eric Test")
        Thread.sleep(2000)
        server.stop()

        val expected = ":localhost 001 eric :Welcome to the Internet Relay Network eric!eric@localhost"

        assertTrue(outputs.contains(expected))
    }

}