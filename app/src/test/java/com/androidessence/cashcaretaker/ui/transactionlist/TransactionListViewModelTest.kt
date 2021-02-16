package com.androidessence.cashcaretaker.ui.transactionlist

import com.androidessence.cashcaretaker.CoroutinesTestRule
import com.androidessence.cashcaretaker.core.models.Transaction
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TransactionListViewModelTest {
    private lateinit var testRobot: TransactionListViewModelRobot

    @JvmField
    @Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
        testRobot = TransactionListViewModelRobot()
    }

    @Test
    fun fetchValidTransactions() {
        val accountName = "Checking"

        val testTransactions = listOf(
            Transaction(
                description = "Test Transaction"
            )
        )

        val expectedViewState = TransactionListViewState(
            showLoading = false,
            accountName = accountName,
            transactions = testTransactions,
        )

        testRobot
            .mockTransactionsForAccount(
                accountName,
                testTransactions
            )
            .buildViewModel(accountName)
            .assertViewState(expectedViewState)
    }

    @Test
    fun fetchEmptyTransactionList() {
        val accountName = "Checking"

        val expectedViewState = TransactionListViewState(
            showLoading = false,
            accountName = accountName,
            transactions = emptyList(),
        )

        testRobot
            .mockTransactionsForAccount(
                accountName,
                emptyList()
            )
            .buildViewModel(accountName)
            .assertViewState(expectedViewState)
    }
}
