package com.androidessence.cashcaretaker.dataTransferObjects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.androidessence.cashcaretaker.core.CoreDTO;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Represents a category for a transaction.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
public class CategoryR extends CoreDTO {
    private String description;
    private boolean isDefault;

    public static Creator<CategoryR> CREATOR = new Creator<CategoryR>() {

        @Override
        public CategoryR createFromParcel(Parcel source) {
            return new CategoryR(source);
        }

        @Override
        public CategoryR[] newArray(int size) {
            return new CategoryR[size];
        }
    };

    public CategoryR(long identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    public CategoryR(Cursor cursor){
        this.identifier = cursor.getLong(cursor.getColumnIndex(CCContract.CategoryEntry._ID));
        this.description = cursor.getString(cursor.getColumnIndex(CCContract.CategoryEntry.COLUMN_DESCRIPTION));
        int defaultInt = cursor.getInt(cursor.getColumnIndex(CCContract.CategoryEntry.COLUMN_IS_DEFAULT));
        this.isDefault = defaultInt == 1;
    }

    private CategoryR(Parcel parcel){
        super(parcel);
        this.description = parcel.readString();
        this.isDefault = parcel.readInt() == 1;
    }

    public CategoryR(){
        this.identifier = 0;
        this.description = "";
        this.isDefault = false;
    }

    public long getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        if(identifier > 0) {
            values.put(CCContract.CategoryEntry._ID, identifier);
        }

        values.put(CCContract.CategoryEntry.COLUMN_DESCRIPTION, description);
        values.put(CCContract.CategoryEntry.COLUMN_IS_DEFAULT, isDefault ? 1 : 0);

        return values;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(getDescription());
        dest.writeInt(isDefault ? 1 : 0);
    }
}
