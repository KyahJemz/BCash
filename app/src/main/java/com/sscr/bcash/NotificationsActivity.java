package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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

public class NotificationsActivity extends AppCompatActivity {
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    private RecyclerViewNotificationsStaticAdapter adapter;
    private ArrayList<ModelNotifications> notificationsArrayList;
    private RecyclerView rv_Notifications;

    // Main Navigation

    // Main Navigation
    LinearLayout ll_Nav1, ll_Nav2, ll_Nav3, ll_Nav4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        notificationsArrayList = new ArrayList<>();
        rv_Notifications = findViewById(R.id.rv_Notifications);

        ll_Nav1 = findViewById(R.id.ll_Nav1);
        ll_Nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationsActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav2 = findViewById(R.id.ll_Nav2);
        ll_Nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationsActivity.this, TransactionsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav3 = findViewById(R.id.ll_Nav3);
        ll_Nav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationsActivity.this, NotificationsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav4 = findViewById(R.id.ll_Nav4);
        ll_Nav4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationsActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });


        PopulateNotifications();
    }

    private void PopulateNotifications(){

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(NotificationsActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(NotificationsActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(NotificationsActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(NotificationsActivity.this));
        customHeaders.addHeader("Intent", "get my notifications");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Test Key", "Test Value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback SignInCallback = new Callback() {
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

                        Helpers.responseIntentController(NotificationsActivity.this,targetResult);
                        Helpers.responseMessageController(NotificationsActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (parametersArray != null) {
                                    for (int i = 0; i < parametersArray.length(); i++) {
                                        JSONObject parameterObject = parametersArray.optJSONObject(i);
                                        if (parameterObject != null) {
                                            String Title = parameterObject.optString("Title");
                                            String Contents = parameterObject.optString("Content");
                                            String Date = Helpers.convertTimestamp(parameterObject.optString("Timestamp"));

                                            notificationsArrayList.add(new ModelNotifications(Date, Title, Contents));

                                        }
                                    }
                                    rv_Notifications.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
                                    adapter = new RecyclerViewNotificationsStaticAdapter(notificationsArrayList, NotificationsActivity.this);
                                    rv_Notifications.setAdapter(adapter);
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

        requestHelper.makeRequest(NotificationsActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SignInCallback);
    }
}