package com.adammcneilly.cashcaretaker.account

import com.adammcneilly.cashcaretaker.entity.EntityController
import com.adammcneilly.cashcaretaker.entity.EntityPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for accounts.
 */
class AccountPresenterImpl(private var accountController: EntityController<Account>?, private val accountInteractor: AccountInteractor) : EntityPresenter<Account> {

    override fun onAttach() {
        accountController?.showProgress()
        accountInteractor.getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onFetched(it) })
    }

    override fun onDestroy() {
        accountController = null
    }

    override fun onFetched(entities: List<Account>) {
        accountController?.showEntities(entities)
        accountController?.hideProgress()
    }
}