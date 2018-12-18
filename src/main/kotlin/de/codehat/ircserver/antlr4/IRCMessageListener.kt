package de.codehat.ircserver.antlr4

class IRCMessageListener: IRCGrammarBaseListener() {
    var prefix: String? = null
    var command: String? = null
    var params: List<String>? = null

    override fun exitPrefix(ctx: IRCGrammarParser.PrefixContext) {
        prefix = ctx.PREFIX().text
    }

    override fun exitCommand(ctx: IRCGrammarParser.CommandContext) {
        command = ctx.COMMAND().text
    }

    override fun exitParams(ctx: IRCGrammarParser.ParamsContext) {
        params = ctx.PARAMS().text.split(Regex(" +"))
    }
}