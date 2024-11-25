package com.xml.yandextodo.domain.usecases
import com.xml.yandextodo.domain.repository.TodoRepository

class GetTaskListUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(onGettingFromLocal: (String) -> Unit) = repository.getAllTasks(onGettingFromLocal)

}