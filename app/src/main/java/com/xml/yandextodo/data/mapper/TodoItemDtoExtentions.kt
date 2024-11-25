package com.xml.yandextodo.data.mapper

import com.xml.yandextodo.data.local.entity.TaskItemEntity
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.data.model.TodoItemDto
import com.xml.yandextodo.domain.model.Importance
import java.util.Date

fun TodoItemUiModel.toDto(): TodoItemDto {
    return TodoItemDto(
        id = id,
        text = this.text,
        importance = this.importance.name.lowercase(),
        deadline = deadline?.time,
        done = this.isCompleted,
        createdAt = createdAt?.time ?: 0,
        changedAt = changed_at?.time ?: 1234567,
        lastUpdatedBy = "this"
    )
}

fun TodoItemDto.toUi(): TodoItemUiModel {
    return TodoItemUiModel(
        id = this.id,
        text = this.text,
        importance = this.importance.let { Importance.valueOf(it.uppercase()) },
        deadline = deadline?.let { Date(it) },
        isCompleted = this.done,
        createdAt = Date(createdAt),
        changed_at = changedAt?.let { Date(it) },
        last_updated_by = this.lastUpdatedBy,
    )
}

fun List<TodoItemDto>.toUiList(): List<TodoItemUiModel> {
    return map { itemDto -> itemDto.toUi() }
}


fun TaskItemEntity.toUiModel(): TodoItemUiModel {
    return TodoItemUiModel(
        id = id.toString(),
        text = this.text,
        importance = this.importance.let { Importance.valueOf(it) },
//        importance = this.importance.let { Importance.valueOf(it.lowercase()) },
        deadline = this.deadline?.let { Date(it) },
        isCompleted = this.isCompleted,
        createdAt =  this.createdAt?.let { Date(it) },
    )
}

fun List<TaskItemEntity>.toUi(): List<TodoItemUiModel> {
    return map { itemEntity -> itemEntity.toUiModel() }
}


fun TodoItemUiModel.toEntity(): TaskItemEntity {
    return TaskItemEntity(
        id = id.toLong(),
        text = this.text,
        importance = "low",
        deadline = deadline?.time,
        isCompleted = this.isCompleted,
        createdAt = createdAt?.time ?: 0,
    )
}
//
fun TodoItemDto.toDomain(): TaskItemEntity {
    return TaskItemEntity(
        id = if (this.id == "") 0 else this.id.toLong(),
        text = this.text,
        importance = Importance.valueOf(importance.uppercase()).toString(),
        deadline = deadline,
        isCompleted = this.done,
        createdAt = createdAt,
    )
}

fun List<TodoItemDto>.toDomainList(): List<TaskItemEntity> {
    return map { itemDto -> itemDto.toDomain() }
}

//    fun toEntity(todoItem: TodoItem): TaskItemEntity {
//        return TaskItemEntity(
//            id = todoItem.id,
//            text = todoItem.text,
//            importance = todoItem.importance.name,
//            deadline = todoItem.deadline?.time,
//            isCompleted = todoItem.isCompleted,
//            createdAt = todoItem.createdAt?.time
//        )
//    }
