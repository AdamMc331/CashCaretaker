package com.androidessence.cashcaretaker.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.sqlite.room.Room
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.main.MainActivity
import com.androidessence.cashcaretaker.transaction.Transaction
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

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @JvmField
    @Rule
    val mainActivity = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        val context = mainActivity.activity
        database = Room.inMemoryDatabaseBuilder(context, CCDatabase::class.java).addCallback(CCDatabase.CALLBACK).allowMainThreadQueries().build()
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
        accountDao.insert(testAccount)

        accountDao.getAll()
                .test()
                .assertValue(listOf(testAccount))
    }

    @Test
    fun testWriteDeleteAccount() {
        // Insert
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
        accountDao.insert(testAccount)

        // Delete
        val removedCount = accountDao.delete(testAccount)
        assertEquals(1, removedCount)

        accountDao.getAll()
                .test()
                .assertValue(emptyList())
    }

    @Test
    fun testWriteReadTransactionWithGetAll() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
        accountDao.insert(testAccount)

        val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT)
        val transactionId = transactionDao.insert(testWithdrawal)
        testWithdrawal.id = transactionId

        transactionDao.getAll()
                .test()
                .assertValue(listOf(testWithdrawal))
    }

    @Test
    fun testWriteReadTransactionWithGetForAccount() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
        accountDao.insert(testAccount)

        val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT)
        val transactionId = transactionDao.insert(testWithdrawal)
        testWithdrawal.id = transactionId

        transactionDao.getAllForAccount(testAccount.name)
                .test()
                .assertValue(listOf(testWithdrawal))
    }

    @Test
    fun testWithdrawalBalanceChangeTrigger() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
        accountDao.insert(testAccount)

        val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT)
        transactionDao.insert(testWithdrawal)

        val account = accountDao.getAll().test().values().first().first()
        assertEquals(TEST_ACCOUNT_BALANCE - TEST_TRANSACTION_AMOUNT, account.balance, 0.0)
    }

    @Test
    fun testDepositBalanceChangeTrigger() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
        accountDao.insert(testAccount)

        val testDeposit = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, false)
        transactionDao.insert(testDeposit)

        val account = accountDao.getAll().test().values().first().first()
        assertEquals(TEST_ACCOUNT_BALANCE + TEST_TRANSACTION_AMOUNT, account.balance, 0.0)
    }

    @Test
    fun testDepositRemovalBalanceChangeTrigger() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
        accountDao.insert(testAccount)

        val testDeposit = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, false)
        val transactionId = transactionDao.insert(testDeposit)

        val account = accountDao.getAll().test().values().first().first()
        assertEquals(TEST_ACCOUNT_BALANCE + TEST_TRANSACTION_AMOUNT, account.balance, 0.0)

        testDeposit.id = transactionId
        val removalCount = transactionDao.delete(testDeposit)
        assertEquals(1, removalCount)

        val account2 = accountDao.getAll().test().values().first().first()
        assertEquals(TEST_ACCOUNT_BALANCE, account2.balance, 0.0)
    }

    @Test
    fun testWithdrawalRemovalBalanceChangeTrigger() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)

        accountDao.insert(testAccount)

        val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, true)
        val transactionId = transactionDao.insert(testWithdrawal)

        val account = accountDao.getAll().test().values().first().first()
        assertEquals(TEST_ACCOUNT_BALANCE - TEST_TRANSACTION_AMOUNT, account.balance, 0.0)

        testWithdrawal.id = transactionId
        val removalCount = transactionDao.delete(testWithdrawal)
        assertEquals(1, removalCount)

        val account2 = accountDao.getAll().test().values().first().first()
        assertEquals(TEST_ACCOUNT_BALANCE, account2.balance, 0.0)
    }

    @Test
    fun testUpdateWithdrawal() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
        accountDao.insert(testAccount)

        val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, true)
        val transactionId = transactionDao.insert(testWithdrawal)

        // Update transaction to be 2 times the amount to test balance change
        testWithdrawal.id = transactionId
        testWithdrawal.amount *= 2

        val updateCount = transactionDao.update(testWithdrawal)
        assertEquals(1, updateCount)

        val expectedBalance = TEST_ACCOUNT_BALANCE - testWithdrawal.amount
        val account = accountDao.getAll().test().values().first().first()
        assertEquals(expectedBalance, account.balance, 0.0)
    }

    @Test
    fun testUpdateDeposit() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
        accountDao.insert(testAccount)

        val testDeposit = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, false)
        val transactionId = transactionDao.insert(testDeposit)

        // Update transaction to be 2 times the amount to test balance change
        testDeposit.id = transactionId
        testDeposit.amount *= 2

        val updateCount = transactionDao.update(testDeposit)
        assertEquals(1, updateCount)

        val expectedBalance = TEST_ACCOUNT_BALANCE + testDeposit.amount
        val account = accountDao.getAll().test().values().first().first()
        assertEquals(expectedBalance, account.balance, 0.0)
    }

    @Test
    fun testWithdrawalToDepositChange() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
        accountDao.insert(testAccount)

        val testTransaction = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, true)
        val transactionId = transactionDao.insert(testTransaction)

        // Update transaction to be 2 times the amount to test balance change
        testTransaction.id = transactionId
        testTransaction.amount *= 2
        testTransaction.withdrawal = false

        val updateCount = transactionDao.update(testTransaction)
        assertEquals(1, updateCount)

        val expectedBalance = TEST_ACCOUNT_BALANCE + testTransaction.amount
        val account = accountDao.getAll().test().values().first().first()
        assertEquals(expectedBalance, account.balance, 0.0)
    }

    @Test
    fun testDepositToWithdrawalChange() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
        accountDao.insert(testAccount)

        val testTransaction = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, false)
        val transactionId = transactionDao.insert(testTransaction)

        // Update transaction to be 2 times the amount to test balance change
        testTransaction.id = transactionId
        testTransaction.amount *= 2
        testTransaction.withdrawal = true

        val updateCount = transactionDao.update(testTransaction)
        assertEquals(1, updateCount)

        val expectedBalance = TEST_ACCOUNT_BALANCE - testTransaction.amount
        val account = accountDao.getAll().test().values().first().first()
        assertEquals(expectedBalance, account.balance, 0.0)
    }

    companion object {
        const val TEST_ACCOUNT_NAME = "Checking"
        const val TEST_TRANSACTION_NAME = "Speedway"
        const val TEST_ACCOUNT_BALANCE = 100.00
        const val TEST_TRANSACTION_AMOUNT = 5.00
    }
}