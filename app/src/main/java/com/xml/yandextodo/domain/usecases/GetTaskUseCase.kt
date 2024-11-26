package com.xml.yandextodo.domain.usecases

import com.xml.yandextodo.domain.repository.TodoRepository
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(private val  repository: TodoRepository) {
    suspend operator fun invoke(id: String) = repository.getTaskById(id)
}