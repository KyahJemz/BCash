package com.example.bcash;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    // SET

    public void setFirstName(String firstname) {
        prefs.edit().putString("firstname", firstname).commit();
    }

    public void setLastName(String lastname) {
        prefs.edit().putString("lastname", lastname).commit();
    }

    public void setEmail(String email) {
        prefs.edit().putString("email", email).commit();
    }

    public void setPersonalId(String personalId) {
        prefs.edit().putString("personalid", personalId).commit();
    }

    public void setProfileImage(String profileImage) {
        prefs.edit().putString("profileimage", profileImage).commit();
    }

    public void setToken(String token) {
        prefs.edit().putString("token", token).commit();
    }


    // GET

    public String getFirstName() {
        String firstname = prefs.getString("firstname","");
        return firstname;
    }

    public String getLastName() {
        String lastname = prefs.getString("lastname","");
        return lastname;
    }

    public String getEmail() {
        String email = prefs.getString("email","");
        return email;
    }

    public String getPersonalId() {
        String personalId = prefs.getString("personalid","");
        return personalId;
    }

    public String getProfileImage() {
        String profileImage = prefs.getString("profileimage","");
        return profileImage;
    }

    public String getToken() {
        String token = prefs.getString("token","");
        return token;
    }
}

/*
    private Session session;//global variable
    session = new Session(cntx); //in oncreate

    session.setusename("USERNAME");
    session.getusename();
*/

