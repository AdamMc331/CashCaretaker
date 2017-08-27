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
        addAccountView?.onInserted(ids)
        addAccountView?.hideProgress()
    }

    override fun onInsertConflict() {
        addAccountView?.onInsertConflict()
        addAccountView?.hideProgress()
    }

    override fun onAccountNameError() {
        addAccountView?.showAccountNameError()
        addAccountView?.hideProgress()
    }

    override fun onAccountBalanceError() {
        addAccountView?.showAccountBalanceError()
        addAccountView?.hideProgress()
    }
}