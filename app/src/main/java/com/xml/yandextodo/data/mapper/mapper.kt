package com.xml.yandextodo.data.mapper

import com.xml.yandextodo.data.local.entity.TaskItemEntity
import com.xml.yandextodo.data.model.Importance
import com.xml.yandextodo.data.model.TaskItem
import java.util.Date

object Mapper {

    fun toEntity(taskItem: TaskItem): TaskItemEntity {
        return TaskItemEntity(
            id = taskItem.id,
            text = taskItem.text,
            importance = taskItem.importance.name,
            deadline = taskItem.deadline?.time,
            isCompleted = taskItem.isCompleted,
            createdAt = taskItem.createdAt?.time
        )
    }

    fun toDomain(taskItemEntity: TaskItemEntity): TaskItem {
        return TaskItem(
            id = taskItemEntity.id,
            text = taskItemEntity.text,
            importance = Importance.valueOf(taskItemEntity.importance),
            deadline = taskItemEntity.deadline?.let { Date(it) },
            isCompleted = taskItemEntity.isCompleted,
            createdAt = taskItemEntity.createdAt?.let { Date(it) }
        )
    }

    fun toDomainList(taskItemEntities: List<TaskItemEntity>): List<TaskItem> {
        return taskItemEntities.map { toDomain(it) }
    }

}