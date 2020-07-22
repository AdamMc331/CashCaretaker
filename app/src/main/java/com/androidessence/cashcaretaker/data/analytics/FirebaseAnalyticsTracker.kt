package com.androidessence.cashcaretaker.data.analytics

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class FirebaseAnalyticsTracker : AnalyticsTracker {
    private val firebase = Firebase.analytics

    override fun trackAccountAdded() {
        firebase.logEvent(EVENT_ACCOUNT_ADDED, null)
    }

    override fun trackAccountClicked() {
        firebase.logEvent(EVENT_ACCOUNT_CLICKED, null)
    }

    override fun trackAccountDeleted() {
        firebase.logEvent(EVENT_ACCOUNT_DELETED, null)
    }

    companion object {
        private const val EVENT_ACCOUNT_ADDED = "user added account"
        private const val EVENT_ACCOUNT_CLICKED = "user clicked account"
        private const val EVENT_ACCOUNT_DELETED = "user deleted account"
    }
}
