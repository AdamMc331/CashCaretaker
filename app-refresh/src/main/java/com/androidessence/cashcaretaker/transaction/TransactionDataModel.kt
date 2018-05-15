package com.androidessence.cashcaretaker.transaction

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.androidessence.cashcaretaker.R
import com.androidessence.utility.asCurrency
import com.androidessence.utility.asUIString

class TransactionDataModel : BaseObservable() {
    var transaction: Transaction? = null
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun getDescription(): String = transaction?.description.orEmpty()

    @Bindable
    fun getDateString(): String = transaction?.date?.asUIString().orEmpty()

    @Bindable
    fun getAmount(): String = transaction?.amount?.asCurrency().orEmpty()

    @Bindable
    fun getIndicatorColorResource(): Int =
            if (transaction?.withdrawal == true) R.color.mds_red_500 else R.color.mds_green_500
}