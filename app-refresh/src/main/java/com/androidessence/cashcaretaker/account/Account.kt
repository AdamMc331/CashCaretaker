package com.androidessence.cashcaretaker.account

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * A bank account that the user may have.
 *
 * @property[name] The name of the account.
 * @property[balance] The amount of funds in the account.
 */
@Entity(indices = [(Index("name"))])
data class Account(
        @PrimaryKey(autoGenerate = false) var name: String = "",
        var balance: Double = 0.0)