package de.codehat.ircserver.command

import de.codehat.ircserver.antlr4.ParsedMessage
import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Entry
import de.codehat.ircserver.util.ResponseEntry

class PingCommand(server: IRCServer): Command(server) {

    override fun execute(client: IClient, command: ParsedMessage) {
        client.queue().put(ResponseEntry(client, "PONG"))
    }

}