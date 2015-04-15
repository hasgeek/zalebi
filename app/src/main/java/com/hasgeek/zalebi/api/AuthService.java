package com.hasgeek.zalebi.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by karthikbalakrishnan on 15/04/15.
 */
public class AuthService {

    private static SharedPreferences pref;


    public AuthService(Context ctx) {
        pref = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void addToPref(String key, String value) {
        pref.edit().putString(key, value).apply();
    }

    public static String getFromPref(String key) {
        return pref.getString(key, null);
    }

    public static void deleteFromPref(String key) {
        pref.edit().remove(key).apply();
    }

    public static void saveUserToken(String access_token) {
        addToPref("access_token", access_token);
    }

    public static void deleteUserToken() {
        deleteFromPref("access_token");
    }

    public static boolean isLoggedIn() {
        if(getFromPref("access_token")!=null) {
            return true;
        }
        return false;
    }

    public static String getAuthHeader() {
        if(isLoggedIn()) {
            return "Bearer "+getFromPref("access_token");
        }
        return "";
    }
}
