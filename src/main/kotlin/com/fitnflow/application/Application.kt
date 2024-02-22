package com.fitnflow.application

import com.fitnflow.application.plugins.configureInOutContent
import com.fitnflow.application.plugins.configureRouting
import com.fitnflow.application.plugins.configureTemplating
import io.ktor.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    configureRouting()
    configureTemplating()
    configureInOutContent()
}


