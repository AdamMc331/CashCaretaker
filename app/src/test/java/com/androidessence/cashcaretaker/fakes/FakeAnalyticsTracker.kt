package com.androidessence.cashcaretaker.fakes

import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker

class FakeAnalyticsTracker : AnalyticsTracker {
    private var trackAccountAddedCount = 0

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
        TODO("Not yet implemented")
    }

    override fun trackTransactionEdited() {
        TODO("Not yet implemented")
    }

    override fun trackTransactionDeleted() {
        TODO("Not yet implemented")
    }

    override fun trackTransferAdded() {
        TODO("Not yet implemented")
    }

    fun getTrackAccountAddedCount(): Int {
        return trackAccountAddedCount
    }
}
