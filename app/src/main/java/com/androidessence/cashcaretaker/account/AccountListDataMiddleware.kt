package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.redux.Action
import com.androidessence.cashcaretaker.redux.Middleware
import com.androidessence.cashcaretaker.redux.NextDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class AccountListDataMiddleware(
    val repository: CCRepository
) : Middleware {
    override fun dispatch(action: Action, next: NextDispatcher) {
        when (action) {
            is AccountListAction.FetchAccounts -> {
                next.dispatch(AccountListAction.AccountsLoading)

                requestAccounts(action, next)
            }
            else -> next.dispatch(action)
        }
    }

    private fun requestAccounts(action: AccountListAction.FetchAccounts, next: NextDispatcher) {
        action.scope.launch {
            repository.fetchAllAccounts().collect { accounts ->
                next.dispatch(AccountListAction.LoadedAccounts(accounts))
            }
        }
    }
}
