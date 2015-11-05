//package com.androidessence.cashcaretaker.activities;
//
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.view.MenuItem;
//
//import com.androidessence.cashcaretaker.R;
//import com.androidessence.cashcaretaker.fragments.AddTransactionFragment;
//
//public class AddTransactionActivity extends AppCompatActivity {
//
//    private long mAccount;
//    public static final String ACCOUNT_ARG = "accountArg";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_transaction);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        mAccount = getIntent().getLongExtra(ACCOUNT_ARG, 0);
//
//        Fragment addTransactionFragment = AddTransactionFragment.NewInstance(mAccount);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_add_transaction, addTransactionFragment).commit();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()){
//            // If we hit back, just finish, that way the next activity can recreate fine.
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//}
