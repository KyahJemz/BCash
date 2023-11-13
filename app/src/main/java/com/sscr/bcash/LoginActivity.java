package com.sscr.bcash;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private LinearLayout btn_GoogleSignIn;
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String webClientId = getString(R.string.web_client_id);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        btn_GoogleSignIn = findViewById(R.id.btn_GoogleSignIn);
        btn_GoogleSignIn.setOnClickListener(view -> signIn());

        Helpers.getIpAddress(this);

        gsc.signOut()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                    } else {

                    }
                });
        gsc.revokeAccess()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    } else {

                    }
                });
    }

    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    private void navigateToSecondActivity(String idToken, String userEmail, String userGivenName, String userFamilyName, String userPhotoUrl) {

        Session.setAccountAddress(LoginActivity.this, "");
        Session.setAuthorization(LoginActivity.this, "");

        Session.setFirstName(LoginActivity.this,userGivenName);
        Session.setLastName(LoginActivity.this,userFamilyName);
        Session.setProfileImage(LoginActivity.this,userPhotoUrl);
        Session.setEmail(LoginActivity.this,userEmail);
        Session.setToken(LoginActivity.this,idToken);

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Optional.ofNullable(Session.getAuthorization(LoginActivity.this)).orElse(""));
        customHeaders.addHeader("AccountAddress", Optional.ofNullable(Session.getAccountAddress(LoginActivity.this)).orElse(""));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("Intent", "MobileLogin");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("GoogleToken", Session.getToken(LoginActivity.this));
            jsonParams.put("IpAddress", Session.getIpAddress(LoginActivity.this));
            jsonParams.put("Device", Helpers.getDevice());
            jsonParams.put("Location", Session.getLocation(LoginActivity.this));
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Network Failure!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        Log.e("OkHttpRequest", "Unsuccessful response: " + response.code());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Network Failure: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String successResult = jsonResponse.optString("Success");
                        String targetResult = jsonResponse.optString("Target");
                        JSONObject parametersResult = jsonResponse.optJSONObject("Parameters");
                        String responseResult = jsonResponse.optString("Response");

                        if (parametersResult != null) {
                            Session.setAccountAddress(LoginActivity.this, Optional.ofNullable(parametersResult.optString("AccountAddress")).orElse(""));
                            Session.setAuthorization(LoginActivity.this, Optional.ofNullable(parametersResult.optString("AuthorizationToken")).orElse(""));
                        }
                        Session.setActorCategory(LoginActivity.this,targetResult);
                        Helpers.responseIntentController(LoginActivity.this,targetResult);
                        Helpers.responseMessageController(LoginActivity.this,responseResult);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };

        requestHelper.makeRequest(LoginActivity.this,Defaults.AuthEndPoint, customHeaders, requestBody, SignInCallback);
    }

    private void handleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        task.addOnSuccessListener(this, googleSignInAccount -> {
            if (googleSignInAccount != null) {
                String idToken = googleSignInAccount.getIdToken();
                if (idToken != null) {
                    navigateToSecondActivity(idToken, googleSignInAccount.getEmail(), googleSignInAccount.getGivenName(), googleSignInAccount.getFamilyName(), String.valueOf(googleSignInAccount.getPhotoUrl()));
                } else {
                    Toast.makeText(this, "ID Token is null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Sign In Failed: Invalid Account", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(this, e -> {
            Toast.makeText(this, "Sign In Process Failed", Toast.LENGTH_SHORT).show();
        });
    }

    private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    handleSignInResult(data);
                } else {
                    Toast.makeText(this, "Sign-in process failed. Result code: " + result.getResultCode(), Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    public void onBackPressed() {
        // Disable back button functionality in this Activity
    }
}
