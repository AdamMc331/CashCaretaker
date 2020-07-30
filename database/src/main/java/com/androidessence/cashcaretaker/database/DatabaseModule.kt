package com.androidessence.cashcaretaker.database

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<CCDatabase> {
        RoomDatabase(androidContext())
    }
}
