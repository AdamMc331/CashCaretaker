package com.androidessence.cashcaretaker.core.di

interface CashCaretakerGraph {
    val viewModelFactoryGraph: ViewModelFactoryGraph
}

class BaseCashCaretakerGraph() : CashCaretakerGraph {

    override val viewModelFactoryGraph: ViewModelFactoryGraph = BaseViewModelFactoryGraph()
}
