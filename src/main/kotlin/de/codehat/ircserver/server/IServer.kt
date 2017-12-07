package de.codehat.ircserver.server

interface IServer {

    /**
     * @throws [ServerAlreadyStartedException]
     */
    fun start()

    /**
     * @throws [ServerAlreadyStoppedException]
     */
    fun stop()
    fun isRunning(): Boolean

}