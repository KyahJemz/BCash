package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PINValidationActivity extends AppCompatActivity {

    // Display
    TextView tv_CodeInput1, tv_CodeInput2, tv_CodeInput3, tv_CodeInput4, tv_CodeInput5, tv_CodeInput6;

    // Keypad
    TextView tv_Keypad1, tv_Keypad2, tv_Keypad3, tv_Keypad4, tv_Keypad5, tv_Keypad6, tv_Keypad7, tv_Keypad8, tv_Keypad9, tv_Keypad11;

    // Clear
    TextView tv_Keypad10;

    // Backspace
    TextView tv_Keypad12;

    Boolean canType = true;
    ConstraintLayout cl_Loading;

    Stack<String> PINCode = new Stack<>();

    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinvalidation);

        cl_Loading = findViewById(R.id.cl_Loading);

        tv_CodeInput1 = findViewById(R.id.tv_CodeInput1);
        tv_CodeInput2 = findViewById(R.id.tv_CodeInput2);
        tv_CodeInput3 = findViewById(R.id.tv_CodeInput3);
        tv_CodeInput4 = findViewById(R.id.tv_CodeInput4);
        tv_CodeInput5 = findViewById(R.id.tv_CodeInput5);
        tv_CodeInput6 = findViewById(R.id.tv_CodeInput6);

        tv_Keypad1 = findViewById(R.id.tv_Keypad1);
        tv_Keypad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.push("1");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad2 = findViewById(R.id.tv_Keypad2);
        tv_Keypad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.push("2");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad3 = findViewById(R.id.tv_Keypad3);
        tv_Keypad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.push("3");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad4 = findViewById(R.id.tv_Keypad4);
        tv_Keypad4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.push("4");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad5 = findViewById(R.id.tv_Keypad5);
        tv_Keypad5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.push("5");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad6 = findViewById(R.id.tv_Keypad6);
        tv_Keypad6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.push("6");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad7 = findViewById(R.id.tv_Keypad7);
        tv_Keypad7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.push("7");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad8 = findViewById(R.id.tv_Keypad8);
        tv_Keypad8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.push("8");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad9 = findViewById(R.id.tv_Keypad9);
        tv_Keypad9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.push("9");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad11 = findViewById(R.id.tv_Keypad11);
        tv_Keypad11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.push("0");
                    refreshDisplay();
                }
            }
        });

        // Clear
        tv_Keypad10 = findViewById(R.id.tv_Keypad10);
        tv_Keypad10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    PINCode.clear();
                    refreshDisplay();
                }
            }
        });
        // Backspace
        tv_Keypad12 = findViewById(R.id.tv_Keypad12);
        tv_Keypad12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType && !PINCode.isEmpty()) {  // Check if stack is not empty
                    PINCode.pop();
                    refreshDisplay();
                }
            }
        });
    }

    private void refreshDisplay(){
        if (!PINCode.isEmpty()) {
            for (int i = 0; i < 6; i++) {
                if (i < PINCode.size()) {
                    switch (i) {
                        case 0:
                            tv_CodeInput1.setText("*");
                            break;
                        case 1:
                            tv_CodeInput2.setText("*");
                            break;
                        case 2:
                            tv_CodeInput3.setText("*");
                            break;
                        case 3:
                            tv_CodeInput4.setText("*");
                            break;
                        case 4:
                            tv_CodeInput5.setText("*");
                            break;
                        case 5:
                            tv_CodeInput6.setText("*");
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (i) {
                        case 0:
                            tv_CodeInput1.setText("");
                            break;
                        case 1:
                            tv_CodeInput2.setText("");
                            break;
                        case 2:
                            tv_CodeInput3.setText("");
                            break;
                        case 3:
                            tv_CodeInput4.setText("");
                            break;
                        case 4:
                            tv_CodeInput5.setText("");
                            break;
                        case 5:
                            tv_CodeInput6.setText("");
                            break;
                        default:
                            break;
                    }
                }
            }

            if (PINCode.size() == 6) {
                canType = false;
                cl_Loading.setVisibility(View.VISIBLE);
            }
        } else {
            tv_CodeInput1.setText("");
            tv_CodeInput2.setText("");
            tv_CodeInput3.setText("");
            tv_CodeInput4.setText("");
            tv_CodeInput5.setText("");
            tv_CodeInput6.setText("");
        }

        if (PINCode.size() == 6){
            canType = false;
            cl_Loading.setVisibility(View.VISIBLE);

            // HEADER
            RequestCustomHeaders customHeaders = new RequestCustomHeaders();
            customHeaders.addHeader("Content-Type", "application/json");
            customHeaders.addHeader("Authorization", Optional.ofNullable(Session.getAuthorization(PINValidationActivity.this)).orElse(""));
            customHeaders.addHeader("AccountAddress", Optional.ofNullable(Session.getAccountAddress(PINValidationActivity.this)).orElse(""));
            customHeaders.addHeader("ClientVersion", "1.0");
            customHeaders.addHeader("Intent", "PINValidation");

            // BODY
            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("PIN", PINCode.get(0).toString()+PINCode.get(1).toString()+PINCode.get(2).toString()+PINCode.get(3).toString()+PINCode.get(4).toString()+PINCode.get(5).toString());
                jsonParams.put("IpAddress", Session.getIpAddress(PINValidationActivity.this));
                jsonParams.put("Device", Helpers.getDevice());
                jsonParams.put("Location", Session.getLocation(PINValidationActivity.this));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    jsonParams.toString()
            );

            Callback OTPValidationCallback = new Callback() {
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

                            Session.setAccountAddress(PINValidationActivity.this, Optional.ofNullable(parametersResult.optString("AccountAddress")).orElse(""));
                            Session.setAuthorization(PINValidationActivity.this, Optional.ofNullable(parametersResult.optString("AuthorizationToken")).orElse(""));

                            Log.d("TARGET RESULT", targetResult);
                            Session.setActorCategory(PINValidationActivity.this,targetResult);
                            Helpers.responseIntentController(PINValidationActivity.this,targetResult);
                            Helpers.responseMessageController(PINValidationActivity.this,responseResult);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("OkHttpRequest", "JSON Parsing Error");
                    }
                }
            };

            requestHelper.makeRequest(PINValidationActivity.this,Defaults.AuthEndPoint, customHeaders, requestBody, OTPValidationCallback);
        }
    }
}