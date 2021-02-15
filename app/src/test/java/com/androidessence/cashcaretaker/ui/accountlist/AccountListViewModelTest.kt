package com.androidessence.cashcaretaker.ui.accountlist

import com.androidessence.cashcaretaker.CoroutinesTestRule
import com.androidessence.cashcaretaker.core.models.Account
import org.junit.Rule
import org.junit.Test

class AccountListViewModelTest {
    private val testRobot = AccountListViewModelRobot()

    @JvmField
    @Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Test
    fun fetchValidAccounts() {
        val accounts = listOf(
            Account(name = "Test Account")
        )

        val expectedViewState = AccountListViewState.success(
            data = accounts,
        )

        testRobot
            .mockAccounts(accounts)
            .buildViewModel()
            .assertViewState(expectedViewState)
    }

    @Test
    fun fetchEmptyAccounts() {
        val expectedViewState = AccountListViewState.success(
            data = emptyList(),
        )

        testRobot
            .mockAccounts(emptyList())
            .buildViewModel()
            .assertViewState(expectedViewState)
    }
}
