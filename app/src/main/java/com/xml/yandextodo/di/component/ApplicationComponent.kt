package com.xml.yandextodo.di.component

import com.xml.yandextodo.di.module.AppModule
import com.xml.yandextodo.di.module.DatabaseModule
import com.xml.yandextodo.di.module.NetworkModule
import com.xml.yandextodo.di.module.RepositoryModule
import com.xml.yandextodo.di.module.UseCaseModule
import com.xml.yandextodo.di.module.ViewModelModule
import com.xml.yandextodo.presentation.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        ViewModelModule::class,
    ]
)
interface ApplicationComponent {
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): ApplicationComponent
    }
}