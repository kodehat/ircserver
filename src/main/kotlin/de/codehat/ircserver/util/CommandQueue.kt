package de.codehat.ircserver.util

import java.util.concurrent.LinkedBlockingQueue

class CommandQueue {

    private val queue = LinkedBlockingQueue<Entry>()

    fun get(): Entry = this.queue.take()
    fun put(entry: Entry) = this.queue.put(entry)

}