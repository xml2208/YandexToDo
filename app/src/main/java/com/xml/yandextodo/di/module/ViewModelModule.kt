package com.xml.yandextodo.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xml.yandextodo.domain.usecases.AddTaskUseCase
import com.xml.yandextodo.domain.usecases.CheckInternetConnectivityRepository
import com.xml.yandextodo.domain.usecases.DeleteTaskUseCase
import com.xml.yandextodo.domain.usecases.GetTaskListUseCase
import com.xml.yandextodo.domain.usecases.GetTaskUseCase
import com.xml.yandextodo.domain.usecases.UpdateTaskUseCase
import com.xml.yandextodo.presentation.add.view_model.TaskDetailViewModel
import com.xml.yandextodo.presentation.list.view_model.TaskListViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun provideTaskDetailViewModelFactory(
        taskListUseCase: GetTaskListUseCase,
        getTaskUseCase: GetTaskUseCase,
        updateTaskUseCase: UpdateTaskUseCase,
        addTaskUseCase: AddTaskUseCase,
        deleteTaskUseCase: DeleteTaskUseCase,
        internetConnectivityRepository: CheckInternetConnectivityRepository
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when (modelClass) {
                    TaskDetailViewModel::class.java -> TaskDetailViewModel(
                        getTaskUseCase,
                        updateTaskUseCase,
                        addTaskUseCase,
                        deleteTaskUseCase,
                        internetConnectivityRepository,
                    ) as T

                    TaskListViewModel::class.java -> TaskListViewModel(
                        taskListUseCase,
                        updateTaskUseCase,
                        getTaskUseCase,
                        internetConnectivityRepository
                    ) as T

                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}

//class ViewModelFactory<VM : ViewModel>(private val creator: () -> VM) :
//    ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        @Suppress("UNCHECKED_CAST")
//        return creator() as T
//    }
//}