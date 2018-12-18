package de.codehat.ircserver.command

import de.codehat.ircserver.antlr4.ParsedMessage
import de.codehat.ircserver.client.ClientList
import de.codehat.ircserver.client.ClientState
import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Entry
import de.codehat.ircserver.util.ResponseEntry

class PrivateMessageCommand(server: IRCServer): Command(server) {

    override fun execute(client: IClient, command: ParsedMessage) {
        if (client.state() != ClientState.CONNECTED) {
            return
        }

        val parameters = command.params!!

        // nick parameter is missing
        if (parameters.size < 2) {
            val rplNoRecipient = Message.ERR_NORECIPIENT.getTemplate()
                    .add("nick", client.info().nickname)
                    .render()
            client.queue().put(ResponseEntry(client, rplNoRecipient))
            return
        }

        // message is missing
        if (parameters.size < 3) {
            val rplNoTextToSend = Message.ERR_NOTEXTTOSEND.getTemplate()
                    .add("nick", client.info().nickname)
                    .render()
            client.queue().put(ResponseEntry(client, rplNoTextToSend))
            return
        }

        val targetClientNick = parameters[1]
        val messageParts = parameters.slice(2..(parameters.size - 1))
        val message = messageParts.joinToString(" ").substring(1) // substring to remove colon

        val targetClient = ClientList.getClientByNick(targetClientNick)
        if (targetClient == null || targetClient.state() != ClientState.CONNECTED) {
            val rplNoSuchNick = Message.ERR_NOSUCHNICK.getTemplate()
                    .add("nick", client.info().nickname)
                    .add("unknown_nick", targetClientNick)
                    .render()
            client.queue().put(ResponseEntry(client, rplNoSuchNick))
            return
        }

        val nonMessage = Message.NON_MESSAGE.getTemplate()
                .add("nick", client.info().nickname)
                .add("user", client.info().username)
                .add("host", this.server.host)
                .add("command", "PRIVMSG")
                .add("target", targetClient.info().nickname)
                .add("message", message)
                .render()
        targetClient.queue().put(ResponseEntry(client, nonMessage))
    }

}