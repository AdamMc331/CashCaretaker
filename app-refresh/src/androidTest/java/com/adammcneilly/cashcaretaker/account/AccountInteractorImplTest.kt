package com.adammcneilly.cashcaretaker.account

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.adammcneilly.cashcaretaker.App
import com.adammcneilly.cashcaretaker.data.CCDatabase
import com.adammcneilly.cashcaretaker.main.MainActivity
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Tests the AccountInteractor.
 */
@RunWith(AndroidJUnit4::class)
class AccountInteractorImplTest {

    @JvmField @Rule val accountActivity = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        // Delete all accounts before starting
        CCDatabase.getInMemoryDatabase(App.instance)
                .accountDao()
                .deleteAll()
    }

    @Test
    fun getAll() {
        val countdown = CountDownLatch(1)

        val listener = object : AccountInteractor.OnFinishedListener {
            override fun onFetched(accounts: List<Account>) {
                assertTrue(accounts.isEmpty())
                countdown.countDown()
            }
        }

        val interactor = AccountInteractorImpl()
        interactor.getAll(listener)

        countdown.await(5, TimeUnit.SECONDS)
    }

}