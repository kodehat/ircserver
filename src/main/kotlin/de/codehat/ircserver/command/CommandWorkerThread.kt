package de.codehat.ircserver.command

import de.codehat.ircserver.client.IClient
import de.codehat.ircserver.util.*

class CommandWorkerThread(private val queue: CommandQueue,
                          private val commandAction: (client: IClient, command: Any) -> Unit): Thread() {

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

                if (fromQueue is SendEntry) {
                    this.commandAction(fromQueue.client, fromQueue.command)
                } else if (fromQueue is ResponseEntry) {
                    this.commandAction(fromQueue.client, fromQueue.response)
                }
            } catch (e: InterruptedException) {
                this.isRunning = false
                Log.info(this.javaClass, "Queue was interrupted")
            }
        }
    }

}