package com.xml.yandextodo.presentation.list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.domain.usecases.CheckInternetConnectivityRepository
import com.xml.yandextodo.domain.usecases.GetTaskListUseCase
import com.xml.yandextodo.domain.usecases.GetTaskUseCase
import com.xml.yandextodo.domain.usecases.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val taskListUseCase: GetTaskListUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    checkInternetConnectivityUseCase: CheckInternetConnectivityRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<TaskListState>(TaskListState.Loading)
    val viewState get() = _viewState.asStateFlow()

    private val _events = MutableSharedFlow<TaskListEvent>()

    val internetAvailable = checkInternetConnectivityUseCase.observeConnectivity()

    init {
        refreshTodos()
    }

    fun subscribeToEvents() {
        viewModelScope.launch {
            _events.collectLatest { event ->
                when (event) {
                    is TaskListEvent.RefreshTodos -> refreshTodos()
                    is TaskListEvent.GetTask -> getTask(event.id)
                    is TaskListEvent.OnCheckedChange -> toggleTaskCompletion(event.todo)
                }
            }
        }
        refreshTodos()
    }

    fun setEvent(event: TaskListEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun refreshTodos() {
        _viewState.value = TaskListState.Loading
        try {
            viewModelScope.launch {
                _viewState.value = TaskListState.Content(taskList = taskListUseCase())
            }
        } catch (e: Exception) {
            _viewState.value = TaskListState.Error(error = "Can not refresh: ${e.message}")
        }
    }

    private fun toggleTaskCompletion(task: TodoItemUiModel) {
        viewModelScope.launch {
            updateTaskUseCase(task.copy(isCompleted = !task.isCompleted))
            refreshTodos()
        }
    }

    private fun getTask(id: String?) {
        if (id != null) {
            viewModelScope.launch {
                try {
                    getTaskUseCase(id)
                } catch (e: Exception) {
                    TaskListState.Error(error = "EXCEPTION: ${e.message}")
                }
            }
        }
    }
}