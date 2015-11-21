package com.androidessence.cashcaretaker.dataTransferObjects;

import android.database.Cursor;
import android.os.Parcel;

import com.androidessence.cashcaretaker.data.CCContract;

/**
 * An object representing transaction details. Instead of just including foreign keys as longs,
 * this class holds reference to a Category object as well.
 *
 * Created by adammcneilly on 11/20/15.
 */
public class TransactionDetails extends Transaction {
    private Category category;

    public TransactionDetails(Cursor cursor) {
        super(cursor);
        // Get category name
        String categoryName = cursor.getString(cursor.getColumnIndex(CCContract.CategoryEntry.COLUMN_DESCRIPTION));
        setCategory(new Category(getCategoryID(), categoryName));
    }

    public TransactionDetails(Parcel source) {
        super(source);
        setCategory((Category) source.readParcelable(Category.class.getClassLoader()));
    }

    public static final Creator<TransactionDetails> CREATOR = new Creator<TransactionDetails>() {
        @Override
        public TransactionDetails createFromParcel(Parcel source) {
            return new TransactionDetails(source);
        }

        @Override
        public TransactionDetails[] newArray(int size) {
            return new TransactionDetails[size];
        }
    };

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static Creator<TransactionDetails> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(getCategory(), flags);
    }
}
