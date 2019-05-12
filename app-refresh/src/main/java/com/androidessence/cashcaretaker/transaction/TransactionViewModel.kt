package com.androidessence.cashcaretaker.transaction

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.androidessence.cashcaretaker.R
import com.androidessence.utility.asCurrency
import com.androidessence.utility.asUIString

class TransactionViewModel : BaseObservable() {
    var transaction: Transaction? = null
        set(value) {
            field = value
            notifyChange()
        }

    val description: String
    @Bindable get() {
        val description = transaction?.description.orEmpty()

        return if (description.isEmpty()) {
            "N/A"
        } else {
            description
        }
    }

    val dateString: String
        @Bindable get() = transaction?.date?.asUIString().orEmpty()

    val amount: String
        @Bindable get() = transaction?.amount?.asCurrency().orEmpty()

    val indicatorColorResource: Int
        @Bindable get() = if (transaction?.withdrawal == true) R.color.mds_red_500 else R.color.mds_green_500
}