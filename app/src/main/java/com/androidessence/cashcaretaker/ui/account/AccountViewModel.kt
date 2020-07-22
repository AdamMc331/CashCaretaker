package com.androidessence.cashcaretaker.ui.account

import androidx.databinding.BaseObservable
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.util.asCurrency

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
        get() = account?.name.orEmpty()

    val balanceString: String
        get() = account?.balance?.asCurrency().orEmpty()

    val textColorResource: Int?
        get() {
            val balance = account?.balance ?: 0.0
            val isNegative = balance < 0.0
            return if (isNegative) R.color.mds_red_500 else null
        }
}
