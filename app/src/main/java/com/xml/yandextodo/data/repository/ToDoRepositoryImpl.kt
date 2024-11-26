package com.xml.yandextodo.data.repository

import com.xml.yandextodo.data.local.datasource.LocalDataSource
import com.xml.yandextodo.data.mapper.toDomainList
import com.xml.yandextodo.data.mapper.toDto
import com.xml.yandextodo.data.mapper.toEntity
import com.xml.yandextodo.data.mapper.toUi
import com.xml.yandextodo.data.mapper.toUiList
import com.xml.yandextodo.data.model.TaskRequest
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.data.remote.datasource.RemoteDataSource
import com.xml.yandextodo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToDoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : TodoRepository {

    private var lastKnownRevision = 0

    override suspend fun getAllTasks(onGettingFromLocal: (String) -> Unit): List<TodoItemUiModel> =
        try {
            val response = remoteDataSource.getAllTasks()
            lastKnownRevision = response.revision
            localDataSource.insertTaskList(response.list.toDomainList())
            response.list.toUiList()
        } catch (e: Exception) {
            onGettingFromLocal("Получено из локальной базы данных")
            localDataSource.getTaskList().first().toUi()
        }


    override suspend fun addTask(task: TodoItemUiModel) {
        val request = TaskRequest(todoItem = task.toDto())
        try {
            remoteDataSource.addTask(lastKnownRevision, request)
        } catch (e: Exception) {
            localDataSource.insertTask(task.toEntity())
            e.printStackTrace()
        }
    }

    override suspend fun updateTask(updatedTask: TodoItemUiModel) {
        try {
            remoteDataSource.updateTask(
                revision = lastKnownRevision,
                id = updatedTask.id,
                request = TaskRequest(todoItem = updatedTask.toDto())
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getTaskById(id: String): TodoItemUiModel? =
        try {
            remoteDataSource.getTask(id).element.toUi()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    override suspend fun deleteTask(id: String) {
        remoteDataSource.deleteTask(id, revision = lastKnownRevision)
    }
}