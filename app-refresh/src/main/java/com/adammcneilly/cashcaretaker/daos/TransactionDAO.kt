package com.adammcneilly.cashcaretaker.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.adammcneilly.cashcaretaker.entities.Account
import com.adammcneilly.cashcaretaker.entities.Transaction
import io.reactivex.Flowable

/**
 * Database access for Transaction entities.
 */
@Dao
interface TransactionDAO {
    @Query("SELECT * FROM transaction")
    fun getAll(): Flowable<List<Transaction>>

    @Query("SELECT * FROM transaction WHERE accountName = :p0")
    fun getAllForAccount(accountName: String): Flowable<List<Account>>

    @Insert
    fun insert(transactions: List<Transaction>): List<Long>

    @Delete
    fun delete(transaction: Transaction): Int
}