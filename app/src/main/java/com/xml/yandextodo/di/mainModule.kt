package com.xml.yandextodo.di

import androidx.room.Room
import com.xml.yandextodo.data.local.TaskDao
import com.xml.yandextodo.data.local.TaskDatabase
import com.xml.yandextodo.data.local.datasource.LocalDataSource
import com.xml.yandextodo.data.remote.api.AuthInterceptor
import com.xml.yandextodo.data.remote.api.TaskApi
import com.xml.yandextodo.data.remote.api.loggingInterceptor
import com.xml.yandextodo.data.remote.datasource.RemoteDataSource
import com.xml.yandextodo.domain.repository.TodoRepository
import com.xml.yandextodo.data.repository.ToDoRepositoryImpl
import com.xml.yandextodo.domain.usecases.AddTaskUseCase
import com.xml.yandextodo.domain.usecases.CheckInternetConnectivityRepository
import com.xml.yandextodo.domain.usecases.DeleteTaskUseCase
import com.xml.yandextodo.domain.usecases.GetTaskListUseCase
import com.xml.yandextodo.domain.usecases.GetTaskUseCase
import com.xml.yandextodo.domain.usecases.UpdateTaskUseCase
import com.xml.yandextodo.presentation.add.view_model.TaskDetailViewModel
import com.xml.yandextodo.presentation.list.view_model.TaskListViewModel
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIME_OUT = 30L

val mainModule = module {

    single {
        Room
            .databaseBuilder(context = get(), TaskDatabase::class.java, name = "app_database")
            .build()
    }

    single { get<TaskDatabase>().taskDao() }

    single<TodoRepository> {
        ToDoRepositoryImpl(
            remoteDataSource = get<RemoteDataSource>(),
            localDataSource = get<LocalDataSource>(),
        )
    }
    factory { DeleteTaskUseCase(repository = get<TodoRepository>()) }
    factory { UpdateTaskUseCase(repository = get<TodoRepository>()) }
    factory { AddTaskUseCase(repository = get<TodoRepository>()) }
    factory { GetTaskListUseCase(repository = get<TodoRepository>()) }
    factory { GetTaskUseCase(repository = get<TodoRepository>()) }

    single { CheckInternetConnectivityRepository(context = get()) }

    viewModel { TaskDetailViewModel(get(), get(), get(), get()) }

    viewModel {
        TaskListViewModel(
            taskListUseCase = get<GetTaskListUseCase>(),
            updateTaskUseCase = get<UpdateTaskUseCase>(),
            getTaskUseCase = get<GetTaskUseCase>(),
            internetConnectivityRepository = get<CheckInternetConnectivityRepository>()
        )
    }

    single { provideOkhttp() }

    single { provideRetrofit(client = get<OkHttpClient>()) }

    single { provideApiService(retrofit = get<Retrofit>()) }

    single { RemoteDataSource(api = get<TaskApi>()) }

    single { LocalDataSource(dao = get<TaskDao>()) }

}

private fun provideOkhttp(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .authenticator(AuthInterceptor())
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .build()
}

private fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://hive.mrdekk.ru/todo/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

fun provideApiService(retrofit: Retrofit): TaskApi = retrofit.create(TaskApi::class.java)

