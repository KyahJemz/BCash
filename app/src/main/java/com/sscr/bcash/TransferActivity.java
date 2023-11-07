package com.sscr.bcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TransferActivity extends AppCompatActivity {

    EditText et_ReceiverId, et_Amount, et_Message;
    TextView tv_TransferBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        // Back Button
        findViewById(R.id.iv_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        et_ReceiverId = findViewById(R.id.et_ReceiverId);
        et_Amount = findViewById(R.id.et_Amount);
        et_Message = findViewById(R.id.et_Message);

        tv_TransferBtn = findViewById(R.id.tv_TransferBtn);
        tv_TransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean next = true;
                if (et_ReceiverId.getText().toString().equals("")){
                    Toast.makeText(TransferActivity.this, "Receiver Id cannot be blank.", Toast.LENGTH_SHORT).show();
                    next = false;
                }
                if (et_Amount.getText().toString().equals("")){
                    Toast.makeText(TransferActivity.this, "Amount cannot be blank.", Toast.LENGTH_SHORT).show();
                    next = false;
                } else {
                    try {
                        double amount = Double.parseDouble(et_Amount.getText().toString());
                        if (amount < Defaults.MinimumTransfer) {
                            Toast.makeText(TransferActivity.this, "The minimum amount to transfer is "+Defaults.MinimumTransfer, Toast.LENGTH_SHORT).show();
                            next = false;
                        }
                        if (amount > Defaults.MaximumTransfer) {
                            Toast.makeText(TransferActivity.this, "The maximum amount to transfer is "+Defaults.MaximumTransfer, Toast.LENGTH_SHORT).show();
                            next = false;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(TransferActivity.this, "Amount is not a number.", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                }
                if (next) {
                    Intent intent = new Intent(TransferActivity.this, TransferConfirmationActivity.class);
                    intent.putExtra("ReceiverId", et_ReceiverId.getText().toString());
                    intent.putExtra("Amount", et_Amount.getText().toString());
                    intent.putExtra("Message", et_Message.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}