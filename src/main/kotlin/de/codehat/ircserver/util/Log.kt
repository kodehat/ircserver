package de.codehat.ircserver.util

class Log {

    companion object {
        fun <T> info(javaClass: Class<T>?, message: String) {
            val className = javaClass?.name ?: "???"
            println("INFO [$className]: $message")
        }

        fun info(message: String) {
            println("INFO [???]: $message")
        }
    }

}