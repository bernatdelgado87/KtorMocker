package mock.space.domain.usecase

import mock.space.data.utils.applyRulesIfExists
import mock.space.data.repository.MockerRepositoryImpl
import mock.space.domain.model.CustomResponse
import mock.space.domain.repository.MockerRepository
import es.experis.app.domain.usecase.UseCase
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.flow.Flow
import mock.space.domain.model.ListenModeModel

class ReadOrWriteMockUseCase (private val repository: MockerRepository = MockerRepositoryImpl.getInstance()
) : UseCase<CustomResponse, ReadOrWriteMockUseCase.Input>() {
    data class Input(val ip: String, val urlReceived: String, val headersReceived: Headers? = null, val httpMethod: HttpMethod? = null, val body: String? = null)

    override suspend fun run(params: Input): Flow<CustomResponse> {
        var response: Flow<CustomResponse>

        if (repository.getCurrentListenModel(params.ip).mockId == null){
            repository.setListenModel(params.ip, ListenModeModel(true, mockId = repository.getLastMockId() + 1))
        }
        if (repository.getCurrentListenModel(params.ip).isCallToRemoteActivated){
            response = repository.callToEndpointAndSaveNewResponse(params.urlReceived, params.headersReceived!!.toMap(), params.httpMethod!!, params.body!!, repository.getCurrentListenModel(params.ip).mockId!!)
        } else {
            response = repository.getMock(params.urlReceived.applyRulesIfExists(params.body, repository.getBodyRules(), params.urlReceived, repository.getUrlRules()), repository.getCurrentListenModel(params.ip).mockId?.let { it }?: repository.getLastMockId())
        }
        return response
    }
}