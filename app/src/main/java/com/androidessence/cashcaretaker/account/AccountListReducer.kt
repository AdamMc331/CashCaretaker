package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.redux.Action
import com.androidessence.cashcaretaker.redux.Reducer

class AccountListReducer : Reducer<AccountListState> {
    override fun reduce(state: AccountListState, action: Action): AccountListState {
        return when (action) {
            AccountListAction.AccountsLoading -> AccountListState.loading()
            is AccountListAction.LoadedAccounts -> AccountListState.success(action.accounts)
            else -> state
        }
    }
}
