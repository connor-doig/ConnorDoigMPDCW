package com.example.doig_connor_s1823609;
// Connor Doig S1823609
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Constant {
    // save arrayList data from sharedPreference
    public static void setEarthQuakesList(Context context, ArrayList<Earthquake> earthquakeArrayList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        // convert arrayList in json
        String json = gson.toJson(earthquakeArrayList);
        prefs.edit().putString("earthquake", json).commit();
    }
      // get arrayList data from sharedPreference
    public static ArrayList<Earthquake> getEarthQuakesList(Context context) {
        ArrayList<Earthquake> weatherArrayList =null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString("earthquake", null);
        Type type = new TypeToken<ArrayList<Earthquake>>() {
        }.getType();
        // convert json in arrayList
        weatherArrayList= gson.fromJson(json, type);
        return weatherArrayList;
    }

    // get date in sharedPreference
    public static String getDate(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("data","");
    }


    // save date in sharedPreference
    public static void setDate(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("data", s).commit();
    }
    // get current date
    public static String getCurrentDate(){
        Date date = Calendar.getInstance().getTime();
        // set the date format
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return   df.format(date);
    }
}
