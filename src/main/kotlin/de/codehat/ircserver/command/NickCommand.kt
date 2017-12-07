package de.codehat.ircserver.command

import de.codehat.ircserver.client.ClientList
import de.codehat.ircserver.client.ClientState
import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Entry

class NickCommand(server: IRCServer): Command(server) {

    override fun execute(client: IClient, command: String) {
        if (client.state() == ClientState.CONNECTING) {
            val parameter = command.split(Regex(" +"))
            val nick = parameter[parameter.size - 1]
            if (ClientList.getClientByNick(nick) != null) { // Nick already in use
                val response = Message.ERR_NICKNAMEINUSE.getTemplate()
                        .add("host", this.server.host)
                        .add("nick", nick)
                        .render()
                client.queue().put(Entry(client, response))
            } else { // Nick still can be used
                client.info().nickname = nick
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

        }
    }

}