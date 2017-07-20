package com.telivercustomer;

import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.teliver.sdk.core.Teliver;


public class Application extends MultiDexApplication {

    public static final String TRACKING_ID = "tracking_id";

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        Teliver.init(this,"your_teliver_key");
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void storeBooleanInPref(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }


    public boolean getBooleanInPef(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void storeStringInPref(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringInPref(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void deletePreference(){
        editor.clear();
        editor.commit();
    }

}
