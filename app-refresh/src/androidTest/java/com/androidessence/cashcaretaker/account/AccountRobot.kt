package com.androidessence.cashcaretaker.account

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.RecyclerViewItemCountAssertion.Companion.withItemCount
import com.androidessence.cashcaretaker.TestUtils.Companion.clickItemInRecyclerView
import com.androidessence.cashcaretaker.TestUtils.Companion.matchTextInRecyclerView

/**
 * Robot for manipulating the account screen.
 */
class AccountRobot {
    fun clickNew(): AccountRobot {
        onView(ADD_BUTTON_MATCHER).perform(click())
        return this
    }

    fun accountName(name: String): AccountRobot {
        onView(ACCOUNT_NAME_MATCHER).perform(clearText(), typeText(name), closeSoftKeyboard())
        return this
    }

    fun accountBalance(balance: String): AccountRobot {
        onView(ACCOUNT_BALANCE_MATCHER).perform(clearText(), typeText(balance), closeSoftKeyboard())
        return this
    }

    fun submit(): AccountRobot {
        onView(SUBMIT_BUTTON_MATCHER).perform(click())
        return this
    }

    fun assertListCount(expectedCount: Int): AccountRobot {
        onView(RECYCLER_VIEW_MATCHER).check(withItemCount(expectedCount))
        return this
    }

    fun assertAccountNameError(error: String): AccountRobot {
        onView(ACCOUNT_NAME_MATCHER).check(matches(hasErrorText(error)))
        return this
    }

    fun assertAccountBalanceError(error: String): AccountRobot {
        onView(ACCOUNT_BALANCE_MATCHER).check(matches(hasErrorText(error)))
        return this
    }

    fun assertAccountNameInList(name: String, position: Int): AccountRobot {
        matchTextInRecyclerView<AccountAdapter.AccountViewHolder>(name, RECYCLER_VIEW_ID, ACCOUNT_NAME_ID, position)
        return this
    }

    fun assertAccountBalanceInList(balance: String, position: Int): AccountRobot {
        matchTextInRecyclerView<AccountAdapter.AccountViewHolder>(balance, RECYCLER_VIEW_ID, ACCOUNT_BALANCE_ID, position)
        return this
    }

    fun clickWithdrawalInList(position: Int): AccountRobot {
        clickItemInRecyclerView<AccountAdapter.AccountViewHolder>(RECYCLER_VIEW_ID, WITHDRAWAL_BUTTON_ID, position)
        return this
    }

    fun clickDepositInList(position: Int): AccountRobot {
        clickItemInRecyclerView<AccountAdapter.AccountViewHolder>(RECYCLER_VIEW_ID, DEPOSIT_BUTTON_ID, position)
        return this
    }

    fun longClick(position: Int): AccountRobot {
        onView(RECYCLER_VIEW_MATCHER).perform(RecyclerViewActions.actionOnItemAtPosition<AccountAdapter.AccountViewHolder>(position, longClick()))
        return this
    }

    fun delete(): AccountRobot {
        onView(DELETE_BUTTON_MATCH).perform(click())
        return this
    }

    companion object {
        private const val RECYCLER_VIEW_ID = R.id.accountsRecyclerView
        private const val ACCOUNT_NAME_ID = R.id.accountNameEditText
        private const val ACCOUNT_BALANCE_ID = R.id.accountBalanceEditText
        private const val WITHDRAWAL_BUTTON_ID = R.id.withdrawal_button
        private const val DEPOSIT_BUTTON_ID = R.id.deposit_button

        private val ADD_BUTTON_MATCHER = withId(R.id.addAccountButton)
        private val RECYCLER_VIEW_MATCHER = withId(RECYCLER_VIEW_ID)
        private val ACCOUNT_NAME_MATCHER = withId(ACCOUNT_NAME_ID)
        private val ACCOUNT_BALANCE_MATCHER = withId(ACCOUNT_BALANCE_ID)
        private val SUBMIT_BUTTON_MATCHER = withId(R.id.submitButton)
        private val DELETE_BUTTON_MATCH = withId(R.id.action_delete)
    }
}