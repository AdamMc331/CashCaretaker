package com.androidessence.cashcaretaker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.androidessence.cashcaretaker.transaction.Transaction
import io.reactivex.Flowable

/**
 * Database access for Transaction entities.
 */
@Dao
interface TransactionDAO {
    @Query("SELECT * FROM transactionTable WHERE accountName = :accountName ORDER BY date DESC")
    fun getAllForAccount(accountName: String): Flowable<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction): Long

    @Update
    suspend fun update(transaction: Transaction): Int

    @Delete
    suspend fun delete(transaction: Transaction): Int
}