package de.codehat.ircserver.command

import de.codehat.ircserver.antlr4.ParsedMessage
import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer

class CapCommand(server: IRCServer): Command(server) {

    override fun execute(client: IClient, command: ParsedMessage) {
        // Do nothing yet!
    }

}