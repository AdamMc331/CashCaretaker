package com.androidessence.cashcaretaker.ui.accountlist

import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.ui.tooling.preview.Preview
import com.androidessence.cashcaretaker.core.models.Account

@Composable
fun AccountListScreenLiveData(
    viewStateLiveData: LiveData<AccountListState>,
    accountClickListener: (Account) -> Unit,
    accountLongClickListener: (Account) -> Unit,
    withdrawalClickListener: (Account) -> Unit,
    depositClickListener: (Account) -> Unit
) {
    val viewState by viewStateLiveData.observeAsState(
        initial = AccountListState.loading()
    )

    AccountListScreen(
        viewState = viewState,
        accountClickListener = accountClickListener,
        accountLongClickListener = accountLongClickListener,
        withdrawalClickListener = withdrawalClickListener,
        depositClickListener = depositClickListener
    )
}

@Composable
fun AccountListScreen(
    viewState: AccountListState,
    accountClickListener: (Account) -> Unit,
    accountLongClickListener: (Account) -> Unit,
    withdrawalClickListener: (Account) -> Unit,
    depositClickListener: (Account) -> Unit
) {
    when {
        viewState.loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                gravity = ContentGravity.Center,
                backgroundColor = Color.White
            ) {
                CircularProgressIndicator()
            }
        }
        viewState.data.isNotEmpty() -> {
            Surface(color = Color.White) {
                ScrollableColumn {
                    viewState.data.forEach { account ->
                        AccountListItem(
                            account = account,
                            accountClickListener = accountClickListener,
                            accountLongClickListener = accountLongClickListener,
                            withdrawalClickListener = withdrawalClickListener,
                            depositClickListener = depositClickListener
                        )
                        Divider(color = Color.LightGray)
                    }
                }
            }
        }
        else -> {
            NoAccountsMessage()
        }
    }
}

@Preview
@Composable
fun PreviewLoadingState() {
    AccountListScreen(
        viewState = AccountListState.loading(),
        accountClickListener = {},
        accountLongClickListener = {},
        withdrawalClickListener = {},
        depositClickListener = {}
    )
}

@Preview
@Composable
fun PreviewEmptyAccounts() {
    AccountListScreen(
        viewState = AccountListState.success(emptyList()),
        accountClickListener = {},
        accountLongClickListener = {},
        withdrawalClickListener = {},
        depositClickListener = {}
    )
}

@Preview
@Composable
@Suppress("MagicNumber")
fun PreviewAccountList() {
    AccountListScreen(
        viewState = AccountListState.success(
            listOf(
                Account(name = "Test One", 100.00),
                Account(name = "Test Two", 500.00),
                Account(name = "Test Three", -100.00)
            )
        ),
        accountClickListener = {},
        accountLongClickListener = {},
        withdrawalClickListener = {},
        depositClickListener = {}
    )
}
