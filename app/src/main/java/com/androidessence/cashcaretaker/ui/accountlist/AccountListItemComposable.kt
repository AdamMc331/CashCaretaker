package com.androidessence.cashcaretaker.ui.accountlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.util.asCurrency

@Composable
fun AccountListItem(
    account: Account,
    accountClickListener: AccountListItemClickListener? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                dimensionResource(id = R.dimen.single_margin)
            )
            .clickable(
                onClick = {
                    accountClickListener?.onAccountClicked(account)
                },
                onLongClick = {
                    accountClickListener?.onAccountLongClicked(account)
                }
            )
    ) {
        Text(
            modifier = Modifier
                .gravity(Alignment.CenterVertically)
                .weight(1F),
            text = account.name,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier
                .gravity(Alignment.CenterVertically)
                .weight(2F),
            text = account.balance.asCurrency(),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            color = if (account.balance < 0.0) Color.Red else Color.Black
        )
        Image(
            modifier = Modifier
                .gravity(Alignment.CenterVertically)
                .padding(dimensionResource(id = R.dimen.double_margin))
                .clickable(
                    onClick = {
                        accountClickListener?.onWithdrawalClicked(account)
                    }
                ),
            asset = vectorResource(id = R.drawable.ic_add_green_24dp),
        )
        Image(
            modifier = Modifier
                .gravity(Alignment.CenterVertically)
                .padding(dimensionResource(id = R.dimen.double_margin))
                .clickable(
                    onClick = {
                        accountClickListener?.onDepositClicked(account)
                    }
                ),
            asset = vectorResource(id = R.drawable.ic_remove_red_24dp),
        )
    }
}

@Preview
@Composable
fun PreviewPositiveAccount() {
    AccountListItem(
        account = Account(
            name = "Checking",
            balance = 100.23
        )
    )
}

@Preview
@Composable
fun PreviewNegativeAccount() {
    AccountListItem(
        account = Account(
            name = "Checking",
            balance = -500.00
        )
    )
}
