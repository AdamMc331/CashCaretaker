package com.androidessence.cashcaretaker.account

data class AccountListState(
    val loading: Boolean,
    val data: List<Account>
) {
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
