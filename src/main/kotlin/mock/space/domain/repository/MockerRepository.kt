package mock.space.domain.repository

import mock.space.domain.model.CustomResponse
import mock.space.domain.model.ListenModeModel
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow

interface MockerRepository {
    suspend fun callToEndpointAndSaveNewResponse(urlReceived: String, headersReceived: Map<String, List<String>>, httpMethod: HttpMethod, body: String, mockId: Int): Flow<CustomResponse>
    suspend fun getMock(urlReceived: String, mockId: Int): Flow<CustomResponse>
    suspend fun getCurrentListenModel(): ListenModeModel
    suspend fun setListenModel(listenModeModel: ListenModeModel)
    suspend fun getLastMockId(): Int
    suspend fun saveNote(note: String, id: Int)
    suspend fun saveRules(note: String)
    suspend fun getNote(id: Int): String
    suspend fun getRules(): List<String>
}