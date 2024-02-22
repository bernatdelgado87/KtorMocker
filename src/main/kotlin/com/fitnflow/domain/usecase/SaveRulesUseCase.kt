package com.fitnflow.domain.usecase

import com.fitnflow.data.repository.MockerRepositoryImpl
import com.fitnflow.domain.repository.MockerRepository
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SaveRulesUseCase (private val repository: MockerRepository = MockerRepositoryImpl.getInstance()
) : UseCase<Unit, SaveRulesUseCase.Input>() {
    data class Input(val text: String)

    override suspend fun run(params: Input): Flow<Unit> {
        repository.saveRules(params.text)
        return flowOf(Unit)
    }
}