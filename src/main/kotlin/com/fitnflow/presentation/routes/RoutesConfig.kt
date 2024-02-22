package com.fitnflow.presentation.routes

import com.fitnflow.domain.usecase.GetUiModelUseCase
import com.fitnflow.domain.usecase.SaveNoteUseCase
import com.fitnflow.domain.usecase.SaveRulesUseCase
import com.fitnflow.domain.usecase.SetListenModeUseCase
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.mustache.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect
import java.io.File

fun Route.configureRoutes() {
    route("/css") {
        get("/") {
            call.respond(MustacheContent("global.css", null))
        }
    }
    route("/") {
        get("/") {
            val usecase = GetUiModelUseCase()
            usecase().collect {
                call.respond(MustacheContent("index.hbs", mapOf("indexUiModel" to it)))
            }
        }
        get("/listen") {
            val params = call.parameters
            val id = params["id"]

            val usecase = SetListenModeUseCase()
            usecase(
                SetListenModeUseCase.Input(
                    true,
                    id?.toInt()
                )
            ).collect {
                call.respond(HttpStatusCode.OK)
            }
        }
        post("/saveNote"){
            val data = call.receive<Map<String, String>>()
            val id = data["id"]
            val text = data["text"]
            System.out.println("Received text:" + text + " for id: " + id)

            val usecase = SaveNoteUseCase()
            usecase(
                SaveNoteUseCase.Input(
                    id!!.toInt(), text!!
                )
            ).collect {
                call.respond(HttpStatusCode.OK)
            }
        }

        post("/saveRules"){
            val data = call.receive<Map<String, String>>()
            val text = data["text"]
            System.out.println("Received rules:" + text)

            val usecase = SaveRulesUseCase()
            usecase(
                SaveRulesUseCase.Input(
                    text!!
                )
            ).collect {
                call.respond(HttpStatusCode.OK)
            }
        }

        get("/localMode") {
            val params = call.parameters
            val id = params["id"]
            val usecase = SetListenModeUseCase()
            usecase(
                SetListenModeUseCase.Input(
                    false,
                    id!!.toInt()
                )
            ).collect {
                call.respond(HttpStatusCode.OK)
            }
        }

        post("/delete") {
            val data = call.receive<Map<String, String>>()
            val file = data["file"]
            System.out.println(file)
            if (File(file).deleteRecursively()) {
                System.out.println("--- Deleted! ----")
            } else {
                System.out.println("--- Cannot Delete ----")
            }
            //return all categories
            call.respondRedirect("/")
        }

    }
}
