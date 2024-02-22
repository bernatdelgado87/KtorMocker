package mock.space.application.plugins

import mock.space.domain.usecase.ReadOrWriteMockUseCase
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.flow.collect

fun Application.configureInOutContent() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }
    install(DefaultHeaders)
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { status ->
                System.out.println("<-- Url Received")
                if (call.request.uri.contains("key")){
                    System.out.println("<-- sankey")

                }
                System.out.println(call.request.uri)
                val usecase = ReadOrWriteMockUseCase()
                usecase(
                    ReadOrWriteMockUseCase.Input(
                        call.request.uri,
                        call.request.headers,
                        call.request.httpMethod,
                        call.receiveText()
                    )
                ).collect {
                    call.respond(it.status, it.body)
                }
        }
        exception<Throwable> {
            when (it) {
                else -> call.respond(HttpStatusCode.InternalServerError, "Fatal Error: " + "${it.message}")
            }
            throw it
        }
    }
}