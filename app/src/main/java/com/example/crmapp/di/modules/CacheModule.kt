package com.example.crmapp.di.modules

import com.example.crmapp.domain.cache.ContactCacheManager
import org.koin.dsl.module

val cacheModule = module {
    single<ContactCacheManager> {
        ContactCacheManager(get())
    }
}