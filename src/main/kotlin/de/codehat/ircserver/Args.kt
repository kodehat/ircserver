package de.codehat.ircserver

import com.beust.jcommander.Parameter

class Args {

    @Parameter(names = ["-i", "--ip"], description = "The ip/hostname for the server")
    var host: String = "localhost"

    @Parameter(names = ["-p", "--port"], description = "The port the server listens on")
    var port: Int = 6667

    @Parameter(names = ["-h", "--help"], description = "Shows the help page", help = true)
    var help: Boolean = false

}