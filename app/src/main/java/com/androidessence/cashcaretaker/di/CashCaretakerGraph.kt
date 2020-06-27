package com.androidessence.cashcaretaker.di

import android.content.Context

interface CashCaretakerGraph {
    val dataGraph: DataGraph
}

class BaseCashCaretakerGraph(
    context: Context
) : CashCaretakerGraph {
    override val dataGraph: DataGraph = SQLiteDatabaseGraph(context)
}
