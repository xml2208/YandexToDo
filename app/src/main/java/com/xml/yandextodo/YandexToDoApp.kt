package com.xml.yandextodo

import android.app.Application
import com.xml.yandextodo.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class YandexToDoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            printLogger()
            androidContext(this@YandexToDoApp)
            modules(listOf(mainModule))
        }
    }
}