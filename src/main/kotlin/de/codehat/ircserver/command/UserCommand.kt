package de.codehat.ircserver.command

import de.codehat.ircserver.antlr4.ParsedMessage
import de.codehat.ircserver.client.ClientState
import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Entry
import de.codehat.ircserver.util.ResponseEntry

class UserCommand(server: IRCServer) : Command(server) {

    override fun execute(client: IClient, command: ParsedMessage) {
        if (client.state() == ClientState.CONNECTING) {
            val parameters = command.params!!
            val username = parameters[1]
            val fullname = parameters.subList(4, parameters.size).joinToString(" ").substring(1)
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
                client.queue().put(ResponseEntry(client, response))
                val rplYourHost = Message.RPL_YOURHOST.getTemplate()
                        .add("host", this.server.host)
                        .add("nick", client.info().nickname)
                        .add("server_name", this.server.servername)
                        .add("version", this.server.version)
                        .render()
                client.queue().put(ResponseEntry(client, rplYourHost))
                val rplCreated = Message.RPL_CREATED.getTemplate()
                        .add("host", this.server.host)
                        .add("nick", client.info().nickname)
                        .add("date", this.server.date.toString())
                        .render()
                client.queue().put(ResponseEntry(client, rplCreated))
                val rplMyInfo = Message.RPL_MYINFO.getTemplate()
                        .add("host", this.server.host)
                        .add("nick", client.info().nickname)
                        .add("server_name", this.server.servername)
                        .add("version", this.server.version)
                        .add("user_modes", "ao")
                        .add("chan_modes", "mtov")
                        .render()
                client.queue().put(ResponseEntry(client, rplMyInfo))
            }
        } else if (client.state() == ClientState.CONNECTED) {
            val response = Message.ERR_ALREADYREGISTRED.getTemplate()
                    .add("nick", client.info().nickname)
                    .render()
            client.queue().put(ResponseEntry(client, response))
        }
    }
}