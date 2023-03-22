package com.example.bcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class transaction_details extends AppCompatActivity {
    private Session session;
    private bcash_api bcashapi;
    private RequestQueue queue;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    TextView id, type,date,sender,receiver,amount,message,summary;

    Map<String,String> stringArrayList = new HashMap<String, String>();


    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        session = new Session(transaction_details.this);
        bcashapi = new bcash_api();
        queue = Volley.newRequestQueue(transaction_details.this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        findViewById(R.id.sv_loading).setVisibility(View.VISIBLE);

        googleVerification();

        id = findViewById(R.id.TransactionId);
        type = findViewById(R.id.TransactionType);
        date = findViewById(R.id.TransactionDate);
        sender = findViewById(R.id.SenderName);
        receiver = findViewById(R.id.ReceiverName);
        amount = findViewById(R.id.TransactionAmount);
        message = findViewById(R.id.TransactionMessage);
        summary = findViewById(R.id.TransationSummary);

        bundle = getIntent().getExtras();
        getDataFromApi(bundle.get("transactionId").toString());
    }

    void getDataFromApi(String id){
        String urlParam = "/transaction.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bcashapi.url+urlParam,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        convertData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(transaction_details.this, "Network Connection Failed, Try Again", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(transaction_details.this, transactions.class);
                        startActivity(intent);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("transaction-details", "");
                params.put("transactionId", id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    void convertData(String data){
        data = data.replace("{", "");
        data = data.replace("}", "");
        String[] newData = data.split(",");

        for (int j = 0; j<newData.length; j++) {
            String[] temp = newData[j].split(":");
            Log.d("array", temp[0]);
            stringArrayList.put(temp[0].replace("\"", ""),temp[1].replace("\"", ""));
        }

        setData();
    }

    void setData(){
        id.setText(stringArrayList.get("transaction_id"));
        type.setText(stringArrayList.get("transaction_type"));
        date.setText(stringArrayList.get("transaction_timestamp"));
        sender.setText(stringArrayList.get("transaction_sender_id"));
        receiver.setText(stringArrayList.get("transaction_receiver_id"));
        amount.setText(stringArrayList.get("transaction_amount"));
        message.setText(stringArrayList.get("transaction_message"));
        summary.setText(stringArrayList.get("transaction_summary"));
        findViewById(R.id.sv_loading).setVisibility(View.INVISIBLE);
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
                Intent intent = new Intent(transaction_details.this, login.class);
                startActivity(intent);
            }
        });
    }

    public void back(View view){
        finish();
        Intent intent = new Intent(transaction_details.this, transactions.class);
        startActivity(intent);
    }
}