package com.fitnflow.domain.usecase

import com.fitnflow.data.repository.MockerRepositoryImpl
import com.fitnflow.domain.repository.MockerRepository
import com.fitnflow.presentation.model.IndexUiModel
import com.fitnflow.presentation.model.MockItemUiModel
import es.experis.app.domain.usecase.None
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.File

class GetUiModelUseCase (private val repository: MockerRepository = MockerRepositoryImpl.getInstance()) : UseCase<IndexUiModel, None>() {

    override suspend fun run(params: None): Flow<IndexUiModel> {
        val currentListenModeModel = repository.getCurrentListenModel()
        val items = mutableListOf<MockItemUiModel>()
        val rootFile = File("demo/")
        rootFile.listFiles()
            .filter { it.isDirectory }
            .forEach {
                items.add(MockItemUiModel(it, repository.getNote(it.name.toInt())))
            }
        var rules = ""
        repository.getRules().forEachIndexed{index, rule ->
            if (index > 0){
                "\n"
            }
            rules += rule
        }

        return flowOf(IndexUiModel(items, currentListenModeModel, rules))
    }
}