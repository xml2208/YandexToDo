package com.xml.yandextodo.data.mapper

import com.xml.yandextodo.data.local.entity.TaskItemEntity
import com.xml.yandextodo.data.model.Importance
import com.xml.yandextodo.data.model.TodoItem
import java.util.Date

class Mapper {

    fun toEntity(todoItem: TodoItem): TaskItemEntity {
        return TaskItemEntity(
            id = todoItem.id,
            text = todoItem.text,
            importance = todoItem.importance.name,
            deadline = todoItem.deadline?.time,
            isCompleted = todoItem.isCompleted,
            createdAt = todoItem.createdAt?.time
        )
    }

    fun toDomain(taskItemEntity: TaskItemEntity): TodoItem {
        return TodoItem(
            id = taskItemEntity.id,
            text = taskItemEntity.text,
            importance = Importance.valueOf(taskItemEntity.importance),
            deadline = taskItemEntity.deadline?.let { Date(it) },
            isCompleted = taskItemEntity.isCompleted,
            createdAt = taskItemEntity.createdAt?.let { Date(it) }
        )
    }

    fun toDomainList(taskItemEntities: List<TaskItemEntity>): List<TodoItem> {
        return taskItemEntities.map { toDomain(it) }
    }

}