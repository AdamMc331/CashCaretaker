package com.adammcneilly.cashcaretaker.account

/**
 * Implementation of the presenter for accounts.
 */
class AccountPresenterImpl(accountView: AccountView, private val accountInteractor: AccountInteractor) : AccountPresenter, AccountInteractor.OnFinishedListener {

    private var accountView: AccountView? = accountView
        private set

    override fun onResume() {
        accountView?.showProgress()
        accountInteractor.getAll(this)
    }

    override fun onDestroy() {
        accountView = null
    }

    override fun onFetched(accounts: List<Account>) {
        accountView?.setAccounts(accounts)
        accountView?.hideProgress()
    }

    override fun onAddClicked() {
        accountView?.navigateToAddAccount()
    }
}