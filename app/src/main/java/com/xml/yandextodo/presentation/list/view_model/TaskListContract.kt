package com.xml.yandextodo.presentation.list.view_model

import com.xml.yandextodo.domain.model.TodoItemUiModel

sealed class TaskListEvent {
    data object RefreshTodos : TaskListEvent()
    data class GetTask(val id: String?) : TaskListEvent()
    data class OnCheckedChange(val todo: TodoItemUiModel) : TaskListEvent()
}

sealed class TaskListState {

    data object Loading : TaskListState()

    data class Content(val taskList: List<TodoItemUiModel>) : TaskListState()

    data class Error(val error: String?, val isNetworkError: Boolean = false) : TaskListState()

}