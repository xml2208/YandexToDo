package com.xml.yandextodo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xml.yandextodo.data.local.entity.TaskItemEntity

@Database(entities = [TaskItemEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
