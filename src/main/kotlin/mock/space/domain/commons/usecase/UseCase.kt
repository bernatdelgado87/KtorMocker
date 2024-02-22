package es.experis.app.domain.usecase

import mock.space.domain.commons.executor.JobCoroutine
import es.experis.app.domain.executor.BackgroundCoroutine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class UseCase<out T, in Params : Any>(private val backgroundCoroutine: BackgroundCoroutine = JobCoroutine()) {

    abstract suspend fun run(params: Params): Flow<T>

    operator suspend fun invoke(params: Params = None as Params) = run(params).flowOn(backgroundCoroutine.coroutineContext)

}

object None
