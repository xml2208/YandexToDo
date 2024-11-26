package com.xml.yandextodo.presentation.list.view_model

import android.util.Log
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
import javax.inject.Inject

class TaskListViewModel @Inject constructor(
    private val taskListUseCase: GetTaskListUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val internetConnectivityRepository: CheckInternetConnectivityRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<TaskListState>(TaskListState.Loading)
    val viewState get() = _viewState.asStateFlow()

    private val isConnected = MutableStateFlow(false)

    private val _events = MutableSharedFlow<TaskListEvent>()

    init {
        subscribeToConnectivity()
        refreshTodos()
    }

    private fun subscribeToConnectivity() {
        viewModelScope.launch {
            internetConnectivityRepository.connectedFlow.collect { netState ->
                isConnected.value = netState
                if (netState) {
                    refreshTodos()
                } else {
                    setErrorState(
                        errorMessage = "Нет подключения к интернету",
                    )
                }
            }
        }
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
        Log.d("TAG", "refreshTodos: ")
        _viewState.value = TaskListState.Loading
        try {
            viewModelScope.launch {
                val taskList = taskListUseCase(onGettingFromLocal = ::setErrorState)
                _viewState.value = TaskListState.Content(taskList = taskList)
            }
        } catch (e: Exception) {
            setErrorState(errorMessage = "Невозможно обновить: ${e.message}")
        }
    }


    private fun toggleTaskCompletion(task: TodoItemUiModel) {
        viewModelScope.launch {
            updateTaskUseCase(task.copy(isCompleted = !task.isCompleted))
            refreshTodos()
        }
    }

    private fun setErrorState(errorMessage: String, isNetworkError: Boolean = true) {
        _viewState.value = TaskListState.Error(errorMessage, isNetworkError = isNetworkError)
    }

    private fun getTask(id: String?) {
        if (id != null) {
            viewModelScope.launch {
                try {
                    getTaskUseCase(id)
                } catch (e: Exception) {
                    setErrorState(errorMessage = "EXCEPTION: ${e.message}", false)
                }
            }
        } else {
            setErrorState("Task не найдена, id=null", false)
        }
    }
}