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

    override fun trackTransactionAdded() {
        firebase.logEvent(EVENT_TRANSACTION_ADDED, null)
    }

    override fun trackTransactionDeleted() {
        firebase.logEvent(EVENT_TRANSACTION_DELETED, null)
    }

    override fun trackTransactionEdited() {
        firebase.logEvent(EVENT_TRANSACTION_EDITED, null)
    }

    override fun trackTransferAdded() {
        firebase.logEvent(EVENT_TRANSFER_ADDED, null)
    }

    companion object {
        private const val EVENT_ACCOUNT_ADDED = "user_added_account"
        private const val EVENT_ACCOUNT_CLICKED = "user_clicked_account"
        private const val EVENT_ACCOUNT_DELETED = "user_deleted_account"

        private const val EVENT_TRANSACTION_ADDED = "user_added_transaction"
        private const val EVENT_TRANSACTION_EDITED = "user_edited_transaction"
        private const val EVENT_TRANSACTION_DELETED = "user_deleted_transaction"

        private const val EVENT_TRANSFER_ADDED = "user_added_transfer"
    }
}
