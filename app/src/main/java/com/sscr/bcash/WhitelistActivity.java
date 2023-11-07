package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WhitelistActivity extends AppCompatActivity implements InterfaceWhitelist{
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    private RecyclerViewWhitelistAdapter adapter;
    private ArrayList<ModelWhitelist> whitelistArrayList;
    private RecyclerView rv_Whitelist;
    private TextView tv_AddAccountToWhitelist;
    private ConstraintLayout cl_Loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist);
        cl_Loading = findViewById(R.id.cl_Loading);
        cl_Loading.setVisibility(View.VISIBLE);

        whitelistArrayList = new ArrayList<>();
        rv_Whitelist = findViewById(R.id.rv_Whitelist);
        rv_Whitelist.setLayoutManager(new LinearLayoutManager(WhitelistActivity.this));
        adapter = new RecyclerViewWhitelistAdapter(whitelistArrayList, WhitelistActivity.this, WhitelistActivity.this);
        rv_Whitelist.setAdapter(adapter);

        // Back Button
        findViewById(R.id.iv_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_AddAccountToWhitelist = findViewById(R.id.tv_AddAccountToWhitelist);
        tv_AddAccountToWhitelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WhitelistActivity.this, WhitelistAddActivity.class);
                startActivity(intent);
            }
        });

        PopulateWhitelist();
    }

    private void PopulateWhitelist() {
        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(WhitelistActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(WhitelistActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(WhitelistActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(WhitelistActivity.this));
        customHeaders.addHeader("Intent", "get my whitelist");

        // BODY
        JSONObject jsonParams = new JSONObject();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback WhitelistCallback = new Callback() {
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
                        String targetResult = jsonResponse.optString("Target");
                        JSONArray parametersArray = jsonResponse.optJSONArray("Parameters"); // ARRAY
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(WhitelistActivity.this,targetResult);
                        Helpers.responseMessageController(WhitelistActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void run() {

                                if (parametersArray != null) {
                                    for (int i = 0; i < parametersArray.length(); i++) {
                                        JSONObject parameterObject = parametersArray.optJSONObject(i);
                                        if (parameterObject != null) {
                                            String accountAddress = parameterObject.optString("Account_Address");
                                            String whitelistedAddress = parameterObject.optString("Whitelisted_Address");
                                            String whitelistedFirstname = parameterObject.optString("Firstname");
                                            String whitelistedLastname = parameterObject.optString("Lastname");
                                            String fullName = whitelistedFirstname + " " + whitelistedLastname;
                                            String timestamp = Helpers.convertTimestamp(parameterObject.optString("Timestamp"));

                                            whitelistArrayList.add(new ModelWhitelist(accountAddress, whitelistedAddress, fullName, timestamp));
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    cl_Loading.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };
        requestHelper.makeRequest(WhitelistActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, WhitelistCallback);
    }

    @Override
    public void onWhitelistRemove(ModelWhitelist modelWhitelist) {
        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(WhitelistActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(WhitelistActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(WhitelistActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(WhitelistActivity.this));
        customHeaders.addHeader("Intent", "remove one from whitelist");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Id", modelWhitelist.getWhitelistedAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback WhiteListRemoveCallback = new Callback() {
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
                        String targetResult = jsonResponse.optString("Target");
                        JSONArray parametersArray = jsonResponse.optJSONArray("Parameters"); // ARRAY
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(WhitelistActivity.this,targetResult);
                        Helpers.responseMessageController(WhitelistActivity.this,responseResult);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };

        requestHelper.makeRequest(WhitelistActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, WhiteListRemoveCallback);

        whitelistArrayList.clear();

        PopulateWhitelist();
    }
}