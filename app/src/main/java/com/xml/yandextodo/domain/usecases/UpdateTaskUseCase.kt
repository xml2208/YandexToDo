package com.xml.yandextodo.domain.usecases

import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.domain.repository.TodoRepository

class UpdateTaskUseCase(private val repository: TodoRepository) {

    suspend operator fun invoke(taskUiModel: TodoItemUiModel) = repository.updateTask(taskUiModel)
}