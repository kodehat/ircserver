package de.codehat.ircserver.antlr4

data class ParsedMessage(val prefix: String? = null,
                         val command: String? = null,
                         val params: List<String>? = null,
                         val response: String? = null)