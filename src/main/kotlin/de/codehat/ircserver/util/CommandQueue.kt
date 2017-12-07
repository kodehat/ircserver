package de.codehat.ircserver.util

import java.util.concurrent.LinkedBlockingQueue

class CommandQueue {

    private val queue = LinkedBlockingQueue<String>()

    fun get(): String = this.queue.take()
    fun put(command: String) = this.queue.put(command)
    fun size() = this.queue.size

}