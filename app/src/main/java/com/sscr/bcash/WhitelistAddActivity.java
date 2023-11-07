package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WhitelistAddActivity extends AppCompatActivity {
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    EditText et_IdNumber, et_Name, et_PinCode;
    LinearLayout ll_NameContainer, ll_ButtonConfirmation;
    TextView tv_SubmitBtn;
    private ConstraintLayout cl_Loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist_add);
        cl_Loading = findViewById(R.id.cl_Loading);

        // Back Button
        findViewById(R.id.iv_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        et_IdNumber = findViewById(R.id.et_IdNumber);
        et_IdNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String enteredText = textView.getText().toString();
                    SearchAccount(enteredText);
                    return true;
                }
                return false;
            }
        });

        et_Name = findViewById(R.id.et_Name);
        et_PinCode = findViewById(R.id.et_PinCode);

        ll_NameContainer = findViewById(R.id.ll_NameContainer);
        ll_NameContainer.setVisibility(View.GONE);

        ll_ButtonConfirmation = findViewById(R.id.ll_ButtonConfirmation);
        ll_ButtonConfirmation.setVisibility(View.GONE);

        tv_SubmitBtn = findViewById(R.id.tv_SubmitBtn);
        tv_SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAccount();
            }
        });
    }

    private void SearchAccount(String enteredText){
        ll_ButtonConfirmation.setVisibility(View.GONE);
        ll_NameContainer.setVisibility(View.GONE);
        et_Name.setText("");
        et_PinCode.setText("");

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(WhitelistAddActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(WhitelistAddActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(WhitelistAddActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(WhitelistAddActivity.this));
        customHeaders.addHeader("Intent", "get other account");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Id", enteredText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback WhitelistAddCallback = new Callback() {
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
                        JSONObject parametersObject= jsonResponse.optJSONObject("Parameters");
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(WhitelistAddActivity.this,targetResult);
                        Helpers.responseMessageController(WhitelistAddActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (parametersObject != null && parametersObject.optJSONObject("Account") != null) {
                                    JSONObject AccountObject= parametersObject.optJSONObject("Account");
                                    String firstName = AccountObject.optString("Firstname");
                                    String lastName = AccountObject.optString("Lastname");
                                    Toast.makeText(WhitelistAddActivity.this, "Account Found!.", Toast.LENGTH_SHORT).show();
                                    ll_ButtonConfirmation.setVisibility(View.VISIBLE);
                                    ll_NameContainer.setVisibility(View.VISIBLE);
                                    String fullName = Helpers.toTitleCase(firstName) + " " + Helpers.toTitleCase(lastName);
                                    et_Name.setText(fullName);

                                } else {
                                    Toast.makeText(WhitelistAddActivity.this, "Account Not Found!.", Toast.LENGTH_SHORT).show();
                                    ll_ButtonConfirmation.setVisibility(View.GONE);
                                    ll_NameContainer.setVisibility(View.GONE);
                                    et_Name.setText("");
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

        requestHelper.makeRequest(WhitelistAddActivity.this, Defaults.RequestEndPoint, customHeaders, requestBody, WhitelistAddCallback);
    }

    private void AddAccount(){

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(WhitelistAddActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(WhitelistAddActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(WhitelistAddActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(WhitelistAddActivity.this));
        customHeaders.addHeader("Intent", "add one to whitelist");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Id", et_IdNumber.getText().toString());
            jsonParams.put("PinCode", et_PinCode.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        Callback WhitelistAddCallback = new Callback() {
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
                        JSONObject parametersObject= jsonResponse.optJSONObject("Parameters");
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(WhitelistAddActivity.this,targetResult);
                        Helpers.responseMessageController(WhitelistAddActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (successResult.equals("true")){
                                    Intent intent = new Intent(WhitelistAddActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
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

        requestHelper.makeRequest(WhitelistAddActivity.this, Defaults.RequestEndPoint, customHeaders, requestBody, WhitelistAddCallback);
    }
}