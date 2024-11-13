package com.xml.yandextodo.domain.repository

import com.xml.yandextodo.domain.model.TodoItemUiModel

interface TodoRepository {

    suspend fun getAllTasks(): List<TodoItemUiModel>

    suspend fun addTask(task: TodoItemUiModel)

    suspend fun updateTask(updatedTask: TodoItemUiModel)

    suspend fun getTaskById(id: String): TodoItemUiModel?

    suspend fun deleteTask(id: String)

//    suspend fun refreshTasks()
}
