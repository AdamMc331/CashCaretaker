package com.androidessence.cashcaretaker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.androidessence.cashcaretaker.account.Account
import io.reactivex.Flowable

/**
 * Database access for Account entities.
 */
@Dao
interface AccountDAO {
    @Query("SELECT * FROM account ORDER BY name")
    fun getAll(): Flowable<List<Account>>

    @Insert
    suspend fun insert(account: Account): Long

    @Delete
    suspend fun delete(account: Account): Int

    @Query("DELETE FROM account")
    suspend fun deleteAll(): Int
}