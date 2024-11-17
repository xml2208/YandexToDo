package com.xml.yandextodo.data.repository

import android.content.Context
import android.widget.Toast
import com.xml.yandextodo.data.local.datasource.LocalDataSource
import com.xml.yandextodo.data.mapper.toDto
import com.xml.yandextodo.data.mapper.toUi
import com.xml.yandextodo.data.mapper.toUiList
import com.xml.yandextodo.data.model.TaskRequest
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.data.remote.datasource.RemoteDataSource
import com.xml.yandextodo.domain.repository.TodoRepository

class ToDoRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val context: Context,
) : TodoRepository {

    private var lastKnownRevision = 0

    override suspend fun getAllTasks(): List<TodoItemUiModel> =
        try {
            val response = remoteDataSource.getAllTasks()
            lastKnownRevision = response.revision
            response.list.toUiList()
//            localDataSource.getTaskList().map { it.toUi() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }


    override suspend fun addTask(task: TodoItemUiModel) {
        val request = TaskRequest(todoItem = task.toDto())
        try {
            remoteDataSource.addTask(lastKnownRevision, request)
        } catch (e: Exception) {
            Toast.makeText(context, "Невозможно добавить задачу. Попробуйте еще раз.", Toast.LENGTH_LONG).show()
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
            Toast.makeText(context, "Невозможно обновить задачу. Попробуйте еще раз.", Toast.LENGTH_LONG).show()
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


//    override suspend fun refreshTasks() {
//        try {
//            val response = remoteDataSource.getAllTasks()
//            if (response.status == "ok") {
//                localDataSource.deleteAll()
//                localDataSource.insertTaskList(response.list.toDomainList())
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }


//    private fun getTaskEntity(id: Long?): TaskItemEntity? {
//        return if (id != -1L) {
//            taskDao.getTaskItem(id)
//        } else {
//            TodoItem.initialTaskEntity
//        }
//    }
}