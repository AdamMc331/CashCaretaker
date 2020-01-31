package com.androidessence.cashcaretaker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.androidessence.cashcaretaker.transaction.Transaction

/**
 * Database access for Transaction entities.
 */
@Dao
interface TransactionDAO {
    @Query("SELECT * FROM transactionTable WHERE accountName = :accountName ORDER BY date DESC")
    fun getAllForAccount(accountName: String): LiveData<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction): Long

    @Update
    suspend fun update(transaction: Transaction): Int

    @Delete
    suspend fun delete(transaction: Transaction): Int

    @androidx.room.Transaction
    suspend fun transfer(withdrawal: Transaction, deposit: Transaction) {
        insert(withdrawal)
        insert(deposit)
    }
}
