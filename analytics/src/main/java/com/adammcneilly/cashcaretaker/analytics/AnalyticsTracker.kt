package com.adammcneilly.cashcaretaker.analytics

interface AnalyticsTracker {
    fun trackAccountAdded()
    fun trackAccountClicked()
    fun trackAccountDeleted()

    fun trackTransactionAdded()
    fun trackTransactionEdited()
    fun trackTransactionDeleted()

    fun trackTransferAdded()
}