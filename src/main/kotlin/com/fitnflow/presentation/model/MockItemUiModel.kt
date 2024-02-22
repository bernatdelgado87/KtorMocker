package com.fitnflow.presentation.model

import com.fitnflow.domain.model.ListenModeModel
import java.io.File

data class IndexUiModel(
    val items: List<MockItemUiModel>,
    val listenMode: ListenModeModel,
    val rules: String
    )

data class MockItemUiModel(
    val folder: File,
    val notes: String
    )