package com.xml.yandextodo.data.repository

import com.xml.yandextodo.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getAllTasks(): Flow<List<TodoItem>>

    suspend fun addTask(task: TodoItem)

    suspend fun updateTask(updatedTask: TodoItem)

    suspend fun getTaskById(id: Long?): Flow<TodoItem?>

    suspend fun deleteTask(id: Long?)

}