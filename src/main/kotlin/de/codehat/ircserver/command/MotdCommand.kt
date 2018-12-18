package de.codehat.ircserver.command

import de.codehat.ircserver.antlr4.ParsedMessage
import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Entry
import de.codehat.ircserver.util.ResponseEntry

class MotdCommand(server: IRCServer): Command(server) {

    override fun execute(client: IClient, command: ParsedMessage) {
        if (server.motdLines == null) {
            val errNoMotd = Message.ERR_NOMOTD.getTemplate()
                    .add("nick", client.info().nickname)
                    .render()
            client.queue().put(ResponseEntry(client, errNoMotd))
        } else {
            val rplMotdStart = Message.RPL_MOTDSTART.getTemplate()
                    .add("nick", client.info().nickname)
                    .add("server_name", server.servername)
                    .render()
            client.queue().put(ResponseEntry(client, rplMotdStart))
            server.motdLines?.forEach {
                val rplMotdLine = Message.RPL_MOTD.getTemplate()
                        .add("nick", client.info().nickname)
                        .add("line", it)
                        .render()
                client.queue().put(ResponseEntry(client, rplMotdLine))
            }
            val rplMotdEnd = Message.RPL_ENDOFMOTD.getTemplate()
                    .add("nick", client.info().nickname)
                    .render()
            client.queue().put(ResponseEntry(client, rplMotdEnd))
        }
    }
}