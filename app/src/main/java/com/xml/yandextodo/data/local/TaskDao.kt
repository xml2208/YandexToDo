package com.xml.yandextodo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.xml.yandextodo.data.local.entity.TaskItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(task: List<TaskItemEntity>)

    @Delete
    suspend fun deleteTask(task: TaskItemEntity)

    @Update
    suspend fun updateTask(task: TaskItemEntity)

    @Query("SELECT * FROM todo_database ORDER BY text asc")
    fun getAllTasks(): Flow<List<TaskItemEntity>>

    @Query("SELECT * FROM todo_database where id = :id")
    fun getTaskItem(id: Long?): TaskItemEntity?

    @Query("DELETE FROM todo_database")
    fun deleteAll()

}