package com.xml.yandextodo.presentation.add.view_model

import com.xml.yandextodo.domain.model.Importance
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.presentation.base.CoreEvent
import com.xml.yandextodo.presentation.base.CoreState
import java.util.Date

class TaskDetailContract {

    sealed class TaskDetailEvent : CoreEvent {
        data class OnLoad(val id: String) : TaskDetailEvent()
        data class OnSave(val tasItemUiModel: TodoItemUiModel) : TaskDetailEvent()
        data class OnTitleValueChanged(val text: String): TaskDetailEvent()
        data class OnImportanceToggleChanged(val importance: Importance): TaskDetailEvent()
        data class OnDeadlineChanged(val deadline: Date?): TaskDetailEvent()
        data class DeleteTask(val id: String): TaskDetailEvent()
    }

    data class State(
        val loading: Boolean,
        val taskItem: TodoItemUiModel,
        val error: String?,
    ) : CoreState

}