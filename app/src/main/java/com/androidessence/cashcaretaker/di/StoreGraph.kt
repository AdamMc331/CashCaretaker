package com.androidessence.cashcaretaker.di

import com.androidessence.cashcaretaker.account.AccountListDataMiddleware
import com.androidessence.cashcaretaker.account.AccountListReducer
import com.androidessence.cashcaretaker.account.AccountListState
import com.androidessence.cashcaretaker.logging.AndroidLogger
import com.androidessence.cashcaretaker.redux.LoggingMiddleware
import com.androidessence.cashcaretaker.redux.Store

interface StoreGraph {
    fun accountListStore(): Store<AccountListState>
}

class BaseStoreGraph(
    private val dataGraph: DataGraph
) : StoreGraph {
    override fun accountListStore(): Store<AccountListState> {
        return Store(
            initialState = AccountListState.loading(),
            reducer = AccountListReducer(),
            middlewares = listOf(
                LoggingMiddleware(AndroidLogger()),
                AccountListDataMiddleware(dataGraph.repository)
            )
        )
    }
}
