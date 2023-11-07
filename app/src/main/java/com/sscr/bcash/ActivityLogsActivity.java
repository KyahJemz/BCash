package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class ActivityLogsActivity extends AppCompatActivity {
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    private RecyclerViewActivityLogsStaticAdapter adapter;
    private ArrayList<ModelActivityLogs> activityLogsArrayList;
    private RecyclerView rv_ActivityLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_logs);
        activityLogsArrayList = new ArrayList<>();
        rv_ActivityLogs = findViewById(R.id.rv_ActivityLogs);

        // Back Button
        findViewById(R.id.iv_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        PopulateActivityLogs();
    }

    private void PopulateActivityLogs() {

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(ActivityLogsActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(ActivityLogsActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(ActivityLogsActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(ActivityLogsActivity.this));
        customHeaders.addHeader("Intent", "get my activity logs");

        // BODY
        JSONObject jsonParams = new JSONObject();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback ActivityLogsCallback = new Callback() {
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

                        Helpers.responseIntentController(ActivityLogsActivity.this,targetResult);
                        Helpers.responseMessageController(ActivityLogsActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (parametersArray != null) {
                                    for (int i = 0; i < parametersArray.length(); i++) {
                                        JSONObject parameterObject = parametersArray.optJSONObject(i);
                                        if (parameterObject != null) {
                                            String accountAddress =
                                                    parameterObject.optString("Account_Address").equals(Session.getAccountAddress(ActivityLogsActivity.this)) ? (
                                                                    "You"
                                                            ) : (
                                                                parameterObject.optString("Account_Address").substring(0, Math.min(parameterObject.optString("Account_Address").length(), 3)).equals("ADM")
                                                            ) ? (
                                                                    "Administrator"
                                                            ) : (
                                                                parameterObject.optString("Account_Address").substring(0, Math.min(parameterObject.optString("Account_Address").length(), 3)).equals("ACT")
                                                            ) ? (
                                                                    "Accounting"
                                                            ) : (
                                                                parameterObject.optString("Account_Address").substring(0, Math.min(parameterObject.optString("Account_Address").length(), 3)).equals("GDN")
                                                            ) ? (
                                                                    "Your Guardian"
                                                            ) : (
                                                                    "Unknown"
                                                            );
                                            String targetAccountAddress = parameterObject.optString("Target_Account_Address");
                                            String action = parameterObject.optString("Action");
                                            String task = parameterObject.optString("Task");
                                            String timestamp = Helpers.convertTimestamp(parameterObject.optString("Timestamp"));

                                            activityLogsArrayList.add(new ModelActivityLogs(accountAddress, targetAccountAddress, action, task, timestamp));

                                        }
                                    }
                                    rv_ActivityLogs.setLayoutManager(new LinearLayoutManager(ActivityLogsActivity.this));
                                    adapter = new RecyclerViewActivityLogsStaticAdapter(activityLogsArrayList, ActivityLogsActivity.this);
                                    rv_ActivityLogs.setAdapter(adapter);
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

        requestHelper.makeRequest(ActivityLogsActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, ActivityLogsCallback);
    }
}