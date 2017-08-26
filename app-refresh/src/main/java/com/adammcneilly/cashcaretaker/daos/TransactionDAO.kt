package com.adammcneilly.cashcaretaker.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.adammcneilly.cashcaretaker.entities.Transaction
import io.reactivex.Flowable

/**
 * Database access for Transaction entities.
 */
@Dao
interface TransactionDAO {
    @Query("SELECT * FROM transactionTable")
    fun getAll(): Flowable<List<Transaction>>

    @Query("SELECT * FROM transactionTable WHERE accountName = :arg0")
    fun getAllForAccount(accountName: String): Flowable<List<Transaction>>

    @Insert
    fun insert(transactions: List<Transaction>): List<Long>

    @Delete
    fun delete(transaction: Transaction): Int
}