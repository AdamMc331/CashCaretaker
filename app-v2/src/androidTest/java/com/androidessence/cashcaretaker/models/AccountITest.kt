package com.androidessence.cashcaretaker.models

import android.os.Parcel
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests the Account class.
 *
 * Created by adam.mcneilly on 3/5/17.
 */
class AccountITest {
    @Test
    fun testParcel() {
        val account = Account()
        account.name = "Checking"
        account.balance = 100.00

        val parcel = Parcel.obtain()
        account.writeToParcel(parcel, 0)

        parcel.setDataPosition(0)

        val testAccount = Account.CREATOR.createFromParcel(parcel)
        assertEquals(account, testAccount)
    }
}