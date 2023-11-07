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

public class OTPValidationActivity extends AppCompatActivity {

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

    Stack<String> OTPCode = new Stack<>();

    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvalidation);

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
                    OTPCode.push("1");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad2 = findViewById(R.id.tv_Keypad2);
        tv_Keypad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    OTPCode.push("2");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad3 = findViewById(R.id.tv_Keypad3);
        tv_Keypad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    OTPCode.push("3");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad4 = findViewById(R.id.tv_Keypad4);
        tv_Keypad4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    OTPCode.push("4");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad5 = findViewById(R.id.tv_Keypad5);
        tv_Keypad5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    OTPCode.push("5");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad6 = findViewById(R.id.tv_Keypad6);
        tv_Keypad6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    OTPCode.push("6");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad7 = findViewById(R.id.tv_Keypad7);
        tv_Keypad7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    OTPCode.push("7");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad8 = findViewById(R.id.tv_Keypad8);
        tv_Keypad8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    OTPCode.push("8");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad9 = findViewById(R.id.tv_Keypad9);
        tv_Keypad9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    OTPCode.push("9");
                    refreshDisplay();
                }
            }
        });

        tv_Keypad11 = findViewById(R.id.tv_Keypad11);
        tv_Keypad11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType) {
                    OTPCode.push("0");
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
                    OTPCode.clear();
                    refreshDisplay();
                }
            }
        });
        // Backspace
        tv_Keypad12 = findViewById(R.id.tv_Keypad12);
        tv_Keypad12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canType && !OTPCode.isEmpty()) {
                    OTPCode.pop();
                    refreshDisplay();
                }
            }
        });
    }

    private void refreshDisplay(){
        if (!OTPCode.isEmpty()) {
            for (int i = 0; i < 6; i++) {
                if (i < OTPCode.size()) {
                    switch (i) {
                        case 0:
                            tv_CodeInput1.setText(OTPCode.get(0));
                            break;
                        case 1:
                            tv_CodeInput2.setText(OTPCode.get(1));
                            break;
                        case 2:
                            tv_CodeInput3.setText(OTPCode.get(2));
                            break;
                        case 3:
                            tv_CodeInput4.setText(OTPCode.get(3));
                            break;
                        case 4:
                            tv_CodeInput5.setText(OTPCode.get(4));
                            break;
                        case 5:
                            tv_CodeInput6.setText(OTPCode.get(5));
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
        } else {
            tv_CodeInput1.setText("");
            tv_CodeInput2.setText("");
            tv_CodeInput3.setText("");
            tv_CodeInput4.setText("");
            tv_CodeInput5.setText("");
            tv_CodeInput6.setText("");
        }

        if (OTPCode.size() == 6){
            canType = false;
            cl_Loading.setVisibility(View.VISIBLE);

            // HEADER
            RequestCustomHeaders customHeaders = new RequestCustomHeaders();
            customHeaders.addHeader("Content-Type", "application/json");
            customHeaders.addHeader("Authorization", Optional.ofNullable(Session.getAuthorization(OTPValidationActivity.this)).orElse(""));
            customHeaders.addHeader("AccountAddress", Optional.ofNullable(Session.getAccountAddress(OTPValidationActivity.this)).orElse(""));
            customHeaders.addHeader("ClientVersion", "1.0");
            customHeaders.addHeader("Intent", "OTPValidation");

            // BODY
            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("OTP", OTPCode.get(0).toString()+OTPCode.get(1).toString()+OTPCode.get(2).toString()+OTPCode.get(3).toString()+OTPCode.get(4).toString()+OTPCode.get(5).toString());
                jsonParams.put("IpAddress", Session.getIpAddress(OTPValidationActivity.this));
                jsonParams.put("Device", Helpers.getDevice());
                jsonParams.put("Location", Session.getLocation(OTPValidationActivity.this));
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
                            String parametersResult = jsonResponse.optString("Parameters");
                            String responseResult = jsonResponse.optString("Response");

                            Helpers.responseIntentController(OTPValidationActivity.this,targetResult);
                            Helpers.responseMessageController(OTPValidationActivity.this,responseResult);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("OkHttpRequest", "JSON Parsing Error");
                    }
                }
            };

            requestHelper.makeRequest(OTPValidationActivity.this,Defaults.AuthEndPoint, customHeaders, requestBody, OTPValidationCallback);
        }
    }
}