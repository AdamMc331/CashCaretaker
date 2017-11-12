package com.adammcneilly.cashcaretaker.account

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for accounts.
 */
class AccountPresenterImpl(accountView: AccountController, private val accountInteractor: AccountInteractor) : AccountPresenter {

    private var accountView: AccountController? = accountView
        private set

    override fun onResume() {
        accountView?.showProgress()
        accountInteractor.getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onFetched(it) })
    }

    override fun onDestroy() {
        accountView = null
    }

    override fun onFetched(accounts: List<Account>) {
        accountView?.setAccounts(accounts)
        accountView?.hideProgress()
    }
}