package de.codehat.ircserver.command

import de.codehat.ircserver.util.CommandQueue
import de.codehat.ircserver.util.Log

class CommandWorkerThread(private val queue: CommandQueue, private val commandAction: (cmd: String) -> Unit): Thread() {

    var isRunning = false

    override fun start() {
        this.isRunning = true
        super.start()
    }

    override fun run() {
        var fromQueue: String? = null
        while (isRunning) {
            try {
                fromQueue = this.queue.get()

                this.commandAction(fromQueue)
            } catch (e: InterruptedException) {
                this.isRunning = false
                Log.Companion.info(this.javaClass, "Queue was interrupted")
            }
        }
    }

}