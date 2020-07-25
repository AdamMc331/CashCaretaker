package com.androidessence.cashcaretaker.fakes

import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker

class FakeAnalyticsTracker : AnalyticsTracker {
    private var trackAccountAddedCount = 0
    private var trackTransactionAddedCount = 0
    private var trackTransactionEditedCount = 0
    private var trackTransferAddedCount = 0

    override fun trackAccountAdded() {
        trackAccountAddedCount++
    }

    override fun trackAccountClicked() {
        TODO("Not yet implemented")
    }

    override fun trackAccountDeleted() {
        TODO("Not yet implemented")
    }

    override fun trackTransactionAdded() {
        trackTransactionAddedCount++
    }

    override fun trackTransactionEdited() {
        trackTransactionEditedCount++
    }

    override fun trackTransactionDeleted() {
        TODO("Not yet implemented")
    }

    override fun trackTransferAdded() {
        trackTransferAddedCount++
    }

    fun getTrackAccountAddedCount(): Int {
        return trackAccountAddedCount
    }

    fun getTrackTransactionAddedCount(): Int {
        return trackTransactionAddedCount
    }

    fun getTrackTransactionEditedCount(): Int {
        return trackTransactionEditedCount
    }

    fun getTrackTransferAddedCount(): Int {
        return trackTransferAddedCount
    }
}
