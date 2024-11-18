package com.xml.yandextodo.presentation.list.view_model

import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.presentation.base.CoreEvent
import com.xml.yandextodo.presentation.base.CoreState

class TaskListContract  {

    sealed class TaskListEvent : CoreEvent {

        data object RefreshTodos : TaskListEvent()
        data class GetTask(val id: String?) : TaskListEvent()
        data class OnCheckedChange(val todo: TodoItemUiModel) : TaskListEvent()

    }

    data class State(
        val loading: Boolean,
        val taskList: List<TodoItemUiModel>,
        val error: String?,
    ): CoreState
}