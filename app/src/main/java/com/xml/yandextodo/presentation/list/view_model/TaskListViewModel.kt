package com.xml.yandextodo.presentation.list.view_model

import androidx.lifecycle.viewModelScope
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.domain.usecases.GetTaskListUseCase
import com.xml.yandextodo.domain.usecases.GetTaskUseCase
import com.xml.yandextodo.domain.usecases.UpdateTaskUseCase
import com.xml.yandextodo.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val taskListUseCase: GetTaskListUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
) : BaseViewModel<TaskListContract.State, TaskListContract.TaskListEvent>() {

    init {
        refreshTodos()
    }

    override fun setInitialState(): TaskListContract.State {
        return TaskListContract.State(loading = true, taskList = emptyList(), error = null)
    }

    override fun handleEvents(event: TaskListContract.TaskListEvent) {
        when (event) {
            is TaskListContract.TaskListEvent.RefreshTodos -> {
                viewModelScope.launch(Dispatchers.IO) { refreshTodos() }
            }

            is TaskListContract.TaskListEvent.GetTask -> getTask(event.id)
            is TaskListContract.TaskListEvent.OnCheckedChange -> toggleTaskCompletion(event.todo)
        }
    }

    private fun refreshTodos() {
        setState { copy(loading = true) }
        try {
            viewModelScope.launch {
                val list = taskListUseCase()
                setState { copy(loading = false, taskList = list) }
            }
        } catch (e: Exception) {
            setState { copy(loading = false, error = e.message) }
        }
    }

    private fun toggleTaskCompletion(task: TodoItemUiModel) {
        viewModelScope.launch {
            updateTaskUseCase(task.copy(isCompleted = !task.isCompleted))
            refreshTodos()
        }
    }

    private fun getTask(id: String?) {
        if (id != null) viewModelScope.launch { getTaskUseCase(id) }
    }


    /*    refreshing taskList from local
                refreshTaskUseCase()
                    .catch { exception -> setState { copy(error = "Error ${exception.message}") } }
                    .collectLatest { list -> setState { copy(loading = false, taskList = list) } }
    * */
}