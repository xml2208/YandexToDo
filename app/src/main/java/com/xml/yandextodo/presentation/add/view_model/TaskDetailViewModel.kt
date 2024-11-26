package com.xml.yandextodo.presentation.add.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xml.yandextodo.domain.model.Importance
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.domain.usecases.AddTaskUseCase
import com.xml.yandextodo.domain.usecases.CheckInternetConnectivityRepository
import com.xml.yandextodo.domain.usecases.DeleteTaskUseCase
import com.xml.yandextodo.domain.usecases.GetTaskUseCase
import com.xml.yandextodo.domain.usecases.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class TaskDetailViewModel @Inject constructor(
    private val getTaskUseCase: GetTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val internetConnectivityRepository: CheckInternetConnectivityRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow<TaskDetailState>(TaskDetailState.Loading)
    val viewState get() = _viewState.asStateFlow()

    private val _events = MutableSharedFlow<TaskDetailEvent>()
    private val sad = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            internetConnectivityRepository.connectedFlow.collectLatest {
                sad.value = it
                if (!it) {
                    _viewState.value = TaskDetailState.Error("Нет подключения к интернету", true)
                }
            }
        }
    }

    fun subscribeToEvents() {
        viewModelScope.launch {
            _events.collect { event ->
                when (event) {
                    is TaskDetailEvent.OnLoad -> loadTask(event.id)
                    is TaskDetailEvent.OnSave -> saveTask(event.tasItemUiModel)
                    is TaskDetailEvent.OnTitleValueChanged -> onTaskTitleChanged(event.text)
                    is TaskDetailEvent.OnImportanceToggleChanged -> togglePriority(event.importance)
                    is TaskDetailEvent.OnDeadlineChanged -> onDeadlineChange(event.deadline)
                    is TaskDetailEvent.DeleteTask -> deleteTask(event.id)
                    is TaskDetailEvent.OnError -> setError(event.message)
                }
            }
        }
    }

    fun setEvent(event: TaskDetailEvent) {
        viewModelScope.launch { _events.emit(event) }
    }

    private fun setErrorState(message: String, isNetworkError: Boolean = false) {
        _viewState.value = TaskDetailState.Error(message = message, isNetworkError = isNetworkError)
    }

    private suspend fun getTask(id: String) = getTaskUseCase(id)

    private fun loadTask(id: String) {
        _viewState.value = TaskDetailState.Loading
        viewModelScope.launch {
            try {
                if (id.isEmpty()) {
                    _viewState.value = TaskDetailState.Content()
                    return@launch
                }
                val task = getTask(id)

                task?.let {
                    _viewState.value = TaskDetailState.Content(
                        id = it.id,
                        taskTitle = it.text,
                        importance = it.importance,
                        deadline = it.deadline,
                        isCompleted = it.isCompleted
                    )
                } ?: run {
                    setErrorState(
                        isNetworkError = true,
                        message = "Задача не найдена, включите Интернет"
                    )
                }
            } catch (e: Exception) {
                _viewState.value = TaskDetailState.Error(
                    message = e.message ?: "Произошла ошибка",
                    isNetworkError = e is IOException
                )
            }
        }
    }

    private fun saveTask(todoItemUiModel: TodoItemUiModel?) {
        val currentTask = todoItemUiModel ?: return
        viewModelScope.launch {
            try {
                _viewState.value = TaskDetailState.Loading
                if (getTask(currentTask.id) == null) {
                    addTaskUseCase(currentTask)
                } else {
                    updateTaskUseCase(currentTask)
                }
                _viewState.value = TaskDetailState.OnDone
            } catch (e: Exception) {
                setErrorState(message = "Невозможно добавить задачу. ${e.message}")
            }

        }

    }

    private fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(taskId)
                _viewState.value = TaskDetailState.OnDone
            } catch (e: Exception) {
                setErrorState(message = "Невозможно удалить задачу: ${e.message}")
            }
        }
    }

    private fun setError(message: String?) {
        _viewState.value = TaskDetailState.Error(message)
    }

    private fun onTaskTitleChanged(newTitle: String) {
        val currentState = _viewState.value as TaskDetailState.Content
        _viewState.value = currentState.copy(taskTitle = newTitle)
    }

    fun togglePriority(importance: Importance) {
        val currentState = _viewState.value as? TaskDetailState.Content ?: return
        _viewState.value = currentState.copy(importance = importance)
    }

    private fun onDeadlineChange(newDeadline: Date?) {
        val currentState = _viewState.value as? TaskDetailState.Content ?: return
        _viewState.value = currentState.copy(deadline = newDeadline)
    }

    fun formatDate(date: Date?): String? =
        date?.let { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it) }


}