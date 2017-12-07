package de.codehat.ircserver.command

import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer

class CommandRegistry(private val server: IRCServer) {

    private val commands = mutableMapOf<String, Command>()

    init {
        this.addCommand("NICK", NickCommand(this.server))
    }

    fun getCommand(mainCommand: String): Command? {
        return this.commands[mainCommand.toUpperCase()]
    }

    fun commandExists(mainCommand: String) = this.commands.containsKey(mainCommand.toUpperCase())

    fun execute(mainCommand: String, command: String) {
        this.getCommand(command)?.execute(command)
    }

    private fun addCommand(mainCommand: String, command: Command) {
        this.commands.put(mainCommand, command)
    }

}