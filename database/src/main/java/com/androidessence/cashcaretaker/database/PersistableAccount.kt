package com.androidessence.cashcaretaker.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [(Index("name"))],
    tableName = "Account"
)
data class PersistableAccount(
    @PrimaryKey(autoGenerate = false) val name: String = "",
    val balance: Double = 0.0
)