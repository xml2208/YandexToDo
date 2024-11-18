package com.xml.yandextodo.data.remote.datasource

import com.xml.yandextodo.data.model.TaskRequest
import com.xml.yandextodo.data.remote.api.TaskApi

class RemoteDataSource(private val api: TaskApi) {

    suspend fun getAllTasks() = api.getTasks()

    suspend fun updateTask(revision: Int, id: String, request: TaskRequest) =
        api.updateTask(id = id, revision = revision, task = request)

    suspend fun addTask(revision: Int, request: TaskRequest) = api.addTask(revision, request)

    suspend fun getTask(id: String) = api.getTask(id)

    suspend fun deleteTask(id: String, revision: Int) = api.deleteTask(id, revision)
}
