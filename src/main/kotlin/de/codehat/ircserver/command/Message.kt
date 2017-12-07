package de.codehat.ircserver.command

import org.stringtemplate.v4.ST

enum class Message(val code: Int,
                   private val template: String) {
    RPL_WELCOME(1, ":<host> 001 <nick> :Welcome to the Internet Relay Network <nick>!<user>@<host>"),
    ERR_UNKNOWNCOMMAND(421, "421 * <nick> <command> :Unknown command"),
    ERR_NICKNAMEINUSE(433, ":<host> 433 * <nick> :Nickname is already in use"),
    ERR_ALREADYREGISTRED(462, "462 * <nick> :Unauthorized command (already registered)"),
    ERR_RESTRICTED(484, "484 * <nick> :Your connection is restricted!");

    fun getRaw() = this.template

    fun getTemplate() = ST(this.template)
}