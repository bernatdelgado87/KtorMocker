package com.fitnflow.data.repository

import com.fitnflow.data.utils.Constants
import com.fitnflow.data.utils.LogUtils
import com.fitnflow.data.datasource.LocalDataSource
import com.fitnflow.data.datasource.RemoteDataSource
import com.fitnflow.data.utils.applyRulesIfExists
import com.fitnflow.data.utils.cleanPath
import com.fitnflow.domain.model.CustomResponse
import com.fitnflow.domain.model.ListenModeModel
import com.fitnflow.domain.repository.MockerRepository
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockerRepositoryImpl private constructor() : MockerRepository {
    companion object {
        @Volatile
        private var instance: MockerRepositoryImpl? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: MockerRepositoryImpl().also { instance = it }
            }
    }

    val localDataSource = LocalDataSource()
    val remoteDataSource = RemoteDataSource()

    override suspend fun callToEndpointAndSaveNewResponse(
        urlReceived: String,
        headersReceived: Map<String, List<String>>,
        httpMethod: HttpMethod,
        body: String,
        mockId: Int
    ): Flow<CustomResponse> {
        System.out.println("Url received : " + urlReceived)
        System.out.println("Body received : " + body)

        val response = remoteDataSource.executeSSLRequest(urlReceived, headersReceived, httpMethod, body)
        LogUtils.logHeaders(response.headers)
        System.out.println("<-- Status Response: " + response.status)

        val remoteBody = response.readText()
        val remoteHeaders = response.headers
        val remoteStatus = response.status

        var path: String = urlReceived.cleanPath()
        localDataSource.saveResponse(
            Constants.PREFIX_FOLDER + mockId + path.applyRulesIfExists(body, localDataSource.getBodyRules()),
            remoteBody,
            remoteHeaders,
            remoteStatus
        )
        return flowOf(localDataSource.readResponse(Constants.PREFIX_FOLDER + (mockId.toString()) + path.applyRulesIfExists(body, localDataSource.getBodyRules())))
    }

    override suspend fun getMock(urlReceived: String, mockId: Int): Flow<CustomResponse> {
        return flowOf(localDataSource.readResponse(Constants.PREFIX_FOLDER + mockId + urlReceived.cleanPath()))
    }

    override suspend fun getCurrentListenModel(): ListenModeModel {
        return localDataSource.listenModeModel
    }

    override suspend fun setListenModel(listenModeModel: ListenModeModel) {
        localDataSource.listenModeModel = listenModeModel
    }

    override suspend fun getLastMockId(): Int {
        return localDataSource.getLastFolderNumber()
    }

    override suspend fun saveNote(note: String, id: Int) {
        return localDataSource.saveNote(note, id)
    }

    override suspend fun saveRules(note: String) {
        return localDataSource.saveRules(note)
    }

    override suspend fun getNote(id: Int): String {
        return localDataSource.getNote(id)
    }

    override suspend fun getRules(): List<String> {
        return localDataSource.getBodyRules()
    }

}