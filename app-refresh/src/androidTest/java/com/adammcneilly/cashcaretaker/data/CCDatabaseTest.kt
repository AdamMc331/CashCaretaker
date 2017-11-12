package com.adammcneilly.cashcaretaker.data

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.adammcneilly.cashcaretaker.account.Account
import com.adammcneilly.cashcaretaker.main.MainActivity
import com.adammcneilly.cashcaretaker.transaction.Transaction
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests the database code.
 */
@RunWith(AndroidJUnit4::class)
class CCDatabaseTest {
    private lateinit var database: CCDatabase
    private lateinit var accountDao: AccountDAO
    private lateinit var transactionDao: TransactionDAO

    @JvmField @Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @JvmField @Rule val mainActivity = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        val context = mainActivity.activity
        database = Room.inMemoryDatabaseBuilder(context, CCDatabase::class.java).allowMainThreadQueries().build()
        accountDao = database.accountDao()
        transactionDao = database.transactionDao()
    }

    @After
    fun tearDown() {
        accountDao.deleteAll()
        database.close()
    }

    @Test
    fun testWriteReadAccount() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)

        val ids = accountDao.insert(listOf(testAccount))
        assertEquals(1, ids.size)

        accountDao.getAll()
                .test()
                .assertValue(listOf(testAccount))
    }

    @Test
    fun testWriteReadTransactionWithGetAll() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)

        val ids = accountDao.insert(listOf(testAccount))
        assertEquals(1, ids.size)

        val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT)
        val transactionIds = transactionDao.insert(listOf(testWithdrawal))
        assertEquals(1, transactionIds.size)
        testWithdrawal.id = transactionIds.first()

        transactionDao.getAll()
                .test()
                .assertValue(listOf(testWithdrawal))
    }

    @Test
    fun testWriteReadTransactionWithGetForAccount() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)

        val ids = accountDao.insert(listOf(testAccount))
        assertEquals(1, ids.size)

        val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT)
        val transactionIds = transactionDao.insert(listOf(testWithdrawal))
        assertEquals(1, transactionIds.size)
        testWithdrawal.id = transactionIds.first()

        transactionDao.getAllForAccount(testAccount.name)
                .test()
                .assertValue(listOf(testWithdrawal))
    }

    @Test
    fun testWithdrawalBalanceChangeTrigger() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)

        val ids = accountDao.insert(listOf(testAccount))
        assertEquals(1, ids.size)

        val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT)
        val transactionIds = transactionDao.insert(listOf(testWithdrawal))
        assertEquals(1, transactionIds.size)

        val account = accountDao.getAll().test().values().first().first()
        assertEquals(TEST_ACCOUNT_BALANCE - TEST_TRANSACTION_AMOUNT, account.balance, 0.0)
    }

    @Test
    fun testDepositBalanceChangeTrigger() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)

        val ids = accountDao.insert(listOf(testAccount))
        assertEquals(1, ids.size)

        val testDeposit = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, false)
        val transactionIds = transactionDao.insert(listOf(testDeposit))
        assertEquals(1, transactionIds.size)

        val account = accountDao.getAll().test().values().first().first()
        assertEquals(TEST_ACCOUNT_BALANCE + TEST_TRANSACTION_AMOUNT, account.balance, 0.0)
    }

    @Test
    fun testDepositRemovalBalanceChangeTrigger() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)

        val ids = accountDao.insert(listOf(testAccount))
        assertEquals(1, ids.size)

        val testDeposit = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, false)
        val transactionIds = transactionDao.insert(listOf(testDeposit))
        assertEquals(1, transactionIds.size)

        val account = accountDao.getAll().test().values().first().first()
        assertEquals(TEST_ACCOUNT_BALANCE + TEST_TRANSACTION_AMOUNT, account.balance, 0.0)
    }

    companion object {
        val TEST_ACCOUNT_NAME = "Checking"
        val TEST_TRANSACTION_NAME = "Speedway"
        val TEST_ACCOUNT_BALANCE = 100.00
        val TEST_TRANSACTION_AMOUNT = 5.00
    }
}