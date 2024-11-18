package com.xml.yandextodo.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_database")
data class TaskItemEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "importance") val importance: String,
    @ColumnInfo(name = "deadline") val deadline: Long?,
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean,
    @ColumnInfo(name = "createdAt") val createdAt: Long?,
    @ColumnInfo(name = "changedAt") val changedAt: Long?,
    @ColumnInfo(name = "lastUpdatedBy") val lastUpdatedBy: String,
)