package com.fitnflow.application.plugins

import com.fitnflow.presentation.routes.configureRoutes
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        configureRoutes()
    }
}
