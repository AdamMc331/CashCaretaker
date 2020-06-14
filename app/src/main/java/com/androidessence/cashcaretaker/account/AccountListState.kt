package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.redux.State

data class AccountListState(
    val loading: Boolean,
    val data: List<Account>
) : State {
    companion object {
        fun loading(): AccountListState {
            return AccountListState(
                loading = true,
                data = emptyList()
            )
        }

        fun success(data: List<Account>): AccountListState {
            return AccountListState(
                loading = false,
                data = data
            )
        }
    }
}
