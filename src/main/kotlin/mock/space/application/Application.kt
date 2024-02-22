package mock.space.application

import mock.space.application.plugins.configureInOutContent
import mock.space.application.plugins.configureRouting
import mock.space.application.plugins.configureTemplating
import io.ktor.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    configureRouting()
    configureTemplating()
    configureInOutContent()
}


