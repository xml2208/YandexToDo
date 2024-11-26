package com.xml.yandextodo.di.module

import android.content.Context
import com.xml.yandextodo.data.local.TaskDao
import com.xml.yandextodo.data.local.datasource.LocalDataSource
import com.xml.yandextodo.data.remote.api.TaskApi
import com.xml.yandextodo.data.remote.datasource.RemoteDataSource
import com.xml.yandextodo.data.repository.ToDoRepositoryImpl
import com.xml.yandextodo.domain.repository.TodoRepository
import com.xml.yandextodo.domain.usecases.CheckInternetConnectivityRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRemoteDataSource(api: TaskApi): RemoteDataSource =
        RemoteDataSource(api)

    @Provides
    @Singleton
    fun provideLocalDataSource(dao: TaskDao): LocalDataSource =
        LocalDataSource(dao)

    @Provides
    @Singleton
    fun provideTodoRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): TodoRepository = ToDoRepositoryImpl(remoteDataSource, localDataSource)

    @Provides
    @Singleton
    fun provideConnectivityRepository(context: Context): CheckInternetConnectivityRepository =
        CheckInternetConnectivityRepository(context)
}
