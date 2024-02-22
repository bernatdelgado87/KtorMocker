package mock.space.domain.model

import io.ktor.http.*

data class CustomResponse(
    val body: String,
    val status: HttpStatusCode,
    val headers: Headers,
)