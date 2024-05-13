package mock.space.domain.usecase

import mock.space.data.repository.MockerRepositoryImpl
import mock.space.domain.model.ListenModeModel
import mock.space.domain.repository.MockerRepository
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SetListenModeUseCase (private val repository: MockerRepository = MockerRepositoryImpl.getInstance()
) : UseCase<Unit, SetListenModeUseCase.Input>() {
    data class Input(val ip: String, val listenFromRemote: Boolean, val mockkId: Int? = null)

    override suspend fun run(params: Input): Flow<Unit> {
        var listenModeModel: ListenModeModel
        if (params.listenFromRemote && params.mockkId == null){
            listenModeModel = ListenModeModel(params.listenFromRemote, repository.getLastMockId() + 1)
        } else {
            listenModeModel = ListenModeModel(params.listenFromRemote, params.mockkId?.let { it }?: repository.getLastMockId() )
        }
        repository.setListenModel(params.ip, listenModeModel)
        return flowOf(Unit)
    }
}