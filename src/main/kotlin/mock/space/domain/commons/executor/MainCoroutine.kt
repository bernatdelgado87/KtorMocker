package mock.space.domain.commons.executor

import es.experis.app.domain.executor.BackgroundCoroutine
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class MainCoroutine : BackgroundCoroutine {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}
