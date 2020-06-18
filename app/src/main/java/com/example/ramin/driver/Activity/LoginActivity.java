package com.example.ramin.driver.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ramin.driver.MainActivity;
import com.example.ramin.driver.Model.LoginModel;
import com.example.ramin.driver.Network.GetDriverData;
import com.example.ramin.driver.Network.RetrofitDriverInstance;
import com.example.ramin.driver.R;
import com.example.ramin.driver.Preferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG ="TAG";
    TextInputLayout userNameLayout,passwordLayout;
    EditText etUserName,etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViews();
    }

    private void findViews() {
        etPassword = findViewById(R.id.et_password);
        etUserName = findViewById(R.id.et_user_name);
        userNameLayout = findViewById(R.id.et_user_name_layout);
        passwordLayout = findViewById(R.id.et_password_layout);
        btnLogin = findViewById(R.id.btn_login);
    }

    public void onBtnLoginClick(View v) {
        if (etUserName.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
            if (etUserName.getText().toString().isEmpty()) {
                userNameLayout.setError("نام کاربری خود را وارد نمایید.");
            } else {
                userNameLayout.setErrorEnabled(false);
            }
            if (etPassword.getText().toString().isEmpty()) {
                passwordLayout.setError("رمز عبور خود را وارد نمایید.");
            } else {
                passwordLayout.setErrorEnabled(false);
            }

            etUserName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etUserName.getText().toString().isEmpty()) {
                        userNameLayout.setError("نام کاربری خود را وارد نمایید.");
                    } else {
                        userNameLayout.setErrorEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etPassword.getText().toString().isEmpty()) {
                        passwordLayout.setError("رمز عبور خود را وارد نمایید.");
                    } else {
                        passwordLayout.setErrorEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        } else {
            String user = etUserName.getText().toString();
            String pass = etPassword.getText().toString();
            GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
            Call<LoginModel> call = api.getDriverLogin(user,pass);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    switch (response.body().getResponse()) {
                        case "SUCCESS":
                            LoginModel loginModel = response.body();
                            String userName = etUserName.getText().toString();
                            String password = etPassword.getText().toString();
                            Preferences preferences = new Preferences(LoginActivity.this);
                            preferences.setDriverSharedPreferences(loginModel.getDriverId(), loginModel.getDriverName(), loginModel.getDriverFamily(), loginModel.getDriverImage(), userName, password);
                            Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainActivity);
                            finish();
                            break;
                        case "FAILED":
                            Toast.makeText(getApplicationContext(), "نام کاربری یا رمز عبور را اشتباه وارد کرده اید!", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Log.i(TAG, "onLoginResponse: " + response.code() + " " + response.message());
                            Toast.makeText(LoginActivity.this, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(LoginActivity.this, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
