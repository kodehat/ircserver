package de.codehat.ircserver

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import de.codehat.ircserver.server.IRCServer
import de.codehat.ircserver.util.Log

fun main(args: Array<String>) {
    val argObject = Args()
    val jCommander = JCommander.newBuilder().addObject(argObject).build()
    jCommander.programName = "IRCServer"
    try {
        jCommander.parse(*args)
    } catch (e: ParameterException) {
        println("Unknown or missing parameter")
        jCommander.usage()
        return
    }
    if (argObject.help) {
        jCommander.usage()
        return
    }
    if (argObject.debug) Log.DEBUG = true
    Log.info("Main", "Starting IRCServer at ${argObject.host}:${argObject.port}")
    IRCServer(argObject.host, argObject.port, argObject.maxClients).apply {
        start()
        serverThread.join()
    }
}
