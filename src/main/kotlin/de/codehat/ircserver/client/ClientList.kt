package de.codehat.ircserver.client

import de.codehat.ircserver.util.Log
import java.util.concurrent.atomic.AtomicInteger

object ClientList {

    private val nextId = AtomicInteger(0) // Initial value is 0, because first ID is 0
    private val clients = mutableListOf<IClient>()
    private val cachedClients = mutableMapOf<String, IClient>()

    fun addClient(client: IClient) {
        Log.Companion.info(this.javaClass, "Adding client with id ${client.info().id} to client list")
        this.clients.add(this.nextId.get(), client)
        this.nextId.incrementAndGet()
    }

    fun removeClient(id: Int) {
        this.cachedClients.remove(this.getClientById(id)?.info()?.username)
        this.clients.removeAt(id)
    }

    fun getClientById(id: Int): IClient? = this.clients[id]

    fun getClientByUsername(username: String): IClient? {
        return if (this.cachedClients.containsKey(username)) {
            this.cachedClients[username]
        } else {
            val client = this.clients.find { it.info().username == username }
            if (client != null) {
                this.cachedClients.put(username, client)
            }
            client
        }
    }

    fun getNextId() = this.nextId.get()

}