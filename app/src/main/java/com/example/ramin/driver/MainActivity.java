package com.example.ramin.driver;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ramin.driver.Activity.AboutMeActivity;
import com.example.ramin.driver.Activity.CalculatorActivity;
import com.example.ramin.driver.Activity.LoginActivity;
import com.example.ramin.driver.Activity.TripsActivity;
import com.example.ramin.driver.Fragment.InsertTripFragment;
import com.example.ramin.driver.Fragment.PassengersListFragment;
import com.example.ramin.driver.Fragment.RegisteredTripsFragment;

import java.lang.reflect.Field;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mainToolbar;
    BottomNavigationView mainBottomNav;
    DrawerLayout mainDrawerLayout;
    NavigationView mainNavigationView;
    TextView toolbarTitle;
    String getOrigin;
    String getDestination;
    String getDistance;
    TextView tvDriverName;
    CircleImageView driverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        setNavigationDrawer();
        serNavigationViewHeader();
        setBottomNavigationClickListener();
        disableShiftMode(mainBottomNav);
        setDefaultFragment();

       /* Bundle getData = getIntent().getExtras();
        if (getData != null) {
            getOrigin = getData.getString("origin");
            getDestination = getData.getString("destination");
            getDistance = getData.getString("distance");
        }*/

        Preferences p = new Preferences(this);

        if (p.getDriverUserName() == null && p.getDriverPassword() == null) {
            Intent loginActivity = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }
    }

    /*public String getGetDestination() {
        return getDestination;
    }

    public String getGetOrigin() {
        return getOrigin;
    }

    public String getGetDistance() {
        return getDistance;
    }*/

    private void setBottomNavigationClickListener() {

        mainBottomNav = findViewById(R.id.main_bottom_nav);
        mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.bn_insertTrip :
                        InsertTripFragment itFragment = new InsertTripFragment();
                        FragmentManager itManager = getSupportFragmentManager();
                        FragmentTransaction itTransaction = itManager.beginTransaction();
                        itTransaction.replace(R.id.main_frame_holder,itFragment);
                        itTransaction.commit();
                        toolbarTitle.setText(R.string.main_bottom_nav_insert_trip);
                        return true;

                    case R.id.bn_registered_trips :
                        RegisteredTripsFragment tFragment = new RegisteredTripsFragment();
                        FragmentManager tManager = getSupportFragmentManager();
                        FragmentTransaction tTransaction = tManager.beginTransaction();
                        tTransaction.replace(R.id.main_frame_holder,tFragment);
                        tTransaction.commit();
                        toolbarTitle.setText(R.string.main_bottom_nav_registered_trips);
                        return true;

                    case R.id.bn_path :
                        /*PathFragment pFragment = new PathFragment();
                        FragmentManager pManager = getSupportFragmentManager();
                        FragmentTransaction pTransaction = pManager.beginTransaction();
                        pTransaction.replace(R.id.main_frame_holder,pFragment);
                        pTransaction.commit();
                        toolbarTitle.setText(R.string.main_bottom_nav_path);*/
                        Intent navigationActivity = new Intent(MainActivity.this,NavigationActivity.class);
                        startActivity(navigationActivity);
                        return true;

                    case R.id.bn_passengerList :
                        PassengersListFragment passFragment = new PassengersListFragment();
                        FragmentManager passManager = getSupportFragmentManager();
                        FragmentTransaction passTransaction = passManager.beginTransaction();
                        passTransaction.replace(R.id.main_frame_holder,passFragment);
                        passTransaction.commit();
                        toolbarTitle.setText(R.string.main_bottom_passengers_list);
                        return true;
                }
                return false;
            }
        });

    }

    private void setToolbar() {
        mainToolbar = findViewById(R.id.main_toolbar);
        toolbarTitle  = mainToolbar.findViewById(R.id.main_toolbar_title);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void setNavigationDrawer() {
        mainDrawerLayout = findViewById(R.id.main_drawer_layout);
        mainNavigationView = findViewById(R.id.main_navigation_view);
        mainNavigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mainToggle = new ActionBarDrawerToggle(this,mainDrawerLayout,mainToolbar,R.string.open,R.string.close);
        mainDrawerLayout.addDrawerListener(mainToggle);
        mainToggle.syncState();
    }

    private void serNavigationViewHeader() {

        View headerView = mainNavigationView.getHeaderView(0);
        tvDriverName = headerView.findViewById(R.id.driver_name);
        driverImage = headerView.findViewById(R.id.driver_picture);
        Preferences preferences = new Preferences(MainActivity.this);
        String nameFamily = preferences.getDriverName() + " " + preferences.getDriverFamily();
        tvDriverName.setText(nameFamily);
        Glide.with(MainActivity.this).load(preferences.getDriverImage()).into(driverImage);

    }

    private void setDefaultFragment() {
        InsertTripFragment itFragment = new InsertTripFragment();
        FragmentManager itManager = getSupportFragmentManager();
        FragmentTransaction itTransaction = itManager.beginTransaction();
        itTransaction.replace(R.id.main_frame_holder,itFragment);
        itTransaction.commit();
        toolbarTitle.setText(R.string.main_bottom_nav_insert_trip);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void disableShiftMode (BottomNavigationView view) {

        BottomNavigationMenuView menuView = (BottomNavigationMenuView)view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView,false);
            shiftingMode.setAccessible(false);
            for (int i=0; i<menuView.getChildCount(); i++)
            {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        }catch (NoSuchFieldException e) {
            Log.e("BNVHelper","unable to get shift mode",e);
        }catch (IllegalAccessException e) {
            Log.e("BNVHelper","unable to get shift mode",e);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.calculating :
                Intent calculatorActivity = new Intent(MainActivity.this,CalculatorActivity.class);
                startActivity(calculatorActivity);
                break;

            case R.id.trips :
                Intent tripsActivity = new Intent(MainActivity.this,TripsActivity.class);
                startActivity(tripsActivity);
                break;

            case R.id.comments :
                Toast.makeText(getApplicationContext(),"امتیازات و نظرات",Toast.LENGTH_SHORT).show();
                break;

            case R.id.settings :
                Toast.makeText(getApplicationContext(),"تنظیمات",Toast.LENGTH_SHORT).show();
                break;

            case R.id.about_me :
                Intent aboutMeActivity = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(aboutMeActivity);
                break;

            case R.id.exit :
                Preferences preferences = new Preferences(this);
                preferences.exitAccount();
                Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginActivity);
                finish();
                break;
            case R.id.driver_name :

        }

        mainDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {

            mainDrawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
            finish();
        }
    }
}
