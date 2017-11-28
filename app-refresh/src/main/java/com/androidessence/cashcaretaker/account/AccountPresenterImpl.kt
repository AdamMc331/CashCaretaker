package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.entity.EntityPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for accounts.
 */
class AccountPresenterImpl(private var accountView: AccountController?, private val accountInteractor: AccountInteractor) : EntityPresenter<Account> {

    override fun onAttach() {
        accountView?.showProgress()
        accountInteractor.getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onFetched(it) })
    }

    override fun onDestroy() {
        accountView = null
    }

    override fun onFetched(entities: List<Account>) {
        accountView?.setAccounts(entities)
        accountView?.hideProgress()
    }
}