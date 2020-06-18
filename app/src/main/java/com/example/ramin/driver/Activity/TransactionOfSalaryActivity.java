package com.example.ramin.driver.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.driver.Network.GetDriverData;
import com.example.ramin.driver.Network.RetrofitDriverInstance;
import com.example.ramin.driver.Preferences;
import com.example.ramin.driver.R;
import com.example.ramin.driver.Adapter.RecyclerTransactionOfSalaryAdapter;
import com.example.ramin.driver.Model.TransactionOfSalaryModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TransactionOfSalaryActivity extends AppCompatActivity {

    RecyclerView transactionOfSalaryRecycler;
    RecyclerTransactionOfSalaryAdapter adapter;
    List<TransactionOfSalaryModel> modelList = new ArrayList<>();
    Toolbar transactionOfSalaryToolbar;
    TextView tvToolbarTitle;
    private static final String TAG ="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_of_salary);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        setRecyclerView();
        getTransactionSalaryFromServer();
    }

    private void setToolbar() {
        transactionOfSalaryToolbar = findViewById(R.id.transaction_of_salary_toolbar);
        tvToolbarTitle = transactionOfSalaryToolbar.findViewById(R.id.transaction_of_salary_toolbar_title);
        setSupportActionBar(transactionOfSalaryToolbar);
        tvToolbarTitle.setText(R.string.transaction_of_salary_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setRecyclerView() {
        transactionOfSalaryRecycler = findViewById(R.id.transaction_of_salary_recycler);
        adapter = new RecyclerTransactionOfSalaryAdapter(modelList,getApplicationContext());
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(getApplicationContext());
        transactionOfSalaryRecycler.setLayoutManager(regLayoutManager);
        transactionOfSalaryRecycler.setItemAnimator(new DefaultItemAnimator());
        transactionOfSalaryRecycler.setHasFixedSize(true);
        transactionOfSalaryRecycler.setAdapter(adapter);
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

    private void getTransactionSalaryFromServer() {
        Preferences p = new Preferences(this);
        int dId = p.getDriverId();
        GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
        Call<List<TransactionOfSalaryModel>> call = api.getTransactionSalary(dId);
        call.enqueue(new Callback<List<TransactionOfSalaryModel>>() {
            @Override
            public void onResponse(Call<List<TransactionOfSalaryModel>> call, Response<List<TransactionOfSalaryModel>> response) {
                if (response.isSuccessful()) {
                    modelList = response.body();
                    adapter.updateTransactionSalaryList(modelList);
                }else {
                    Log.i(TAG, "onTransactionSalaryResponse: " + response.code() + " " + response.message());
                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TransactionOfSalaryModel>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
