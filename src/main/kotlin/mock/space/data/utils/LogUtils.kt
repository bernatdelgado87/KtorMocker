package mock.space.data.utils

import io.ktor.http.*

class LogUtils {
    companion object {
        fun logHeaders(headers: Headers) {
            System.out.println("<-- Received Header List from request: " + headers.forEach
            { name, list ->
                System.out.println(
                    name + ": " + headers.get(
                        name
                    )
                )
            })
            System.out.println("<-- END Received Header List as client: ")
        }
    }
}