package de.codehat.ircserver.command

import org.stringtemplate.v4.ST

enum class Message(val code: Int,
                   private val template: String) {
    RPL_WELCOME(1, "Welcome to the Internet Relay Network <nick>!<user>@<host>"),
    ERR_UNKNOWNCOMMAND(421, "<command> :Unknown command"),
    ERR_NICKNAMEINUSE(433, "<nick> :Nickname is already in use"),
    ERR_RESTRICTED(484, ":Your connection is restricted!");

    fun getRaw() = this.template

    fun getTemplate() = ST(this.template)
}