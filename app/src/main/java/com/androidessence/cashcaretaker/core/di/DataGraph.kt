package com.androidessence.cashcaretaker.core.di

import android.content.Context
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DatabaseService
import com.androidessence.cashcaretaker.database.RoomDatabase

interface DataGraph {
    val repository: CCRepository
}

class SQLiteDatabaseGraph(
    private val context: Context
) : DataGraph {
    override val repository: CCRepository by lazy {
        DatabaseService(
            database = RoomDatabase(context)
        )
    }
}
