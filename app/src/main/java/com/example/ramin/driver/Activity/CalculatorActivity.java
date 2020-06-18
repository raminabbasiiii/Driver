package com.example.ramin.driver.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.driver.Model.DriverFinancialAccountModel;
import com.example.ramin.driver.Network.GetDriverData;
import com.example.ramin.driver.Network.RetrofitDriverInstance;
import com.example.ramin.driver.Preferences;
import com.example.ramin.driver.R;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar calculatorToolbar;
    TextView tvAmountOfDept,tvAmountOfCreditor,tvAmountOfInventory,tvToolbarTitle;
    Button btnTransactionOfTrip,btnDateGetSalary;
    int d =0 , r = 0;
    private static final String TAG ="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        findViews();
        getDeptFromServer();
        getRecruitmentFromServer();

    }

    private void findViews() {

        tvAmountOfCreditor = findViewById(R.id.tv_amount_of_creditor);
        tvAmountOfDept = findViewById(R.id.tv_amount_of_dept);
        tvAmountOfInventory = findViewById(R.id.tv_amount_of_inventory);

        btnDateGetSalary = findViewById(R.id.btn_date_get_salary);
        btnDateGetSalary.setOnClickListener(this);

        btnTransactionOfTrip = findViewById(R.id.btn_transaction_of_trip);
        btnTransactionOfTrip.setOnClickListener(this);



    }

    private void setToolbar() {
        calculatorToolbar = findViewById(R.id.calculator_toolbar);
        tvToolbarTitle = calculatorToolbar.findViewById(R.id.calculator_toolbar_title);
        setSupportActionBar(calculatorToolbar);
        tvToolbarTitle.setText(R.string.calculator_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
    public void onClick(final View v) {

        switch (v.getId()) {

            case R.id.btn_transaction_of_trip :
                Intent transactionOfTripActivity = new Intent(CalculatorActivity.this,TransactionOfTripActivity.class);
                startActivity(transactionOfTripActivity);
                break;

            case R.id.btn_date_get_salary :
                Intent transactionOfSalaryActivity = new Intent(CalculatorActivity.this,TransactionOfSalaryActivity.class);
                startActivity(transactionOfSalaryActivity);
                break;
        }
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

    private void getDeptFromServer() {
        Preferences p = new Preferences(this);
        int dId = p.getDriverId();
        PersianCalendar now = new PersianCalendar();
        int m = now.getPersianMonth() + 1;
        String nowDate = now.getPersianYear() + "/" + m + "/" + now.getPersianDay();
        //Toast.makeText(getApplicationContext(),nowDate,Toast.LENGTH_LONG).show();

        GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
        Call<DriverFinancialAccountModel> call = api.getDept(dId,nowDate);
        call.enqueue(new Callback<DriverFinancialAccountModel>() {
            @Override
            public void onResponse(Call<DriverFinancialAccountModel> call, Response<DriverFinancialAccountModel> response) {
                if (response.isSuccessful()) {
                    DriverFinancialAccountModel model = response.body();
                    d = model.getDept();
                    //String dept = String.valueOf(model.getDept());
                    DecimalFormat dc = new DecimalFormat("###,###,###");
                    String rial = String.valueOf(dc.format(model.getDept())) + " " + "ریال";
                    tvAmountOfDept.setText(rial);
                    int i = r -d;
                    DecimalFormat dcc = new DecimalFormat("###,###,###");
                    String riall = String.valueOf(dcc.format(i)) + " " + "ریال";
                    tvAmountOfInventory.setText(riall);
                }else {
                    Log.i(TAG, "onDeptResponse: " + response.code() + " " + response.message());
                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DriverFinancialAccountModel> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRecruitmentFromServer() {
        Preferences p = new Preferences(this);
        int dId = p.getDriverId();
        PersianCalendar now = new PersianCalendar();
        int m = now.getPersianMonth() + 1;
        String nowDate = now.getPersianYear() + "/" + m + "/" + now.getPersianDay();

        GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
        Call<DriverFinancialAccountModel> call = api.getRecruitment(dId,nowDate);
        call.enqueue(new Callback<DriverFinancialAccountModel>() {
            @Override
            public void onResponse(Call<DriverFinancialAccountModel> call, Response<DriverFinancialAccountModel> response) {
                if (response.isSuccessful()) {
                    DriverFinancialAccountModel model = response.body();
                    r = model.getRecruitment();
                    DecimalFormat dc = new DecimalFormat("###,###,###");
                    String rial = String.valueOf(dc.format(model.getRecruitment())) + " " + "ریال";
                    tvAmountOfCreditor.setText(rial);
                    int i = r -d;
                    DecimalFormat dcc = new DecimalFormat("###,###,###");
                    String riall = String.valueOf(dcc.format(i)) + " " + "ریال";
                    tvAmountOfInventory.setText(riall);

                }else {
                    Log.i(TAG, "onRecruitmentResponse: " + response.code() + " " + response.message());
                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DriverFinancialAccountModel> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
