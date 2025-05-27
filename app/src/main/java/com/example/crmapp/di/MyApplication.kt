package com.example.crmapp.di

import android.app.Application
import com.example.crmapp.di.modules.cacheModule
import com.example.crmapp.di.modules.networkModule
import com.example.crmapp.di.modules.repositoryModule
import com.example.crmapp.di.modules.securityModule
import com.example.crmapp.di.modules.stateModule
import com.example.crmapp.di.modules.useCaseModule
import com.example.crmapp.di.modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level


class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            //Why are we declaring android context?
            androidContext(this@MyApplication)
            modules(listOf(
                securityModule,
                stateModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
                networkModule,
                cacheModule
            ))
        }
    }
}