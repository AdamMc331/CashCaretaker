package com.androidessence.cashcaretaker.transaction

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.RecyclerViewItemCountAssertion
import com.androidessence.cashcaretaker.TestUtils.Companion.matchTextInRecyclerView
import com.androidessence.cashcaretaker.TestUtils.Companion.setChecked

/**
 * Robot for interacting with the Transaction view.
 */
class TransactionRobot {
    fun clickNew(): TransactionRobot {
        onView(ADD_BUTTON_MATCHER).perform(ViewActions.click())
        return this
    }

    fun submit(): TransactionRobot {
        onView(SUBMIT_BUTTON_MATCHER).perform(ViewActions.click())
        return this
    }

    fun assertListCount(expectedCount: Int): TransactionRobot {
        onView(RECYCLER_VIEW_MATCHER).check(RecyclerViewItemCountAssertion.withItemCount(expectedCount))
        return this
    }

    fun setWithdrawalSwitch(checked: Boolean): TransactionRobot {
        onView(WITHDRAWAL_SWITCH_MATCHER).perform(setChecked(checked))
        return this
    }

    fun assertWithdrawalSwitchState(checked: Boolean): TransactionRobot {
        val checkedMatcher = if (checked) isChecked() else isNotChecked()
        onView(WITHDRAWAL_SWITCH_MATCHER).check(matches(checkedMatcher))
        return this
    }

    fun transactionDescription(description: String): TransactionRobot {
        onView(TRANSACTION_DESCRIPTION_MATCHER).perform(clearText(), typeText(description), closeSoftKeyboard())
        return this
    }

    fun transactionAmount(amount: String): TransactionRobot {
        onView(TRANSACTION_AMOUNT_MATCHER).perform(clearText(), typeText(amount), closeSoftKeyboard())
        return this
    }

    fun assertTransactionDescriptionAtPosition(description: String, position: Int): TransactionRobot {
        matchTextInRecyclerView<TransactionAdapter.TransactionViewHolder>(description, RECYCLER_VIEW_ID, TRANSACTION_DESCRIPTION_ID, position)
        return this
    }

    fun assertTransactionAmountAtPosition(amount: String, position: Int): TransactionRobot {
        matchTextInRecyclerView<TransactionAdapter.TransactionViewHolder>(amount, RECYCLER_VIEW_ID, TRANSACTION_AMOUNT_ID, position)
        return this
    }

    fun longClick(position: Int): TransactionRobot {
        onView(RECYCLER_VIEW_MATCHER).perform(RecyclerViewActions.actionOnItemAtPosition<TransactionAdapter.TransactionViewHolder>(position, longClick()))
        return this
    }

    fun delete(): TransactionRobot {
        onView(DELETE_BUTTON_MATCHER).perform(click())
        return this
    }

    companion object {
        private const val RECYCLER_VIEW_ID = R.id.transactionsRecyclerView
        private const val WITHDRAWAL_SWITCH_ID = R.id.withdrawalSwitch
        private const val TRANSACTION_DESCRIPTION_ID = R.id.transactionDescription
        private const val TRANSACTION_AMOUNT_ID = R.id.transactionAmount

        private val ADD_BUTTON_MATCHER = withId(R.id.addTransactionButton)
        private val RECYCLER_VIEW_MATCHER = withId(RECYCLER_VIEW_ID)
        private val WITHDRAWAL_SWITCH_MATCHER = withId(WITHDRAWAL_SWITCH_ID)
        private val TRANSACTION_DESCRIPTION_MATCHER = withId(TRANSACTION_DESCRIPTION_ID)
        private val TRANSACTION_AMOUNT_MATCHER = withId(TRANSACTION_AMOUNT_ID)
        private val SUBMIT_BUTTON_MATCHER = withId(R.id.submitButton)
        private val DELETE_BUTTON_MATCHER = withId(R.id.action_delete)
    }
}