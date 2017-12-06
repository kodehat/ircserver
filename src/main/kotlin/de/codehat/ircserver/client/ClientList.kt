package de.codehat.ircserver.client

import java.util.concurrent.atomic.AtomicInteger

class ClientList {

    private val nextId = AtomicInteger(0) // Initial value is 0, because first ID is 0
    private val clients = mutableListOf<IClient>()

    fun addClient(client: IClient) {
        this.clients.add(this.nextId.get(), client)
        this.nextId.incrementAndGet()
    }

    fun removeClient(id: Int) = this.clients.removeAt(id)

    fun getClientById(id: Int): IClient? = this.clients[id]

    fun getClientByUsername(username: String): IClient? {
        return this.clients.find { it.info().username == username }
    }

    fun getNextId() = this.nextId.get()

}