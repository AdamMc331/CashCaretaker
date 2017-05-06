package com.androidessence.cashcaretaker

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.base.DefaultFailureHandler
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.androidessence.cashcaretaker.TestUtils.takeScreenshot
import com.androidessence.cashcaretaker.activities.AccountsActivity
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.cashcaretaker.dataTransferObjects.Account
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests creating an account

 * Created by adam.mcneilly on 11/17/16.
 */
@RunWith(AndroidJUnit4::class)
class AccountTest {

    @JvmField @Rule
    var activityTestRule = ActivityTestRule(AccountsActivity::class.java)

    @Before
    fun setup() {
        activityTestRule.activity.contentResolver.delete(
                CCContract.AccountEntry.CONTENT_URI, null, null
        )

        Espresso.setFailureHandler { error, viewMatcher ->
            takeScreenshot("test_failed")
            DefaultFailureHandler(activityTestRule.activity).handle(error, viewMatcher)
        }
    }

    @After
    fun teardown() {
        activityTestRule.activity.contentResolver.delete(
                CCContract.AccountEntry.CONTENT_URI, null, null
        )
    }

    @Test
    fun testAddAccount() {
        AccountRobot()
                .newAccount()
                .accountName(VALID_ACCOUNT_NAME)
                .startingBalance(VALID_STARTING_BALANCE)
                .submit()

        // Match checking
        onView(withId(R.id.account_name)).check(matches(withText(VALID_ACCOUNT_NAME)))
        onView(withId(R.id.account_balance)).check(matches(withText(VALID_ACCOUNT_DISPLAY_BALANCE)))
        takeScreenshot("account_displayed")
    }

    @Test
    fun testEmptyAccountNameError() {
        AccountRobot()
                .newAccount()
                .startingBalance(VALID_STARTING_BALANCE)
                .submit()
                .assertAccountNameEmptyError()
    }

    @Test
    fun testEmptyStartingBalanceError() {
        AccountRobot()
                .newAccount()
                .accountName(VALID_ACCOUNT_NAME)
                .submit()
                .assertBalanceEmptyError()
    }

    @Test
    fun testAccountExistsError() {
        // Insert test account
        val account = Account(VALID_ACCOUNT_NAME, java.lang.Double.parseDouble(VALID_STARTING_BALANCE))

        activityTestRule.activity.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, account.getContentValues())

        // Try to create
        AccountRobot()
                .newAccount()
                .accountName(VALID_ACCOUNT_NAME)
                .startingBalance(VALID_STARTING_BALANCE)
                .submit()
                .assertAccountNameExistsError()
    }

    companion object {
        private val VALID_ACCOUNT_NAME = "Checking"
        private val VALID_STARTING_BALANCE = "1000.00"
        private val VALID_ACCOUNT_DISPLAY_BALANCE = "$1,000.00"
    }
}
