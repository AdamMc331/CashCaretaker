package com.androidessence.cashcaretaker.ui.accountlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import com.androidessence.cashcaretaker.R

private const val EMPTY_IMAGE_SCALE = 0.5F

@Composable
fun NoAccountsMessage() {
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.empty_accounts_padding))
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(fraction = EMPTY_IMAGE_SCALE)
                    .gravity(Alignment.CenterHorizontally),
                asset = vectorResource(id = R.drawable.ic_money_off_24dp),
                colorFilter = ColorFilter.tint(Color.Black),
                contentScale = ContentScale.FillWidth

            )
            Text(
                text = stringResource(id = R.string.no_accounts_message),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun PreviewNoAccounts() {
    NoAccountsMessage()
}