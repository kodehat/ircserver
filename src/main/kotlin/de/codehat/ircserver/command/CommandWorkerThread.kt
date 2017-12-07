package de.codehat.ircserver.command

import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.util.CommandQueue
import de.codehat.ircserver.util.Entry
import de.codehat.ircserver.util.Log

class CommandWorkerThread(private val queue: CommandQueue,
                          private val commandAction: (client: IClient, command: String) -> Unit): Thread() {

    var isRunning = false

    override fun start() {
        this.isRunning = true
        super.start()
    }

    override fun run() {
        var fromQueue: Entry?
        while (isRunning) {
            try {
                fromQueue = this.queue.get()

                this.commandAction(fromQueue.client, fromQueue.command)
            } catch (e: InterruptedException) {
                this.isRunning = false
                Log.Companion.info(this.javaClass, "Queue was interrupted")
            }
        }
    }

}