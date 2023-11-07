package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

public class LoginHistoryActivity extends AppCompatActivity implements InterfaceLoginHistory{
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    private RecyclerViewLoginHistoryAdapter adapter;
    private ArrayList<ModelLoginHistory> loginHistoryArrayList;
    private RecyclerView rv_LoginHistory;
    private TextView tv_RemoveAllLoginHistory;
    private ConstraintLayout cl_Loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_history);
        cl_Loading = findViewById(R.id.cl_Loading);
        cl_Loading.setVisibility(View.VISIBLE);
        loginHistoryArrayList = new ArrayList<>();
        rv_LoginHistory = findViewById(R.id.rv_LoginHistory);
        rv_LoginHistory.setLayoutManager(new LinearLayoutManager(LoginHistoryActivity.this));
        adapter = new RecyclerViewLoginHistoryAdapter(loginHistoryArrayList, LoginHistoryActivity.this, LoginHistoryActivity.this);
        rv_LoginHistory.setAdapter(adapter);

        // Back Button
        findViewById(R.id.iv_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_RemoveAllLoginHistory = findViewById(R.id.tv_RemoveAllLoginHistory);
        tv_RemoveAllLoginHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveAllLoginHistory();
            }
        });

        PopulateLoginHistory();
    }

    private void RemoveAllLoginHistory() {
        cl_Loading.setVisibility(View.VISIBLE);
        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(LoginHistoryActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(LoginHistoryActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(LoginHistoryActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(LoginHistoryActivity.this));
        customHeaders.addHeader("Intent", "delete all login history");

        // BODY
        JSONObject jsonParams = new JSONObject();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback LoginHistoryClearCallback = new Callback() {
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

                        Helpers.responseIntentController(LoginHistoryActivity.this,targetResult);
                        Helpers.responseMessageController(LoginHistoryActivity.this,responseResult);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
                cl_Loading.setVisibility(View.GONE);
            }
        };

        requestHelper.makeRequest(LoginHistoryActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, LoginHistoryClearCallback);

        loginHistoryArrayList.clear();

        adapter.notifyDataSetChanged();

        PopulateLoginHistory();
    }

    private void PopulateLoginHistory() {
        cl_Loading.setVisibility(View.VISIBLE);
        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(LoginHistoryActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(LoginHistoryActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(LoginHistoryActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(LoginHistoryActivity.this));
        customHeaders.addHeader("Intent", "get my login history");

        // BODY
        JSONObject jsonParams = new JSONObject();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback LoginHistoryback = new Callback() {
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

                        Helpers.responseIntentController(LoginHistoryActivity.this,targetResult);
                        Helpers.responseMessageController(LoginHistoryActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void run() {

                                if (parametersArray != null) {
                                    for (int i = 0; i < parametersArray.length(); i++) {
                                        JSONObject parameterObject = parametersArray.optJSONObject(i);
                                        if (parameterObject != null) {
                                            String accountAddress = parameterObject.optString("Account_Address");
                                            String ipAddress = parameterObject.optString("IpAddress");
                                            String location = parameterObject.optString("Location");
                                            String device = parameterObject.optString("Device");
                                            String lastOnline = Helpers.convertTimestamp(parameterObject.optString("LastOnline"));

                                            loginHistoryArrayList.add(new ModelLoginHistory(accountAddress, ipAddress, location, device, lastOnline));
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

        requestHelper.makeRequest(LoginHistoryActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, LoginHistoryback);
    }

    @Override
    public void onLoginHistoryRemove(ModelLoginHistory modelLoginHistory) {
        cl_Loading.setVisibility(View.VISIBLE);
        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(LoginHistoryActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(LoginHistoryActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(LoginHistoryActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(LoginHistoryActivity.this));
        customHeaders.addHeader("Intent", "delete one login history");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("IpAddress", modelLoginHistory.getIpAddress());
            jsonParams.put("Device", modelLoginHistory.getDevice());
            jsonParams.put("Location", modelLoginHistory.getLocation());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback LoginHistoryRemoveCallback = new Callback() {
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

                        Helpers.responseIntentController(LoginHistoryActivity.this,targetResult);
                        Helpers.responseMessageController(LoginHistoryActivity.this,responseResult);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
                cl_Loading.setVisibility(View.GONE);
            }
        };

        requestHelper.makeRequest(LoginHistoryActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, LoginHistoryRemoveCallback);

        loginHistoryArrayList.remove(modelLoginHistory);

        adapter.notifyDataSetChanged();

        PopulateLoginHistory();
    }
}