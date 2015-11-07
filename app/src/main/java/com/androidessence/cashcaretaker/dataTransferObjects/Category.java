package com.androidessence.cashcaretaker.dataTransferObjects;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Class pertaining information of a Category of transactions.
 *
 * Created by adammcneilly on 11/2/15.
 */
public class Category implements Parcelable{
    private long identifier;
    private String description;

    public static Creator<Category> CREATOR = new Creator<Category>() {

        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public Category(Cursor cursor){
        setIdentifier(cursor.getLong(cursor.getColumnIndex(CCContract.CategoryEntry._ID)));
        setDescription(cursor.getString(cursor.getColumnIndex(CCContract.CategoryEntry.COLUMN_DESCRIPTION)));
    }

    private Category(Parcel parcel){
        setIdentifier(parcel.readLong());
        setDescription(parcel.readString());
    }

    public Category(){
        setIdentifier(0);
        setDescription("");
    }

    public long getIdentifier() {
        return identifier;
    }

    private void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getIdentifier());
        dest.writeString(getDescription());
    }
}
