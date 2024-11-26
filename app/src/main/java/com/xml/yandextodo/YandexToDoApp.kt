package com.xml.yandextodo

import android.app.Application
import com.xml.yandextodo.di.component.ApplicationComponent
import com.xml.yandextodo.di.component.DaggerApplicationComponent
import com.xml.yandextodo.di.module.AppModule

class YandexToDoApp : Application() {

    lateinit var appComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}