package com.androidessence.cashcaretaker;

import android.content.Context;
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
 * Created by adammcneilly on 12/27/15.
 */
public class AccountAdapter extends BaseAdapter {
    private Context mContext;
    private List<Account> mAccounts;
    private double mTotalAmount;
    private MessageFormat mTotalFormat;

    private static final int HEADER_TYPE = 0;
    private static final int ACCOUNT_TYPE = 1;

    public AccountAdapter(Context context, List<Account> accounts) {
        this.mContext = context;
        this.mAccounts = accounts;

        mTotalFormat = new MessageFormat(mContext.getString(R.string.total_message));

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
    public int getCount() {
        return mAccounts.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return mAccounts.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch(getItemViewType(position)) {
            case HEADER_TYPE:
                View headerView = LayoutInflater.from(mContext).inflate(R.layout.list_item_account_header, parent, false);
                HeaderViewHolder headerViewHolder = new HeaderViewHolder(headerView);
                headerViewHolder.bindTotal();
                headerView.setTag(headerViewHolder);
                return headerView;
            case ACCOUNT_TYPE:
            default:
                View accountView = LayoutInflater.from(mContext).inflate(R.layout.list_item_account, parent, false);
                AccountViewHolder accountViewHolder = new AccountViewHolder(accountView);
                accountViewHolder.bindAccount(mAccounts.get(position - 1));
                accountView.setTag(accountViewHolder);
                return accountView;
        }
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
