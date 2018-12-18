package de.codehat.ircserver.antlr4

data class ParsedMessage(val prefix: String?,
                         val command: String,
                         val params: List<String>?)