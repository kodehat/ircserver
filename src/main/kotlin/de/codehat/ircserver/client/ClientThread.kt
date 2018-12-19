package de.codehat.ircserver.client

import de.codehat.ircserver.antlr4.IRCGrammarLexer
import de.codehat.ircserver.antlr4.IRCGrammarParser
import de.codehat.ircserver.antlr4.IRCMessageListener
import de.codehat.ircserver.antlr4.ParsedMessage
import de.codehat.ircserver.command.Message
import de.codehat.ircserver.util.Entry
import de.codehat.ircserver.util.Log
import de.codehat.ircserver.util.ResponseEntry
import de.codehat.ircserver.util.SendEntry
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import java.net.SocketException
import org.antlr.v4.runtime.tree.ParseTreeWalker

class ClientThread(private val client: Client, private val socket: Socket) : Runnable {

    var isRunning = false
    private val inputStream = BufferedReader(InputStreamReader(this.socket.getInputStream()))

    override fun run() {
        Log.info(this.javaClass, "${getId()} started his thread")
        try {
            this.inputStream.forEachLine {
                val parsedMessage = parse(it + "\n")
                Log.info(this.javaClass, "Got command '${parsedMessage.command}'")

                if (parsedMessage.command == "PONG") return@forEachLine // silently drop "PONG" command

                if (!this.client.server.commandRegistry.commandExists(parsedMessage.command)) {
                    val response = Message.ERR_UNKNOWNCOMMAND.getTemplate()
                            .add("nick", this.client.info().nickname ?: "*")
                            .add("command", it)
                            .render()
                    Log.info(this.javaClass, "${getId()} sent unknown command '${parsedMessage.command}'")
                    this.client.queue().put(ResponseEntry(this.client, response))
                } else {
                    Log.info(this.javaClass, "${getId()} sent command '${parsedMessage.command}'")
                    this.client.server.queue.put(SendEntry(this.client, parsedMessage))
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

    private fun parse(message: String): ParsedMessage {
        val input = ANTLRInputStream(message)
        val lexer = IRCGrammarLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = IRCGrammarParser(tokens)
        val tree = parser.message()
        val walker = ParseTreeWalker()
        val listener = IRCMessageListener()
        walker.walk(listener, tree)
        return ParsedMessage(listener.prefix, listener.command!!, listener.params)
    }

}