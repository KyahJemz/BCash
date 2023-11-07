package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TransferConfirmationActivity extends AppCompatActivity {
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    TextView tv_TransferConfirmationSourceName, tv_TransferConfirmationSourceId;
    TextView tv_TransferConfirmationDestinationName, tv_TransferConfirmationDestinationId;
    TextView tv_TransferConfirmationAmount;
    TextView tv_TransferConfirmationMessage;
    TextView tv_ConfirmTransferBtn;
    ConstraintLayout cl_Loading;
    String ReceiverId, Amount, Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_confirmation);

        Intent intentExtras = getIntent();
        if (intentExtras != null) {
            ReceiverId = intentExtras.getStringExtra("ReceiverId");
            Amount = intentExtras.getStringExtra("Amount");
            Message = intentExtras.getStringExtra("Message");
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

        cl_Loading = findViewById(R.id.cl_Loading);
        cl_Loading.setVisibility(View.VISIBLE);

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
            customHeaders.addHeader("Content-Type", "application/json");
            customHeaders.addHeader("Authorization", Session.getAuthorization(TransferConfirmationActivity.this));
            customHeaders.addHeader("AccountAddress", Session.getAccountAddress(TransferConfirmationActivity.this));
            customHeaders.addHeader("ClientVersion", "1.0");
            customHeaders.addHeader("IpAddress", Session.getIpAddress(TransferConfirmationActivity.this));
            customHeaders.addHeader("Device", Helpers.getDevice());
            customHeaders.addHeader("Location", Session.getLocation(TransferConfirmationActivity.this));
            customHeaders.addHeader("Intent", "get other account");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Id", ReceiverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback TransferValidationCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cl_Loading.setVisibility(View.GONE);
                    }
                });
                Log.e("OkHttpRequest", "Failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cl_Loading.setVisibility(View.GONE);
                    }
                });
                try {
                    if (!response.isSuccessful()) {
                        Log.e("OkHttpRequest", "Unsuccessful response: " + response.code());
                    } else {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String successResult = jsonResponse.optString("Success");
                        String targetResult = jsonResponse.optString("Target");
                        JSONObject parametersResult = jsonResponse.optJSONObject("Parameters");
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(TransferConfirmationActivity.this,targetResult);
                        Helpers.responseMessageController(TransferConfirmationActivity.this,responseResult);

                        if (!(parametersResult == null)){
                            JSONObject Account = parametersResult.optJSONObject("Account");
                            JSONObject Details = parametersResult.optJSONObject("Details");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_TransferConfirmationSourceName = findViewById(R.id.tv_TransferConfirmationSourceName);
                                    String sourceFullName = Helpers.toTitleCase(Session.getFirstName(TransferConfirmationActivity.this)) + " " + Helpers.toTitleCase(Session.getLastName(TransferConfirmationActivity.this));
                                    tv_TransferConfirmationSourceName.setText(sourceFullName);

                                    tv_TransferConfirmationSourceId = findViewById(R.id.tv_TransferConfirmationSourceId);
                                    tv_TransferConfirmationSourceId.setText(Session.getPersonalId(TransferConfirmationActivity.this));

                                    tv_TransferConfirmationDestinationName = findViewById(R.id.tv_TransferConfirmationDestinationName);
                                    String destinationFullName = Helpers.toTitleCase(Account.optString("Firstname")) + " " + Helpers.toTitleCase(Account.optString("Lastname"));
                                    tv_TransferConfirmationDestinationName.setText(destinationFullName);

                                    tv_TransferConfirmationDestinationId = findViewById(R.id.tv_TransferConfirmationDestinationId);
                                    tv_TransferConfirmationDestinationId.setText(Details.optString("SchoolPersonalId"));

                                    tv_TransferConfirmationAmount = findViewById(R.id.tv_TransferConfirmationAmount);
                                    tv_TransferConfirmationAmount.setText(Helpers.getBalance(Amount));

                                    tv_TransferConfirmationMessage = findViewById(R.id.tv_TransferConfirmationMessage);
                                    tv_TransferConfirmationMessage.setText(Message);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onBackPressed();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };

        requestHelper.makeRequest(TransferConfirmationActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, TransferValidationCallback);

        tv_ConfirmTransferBtn = findViewById(R.id.tv_ConfirmTransferBtn);
        tv_ConfirmTransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processTransfer();
            }
        });
    }

    private void processTransfer(){

        cl_Loading.setVisibility(View.VISIBLE);

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
            customHeaders.addHeader("Content-Type", "application/json");
            customHeaders.addHeader("Authorization", Session.getAuthorization(TransferConfirmationActivity.this));
            customHeaders.addHeader("AccountAddress", Session.getAccountAddress(TransferConfirmationActivity.this));
            customHeaders.addHeader("ClientVersion", "1.0");
            customHeaders.addHeader("IpAddress", Session.getIpAddress(TransferConfirmationActivity.this));
            customHeaders.addHeader("Device", Helpers.getDevice());
            customHeaders.addHeader("Location", Session.getLocation(TransferConfirmationActivity.this));
            customHeaders.addHeader("Intent", "initiate transfer");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("SchoolPersonalId", ReceiverId);
            jsonParams.put("Amount", Amount);
            jsonParams.put("Message", Optional.ofNullable(Message).orElse(""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback TransferProcessCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cl_Loading.setVisibility(View.GONE);
                    }
                });
                Log.e("OkHttpRequest", "Failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cl_Loading.setVisibility(View.GONE);
                    }
                });
                try {
                    if (!response.isSuccessful()) {
                        Log.e("OkHttpRequest", "Unsuccessful response: " + response.code());
                    } else {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String successResult = jsonResponse.optString("Success");
                        String targetResult = jsonResponse.optString("Target");
                        String parametersResult = jsonResponse.optString("Parameters");
                        String responseResult = jsonResponse.optString("Response");
                        JSONObject parameters = jsonResponse.optJSONObject("Parameters");

                        Helpers.responseIntentController(TransferConfirmationActivity.this,targetResult);
                        Helpers.responseMessageController(TransferConfirmationActivity.this,responseResult);
                        Log.d("PARAMETERS1" , parametersResult);
                        if (!parametersResult.equals("null")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(TransferConfirmationActivity.this, ReceiptActivity.class);
                                    intent.putExtra("TransactionAddress",parametersResult);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };

        requestHelper.makeRequest(TransferConfirmationActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, TransferProcessCallback);
    }


}