package com.xml.yandextodo.domain.usecases

import com.xml.yandextodo.domain.repository.TodoRepository

class GetTaskUseCase(private val  repository: TodoRepository) {
    suspend operator fun invoke(id: String) = repository.getTaskById(id)
}