package com.androidessence.cashcaretaker.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "transactionTable",
    foreignKeys = [
        (
            ForeignKey(
                entity = PersistableAccount::class,
                parentColumns = arrayOf("name"),
                childColumns = arrayOf("accountName"),
                onDelete = ForeignKey.CASCADE
            )
            )
    ]
)
data class PersistableTransaction(
    val accountName: String = "",
    val description: String = "",
    val amount: Double = 0.0,
    val withdrawal: Boolean = true,
    val date: Date = Date(),
    @PrimaryKey(autoGenerate = true) val id: Long = 0
) : Parcelable
