package com.example.bcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class qrscanner extends AppCompatActivity implements View.OnClickListener {

    Button btn_qrscanner;
    String receiverId, amount,summary;
    Bundle extras = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        btn_qrscanner = findViewById(R.id.btn_qrscan);

        btn_qrscanner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                transferConfrimation(intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void transferConfrimation(String data){
        if (!(data.contains("summary"))) {
            Toast.makeText(this, "Invalid Scan", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("report", data.toString());
        data = data.replace("[", "");
        data = data.replace("]", "");
        data = data.replace("{", "");
        data = data.replace("}", "");
        String[] newData = data.split(",");

        for (int i = 0; i < 4; i++) {
            String[] temp = newData[i].split(":");
            String temp2 = temp[1].replace("\"", "");
            String temp1 = temp[0].replace("\"", "");
            extras.putString(temp1,temp2);
            Log.d("report", String.valueOf(i));
            Log.d("report", temp1+" || "+temp2);;
        }
        extras.putString("message", "");
        finish();
        Intent intent = new Intent(qrscanner.this, transfer_confirmation.class);
        intent.putExtras(extras);
        startActivity(intent);
    }
}