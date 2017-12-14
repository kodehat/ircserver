package de.codehat.ircserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.TimeUnit

class Util {

    companion object {
        fun connect(host: String, port: Int): Triple<Socket, BufferedReader, PrintWriter> {
            val socket = Socket(host, port)
            val bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val printWriter = PrintWriter(socket.getOutputStream(), true)
            return Triple(socket, bufferedReader, printWriter)
        }
    }

}