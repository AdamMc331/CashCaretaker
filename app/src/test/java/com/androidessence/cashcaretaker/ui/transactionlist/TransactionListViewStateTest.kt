package com.androidessence.cashcaretaker.ui.transactionlist

import com.androidessence.cashcaretaker.core.models.Transaction
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TransactionListViewStateTest {
    private val defaultViewState = TransactionListViewState(
        showLoading = false,
        accountName = "",
        transactions = emptyList()
    )

    @Test
    fun hideContentWhileLoading() {
        val testTransaction = Transaction()
        val transactionList = listOf(testTransaction)

        val viewState = defaultViewState.copy(
            transactions = transactionList,
            showLoading = true,
        )

        assertThat(viewState.showContent).isFalse()
    }

    @Test
    fun hideContentWhenNoTransactionsAndNotLoading() {
        val viewState = defaultViewState.copy(
            transactions = emptyList(),
            showLoading = false,
        )

        assertThat(viewState.showContent).isFalse()
    }

    @Test
    fun showContentWhenHasTransactionsAndNotLoading() {
        val testTransaction = Transaction()
        val transactionList = listOf(testTransaction)

        val viewState = defaultViewState.copy(
            transactions = transactionList,
            showLoading = false,
        )

        assertThat(viewState.showContent).isTrue()
    }

    @Test
    fun hideEmptyStateWhileLoading() {
        val viewState = defaultViewState.copy(
            transactions = emptyList(),
            showLoading = true,
        )

        assertThat(viewState.showEmptyState).isFalse()
    }

    @Test
    fun hideEmptyStateWhenHasTransactionsAndNotLoading() {
        val testTransaction = Transaction()
        val transactionList = listOf(testTransaction)

        val viewState = defaultViewState.copy(
            transactions = transactionList,
            showLoading = false,
        )

        assertThat(viewState.showEmptyState).isFalse()
    }

    @Test
    fun showEmptyStateWhenNoTransactionsAndNotLoading() {
        val viewState = defaultViewState.copy(
            transactions = emptyList(),
            showLoading = false,
        )

        assertThat(viewState.showEmptyState).isTrue()
    }
}
