package com.androidessence.cashcaretaker.core.di

import android.content.Context

interface CashCaretakerGraph {
    val dataGraph: DataGraph
    val viewModelFactoryGraph: ViewModelFactoryGraph
}

class BaseCashCaretakerGraph(
    context: Context
) : CashCaretakerGraph {
    override val dataGraph: DataGraph = SQLiteDatabaseGraph(context)

    override val viewModelFactoryGraph: ViewModelFactoryGraph = BaseViewModelFactoryGraph(dataGraph)
}
