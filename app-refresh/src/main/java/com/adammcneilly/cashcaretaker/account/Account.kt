package com.adammcneilly.cashcaretaker.account

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * A bank account that the user may have.
 */
@Entity(indices = arrayOf(Index("name")))
data class Account(
        @PrimaryKey(autoGenerate = false) var name: String = "",
        var balance: Double = 0.0)