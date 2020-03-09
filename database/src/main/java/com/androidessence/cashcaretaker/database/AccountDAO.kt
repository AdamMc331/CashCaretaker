package com.androidessence.cashcaretaker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
internal interface AccountDAO {
    @Query("SELECT * FROM account ORDER BY name")
    fun getAll(): LiveData<List<PersistableAccount>>

    @Insert
    suspend fun insert(account: PersistableAccount): Long

    @Delete
    suspend fun delete(account: PersistableAccount): Int

    @Query("DELETE FROM account")
    suspend fun deleteAll(): Int
}
