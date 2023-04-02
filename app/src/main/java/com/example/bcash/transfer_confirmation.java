package com.example.bcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class transfer_confirmation extends AppCompatActivity {
    private Session session;
    private bcash_api bcashapi;
    private RequestQueue queue;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    String type, receiverId, amount, message, summary;

    TextView tvType, tvReceiverId, tvAmount, tvMessage, TvSummary;
    Button btnConfirmTransaction;
    Bundle extras;

    ConstraintLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_confirmation);
        session = new Session(transfer_confirmation.this);
        bcashapi = new bcash_api();
        queue = Volley.newRequestQueue(transfer_confirmation.this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleVerification();

        extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type");
            receiverId = extras.getString("receiverId");
            amount = extras.getString("amount");
            message = extras.getString("message");
            summary = extras.getString("summary");
        } else {
            finish();
            Intent intent = new Intent(transfer_confirmation.this, dashboard.class);
            startActivity(intent);
        }

        tvType = findViewById(R.id.tv_type);
        tvReceiverId  = findViewById(R.id.tv_receiverId);
        tvAmount  = findViewById(R.id.tv_amount);
        tvMessage = findViewById(R.id.tv_message);
        TvSummary = findViewById(R.id.tv_summary);;
        loading = findViewById(R.id.sv_loading);

        tvType.setText("Transaction Type : "+type);
        tvReceiverId.setText("Receiver Id : "+receiverId);
        tvAmount.setText("Amount : "+amount);
        tvMessage.setText("Message : "+message);
        TvSummary.setText("Summary : "+summary);

        btnConfirmTransaction = findViewById(R.id.btn_confirmTransaction);
        btnConfirmTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                sendmoney();
            }
        });
    }

    void sendmoney(){
        String urlParam = "/transaction.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bcashapi.url+urlParam,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        Toast.makeText(transfer_confirmation.this, response, Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(transfer_confirmation.this, dashboard.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        loading.setVisibility(View.INVISIBLE);
                        Toast.makeText(transfer_confirmation.this, "Network Connection Failed, Try Again", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("bcash-bcash-transaction-data", "");
                params.put("type", type);
                params.put("senderId", session.getEmail());
                params.put("receiverId", receiverId);
                params.put("amount", amount);
                params.put("message", message);
                return params;
            }
        };
        queue.add(stringRequest);
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
                Intent intent = new Intent(transfer_confirmation.this, login.class);
                startActivity(intent);
            }
        });
    }

    public void back(View view){
        finish();
        Intent intent = new Intent(transfer_confirmation.this, transfer_form.class);
        startActivity(intent);
    }
}