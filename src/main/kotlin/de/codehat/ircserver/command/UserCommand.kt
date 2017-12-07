package de.codehat.ircserver.command

import de.codehat.ircserver.client.ClientState
import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Entry

class UserCommand(server: IRCServer) : Command(server) {

    override fun execute(client: IClient, command: String) {
        if (client.state() == ClientState.CONNECTING) {
            val parameters = command.split(Regex(" +"))
            val username = parameters[1]
            val fullname = parameters.subList(4, parameters.size).joinToString(" ")
            with(client.info()) {
                this.username = username
                this.fullname = fullname
            }
            if (client.info().username != null && client.info().nickname != null) { // Send RPL_WELCOME message
                client.info().state = ClientState.CONNECTED
                val response = Message.RPL_WELCOME.getTemplate()
                        .add("host", this.server.host)
                        .add("nick", client.info().nickname)
                        .add("user", client.info().username)
                        .render()
                client.queue().put(Entry(client, response))
            }
        } else if (client.state() == ClientState.CONNECTED) {
            val response = Message.ERR_ALREADYREGISTRED.getTemplate()
                    .add("nick", client.info().nickname)
                    .render()
            client.queue().put(Entry(client, response))
        }
    }
}