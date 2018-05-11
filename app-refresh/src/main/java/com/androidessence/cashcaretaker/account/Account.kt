package com.androidessence.cashcaretaker.account

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.androidessence.utility.asCurrency

/**
 * POKO for a user's bank account.
 */
@Entity(indices = [(Index("name"))])
data class Account(
        @PrimaryKey(autoGenerate = false) var name: String = "",
        var balance: Double = 0.0) {

    override fun toString(): String = "$name (${balance.asCurrency()})"
}