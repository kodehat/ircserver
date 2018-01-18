package de.codehat.ircserver.command

import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Log

class CommandRegistry(private val server: IRCServer) {

    private val commands = mutableMapOf<String, Command>()

    init {
        this.addCommand("NICK", NickCommand(this.server))
        this.addCommand("USER", UserCommand(this.server))
        this.addCommand("CAP", CapCommand(this.server))
        this.addCommand("PRIVMSG", PrivateMessageCommand(this.server))
        this.addCommand("NOTICE", NoticeCommand(this.server))
        this.addCommand("PING", PingCommand(this.server))
        this.addCommand("MOTD", MotdCommand(this.server))
        this.addCommand("WHOIS", WhoisCommand(this.server))
    }

    fun getCommand(mainCommand: String): Command? {
        return this.commands[mainCommand.toUpperCase()]
    }

    fun commandExists(mainCommand: String) = this.commands.containsKey(mainCommand.toUpperCase())

    fun execute(client: IClient, mainCommand: String, command: String) {
        Log.info(this.javaClass, "{${client.info().id}} executing command '$command'")
        this.getCommand(mainCommand)?.execute(client, command)
    }

    private fun addCommand(mainCommand: String, command: Command) {
        this.commands.put(mainCommand, command)
    }

}