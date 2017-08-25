package com.adammcneilly.cashcaretaker.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

/**
 * Transaction entity for an account
 */
@Entity(foreignKeys = arrayOf(ForeignKey(entity = Account::class, parentColumns = arrayOf("name"), childColumns = arrayOf("accountName"), onDelete = ForeignKey.CASCADE)))
data class Transaction(
        var accountName: String = "",
        var amount: Double = 0.0,
        var withdrawal: Boolean = true,
        @PrimaryKey(autoGenerate = true) var id: Long = 0
)