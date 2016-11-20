package com.androidessence.cashcaretaker;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.androidessence.cashcaretaker.TestUtils.takeScreenshot;

/**
 * Robot that preforms account actions.
 *
 * Created by adam.mcneilly on 11/18/16.
 */
public class AccountRobot {
    private static final Matcher<View> ADD_ACCOUNT_FAB = withId(R.id.add_account_fab);
    private static final Matcher<View> ACCOUNT_NAME = withId(R.id.account_name);
    private static final Matcher<View> STARTING_BALANCE = withId(R.id.starting_balance);
    private static final Matcher<View> SUBMIT = withId(R.id.submit);

    public AccountRobot() {

    }

    public AccountRobot newAccount() {
        takeScreenshot("adding_account");
        onView(ADD_ACCOUNT_FAB).perform(click());
        return this;
    }

    public AccountRobot accountName(String accountName) {
        onView(ACCOUNT_NAME).perform(clearText(), typeText(accountName), closeSoftKeyboard());
        takeScreenshot("account_name_entered");
        return this;
    }

    public AccountRobot startingBalance(String startingBalance) {
        onView(STARTING_BALANCE).perform(clearText(), typeText(startingBalance), closeSoftKeyboard());
        takeScreenshot("starting_balance_entered");
        return this;
    }

    public AccountRobot submit() {
        takeScreenshot("adding_account");
        onView(SUBMIT).perform(click());
        return this;
    }

    public AccountRobot assertAccountNameEmptyError() {
        // Click and close keyboard to view error
        onView(ACCOUNT_NAME).perform(click(), closeSoftKeyboard());
        onView(ACCOUNT_NAME).check(matches(hasErrorText(R.string.err_account_blank)));
        takeScreenshot("account_name_error_matched");
        return this;
    }

    public AccountRobot assertBalanceEmptyError() {
        onView(STARTING_BALANCE).perform(click(), closeSoftKeyboard());
        onView(STARTING_BALANCE).check(matches(hasErrorText(R.string.err_starting_balance_blank)));
        takeScreenshot("starting_balance_error_matched");
        return this;
    }

    public AccountRobot assertAccountNameExistsError() {
        onView(ACCOUNT_NAME).perform(click(), closeSoftKeyboard());
        onView(ACCOUNT_NAME).check(matches(hasErrorText(R.string.err_account_exists)));
        takeScreenshot("account_name_error_matched");
        return this;
    }

    private static Matcher<View> hasErrorText(final int errorText) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                String expectedError = item.getContext().getString(errorText);

                return (item instanceof EditText) && expectedError.equals(((EditText)item).getError());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with error: ");
            }
        };
    }
}
