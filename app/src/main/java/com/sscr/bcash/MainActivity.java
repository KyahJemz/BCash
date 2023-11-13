package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogoutCurrentAccount();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, Defaults.SplashScreenTime);
    }

    public void LogoutCurrentAccount(){
        if (Session.getAccountAddress(MainActivity.this).equals("") || Session.getAccountAddress(MainActivity.this) == null){
            return;
        }
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Optional.ofNullable(Session.getAuthorization(MainActivity.this)).orElse(""));
        customHeaders.addHeader("AccountAddress", Optional.ofNullable(Session.getAccountAddress(MainActivity.this)).orElse(""));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("Intent", "Logout");

        // BODY
        JSONObject jsonParams = new JSONObject();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback LogoutCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("OkHttpRequest", "Failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        Log.e("OkHttpRequest", "Unsuccessful response: " + response.code());
                    } else {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String successResult = jsonResponse.optString("Success");

                       // if (successResult.equals("false")){
                            //LogoutCurrentAccount();
                       //} else {
                       //     Session.setAccountAddress(MainActivity.this, "");
                       //     Session.setAuthorization(MainActivity.this, "");
                       // }

                        Session.setAccountAddress(MainActivity.this, "");
                        Session.setAuthorization(MainActivity.this, "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };

        requestHelper.makeRequest(MainActivity.this,Defaults.AuthEndPoint, customHeaders, requestBody, LogoutCallback);
    }
}