package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class PurchaseConfirmationActivity extends AppCompatActivity {
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    private RecyclerViewItemsStaticAdapter adapter;
    private ArrayList<ModelItems> itemsArrayList;
    RecyclerView rv_PurchaseConfirmation;
    TextView tv_ConfirmPurchaseBtn, tv_CancelPurchaseBtn;
    TextView tv_PurchaseConfirmationAmount, tv_PurchaseConfirmationDiscount, tv_PurchaseConfirmationTotalAmount, tv_PurchaseConfirmationMerchant;
    ConstraintLayout cl_Loading;
    String TransactionAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_confirmation);
        cl_Loading = findViewById(R.id.cl_Loading);
        cl_Loading.setVisibility(View.VISIBLE);

        Intent intentExtras = getIntent();
        if (intentExtras != null) {
            TransactionAddress = intentExtras.getStringExtra("TransactionAddress");
        } else {
            Toast.makeText(this, "Invalid Request", Toast.LENGTH_SHORT).show();
            onBackPressed();
            finish();
        }

        // Back Button
        findViewById(R.id.iv_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_ConfirmPurchaseBtn = findViewById(R.id.tv_ConfirmPurchaseBtn);
        tv_CancelPurchaseBtn = findViewById(R.id.tv_CancelPurchaseBtn);

        tv_PurchaseConfirmationAmount = findViewById(R.id.tv_PurchaseConfirmationAmount);
        tv_PurchaseConfirmationDiscount = findViewById(R.id.tv_PurchaseConfirmationDiscount);
        tv_PurchaseConfirmationTotalAmount = findViewById(R.id.tv_PurchaseConfirmationTotalAmount);
        tv_PurchaseConfirmationMerchant = findViewById(R.id.tv_PurchaseConfirmationMerchant);

        rv_PurchaseConfirmation = findViewById(R.id.rv_PurchaseConfirmation);
        itemsArrayList = new ArrayList<>();

        tv_ConfirmPurchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetApproved();
            }
        });
        tv_CancelPurchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetCancel();
            }
        });

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("Intent", "get receipt");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("TransactionAddress", TransactionAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback PurchaseConfirmationCallback = new Callback() {
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
                        JSONObject parametersResult = jsonResponse.optJSONObject("Parameters");
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(PurchaseConfirmationActivity.this,targetResult);
                        Helpers.responseMessageController(PurchaseConfirmationActivity.this,responseResult);

                        JSONObject Info = parametersResult.optJSONObject("Info");
                        JSONArray Items = parametersResult.optJSONArray("Items");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cl_Loading.setVisibility(View.GONE);

                                tv_PurchaseConfirmationMerchant.setText(Helpers.toTitleCase(String.format("%s %s", Info.optString("Receiver_Firstname"), Info.optString("Receiver_Lastname"))));

                                tv_PurchaseConfirmationAmount.setText(Helpers.getBalance(Info.optString("Amount")));
                                tv_PurchaseConfirmationDiscount.setText(Helpers.getBalance(Info.optString("Discount")));

                                tv_PurchaseConfirmationTotalAmount.setText(Helpers.getBalance(Info.optString("TotalAmount")));

                                for (int i = 0; i < Items.length(); i++) {
                                    JSONObject parameterObject = Items.optJSONObject(i);

                                    String ItemName = parameterObject.optString("ItemName");
                                    String ItemPrice = parameterObject.optString("ItemAmount");
                                    String ItemQuantity = parameterObject.optString("ItemQuantity");

                                    itemsArrayList.add(new ModelItems(ItemName, ItemQuantity, ItemPrice));

                                }
                                rv_PurchaseConfirmation.setLayoutManager(new LinearLayoutManager(PurchaseConfirmationActivity.this));
                                adapter = new RecyclerViewItemsStaticAdapter(itemsArrayList, PurchaseConfirmationActivity.this);
                                rv_PurchaseConfirmation.setAdapter(adapter);

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };

        requestHelper.makeRequest(PurchaseConfirmationActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, PurchaseConfirmationCallback);
    }

    private void SetCancel(){
        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("Intent", "set purchase cancel");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("TransactionAddress", TransactionAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback SetApprovedCallback = new Callback() {
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
                        JSONObject parametersResult = jsonResponse.optJSONObject("Parameters");
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(PurchaseConfirmationActivity.this,targetResult);
                        Helpers.responseMessageController(PurchaseConfirmationActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (successResult.equals("true")){
                                    Toast.makeText(PurchaseConfirmationActivity.this, "Transaction Canceled", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PurchaseConfirmationActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(PurchaseConfirmationActivity.this, "Failed, Try Again", Toast.LENGTH_SHORT).show();
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

        requestHelper.makeRequest(PurchaseConfirmationActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SetApprovedCallback);
    }

    private void SetApproved(){
        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(PurchaseConfirmationActivity.this));
        customHeaders.addHeader("Intent", "set purchase approved");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("TransactionAddress", TransactionAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback SetApprovedCallback = new Callback() {
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
                        JSONObject parametersResult = jsonResponse.optJSONObject("Parameters");
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(PurchaseConfirmationActivity.this,targetResult);
                        Helpers.responseMessageController(PurchaseConfirmationActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (successResult.equals("true")){
                                    Toast.makeText(PurchaseConfirmationActivity.this, "Transaction Completed", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PurchaseConfirmationActivity.this, ReceiptActivity.class);
                                    intent.putExtra("TransactionAddress", TransactionAddress);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(PurchaseConfirmationActivity.this, "Failed, Try Again", Toast.LENGTH_SHORT).show();
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

        requestHelper.makeRequest(PurchaseConfirmationActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SetApprovedCallback);
    }
}