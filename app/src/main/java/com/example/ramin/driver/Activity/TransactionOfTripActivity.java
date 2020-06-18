package com.example.ramin.driver.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ramin.driver.R;
import com.example.ramin.driver.RecyclerTransactionOfTripAdapter;
import com.example.ramin.driver.TransactionOfTripModel;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TransactionOfTripActivity extends AppCompatActivity {

    RecyclerView transactionOfTripRecycler;
    RecyclerTransactionOfTripAdapter adapter;
    List<TransactionOfTripModel> modelList = new ArrayList<>();
    Toolbar transactionOfTripToolbar;
    TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_of_trip);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        setRecyclerView();
        setData();
    }

    private void setToolbar() {
        transactionOfTripToolbar = findViewById(R.id.transaction_of_trip_toolbar);
        tvToolbarTitle = transactionOfTripToolbar.findViewById(R.id.transaction_of_trip_toolbar_title);
        setSupportActionBar(transactionOfTripToolbar);
        tvToolbarTitle.setText(R.string.transaction_of_trip_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setRecyclerView() {
        transactionOfTripRecycler = findViewById(R.id.transaction_of_trip_recycler);
        adapter = new RecyclerTransactionOfTripAdapter(modelList,getApplicationContext());
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(getApplicationContext());
        transactionOfTripRecycler.setLayoutManager(regLayoutManager);
        transactionOfTripRecycler.setItemAnimator(new DefaultItemAnimator());
        transactionOfTripRecycler.setHasFixedSize(true);
        transactionOfTripRecycler.setAdapter(adapter);
    }

    private void setData() {
        modelList.add(new TransactionOfTripModel("ملایر","تهران","1398/04/24","2,000,000","1234","21:30"));
        modelList.add(new TransactionOfTripModel("ملایر","تهران","1398/04/24","2,000,000","1234","21:30"));
        modelList.add(new TransactionOfTripModel("ملایر","تهران","1398/04/24","2,000,000","1234","21:30"));
        modelList.add(new TransactionOfTripModel("ملایر","تهران","1398/04/24","2,000,000","1234","21:30"));
        modelList.add(new TransactionOfTripModel("ملایر","تهران","1398/04/24","2,000,000","1234","21:30"));
        modelList.add(new TransactionOfTripModel("ملایر","تهران","1398/04/24","2,000,000","1234","21:30"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
