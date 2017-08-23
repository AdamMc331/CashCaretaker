package com.adammcneilly.cashcaretaker.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.adammcneilly.cashcaretaker.daos.AccountDAO
import com.adammcneilly.cashcaretaker.entities.Account

/**
 * Database class for the CC application.
 */
@Database(entities = arrayOf(Account::class), version = 1)
abstract class CCDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDAO

    companion object {
	        private var INSTANCE: CCDatabase? = null
	            private set

	        fun getInMemoryDatabase(context: Context): CCDatabase {
	            if (INSTANCE == null) {
	                INSTANCE = Room.databaseBuilder(context,
	                        CCDatabase::class.java, "cashcaretaker.db")
	                        .build()
	            }

	            return INSTANCE!!
	        }
	    }
}