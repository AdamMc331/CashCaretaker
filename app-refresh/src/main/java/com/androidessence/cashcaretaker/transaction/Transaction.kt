package com.androidessence.cashcaretaker.transaction

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.androidessence.cashcaretaker.account.Account
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Transaction entity for an account
 */
@Parcelize
@Entity(tableName = "transactionTable", foreignKeys = [(ForeignKey(entity = Account::class, parentColumns = arrayOf("name"), childColumns = arrayOf("accountName"), onDelete = ForeignKey.CASCADE))])
data class Transaction(
        var accountName: String = "",
        var description: String = "",
        var amount: Double = 0.0,
        var withdrawal: Boolean = true,
        var date: Date = Date(),
        @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable