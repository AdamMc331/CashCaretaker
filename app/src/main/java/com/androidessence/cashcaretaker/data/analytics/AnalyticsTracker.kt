package com.androidessence.cashcaretaker.data.analytics

interface AnalyticsTracker {
    fun trackAccountAdded()
    fun trackAccountClicked()
    fun trackAccountDeleted()

    fun trackTransactionAdded()
    fun trackTransactionEdited()
    fun trackTransactionDeleted()
}
