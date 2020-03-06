package com.androidessence.cashcaretaker.account

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.androidessence.cashcaretaker.database.PersistableAccount
import com.androidessence.cashcaretaker.util.asCurrency

/**
 * POKO for a user's bank account. This is the entity that will be saved in the database.
 */
@Entity(indices = [(Index("name"))])
data class Account(
    @PrimaryKey(autoGenerate = false) var name: String = "",
    var balance: Double = 0.0
) {
    override fun toString(): String = "$name (${balance.asCurrency()})"

    fun toPersistableAccount(): PersistableAccount {
        return PersistableAccount(
            name = this.name,
            balance = this.balance
        )
    }
}

fun PersistableAccount.toAccount(): Account {
    return Account(
        name = this.name,
        balance = this.balance
    )
}