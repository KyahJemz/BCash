package com.example.bcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class transfer_form extends AppCompatActivity {

    private Session session;
    private bcash_api bcashapi;
    private RequestQueue queue;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    EditText receiverId, amount, message;
    Button btnSendMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_form);
        session = new Session(transfer_form.this);
        bcashapi = new bcash_api();
        queue = Volley.newRequestQueue(transfer_form.this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        receiverId = findViewById(R.id.et_ReceiverId);
        amount = findViewById(R.id.et_Amount);
        message = findViewById(R.id.et_Message);
        btnSendMoney = findViewById(R.id.btn_SendMoney);

        googleVerification();

        btnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putString("type", "BCash Transfer");
                extras.putString("receiverId", receiverId.getText().toString());
                extras.putString("amount", amount.getText().toString());
                extras.putString("message", message.getText().toString());
                extras.putString("summary", "");

                Intent intent = new Intent(transfer_form.this, transfer_confirmation.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    void googleVerification(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String email = acct.getEmail();
            if (!(email.contains("@sscr.edu"))) {
                Toast.makeText(this, "Please use a valid SSCR account.", Toast.LENGTH_LONG).show();
                signOut();
            }
        }
    }

    void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                Intent intent = new Intent(transfer_form.this, login.class);
                startActivity(intent);
            }
        });
    }

    public void back(View view){
        finish();
        Intent intent = new Intent(transfer_form.this, dashboard.class);
        startActivity(intent);
    }
}