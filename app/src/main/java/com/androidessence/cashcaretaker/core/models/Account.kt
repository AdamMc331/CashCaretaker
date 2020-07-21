package com.androidessence.cashcaretaker.core.models

import com.androidessence.cashcaretaker.database.PersistableAccount

data class Account(
    val name: String = "",
    val balance: Double = 0.0
) {
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
