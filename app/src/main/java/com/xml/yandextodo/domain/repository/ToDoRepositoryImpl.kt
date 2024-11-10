package com.xml.yandextodo.domain.repository

import com.xml.yandextodo.data.local.TaskDao
import com.xml.yandextodo.data.local.entity.TaskItemEntity
import com.xml.yandextodo.data.mapper.Mapper
import com.xml.yandextodo.data.model.FakeTaskListData
import com.xml.yandextodo.data.model.TodoItem
import com.xml.yandextodo.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ToDoRepositoryImpl(private val taskDao: TaskDao) : TodoRepository {

    override fun getAllTasks(): Flow<List<TodoItem>> {
        return taskDao.getAllTasks()
            .map { taskEntities ->
                if (taskEntities.isEmpty()) {
                    val list = FakeTaskListData.harCodedList.map { Mapper().toEntity(it) }
                    taskDao.insertAll(list)
                    Mapper().toDomainList(list)
                } else {
                    Mapper().toDomainList(taskEntities)
                }
            }
    }

    override suspend fun addTask(task: TodoItem) {
        val taskEntity = Mapper().toEntity(task)
        taskDao.insertTask(taskEntity)
    }

    override suspend fun updateTask(updatedTask: TodoItem) {
        val updatedTaskEntity = Mapper().toEntity(updatedTask)
        taskDao.updateTask(updatedTaskEntity)
    }

    override suspend fun deleteTask(id: Long?) {
        val taskEntity = withContext(Dispatchers.IO) {
            taskDao.getTaskItem(id)
        }
        if (taskEntity != null) taskDao.deleteTask(taskEntity)
    }

    override suspend fun getTaskById(id: Long?): Flow<TodoItem> =
        flow {
            val taskEntity = withContext(Dispatchers.IO) { getTaskEntity(id) }
            taskEntity?.let { Mapper().toDomain(it) }?.let { emit(it) }
        }

    private fun getTaskEntity(id: Long?): TaskItemEntity? {
        return if (id != -1L) {
            taskDao.getTaskItem(id)
        } else {
            TodoItem.initialTaskEntity
        }
    }
}