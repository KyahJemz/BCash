package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    // Main Navigation
    LinearLayout ll_Nav1, ll_Nav2, ll_Nav3, ll_Nav4;
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    OkHttpRequestPoolingHelper requestPoolingHelper = new OkHttpRequestPoolingHelper();
    private RecyclerViewTransactionStaticAdapter adapter;
    private ArrayList<TransactionModel> transactionModelArrayList;
    private RecyclerView rv_TransactionsList;
    private TextView tv_TransactionsListViewMore;
    private LinearLayout ll_HomeCardNav1, ll_HomeCardNav2, ll_HomeCardNav3, ll_HomeCardNav4, ll_HomeCardNav5;
    private ImageView iv_HomeProfileCardIdVisible, iv_HomeProfileCardBalanceVisible;
    private Boolean CardIdVisible = true, CardBalanceVisible = true;
    private ImageView iv_HomeProfileImage;
    private TextView tv_HomeProfileCardBalance;
    private TextView tv_HomeProfileCardId;
    private TextView tv_HomeProfileCardName;
    private TextView tv_HomeProfileName;
    private TextView tv_HomeGreetings;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        transactionModelArrayList = new ArrayList<>();
        rv_TransactionsList = findViewById(R.id.rv_TransactionsList);


        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(HomeActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(HomeActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(HomeActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(HomeActivity.this));
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

                        Helpers.responseIntentController(HomeActivity.this,targetResult);
                        Helpers.responseMessageController(HomeActivity.this,responseResult);

                        JSONObject Accounts = parametersResult.optJSONObject("Account");
                        JSONObject Data = parametersResult.optJSONObject("Details");
                        JSONObject Guardian;

                        String SchoolPersonalId = Data.optString("SchoolPersonalId");
                        String Balance = Data.optString("Balance");

                        String Firstname = Accounts.optString("Firstname");
                        String Lastname = Accounts.optString("Lastname");

                        if (Objects.equals(Session.getActorCategory(HomeActivity.this), "7")) {
                            Guardian = parametersResult.optJSONObject("Guardian");
                            Session.setFirstName(HomeActivity.this, Guardian.optString("Firstname"));
                            Session.setLastName(HomeActivity.this, Guardian.optString("Lastname"));
                        }

                        Session.setBalance(HomeActivity.this,Balance);
                        Session.setPersonalId(HomeActivity.this,SchoolPersonalId);

                        String finalFirstname = Firstname;
                        String finalLastname = Lastname;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_HomeProfileCardId = findViewById(R.id.tv_HomeProfileCardId);
                                if (CardIdVisible) {
                                    CardIdVisible = false;
                                    tv_HomeProfileCardId.setText(Helpers.getHiddenPersonalId(Optional.ofNullable(Session.getPersonalId(HomeActivity.this)).orElse("")));
                                    iv_HomeProfileCardIdVisible.setImageResource(R.drawable.icon_visibility_off);
                                } else {
                                    CardIdVisible = true;
                                    tv_HomeProfileCardId.setText(Optional.ofNullable(Session.getPersonalId(HomeActivity.this)).orElse(""));
                                    iv_HomeProfileCardIdVisible.setImageResource(R.drawable.icon_visibility);
                                }

                                tv_HomeProfileCardBalance = findViewById(R.id.tv_HomeProfileCardBalance);
                                if (CardBalanceVisible) {
                                    CardBalanceVisible = false;
                                    tv_HomeProfileCardBalance.setText(Helpers.getHiddenBalance(Optional.ofNullable(Session.getBalance(HomeActivity.this)).orElse("0.00")));
                                    iv_HomeProfileCardBalanceVisible.setImageResource(R.drawable.icon_visibility_off);
                                } else {
                                    CardBalanceVisible = true;
                                    tv_HomeProfileCardBalance.setText(Helpers.getBalance(Optional.ofNullable(Session.getBalance(HomeActivity.this)).orElse("0.00")));
                                    iv_HomeProfileCardBalanceVisible.setImageResource(R.drawable.icon_visibility);
                                }
                                if (Session.getActorCategory(HomeActivity.this).equals("5")) {
                                    ll_HomeCardNav2.setVisibility(View.VISIBLE);
                                    ll_HomeCardNav3.setVisibility(View.VISIBLE);
                                    ll_HomeCardNav4.setVisibility(View.VISIBLE);
                                } else if (Session.getActorCategory(HomeActivity.this).equals("6")) {
                                    ll_HomeCardNav2.setVisibility(View.VISIBLE);
                                    ll_HomeCardNav3.setVisibility(View.VISIBLE);
                                    ll_HomeCardNav4.setVisibility(View.VISIBLE);
                                } else if (Session.getActorCategory(HomeActivity.this).equals("7")) {
                                    ll_HomeCardNav2.setVisibility(View.GONE);
                                    ll_HomeCardNav3.setVisibility(View.GONE);
                                    ll_HomeCardNav4.setVisibility(View.GONE);
                                } else {

                                }

                                tv_HomeProfileCardName = findViewById(R.id.tv_HomeProfileCardName);
                                tv_HomeProfileCardName.setText(Firstname.toUpperCase() + " " + Lastname.toUpperCase());
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };

        requestHelper.makeRequest(HomeActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SignInCallback);

        tv_HomeProfileName = findViewById(R.id.tv_HomeProfileName);
        tv_HomeProfileName.setText(Helpers.toTitleCase(Session.getFirstName(this)) + " " +Helpers.toTitleCase(Session.getLastName(this)));

        iv_HomeProfileImage = findViewById(R.id.iv_HomeProfileImage);
        Picasso.get().load(Session.getProfileImage(this)).into(iv_HomeProfileImage);

        tv_HomeProfileCardName = findViewById(R.id.tv_HomeProfileCardName);

        tv_HomeGreetings = findViewById(R.id.tv_HomeGreetings);
        tv_HomeGreetings.setText("Good Afternoon");

        tv_HomeProfileCardId = findViewById(R.id.tv_HomeProfileCardId);

        tv_HomeProfileCardBalance = findViewById(R.id.tv_HomeProfileCardBalance);

        iv_HomeProfileCardBalanceVisible = findViewById(R.id.iv_HomeProfileCardBalanceVisible);
        iv_HomeProfileCardBalanceVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CardBalanceVisible) {
                    CardBalanceVisible = false;
                    tv_HomeProfileCardBalance.setText(Helpers.getHiddenBalance(Optional.ofNullable(Session.getBalance(HomeActivity.this)).orElse("0.00")));
                    iv_HomeProfileCardBalanceVisible.setImageResource(R.drawable.icon_visibility_off);
                } else {
                    CardBalanceVisible = true;
                    tv_HomeProfileCardBalance.setText(Helpers.getBalance(Optional.ofNullable(Session.getBalance(HomeActivity.this)).orElse("0.00")));
                    iv_HomeProfileCardBalanceVisible.setImageResource(R.drawable.icon_visibility);
                }
            }
        });

        iv_HomeProfileCardIdVisible = findViewById(R.id.iv_HomeProfileCardIdVisible);
        iv_HomeProfileCardIdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CardIdVisible) {
                    CardIdVisible = false;
                    tv_HomeProfileCardId.setText(Helpers.getHiddenPersonalId(Optional.ofNullable(Session.getPersonalId(HomeActivity.this)).orElse("")));
                    iv_HomeProfileCardIdVisible.setImageResource(R.drawable.icon_visibility_off);
                } else {
                    CardIdVisible = true;
                    tv_HomeProfileCardId.setText(Optional.ofNullable(Session.getPersonalId(HomeActivity.this)).orElse(""));
                    iv_HomeProfileCardIdVisible.setImageResource(R.drawable.icon_visibility);
                }
            }
        });

        tv_TransactionsListViewMore = findViewById(R.id.tv_TransactionsListViewMore);
        tv_TransactionsListViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TransactionsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_HomeCardNav1 = findViewById(R.id.ll_HomeCardNav1);
        ll_HomeCardNav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        ll_HomeCardNav2 = findViewById(R.id.ll_HomeCardNav2);
        ll_HomeCardNav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TransferActivity.class);
                startActivity(intent);
            }
        });

        ll_HomeCardNav3 = findViewById(R.id.ll_HomeCardNav3);
        ll_HomeCardNav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, QRScanActivity.class);
                startActivity(intent);
            }
        });

        ll_HomeCardNav4 = findViewById(R.id.ll_HomeCardNav4);
        ll_HomeCardNav4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, WhitelistActivity.class);
                startActivity(intent);
            }
        });

        ll_HomeCardNav5 = findViewById(R.id.ll_HomeCardNav5);
        ll_HomeCardNav5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SecuritySettingsActivity.class);
                startActivity(intent);
            }
        });

        ll_Nav1 = findViewById(R.id.ll_Nav1);
        ll_Nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav2 = findViewById(R.id.ll_Nav2);
        ll_Nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TransactionsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav3 = findViewById(R.id.ll_Nav3);
        ll_Nav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ll_Nav4 = findViewById(R.id.ll_Nav4);
        ll_Nav4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        PopulateRecentTransactions();

        ScanForPurchases();
    }

    private void PopulateRecentTransactions(){

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(HomeActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(HomeActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(HomeActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(HomeActivity.this));
        customHeaders.addHeader("Intent", "get recent transactions");

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

                        Helpers.responseIntentController(HomeActivity.this,targetResult);
                        Helpers.responseMessageController(HomeActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (parametersArray != null) {
                                    for (int i = 0; i < parametersArray.length(); i++) {
                                        JSONObject parameterObject = parametersArray.optJSONObject(i);
                                        if (parameterObject != null) {
                                            String TransactionAddress = parameterObject.optString("Transaction_Address");
                                            String TransactionAmount = Session.getAccountAddress(HomeActivity.this).equals(parameterObject.optString("Sender_Address")) ? "- "+Helpers.getBalance(parameterObject.optString("TotalAmount")) : "+ "+Helpers.getBalance(parameterObject.optString("TotalAmount"));
                                            String TransactionDate = Helpers.convertTimestamp(parameterObject.optString("Timestamp"));
                                            String TransactionType = parameterObject.optString("TransactionType");

                                            transactionModelArrayList.add(new TransactionModel(TransactionAddress, TransactionAmount, TransactionDate, TransactionType));
                                            //String transactionAddress, String transactionAmount, String transactionDate, String transactionType) {

                                        }
                                    }
                                    rv_TransactionsList.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                                    adapter = new RecyclerViewTransactionStaticAdapter(transactionModelArrayList, HomeActivity.this);
                                    rv_TransactionsList.setAdapter(adapter);
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

        requestHelper.makeRequest(HomeActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SignInCallback);
    }




    private void ScanForPurchases(){
        if (Session.getActorCategory(HomeActivity.this).equals("7")){
            return;
        }
        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(HomeActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(HomeActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(HomeActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(HomeActivity.this));
        customHeaders.addHeader("Intent", "scan for purchases");

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
                try {
                    if (!response.isSuccessful()) {
                        Log.e("OkHttpRequest", "Unsuccessful response: " + response.code());
                    } else {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String successResult = jsonResponse.optString("Success");
                        String targetResult = jsonResponse.optString("Target");
                        String parametersArray = jsonResponse.optString("Parameters");
                        String responseResult = jsonResponse.optString("Response");

                        Helpers.responseIntentController(HomeActivity.this,targetResult);
                        Helpers.responseMessageController(HomeActivity.this,responseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (successResult.equals("true")){
                                    Intent intent = new Intent(HomeActivity.this, PurchaseConfirmationActivity.class);
                                    intent.putExtra("TransactionAddress", parametersArray);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if(!targetResult.equals("Login")){
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ScanForPurchases();
                                            }
                                        }, 3000);
                                    }
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

        requestHelper.makeRequest(HomeActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SignInCallback);

    }
}