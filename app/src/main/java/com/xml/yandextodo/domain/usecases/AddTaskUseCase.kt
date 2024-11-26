package com.xml.yandextodo.domain.usecases

import com.xml.yandextodo.domain.repository.TodoRepository
import com.xml.yandextodo.domain.model.TodoItemUiModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(taskItem: TodoItemUiModel) = repository.addTask(task = taskItem)
}