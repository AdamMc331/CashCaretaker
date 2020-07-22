package com.androidessence.cashcaretaker.ui.account

import com.androidessence.cashcaretaker.core.models.Account
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountTest {
    @Test
    fun toStringConversion() {
        val defaultAccount = Account()
        assertEquals(" ($0.00)", defaultAccount.toString())

        val positiveAccount = Account("Test", 100.0)
        assertEquals("Test ($100.00)", positiveAccount.toString())

        val negativeAccount = Account("Test", -100.0)
        assertEquals("Test (($100.00))", negativeAccount.toString())
    }
}
