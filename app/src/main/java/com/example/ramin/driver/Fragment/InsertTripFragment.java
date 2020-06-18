package com.example.ramin.driver.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.driver.Model.InsertTripsModel;
import com.example.ramin.driver.NavigationActivity;
import com.example.ramin.driver.Network.GetDriverData;
import com.example.ramin.driver.Network.RetrofitDriverInstance;
import com.example.ramin.driver.Preferences;
import com.example.ramin.driver.R;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class InsertTripFragment extends Fragment implements View.OnClickListener {

    TextInputLayout etOriginLayout,etDestinationLayout,etDateLayout,etTimeLayout,etStoppingLayout;
    EditText etOrigin,etDestination,etDate,etTime,etStopping;
    View root;
    Spinner chairSpinner,shipmentCapacitySpinner;
    List<String> chairCount,shipmentCapacity;
    CheckBox shipmentCheckBox;
    TextView tvDistance,tvPrice;
    Button btnInsertTrip;
    String origin,destination,distance="0",chairEmpty,capacity;
    public static final int basePrice = 5000;
    public static final int requestCode = 1;
    private static final String TAG ="TAG";
    int price = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_insert_trip, container, false);

        findViews();
        setChairSpinner();
        setShipmentCapacitySpinner();

        return root;
    }

    private void findViews() {

        etOrigin = root.findViewById(R.id.et_origin);
        etOriginLayout = root.findViewById(R.id.et_origin_layout);
        etOrigin.setOnClickListener(this);

        etDestination = root.findViewById(R.id.et_destination);
        etDestinationLayout = root.findViewById(R.id.et_destination_layout);
        etDestination.setOnClickListener(this);

        etDate = root.findViewById(R.id.et_date);
        etDateLayout = root.findViewById(R.id.et_date_layout);
        etDate.setOnClickListener(this);

        etTime = root.findViewById(R.id.et_time);
        etTimeLayout = root.findViewById(R.id.et_time_layout);
        etTime.setOnClickListener(this);

        etStopping = root.findViewById(R.id.et_stopping);
        etStoppingLayout = root.findViewById(R.id.et_stopping_layout);

        shipmentCheckBox = root.findViewById(R.id.shipment_checkbox);
        shipmentCheckBox.setOnClickListener(this);

        tvDistance = root.findViewById(R.id.tv_dis);
        tvPrice = root.findViewById(R.id.tv_price);

        btnInsertTrip = root.findViewById(R.id.btn_insert_trip);
        btnInsertTrip.setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                origin = data.getStringExtra("origin");
                destination =data.getStringExtra("destination");
                distance =data.getStringExtra("distance");
                etOrigin.setText(origin);
                etDestination.setText(destination);
                tvDistance.setText(distance);
                calculatorPrice();
            }
        }
    }

    private void calculatorPrice() {

        if (distance != null) {
            String digit = drawDigitsFromString(distance);
            price = basePrice * Integer.parseInt(digit);
            DecimalFormat d = new DecimalFormat("###,###,###");
            String rial = String.valueOf(d.format(price)) + " " + "ریال";
            tvPrice.setText(rial);
        }
    }

    private String drawDigitsFromString(String strValue){
        String str = strValue.trim();
        String digits="";
        for (int i = 0; i < str.length(); i++) {
            char chrs = str.charAt(i);
            if (Character.isDigit(chrs))
                digits = digits+chrs;
        }
        return digits;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.et_origin :
                Bundle sendData = new Bundle();
                sendData.putInt("requestCode",requestCode);
                Intent navigationActivity1 = new Intent(getContext(),NavigationActivity.class);
                startActivityForResult(navigationActivity1,1);
                break;

            case R.id.et_destination :
                Intent navigationActivity2 = new Intent(getContext(),NavigationActivity.class);
                startActivityForResult(navigationActivity2,1);
                break;

            case R.id.et_date :
                persianDatePicker();
                break;

            case R.id.et_time :
                persianTimePicker();
                break;

            case R.id.shipment_checkbox : {
                if (shipmentCheckBox.isChecked()) {
                    shipmentCapacitySpinner.setVisibility(View.VISIBLE);
                } else {
                    shipmentCapacitySpinner.setVisibility(View.GONE);
                }
            }
            break;

            case R.id.btn_insert_trip :
                insertTrip();
                break;
        }
    }

    public void setChairSpinner() {

        chairSpinner = root.findViewById(R.id.chair_spinner);
        chairCount = new ArrayList<>();
        chairCount.add("تعداد صندلی خالی را انتخاب کنید :");
        chairCount.add("1");
        chairCount.add("2");
        chairCount.add("3");
        chairCount.add("4");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item_selected,chairCount);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chairSpinner.setAdapter(adapter);

        chairSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        chairEmpty = parent.getItemAtPosition(position).toString();
                        break;
                    case 2 :
                        chairEmpty = parent.getItemAtPosition(position).toString();
                        break;
                    case 3:
                        chairEmpty = parent.getItemAtPosition(position).toString();
                        break;
                    case 4:
                        chairEmpty = parent.getItemAtPosition(position).toString();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setShipmentCapacitySpinner() {

        shipmentCapacitySpinner = root.findViewById(R.id.shipment_capacity_spinner);
        shipmentCapacity = new ArrayList<>();
        shipmentCapacity.add("ظرفیت حمل بار را انتخاب کنید :");
        shipmentCapacity.add("10 کیلو");
        shipmentCapacity.add("20 کیلو");
        shipmentCapacity.add("30 کیلو");
        shipmentCapacity.add("40 کیلو");
        shipmentCapacity.add("50 کیلو");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item_selected,shipmentCapacity);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shipmentCapacitySpinner.setAdapter(adapter);

        shipmentCapacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        capacity = parent.getItemAtPosition(position).toString();
                        break;
                    case 2 :
                        capacity = parent.getItemAtPosition(position).toString();
                        break;
                    case 3:
                        capacity = parent.getItemAtPosition(position).toString();
                        break;
                    case 4:
                        capacity = parent.getItemAtPosition(position).toString();
                        break;

                    case 5:
                        capacity = parent.getItemAtPosition(position).toString();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void persianDatePicker(){
        final PersianCalendar now = new PersianCalendar();
        final DatePickerDialog pickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                int m = monthOfYear+1;
                String persianDate = "" + year + "/" + m + "/" + dayOfMonth;
                etDate.setText(persianDate);
            }
        }, now.getPersianYear(),now.getPersianMonth(),now.getPersianDay());

        if (getActivity() != null) {
            pickerDialog.setThemeDark(true);
            pickerDialog.show(getActivity().getFragmentManager(), "tag");
        }
    }

    private void persianTimePicker(){
        PersianCalendar now = new PersianCalendar();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                String time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                etTime.setText(time);
            }
        }, now.getTime().getHours(), now.getTime().getMinutes(), true);
        if (getActivity() != null)
            timePickerDialog.setThemeDark(true);
            timePickerDialog.show(getActivity().getFragmentManager(),"tag");
    }

    private void insertTrip () {

        PersianCalendar now = new PersianCalendar();
        int m = now.getPersianMonth() + 1;

        if (etOrigin.getText().toString().isEmpty() || etDestination.getText().toString().isEmpty() || etDate.getText().toString().isEmpty() || etTime.getText().toString().isEmpty() || tvDistance.getText().toString().isEmpty() || tvPrice.getText().toString().isEmpty()) {
            Toast.makeText(getContext(),"فیلدهای مورد نظر را پر کنید!!!",Toast.LENGTH_LONG).show();
        } else {

            Preferences preferences = new Preferences(getContext());

            String d = drawDigitsFromString(tvDistance.getText().toString());
            String p = drawDigitsFromString(tvPrice.getText().toString());
            int distance = Integer.parseInt(d);
            String origin = etOrigin.getText().toString();
            String destination = etDestination.getText().toString();
            String date = etDate.getText().toString();
            String time = etTime.getText().toString();
            int pricee = Integer.parseInt(p);
            int driverId = preferences.getDriverId();
            String stopping = etStopping.getText().toString();
            int emptyChair = Integer.parseInt(chairEmpty);
            String bar;
            if (shipmentCheckBox.isChecked()) {
                bar = capacity;
            } else {
                bar = "ندارد";
            }

                GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
                Call<InsertTripsModel> call = api.insertTrips(distance, origin, destination, date, time, pricee, driverId, stopping, emptyChair, bar);
                call.enqueue(new Callback<InsertTripsModel>() {
                    @Override
                    public void onResponse(Call<InsertTripsModel> call, Response<InsertTripsModel> response) {
                        switch (response.body().getResponse()) {
                            case "SUCCESS":
                                Toast.makeText(getContext(), "سفر ثبت شد!", Toast.LENGTH_LONG).show();
                                break;
                            case "FAILED":
                                Toast.makeText(getContext(), "سفر ثبت نشد!!!", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.i(TAG, "onInsertTripResponse: " + response.code() + " " + response.message());
                                Toast.makeText(getContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<InsertTripsModel> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(getContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
}