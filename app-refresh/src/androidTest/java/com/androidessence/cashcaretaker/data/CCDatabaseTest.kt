package com.androidessence.cashcaretaker.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.main.MainActivity
import com.androidessence.cashcaretaker.transaction.Transaction
import kotlinx.coroutines.runBlocking
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
    private lateinit var databaseRobot: CCDatabaseRobot

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
        databaseRobot = CCDatabaseRobot(database)
    }

    @After
    fun tearDown() {
        runBlocking {
            databaseRobot.deleteAllAccounts()
            databaseRobot.closeDatabase()
        }
    }

    @Test
    fun testWriteReadAccount() {
        runBlocking {
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            val expected = listOf(testAccount)
            databaseRobot.assertAccountsEqual(expected)
        }
    }

    @Test
    fun testWriteDeleteAccount() {
        runBlocking {
            // Insert
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            // Delete
            val removedCount = databaseRobot.deleteAccount(testAccount)
            assertEquals(1, removedCount)

            databaseRobot.assertAccountsEqual(emptyList())
        }
    }

    @Test
    fun testWriteReadTransactionWithGetForAccount() {
        runBlocking {
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT)
            val transactionId = databaseRobot.insertTransaction(testWithdrawal)
            testWithdrawal.id = transactionId

            val expected = listOf(testWithdrawal)
            databaseRobot.assertTransactionsForAccount(expected, testAccount.name)
        }
    }

    @Test
    fun testWithdrawalBalanceChangeTrigger() {
        runBlocking {
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT)
            databaseRobot.insertTransaction(testWithdrawal)

            val account = databaseRobot.getFirstAccount()
            assertEquals(TEST_ACCOUNT_BALANCE - TEST_TRANSACTION_AMOUNT, account.balance, 0.0)
        }
    }

    @Test
    fun testDepositBalanceChangeTrigger() {
        runBlocking {
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            val testDeposit = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, false)
            databaseRobot.insertTransaction(testDeposit)

            val account = databaseRobot.getFirstAccount()
            assertEquals(TEST_ACCOUNT_BALANCE + TEST_TRANSACTION_AMOUNT, account.balance, 0.0)
        }
    }

    @Test
    fun testDepositRemovalBalanceChangeTrigger() {
        runBlocking {
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            val testDeposit = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, false)
            val transactionId = databaseRobot.insertTransaction(testDeposit)

            val account = databaseRobot.getFirstAccount()
            assertEquals(TEST_ACCOUNT_BALANCE + TEST_TRANSACTION_AMOUNT, account.balance, 0.0)

            testDeposit.id = transactionId
            val removalCount = databaseRobot.deleteTransaction(testDeposit)
            assertEquals(1, removalCount)

            val account2 = databaseRobot.getFirstAccount()
            assertEquals(TEST_ACCOUNT_BALANCE, account2.balance, 0.0)
        }
    }

    @Test
    fun testWithdrawalRemovalBalanceChangeTrigger() {
        runBlocking {
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, true)
            val transactionId = databaseRobot.insertTransaction(testWithdrawal)

            val account = databaseRobot.getFirstAccount()
            assertEquals(TEST_ACCOUNT_BALANCE - TEST_TRANSACTION_AMOUNT, account.balance, 0.0)

            testWithdrawal.id = transactionId
            val removalCount = databaseRobot.deleteTransaction(testWithdrawal)
            assertEquals(1, removalCount)

            val account2 = databaseRobot.getFirstAccount()
            assertEquals(TEST_ACCOUNT_BALANCE, account2.balance, 0.0)
        }
    }

    @Test
    fun testUpdateWithdrawal() {
        runBlocking {
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            val testWithdrawal = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, true)
            val transactionId = databaseRobot.insertTransaction(testWithdrawal)

            // Update transaction to be 2 times the amount to test balance change
            testWithdrawal.id = transactionId
            testWithdrawal.amount *= 2

            val updateCount = databaseRobot.updateTransaction(testWithdrawal)
            assertEquals(1, updateCount)

            val expectedBalance = TEST_ACCOUNT_BALANCE - testWithdrawal.amount
            val account = databaseRobot.getFirstAccount()
            assertEquals(expectedBalance, account.balance, 0.0)
        }
    }

    @Test
    fun testUpdateDeposit() {
        runBlocking {
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            val testDeposit = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, false)
            val transactionId = databaseRobot.insertTransaction(testDeposit)

            // Update transaction to be 2 times the amount to test balance change
            testDeposit.id = transactionId
            testDeposit.amount *= 2

            val updateCount = databaseRobot.updateTransaction(testDeposit)
            assertEquals(1, updateCount)

            val expectedBalance = TEST_ACCOUNT_BALANCE + testDeposit.amount
            val account = databaseRobot.getFirstAccount()
            assertEquals(expectedBalance, account.balance, 0.0)
        }
    }

    @Test
    fun testWithdrawalToDepositChange() {
        runBlocking {
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            val testTransaction = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, true)
            val transactionId = databaseRobot.insertTransaction(testTransaction)

            // Update transaction to be 2 times the amount to test balance change
            testTransaction.id = transactionId
            testTransaction.amount *= 2
            testTransaction.withdrawal = false

            val updateCount = databaseRobot.updateTransaction(testTransaction)
            assertEquals(1, updateCount)

            val expectedBalance = TEST_ACCOUNT_BALANCE + testTransaction.amount
            val account = databaseRobot.getFirstAccount()
            assertEquals(expectedBalance, account.balance, 0.0)
        }
    }

    @Test
    fun testDepositToWithdrawalChange() {
        runBlocking {
            val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)
            databaseRobot.insertAccount(testAccount)

            val testTransaction = Transaction(TEST_ACCOUNT_NAME, TEST_TRANSACTION_NAME, TEST_TRANSACTION_AMOUNT, false)
            val transactionId = databaseRobot.insertTransaction(testTransaction)

            // Update transaction to be 2 times the amount to test balance change
            testTransaction.id = transactionId
            testTransaction.amount *= 2
            testTransaction.withdrawal = true

            val updateCount = databaseRobot.updateTransaction(testTransaction)
            assertEquals(1, updateCount)

            val expectedBalance = TEST_ACCOUNT_BALANCE - testTransaction.amount
            val account = databaseRobot.getFirstAccount()
            assertEquals(expectedBalance, account.balance, 0.0)
        }
    }

    companion object {
        const val TEST_ACCOUNT_NAME = "Checking"
        const val TEST_TRANSACTION_NAME = "Speedway"
        const val TEST_ACCOUNT_BALANCE = 100.00
        const val TEST_TRANSACTION_AMOUNT = 5.00
    }
}