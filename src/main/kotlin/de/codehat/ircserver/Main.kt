package de.codehat.ircserver

import com.beust.jcommander.JCommander
import de.codehat.ircserver.util.Log

fun main(args: Array<String>) {
    val argObject = Args()
    val jCommander = JCommander.newBuilder().addObject(args).build()
    jCommander.programName = "IRCServer"
    args.forEach {
        println(it)
    }
    jCommander.parse(*args)
    if (argObject.help) {
        jCommander.usage()
        return
    }
    Log.info("Starting IRCServer at ${argObject.host}:${argObject.port}")
}
