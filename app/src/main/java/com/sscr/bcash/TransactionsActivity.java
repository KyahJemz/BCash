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

public class TransactionsActivity extends AppCompatActivity {
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    private RecyclerViewTransactionStaticAdapter adapter;
    private ArrayList<TransactionModel> transactionModelArrayList;
    private RecyclerView rv_TransactionHistory;

    // Main Navigation
    LinearLayout ll_Nav1, ll_Nav2, ll_Nav3, ll_Nav4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        transactionModelArrayList = new ArrayList<>();
        rv_TransactionHistory = findViewById(R.id.rv_TransactionHistory);

        ll_Nav1 = findViewById(R.id.ll_Nav1);
        ll_Nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionsActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav2 = findViewById(R.id.ll_Nav2);
        ll_Nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionsActivity.this, TransactionsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav3 = findViewById(R.id.ll_Nav3);
        ll_Nav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionsActivity.this, NotificationsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav4 = findViewById(R.id.ll_Nav4);
        ll_Nav4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionsActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });





        PopulateRecentTransactions();
    }

    private void PopulateRecentTransactions(){

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(TransactionsActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(TransactionsActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(TransactionsActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(TransactionsActivity.this));
        customHeaders.addHeader("Intent", "get all transactions");

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

                        Helpers.responseIntentController(TransactionsActivity.this,targetResult);
                        Helpers.responseMessageController(TransactionsActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (parametersArray != null) {
                                    for (int i = 0; i < parametersArray.length(); i++) {
                                        JSONObject parameterObject = parametersArray.optJSONObject(i);
                                        if (parameterObject != null) {
                                            String TransactionAddress = parameterObject.optString("Transaction_Address");
                                            String TransactionAmount = Session.getAccountAddress(TransactionsActivity.this).equals(parameterObject.optString("Sender_Address")) ? "- "+Helpers.getBalance(parameterObject.optString("TotalAmount")) : "+ "+Helpers.getBalance(parameterObject.optString("TotalAmount"));
                                            String TransactionDate = Helpers.convertTimestamp(parameterObject.optString("Timestamp"));
                                            String TransactionType = parameterObject.optString("TransactionType");

                                            transactionModelArrayList.add(new TransactionModel(TransactionAddress, TransactionAmount, TransactionDate, TransactionType));
                                            //String transactionAddress, String transactionAmount, String transactionDate, String transactionType) {

                                        }
                                    }
                                    rv_TransactionHistory.setLayoutManager(new LinearLayoutManager(TransactionsActivity.this));
                                    adapter = new RecyclerViewTransactionStaticAdapter(transactionModelArrayList, TransactionsActivity.this);
                                    rv_TransactionHistory.setAdapter(adapter);
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

        requestHelper.makeRequest(TransactionsActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SignInCallback);
    }
}