package com.xml.yandextodo.domain.repository

import com.xml.yandextodo.data.local.TaskDao
import com.xml.yandextodo.data.mapper.Mapper
import com.xml.yandextodo.data.model.FakeTaskListData
import com.xml.yandextodo.data.model.TaskItem
import com.xml.yandextodo.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ToDoRepositoryImpl(private val taskDao: TaskDao) : TodoRepository {

    override fun getAllTasks(): Flow<List<TaskItem>> {
        return taskDao.getAllTasks()
            .map { taskEntities ->
                if (taskEntities.isEmpty()) {
                    val list = FakeTaskListData.harCodedList.map { Mapper.toEntity(it) }
                    taskDao.insertAll(list)
                    Mapper.toDomainList(list)
                } else {
                    Mapper.toDomainList(taskEntities)
                }
            }
    }

    override suspend fun addTask(task: TaskItem) {
        val taskEntity = Mapper.toEntity(task)
        taskDao.insertTask(taskEntity)
    }

    override suspend fun updateTask(updatedTask: TaskItem) {
        val updatedTaskEntity = Mapper.toEntity(updatedTask)
        taskDao.updateTask(updatedTaskEntity)
    }

    override suspend fun deleteTask(id: Long?) {
        val taskEntity = withContext(Dispatchers.IO) {
            taskDao.getTaskItem(id)
        }
        if (taskEntity != null) taskDao.deleteTask(taskEntity)
    }

    override suspend fun getTaskById(id: Long?): Flow<TaskItem> =
        flow {
            val taskEntity = withContext(Dispatchers.IO) {
                taskDao.getTaskItem(id)
            }
            taskEntity?.let { Mapper.toDomain(it) }?.let { emit(it) }
        }
}