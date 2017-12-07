package de.codehat.ircserver.client

import de.codehat.ircserver.util.Log
import java.util.concurrent.atomic.AtomicInteger

object ClientList {

    private val nextId = AtomicInteger() // Initial value is 0, because first ID is 0
    private val clients = mutableMapOf<Int, IClient>()
    private val cachedClients = mutableMapOf<String, IClient>()

    fun addClient(client: IClient) {
        Log.info(this.javaClass, "Adding client with id ${client.info().id} to client list")
        this.clients.put(this.nextId.toInt(), client)
        this.nextId.incrementAndGet()
    }

    fun removeClient(id: Int) {
        this.cachedClients.remove(this.getClientById(id)?.info()?.username)
        this.clients.remove(id)
    }

    fun getClientById(id: Int): IClient? = this.clients[id]

    fun getClientByUsername(username: String): IClient? {
        return if (this.cachedClients.containsKey(username)) {
            this.cachedClients[username]
        } else {
            val client = this.clients.values.find { it.info().username == username }
            if (client != null) {
                this.cachedClients.put(username, client)
            }
            client
        }
    }

    fun getClientByNick(nick: String): IClient? {
        return this.clients.values.find { it.info().nickname == nick }
    }

    fun getNextId() = this.nextId.get()

}