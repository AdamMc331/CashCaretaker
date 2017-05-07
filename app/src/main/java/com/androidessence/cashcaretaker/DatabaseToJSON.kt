package com.androidessence.cashcaretaker

import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns

import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.cashcaretaker.dataTransferObjects.Account

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Helper class to convert some database information to JSON objects to be sent from the handheld to
 * the Wear device.

 * Created by adammcneilly on 12/28/15.
 */
class DatabaseToJSON(private val context: Context) {

    val accountJSON: JSONArray
        @Throws(JSONException::class)
        get() {
            val root = JSONArray()

            val accounts = accounts

            for (account in accounts) {
                val `object` = JSONObject()

                `object`.put(BaseColumns._ID, account.identifier)
                `object`.put(CCContract.AccountEntry.COLUMN_NAME, account.name)
                `object`.put(CCContract.AccountEntry.COLUMN_BALANCE, account.balance)

                root.put(`object`)
            }

            return root
        }

    private val accounts: List<Account>
        get() {
            val accounts = ArrayList<Account>()

            val cursor = context.contentResolver.query(
                    CCContract.AccountEntry.CONTENT_URI, null, null, null, null
            )!!

            while (cursor.moveToNext()) {
                accounts.add(Account(cursor))
            }

            cursor.close()

            return accounts
        }
}
