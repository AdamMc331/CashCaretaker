package com.androidessence.cashcaretaker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidessence.utility.Utility;

import java.text.MessageFormat;
import java.util.List;

/**
 * Adapter class that displays a list of accounts and their balances on an Android Wear device.
 *
 * Created by adammcneilly on 12/27/15.
 */
public class WearableAccountAdapter extends WearableListView.Adapter{
    private Context mContext;
    private List<Account> mAccounts;
    private double mTotalAmount;
    private MessageFormat mTotalFormat;

    private static final int HEADER_TYPE = 0;
    private static final int ACCOUNT_TYPE = 1;

    public WearableAccountAdapter(Context context, List<Account> accounts) {
        this.mContext = context;
        this.mAccounts = accounts;

        mTotalFormat = new MessageFormat(mContext.getString(R.string.account_header));

        // Calculate total
        for(Account account : mAccounts) {
            mTotalAmount += account.getBalance();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return HEADER_TYPE;
        } else {
            return ACCOUNT_TYPE;
        }
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case HEADER_TYPE:
                return new HeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_account_header, parent, false));
            case ACCOUNT_TYPE:
            default:
                return new AccountViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_account, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        switch(getItemViewType(position)) {
            case HEADER_TYPE:
                ((HeaderViewHolder)holder).bindTotal();
                break;
            case ACCOUNT_TYPE:
            default:
                ((AccountViewHolder)holder).bindAccount(mAccounts.get(position - 1));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mAccounts.size() + 1;
    }

    public class HeaderViewHolder extends WearableListView.ViewHolder{
        private TextView mTotal;

        public HeaderViewHolder(View view) {
            super(view);

            mTotal = (TextView) view.findViewById(R.id.account_header);
        }

        public void bindTotal() {
            mTotal.setText(mTotalFormat.format(new Object[] {mTotalAmount}));
        }
    }

    public class AccountViewHolder extends WearableListView.ViewHolder {
        private TextView mAccountName;
        private TextView mAccountBalance;

        public AccountViewHolder(View view) {
            super(view);

            mAccountName = (TextView) view.findViewById(R.id.account_name);
            mAccountBalance = (TextView) view.findViewById(R.id.account_balance);
        }

        public void bindAccount(Account account) {
            mAccountName.setText(account.getName());
            mAccountBalance.setText(Utility.getCurrencyString(account.getBalance()));
        }
    }
}
