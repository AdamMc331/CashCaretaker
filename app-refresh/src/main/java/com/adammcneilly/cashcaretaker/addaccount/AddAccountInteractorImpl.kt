package com.adammcneilly.cashcaretaker.addaccount

import android.database.sqlite.SQLiteConstraintException
import com.adammcneilly.cashcaretaker.App
import com.adammcneilly.cashcaretaker.data.CCDatabase
import com.adammcneilly.cashcaretaker.account.Account
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Implementation of [AddAccountInteractor].
 */
class AddAccountInteractorImpl: AddAccountInteractor {
    override fun insert(accounts: List<Account>): List<Long> {
        return CCDatabase.getInMemoryDatabase(App.instance)
                .accountDao()
                .insert(accounts)
    }
}