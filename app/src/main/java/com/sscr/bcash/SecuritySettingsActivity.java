package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SecuritySettingsActivity extends AppCompatActivity {
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    EditText et_ReNewPinCode, et_NewPinCode, et_OldPinCode;
    TextView tv_ChangePinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_settings);

        // Back Button
        findViewById(R.id.iv_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        et_ReNewPinCode = findViewById(R.id.et_ReNewPinCode);
        et_NewPinCode = findViewById(R.id.et_NewPinCode);
        et_OldPinCode = findViewById(R.id.et_OldPinCode);

        tv_ChangePinBtn = findViewById(R.id.tv_ChangePinBtn);
        tv_ChangePinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(et_NewPinCode.getText().toString().equals(et_ReNewPinCode.getText().toString()))){
                    Toast.makeText(SecuritySettingsActivity.this, "New PIN code does not match. Try again!", Toast.LENGTH_SHORT).show();
                } else if (et_NewPinCode.getText().toString().equals(et_OldPinCode.getText().toString())){
                    Toast.makeText(SecuritySettingsActivity.this, "Old PIN and New PIN should not be the same. Try again!", Toast.LENGTH_SHORT).show();
                }else {
                    processChangePIN();
                }
            }
        });
    }

    private void processChangePIN(){

        // HEADER
        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");
        customHeaders.addHeader("Authorization", Session.getAuthorization(SecuritySettingsActivity.this));
        customHeaders.addHeader("AccountAddress", Session.getAccountAddress(SecuritySettingsActivity.this));
        customHeaders.addHeader("ClientVersion", "1.0");
        customHeaders.addHeader("IpAddress", Session.getIpAddress(SecuritySettingsActivity.this));
        customHeaders.addHeader("Device", Helpers.getDevice());
        customHeaders.addHeader("Location", Session.getLocation(SecuritySettingsActivity.this));
        customHeaders.addHeader("Intent", "update my pin");

        // BODY
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("CurrentPIN", et_OldPinCode.getText().toString());
            jsonParams.put("NewPINCode1", et_NewPinCode.getText().toString());
            jsonParams.put("NewPINCode2", et_ReNewPinCode.getText().toString());
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

                        Helpers.responseIntentController(SecuritySettingsActivity.this, targetResult);
                        Helpers.responseMessageController(SecuritySettingsActivity.this, responseResult);

                        if (successResult.equals("true")) {
                            Intent intent = new Intent(SecuritySettingsActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OkHttpRequest", "JSON Parsing Error");
                }
            }
        };

        requestHelper.makeRequest(SecuritySettingsActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, SignInCallback);
    }
}