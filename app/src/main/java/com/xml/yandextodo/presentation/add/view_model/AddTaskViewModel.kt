package com.xml.yandextodo.presentation.add.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xml.yandextodo.data.model.Importance
import com.xml.yandextodo.data.model.TodoItem
import com.xml.yandextodo.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTaskViewModel(
    private val todoRepository: TodoRepository,
) : ViewModel() {

    private val _task = MutableStateFlow(TodoItem.initialTask)
    val task = _task.asStateFlow()

    fun loadTask(id: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = todoRepository.getTaskById(id).first()
            if (task != null) _task.value = task
        }
    }

    fun onTaskTitleChanged(newTitle: String) {
        _task.value = _task.value.copy(text = newTitle)
    }

    fun saveTask(todoItem: TodoItem?) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTask = todoItem ?: return@launch
            if (currentTask.id == null) {
                todoRepository.addTask(todoItem)
            } else {
                todoRepository.updateTask(currentTask)
            }
        }
    }

    fun deleteTask(taskId: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (taskId != null) {
                todoRepository.deleteTask(taskId)
            }
        }
    }

    fun togglePriority(importance: Importance) {
        _task.value = _task.value.copy(importance = importance)
    }

    fun onDeadlineChange(newDeadline: Date?) {
        _task.value = _task.value.copy(deadline = newDeadline)
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