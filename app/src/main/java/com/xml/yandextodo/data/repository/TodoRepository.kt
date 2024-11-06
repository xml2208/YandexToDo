package com.xml.yandextodo.data.repository

import com.xml.yandextodo.data.model.TaskItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getAllTasks(): Flow<List<TaskItem>>

    suspend fun addTask(task: TaskItem)

    suspend fun updateTask(updatedTask: TaskItem)

    suspend fun getTaskById(id: Long?): Flow<TaskItem?>

    suspend fun deleteTask(id: Long?)

}