package mock.space.presentation.model

import mock.space.domain.model.ListenModeModel
import java.io.File

data class IndexUiModel(
    val items: List<MockItemUiModel>,
    val listenMode: ListenModeModel,
    val rules: Rules
    )

data class MockItemUiModel(
    val folder: File,
    val notes: String
    )

data class Rules(
    val body: String,
    val url: String
)