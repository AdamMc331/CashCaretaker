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
    @Query("SELECT * FROM transactionTable ORDER BY date DESC")
    fun getAll(): Flowable<List<Transaction>>

    @Query("SELECT * FROM transactionTable WHERE accountName = :accountName ORDER BY date DESC")
    fun getAllForAccount(accountName: String): Flowable<List<Transaction>>

    @Insert
    fun insert(transaction: Transaction): Long

    @Update
    fun update(transaction: Transaction): Int

    @Delete
    fun delete(transaction: Transaction): Int
}