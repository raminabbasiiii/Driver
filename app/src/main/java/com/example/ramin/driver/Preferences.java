package com.example.ramin.driver;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private final Context context;
    private static final String myPref = "DriverPreferences";
    private static final String driverName ="driverName";
    private static final String driverFamily = "driverFamily";
    private static final String driverImage = "driverImage";
    private static final String driverUserName = "driverUserName";
    private static final String driverPassword = "driverPassword";
    private static final String driverId = "driverId";
    //private SharedPreferences.Editor editor;
    //private final SharedPreferences settings;
    private final SharedPreferences driverPreferences;
    //private static final String firstVisit = "firstVisit";

    public Preferences(Context context) {
        this.context = context;
        //settings = context.getSharedPreferences("MY_PREFERENCES", 0);
        driverPreferences = context.getSharedPreferences(myPref,Context.MODE_PRIVATE);
    }

    /*public boolean isActivityVisitedForFirstTime() {
        final boolean ret = settings.getBoolean(firstVisit, true);
        if (ret) {
            editor = settings.edit();
            editor.putBoolean(firstVisit, false);
            editor.apply();
        }
        return ret;
    }*/

    public void exitAccount() {
        SharedPreferences.Editor editor = driverPreferences.edit();
            //editor.remove(firstVisit);
            editor.clear();
            editor.apply();
           // editor.apply();
    }

    public void setDriverSharedPreferences (int dId,String dName,String dFamily,String dImage,String dUser,String dPass) {

        SharedPreferences.Editor editor = driverPreferences.edit();
        editor.putInt(driverId,dId);
        editor.putString(driverName,dName);
        editor.putString(driverFamily,dFamily);
        editor.putString(driverImage,dImage);
        editor.putString(driverUserName,dUser);
        editor.putString(driverPassword,dPass);
        editor.apply();

    }

    public int getDriverId() {
        int dId = 0;
        if (driverPreferences.contains(driverId)) {
            dId = driverPreferences.getInt(driverId,0);
        }
        return dId;
    }

    public String getDriverName() {
        String dName = null;
        if (driverPreferences.contains(driverName)) {
            dName = driverPreferences.getString(driverName,"not exist");
        }
        return dName;
    }

    public String getDriverFamily() {
        String dFamily = null;
        if (driverPreferences.contains(driverFamily)) {
            dFamily = driverPreferences.getString(driverFamily,"not exist");
        }
        return dFamily;
    }

    public String getDriverImage() {
        String dImage = null;
        if (driverPreferences.contains(driverImage)) {
            dImage = driverPreferences.getString(driverImage,"not exist");
        }
        return dImage;
    }

    public String getDriverUserName() {
        String dUser = null;
        if (driverPreferences.contains(driverUserName)) {
            dUser = driverPreferences.getString(driverUserName,"not exist");
        }
        return dUser;
    }

    public String getDriverPassword() {
        String dPassword = null;
        if (driverPreferences.contains(driverPassword)) {
            dPassword = driverPreferences.getString(driverPassword,"not exist");
        }
        return dPassword;
    }
}
