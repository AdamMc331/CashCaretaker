package com.androidessence.cashcaretaker.ui.accountlist

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.util.asCurrency

@Composable
fun AccountListItem(account: Account) {
    Row(Modifier.fillMaxHeight()) {
        Text(
            text = account.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = account.balance.asCurrency(),
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@Preview
@Composable
fun PreviewAccount() {
    AccountListItem(
        account = Account(
            name = "Checking",
            balance = 100.23
        )
    )
}
