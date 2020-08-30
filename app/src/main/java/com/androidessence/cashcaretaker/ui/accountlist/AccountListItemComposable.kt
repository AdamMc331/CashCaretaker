package com.androidessence.cashcaretaker.ui.accountlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.util.asCurrency

@Composable
fun AccountListItem(account: Account) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .gravity(Alignment.CenterVertically)
                .weight(1F),
            text = account.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier
                .gravity(Alignment.CenterVertically)
                .weight(2F),
            text = account.balance.asCurrency(),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier
                .gravity(Alignment.CenterVertically)
                .padding(dimensionResource(id = R.dimen.double_margin)),
            asset = vectorResource(id = R.drawable.ic_add_green_24dp),
        )
        Image(
            modifier = Modifier
                .gravity(Alignment.CenterVertically)
                .padding(dimensionResource(id = R.dimen.double_margin)),
            asset = vectorResource(id = R.drawable.ic_remove_red_24dp)
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
