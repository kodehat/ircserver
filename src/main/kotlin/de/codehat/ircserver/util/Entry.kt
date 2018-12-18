package de.codehat.ircserver.util

import de.codehat.ircserver.antlr4.ParsedMessage
import de.codehat.ircserver.client.IClient

open class Entry(client: IClient)

class SendEntry(val client: IClient, val command: ParsedMessage): Entry(client)
class ResponseEntry(val client: IClient, val response: String): Entry(client)