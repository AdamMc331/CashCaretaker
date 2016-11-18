package com.androidessence.cashcaretaker;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.androidessence.cashcaretaker.activities.AccountsActivity;
import com.androidessence.cashcaretaker.data.CCContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Tests creating an account
 *
 * Created by adam.mcneilly on 11/17/16.
 */
@RunWith(AndroidJUnit4.class)
public class AccountTest {

    @Rule
    public ActivityTestRule<AccountsActivity> activityTestRule = new ActivityTestRule<>(AccountsActivity.class);

    @Before
    public void setup() {
        activityTestRule.getActivity().getContentResolver().delete(
                CCContract.AccountEntry.CONTENT_URI,
                null,
                null
        );
    }

    @After
    public void teardown() {
        activityTestRule.getActivity().getContentResolver().delete(
                CCContract.AccountEntry.CONTENT_URI,
                null,
                null
        );
    }

    @Test
    public void testAddAccount() {
        onView(withId(R.id.add_account_fab)).perform(click());
        onView(withId(R.id.account_name)).perform(clearText(), typeText("Checking"), closeSoftKeyboard());
        onView(withId(R.id.starting_balance)).perform(clearText(), typeText("1000.00"), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());
    }
}
