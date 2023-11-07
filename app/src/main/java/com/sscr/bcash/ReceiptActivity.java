package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
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

public class ReceiptActivity extends AppCompatActivity {
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    private RecyclerViewItemsStaticAdapter adapter;
    private ArrayList<ModelItems> itemsArrayList;
    RecyclerView rv_Receipt;
    TextView tv_ReceiptDate, tv_TransactionType;
    TextView tv_ReceiptSourceName, tv_ReceiptSourceId;
    TextView tv_ReceiptDestinationName, tv_ReceiptDestinationId;
    TextView tv_ReceiptAmount, tv_ReceiptDiscount;
    TextView tv_ReceiptTotalAmount,tv_ReceiptMessage,tv_ReceiptReference;
    TableRow tr_ReceiptAmountContainer, tr_ReceiptDiscountContainer;
    LinearLayout ll_ReceiptBottom;
    String TransactionAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        rv_Receipt = findViewById(R.id.rv_Receipt);
        itemsArrayList = new ArrayList<>();

        Intent intentExtras = getIntent();
        if (intentExtras != null) {
            TransactionAddress = intentExtras.getStringExtra("TransactionAddress");
        } else {
            Toast.makeText(this, "Transfer verification failed. Try again.", Toast.LENGTH_SHORT).show();
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

        tv_ReceiptDate = findViewById(R.id.tv_ReceiptDate);
        tv_TransactionType = findViewById(R.id.tv_TransactionType);

        tv_ReceiptSourceName = findViewById(R.id.tv_ReceiptSourceName);
        tv_ReceiptSourceId = findViewById(R.id.tv_ReceiptSourceId);

        tv_ReceiptDestinationName = findViewById(R.id.tv_ReceiptDestinationName);
        tv_ReceiptDestinationId = findViewById(R.id.tv_ReceiptDestinationId);

        tv_ReceiptAmount = findViewById(R.id.tv_ReceiptAmount);
        tv_ReceiptDiscount = findViewById(R.id.tv_ReceiptDiscount);

        tv_ReceiptTotalAmount = findViewById(R.id.tv_ReceiptTotalAmount);
        tv_ReceiptMessage = findViewById(R.id.tv_ReceiptMessage);

        tv_ReceiptReference = findViewById(R.id.tv_ReceiptReference);

        tr_ReceiptAmountContainer = findViewById(R.id.tr_ReceiptAmountContainer);
        tr_ReceiptDiscountContainer = findViewById(R.id.tr_ReceiptDiscountContainer);

        ll_ReceiptBottom = findViewById(R.id.ll_ReceiptBottom);


        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(ReceiptActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(ReceiptActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(ReceiptActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(ReceiptActivity.this));
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

        Callback ReceiptCallback = new Callback() {
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

                        Helpers.responseIntentController(ReceiptActivity.this,targetResult);
                        Helpers.responseMessageController(ReceiptActivity.this,responseResult);

                        JSONObject Info = parametersResult.optJSONObject("Info");
                        JSONArray Items = parametersResult.optJSONArray("Items");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                tv_ReceiptDate.setText(Helpers.convertTimestamp(Info.optString("Timestamp")));
                                tv_TransactionType.setText(Info.optString("TransactionType"));

                                tv_ReceiptSourceName.setText(Helpers.toTitleCase(String.format("%s %s", Info.optString("Sender_Firstname"), Info.optString("Sender_Lastname"))));
                                tv_ReceiptSourceId.setText(Info.optString("sender_SchoolPersonalId"));

                                tv_ReceiptDestinationName.setText(Helpers.toTitleCase(String.format("%s %s", Info.optString("Receiver_Firstname"), Info.optString("Receiver_Lastname"))));
                                tv_ReceiptDestinationId.setText(Info.optString("Receiver_SchoolPersonalId"));

                                tv_ReceiptAmount.setText(Helpers.getBalance(Info.optString("Amount")));
                                tv_ReceiptDiscount.setText(Helpers.getBalance(Info.optString("Discount")));

                                tv_ReceiptTotalAmount.setText(Helpers.getBalance(Info.optString("TotalAmount")));
                                tv_ReceiptMessage.setText(Info.optString("Notes"));

                                tv_ReceiptReference.setText(String.format("Ref No. %s", Info.optString("Transaction_Address")));

                                if (!Info.optString("TransactionType").equals("Purchase")){
                                    tr_ReceiptAmountContainer.setVisibility(View.GONE);
                                    tr_ReceiptDiscountContainer.setVisibility(View.GONE);
                                    ll_ReceiptBottom.setVisibility(View.GONE);
                                } else {
                                    for (int i = 0; i < Items.length(); i++) {
                                        JSONObject parameterObject = Items.optJSONObject(i);

                                        String ItemName = parameterObject.optString("ItemName");
                                        String ItemPrice = parameterObject.optString("ItemAmount");
                                        String ItemQuantity = parameterObject.optString("ItemQuantity");

                                        itemsArrayList.add(new ModelItems(ItemName, ItemQuantity, ItemPrice));

                                    }
                                    rv_Receipt.setLayoutManager(new LinearLayoutManager(ReceiptActivity.this));
                                    adapter = new RecyclerViewItemsStaticAdapter(itemsArrayList, ReceiptActivity.this);
                                    rv_Receipt.setAdapter(adapter);
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

        requestHelper.makeRequest(ReceiptActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, ReceiptCallback);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReceiptActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}