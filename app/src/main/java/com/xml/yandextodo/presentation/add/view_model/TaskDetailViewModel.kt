package com.xml.yandextodo.presentation.add.view_model

import androidx.lifecycle.viewModelScope
import com.xml.yandextodo.domain.model.Importance
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.domain.usecases.AddTaskUseCase
import com.xml.yandextodo.domain.usecases.DeleteTaskUseCase
import com.xml.yandextodo.domain.usecases.GetTaskUseCase
import com.xml.yandextodo.domain.usecases.UpdateTaskUseCase
import com.xml.yandextodo.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskDetailViewModel(
    private val getTaskUseCase: GetTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : BaseViewModel<TaskDetailContract.State, TaskDetailContract.TaskDetailEvent>() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    override fun setInitialState(): TaskDetailContract.State {
        return TaskDetailContract.State(
            loading = false,
            taskItem = TodoItemUiModel.initialTask,
            error = null
        )
    }

    override fun handleEvents(event: TaskDetailContract.TaskDetailEvent) {

        when (event) {
            is TaskDetailContract.TaskDetailEvent.OnLoad -> loadTask(event.id)
            is TaskDetailContract.TaskDetailEvent.OnSave -> saveTask(event.tasItemUiModel)
            is TaskDetailContract.TaskDetailEvent.OnTitleValueChanged -> onTaskTitleChanged(event.text)
            is TaskDetailContract.TaskDetailEvent.OnImportanceToggleChanged -> togglePriority(event.importance)
            is TaskDetailContract.TaskDetailEvent.OnDeadlineChanged -> onDeadlineChange(event.deadline)
            is TaskDetailContract.TaskDetailEvent.DeleteTask -> deleteTask(event.id)
        }
    }

    private fun loadTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(loading = true) }
            val task = getTaskUseCase.invoke(id)
            if (task != null) setState { copy(loading = false, taskItem = task) }
            setState { copy(loading = false)  }
        }
    }

    private suspend fun getTask(id: String) = getTaskUseCase(id)

    private fun onTaskTitleChanged(newTitle: String) {
        setState { copy(taskItem = taskItem.copy(text = newTitle)) }
    }

    fun togglePriority(importance: Importance) {
        setState { copy(taskItem = taskItem.copy(importance = importance)) }
    }

    private fun onDeadlineChange(newDeadline: Date?) {
        setState { copy(taskItem = taskItem.copy(deadline = newDeadline)) }
    }


    private fun saveTask(todoItemUiModel: TodoItemUiModel?) {
        val scope = CoroutineScope(Job())
        scope.launch(handler) {
            val currentTask = todoItemUiModel ?: return@launch
            if (getTask(currentTask.id) == null) {
                addTaskUseCase(currentTask)
            } else {
                updateTaskUseCase(currentTask)
            }
        }
    }

    private fun deleteTask(taskId: String?) {
        viewModelScope.launch(handler) {
            if (taskId != null) {
                deleteTaskUseCase(taskId)
            }
        }
    }


    fun formatDate(date: Date?): String? =
        date?.let { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it) }


}

/*
         Saving/Editing TaskItems without database!

 fun saveTask(text: String, importance: Importance, deadline: Date?) {
    viewModelScope.launch {
        if (text.isBlank()) return@launch

        val newTask = TaskItem(
            id = null, // Repository will set this
            text = text,
            importance = importance,
            deadline = null, // You can add deadline handling later
            isCompleted = false,
            createdAt = Date()
        )

        todoRepository.addTask(newTask)
    }
}

private val _taskItemState = MutableStateFlow(AddTaskState())
val taskItemState = _taskItemState.asStateFlow()


fun saveTask1() {
    val currentState = _taskItemState.value
    val task = TaskItem(
        id = currentState.id,
        text = currentState.text,
        importance = currentState.importance,
        deadline = currentState.deadline,
        isCompleted = currentState.isCompleted,
        createdAt = currentState.createdAt
    )

    if (task.id == null) {
        viewModelScope.launch {
            todoRepository.saveTask(task) // Adding new task
        }
    } else {
        todoRepository.updateTask(task) // Updating existing task
    }
}
*/