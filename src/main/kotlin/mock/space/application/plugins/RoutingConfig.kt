package mock.space.application.plugins

import mock.space.presentation.routes.configureRoutes
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        configureRoutes()
    }
}
