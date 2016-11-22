package com.androidessence.cashcaretaker;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.FailureHandler;
import android.support.test.espresso.base.DefaultFailureHandler;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.androidessence.cashcaretaker.activities.AccountsActivity;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.androidessence.cashcaretaker.TestUtils.takeScreenshot;

/**
 * Tests creating an account
 *
 * Created by adam.mcneilly on 11/17/16.
 */
@RunWith(AndroidJUnit4.class)
public class AccountTest {
    private static final String VALID_ACCOUNT_NAME = "Checking";
    private static final String VALID_STARTING_BALANCE = "1000.00";
    private static final String VALID_ACCOUNT_DISPLAY_BALANCE = "$1,000.00";

    @Rule
    public ActivityTestRule<AccountsActivity> activityTestRule = new ActivityTestRule<>(AccountsActivity.class);

    @Before
    public void setup() {
        activityTestRule.getActivity().getContentResolver().delete(
                CCContract.AccountEntry.CONTENT_URI,
                null,
                null
        );

        Espresso.setFailureHandler(new FailureHandler() {
            @Override
            public void handle(Throwable error, Matcher<View> viewMatcher) {
                takeScreenshot("test_failed");
                new DefaultFailureHandler(activityTestRule.getActivity()).handle(error, viewMatcher);
            }
        });
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
        new AccountRobot()
                .newAccount()
                .accountName(VALID_ACCOUNT_NAME)
                .startingBalance(VALID_STARTING_BALANCE)
                .submit();

        // Match checking
        onView(withId(R.id.account_name)).check(matches(withText(VALID_ACCOUNT_NAME)));
        onView(withId(R.id.account_balance)).check(matches(withText(VALID_ACCOUNT_DISPLAY_BALANCE)));
        takeScreenshot("account_displayed");
    }

    @Test
    public void testEmptyAccountNameError() {
        new AccountRobot()
                .newAccount()
                .startingBalance(VALID_STARTING_BALANCE)
                .submit()
                .assertAccountNameEmptyError();
    }

    @Test
    public void testEmptyStartingBalanceError() {
        new AccountRobot()
                .newAccount()
                .accountName(VALID_ACCOUNT_NAME)
                .submit()
                .assertBalanceEmptyError();
    }

    @Test
    public void testAccountExistsError() {
        // Insert test account
        Account account = new Account(VALID_ACCOUNT_NAME, Double.parseDouble(VALID_STARTING_BALANCE));
        activityTestRule.getActivity().getContentResolver().insert(CCContract.AccountEntry.CONTENT_URI, account.getContentValues());

        // Try to create
        new AccountRobot()
                .newAccount()
                .accountName(VALID_ACCOUNT_NAME)
                .startingBalance(VALID_STARTING_BALANCE)
                .submit()
                .assertAccountNameExistsError();
    }
}
