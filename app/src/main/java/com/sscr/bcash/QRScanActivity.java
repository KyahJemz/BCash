package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QRScanActivity extends AppCompatActivity {
    private GmsBarcodeScanner scanner;
    OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();
    TextView tv_StartScanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        // Back Button
        findViewById(R.id.iv_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC)
                .enableAutoZoom()
                .build();

        scanner = GmsBarcodeScanning.getClient(this);

        tv_StartScanBtn = findViewById(R.id.tv_StartScanBtn);
        tv_StartScanBtn.setOnClickListener(v -> startBarcodeScanning());
    }

    private void startBarcodeScanning() {
        scanner.startScan()
                .addOnSuccessListener(barcode -> {
                    String rawValue = barcode.getRawValue();
                    String MerchantAddress = rawValue;
                    Toast.makeText(this, MerchantAddress, Toast.LENGTH_SHORT).show();

                    // HEADER
                    RequestCustomHeaders customHeaders = new RequestCustomHeaders();
                    customHeaders.addHeader("Content-Type", "application/json");
                    customHeaders.addHeader("Authorization", Session.getAuthorization(QRScanActivity.this));
                    customHeaders.addHeader("AccountAddress", Session.getAccountAddress(QRScanActivity.this));
                    customHeaders.addHeader("ClientVersion", "1.0");
                    customHeaders.addHeader("IpAddress", Session.getIpAddress(QRScanActivity.this));
                    customHeaders.addHeader("Device", Helpers.getDevice());
                    customHeaders.addHeader("Location", Session.getLocation(QRScanActivity.this));
                    customHeaders.addHeader("Intent", "QRscan for purchase");

                    // BODY
                    JSONObject jsonParams = new JSONObject();
                    try {
                        jsonParams.put("MerchantAddress", MerchantAddress);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestBody requestBody = RequestBody.create(
                            MediaType.parse("application/json; charset=utf-8"),
                            jsonParams.toString()
                    );

                    Callback QRScanCallback = new Callback() {
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

                                    Helpers.responseIntentController(QRScanActivity.this,targetResult);
                                    Helpers.responseMessageController(QRScanActivity.this,responseResult);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("OkHttpRequest", "JSON Parsing Error");
                            }
                        }
                    };

                    requestHelper.makeRequest(QRScanActivity.this,Defaults.RequestEndPoint, customHeaders, requestBody, QRScanCallback);

                })
                .addOnCanceledListener(() -> {
                    Toast.makeText(this, "Scan QR Canceled!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Scan QR Failed!", Toast.LENGTH_SHORT).show();
                });
    }
}