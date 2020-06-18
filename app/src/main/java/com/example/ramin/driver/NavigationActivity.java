package com.example.ramin.driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.driver.Adapter.RecyclerSearchLocationAdapter;
import com.example.ramin.driver.Model.DirectionModel.Leg;
import com.example.ramin.driver.Model.DirectionModel.LegsDistance;
import com.example.ramin.driver.Model.DirectionModel.NeshanDirection;
import com.example.ramin.driver.Model.DirectionModel.Route;
import com.example.ramin.driver.Model.DirectionModel.Step;
import com.example.ramin.driver.Model.SearchMap.Item;
import com.example.ramin.driver.Model.SearchMap.NeshanSearch;
import com.example.ramin.driver.Network.GetDataService;
import com.example.ramin.driver.Network.RetrofitClientInstance;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.neshan.core.Bounds;
import org.neshan.core.LngLat;
import org.neshan.core.LngLatVector;
import org.neshan.core.Range;
import org.neshan.core.ViewportBounds;
import org.neshan.core.ViewportPosition;
import org.neshan.geometry.LineGeom;
import org.neshan.graphics.ARGB;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.styles.LineStyle;
import org.neshan.styles.LineStyleCreator;
import org.neshan.styles.MarkerStyle;
import org.neshan.styles.MarkerStyleCreator;
import org.neshan.ui.MapView;
import org.neshan.utils.BitmapUtils;
import org.neshan.vectorelements.Line;
import org.neshan.vectorelements.Marker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NavigationActivity extends AppCompatActivity implements RecyclerSearchLocationAdapter.OnSearchItemListener{

    private static final String TAG ="TAG";
    final int BASE_MAP_INDEX = 0;
    final int REQUEST_CODE = 123;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static double originLng;
    public static double originLat;
    public static double destinationLng;
    public static double destinationLat;
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    List<Item> items;
    List<PolylineEncoding.LatLng> decodedStepByStepPath;
    List<Route> routes = new ArrayList<>();
    List<Leg> legs;
    String lastUpdateTime,distance,summary,duration;
    Boolean mRequestingLocationUpdates;
    MapView map;
    VectorElementLayer userMarkerLayer,originMarkerLayer,destinationMarkerLayer,lineLayer;
    FloatingActionButton fab;
    Toolbar navigationToolbar;
    RecyclerSearchLocationAdapter adapter;
    RecyclerView searchLocationRecycler;
    EditText etOriginSearchLocation,etDestinationSearchLocation;
    Button btnDirection,btnInsertPath,btnDriving;
    boolean overview = false;
    BottomSheetBehavior sheetBehavior;
    ConstraintLayout constraintLayout;
    TextView tvSummary,tvDistance,tvDuration;
    String pathTripOne = "";
    String pathTripTwo = "";
    boolean clicked= false;
    List<Double> all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // everything related to ui is initialized here
        initLayoutReferences();
        initLocation();
       startReceivingLocationUpdates();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        startLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdate();
    }

    // Initializing layout references (views, map and map events)
    private void initLayoutReferences(){
        //initialising views

        initViews();
        // Initializing mapView element

        initMap();

        etOriginSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchLocationRecycler.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                constraintLayout.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                doSearch(s.toString());
            }
        });

        etDestinationSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchLocationRecycler.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                constraintLayout.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                doSearch(s.toString());
            }
        });
    }

    // We use findViewByID for every element in our layout file here
    private void initViews() {

        map = findViewById(R.id.map);

        fab = findViewById(R.id.location_fab);

        navigationToolbar = findViewById(R.id.navigation_toolbar);
        etOriginSearchLocation = navigationToolbar.findViewById(R.id.et_origin_search_location);
        etDestinationSearchLocation = navigationToolbar.findViewById(R.id.et_destination_search_location);
        btnDirection = findViewById(R.id.btn_direction);
        setSupportActionBar(navigationToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchLocationRecycler = findViewById(R.id.search_location_recycler);
        items = new ArrayList<>();
        adapter = new RecyclerSearchLocationAdapter(items,NavigationActivity.this);
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(NavigationActivity.this);
        searchLocationRecycler.setLayoutManager(regLayoutManager);
        searchLocationRecycler.setItemAnimator(new DefaultItemAnimator());
        searchLocationRecycler.setHasFixedSize(true);
        searchLocationRecycler.setAdapter(adapter);
        searchLocationRecycler.setVisibility(View.GONE);

        constraintLayout = findViewById(R.id.constraint_sheet);
        sheetBehavior = BottomSheetBehavior.from(constraintLayout);
        tvDistance = constraintLayout.findViewById(R.id.tv_distance);
        tvSummary = constraintLayout.findViewById(R.id.tv_summary);
        tvDuration = constraintLayout.findViewById(R.id.tv_duration);
        btnInsertPath = constraintLayout.findViewById(R.id.btn_insert_path);
        btnDriving = constraintLayout.findViewById(R.id.btn_driving);

        constraintLayout.setVisibility(View.GONE);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    // Initializing map
    private void initMap() {
        // Creating a VectorElementLayer(called markerLayer) to add all markers to it and adding it to map's layers
        //userMarkerLayer = NeshanServices.createVectorElementLayer();
        userMarkerLayer = NeshanServices.createVectorElementLayer();
        originMarkerLayer = NeshanServices.createVectorElementLayer();
        destinationMarkerLayer = NeshanServices.createVectorElementLayer();
        lineLayer = NeshanServices.createVectorElementLayer();
        map.getLayers().add(userMarkerLayer);
        map.getLayers().add(originMarkerLayer);
        map.getLayers().add(destinationMarkerLayer);
        map.getLayers().add(lineLayer);
        //map.getLayers().add(userMarkerLayer);

        // add Standard_day map to layer BASE_MAP_INDEX
        map.getOptions().setZoomRange(new Range(4.5f,18f));
        map.getLayers().insert(BASE_MAP_INDEX,NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));

        // Setting map focal position to a fixed position and setting camera zoom
        map.setFocalPointPosition(new LngLat(51.330743, 35.767234),0);
        map.setZoom(14,0);
    }

    private void initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                userLocation = locationResult.getLastLocation();
                lastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                onLocationChange();
            }
        };

        mRequestingLocationUpdates = false;
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdate() {
        settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG,"All location settings are satisfied.");

                        //noinspection MissingPermission
                        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());

                        onLocationChange();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(NavigationActivity.this, REQUEST_CODE);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(NavigationActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        onLocationChange();
                    }
                });
    }

    public void stopLocationUpdate() {
        // Removing location updates
        fusedLocationClient
                .removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void startReceivingLocationUpdates() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdate();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void onLocationChange() {
        if (userLocation != null) {
            userMarkerLayer.clear();
            userAddMarker(new LngLat(userLocation.getLongitude(),userLocation.getLatitude()),20f);
        }
    }

    public void focusOnUserLocation(View view) {
        if(userLocation != null) {
            //userMarkerLayer.clear();
            userMarkerLayer.clear();
            map.setFocalPointPosition(
                    new LngLat(userLocation.getLongitude(), userLocation.getLatitude()), 0.25f);
            map.setZoom(20, 0.25f);
        }
    }

    private void doSearch(String term) {
        final double lat = map.getFocalPointPosition().getY();
        final double lng = map.getFocalPointPosition().getX();
        final String requestURL = "https://api.neshan.org/v1/search?term=" + term + "&lat=" + lat + "&lng=" + lng;

        GetDataService api = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<NeshanSearch> call = api.getNeshanSearch(requestURL);

        call.enqueue(new Callback<NeshanSearch>() {
            @Override
            public void onResponse(Call<NeshanSearch> call, Response<NeshanSearch> response) {
                if (response.isSuccessful()) {
                    NeshanSearch neshanSearch = response.body();
                    items = neshanSearch.getItems();
                    adapter.updateList(items);
                } else {
                    Log.i(TAG, "onResponse: " + response.code() + " " + response.message());
                        Toast.makeText(NavigationActivity.this, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NeshanSearch> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(NavigationActivity.this, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void direction() {

        final String requestURL = "https://api.neshan.org/v2/direction?origin="+ originLng + "," + originLat + "&destination="+ destinationLng + "," + destinationLat;

        GetDataService api = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<NeshanDirection> call = api.getNeshanDirection(requestURL);

        call.enqueue(new Callback<NeshanDirection>() {
            @Override
            public void onResponse(Call<NeshanDirection> call, Response<NeshanDirection> response) {

                if (response.isSuccessful()) {
                    NeshanDirection neshanDirection = response.body();
                    routes = neshanDirection.getRoutes();
                }
                else {
                    Log.i(TAG, "onDirectionResponse: " + response.code() + " " + response.message());
                    Toast.makeText(NavigationActivity.this, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NeshanDirection> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(NavigationActivity.this, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userAddMarker(LngLat lngLat, float size) {
        Marker marker = new Marker(lngLat, getUserMarkerStyle(size));
        userMarkerLayer.add(marker);
    }

    private MarkerStyle getUserMarkerStyle(float size) {
        MarkerStyleCreator styleCreator = new MarkerStyleCreator();
        styleCreator.setSize(size);
        styleCreator.setBitmap
                (BitmapUtils.createBitmapFromAndroidBitmap
                        (BitmapFactory.decodeResource(getResources(), R.drawable.your_location)));
        return styleCreator.buildStyle();
    }

    private void originAddMarker(LngLat lngLat, float size) {
        Marker marker = new Marker(lngLat, getOriginMarkerStyle(size));
        originMarkerLayer.add(marker);
    }

    private MarkerStyle getOriginMarkerStyle(float size) {
        MarkerStyleCreator styleCreator = new MarkerStyleCreator();
        styleCreator.setSize(size);
        styleCreator.setBitmap
                (BitmapUtils.createBitmapFromAndroidBitmap
                        (BitmapFactory.decodeResource(getResources(), R.drawable.origin)));
        return styleCreator.buildStyle();
    }

    private void destinationAddMarker(LngLat lngLat, float size) {
        Marker marker = new Marker(lngLat, getDestinationMarkerStyle(size));
        destinationMarkerLayer.add(marker);
    }

    private MarkerStyle getDestinationMarkerStyle(float size) {
        MarkerStyleCreator styleCreator = new MarkerStyleCreator();
        styleCreator.setSize(size);
        styleCreator.setBitmap
                (BitmapUtils.createBitmapFromAndroidBitmap
                        (BitmapFactory.decodeResource(getResources(), R.drawable.destination)));
        return styleCreator.buildStyle();
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onSearchItemClick(double lat, double lng, String title) {
        closeKeyBoard();

        if (this.getCurrentFocus().getId() == etOriginSearchLocation.getId()) {
            etOriginSearchLocation.setText(title);
            originLng = lng;
            originLat = lat;
        } else {
            etDestinationSearchLocation.setText(title);
            destinationLng = lng;
            destinationLat = lat;
        }
        searchLocationRecycler.setVisibility(View.GONE);

        originMarkerLayer.clear();
        destinationMarkerLayer.clear();
        adapter.updateList(new ArrayList<Item>());
        LngLat lngLat = new LngLat(lat,lng);
        map.setFocalPointPosition(lngLat, .5f);
        map.setZoom(10f, .5f);
        if (this.getCurrentFocus().getId() == etOriginSearchLocation.getId()) {
            originAddMarker(lngLat, 50f);
        } else {
            destinationAddMarker(lngLat, 50f);
        }
        direction();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public void brnDirectionOnclick(View v){

        if (etOriginSearchLocation.getText().toString().isEmpty() || etDestinationSearchLocation.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),"لطفا مبدا و مقصد را وارد کنید!!",Toast.LENGTH_LONG).show();
        } else {
            lineLayer.clear();
            fab.setVisibility(View.VISIBLE);

            distance = routes.get(0).getLegs().get(0).getDistance().getText();
            summary = routes.get(0).getLegs().get(0).getSummary();
            duration = routes.get(0).getLegs().get(0).getDuration().getText();

            List<Step> stepByStepPath = routes.get(0).getLegs().get(0).getSteps();
            decodedStepByStepPath = new ArrayList<>();
            for (int i = 0; i < stepByStepPath.size(); i++) {
                List<PolylineEncoding.LatLng> decodedEachStep = PolylineEncoding.decode(stepByStepPath.get(i).getPolyline());
                decodedStepByStepPath.addAll(decodedEachStep);
            }

            drawLineGeom(decodedStepByStepPath);

            constraintLayout.setVisibility(View.VISIBLE);

            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            tvDuration.setText(duration);
            tvSummary.setText(summary);
            tvDistance.setText(distance);
        }

    }

    private void mapSetPosition(boolean overview) {
        double centerFirstMarkerX = originLat;
        double centerFirstMarkerY = originLng;
        if (overview) {
            double centerFocalPositionX = (centerFirstMarkerX) / 2;
            double centerFocalPositionY = (centerFirstMarkerY ) / 2;
            map.setFocalPointPosition(new LngLat(centerFocalPositionX, centerFocalPositionY),0.5f );
            map.setZoom(14,0.5f);
        } else {

            double minLat = Double.MAX_VALUE;
            double minLng = Double.MAX_VALUE;
            double maxLat = Double.MIN_VALUE;
            double maxLng = Double.MIN_VALUE;
            minLat = Math.min(originLng, minLat);
            minLng = Math.min(originLat, minLng);
            maxLat = Math.max(destinationLng, maxLat);
            maxLng = Math.max(destinationLat, maxLng);
            //map.setFocalPointPosition(new LngLat(centerFirstMarkerX, centerFirstMarkerY),0.5f );
            map.moveToCameraBounds(new Bounds(new LngLat(minLng, minLat), new LngLat(maxLng, maxLat)),new ViewportBounds(new ViewportPosition(0, 0), new ViewportPosition(map.getWidth(), map.getHeight())),true, 0.5f);
           // map.setZoom(8,0.5f);
            originMarkerLayer.clear();
            destinationMarkerLayer.clear();
            originAddMarker(new LngLat(originLat,originLng),50f);
            destinationAddMarker(new LngLat(destinationLat,destinationLng),50f);
        }

    }

    public LineGeom drawLineGeom(List<PolylineEncoding.LatLng> paths) {
        // we clear every line that is currently on map
        lineLayer.clear();
        // Adding some LngLat points to a LngLatVector
        LngLatVector lngLatVector = new LngLatVector();
        for (PolylineEncoding.LatLng path : paths) {
            lngLatVector.add(new LngLat(path.lng, path.lat));
        }

        // Creating a lineGeom from LngLatVector
        LineGeom lineGeom = new LineGeom(lngLatVector);
        // Creating a line from LineGeom. here we use getLineStyle() method to define line styles
        Line line = new Line(lineGeom, getLineStyle());
        // adding the created line to lineLayer, showing it on map
        lineLayer.add(line);
        // focusing camera on first point of drawn line
        mapSetPosition(overview);
        return lineGeom;
    }

    private LineStyle getLineStyle(){
        LineStyleCreator lineStCr = new LineStyleCreator();
        lineStCr.setColor(new ARGB((short) 2, (short) 119, (short) 189, (short)190));
        lineStCr.setWidth(10f);
        lineStCr.setStretchFactor(0f);
        return lineStCr.buildStyle();
    }

    public void btnInsertPathOnClick(View v) {

        /*double minLat = Double.MAX_VALUE;
        double minLng = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double maxLng = Double.MIN_VALUE;
        minLat = Math.min(originLng, minLat);
        minLng = Math.min(originLat, minLng);
        maxLat = Math.max(destinationLng, maxLat);
        maxLng = Math.max(destinationLat, maxLng);

        //String a ;
        List<String> list = new ArrayList<>();
        List<Step> stepByStepPath = routes.get(0).getLegs().get(0).getSteps();

        for (int i=0; i<stepByStepPath.size();i++) {
            pathTripOne += stepByStepPath.get(i).getPolyline();
            pathTripOne += "PumkiN";

            //list.add(a +"PumkiN");
        }*/


        /*for (int i=0; i<list.size();i++) {
            pathTrip += list.get(i);
            //pathTrip += "PumkiN";
            Log.i("ramin",pathTrip);
        }*/

        String origin = etOriginSearchLocation.getText().toString();
        String destination = etDestinationSearchLocation.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("origin",origin);
        intent.putExtra("destination",destination);
        intent.putExtra("distance",distance);
       /* intent.putExtra("minLng",minLng);
        intent.putExtra("minLat",minLat);
        intent.putExtra("maxLng",maxLng);
        intent.putExtra("maxLat",maxLat);
        //intent.putStringArrayListExtra("steps",(ArrayList<String>) list);
        intent.putExtra("stepOne",pathTripOne);
        //intent.putExtra("stepTwo",pathTripTwo);*/
        setResult(RESULT_OK,intent);
        finish();
    }

    public void btnDrivingOnClick (View v) {

        clicked = true;

        map.setFocalPointPosition(new LngLat(originLat, originLng),0.5f );
        map.setZoom(24,0.5f);
        map.setTilt(10,5f);
        map.setBearing(map.getBearing() + 90,5f);
    }
}
