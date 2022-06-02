package com.example.myfilms.presentation.common

import android.app.Application
import com.example.myfilms.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

}