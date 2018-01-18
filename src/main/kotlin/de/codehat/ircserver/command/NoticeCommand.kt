package de.codehat.ircserver.command

import de.codehat.ircserver.client.ClientList
import de.codehat.ircserver.client.ClientState
import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Entry

class NoticeCommand(server: IRCServer): Command(server) {

    override fun execute(client: IClient, command: String) {
        if (client.state() != ClientState.CONNECTED) {
            return
        }

        val parameters = command.split(Regex(" +"))

        // nick parameter is missing
        if (parameters.size < 3) {
            return
        }

        val targetClientNick = parameters[1]
        val messageParts = parameters.slice(2..(parameters.size - 1))
        val message = messageParts.joinToString(" ").substring(1) // substring to remove colon

        val targetClient = ClientList.getClientByNick(targetClientNick)
        if (targetClient == null || targetClient.state() != ClientState.CONNECTED) {
            return
        }

        val nonMessage = Message.NON_MESSAGE.getTemplate()
                .add("nick", client.info().nickname)
                .add("user", client.info().username)
                .add("host", this.server.host)
                .add("command", "NOTICE")
                .add("target", targetClient.info().nickname)
                .add("message", message)
                .render()
        targetClient.queue().put(Entry(client, nonMessage))
    }

}