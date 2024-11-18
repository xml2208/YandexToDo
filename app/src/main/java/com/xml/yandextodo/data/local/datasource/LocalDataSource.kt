package com.xml.yandextodo.data.local.datasource

import com.xml.yandextodo.data.local.TaskDao
import com.xml.yandextodo.data.local.entity.TaskItemEntity

class LocalDataSource(
    private val dao: TaskDao,
) {

    suspend fun insertTaskList(tasks: List<TaskItemEntity>) {
        dao.insertAll(tasks)
    }

    suspend fun insertTask(task: TaskItemEntity) {
        dao.insertTask(task)
    }

    suspend fun deleteTask(task: TaskItemEntity) {
        dao.deleteTask(task)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun updateTask(task: TaskItemEntity) {
        dao.updateTask(task)
    }

    suspend fun getTaskList() = dao.getAllTasks()

    suspend fun getTask(id: Long?): TaskItemEntity? = dao.getTaskItem(id)
}