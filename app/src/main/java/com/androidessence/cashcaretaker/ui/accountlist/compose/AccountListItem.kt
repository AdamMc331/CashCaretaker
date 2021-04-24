package com.androidessence.cashcaretaker.ui.accountlist.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.ui.theme.CashCaretakerTheme
import com.androidessence.cashcaretaker.util.asCurrency

@Composable
fun AccountListItem(
    account: Account
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AccountName(
            account.name,
            modifier = Modifier
                .weight(1F)
        )

        AccountBalance(
            account.balance,
            modifier = Modifier
                .weight(1F),
        )

        DepositButton()

        Spacer(modifier = Modifier.width(12.dp))

        WithdrawalButton()
    }
}

@Composable
private fun WithdrawalButton() {
    IconButton(
        onClick = { /*TODO*/ }
    ) {
        Icon(
            imageVector = Icons.Default.Remove,
            contentDescription = "Create Withdrawal",
            tint = Color.Red,
        )
    }
}

@Composable
private fun DepositButton() {
    IconButton(
        onClick = { /*TODO*/ }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Create Deposit",
            tint = Color.Green,
        )
    }
}

@Composable
private fun AccountBalance(
    balance: Double,
    modifier: Modifier = Modifier,
) {
    Text(
        text = balance.asCurrency(),
        modifier = modifier,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun AccountName(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = name,
        style = MaterialTheme.typography.h6,
        modifier = modifier,
    )
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
private fun AccountListItemPreview() {
    val account = Account(
        name = "Checking",
        balance = 100.00,
    )

    CashCaretakerTheme {
        Surface {
            AccountListItem(account)
        }
    }
}
