package mock.space.domain.usecase

import mock.space.data.repository.MockerRepositoryImpl
import mock.space.domain.repository.MockerRepository
import mock.space.presentation.model.IndexUiModel
import mock.space.presentation.model.MockItemUiModel
import es.experis.app.domain.usecase.None
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import mock.space.presentation.model.Rules
import java.io.File

class GetUiModelUseCase (private val repository: MockerRepository = MockerRepositoryImpl.getInstance()) : UseCase<IndexUiModel, String>() {

    override suspend fun run(params: String): Flow<IndexUiModel> {
        val currentListenModeModel = repository.getCurrentListenModel(params)
        val items = mutableListOf<MockItemUiModel>()
        val rootFile = File("demo/")
        rootFile.listFiles()
            .filter { it.isDirectory }
            .forEach {
                items.add(MockItemUiModel(it, repository.getNote(it.name.toInt())))
            }
        var rulesBody = ""
        repository.getBodyRules().forEachIndexed{ index, rule ->
            if (index > 0){
                "\n"
            }
            rulesBody += rule
        }

        var rulesUrl = ""
        repository.getUrlRules().forEachIndexed{ index, rule ->
            if (index > 0){
                "\n"
            }
            rulesUrl += rule
        }

        return flowOf(IndexUiModel(items, currentListenModeModel, Rules(rulesBody, rulesUrl)))
    }
}