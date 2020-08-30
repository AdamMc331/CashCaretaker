package com.androidessence.cashcaretaker.ui.accountlist

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.androidessence.cashcaretaker.core.models.Account

@Composable
fun AccountListScroller(accounts: List<Account>) {
    ScrollableColumn {
        accounts.forEach { account ->
            AccountListItem(account = account)
            Divider(color = Color.LightGray)
        }
    }
}
