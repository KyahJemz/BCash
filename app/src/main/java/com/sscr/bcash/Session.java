package com.sscr.bcash;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
    }

    // SET
    public static void setFirstName(Context context, String firstname) {
        getPreferences(context).edit().putString("userFirstname", firstname).apply();
    }
    public static void setLastName(Context context, String lastname) {
        getPreferences(context).edit().putString("userLastname", lastname).apply();
    }
    public static void setEmail(Context context, String email) {
        getPreferences(context).edit().putString("userEmail", email).apply();
    }
    public static void setPersonalId(Context context, String personalId) {
        getPreferences(context).edit().putString("userPersonalId", personalId).apply();
    }
    public static void setProfileImage(Context context, String profileImage) {
        getPreferences(context).edit().putString("userProfileImage", profileImage).apply();
    }
    public static void setToken(Context context, String token) {
        getPreferences(context).edit().putString("userToken", token).apply();
    }
    public static void setIpAddress(Context context, String token) {
        getPreferences(context).edit().putString("userIpAddress", token).apply();
    }
    public static void setLocation(Context context, String token) {
        getPreferences(context).edit().putString("userLocation", token).apply();
    }
    public static void setAccountAddress(Context context, String token) {
        getPreferences(context).edit().putString("userAccountAddress", token).apply();
    }
    public static void setAuthorization(Context context, String token) {
        getPreferences(context).edit().putString("userAuthorization", token).apply();
    }
    public static void setIntent(Context context, String token) {
        getPreferences(context).edit().putString("userIntent", token).apply();
    }
    public static void setBalance(Context context, String token) {
        getPreferences(context).edit().putString("userBalance", token).apply();
    }
    public static void setActorCategory(Context context, String token) {
        getPreferences(context).edit().putString("userActorCategory", token).apply();
    }



    // GET
    public static String getFirstName(Context context) {
        return getPreferences(context).getString("userFirstname", null);
    }
    public static String getLastName(Context context) {
        return getPreferences(context).getString("userLastname", null);
    }
    public static String getEmail(Context context) {
        return getPreferences(context).getString("userEmail", null);
    }
    public static String getPersonalId(Context context) {
        return getPreferences(context).getString("userPersonalId", null);
    }
    public static String getProfileImage(Context context) {
        return getPreferences(context).getString("userProfileImage", null);
    }
    public static String getToken(Context context) {
        return getPreferences(context).getString("userToken", null);
    }
    public static String getIpAddress(Context context) {
        return getPreferences(context).getString("userIpAddress", null);
    }
    public static String getLocation(Context context) {
        return getPreferences(context).getString("userLocation", null);
    }
    public static String getAccountAddress(Context context) {
        return getPreferences(context).getString("userAccountAddress", null);
    }
    public static String getAuthorization(Context context) {
        return getPreferences(context).getString("userAuthorization", null);
    }
    public static String getIntent(Context context) {
        return getPreferences(context).getString("userIntent", null);
    }
    public static String getBalance(Context context) {
        return getPreferences(context).getString("userBalance", null);
    }
    public static String getActorCategory(Context context) {
        return getPreferences(context).getString("userActorCategory", null);
    }
}


/*
    private Session session;//global variable
    session = new Session(cntx); //in oncreate

    session.setusename("USERNAME");
    session.getusename();
*/