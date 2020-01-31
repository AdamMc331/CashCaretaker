package com.androidessence.cashcaretaker.transaction

import androidx.databinding.BaseObservable
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.util.asCurrency
import com.androidessence.cashcaretaker.util.asUIString

class TransactionViewModel : BaseObservable() {
    var transaction: Transaction? = null
        set(value) {
            field = value
            notifyChange()
        }

    val description: String
        get() {
            val description = transaction?.description.orEmpty()

            return when {
                description.isEmpty() -> NO_DESCRIPTION
                else -> description
            }
        }

    val dateString: String
        get() = transaction?.date?.asUIString().orEmpty()

    val amount: String
        get() = transaction?.amount?.asCurrency().orEmpty()

    val indicatorColorResource: Int
        get() = when {
            transaction?.withdrawal == true -> R.color.mds_red_500
            else -> R.color.mds_green_500
        }

    companion object {
        const val NO_DESCRIPTION = "N/A"
    }
}
