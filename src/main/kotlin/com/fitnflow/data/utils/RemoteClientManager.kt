package com.fitnflow.data.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class RemoteClientManager {

    companion object {
        @Volatile
        private var instance: RemoteClientManager? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RemoteClientManager().also { instance = it }
            }
    }
    private var actualHeaders = StringValues.Empty

    val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(DefaultRequest){
            headers.appendAll(actualHeaders)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 60000
        }

        expectSuccess = false
        engine {
            https {
                //Work also with no trusted certificates
                trustManager = object : X509TrustManager {
                    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

                    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

                    override fun getAcceptedIssuers(): Array<X509Certificate>? = null
                }
            }
        }
    }

    fun putHeaders(stringValues: StringValues) {
        actualHeaders = ParametersBuilder().apply {
            appendAll(stringValues)
        }.build()
    }
}