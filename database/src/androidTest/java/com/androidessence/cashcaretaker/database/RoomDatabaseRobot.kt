// package com.androidessence.cashcaretaker.database
//
// import org.junit.Assert
//
// class RoomDatabaseRobot(
//     private val database: CashCaretakerRoomDatabase
// ) {
//     suspend fun deleteAllAccounts(): Int {
//         return database.accountDao().deleteAll()
//     }
//
//     suspend fun insertAccount(account: PersistableAccount): Long {
//         return database.accountDao().insert(account)
//     }
//
//     suspend fun deleteAccount(account: PersistableAccount): Int {
//         return database.accountDao().delete(account)
//     }
//
//     fun getFirstAccount(): PersistableAccount? {
//         return database.accountDao().getAll().testObserver().observedValue?.first()
//     }
//
//     fun assertAccountsEqual(expectedAccounts: List<PersistableAccount>) {
//         val actualAccounts = database.accountDao().getAll().testObserver().observedValue
//         Assert.assertEquals(expectedAccounts, actualAccounts)
//     }
//
//     suspend fun insertTransaction(transaction: PersistableAccount): Long {
//         return database.transactionDao().insert(transaction)
//     }
//
//     suspend fun deleteTransaction(transaction: PersistableAccount): Int {
//         return database.transactionDao().delete(transaction)
//     }
//
//     suspend fun updateTransaction(transaction: PersistableAccount): Int {
//         return database.transactionDao().update(transaction)
//     }
//
//     fun assertTransactionsForAccount(expectedTransactions: List<PersistableAccount>, accountName: String) {
//         val actualTransactions =
//             database.transactionDao().getAllForAccount(accountName).testObserver().observedValue
//         Assert.assertEquals(expectedTransactions, actualTransactions)
//     }
//
//     fun closeDatabase() {
//         database.close()
//     }
// }
