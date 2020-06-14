package com.androidessence.cashcaretaker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TransactionDAO {
    @Query("SELECT * FROM transactionTable WHERE accountName = :accountName ORDER BY date DESC")
    fun fetchTransactionsForAccount(accountName: String): Flow<List<PersistableTransaction>>

    @Insert
    suspend fun insert(transaction: PersistableTransaction): Long

    @Update
    suspend fun update(transaction: PersistableTransaction): Int

    @Delete
    suspend fun delete(transaction: PersistableTransaction): Int

    @androidx.room.Transaction
    suspend fun transfer(withdrawal: PersistableTransaction, deposit: PersistableTransaction) {
        insert(withdrawal)
        insert(deposit)
    }
}
