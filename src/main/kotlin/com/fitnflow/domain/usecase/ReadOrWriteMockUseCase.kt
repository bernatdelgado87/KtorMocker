package com.fitnflow.domain.usecase

import com.fitnflow.data.utils.applyRulesIfExists
import com.fitnflow.data.repository.MockerRepositoryImpl
import com.fitnflow.domain.model.CustomResponse
import com.fitnflow.domain.repository.MockerRepository
import es.experis.app.domain.usecase.UseCase
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.flow.Flow

class ReadOrWriteMockUseCase (private val repository: MockerRepository = MockerRepositoryImpl.getInstance()
) : UseCase<CustomResponse, ReadOrWriteMockUseCase.Input>() {
    data class Input(val urlReceived: String, val headersReceived: Headers? = null, val httpMethod: HttpMethod? = null, val body: String? = null)

    override suspend fun run(params: Input): Flow<CustomResponse> {
        var response: Flow<CustomResponse>

        val currentListenModeModel = repository.getCurrentListenModel()
        if (currentListenModeModel.isCallToRemoteActivated){
            response = repository.callToEndpointAndSaveNewResponse(params.urlReceived, params.headersReceived!!.toMap(), params.httpMethod!!, params.body!!, currentListenModeModel.mockId!!)
        } else {
            response = repository.getMock(params.urlReceived.applyRulesIfExists(params.body, repository.getRules()), currentListenModeModel.mockId?.let { it }?: repository.getLastMockId())
        }
        return response
    }
}