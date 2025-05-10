package com.example.crmapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import com.example.crmapp.di.appModule
import com.example.crmapp.di.networkModule
import com.example.crmapp.di.repositoryModule
import com.example.crmapp.di.useCaseModule
import com.example.crmapp.di.viewModelModule


class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            //Why are we declaring android context?
            androidContext(this@MyApplication)
            modules(listOf(
                appModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
                networkModule
            ))
        }
    }
}