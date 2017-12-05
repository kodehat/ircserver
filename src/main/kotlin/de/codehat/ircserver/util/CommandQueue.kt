package de.codehat.ircserver.util

import de.codehat.ircserver.command.Command
import java.util.concurrent.LinkedBlockingQueue

class CommandQueue {

    private val queue = LinkedBlockingQueue<Command>()

    fun get() = this.queue.take()
    fun put(command: Command) = this.queue.put(command)
    fun size() = this.queue.size

}