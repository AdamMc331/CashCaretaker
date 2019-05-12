package com.androidessence.cashcaretaker.account

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.androidessence.cashcaretaker.R
import com.androidessence.utility.asCurrency

/**
 * Maintains a reference to an [account] and exposes the various fields required for data binding.
 */
class AccountViewModel : BaseObservable() {

    var account: Account? = null
        set(value) {
            field = value
            notifyChange()
        }

    val name: String
        @Bindable get() = account?.name.orEmpty()

    val balanceString: String
        @Bindable get() = account?.balance?.asCurrency().orEmpty()

    val textColorResource: Int
        @Bindable get() {
            val balance = account?.balance ?: 0.0
            val isNegative = balance < 0.0
            return if (isNegative) R.color.mds_red_500 else R.color.mds_black
        }
}