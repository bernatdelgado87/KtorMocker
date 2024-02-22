package mock.space.data.utils

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.logging.*
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class RemoteClientManagerOkHttp {

    companion object {
        @Volatile
        private var instance: RemoteClientManagerOkHttp? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RemoteClientManagerOkHttp().also { instance = it }
            }
    }
    private var actualHeaders:Headers = Headers.headersOf()

    val client = HttpClient(OkHttp) {

        val trustManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

            override fun getAcceptedIssuers(): Array<X509Certificate>? = arrayOf()
        }

        val sslContext = SSLContext.getInstance("SSL").apply {
            init(null, arrayOf(trustManager), java.security.SecureRandom())
        }


        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 60000
        }

        expectSuccess = false
        engine {
            preconfigured = OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustManager)
                .hostnameVerifier { _, _ -> true }
                .build()

            addNetworkInterceptor(provideHeadersInterceptor())
        }
    }

    fun provideHeadersInterceptor() = Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        var header: Pair<String, String>? = null
        originalRequest.headers.forEach{
            if (it.first.uppercase().equals("HOST")){
                header= Pair(it.first, it.second)
            }
        }

        originalRequest.headers.names().forEach{
            requestBuilder.removeHeader(it)
        }

        requestBuilder.headers(actualHeaders)
        header?.let {
            requestBuilder.addHeader(header!!.first, header!!.second)
        }

        chain.proceed(requestBuilder.build())
    }

    fun putHeaders(stringValues: Map<String, List<String>>) {
        val headersBuilder = Headers.Builder()
        stringValues.forEach { k, v ->
            v.forEach { vv->
                if (!Constants.NOT_ALLOWED_CLIENT_HEADER.contains(k.uppercase())) {
                    System.out.println("Header Received <--" + k + " : " + vv)
                    headersBuilder.add(k, vv)
                }
            }
        }
        actualHeaders = headersBuilder.build()
    }
}