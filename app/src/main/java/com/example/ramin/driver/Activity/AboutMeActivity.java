package com.example.ramin.driver.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.driver.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutMeActivity extends AppCompatActivity {

    Toolbar aboutMeToolbar;
    TextView tvToolbarTitle;
    WebView aboutMeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();

        aboutMeWebView = findViewById(R.id.about_me_web_view);
        String siteUrl = "file:///android_asset/about.html";
        aboutMeWebView.loadUrl(siteUrl);
    }

    private void setToolbar() {
        aboutMeToolbar = findViewById(R.id.about_me_toolbar);
        tvToolbarTitle = aboutMeToolbar.findViewById(R.id.about_me_toolbar_title);
        setSupportActionBar(aboutMeToolbar);
        tvToolbarTitle.setText(R.string.about_me);
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
