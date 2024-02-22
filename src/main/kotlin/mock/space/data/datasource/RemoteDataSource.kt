package mock.space.data.datasource

import mock.space.data.utils.RemoteClientManagerOkHttp
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class RemoteDataSource {

    suspend fun executeSSLRequest(
        endpoint: String,
        headersReceived: Map<String, List<String>>,
        httpMethod: HttpMethod,
        bodyReceived: String?,
    ): HttpResponse {
        val request = HttpRequestBuilder()
        request.apply {
            url.takeFrom("https:/$endpoint")
            method = httpMethod
            contentType()
            bodyReceived?.let {
                body = bodyReceived
            }
        }
        val clientManager = RemoteClientManagerOkHttp.getInstance()
        System.out.println("--> Headers Appending")
        clientManager.putHeaders(headersReceived)
        return clientManager.client.request<HttpStatement>(request).execute()
    }
}