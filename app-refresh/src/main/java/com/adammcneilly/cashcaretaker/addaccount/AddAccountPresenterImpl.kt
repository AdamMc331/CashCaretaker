package com.adammcneilly.cashcaretaker.addaccount

/**
 * Implementation of [AddAccountPresenter]
 */
class AddAccountPresenterImpl(addAccountView: AddAccountView, private val interactor: AddAccountInteractor): AddAccountPresenter, AddAccountInteractor.OnInsertedListener {
    private var addAccountView: AddAccountView? = addAccountView
        private set

    override fun onDestroy() {
        addAccountView = null
    }

    override fun insert(accountName: String, accountBalance: String) {
        addAccountView?.showProgress()
        interactor.insert(accountName, accountBalance, this)
    }

    override fun onInserted(ids: List<Long>) {
        addAccountView?.hideProgress()
    }

    override fun onInsertConflict() {
        addAccountView?.hideProgress()
        addAccountView?.onInsertConflict()
    }

    override fun onAccountNameError() {
        addAccountView?.hideProgress()
        addAccountView?.showAccountNameError()
    }

    override fun onAccountBalanceError() {
        addAccountView?.hideProgress()
        addAccountView?.showAccountBalanceError()
    }
}