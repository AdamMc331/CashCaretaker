package com.androidessence.cashcaretaker.di

import android.content.Context

interface CashCaretakerGraph {
    val dataGraph: DataGraph
    val storeGraph: StoreGraph
    val viewModelFactoryGraph: ViewModelFactoryGraph
}

class BaseCashCaretakerGraph(
    context: Context
) : CashCaretakerGraph {
    override val dataGraph: DataGraph = SQLiteDatabaseGraph(context)

    override val storeGraph: StoreGraph = BaseStoreGraph(dataGraph)

    override val viewModelFactoryGraph: ViewModelFactoryGraph = BaseViewModelFactoryGraph(storeGraph, dataGraph)
}
