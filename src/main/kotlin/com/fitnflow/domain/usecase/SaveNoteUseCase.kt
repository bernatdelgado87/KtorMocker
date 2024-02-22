package com.fitnflow.domain.usecase

import com.fitnflow.data.repository.MockerRepositoryImpl
import com.fitnflow.domain.repository.MockerRepository
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SaveNoteUseCase (private val repository: MockerRepository = MockerRepositoryImpl.getInstance()
) : UseCase<Unit, SaveNoteUseCase.Input>() {
    data class Input(val id:Int, val text: String)

    override suspend fun run(params: Input): Flow<Unit> {
        repository.saveNote(params.text, params.id)
        return flowOf(Unit)
    }
}