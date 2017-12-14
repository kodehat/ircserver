package de.codehat.ircserver.client

import de.codehat.ircserver.util.Log
import java.util.concurrent.atomic.AtomicInteger

object ClientList {

    private val nextId = AtomicInteger() // Initial value is 0, because first ID is 0
    private val clients = mutableMapOf<Int, IClient>()

    fun getAllClients(): List<IClient> = this.clients.values.toList()

    fun addClient(client: IClient) {
        Log.info(this.javaClass, "Adding client with id ${client.info().id} to client list")
        synchronized(clients) {
            this.clients.put(this.nextId.toInt(), client)
        }
        synchronized(nextId) {
            this.nextId.incrementAndGet()
        }
    }

    fun removeClient(id: Int) {
        synchronized(clients) {
            this.clients.remove(id)
        }
    }

    fun getClientById(id: Int) {
        this.clients[id]
    }

    fun getClientByNick(nick: String): IClient? {
        return this.clients.values.find { it.info().nickname == nick }
    }

    fun getNextId() = this.nextId.get()

}