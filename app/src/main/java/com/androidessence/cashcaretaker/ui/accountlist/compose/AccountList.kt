package com.androidessence.cashcaretaker.ui.accountlist.compose

import android.content.res.Configuration
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.ui.theme.CashCaretakerTheme

@Composable
fun AccountList(
    accounts: List<Account>
) {
    LazyColumn {
        items(accounts) { account ->
            AccountListItem(account)

            Divider()
        }
    }
}

@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun AccountListPreview() {
    val checking = Account(
        name = "Checking",
        balance = 123.45,
    )

    val saving = Account(
        name = "Savings",
        balance = 123.45,
    )

    val accounts = listOf(checking, saving)

    CashCaretakerTheme {
        Surface {
            AccountList(accounts)
        }
    }
}
