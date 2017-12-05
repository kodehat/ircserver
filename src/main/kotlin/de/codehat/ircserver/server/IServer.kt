package de.codehat.ircserver.server

interface IServer {

    fun start(): Boolean
    fun stop(): Boolean
    fun isRunning(): Boolean

}