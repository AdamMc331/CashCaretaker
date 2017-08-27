package com.adammcneilly.cashcaretaker.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.adammcneilly.cashcaretaker.main.MainActivity;
import com.adammcneilly.cashcaretaker.account.Account;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static junit.framework.Assert.assertEquals;

/**
 * Tests the Cash Caretaker database.
 */
@RunWith(AndroidJUnit4.class)
public class CCDatabaseTestJava {
    private static final String TEST_ACCOUNT_NAME = "Checking";
    private static final Double TEST_ACCOUNT_BALANCE = 5.0;

    private CCDatabase database;
    private AccountDAO accountDAO;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        Context context = mainActivity.getActivity();
        database = Room.inMemoryDatabaseBuilder(context, CCDatabase.class).allowMainThreadQueries().build();
        accountDAO = database.accountDao();
    }

    @After
    public void tearDown() {
        accountDAO.deleteAll();
        database.close();
    }

    //@Test
    public void testWriteReadAccount() {
        Account testAccount = new Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE);

        List<Account> accounts = new ArrayList<>();
        accounts.add(testAccount);
        List<Long> ids = accountDAO.insert(accounts);
        assertEquals(1, ids.size());

        Flowable<List<Account>> accountsFlowable = accountDAO.getAll();
        TestSubscriber<List<Account>> testSubscriber = new TestSubscriber<>();
        accountsFlowable.subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
    }
}
