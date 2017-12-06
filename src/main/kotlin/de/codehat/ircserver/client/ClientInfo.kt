package de.codehat.ircserver.client

data class ClientInfo(val id: Int,
                      val host: String,
                      val port: Int,
                      var username: String? = null,
                      var fullname: String? = null,
                      var nickname: String? = null,
                      var state: ClientState = ClientState.CONNECTING,
                      val connectedSince: Long)