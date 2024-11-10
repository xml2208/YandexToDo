package com.xml.yandextodo.di

import androidx.room.Room
import com.xml.yandextodo.data.local.TaskDatabase
import com.xml.yandextodo.data.repository.TodoRepository
import com.xml.yandextodo.domain.repository.ToDoRepositoryImpl
import com.xml.yandextodo.presentation.add.view_model.AddTaskViewModel
import com.xml.yandextodo.presentation.list.view_model.TaskListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    single {
        Room
            .databaseBuilder(context = get(), TaskDatabase::class.java, name = "app_database")
            .build()
    }

    single { get<TaskDatabase>().taskDao() }

    factory<TodoRepository> { ToDoRepositoryImpl(get()) }

    viewModel { AddTaskViewModel(todoRepository = get()) }

    viewModel { TaskListViewModel(repository = get()) }

}