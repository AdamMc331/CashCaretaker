package com.androidessence.cashcaretaker.transaction

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.androidessence.cashcaretaker.account.Account
import java.util.*

/**
 * Transaction entity for an account
 */
@Entity(tableName = "transactionTable", foreignKeys = arrayOf(ForeignKey(entity = Account::class, parentColumns = arrayOf("name"), childColumns = arrayOf("accountName"), onDelete = ForeignKey.CASCADE)))
data class Transaction(
        var accountName: String = "",
        var description: String = "",
        var amount: Double = 0.0,
        var withdrawal: Boolean = true,
        var date: Date = Date(),
        @PrimaryKey(autoGenerate = true) var id: Long = 0
)