package com.androidessence.cashcaretaker.core.models

import android.os.Parcelable
import com.androidessence.cashcaretaker.database.PersistableTransaction
import java.util.Date
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(
    val accountName: String = "",
    val description: String = "",
    val amount: Double = 0.0,
    val withdrawal: Boolean = true,
    val date: Date = Date(),
    val id: Long = 0
) : Parcelable {

    fun toPersistableTransaction(): PersistableTransaction {
        return PersistableTransaction(
            accountName = this.accountName,
            description = this.description,
            amount = this.amount,
            withdrawal = this.withdrawal,
            date = this.date
        )
    }
}

fun PersistableTransaction.toTransaction(): Transaction {
    return Transaction(
        accountName = this.accountName,
        description = this.description,
        amount = this.amount,
        withdrawal = this.withdrawal,
        date = this.date
    )
}
