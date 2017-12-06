package de.codehat.ircserver.util

class Log {

    companion object {

        var DEBUG = false

        fun <T> info(javaClass: Class<T>?, message: String) {
            if (!DEBUG) return
            val className = javaClass?.simpleName ?: "???"
            println("INFO [$className]: $message")
        }

        fun info(className: String, message: String) {
            if (!DEBUG) return
            println("INFO [$className]: $message")
        }
    }

}