package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.R
import com.androidessence.utility.asCurrency
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountViewModelTest {
    @Test
    fun getName() {
        val testName = "testName"
        val account = Account(name = testName)
        val viewModel = AccountViewModel().apply {
            this.account = account
        }

        assertEquals(viewModel.name, testName)
    }

    @Test
    fun getBalance() {
        val testBalance = 5.00
        val account = Account(balance = testBalance)
        val viewModel = AccountViewModel().apply {
            this.account = account
        }

        assertEquals(testBalance.asCurrency(), viewModel.balanceString)
    }

    @Test
    fun getPositiveBalanceTextColorRes() {
        val balance = 5.00
        val account = Account(balance = balance)
        val viewModel = AccountViewModel().apply {
            this.account = account
        }

        assertEquals(null, viewModel.textColorResource)
    }

    @Test
    fun getNegativeBalanceTextColorRes() {
        val balance = -5.00
        val account = Account(balance = balance)
        val viewModel = AccountViewModel().apply {
            this.account = account
        }

        assertEquals(R.color.mds_red_500, viewModel.textColorResource)
    }
}
