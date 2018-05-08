package com.androidessence.cashcaretaker.transaction

import android.databinding.BaseObservable
import android.databinding.Bindable
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
    fun getIndicatorColorResource(): Int {
        val amount = transaction?.amount ?: 0.0
        val isNegative = amount < 0.0
        return if (isNegative) R.color.mds_red_500 else R.color.mds_black
    }
}