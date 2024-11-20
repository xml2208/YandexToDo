package com.xml.yandextodo.presentation.add.view_model

import com.xml.yandextodo.domain.model.Importance
import com.xml.yandextodo.domain.model.TodoItemUiModel
import java.util.Date
import kotlin.random.Random

sealed class TaskDetailEvent {
    data class OnLoad(val id: String) : TaskDetailEvent()
    data class OnSave(val tasItemUiModel: TodoItemUiModel) : TaskDetailEvent()
    data class OnTitleValueChanged(val text: String) : TaskDetailEvent()
    data class OnImportanceToggleChanged(val importance: Importance) : TaskDetailEvent()
    data class OnDeadlineChanged(val deadline: Date?) : TaskDetailEvent()
    data class DeleteTask(val id: String) : TaskDetailEvent()
    data class OnError(val message: String?) : TaskDetailEvent()
}

sealed class TaskDetailState {

    data object Loading : TaskDetailState()
    data class Content(
        val id: String = Random.nextInt().toString(),
        val taskTitle: String = "",
        val importance: Importance = Importance.BASIC,
        val deadline: Date? = null,
        val isCompleted: Boolean = false,
    ) : TaskDetailState()
    data class Error(val message: String?) : TaskDetailState()
}