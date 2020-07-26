package com.androidessence.cashcaretaker.ui.transactionlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androidessence.cashcaretaker.CoroutinesTestRule
import com.androidessence.cashcaretaker.core.models.Transaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TransactionListViewModelTest {
    private lateinit var testRobot: TransactionListViewModelRobot

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
        testRobot = TransactionListViewModelRobot()
    }

    @Test
    fun fetchValidTransactions() {
        val testTransactions = listOf(
            Transaction(
                description = "Test Transaction"
            )
        )

        testRobot
            .mockTransactions(testTransactions)
            .buildViewModel("")
            .assertTransactions(testTransactions)
            .assertShowTransactions(true)
            .assertShowLoading(false)
            .assertShowEmptyMessage(false)
    }

    @Test
    fun fetchEmptyTransactionList() {
        testRobot
            .mockTransactions(emptyList())
            .buildViewModel("")
            .assertTransactions(emptyList())
            .assertShowTransactions(false)
            .assertShowLoading(false)
            .assertShowEmptyMessage(true)
    }
}
