package com.androidessence.cashcaretaker.ui.accountlist

import com.androidessence.cashcaretaker.core.models.Account
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AccountListViewStateTest {
    private val defaultViewState = AccountListViewState(
        showLoading = false,
        accounts = emptyList(),
    )

    @Test
    fun disallowTransfersWhenZeroAccounts() {
        val viewState = defaultViewState.copy(
            accounts = emptyList()
        )

        assertThat(viewState.allowTransfers).isFalse()
    }

    @Test
    fun disallowTransfersWhenOneAccount() {
        val testAccount = Account(name = "Testing")
        val accountList = listOf(testAccount)

        val viewState = defaultViewState.copy(
            accounts = accountList
        )

        assertThat(viewState.allowTransfers).isFalse()
    }

    @Test
    fun allowTransfersWhenTwoAccounts() {
        val firstAccount = Account(name = "Testing")
        val secondAccount = Account(name = "More Testing")
        val accountList = listOf(firstAccount, secondAccount)

        val viewState = defaultViewState.copy(
            accounts = accountList
        )

        assertThat(viewState.allowTransfers).isTrue()
    }

    @Test
    fun hideContentWhileLoading() {
        val testAccount = Account(name = "Testing")
        val accountList = listOf(testAccount)

        val viewState = defaultViewState.copy(
            accounts = accountList,
            showLoading = true,
        )

        assertThat(viewState.showContent).isFalse()
    }

    @Test
    fun hideContentWhenNoAccountsAndNotLoading() {
        val viewState = defaultViewState.copy(
            accounts = emptyList(),
            showLoading = false,
        )

        assertThat(viewState.showContent).isFalse()
    }

    @Test
    fun showContentWhenHasAccountsAndNotLoading() {
        val testAccount = Account(name = "Testing")
        val accountList = listOf(testAccount)

        val viewState = defaultViewState.copy(
            accounts = accountList,
            showLoading = false,
        )

        assertThat(viewState.showContent).isTrue()
    }

    @Test
    fun hideEmptyStateWhileLoading() {
        val viewState = defaultViewState.copy(
            accounts = emptyList(),
            showLoading = true,
        )

        assertThat(viewState.showEmptyState).isFalse()
    }

    @Test
    fun hideEmptyStateWhenHasAccountsAndNotLoading() {
        val testAccount = Account(name = "Testing")
        val accountList = listOf(testAccount)

        val viewState = defaultViewState.copy(
            accounts = accountList,
            showLoading = false,
        )

        assertThat(viewState.showEmptyState).isFalse()
    }

    @Test
    fun showEmptyStateWhenNoAccountsAndNotLoading() {
        val viewState = defaultViewState.copy(
            accounts = emptyList(),
            showLoading = false,
        )

        assertThat(viewState.showEmptyState).isTrue()
    }
}
