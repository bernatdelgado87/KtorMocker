package com.fitnflow.domain.commons.executor

import es.experis.app.domain.executor.BackgroundCoroutine
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class JobCoroutine : BackgroundCoroutine {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}
