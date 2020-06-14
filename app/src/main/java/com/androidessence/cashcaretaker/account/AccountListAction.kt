package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.redux.Action
import kotlinx.coroutines.CoroutineScope

sealed class AccountListAction : Action {
    data class FetchAccounts(val scope: CoroutineScope) : AccountListAction()
    object AccountsLoading : AccountListAction()
    data class LoadedAccounts(val accounts: List<Account>) : AccountListAction()
}
