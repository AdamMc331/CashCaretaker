package com.androidessence.cashcaretaker.main

/**
 * Functionality for the main view.
 */
interface MainController {
    fun navigateToAddAccount()
    fun showTransactions(accountName: String)
}