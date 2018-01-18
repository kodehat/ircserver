package de.codehat.ircserver.command

import org.stringtemplate.v4.ST

enum class Message(val code: Int,
                   private val template: String) {
    NON_MESSAGE(-1, ":<nick>!<user>@<host> <command> <target> :<message>"),
    RPL_WELCOME(1, ":<host> 001 <nick> :Welcome to the Internet Relay Network <nick>!<user>@<host>"),
    RPL_YOURHOST(2, ":<host> 002 <nick> :Your host is <server_name>, running version <version>"),
    RPL_CREATED(3, ":<host> 003 <nick> :This server was created <date>"),
    RPL_MYINFO(4, ":<host> 004 <nick> <server_name> <version> <user_modes> <chan_modes>"),
    RPL_WHOISUSER(311, "311 <nick> <target_nick> <user> <host> * :<real_name>"),
    RPL_WHOISSERVER(312, "312 <nick> <server_name> :<server_info>"),
    RPL_ENDOFWHOIS(318, "318 <nick> :End of WHOIS list"),
    RPL_MOTD(372, "372 <nick> :- <line>"),
    RPL_MOTDSTART(375, "375 <nick> :- <server_name> Message of the day -"),
    RPL_ENDOFMOTD(376, "376 <nick> :End of /MOTD command"),
    ERR_NOSUCHNICK(401, "401 <nick> <unknown_nick> :No such nick"),
    ERR_NORECIPIENT(411, "411 <nick> :No recipient given"),
    ERR_NOTEXTTOSEND(412, "412 <nick> :No text to send"),
    ERR_UNKNOWNCOMMAND(421, "421 <nick> <command> :Unknown command"),
    ERR_NOMOTD(422, "422 <nick> :MOTD File is missing"),
    ERR_NICKNAMEINUSE(433, ":<host> 433 * <nick> :Nickname is already in use"),
    ERR_ALREADYREGISTRED(462, "462 * <nick> :Unauthorized command (already registered)"),
    ERR_RESTRICTED(484, "484 * <nick> :Your connection is restricted");

    fun getRaw() = this.template

    fun getTemplate() = ST(this.template)
}