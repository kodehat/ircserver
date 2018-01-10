package de.codehat.ircserver.command

import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Entry

class PingCommand(server: IRCServer): Command(server) {

    override fun execute(client: IClient, command: String) {
        client.queue().put(Entry(client, "PONG"))
    }

}