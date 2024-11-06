package com.xml.yandextodo.presentation.list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xml.yandextodo.data.model.TaskItem
import com.xml.yandextodo.data.repository.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val repository: TodoRepository,
) : ViewModel() {

    val tasks = repository.getAllTasks().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleTaskCompletion(task: TaskItem) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
                                /* Showing TaskLists without database*/
//    private var _allTasks = MutableStateFlow<List<TaskItem>>(emptyList())
//    val allTasks = _allTasks
//
//    var showCompleted = mutableStateOf(true)
//
//    init {
//        getAllTasks()
//    }
//
//    fun onCheckedChange(taskItem: TaskItem) {
//        taskItem.isCompleted = !taskItem.isCompleted
////        _allTasks.value = _allTasks.value.
//
//    }
//
//     fun getAllTasks() {
//        viewModelScope.launch {
//            todoRepository.getTasks().collectLatest {
//                _allTasks.value = it
//            }
////            _allTasks.value = todoRepository.getTasks()
//        }
//    }
}