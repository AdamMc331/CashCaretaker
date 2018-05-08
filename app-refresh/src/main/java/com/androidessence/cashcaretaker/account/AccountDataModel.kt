package com.androidessence.cashcaretaker.account

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.androidessence.cashcaretaker.R
import com.androidessence.utility.asCurrency

class AccountDataModel : BaseObservable() {

    var account: Account? = null
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun getName(): String = account?.name.orEmpty()

    @Bindable
    fun getBalanceString(): String = account?.balance?.asCurrency().orEmpty()

    @Bindable
    fun getTextColorResource(): Int {
        val balance = account?.balance ?: 0.0
        val isNegative = balance < 0.0
        return if (isNegative) R.color.mds_red_500 else R.color.mds_black
    }
}