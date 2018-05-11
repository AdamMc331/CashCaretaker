package com.androidessence.cashcaretaker.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
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
    fun insert(account: Account): Long

    @Delete
    fun delete(account: Account): Int

    @Query("DELETE FROM account")
    fun deleteAll(): Int
}