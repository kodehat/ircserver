package de.codehat.ircserver.client

import de.codehat.ircserver.util.CommandQueue

interface IClient {

    /**
     * @throws [ClientAlreadyStartedException]
     */
    fun start()

    /**
     * @throws [ClientAlreadyClosedException]
     */
    fun close()
    fun info(): ClientInfo
    fun state(): ClientState
    fun queue(): CommandQueue

}