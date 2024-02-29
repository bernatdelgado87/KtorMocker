package mock.space.domain.usecase

import mock.space.data.repository.MockerRepositoryImpl
import mock.space.domain.repository.MockerRepository
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import mock.space.data.utils.Constants.Companion.RULE_TYPE_BODY
import mock.space.data.utils.Constants.Companion.RULE_TYPE_URL
import java.lang.Exception

class SaveRulesUseCase (private val repository: MockerRepository = MockerRepositoryImpl.getInstance()
) : UseCase<Unit, SaveRulesUseCase.Input>() {
    data class Input(val text: String, val type: String)

    override suspend fun run(params: Input): Flow<Unit> {
        when(params.type) {
            RULE_TYPE_BODY -> repository.saveBodyRules(params.text)
            RULE_TYPE_URL -> repository.saveUrlRules(params.text)
            else -> throw Exception("Type sent is not valid. Must be $RULE_TYPE_BODY or $RULE_TYPE_URL")
        }
        return flowOf(Unit)
    }
}