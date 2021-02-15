package com.androidessence.cashcaretaker.ui.accountlist

import com.androidessence.cashcaretaker.core.models.Account

data class AccountListViewState(
    val showLoading: Boolean,
    val accounts: List<Account>,
) {
    val allowTransfers: Boolean
        get() = accounts.size >= 2

    val showContent: Boolean
        get() = !showLoading && accounts.isNotEmpty()

    val showEmptyState: Boolean
        get() = !showLoading && accounts.isEmpty()

    companion object {
        fun loading(): AccountListViewState {
            return AccountListViewState(
                showLoading = true,
                accounts = emptyList()
            )
        }

        fun success(data: List<Account>): AccountListViewState {
            return AccountListViewState(
                showLoading = false,
                accounts = data
            )
        }
    }
}
