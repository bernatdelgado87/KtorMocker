package com.fitnflow.data.model

enum class ResponseType(val sufix: String) {
    HEADER(".header"),
    BODY(".body"),
    STATUS(".status")
}