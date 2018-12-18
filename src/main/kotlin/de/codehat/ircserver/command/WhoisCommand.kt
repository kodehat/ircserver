package de.codehat.ircserver.command

import de.codehat.ircserver.antlr4.ParsedMessage
import de.codehat.ircserver.client.ClientList
import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Entry
import de.codehat.ircserver.util.ResponseEntry

class WhoisCommand(server: IRCServer): Command(server) {

    override fun execute(client: IClient, command: ParsedMessage) {
        val parameters = command.params!!
        if (parameters.size < 2) return
        val targetNick = parameters[1]
        val targetClient = ClientList.getClientByNick(targetNick)

        if (targetClient == null) {
            val errNoSuchNick = Message.ERR_NOSUCHNICK.getTemplate()
                    .add("nick", client.info().nickname)
                    .add("unknown_nick", targetNick)
                    .render()
            client.queue().put(ResponseEntry(client, errNoSuchNick))
        } else {
            val rplWhoIs = Message.RPL_WHOISUSER.getTemplate()
                    .add("nick", client.info().nickname)
                    .add("target_nick", targetClient.info().nickname)
                    .add("user", targetClient.info().username)
                    .add("host", targetClient.info().host)
                    .add("real_name", targetClient.info().fullname)
                    .render()
            client.queue().put(ResponseEntry(client, rplWhoIs))

            val rplWhoIsServer = Message.RPL_WHOISSERVER.getTemplate()
                    .add("nick", client.info().nickname)
                    .add("server_name", server.servername)
                    .add("server_info", server.servername)
                    .render()
            client.queue().put(ResponseEntry(client, rplWhoIsServer))

            val rplEndOfWhoIs = Message.RPL_ENDOFWHOIS.getTemplate()
                    .add("nick", client.info().nickname)
                    .render()
            client.queue().put(ResponseEntry(client, rplEndOfWhoIs))
        }
    }
}