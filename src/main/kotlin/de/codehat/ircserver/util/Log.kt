package de.codehat.ircserver.util

class Log {

    companion object {

        var DEBUG = true

        fun <T> info(javaClass: Class<T>?, message: String) {
            if (!DEBUG) return
            val className = javaClass?.name ?: "???"
            println("INFO [$className]: $message")
        }

        fun info(message: String) {
            if (!DEBUG) return
            println("INFO [???]: $message")
        }
    }

}