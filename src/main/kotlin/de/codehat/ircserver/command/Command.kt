package de.codehat.ircserver.command

import de.codehat.ircserver.server.IRCServer

abstract class Command(val server: IRCServer) {

    abstract fun execute(command: String)

}