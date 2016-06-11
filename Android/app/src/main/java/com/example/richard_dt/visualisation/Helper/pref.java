package com.example.richard_dt.visualisation.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class pref {
    public static final String MY_PREF = "MyPreferences";
    public static final String DEFAULT_URL="http://192.168.215.27:3000/api/";
    public static final int DEFAULT_START_HOUR= 7;
    public static final int DEFAULT_ZOOM= 70;
    public static  final  int DEFAULT_RAYON=30;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public pref(Context context) {
        this.sharedPreferences = context.getSharedPreferences(MY_PREF, 0);
        this.editor = this.sharedPreferences.edit();
    }
    public void setString(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    public void setInt(String key, int value) {
        this.editor.putInt(key, value);
        this.editor.commit();
    }

    public String getString(String key) {
        switch (key) {
            case "urlApi":
                return this.sharedPreferences.getString(key,DEFAULT_URL);
        }
        return null;
    }
    public int getInt(String key){
        switch (key){
            case "startHour":
                return this.sharedPreferences.getInt(key,DEFAULT_START_HOUR);
            case "zoom":
                return this.sharedPreferences.getInt(key,DEFAULT_ZOOM);
            case "rayon":
                return this.sharedPreferences.getInt(key,DEFAULT_RAYON);
        }
        return 0;
    }


    public void clear(String key) {
        this.editor.remove(key);
        this.editor.commit();
    }
    public static SharedPreferences getSharedPreferences (Context ctxt) {
        return ctxt.getSharedPreferences(MY_PREF, 0);
    }

    public void clear() {
        this.editor.clear();
        this.editor.commit();
    }
}
