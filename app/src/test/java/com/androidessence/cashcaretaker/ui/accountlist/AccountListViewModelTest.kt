package com.androidessence.cashcaretaker.ui.accountlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androidessence.cashcaretaker.CoroutinesTestRule
import com.androidessence.cashcaretaker.core.models.Account
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AccountListViewModelTest {
    private lateinit var testRobot: AccountListViewModelRobot

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
        testRobot = AccountListViewModelRobot()
    }

    @Test
    fun fetchValidAccounts() {
        val accounts = listOf(
            Account(name = "Test Account")
        )

        testRobot
            .mockAccounts(accounts)
            .buildViewModel()
            .assertAccounts(accounts)
            .assertShowingAccounts(true)
            .assertShowLoading(false)
            .assertShowingEmptyMessage(false)
    }

    @Test
    fun fetchEmptyAccounts() {
        testRobot
            .mockAccounts(emptyList())
            .buildViewModel()
            .assertAccounts(emptyList())
            .assertShowingAccounts(false)
            .assertShowLoading(false)
            .assertShowingEmptyMessage(true)
    }
}
