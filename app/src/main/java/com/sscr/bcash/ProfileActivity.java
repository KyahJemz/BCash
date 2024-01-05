package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    // Main Navigation
    LinearLayout ll_Nav1, ll_Nav2, ll_Nav3, ll_Nav4;

    LinearLayout ll_SecuritySettings, ll_Whitelist, ll_ActivityLogs, ll_LoginHistory, ll_Logout;

    ImageView iv_ProfileImage;
    TextView tv_ProfileName, tv_ProfileEmail;

    LinearLayout ll_AllowModifySettingsContainer;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch s_AllowTransfers, s_AllowTransactions, s_AllowUseOfCard, s_TransactionAutoConfirm, s_AllowModifySettings;

    ConstraintLayout cl_Loading, cl_PinConfirmation;
    EditText et_PinCode;
    TextView tv_PinCancel, tv_PinConfirm;

    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        cl_Loading = findViewById(R.id.cl_Loading);
        cl_Loading.setVisibility(View.VISIBLE);

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(ProfileActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(ProfileActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(ProfileActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(ProfileActivity.this));
        customHeaders.addHeader("Intent", "get my account");

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
                        JSONObject parametersResult = jsonResponse.optJSONObject("Parameters");
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(ProfileActivity.this,targetResult);
                        Helpers.responseMessageController(ProfileActivity.this,responseResult);

                        JSONObject Data = parametersResult.optJSONObject("Details");

                        String AllowTransfers = Data.optString("CanDoTransfers");
                        String AllowTransactions = Data.optString("CanDoTransactions");
                        String AllowUseOfCard = Data.optString("CanUseCard");
                        String TransactionAutoConfirm = Data.optString("IsTransactionAutoConfirm");
                        String AllowModifySettings = Data.optString("CanModifySettings");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                s_AllowTransfers = findViewById(R.id.s_AllowTransfers);
                                s_AllowTransfers.setChecked(AllowTransfers.equals("1"));

                                s_AllowTransactions = findViewById(R.id.s_AllowTransactions);
                                s_AllowTransactions.setChecked(AllowTransactions.equals("1"));

                                s_AllowUseOfCard = findViewById(R.id.s_AllowUseOfCard);
                                s_AllowUseOfCard.setChecked(AllowUseOfCard.equals("1"));

                                s_TransactionAutoConfirm = findViewById(R.id.s_TransactionAutoConfirm);
                                s_TransactionAutoConfirm.setChecked(TransactionAutoConfirm.equals("1"));

                                s_AllowModifySettings = findViewById(R.id.s_AllowModifySettings);
                                s_AllowModifySettings.setChecked(AllowModifySettings.equals("1"));


                                if (Session.getActorCategory(ProfileActivity.this).equals("7")){

                                } else {
                                    if(AllowModifySettings.equals("0")){
                                        s_TransactionAutoConfirm.setClickable(false);
                                        s_AllowUseOfCard.setClickable(false);
                                        s_AllowTransactions.setClickable(false);
                                        s_AllowTransfers.setClickable(false);
                                    }
                                }

                                cl_Loading = findViewById(R.id.cl_Loading);
                                cl_Loading.setVisibility(View.GONE);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };

        requestHelper.makeRequest(ProfileActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SignInCallback);



        ll_AllowModifySettingsContainer = findViewById(R.id.ll_AllowModifySettingsContainer);

        cl_PinConfirmation = findViewById(R.id.cl_PinConfirmation);

        et_PinCode = findViewById(R.id.et_PinCode);

        tv_PinCancel = findViewById(R.id.tv_PinCancel);
        tv_PinCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_PinConfirm = findViewById(R.id.tv_PinConfirm);
        tv_PinConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChanges();
            }
        });

        s_AllowTransfers = findViewById(R.id.s_AllowTransfers);
        s_AllowTransfers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cl_PinConfirmation.setVisibility(View.VISIBLE);
            }
        });

        s_AllowTransactions = findViewById(R.id.s_AllowTransactions);
        s_AllowTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cl_PinConfirmation.setVisibility(View.VISIBLE);
            }
        });

        s_AllowUseOfCard = findViewById(R.id.s_AllowUseOfCard);
        s_AllowUseOfCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cl_PinConfirmation.setVisibility(View.VISIBLE);
            }
        });

        s_TransactionAutoConfirm = findViewById(R.id.s_TransactionAutoConfirm);
        s_TransactionAutoConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cl_PinConfirmation.setVisibility(View.VISIBLE);
            }
        });

        s_AllowModifySettings = findViewById(R.id.s_AllowModifySettings);
        s_AllowModifySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cl_PinConfirmation.setVisibility(View.VISIBLE);
            }
        });

        iv_ProfileImage = findViewById(R.id.iv_ProfileImage);
        Picasso.get().load(Session.getProfileImage(this)).into(iv_ProfileImage);

        tv_ProfileName = findViewById(R.id.tv_ProfileName);
        tv_ProfileName.setText(Helpers.toTitleCase(Session.getFirstName(this)) + " " +Helpers.toTitleCase(Session.getLastName(this)));

        tv_ProfileEmail = findViewById(R.id.tv_ProfileEmail);
        tv_ProfileEmail.setText(Session.getEmail(this));

        ll_SecuritySettings = findViewById(R.id.ll_SecuritySettings);
        ll_SecuritySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SecuritySettingsActivity.class);
                startActivity(intent);
            }
        });

        ll_Whitelist = findViewById(R.id.ll_Whitelist);
        ll_Whitelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, WhitelistActivity.class);
                startActivity(intent);
            }
        });

        ll_ActivityLogs = findViewById(R.id.ll_ActivityLogs);
        ll_ActivityLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ActivityLogsActivity.class);
                startActivity(intent);
            }
        });

        ll_LoginHistory = findViewById(R.id.ll_LoginHistory);
        ll_LoginHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, LoginHistoryActivity.class);
                startActivity(intent);
            }
        });

        ll_Logout = findViewById(R.id.ll_Logout);
        ll_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProcessLogout();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ll_Nav1 = findViewById(R.id.ll_Nav1);
        ll_Nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav2 = findViewById(R.id.ll_Nav2);
        ll_Nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, TransactionsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav3 = findViewById(R.id.ll_Nav3);
        ll_Nav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, NotificationsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav4 = findViewById(R.id.ll_Nav4);
        ll_Nav4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        if (Objects.equals(Session.getActorCategory(ProfileActivity.this), "5")) {
            ll_AllowModifySettingsContainer.setVisibility(View.GONE);
        } else if (Objects.equals(Session.getActorCategory(ProfileActivity.this), "6")) {
            ll_AllowModifySettingsContainer.setVisibility(View.GONE);
        } else if (Objects.equals(Session.getActorCategory(ProfileActivity.this), "7")) {
            ll_Whitelist.setVisibility(View.GONE);
        } else {

        }
    }

    public void setChanges() {
        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
            customHeaders.addHeader("Content-Type", "application/json");
            customHeaders.addHeader("Authorization", Session.getAuthorization(ProfileActivity.this));
            customHeaders.addHeader("AccountAddress", Session.getAccountAddress(ProfileActivity.this));
            customHeaders.addHeader("ClientVersion", "1.0");
            customHeaders.addHeader("IpAddress", Session.getIpAddress(ProfileActivity.this));
            customHeaders.addHeader("Device", Helpers.getDevice());
            customHeaders.addHeader("Location", Session.getLocation(ProfileActivity.this));
            customHeaders.addHeader("Intent", "update my settings");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("PinCode", et_PinCode.getText());
            jsonParams.put("CanDoTransfers", s_AllowTransfers.isChecked() ? "1" : "0");
            jsonParams.put("CanDoTransactions", s_AllowTransactions.isChecked() ? "1" : "0");
            jsonParams.put("CanUseCard", s_AllowUseOfCard.isChecked() ? "1" : "0");
            jsonParams.put("IsTransactionAutoConfirm", s_TransactionAutoConfirm.isChecked() ? "1" : "0");
            jsonParams.put("CanModifySettings", s_AllowModifySettings.isChecked() ? "1" : "0");
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
                        String parametersArray = jsonResponse.optString("Parameters");
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(ProfileActivity.this,targetResult);
                        Helpers.responseMessageController(ProfileActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };

        requestHelper.makeRequest(ProfileActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SignInCallback);
    }

    private void ProcessLogout(){
        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(ProfileActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(ProfileActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(ProfileActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(ProfileActivity.this));
        customHeaders.addHeader("Intent", "Logout");

        // BODY
        JSONObject jsonParams = new JSONObject();

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
                if (!response.isSuccessful()) {
                    Log.e("OkHttpRequest", "Unsuccessful response: " + response.code());
                } else {

                }
            }
        };

        requestHelper.makeRequest(ProfileActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SignInCallback);
    }
}