package de.codehat.ircserver.command

import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer

abstract class Command(val server: IRCServer) {

    abstract fun execute(client: IClient, command: String)

}