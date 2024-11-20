package com.xml.yandextodo.presentation.add.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xml.yandextodo.domain.model.Importance
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.domain.usecases.AddTaskUseCase
import com.xml.yandextodo.domain.usecases.DeleteTaskUseCase
import com.xml.yandextodo.domain.usecases.GetTaskUseCase
import com.xml.yandextodo.domain.usecases.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskDetailViewModel(
    private val getTaskUseCase: GetTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow<TaskDetailState>(TaskDetailState.Loading)
    val viewState get() = _viewState.asStateFlow()

    private val _events = MutableSharedFlow<TaskDetailEvent>()

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

    private suspend fun getTask(id: String) = getTaskUseCase(id)

    private fun loadTask(id: String) {
        viewModelScope.launch {
            try {
                _viewState.value = TaskDetailState.Loading
                val task = getTask(id)
                if (task != null) {
                    _viewState.value = TaskDetailState.Content(
                        id = task.id,
                        taskTitle = task.text,
                        importance = task.importance,
                        deadline = task.deadline,
                        isCompleted = task.isCompleted
                    )
                } else {
                    _viewState.value = TaskDetailState.Content()
                }
            } catch (e: Exception) {
                _viewState.value = TaskDetailState.Error(e.message)
            }
        }
    }

    private fun saveTask(todoItemUiModel: TodoItemUiModel?) {
        val currentTask = todoItemUiModel ?: return
        viewModelScope.launch {
            try {
                if (getTask(currentTask.id) == null) {
                    addTaskUseCase(currentTask)
                } else {
                    updateTaskUseCase(currentTask)
                }
            } catch (e: Exception) {
                _viewState.value = TaskDetailState.Error(message = "Невозможно добавить задачу. ${e.message}")
            }
        }

    }

    private fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(taskId)
            } catch (e: Exception) {
                _viewState.value = TaskDetailState.Error(message = "Невозможно удалить задачу: ${e.message}")
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
        val currentState = _viewState.value as TaskDetailState.Content
        _viewState.value = currentState.copy(importance = importance)
    }

    private fun onDeadlineChange(newDeadline: Date?) {
        val currentState = _viewState.value as TaskDetailState.Content
        _viewState.value = currentState.copy(deadline = newDeadline)
    }

    fun formatDate(date: Date?): String? =
        date?.let { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it) }


}