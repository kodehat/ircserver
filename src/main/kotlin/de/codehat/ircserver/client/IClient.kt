package de.codehat.ircserver.client

import de.codehat.ircserver.util.CommandQueue

interface IClient {

    fun start(): Boolean
    fun info(): ClientInfo
    fun close(): Boolean
    fun state(): ClientState
    fun queue(): CommandQueue

}