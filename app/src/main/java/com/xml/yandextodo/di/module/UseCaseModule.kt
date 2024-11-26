package com.xml.yandextodo.di.module

import com.xml.yandextodo.domain.repository.TodoRepository
import com.xml.yandextodo.domain.usecases.AddTaskUseCase
import com.xml.yandextodo.domain.usecases.DeleteTaskUseCase
import com.xml.yandextodo.domain.usecases.GetTaskListUseCase
import com.xml.yandextodo.domain.usecases.GetTaskUseCase
import com.xml.yandextodo.domain.usecases.UpdateTaskUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun provideAddTaskUseCase(repository: TodoRepository): AddTaskUseCase =
        AddTaskUseCase(repository)

    @Provides
    fun provideDeleteTaskUseCase(repository: TodoRepository): DeleteTaskUseCase =
        DeleteTaskUseCase(repository)

    @Provides
    fun provideUpdateTaskUseCase(repository: TodoRepository): UpdateTaskUseCase =
        UpdateTaskUseCase(repository)

    @Provides
    fun provideTaskListUseCase(repository: TodoRepository): GetTaskListUseCase =
        GetTaskListUseCase(repository)

    @Provides
    fun provideGetTaskUseCase(repository: TodoRepository): GetTaskUseCase =
        GetTaskUseCase(repository)
}