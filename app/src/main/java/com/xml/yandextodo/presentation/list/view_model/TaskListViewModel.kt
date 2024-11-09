package com.xml.yandextodo.presentation.list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xml.yandextodo.data.model.TodoItem
import com.xml.yandextodo.data.repository.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val repository: TodoRepository,
) : ViewModel() {

    val tasks = repository.getAllTasks().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleTaskCompletion(task: TodoItem) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
}