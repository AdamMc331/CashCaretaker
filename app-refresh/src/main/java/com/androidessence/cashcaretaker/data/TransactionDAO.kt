package com.androidessence.cashcaretaker.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.androidessence.cashcaretaker.transaction.Transaction
import io.reactivex.Flowable

/**
 * Database access for Transaction entities.
 */
@Dao
interface TransactionDAO {
    @Query("SELECT * FROM transactionTable ORDER BY date DESC")
    fun getAll(): Flowable<List<Transaction>>

    @Query("SELECT * FROM transactionTable WHERE accountName = :arg0 ORDER BY date DESC")
    fun getAllForAccount(accountName: String): Flowable<List<Transaction>>

    @Insert
    fun insert(transactions: List<Transaction>): List<Long>

    @Delete
    fun delete(transaction: Transaction): Int
}