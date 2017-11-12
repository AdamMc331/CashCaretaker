package com.adammcneilly.cashcaretaker.main

/**
 * Functionality for the main view.
 */
interface MainView {
    fun navigateToAddAccount()
    fun onAccountInserted()
    fun showTransactions(accountName: String)
}