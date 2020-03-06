package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.database.PersistableAccount
import com.androidessence.cashcaretaker.util.asCurrency

/**
 * POKO for a user's bank account.
 */
data class Account(
    val name: String = "",
    val balance: Double = 0.0
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
